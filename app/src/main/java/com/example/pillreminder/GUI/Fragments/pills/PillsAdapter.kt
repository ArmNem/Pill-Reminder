package com.example.pillreminder.GUI.Fragments.pills


import android.view.LayoutInflater
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.pillreminder.data.BEPill
import android.view.ViewGroup
import com.example.pillreminder.databinding.ItemPillsBinding


class PillsAdapter(private val listener: OnItemClickListener) :
    ListAdapter<BEPill, PillsAdapter.PillsViewHolder>(DiffCallBack()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PillsViewHolder {
        val binding = ItemPillsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PillsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PillsViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(currentItem)
    }

    inner class PillsViewHolder(private val binding: ItemPillsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.apply {
                root.setOnClickListener {
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        val pill = getItem(position)
                        listener.onItemClick(pill)
                    }
                }
            }
        }

        fun bind(pill: BEPill) {
            binding.apply {
                textviewName.text = pill.name
                textviewDose.text = pill.dose
                textviewType.text = pill.type
                texviewDaytime.text = pill.daytime
            }
        }
    }


    interface OnItemClickListener {
        fun onItemClick(pill: BEPill)
    }

    class DiffCallBack : DiffUtil.ItemCallback<BEPill>() {
        override fun areItemsTheSame(oldItem: BEPill, newItem: BEPill) =
            oldItem.pillId == newItem.pillId

        override fun areContentsTheSame(oldItem: BEPill, newItem: BEPill) =
            oldItem == newItem
    }
}