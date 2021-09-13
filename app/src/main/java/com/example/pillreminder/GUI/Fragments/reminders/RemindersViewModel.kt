package com.example.pillreminder.GUI.Fragments.reminders

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.example.pillreminder.GUI.ADD_PILL_RESULT_OK
import com.example.pillreminder.GUI.EDIT_PILL_RESULT_OK
import com.example.pillreminder.GUI.Fragments.pills.PillsViewModel
import com.example.pillreminder.data.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class RemindersViewModel @ViewModelInject constructor(
    private val remiderDAO: RemiderDAO,
    private val preferencesManager: PreferencesManager,
    @Assisted private val state: SavedStateHandle
) : ViewModel() {

    val searchQuery = state.getLiveData("searchQuery", "")
    val preferencesFlow = preferencesManager.preferencesFlow
    private val remindersEventChannel = Channel<ReminderEvent>()
    val reminderEvent = remindersEventChannel.receiveAsFlow()
    private val reminderFlow = remiderDAO.getAll()
    val reminders = reminderFlow.asLiveData()

    fun onReminderSelected(reminder: BEReminder) = viewModelScope.launch {
        remindersEventChannel.send(ReminderEvent.NavigateToEditReminderScreen(reminder))
    }

    fun onAddEditResult(result: Int) {
        when (result) {
            ADD_PILL_RESULT_OK -> showReminderSavedConfirmationMessage("Reminder added")
            EDIT_PILL_RESULT_OK -> showReminderSavedConfirmationMessage("Reminder updated")
        }
    }

    private fun showReminderSavedConfirmationMessage(text: String) = viewModelScope.launch {
        remindersEventChannel.send(ReminderEvent.showReminderSavedConfirmationMessage(text))
    }

    fun onAddNewReminderClick() = viewModelScope.launch {
        remindersEventChannel.send(ReminderEvent.NavigateToAddReminderScreen)
    }

    fun onUndoDeleteClick(reminder: BEReminder) = viewModelScope.launch {
        remiderDAO.insert(reminder)
    }

    fun onPillSwiped(reminder: BEReminder) = viewModelScope.launch {
        remiderDAO.delete(reminder)
        remindersEventChannel.send(
            ReminderEvent.ShowUndoDeleteReminderMessage(
                reminder
            )
        )
    }

    sealed class ReminderEvent {
        object NavigateToAddReminderScreen : ReminderEvent()
        data class NavigateToEditReminderScreen(val reminder: BEReminder) : ReminderEvent()
        data class ShowUndoDeleteReminderMessage(val reminder: BEReminder) :
            ReminderEvent()

        data class showReminderSavedConfirmationMessage(val msg: String) : ReminderEvent()
    }
}