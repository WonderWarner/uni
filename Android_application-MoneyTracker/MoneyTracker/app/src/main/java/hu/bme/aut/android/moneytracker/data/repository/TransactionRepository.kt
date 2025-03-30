package hu.bme.aut.android.moneytracker.data.repository

import hu.bme.aut.android.moneytracker.data.entities.TransactionEntity
import kotlinx.coroutines.flow.Flow

// Adatbázis műveletek interfésze megvalósítástól függetlenül
interface TransactionRepository {
    suspend fun insertTransaction(transaction: TransactionEntity)
    fun getAllTransactions(): Flow<List<TransactionEntity>>
    fun getTransactionById(id: Int): Flow<TransactionEntity>
    suspend fun updateTransaction(transaction: TransactionEntity)
    suspend fun deleteTransaction(id: Int)
    suspend fun deleteAllTransactions()
}