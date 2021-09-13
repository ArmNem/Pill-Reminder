package com.example.pillreminder.GUI.Fragments.pills

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pillreminder.GUI.ADD_PILL_RESULT_OK
import com.example.pillreminder.GUI.EDIT_PILL_RESULT_OK
import com.example.pillreminder.data.BEPill
import com.example.pillreminder.data.PillDAO
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class AddEditPillViewModel @ViewModelInject constructor(
    private val pillDAO: PillDAO,
    @Assisted private val state: SavedStateHandle
) : ViewModel() {
    val pill = state.get<BEPill>("pill")

    var pillName = state.get<String>("pillName") ?: pill?.name ?: ""
        set(value) {
            field = value
            state.set("pillName", value)
        }
    var pillDose = state.get<String>("pillDose") ?: pill?.dose ?: ""
        set(value) {
            field = value
            state.set("pillDose", value)
        }
    var pillType = state.get<String>("pillType") ?: pill?.type ?: ""
        set(value) {
            field = value
            state.set("pillType", value)
        }
    var pillDaytime = state.get<String>("pillDaytime")?: pill?.daytime ?: ""
        set(value) {
            field = value
            state.set("pillDaytime", value)
        }
    var pillDescription = state.get<String>("pillDescription") ?: pill?.description ?: ""
        set(value) {
            field = value
            state.set("pillDescription", value)
        }

    private val addEditPillEventChannel = Channel<AddEditPillEvent>()
    val addEditPillEvent = addEditPillEventChannel.receiveAsFlow()

    fun onSaveClick() {
        if (pillName.isBlank()) {
            ShowInvalidInputMessage("Name cannot be empty")
            return
        }
        if (pill != null) {
            val updatedPill = pill.copy(
                name = pillName,
                dose = pillDose,
                type = pillType,
                daytime = pillDaytime,
                description = pillDescription
            )
            updatePill(updatedPill)
        } else {
            val newPill = BEPill(
                name = pillName,
                dose = pillDose,
                type = pillType,
                daytime = pillDaytime,
                description = pillDescription
            )
            createPill(newPill)
        }
    }

    private fun updatePill(pill: BEPill) = viewModelScope.launch {
        pillDAO.update(pill)
        addEditPillEventChannel.send(AddEditPillEvent.NavigateBackWithResult(EDIT_PILL_RESULT_OK))
    }

    private fun createPill(pill: BEPill) = viewModelScope.launch {
        pillDAO.insert(pill)
        addEditPillEventChannel.send(AddEditPillEvent.NavigateBackWithResult(ADD_PILL_RESULT_OK))
    }

    private fun ShowInvalidInputMessage(text: String) = viewModelScope.launch {
        addEditPillEventChannel.send(AddEditPillEvent.ShowInvalidInputMessage(text))

    }

    sealed class AddEditPillEvent {
        data class ShowInvalidInputMessage(val msg: String) : AddEditPillEvent()
        data class NavigateBackWithResult(val result: Int) : AddEditPillEvent()
    }
}