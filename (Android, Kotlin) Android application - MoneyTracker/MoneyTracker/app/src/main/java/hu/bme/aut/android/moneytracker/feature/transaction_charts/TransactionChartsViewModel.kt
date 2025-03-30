package hu.bme.aut.android.moneytracker.feature.transaction_charts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import hu.bme.aut.android.moneytracker.domain.di.StringProvider
import hu.bme.aut.android.moneytracker.domain.usecases.LoadTransactionsUseCase
import hu.bme.aut.android.moneytracker.ui.model.CategoryUi
import hu.bme.aut.android.moneytracker.ui.model.PayTypeUi
import hu.bme.aut.android.moneytracker.ui.model.TransactionUi
import hu.bme.aut.android.moneytracker.ui.model.asTransactionUi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

// Diagramok megjelenítéséért szolgáló viewmodel
@HiltViewModel
class TransactionChartsViewModel @Inject constructor(
    private val loadTransactionsUseCase: LoadTransactionsUseCase,
    private val stringProvider: StringProvider
) : ViewModel() {
    private val _state = MutableStateFlow(TransactionChartsState())
    val state = _state.asStateFlow()

    private val _transactions = MutableStateFlow<List<TransactionUi>>(emptyList())

    init {
        loadTransactions()
    }

    //Ha biztosan szeretnénk minden frissítést megjeleníteni, többször is meg lehet hívni
    fun loadTransactions() {
        viewModelScope.launch {
            CoroutineScope(coroutineContext).launch(Dispatchers.IO) {
                _transactions.value =
                    loadTransactionsUseCase().getOrThrow().map { it.asTransactionUi() }
            }
        }
    }

    // Adatmódosítás lehetővé tétele
    fun updateFilter(filter : ChartFilter) {
        _state.update {
            it.copy(
                selectedFilter = filter
            )
        }
    }
    fun updateCategory(category: CategoryUi?) {
        _state.update {
            it.copy(
                selectedCategory = category
            )
        }
    }

    fun updatePayType(payType: PayTypeUi?) {
        _state.update {
            it.copy(
                selectedPayType = payType
            )
        }
    }

    fun updateStartDate(date: String?) {
        _state.update {
            it.copy(
                startDate = date
            )
        }
    }

    fun updateEndDate(date: String?) {
        _state.update {
            it.copy(
                endDate = date
            )
        }
    }

    fun updateExpenseOnly(isExpenseOnly: Boolean) {
        _state.update {
            it.copy(
                isExpenseOnly = isExpenseOnly
            )
        }
    }

    // Szűrés elvégzése a tranzakciókon
    fun filterTransactions(): List<TransactionUi> {
        val state = _state.value
        val filtered = _transactions.value.filter { transaction ->
            val withinDateRange = state.startDate?.let { transaction.date >= it } != false &&
                    state.endDate?.let { transaction.date <= it } != false
            val matchesCategory = if(state.selectedFilter != ChartFilter.CATEGORY) true else state.selectedCategory?.let { transaction.category == it } ?: true
            val matchesPayType = if(state.selectedFilter != ChartFilter.PAY_TYPE) true else state.selectedPayType?.let { transaction.payType == it } ?: true
            val matchesExpense = if (state.selectedFilter == ChartFilter.NONE) true else state.isExpenseOnly == transaction.isExpense

            withinDateRange && matchesCategory && matchesPayType && matchesExpense
        }
        return filtered
    }

    // Kiadás/bevétel arányát adja vissza
    fun getIncomeAndExpense(transactions: List<TransactionUi>): List<Int> {
        val expenseSum = transactions.filter { it.isExpense }.sumOf { it.price }
        val incomeSum = transactions.filterNot { it.isExpense }.sumOf { it.price }
        return listOf(expenseSum, incomeSum)
    }

    // Kategóriák szerint csoportosít
    fun getTransactionsGroupByCategory(
        transactions: List<TransactionUi>,
    ): List<Pair<String, Int>> {
        return transactions.groupBy { it.category.name }
            .map { (categoryResId, transactions) ->
                val categoryName = stringProvider.getString(categoryResId)
                categoryName to transactions.sumOf { it.price }
            }
    }

    // Fizetési eszközök szerint csoportosít
    fun getTransactionsGroupByPayType(
        transactions: List<TransactionUi>,
    ): List<Pair<String, Int>> {
        return transactions.groupBy { it.payType.name }
            .map { (payTypeResId, transactions) ->
                val payTypeName = stringProvider.getString(payTypeResId)
                payTypeName to transactions.sumOf { it.price }
            }
    }


}

// ViewModel állapota
data class TransactionChartsState(
    val selectedCategory: CategoryUi? = null,
    val selectedPayType: PayTypeUi? = null,
    val startDate: String? = null,
    val endDate: String? = null,
    val isExpenseOnly: Boolean = true,
    val selectedFilter: ChartFilter = ChartFilter.NONE
)

enum class ChartFilter {
    NONE,
    CATEGORY,
    PAY_TYPE,
}
