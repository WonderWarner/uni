package hu.bme.aut.android.moneytracker.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import hu.bme.aut.android.moneytracker.data.converters.LocalDateConverter
import hu.bme.aut.android.moneytracker.data.converters.TransactionCategoryConverter
import hu.bme.aut.android.moneytracker.data.converters.TransactionPayTypeConverter
import hu.bme.aut.android.moneytracker.data.dao.TransactionDao
import hu.bme.aut.android.moneytracker.data.entities.TransactionEntity

// Adatbázis osztály Room használatával
@Database(entities = [TransactionEntity::class], version = 1, exportSchema = false)
@TypeConverters(TransactionCategoryConverter::class, TransactionPayTypeConverter::class, LocalDateConverter::class)
abstract class TransactionDatabase : RoomDatabase() {
    abstract val dao: TransactionDao
}