package com.example.pillreminder.GUI.Fragments.pills

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.observe
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pillreminder.R
import com.example.pillreminder.data.BEPill
import com.example.pillreminder.databinding.FragmentPillsBinding
import com.example.pillreminder.util.exhaustive
import com.example.pillreminder.util.onQueryTextChanged
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MyPillsFragment : Fragment(R.layout.fragment_pills), PillsAdapter.OnItemClickListener {

    private val viewModel: PillsViewModel by viewModels()
    private lateinit var searchView: SearchView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentPillsBinding.bind(view)
        val pillsAdapter = PillsAdapter(this)

        binding.apply {
            recyclerViewPills.apply {
                adapter = pillsAdapter
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
                    val pill = pillsAdapter.currentList[viewHolder.adapterPosition]
                    viewModel.onPillSwiped(pill)
                }
            }).attachToRecyclerView(recyclerViewPills)
            fabAddPill.setOnClickListener {
                viewModel.onAddNewPillClick()
            }
        }
        setFragmentResultListener("add_edit_request") { _, bundle ->
            val result = bundle.getInt("add_edit_result")
            viewModel.onAddEditResult(result)
        }
        viewModel.pills.observe(viewLifecycleOwner) {
            pillsAdapter.submitList(it)
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.pillEvent.collect { event ->
                when (event) {
                    is PillsViewModel.PillEvent.ShowUndoDeletePillMessage -> {
                        Snackbar.make(requireView(), "Pill deleted", Snackbar.LENGTH_LONG)
                            .setAction("UNDO") {
                                viewModel.onUndoDeleteClick(event.pill)
                            }.show()
                    }
                    is PillsViewModel.PillEvent.NavigateToAddPillScreen -> {
                        val action =
                            MyPillsFragmentDirections.actionMyPillsFragmentToAddEditPillFragment(
                                null,
                                "New pill"
                            )
                        findNavController().navigate(action)
                    }
                    is PillsViewModel.PillEvent.NavigateToEditPillScreen -> {
                        val action =
                            MyPillsFragmentDirections.actionMyPillsFragmentToAddEditPillFragment(
                                event.pill,
                                "Edit pill"
                            )
                        findNavController().navigate(action)
                    }
                    is PillsViewModel.PillEvent.showPillSavedConfirmationMessage -> {
                        Snackbar.make(requireView(), event.msg, Snackbar.LENGTH_SHORT).show()
                    }
                }.exhaustive
            }

        }
        setHasOptionsMenu(true)
    }

    override fun onItemClick(pill: BEPill) {
        viewModel.onPillSelected(pill)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_fragment_pills, menu)

        val searchItem = menu.findItem(R.id.action_search)
        searchView = searchItem.actionView as androidx.appcompat.widget.SearchView

        val pendingQuery = viewModel.searchQuery.value
        if (pendingQuery != null && pendingQuery.isNotEmpty()) {
            searchItem.expandActionView()
            searchView.setQuery(pendingQuery, false)
        }
        searchView.onQueryTextChanged {
            viewModel.searchQuery.value = it
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        searchView.setOnQueryTextListener(null)
    }


}