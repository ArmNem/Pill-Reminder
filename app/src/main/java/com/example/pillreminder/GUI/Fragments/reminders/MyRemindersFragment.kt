package com.example.pillreminder.GUI.Fragments.reminders

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.CallSuper
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pillreminder.GUI.Fragments.pills.MyPillsFragmentDirections
import com.example.pillreminder.GUI.Fragments.pills.PillsAdapter
import com.example.pillreminder.GUI.Fragments.pills.PillsViewModel
import com.example.pillreminder.R
import com.example.pillreminder.data.BEReminder
import com.example.pillreminder.databinding.FragmentMyRemindersBinding
import com.example.pillreminder.databinding.FragmentPillsBinding
import com.example.pillreminder.util.exhaustive
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.item_reminders.*
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class MyRemindersFragment : Fragment(R.layout.fragment_my_reminders),
    RemindersAdapter.OnItemClickListener {
    private val viewModel: RemindersViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentMyRemindersBinding.bind(view)
        val remindersAdapter = RemindersAdapter(this)

        binding.apply {
            recyclerViewReminders.apply {
                adapter = remindersAdapter
                layoutManager = LinearLayoutManager(requireContext())
                setHasFixedSize(true)
            }
            ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(
                0,
                ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
            ) {
                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean {
                    return false
                }

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    val reminder = remindersAdapter.currentList[viewHolder.adapterPosition]
                    viewModel.onReminderSwiped(reminder)
                }
            }).attachToRecyclerView(recyclerViewReminders)
            fabAddReminder.setOnClickListener {
                viewModel.onAddNewReminderClick()
            }
           // switch_reminder.setOnClickListener { viewModel.activateReminder() }
        }
        setFragmentResultListener("add_edit_request") { _, bundle ->
            val result = bundle.getInt("add_edit_result")
            viewModel.onAddEditResult(result)

        }
        viewModel.reminders.observe(viewLifecycleOwner) {
            remindersAdapter.submitList(it)
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.reminderEvent.collect { event ->
                when (event) {
                    is RemindersViewModel.ReminderEvent.ShowUndoDeleteReminderMessage -> {
                        Snackbar.make(requireView(), "Reminder deleted", Snackbar.LENGTH_LONG)
                            .setAction("UNDO") {
                                viewModel.onUndoDeleteClick(event.reminder)
                            }.show()
                    }
                    is RemindersViewModel.ReminderEvent.NavigateToAddReminderScreen -> {
                        val action =
                            MyRemindersFragmentDirections.actionMyRemindersFragmentToAddEditReminderFragment(
                                null,
                                "New reminder"
                            )
                        if(findNavController().currentDestination?.id==R.id.addEditReminderFragment){
                            findNavController().navigate(action)
                        }else {
                            Log.d("NAV", "Current destination is: " + findNavController().currentDestination)
                        }

                    }
                    is RemindersViewModel.ReminderEvent.ActivateReminder -> {
                        Toast.makeText(requireContext(), "Reminder activated", Toast.LENGTH_LONG).show()

                    }
                    is RemindersViewModel.ReminderEvent.NavigateToEditReminderScreen -> {
                        val action =
                            MyRemindersFragmentDirections.actionMyRemindersFragmentToAddEditReminderFragment(
                                event.reminder,
                                "Edit reminder"
                            )
                        if(findNavController().currentDestination?.id==R.id.addEditReminderFragment){
                            findNavController().navigate(action)
                        }else {
                            Log.d("NAV", "Current destination is: " + findNavController().currentDestination)
                        }
                    }
                    is RemindersViewModel.ReminderEvent.showReminderSavedConfirmationMessage -> {
                        Snackbar.make(requireView(), event.msg, Snackbar.LENGTH_SHORT).show()
                    }
                    is RemindersViewModel.ReminderEvent.ActivateReminder -> {
                        Snackbar.make(requireView(), "Reminder activated", Snackbar.LENGTH_LONG).show()
                    }
                    else -> true
                }.exhaustive
            }
        }
        setHasOptionsMenu(true)
    }

    override fun onItemClick(reminder: BEReminder) {
        viewModel.onReminderSelected(reminder)
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    @CallSuper
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
    }
}