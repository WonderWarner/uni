package hu.bme.aut.android.moneytracker.data.converters

import androidx.room.TypeConverter
import hu.bme.aut.android.moneytracker.domain.model.PayType

// Fizetési eszköz <-> String átalakító Room-mal
object TransactionPayTypeConverter {
    @TypeConverter
    fun PayType.asString(): String = this.name

    @TypeConverter
    fun String.asPayType(): PayType {
        return when(this) {
            PayType.CARD.name -> PayType.CARD
            PayType.CASH.name -> PayType.CASH
            else -> PayType.REVOLUT
        }
    }
}