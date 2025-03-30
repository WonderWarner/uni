package hu.bme.aut.android.moneytracker.ui.model

import hu.bme.aut.android.moneytracker.domain.model.Transaction
import kotlinx.datetime.LocalDate
import kotlinx.datetime.toLocalDate
import java.time.LocalDateTime

data class TransactionUi(
    val id : Int = 0,
    val price: Int = 0,
    val isExpense: Boolean = true,
    val category: CategoryUi = CategoryUi.Other,
    val payType: PayTypeUi = PayTypeUi.CreditCard,
    val date: String = LocalDate(
        LocalDateTime.now().year,
        LocalDateTime.now().monthValue,
        LocalDateTime.now().dayOfMonth
    ).toString(),
    val description: String = ""
)

fun Transaction.asTransactionUi(): TransactionUi = TransactionUi(
    id = id,
    price = price,
    isExpense = isExpense,
    category = category.asCategoryUi(),
    payType = payType.asPayTypeUi(),
    date = date.toString(),
    description = description
)

fun TransactionUi.asTransaction(): Transaction = Transaction(
    id = id,
    price = price,
    isExpense = isExpense,
    category = category.asCategory(),
    payType = payType.asPayType(),
    date = date.toLocalDate(),
    description = description
)