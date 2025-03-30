package hu.bme.aut.android.moneytracker.feature.transaction_create

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LargeFloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import hu.bme.aut.android.moneytracker.R
import hu.bme.aut.android.moneytracker.ui.common.TransactionEditor
import hu.bme.aut.android.moneytracker.ui.util.UiEvent
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import kotlinx.datetime.toLocalDate
import java.time.Instant
import java.time.ZoneId

// Új tranzakció létrehozásához szolgáló felület
@ExperimentalMaterial3Api
@Composable
fun CreateTransactionScreen(
    onNavigateBack: () -> Unit,
    loadedId: Int = -1,
    viewModel: CreateTransactionViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    var showDialog by remember { mutableStateOf(false) }
    val hostState = remember { SnackbarHostState() }

    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    // Minden felhasználói változtatáskor leellenőrzi, hogy sikeres volt-e
    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect { uiEvent ->
            when(uiEvent) {
                is UiEvent.Success -> {
                    onNavigateBack()
                }
                is UiEvent.Failure -> {
                    scope.launch {
                        hostState.showSnackbar(uiEvent.message.asString(context))
                    }
                }
            }
        }
    }

    //Ha szerkesztés miatt nyitjuk meg az oldalt, akkor betölti a megadott tranzakciót
    //A kulcs nem változik meg szóval ez csak egyszer történik meg
    LaunchedEffect(key1 = loadedId) {
        if(loadedId != -1) {
            viewModel.loadTransaction(loadedId)
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState)},
        // Mentés
        floatingActionButton = {
            LargeFloatingActionButton(
                onClick = { viewModel.onEvent(CreateTransactionEvent.SaveTransaction) },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ) { Icon(imageVector = Icons.Default.Done, contentDescription = null) }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentAlignment = Alignment.Center
        ) {
            //Tranzakció szerkesztésének megjelenítése
            TransactionEditor(
                priceValue = state.transaction.price,
                priceOnValueChange = { viewModel.onEvent(CreateTransactionEvent.ChangePrice(it)) },
                descriptionValue = state.transaction.description,
                descriptionOnValueChange = { viewModel.onEvent(CreateTransactionEvent.ChangeDescription(it)) },
                selectedCategory = state.transaction.category,
                onCategoryChange = { viewModel.onEvent(CreateTransactionEvent.ChangeCategory(it)) },
                selectedPayType = state.transaction.payType,
                onPayTypeChange = { viewModel.onEvent(CreateTransactionEvent.ChangePayType(it)) },
                isExpense = state.transaction.isExpense,
                onExpenseChange = { viewModel.onEvent(CreateTransactionEvent.ChangeExpense(it)) },
                dateValue = state.transaction.date.toLocalDate(),
                onDatePickerClicked = { showDialog = true },
            )
        }
        //Dátum kiválasztásához szolgáló dialógus ablak
        if (showDialog) {
            Box() {
                val datePickerState = rememberDatePickerState()
                DatePickerDialog(
                    onDismissRequest = { showDialog = false },
                    confirmButton = {
                        TextButton(
                            onClick = {
                                if(datePickerState.selectedDateMillis != null) {
                                    showDialog = false
                                    val date =
                                        Instant.ofEpochMilli(datePickerState.selectedDateMillis!!)
                                            .atZone(ZoneId.systemDefault()).toLocalDateTime()
                                    viewModel.onEvent(
                                        CreateTransactionEvent.ChangeDate(
                                            LocalDate(
                                                date.year,
                                                date.month,
                                                date.dayOfMonth
                                            )
                                        )
                                    )
                                }
                                else {
                                    Toast.makeText(context,
                                        context.getString(R.string.date_picker_no_date_selected_toast), Toast.LENGTH_SHORT).show()
                                }
                            }
                        ) {
                            Text(stringResource(R.string.dialog_ok_button_text))
                        }
                    },
                    dismissButton = {
                        TextButton(
                            onClick = { showDialog = false }
                        ) {
                            Text(stringResource(R.string.dialog_cancel_button_text))
                        }
                    }
                ) {
                    DatePicker(state = datePickerState)
                }
            }
        }
    }
}