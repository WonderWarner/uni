package hu.bme.aut.android.moneytracker.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import hu.bme.aut.android.moneytracker.R
import hu.bme.aut.android.moneytracker.domain.model.Category
import hu.bme.aut.android.moneytracker.domain.model.PayType
import hu.bme.aut.android.moneytracker.ui.model.CategoryUi
import hu.bme.aut.android.moneytracker.ui.model.PayTypeUi
import hu.bme.aut.android.moneytracker.ui.model.asCategoryUi
import hu.bme.aut.android.moneytracker.ui.model.asPayTypeUi
import kotlinx.datetime.LocalDate

@Composable
fun TransactionEditor(
    priceValue: Int,
    priceOnValueChange: (Int) -> Unit,
    descriptionValue: String,
    descriptionOnValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    categories: List<CategoryUi> = Category.categories.map { it.asCategoryUi() },
    selectedCategory: CategoryUi,
    onCategoryChange: (CategoryUi) -> Unit,
    payTypes: List<PayTypeUi> = PayType.payTypes.map { it.asPayTypeUi() },
    selectedPayType: PayTypeUi,
    onPayTypeChange: (PayTypeUi) -> Unit,
    isExpense: Boolean,
    onExpenseChange: (Boolean) -> Unit,
    dateValue: LocalDate,
    onDatePickerClicked: () -> Unit,
    enabled: Boolean = true
) {
    val fraction = 0.8f
    val keyboardController = LocalSoftwareKeyboardController.current

    Column(
       modifier = modifier.background(MaterialTheme.colorScheme.secondaryContainer).padding(top = 20.dp).fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth(fraction)) {
            NormalTextField(
                value = priceValue.toString(),
                label = stringResource(id = R.string.textfield_label_price),
                onValueChange = { priceOnValueChange(it.toIntOrNull() ?: 0) },
                singleLine = true,
                trailingIcon = { Text(text = stringResource(id = R.string.currency_symbol), color = Color.Black)},
                onDone = { keyboardController?.hide() },
                modifier = Modifier.weight(1f),
                enabled = enabled
            )
            Spacer(modifier = Modifier.width(10.dp))
            ExpenseSelector(
                value = isExpense,
                onValueChange = { onExpenseChange(it) },
                modifier = Modifier.weight(1f),
                enabled = enabled
            )
        }
        Spacer(modifier = Modifier.height(10.dp))
        Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth(fraction)){
            PayTypeDropDown(
                payTypes = payTypes,
                selectedPayType = selectedPayType,
                onPayTypeChange = { onPayTypeChange(it) },
                modifier = Modifier.weight(1f),
                enabled = enabled
            )
            CategoryDropDown(
                categories = categories,
                selectedCategory = selectedCategory,
                onCategoryChange = { onCategoryChange(it) },
                modifier = Modifier.weight(1f),
                enabled = enabled
            )
        }
        Spacer(modifier = Modifier.height(10.dp))
        DatePicker(
            pickedDate = dateValue,
            onClick = onDatePickerClicked,
            modifier = Modifier
                .fillMaxWidth(fraction),
            enabled = enabled
        )
        Spacer(modifier = Modifier.height(10.dp))
        NormalTextField(
            value = descriptionValue,
            label = stringResource(id = R.string.textfield_label_description),
            onValueChange = { descriptionOnValueChange(it) },
            singleLine = false,
            onDone = { keyboardController?.hide() },
            modifier = Modifier
                .fillMaxWidth(fraction)
                .padding(bottom = 5.dp),
            enabled = enabled
        )
        Spacer(modifier = Modifier.height(30.dp))
    }
}