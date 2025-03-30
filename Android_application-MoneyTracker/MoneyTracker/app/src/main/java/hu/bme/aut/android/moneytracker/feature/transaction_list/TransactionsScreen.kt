package hu.bme.aut.android.moneytracker.feature.transaction_list

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.InsertChartOutlined
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.outlined.DeleteForever
import androidx.compose.material.icons.outlined.DeleteOutline
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeFloatingActionButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import hu.bme.aut.android.moneytracker.R
import hu.bme.aut.android.moneytracker.ui.common.ScreenNavigatorTopAppBar
import hu.bme.aut.android.moneytracker.ui.model.toUiText

// Kezdő képernyő
// Tranzakciók listájának megjelenítése, további lehetőségek
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionsScreen(
    onSettingsClicked: () -> Unit,
    onNavigateToCharts: () -> Unit,
    onEditClicked: (Int) -> Unit,
    onFabClick: () -> Unit,
    viewModel: TransactionsViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current
    var expandedTransactionId by remember { mutableIntStateOf(-1) }
    var sum = 0
    for (transaction in state.transactions) {
        if(transaction.isExpense)
            sum -= transaction.price
        else sum += transaction.price
    }

    var showDialog by remember { mutableStateOf(false) }

    Scaffold(
        // Egységes TopBar a navigációhoz, kiegészülve a DeleteAll funkcióval
        topBar = {
            ScreenNavigatorTopAppBar(
                title = stringResource(id = R.string.app_name),
                onSettingsClicked = onSettingsClicked,
                onNavigateToOtherScreen = onNavigateToCharts,
                otherScreenIcon = Icons.Default.InsertChartOutlined,
                otherScreenDescription = stringResource(id = R.string.transaction_charts_title),
                optionalIconButton = {
                    IconButton(onClick = { showDialog = true }) {
                        Icon(
                            imageVector = Icons.Outlined.DeleteForever,
                            contentDescription = null
                        )
                    }
                }
            )
        },
        modifier = Modifier.fillMaxSize(),
        // Új létrehozása
        floatingActionButton = {
            LargeFloatingActionButton(
                onClick = onFabClick,
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = null)
            }
        },
        floatingActionButtonPosition = androidx.compose.material3.FabPosition.EndOverlay,
        bottomBar = {
            BottomAppBar(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
            ) {
                Text(
                    text = stringResource(id = R.string.total_balance, sum),
                    fontWeight = FontWeight.Bold,
                    fontSize = MaterialTheme.typography.headlineMedium.fontSize,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(10.dp)
                )
            }
        }
    ) {
        // Minden elem törlésekor figyelmezetető dialógus
        if(showDialog) {
            BasicAlertDialog(onDismissRequest = { showDialog = false }
            ) {
                Card(
                    modifier = Modifier.padding(16.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(),
                    elevation = CardDefaults.cardElevation()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.dialog_delete_title),
                            style = MaterialTheme.typography.titleLarge
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = stringResource(R.string.dialog_delete_label),
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Row(
                            horizontalArrangement = Arrangement.End,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            TextButton(onClick = { showDialog = false }) {
                                Text(stringResource(R.string.dialog_delete_cancel))
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            TextButton(onClick = {
                                viewModel.deleteAllTransactions()
                                showDialog = false
                            }) {
                                Text(stringResource(R.string.dialog_delete_ok))
                            }
                        }
                    }
                }
            }
        }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .background(
                    color = if (!state.isError) {
                        MaterialTheme.colorScheme.secondaryContainer
                    } else {
                        MaterialTheme.colorScheme.background
                    }
                ),
            contentAlignment = Alignment.Center
        ) {
            if (state.isError) {
                Text(
                    text = state.error?.toUiText()?.asString(context)
                        ?: stringResource(id = R.string.error)
                )
            } else {
                if (state.transactions.isEmpty()) {
                    Text(text = stringResource(id = R.string.text_empty_transaction_list))
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize(0.95f)
                            .padding(it)
                            .clip(RoundedCornerShape(5.dp)),
                        reverseLayout = true
                    ) {
                        items(state.transactions.size) { i ->
                            val transaction = state.transactions[i]
                            val isExpanded = expandedTransactionId == transaction.id
                            // Egy tranzakció megjelenítéséért felelős ListItem
                            ListItem(
                                headlineContent = {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        val sign = if (transaction.isExpense) "-" else "+"
                                        Text(
                                            text = sign+transaction.price.toString()+" "+stringResource(id = R.string.currency_symbol),
                                            color = if (transaction.isExpense) Color(0xFF8B0000) else Color(0xFF006400),
                                            fontWeight = FontWeight.Bold,
                                            fontSize = MaterialTheme.typography.headlineSmall.fontSize)
                                        Icon(
                                            imageVector = transaction.payType.icon,
                                            contentDescription = null,
                                            modifier = Modifier
                                                .size(40.dp)
                                                .padding(start = 10.dp),
                                        )
                                        Icon(
                                            imageVector = transaction.category.icon,
                                            contentDescription = null,
                                            modifier = Modifier
                                                .size(40.dp)
                                                .padding(start = 10.dp),
                                        )
                                    }
                                },
                                supportingContent = {
                                    if (isExpanded) {
                                        Column {
                                            Text(
                                                text = stringResource(
                                                    id = R.string.transaction_date_label,
                                                    transaction.date
                                                ),
                                                fontSize = MaterialTheme.typography.bodyLarge.fontSize
                                            )
                                            if (transaction.description.isNotEmpty()) {
                                                Text(
                                                    text = stringResource(id = R.string.transaction_description_label) +" "+ transaction.description,
                                                    fontSize = MaterialTheme.typography.bodyLarge.fontSize
                                                )
                                            }
                                        }
                                    }
                                },
                                trailingContent = {
                                    Row {
                                        IconButton(onClick = { expandedTransactionId = if (isExpanded) -1 else transaction.id }) {
                                            Icon(
                                                imageVector = if (isExpanded) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
                                                contentDescription = null
                                            )
                                        }
                                        IconButton(onClick = { onEditClicked(transaction.id)}) {
                                            Icon(
                                                imageVector = Icons.Default.Edit,
                                                contentDescription = null
                                            )
                                        }
                                        IconButton(onClick = { viewModel.deleteTransaction(transaction.id)}) {
                                            Icon(
                                                imageVector = Icons.Outlined.DeleteOutline,
                                                contentDescription = null
                                            )
                                        }
                                    }
                                },
                                modifier = Modifier.clickable(onClick = { expandedTransactionId = if (isExpanded) -1 else transaction.id })
                            )
                            if (i != state.transactions.lastIndex) {
                                HorizontalDivider(
                                    thickness = 4.dp,
                                    color = MaterialTheme.colorScheme.secondaryContainer
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}