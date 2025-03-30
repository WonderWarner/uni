package hu.bme.aut.android.moneytracker.navigation

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import hu.bme.aut.android.moneytracker.feature.transaction_charts.TransactionChartsScreen
import hu.bme.aut.android.moneytracker.feature.transaction_create.CreateTransactionScreen
import hu.bme.aut.android.moneytracker.feature.transaction_list.TransactionsScreen
import hu.bme.aut.android.moneytracker.feature.transaction_settings.TransactionSettingsScreen

// Navigációs gráf a felületek közötti navigációhoz
@ExperimentalMaterial3Api
@Composable
fun NavGraph(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Transactions.route
    ) {
        // Listanézet
        composable(Screen.Transactions.route) {
            TransactionsScreen(
                onEditClicked = {
                    navController.navigate(Screen.EditTransaction.passId(it))
                },
                onFabClick = {
                    navController.navigate(Screen.CreateTransaction.route)
                },
                onSettingsClicked = {
                    navController.navigate(Screen.TransactionSettings.route)
                },
                onNavigateToCharts = {
                    navController.navigate(Screen.TransactionCharts.route) {
                        popUpTo(Screen.Transactions.route) { inclusive = true }
                    }
                }
            )
        }
        // Létrehozó nézet
        composable(Screen.CreateTransaction.route) {
            CreateTransactionScreen(onNavigateBack = {
                navController.popBackStack(
                    route = Screen.Transactions.route,
                    inclusive = true
                )
                navController.navigate(Screen.Transactions.route)
            })
        }
        // Szerkesztőnézet
        composable(
            route = Screen.EditTransaction.route,
            arguments = listOf(
                navArgument("id") {
                    type = NavType.IntType
                }
            )
        ) {
            CreateTransactionScreen(
                onNavigateBack = {
                    navController.popBackStack(
                        route = Screen.Transactions.route,
                        inclusive = true
                    )
                    navController.navigate(Screen.Transactions.route)
                },
                loadedId = it.arguments?.getInt("id") ?: -1
            )
        }
        // Beállítások nézet
        composable(Screen.TransactionSettings.route) {
            TransactionSettingsScreen(
                // Kiveszi önmagát, így oda nem lehet vissza navigálni
                onSaved = { navController.popBackStack() }
            )
        }
        // Statisztikák nézet
        composable(Screen.TransactionCharts.route) {
            TransactionChartsScreen(
                onSettingsClicked = {
                    navController.navigate(Screen.TransactionSettings.route)
                },
                onNavigateToTransactions = {
                    navController.navigate(Screen.Transactions.route) {
                        popUpTo(Screen.TransactionCharts.route) { inclusive = true}
                    }
                }
            )

        }
    }
}