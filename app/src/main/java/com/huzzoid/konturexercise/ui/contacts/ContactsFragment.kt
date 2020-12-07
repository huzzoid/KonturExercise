package com.huzzoid.konturexercise.ui.contacts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.huzzoid.konturexercise.R
import com.huzzoid.konturexercise.databinding.FragmentContactsBinding
import com.huzzoid.konturexercise.domain.list.ContactsState
import com.huzzoid.konturexercise.domain.list.Loading
import com.huzzoid.konturexercise.ui.widget.DebouncingQueryTextListener
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
        viewModel.state.observe(viewLifecycleOwner, { render(it) })
        binding.swipeToRefreshContainer.setOnRefreshListener {
            viewModel.onSwipeToRefresh()
        }
        with(binding.contacts) {
            layoutManager = LinearLayoutManager(requireContext())
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
        }
        binding.search.setOnQueryTextListener(queryChangeListener)
    }

    private fun render(state: ContactsState) {
        binding.fullScreenLoading.isVisible = state.loading == Loading.FULL
        binding.swipeToRefreshContainer.isEnabled = state.loading != Loading.FULL
        binding.swipeToRefreshContainer.isRefreshing = state.loading == Loading.S2R
        if (state.error != null) {
            binding.swipeToRefreshContainer.snack(
                state.error.message ?: getString(R.string.unexpected_error)
            )
        } else {
            contactsAdapter?.submitList(state.filteredOutList)
        }
        if (binding.search.query != state.searchQuery) {
            binding.search.setQuery(state.searchQuery, false)
        }
    }

    override fun onDestroyView() {
        contactsAdapter = null
        _binding = null
        super.onDestroyView()
    }
}

fun View.snack(message: String, duration: Int = Snackbar.LENGTH_LONG) {
    Snackbar.make(this, message, duration).show()
}