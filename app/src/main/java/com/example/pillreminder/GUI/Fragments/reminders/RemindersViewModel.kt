package com.example.pillreminder.GUI.Fragments.reminders

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pillreminder.GUI.Fragments.pills.PillsViewModel
import com.example.pillreminder.data.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class RemindersViewModel @ViewModelInject constructor(
    private val remiderDAO: RemiderDAO,
    private val preferencesManager: PreferencesManager,
    @Assisted private val state: SavedStateHandle
) : ViewModel() {

    val searchQuery = state.getLiveData("searchQuery", "")
    val preferencesFlow = preferencesManager.preferencesFlow
    private val pillsEventChannel = Channel<ReminderEvent>()
    val rmeinderEvent = pillsEventChannel.receiveAsFlow()



    fun onAddNewPillClick() = viewModelScope.launch {
        pillsEventChannel.send(RemindersViewModel.ReminderEvent.NavigateToAddReminderScreen)
    }

    fun onPillSwiped(reminder: BEReminder) = viewModelScope.launch {
        remiderDAO.delete(reminder)
        pillsEventChannel.send(RemindersViewModel.ReminderEvent.ShowUndoDeleteReminderMessage(reminder))
    }
    sealed class ReminderEvent{
        object NavigateToAddReminderScreen : ReminderEvent()
        data class NavigateToEditReminderScreen(val pill: BEReminder) : ReminderEvent()
        data class ShowUndoDeleteReminderMessage(val pill: BEReminder) : ReminderEvent()
        data class showPillSavedConfirmationMessage(val msg: String): ReminderEvent()
    }
}