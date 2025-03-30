package hu.bme.aut.android.moneytracker.data.converters

import androidx.room.TypeConverter
import hu.bme.aut.android.moneytracker.domain.model.Category

// Kategória <-> String átalakító Room-mal
object TransactionCategoryConverter {
    @TypeConverter
    fun Category.asString(): String = this.name

    @TypeConverter
    fun String.asCategory(): Category {
        return when(this) {
            Category.OTHER.name -> Category.OTHER
            Category.SALARY.name -> Category.SALARY
            Category.GIFT.name -> Category.GIFT
            Category.HOUSING.name -> Category.HOUSING
            Category.FOOD.name -> Category.FOOD
            Category.TRANSPORT.name -> Category.TRANSPORT
            Category.ELECTRONICS.name -> Category.ELECTRONICS
            Category.ENTERTAINMENT.name -> Category.ENTERTAINMENT
            Category.HEALTH.name -> Category.HEALTH
            Category.CLOTHING.name -> Category.CLOTHING
            else -> Category.EDUCATION
        }
    }

}