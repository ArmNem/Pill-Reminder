package com.example.pillreminder.GUI.Fragments.reminders

import android.app.Application
import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.pillreminder.GUI.ADD_PILL_RESULT_OK
import com.example.pillreminder.GUI.EDIT_PILL_RESULT_OK
import com.example.pillreminder.data.BEPill
import com.example.pillreminder.data.BEReminder
import com.example.pillreminder.data.RemiderDAO
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class AddEditReminderViewModel @ViewModelInject constructor(
    private val reminderDao: RemiderDAO,
    @Assisted private val state: SavedStateHandle
) : AndroidViewModel(Application()) {
    val reminder = state.get<BEReminder>("reminder")
    val pill = BEPill(
        "Red pill",
        "1 tablet per day",
        "vitamin",
        "Morning",
        "Take this pill once a day with food"
    )
    var remindername = state.get<BEPill>("reminderName") ?: reminder?.pillinreminder ?: pill
        set(value) {
            field = value
            state.set("reminderName", value)
        }
    var iseveryday = state.get<Boolean>("isEveryDay") ?: reminder?.isEveryDay ?: false
        set(value) {
            field = value
            state.set("isEveryDay", value)
        }
    var alarmtime = state.get<String>("alarmTime") ?: reminder?.alarmTime ?: ""
        set(value) {
            field = value
            state.set("alarmTime", value)
        }
    var pilldose = state.get<String>("pillDose") ?: reminder?.pilldose ?: ""
        set(value) {
            field = value
            state.set("pillDose", value)
        }
    var isactive = state.get<Boolean>("isActive") ?: reminder?.isActive ?: false
        set(value) {
            field = value
            state.set("isActive", value)
        }
    var istaken = state.get<Boolean>("isTaken") ?: reminder?.isTaken ?: false
        set(value) {
            field = value
            state.set("isTaken", value)
        }


    private val addEditRemiderEventChannel = Channel<AddEditReminderEvent>()
    val addEditRemiderEvent = addEditRemiderEventChannel.receiveAsFlow()

    fun onSaveClick() {
        if (remindername == null) {
            ShowInvalidInputMessage("Name cannot be empty")
            return
        }
        if (reminder != null) {
            val updatedReminder = reminder.copy(
                alarmTime = alarmtime,
                isEveryDay = iseveryday,
                pilldose = pilldose,
                isActive = isactive,
                isTaken = istaken
            )
            updateReminder(updatedReminder)
        } else {
            val newReminder = BEReminder(
                alarmTime = alarmtime,
                isEveryDay = iseveryday,
                pilldose = pilldose,
                isActive = isactive,
                isTaken = istaken,
                pillinreminder = remindername
            )
            createReminder(newReminder)
        }
    }
    /*fun openTimePicker() = viewModelScope.launch{
       /* val currentTime = Calendar.getInstance()
        var hour = currentTime.get(Calendar.HOUR_OF_DAY)
        var minute = currentTime.get(Calendar.MINUTE)

        val timePicker =  TimePickerDialog(getApplication(), TimePickerDialog.OnTimeSetListener(){ timePicker: TimePicker, i: Int, i1: Int ->
            fun onTimeSet(timepicker: TimePicker, selectedhour: Int, selectedminute: Int){
                hour = selectedhour
                minute = selectedminute

            }
        },hour,minute,false)
        timePicker.setTitle("Select time")
        timePicker.show()*/
        val reminder = state.get<BEReminder>("reminder")
        addEditRemiderEventChannel.send(AddEditReminderEvent.NavigateToTimePickerPage(reminder))
    }*/

    private fun updateReminder(reminder: BEReminder) = viewModelScope.launch {
        reminderDao.update(reminder)
        addEditRemiderEventChannel.send(
            AddEditReminderEvent.NavigateBackWithResult(
                EDIT_PILL_RESULT_OK
            )
        )
    }

    private fun createReminder(reminder: BEReminder) = viewModelScope.launch {
        reminderDao.insert(reminder)
        addEditRemiderEventChannel.send(
            AddEditReminderEvent.NavigateBackWithResult(
                ADD_PILL_RESULT_OK
            )
        )
    }

    private fun ShowInvalidInputMessage(text: String) = viewModelScope.launch {
        addEditRemiderEventChannel.send(AddEditReminderEvent.ShowInvalidInputMessage(text))

    }

    sealed class AddEditReminderEvent {
        data class ShowInvalidInputMessage(val msg: String) : AddEditReminderEvent()
        data class NavigateBackWithResult(val result: Int) : AddEditReminderEvent()
        //data class NavigateToTimePickerPage(val reminder: BEReminder?): AddEditReminderEvent()
    }



}