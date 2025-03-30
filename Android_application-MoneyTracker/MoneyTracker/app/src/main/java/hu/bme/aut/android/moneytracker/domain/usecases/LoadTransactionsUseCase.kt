package hu.bme.aut.android.moneytracker.domain.usecases

import hu.bme.aut.android.moneytracker.data.repository.TransactionRepository
import hu.bme.aut.android.moneytracker.domain.model.Transaction
import kotlinx.coroutines.flow.first
import hu.bme.aut.android.moneytracker.domain.model.asTransaction
import java.io.IOException

class LoadTransactionsUseCase(private val repository: TransactionRepository) {
    suspend operator fun invoke(): Result<List<Transaction>> {
        return try {
            val transactions = repository.getAllTransactions().first()
            Result.success(transactions.map { it.asTransaction() })
        } catch (e: IOException) {
            Result.failure(e)
        }

    }
}