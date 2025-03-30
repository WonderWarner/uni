package hu.bme.aut.android.moneytracker.domain.usecases

import hu.bme.aut.android.moneytracker.data.repository.TransactionRepository
import hu.bme.aut.android.moneytracker.domain.model.Transaction
import hu.bme.aut.android.moneytracker.domain.model.asTransactionEntity

class SaveTransactionUseCase(private val repository: TransactionRepository) {
    suspend operator fun invoke(transaction: Transaction) {
        repository.insertTransaction(transaction.asTransactionEntity())
    }
}