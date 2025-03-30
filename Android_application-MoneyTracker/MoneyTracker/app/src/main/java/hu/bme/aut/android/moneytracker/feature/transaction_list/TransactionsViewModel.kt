package hu.bme.aut.android.moneytracker.feature.transaction_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import hu.bme.aut.android.moneytracker.domain.usecases.TransactionUseCases
import hu.bme.aut.android.moneytracker.ui.model.TransactionUi
import hu.bme.aut.android.moneytracker.ui.model.asTransactionUi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

// Tranzakciók listanézetéhez szolgáló ViewModel
@HiltViewModel
class TransactionsViewModel@Inject constructor(
    private val transactionOperations: TransactionUseCases
) : ViewModel() {

    private val _state = MutableStateFlow(TransactionsState())
    val state = _state.asStateFlow()

    init {
        loadTransactions()
    }

    // Tranzakciók betöltése
    private fun loadTransactions() {
        viewModelScope.launch {
            try {
                CoroutineScope(coroutineContext).launch(Dispatchers.IO) {
                    val transactions = transactionOperations.loadTransactions().getOrThrow().map { it.asTransactionUi() }
                    _state.update { it.copy(
                        transactions = transactions
                    ) }
                }
            } catch (e: Exception) {
                _state.update { it.copy(
                    error = e
                ) }
            }
        }
    }

    // Kiválasztott tranzakció törlése
    fun deleteTransaction(id: Int) {
        viewModelScope.launch {
            try {
                CoroutineScope(coroutineContext).launch(Dispatchers.IO) {
                    transactionOperations.deleteTransaction(id)
                }
                loadTransactions()
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        error = e
                    )
                }
            }
        }
    }

    // Minden tranzakció végleges törlése
    fun deleteAllTransactions() {
        viewModelScope.launch {
            try {
                CoroutineScope(coroutineContext).launch(Dispatchers.IO) {
                    transactionOperations.deleteTransactions()
                }
                loadTransactions()
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        error = e
                    )
                }
            }
        }
    }

}

// Listanézet állapota
data class TransactionsState(
    val error: Throwable? = null,
    val isError: Boolean = error != null,
    val transactions: List<TransactionUi> = emptyList()
)
