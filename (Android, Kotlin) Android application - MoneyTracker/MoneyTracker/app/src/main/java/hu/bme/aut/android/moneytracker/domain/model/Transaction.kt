package hu.bme.aut.android.moneytracker.domain.model

import hu.bme.aut.android.moneytracker.data.entities.TransactionEntity
import kotlinx.datetime.LocalDate

// Tranzakció osztály és relációs entitássá képezése
data class Transaction(
    val id: Int,
    val price : Int,
    val isExpense : Boolean,
    val category: Category,
    val payType : PayType,
    val date: LocalDate,
    val description: String
)

fun TransactionEntity.asTransaction(): Transaction = Transaction(
    id = id,
    price = price,
    isExpense = isExpense,
    category = category,
    payType = payType,
    date = date,
    description = description
)

fun Transaction.asTransactionEntity(): TransactionEntity = TransactionEntity(
    id = id,
    price = price,
    isExpense = isExpense,
    category = category,
    payType = payType,
    date = date,
    description = description
)