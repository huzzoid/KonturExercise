package com.huzzoid.konturexercise.ui.contacts

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.huzzoid.konturexercise.R
import com.huzzoid.konturexercise.databinding.FragmentContactsBinding
import com.huzzoid.konturexercise.domain.base.Loading
import com.huzzoid.konturexercise.domain.contacts.ContactsEvent
import com.huzzoid.konturexercise.domain.contacts.ContactsEvent.FullError
import com.huzzoid.konturexercise.domain.contacts.ContactsEvent.PartialError
import com.huzzoid.konturexercise.domain.contacts.ContactsState
import com.huzzoid.konturexercise.ui.widget.DebouncingQueryTextListener
import com.huzzoid.konturexercise.ui.widget.snack
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ContactsFragment : Fragment() {

    companion object {
        fun newInstance() = ContactsFragment()
    }

    private val viewModel: ContactsViewModel by viewModels()

    private var _binding: FragmentContactsBinding? = null
    private val binding get() = _binding!!
    private var contactsAdapter: ContactsAdapter? = null
    private val queryChangeListener = DebouncingQueryTextListener(this.lifecycle) {
        viewModel.onSearchQueryChanged(it)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentContactsBinding.inflate(inflater, container, false)
        .run {
            _binding = this
            root
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.swipeToRefreshContainer.setOnRefreshListener {
            viewModel.onSwipeToRefresh()
        }
        val linearLayoutManager = LinearLayoutManager(requireContext())
        with(binding.contacts) {
            layoutManager = linearLayoutManager
            adapter = ContactsAdapter { viewModel.goToDetails(it) }.also { contactsAdapter = it }
            addItemDecoration(
                DividerItemDecoration(
                    requireContext(),
                    DividerItemDecoration.VERTICAL
                ).also {
                    it.setDrawable(
                        ContextCompat.getDrawable(
                            requireContext(),
                            R.drawable.item_decorator
                        )!!
                    )
                }
            )
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    if (dy > 0) {
                        viewModel.onItemIsBeingVisible(linearLayoutManager.findLastCompletelyVisibleItemPosition())
                    }
                }
            })
        }
        binding.search.setOnQueryTextListener(queryChangeListener)
        viewModel.state.observe(viewLifecycleOwner, { render(it) })
        viewModel.events.observe(viewLifecycleOwner, { handle(it) })
    }

    private fun handle(event: ContactsEvent) {
        return when (event) {
            is PartialError -> binding.swipeToRefreshContainer.snack(
                getString(R.string.unexpected_error)
            )
            is FullError -> binding.swipeToRefreshContainer.snack(
                event.error.message ?: getString(R.string.unexpected_error)
            )
        }
    }

    private fun render(state: ContactsState) {
        Log.d("$this ", state.toString())
        binding.fullScreenLoading.isVisible = state.loading == Loading.FULL
        binding.swipeToRefreshContainer.isEnabled = state.loading != Loading.FULL
        binding.swipeToRefreshContainer.isRefreshing = state.loading == Loading.S2R
        contactsAdapter?.submitList(state.filteredOutList)
    }

    override fun onDestroyView() {
        contactsAdapter = null
        _binding = null
        super.onDestroyView()
    }
}
