package com.example.pillreminder.GUI.Fragments.reminders

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.pillreminder.GUI.Fragments.pills.PillsAdapter
import com.example.pillreminder.data.BEPill
import com.example.pillreminder.data.BEReminder
import com.example.pillreminder.databinding.ItemPillsBinding
import com.example.pillreminder.databinding.ItemRemindersBinding

class RemindersAdapter(private val listener:OnItemClickListener):
    ListAdapter<BEReminder, RemindersAdapter.RemindersViewHolder>(DiffCallBack()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RemindersViewHolder {
        val binding = ItemRemindersBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RemindersViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RemindersViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(currentItem)
    }

    inner class RemindersViewHolder(private val binding: ItemRemindersBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.apply {
                root.setOnClickListener {
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        val reminder = getItem(position)
                        listener.onItemClick(reminder)
                    }
                }
            }
        }

        fun bind(reminder: BEReminder) {
            binding.apply {
                textviewRemindertime.text = reminder.alarmTime.toString()
                textviewReminderPillDose.text = reminder.pilldose
                switchReminder.isChecked = reminder.isActive

            }
        }
    }
    interface OnItemClickListener {

        fun onItemClick(reminder: BEReminder)

    }
    class DiffCallBack : DiffUtil.ItemCallback<BEReminder>() {
        override fun areItemsTheSame(oldItem: BEReminder, newItem: BEReminder) =
            oldItem.reminderId == newItem.reminderId

        override fun areContentsTheSame(oldItem: BEReminder, newItem: BEReminder) =
            oldItem == newItem
    }
}