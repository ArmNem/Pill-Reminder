package com.example.pillreminder.GUI.Fragments.pills

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.pillreminder.R
import com.example.pillreminder.databinding.FragmentAddEditPillBinding
import com.example.pillreminder.util.exhaustive
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class AddEditPillFragment : Fragment(R.layout.fragment_add_edit_pill) {
    private val viewModel: AddEditPillViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val binding = FragmentAddEditPillBinding.bind(view)

        binding.apply {
            editTextPillname.setText(viewModel.pillName)
            editTextPilldose.setText(viewModel.pillDose)
            editTextPilltype.setText(viewModel.pillType)
            editTextPilldescript.setText(viewModel.pillDescription)

            editTextPillname.addTextChangedListener {
                viewModel.pillName = it.toString()
            }
            editTextPilldose.addTextChangedListener {
                viewModel.pillDose = it.toString()
            }
            editTextPilltype.addTextChangedListener {
                viewModel.pillType = it.toString()
            }
            editTextPilldescript.addTextChangedListener {
                viewModel.pillDescription = it.toString()
            }
            fabSavePill.setOnClickListener {
                viewModel.onSaveClick()
            }
        }
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.addEditPillEvent.collect { event ->
                when (event) {
                    is AddEditPillViewModel.AddEditPillEvent.ShowInvalidInputMessage -> {
                        Snackbar.make(requireView(), event.msg, Snackbar.LENGTH_LONG).show()
                    }
                    is AddEditPillViewModel.AddEditPillEvent.NavigateBackWithResult -> {
                        binding.editTextPillname.clearFocus()
                        binding.editTextPilldose.clearFocus()
                        binding.editTextPilltype.clearFocus()
                        binding.editTextPilldescript.clearFocus()
                        setFragmentResult(
                            "add_edit_request",
                            bundleOf("add_edit_result" to event.result)
                        )
                        findNavController().popBackStack()
                    }
                }.exhaustive
            }
        }
    }
}