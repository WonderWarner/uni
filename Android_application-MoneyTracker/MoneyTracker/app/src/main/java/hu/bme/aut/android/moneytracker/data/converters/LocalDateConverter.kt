package hu.bme.aut.android.moneytracker.data.converters

import androidx.room.TypeConverter
import kotlinx.datetime.LocalDate
import kotlinx.datetime.toLocalDate

// Idő <-> String átalakító Room-mal
object LocalDateConverter {
    @TypeConverter
    fun LocalDate.asString(): String = this.toString()

    @TypeConverter
    fun String.asLocalDateTime(): LocalDate = this.toLocalDate()
}