package com.example.pillreminder.GUI.Fragments.reminders

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pillreminder.GUI.Fragments.pills.PillsAdapter
import com.example.pillreminder.GUI.Fragments.pills.PillsViewModel
import com.example.pillreminder.R
import com.example.pillreminder.data.BEReminder
import com.example.pillreminder.databinding.FragmentMyRemindersBinding
import com.example.pillreminder.databinding.FragmentPillsBinding
import dagger.hilt.android.AndroidEntryPoint

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
                    val pill = remindersAdapter.currentList[viewHolder.adapterPosition]
                    viewModel.onPillSwiped(pill)
                }
            }).attachToRecyclerView(recyclerViewReminders)
            fabAddReminder.setOnClickListener {
                viewModel.onAddNewPillClick()
            }
        }
    }

    override fun onItemClick(reminder: BEReminder) {
        TODO("Not yet implemented")
    }

}