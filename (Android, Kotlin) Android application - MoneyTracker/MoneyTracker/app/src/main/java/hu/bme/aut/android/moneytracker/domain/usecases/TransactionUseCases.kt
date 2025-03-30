package hu.bme.aut.android.moneytracker.domain.usecases

// Perziztensen tárolt tranzakciókon végezhető egységműveletek összefogása
class TransactionUseCases(
    val loadTransactions: LoadTransactionsUseCase,
    val loadTransaction: LoadTransactionUseCase,
    val saveTransaction: SaveTransactionUseCase,
    val updateTransaction: UpdateTransactionUseCase,
    val deleteTransaction: DeleteTransactionUseCase,
    val deleteTransactions: DeleteTransactionsUseCase
)