package hu.bme.aut.android.moneytracker.domain.usecases

import hu.bme.aut.android.moneytracker.data.repository.TransactionRepository

class DeleteTransactionsUseCase(private val repository: TransactionRepository) {
    suspend operator fun invoke() {
        repository.deleteAllTransactions()
    }
}