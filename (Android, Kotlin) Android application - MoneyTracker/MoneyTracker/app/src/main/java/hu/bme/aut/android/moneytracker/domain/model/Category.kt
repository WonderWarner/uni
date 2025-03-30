package hu.bme.aut.android.moneytracker.domain.model

// Tranzakció kategóriái
enum class Category {
    OTHER,
    SALARY,
    GIFT,
    HOUSING,
    FOOD,
    TRANSPORT,
    ELECTRONICS,
    ENTERTAINMENT,
    HEALTH,
    CLOTHING,
    EDUCATION;

    companion object {
        val categories = listOf(
            OTHER,
            SALARY,
            GIFT,
            HOUSING,
            FOOD,
            TRANSPORT,
            ELECTRONICS,
            ENTERTAINMENT,
            HEALTH,
            CLOTHING,
            EDUCATION
        )
    }
}