package com.example.pillreminder.GUI.Fragments.pills

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.annotation.CallSuper
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
import kotlinx.android.synthetic.main.fragment_add_edit_pill.*
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class AddEditPillFragment : Fragment(R.layout.fragment_add_edit_pill) {
    private val viewModel: AddEditPillViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val binding = FragmentAddEditPillBinding.bind(view)
        binding.apply {
            spinner()
            editTextPillname.setText(viewModel.pillName)
            editTextPilldose.setText(viewModel.pillDose)
            editTextPilltype.setText(viewModel.pillType)
            editTextPilldescript.setText(viewModel.pillDescription)
            when(viewModel.pillDaytime)
            {
                "Morning" -> spinnerDaytime.setSelection(0)
                "Afternoon" -> spinnerDaytime.setSelection(1)
                "Evening" -> spinnerDaytime.setSelection(2)
            }
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
                        binding.spinnerDaytime.clearFocus()
                        setFragmentResult(
                            "add_edit_request",
                            bundleOf("add_edit_result" to event.result)
                        )
                        findNavController().popBackStack()
                    }
                    else -> true
                }.exhaustive
            }
        }
    }

    fun spinner() {
        context?.let {
            ArrayAdapter.createFromResource(
                it,
                R.array.pill_daytime_array,
                android.R.layout.simple_spinner_item
            ).also { adapter ->
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spinner_daytime.adapter = adapter

                spinner_daytime.onItemSelectedListener =
                    object : AdapterView.OnItemSelectedListener {
                        override fun onNothingSelected(parent: AdapterView<*>?) {}
                        override fun onItemSelected(
                            parent: AdapterView<*>?,
                            view: View?,
                            position: Int,
                            id: Long
                        ) {
                            when(position){
                                0 ->{
                                     viewModel.pillDaytime = "Morning"
                                }
                                1 -> {
                                    viewModel.pillDaytime = "Afternoon"
                                }
                                2 ->{
                                    viewModel.pillDaytime = "Evening"
                                }
                            }
                        }

                    }
            }
        }
    }

    @CallSuper
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
    }
}