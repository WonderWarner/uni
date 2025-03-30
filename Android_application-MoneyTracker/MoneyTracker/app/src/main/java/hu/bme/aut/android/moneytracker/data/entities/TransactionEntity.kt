package hu.bme.aut.android.moneytracker.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import hu.bme.aut.android.moneytracker.domain.model.PayType
import hu.bme.aut.android.moneytracker.domain.model.Category
import kotlinx.datetime.LocalDate

// Tranzakció tábla modellje Room használatával
@Entity(tableName = "transaction_table")
data class TransactionEntity(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val price : Int,
    val isExpense : Boolean,
    val category: Category,
    val payType : PayType,
    val date: LocalDate,
    val description: String
)