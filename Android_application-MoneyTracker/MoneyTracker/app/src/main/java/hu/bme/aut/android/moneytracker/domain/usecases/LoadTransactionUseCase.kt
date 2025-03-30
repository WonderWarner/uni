package hu.bme.aut.android.moneytracker.domain.usecases

import hu.bme.aut.android.moneytracker.data.repository.TransactionRepository
import hu.bme.aut.android.moneytracker.domain.model.Transaction
import hu.bme.aut.android.moneytracker.domain.model.asTransaction
import kotlinx.coroutines.flow.first
import java.io.IOException

class LoadTransactionUseCase(private val repository: TransactionRepository) {
    suspend operator fun invoke(id: Int): Result<Transaction> {
        return try {
            Result.success(repository.getTransactionById(id).first().asTransaction())
        } catch (e: IOException) {
            Result.failure(e)
        }
    }
}