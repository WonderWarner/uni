package hu.bme.aut.android.moneytracker.navigation

// Nézetek nevének tárolása
sealed class Screen(val route: String) {
    object Transactions: Screen("transactions")
    object CreateTransaction: Screen("create")
    object EditTransaction: Screen("create/{id}") {
        fun passId(id: Int) = "create/$id"
    }
    object TransactionSettings: Screen("settings")
    object TransactionCharts: Screen("charts")
}