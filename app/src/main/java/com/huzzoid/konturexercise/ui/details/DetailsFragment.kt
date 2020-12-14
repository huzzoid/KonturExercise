package com.huzzoid.konturexercise.ui.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.huzzoid.konturexercise.R
import com.huzzoid.konturexercise.databinding.FragmentDetailsBinding
import com.huzzoid.konturexercise.domain.base.Loading
import com.huzzoid.konturexercise.domain.details.DetailsEvent
import com.huzzoid.konturexercise.domain.details.DetailsState
import com.huzzoid.konturexercise.ui.widget.snack
import dagger.hilt.android.AndroidEntryPoint
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

internal const val ARGS = "args"

@AndroidEntryPoint
class DetailsFragment : Fragment() {

    companion object {
        fun newInstance(id: String) = DetailsFragment().also {
            it.arguments = Bundle().apply { putParcelable(ARGS, DetailsState(id).toParcelable()) }
        }
    }

    private val viewModel: DetailsViewModel by viewModels()

    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!
    private lateinit var state: DetailsState

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        state = if (savedInstanceState == null) {
            arguments?.getParcelable<DetailsParcelable>(ARGS)!!.toState()
        } else {
            savedInstanceState.getParcelable<DetailsParcelable>(ARGS)!!.toState()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentDetailsBinding.inflate(inflater, container, false)
        .run {
            _binding = this
            root
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.toolbar.setNavigationOnClickListener { viewModel.back() }
        binding.phone.setOnClickListener {
            state.contact?.let {
                viewModel.onPhoneClick(it.phone)
            }
        }
        viewModel.state.observe(viewLifecycleOwner, { render(it) })
        viewModel.events.observe(viewLifecycleOwner, { handle(it) })
    }

    private fun handle(event: DetailsEvent) = when (event) {
        is DetailsEvent.Error -> binding.container.snack(
            event.error.message ?: getString(R.string.unexpected_error)
        )
    }

    private fun render(state: DetailsState) {
        this.state = state
        with(binding) {
            progress.isVisible = state.loading == Loading.FULL
            content.isVisible = state.loading == Loading.NONE
            state.contact ?: return
            name.text = state.contact.name
            phone.text = state.contact.phone
            temperament.text = state.contact.temperament.capitalize(Locale.getDefault())
            educationPeriod.text = getString(
                R.string.date_format,
                Formatter.format(state.contact.start),
                Formatter.format(state.contact.end)
            )
            bio.text = state.contact.biography
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        viewModel.savedStateHandle.set(ARGS, state.toParcelable())
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}

private object Formatter {
    private val sourceSdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.ROOT)
    private val targetSdf = SimpleDateFormat("dd.MM.yyyy", Locale.ROOT)

    fun format(sourceString: String) = try {
        sourceSdf.parse(sourceString)?.let { targetSdf.format(it) }
    } catch (e: ParseException) {
        ""
    }
}