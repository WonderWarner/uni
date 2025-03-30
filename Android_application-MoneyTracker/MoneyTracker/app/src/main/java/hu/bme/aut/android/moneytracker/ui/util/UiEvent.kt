package hu.bme.aut.android.moneytracker.ui.util

import hu.bme.aut.android.moneytracker.ui.model.UiText

sealed class UiEvent {
    object Success: UiEvent()
    data class Failure(val message: UiText): UiEvent()
}