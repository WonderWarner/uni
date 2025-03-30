package hu.bme.aut.android.moneytracker.feature.transaction_create

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import hu.bme.aut.android.moneytracker.domain.usecases.TransactionUseCases
import hu.bme.aut.android.moneytracker.ui.model.CategoryUi
import hu.bme.aut.android.moneytracker.ui.model.PayTypeUi
import hu.bme.aut.android.moneytracker.ui.model.TransactionUi
import hu.bme.aut.android.moneytracker.ui.model.asTransaction
import hu.bme.aut.android.moneytracker.ui.model.asTransactionUi
import hu.bme.aut.android.moneytracker.ui.model.toUiText
import hu.bme.aut.android.moneytracker.ui.util.UiEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import javax.inject.Inject

// Új tranzakció létrehozásához szolgáló ViewModel
@HiltViewModel
class CreateTransactionViewModel @Inject constructor(
    private val transactionOperations: TransactionUseCases
) : ViewModel() {

    private val _state = MutableStateFlow(CreateTransactionState())
    val state = _state.asStateFlow()

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    // Eseménykezelő az adatok módosításához
    fun onEvent(event: CreateTransactionEvent) {
        when(event) {
            is CreateTransactionEvent.ChangePrice -> {
                val newValue = event.price
                _state.update { it.copy(
                        transaction = it.transaction.copy(price = newValue)
                    )
                }
            }
            is CreateTransactionEvent.ChangeExpense -> {
                val newValue = event.isExpense
                _state.update {
                    it.copy(
                        transaction = it.transaction.copy(isExpense = newValue)
                    )
                }
            }
            is CreateTransactionEvent.ChangePayType -> {
                val newValue = event.payType
                _state.update {
                    it.copy(
                        transaction = it.transaction.copy(payType = newValue)
                    )
                }
            }
            is CreateTransactionEvent.ChangeCategory -> {
                val newValue = event.category
                _state.update {
                    it.copy(
                        transaction = it.transaction.copy(category = newValue)
                    )
                }
            }
            is CreateTransactionEvent.ChangeDate -> {
                val newValue = event.date
                _state.update {
                    it.copy(
                        transaction = it.transaction.copy(date = newValue.toString())
                    )
                }
            }
            is CreateTransactionEvent.ChangeDescription -> {
                val newValue = event.description
                _state.update {
                    it.copy(
                        transaction = it.transaction.copy(description = newValue)
                    )
                }
            }
            CreateTransactionEvent.SaveTransaction -> {
                onSave()
            }
        }
    }

    // Perzisztens mentés
    private fun onSave() {
        viewModelScope.launch {
            try {
                transactionOperations.saveTransaction(state.value.transaction.asTransaction())
                _uiEvent.send(UiEvent.Success)
            } catch (e: Exception) {
                _uiEvent.send(UiEvent.Failure(e.toUiText()))
            }
        }
    }

    // Betöltése egy tranzakciónak
    fun loadTransaction(id: Int) {
        viewModelScope.launch {
            try {
                CoroutineScope(coroutineContext).launch(Dispatchers.IO) {
                    val transaction = transactionOperations.loadTransaction(id).getOrThrow().asTransactionUi()
                    _state.update {
                        it.copy(
                            transaction = transaction
                        )
                    }
                }
            } catch (e: Exception) {
                _uiEvent.send(UiEvent.Failure(e.toUiText()))
            }
        }
    }

}

// Aktuális tranzakció állapota
data class CreateTransactionState(
    val transaction: TransactionUi = TransactionUi()
)

// Lehetséges események
sealed class CreateTransactionEvent {
    data class ChangePrice(val price: Int) : CreateTransactionEvent()
    data class ChangeExpense(val isExpense: Boolean): CreateTransactionEvent()
    data class ChangePayType(val payType: PayTypeUi): CreateTransactionEvent()
    data class ChangeCategory(val category: CategoryUi): CreateTransactionEvent()
    data class ChangeDate(val date: LocalDate): CreateTransactionEvent()
    data class ChangeDescription(val description: String): CreateTransactionEvent()
    object SaveTransaction: CreateTransactionEvent()
}