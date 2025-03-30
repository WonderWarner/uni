package hu.bme.aut.android.moneytracker.domain.usecases

import hu.bme.aut.android.moneytracker.data.repository.TransactionRepository

class DeleteTransactionUseCase(private val repository: TransactionRepository) {

    suspend operator fun invoke(id: Int) {
        repository.deleteTransaction(id)
    }

}