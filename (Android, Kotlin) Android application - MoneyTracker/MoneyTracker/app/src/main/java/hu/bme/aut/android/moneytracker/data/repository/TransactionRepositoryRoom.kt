package hu.bme.aut.android.moneytracker.data.repository

import hu.bme.aut.android.moneytracker.data.dao.TransactionDao
import hu.bme.aut.android.moneytracker.data.entities.TransactionEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

// Adatbázis műveletek Room használatával implementálva
// DAO injektálva DI-al
class TransactionRepositoryRoom @Inject constructor(private val dao: TransactionDao) : TransactionRepository {
    override suspend fun insertTransaction(transaction: TransactionEntity) {
        dao.insertTransaction(transaction)
    }
    override fun getAllTransactions(): Flow<List<TransactionEntity>> {
        return dao.getAllTransactions()
    }
    override fun getTransactionById(id: Int): Flow<TransactionEntity> {
        return dao.getTransactionById(id)
    }
    override suspend fun updateTransaction(transaction: TransactionEntity) {
        dao.updateTransaction(transaction)
    }
    override suspend fun deleteTransaction(id: Int) {
        dao.deleteTransaction(id)
    }
    override suspend fun deleteAllTransactions() {
        dao.deleteAllTransactions()
    }

}