package hu.bme.aut.android.moneytracker.domain.model

// Tranzakció fizetési eszközei
enum class PayType {
    CARD,
    CASH,
    REVOLUT;

    companion object {
        val payTypes = listOf(CARD, CASH, REVOLUT)
    }
}