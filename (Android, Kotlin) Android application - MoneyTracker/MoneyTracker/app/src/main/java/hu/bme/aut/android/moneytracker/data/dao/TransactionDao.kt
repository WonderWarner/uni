package hu.bme.aut.android.moneytracker.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import hu.bme.aut.android.moneytracker.data.entities.TransactionEntity
import kotlinx.coroutines.flow.Flow

// Adatelérési réteg Room használatával
// Alapvető CRUD műveletek
@Dao
interface TransactionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransaction(transaction: TransactionEntity)

    @Query("SELECT * FROM transaction_table ORDER BY date DESC, id DESC")
    fun getAllTransactions(): Flow<List<TransactionEntity>>

    @Query("SELECT * FROM transaction_table WHERE id = :id")
    fun getTransactionById(id: Int): Flow<TransactionEntity>

    @Update
    suspend fun updateTransaction(transaction: TransactionEntity)

    @Query("DELETE FROM transaction_table WHERE id = :id")
    suspend fun deleteTransaction(id: Int)

    @Query("DELETE FROM transaction_table")
    suspend fun deleteAllTransactions()

    // A grafikonhoz lehetett volna speciális szűrő/szummázó/csoportosító lekérdezés
    // Végül azért nem csináltam, mert nagyon sokszor fordulna az adatbázishoz, inkább helyben végeztem szűrést és számolást
}