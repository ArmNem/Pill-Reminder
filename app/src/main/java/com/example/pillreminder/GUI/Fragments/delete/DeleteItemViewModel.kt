package com.example.pillreminder.GUI.Fragments.delete

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.pillreminder.data.BEPill
import com.example.pillreminder.data.PillDAO
import com.example.pillreminder.di.ApplicationScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class DeleteItemViewModel @ViewModelInject constructor(
    private val pillDAO: PillDAO,
    @ApplicationScope private val applicationScope: CoroutineScope,
    @Assisted private val state: SavedStateHandle
) : ViewModel() {
    val currentpill = state.get<BEPill>("pill")
    fun onCofirmClick() = applicationScope.launch {
        if (currentpill != null) {
            pillDAO.delete(currentpill)
        }
    }
}