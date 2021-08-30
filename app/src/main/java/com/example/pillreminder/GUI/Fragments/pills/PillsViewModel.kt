package com.example.pillreminder.GUI.Fragments.pills

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.example.pillreminder.GUI.ADD_PILL_RESULT_OK
import com.example.pillreminder.GUI.EDIT_PILL_RESULT_OK
import com.example.pillreminder.data.BEPill
import com.example.pillreminder.data.PillDAO
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class PillsViewModel @ViewModelInject constructor(
    private val pillDAO: PillDAO,
    @Assisted private val state: SavedStateHandle
) : ViewModel() {
    val searchQuery = state.getLiveData("searchQuery", "")
    private val pillsEventChannel = Channel<PillEvent>()
    val pillEvent = pillsEventChannel.receiveAsFlow()
    private val pillFlow = combine(
        searchQuery.asFlow()
    ) { query ->
        query
    }.flatMapLatest {
        pillDAO.getPillsBySearch(it.toString())
    }
    val pills = pillFlow.asLiveData()

    fun onPillSelected(pill: BEPill) = viewModelScope.launch {
        pillsEventChannel.send(PillEvent.NavigateToEditPillScreen(pill))
    }
    fun onPillSwiped(pill: BEPill) = viewModelScope.launch {
        pillDAO.delete(pill)
        pillsEventChannel.send(PillEvent.ShowUndoDeletePillMessage(pill))
    }

    fun onUndoDeleteClick(pill: BEPill) = viewModelScope.launch {
        pillDAO.insert(pill)
    }

    fun onAddNewPillClick() = viewModelScope.launch {
        pillsEventChannel.send(PillEvent.NavigateToAddPillScreen)
    }

    fun onAddEditResult(result: Int) {
        when (result) {
            ADD_PILL_RESULT_OK -> showPillSavedConfirmationMessage("Pill added")
            EDIT_PILL_RESULT_OK -> showPillSavedConfirmationMessage("Pill updated")
        }
    }

    private fun showPillSavedConfirmationMessage(text: String) = viewModelScope.launch {
        pillsEventChannel.send(PillEvent.showPillSavedConfirmationMessage(text))
    }

    sealed class PillEvent{
        object NavigateToAddPillScreen : PillEvent()
        data class NavigateToEditPillScreen(val pill: BEPill) : PillEvent()
        data class ShowUndoDeletePillMessage(val pill: BEPill) : PillEvent()
        data class showPillSavedConfirmationMessage(val msg: String): PillEvent()
    }
}