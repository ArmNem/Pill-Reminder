package com.example.pillreminder.GUI.Fragments.reminders

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.pillreminder.GUI.Fragments.pills.AddEditPillViewModel
import com.example.pillreminder.R
import com.example.pillreminder.databinding.FragmentAddEditPillBinding
import com.example.pillreminder.databinding.FragmentNewReminderBinding
import com.example.pillreminder.util.exhaustive
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect


@AndroidEntryPoint
class AddEditReminderFragment : Fragment(R.layout.fragment_new_reminder) {
    private val viewModel: AddEditReminderViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val binding = FragmentNewReminderBinding.bind(view)

        binding.apply {
            edittextReminderName.setText(viewModel.pill.name)
            edittextReminderDose.setText(viewModel.pilldose)
            edittextReminderTime.setText(viewModel.alarmtime as String)
            checkboxEveryday.isChecked=viewModel.iseveryday
            if(viewModel.iseveryday == false)
            {checkboxToday.isChecked= true}

            edittextReminderName.addTextChangedListener {
                viewModel.pill.name = it.toString()
            }
            edittextReminderDose.addTextChangedListener {
                viewModel.pilldose = it.toString()
            }
            edittextReminderTime.addTextChangedListener {
                viewModel.alarmtime = it as Long
            }
            checkboxEveryday.setOnCheckedChangeListener { _, isChecked ->
                viewModel.iseveryday = isChecked
            }
            checkboxToday.setOnCheckedChangeListener { _, isChecked ->
               viewModel.iseveryday = isChecked
            }
            fabSaveReminder.setOnClickListener {
                viewModel.onSaveClick()
            }

        }
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.addEditRemiderEvent.collect { event ->
                when (event) {
                    is AddEditReminderViewModel.AddEditReminderEvent.ShowInvalidInputMessage -> {
                        Snackbar.make(requireView(), event.msg, Snackbar.LENGTH_LONG).show()
                    }
                    is AddEditReminderViewModel.AddEditReminderEvent.NavigateBackWithResult -> {
                        binding.edittextReminderName.clearFocus()
                        binding.edittextReminderDose.clearFocus()
                        binding.edittextReminderTime.clearFocus()
                        binding.checkboxEveryday.clearFocus()
                        binding.checkboxToday.clearFocus()
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