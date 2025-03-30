package hu.bme.aut.android.moneytracker.ui.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AddBox
import androidx.compose.material.icons.outlined.AttachMoney
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material.icons.outlined.Bed
import androidx.compose.material.icons.outlined.Book
import androidx.compose.material.icons.outlined.CardGiftcard
import androidx.compose.material.icons.outlined.DryCleaning
import androidx.compose.material.icons.outlined.ElectricBolt
import androidx.compose.material.icons.outlined.Fastfood
import androidx.compose.material.icons.outlined.HealthAndSafety
import androidx.compose.material.icons.outlined.Train
import androidx.compose.ui.graphics.vector.ImageVector
import hu.bme.aut.android.moneytracker.R
import hu.bme.aut.android.moneytracker.domain.model.Category

sealed class CategoryUi(
    val name: Int,
    val icon: ImageVector
) {
    object Other: CategoryUi(
        name = R.string.category_name_other,
        icon = Icons.Outlined.AddBox
    )
    object Salary: CategoryUi(
        name = R.string.category_name_salary,
        icon = Icons.Outlined.AttachMoney
    )
    object Gift: CategoryUi(
        name = R.string.category_name_gift,
        icon = Icons.Outlined.CardGiftcard
    )
    object Housing: CategoryUi(
        name = R.string.category_name_housing,
        icon = Icons.Outlined.Bed
    )
    object Food: CategoryUi(
        name = R.string.category_name_food,
        icon = Icons.Outlined.Fastfood
    )
    object Transport: CategoryUi(
        name = R.string.category_name_transport,
        icon = Icons.Outlined.Train
    )
    object Electronics: CategoryUi(
        name = R.string.category_name_electronics,
        icon = Icons.Outlined.ElectricBolt
    )
    object Entertainment: CategoryUi(
        name = R.string.category_name_entertainment,
        icon = Icons.Outlined.AutoAwesome
    )
    object Health: CategoryUi(
        name = R.string.category_name_health,
        icon = Icons.Outlined.HealthAndSafety
        )
    object Clothing: CategoryUi(
        name = R.string.category_name_clothing,
        icon = Icons.Outlined.DryCleaning
    )
    object Education: CategoryUi(
        name = R.string.category_name_education,
        icon = Icons.Outlined.Book
    )
}

fun CategoryUi.asCategory(): Category {
    return when (this) {
        is CategoryUi.Other -> Category.OTHER
        is CategoryUi.Salary -> Category.SALARY
        is CategoryUi.Gift -> Category.GIFT
        is CategoryUi.Housing -> Category.HOUSING
        is CategoryUi.Food -> Category.FOOD
        is CategoryUi.Transport -> Category.TRANSPORT
        is CategoryUi.Electronics -> Category.ELECTRONICS
        is CategoryUi.Entertainment -> Category.ENTERTAINMENT
        is CategoryUi.Health -> Category.HEALTH
        is CategoryUi.Clothing -> Category.CLOTHING
        is CategoryUi.Education -> Category.EDUCATION
    }
}

fun Category.asCategoryUi(): CategoryUi {
    return when (this) {
        Category.OTHER -> CategoryUi.Other
        Category.SALARY -> CategoryUi.Salary
        Category.GIFT -> CategoryUi.Gift
        Category.HOUSING -> CategoryUi.Housing
        Category.FOOD -> CategoryUi.Food
        Category.TRANSPORT -> CategoryUi.Transport
        Category.ELECTRONICS -> CategoryUi.Electronics
        Category.ENTERTAINMENT -> CategoryUi.Entertainment
        Category.HEALTH -> CategoryUi.Health
        Category.CLOTHING -> CategoryUi.Clothing
        Category.EDUCATION -> CategoryUi.Education
    }
}