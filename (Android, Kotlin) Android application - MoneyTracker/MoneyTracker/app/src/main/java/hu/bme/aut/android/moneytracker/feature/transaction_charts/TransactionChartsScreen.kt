package hu.bme.aut.android.moneytracker.feature.transaction_charts

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircleOutline
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import co.yml.charts.axis.AxisData
import co.yml.charts.axis.DataCategoryOptions
import co.yml.charts.common.model.PlotType
import co.yml.charts.common.model.Point
import co.yml.charts.ui.barchart.BarChart
import co.yml.charts.ui.barchart.models.BarChartData
import co.yml.charts.ui.barchart.models.BarChartType
import co.yml.charts.ui.barchart.models.BarData
import co.yml.charts.ui.barchart.models.BarStyle
import co.yml.charts.ui.barchart.models.SelectionHighlightData
import co.yml.charts.ui.piechart.charts.PieChart
import co.yml.charts.ui.piechart.models.PieChartConfig
import co.yml.charts.ui.piechart.models.PieChartData
import hu.bme.aut.android.moneytracker.R
import hu.bme.aut.android.moneytracker.domain.model.Category
import hu.bme.aut.android.moneytracker.domain.model.PayType
import hu.bme.aut.android.moneytracker.ui.common.CategoryDropDown
import hu.bme.aut.android.moneytracker.ui.common.ExpenseSelector
import hu.bme.aut.android.moneytracker.ui.common.PayTypeDropDown
import hu.bme.aut.android.moneytracker.ui.common.DatePicker as MyDatePicker
import hu.bme.aut.android.moneytracker.ui.common.ScreenNavigatorTopAppBar
import hu.bme.aut.android.moneytracker.ui.model.asCategoryUi
import hu.bme.aut.android.moneytracker.ui.model.asPayTypeUi
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.toLocalDate
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import kotlin.random.Random

// Diagramok megjelenítéséért szolgáló felület
@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun TransactionChartsScreen(
    onSettingsClicked: () -> Unit,
    onNavigateToTransactions: () -> Unit,
    viewModel: TransactionChartsViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    var showStartDateDialog by remember { mutableStateOf(false) }
    var showEndDateDialog by remember { mutableStateOf(false) }
    val date =
        Instant.ofEpochMilli(Clock.System.now().toEpochMilliseconds())
            .atZone(ZoneId.systemDefault()).toLocalDateTime()
    val today = LocalDate(date.year, date.month, date.dayOfMonth)

    // Első kompozíciókor betölti a tranzakciókat
    LaunchedEffect(key1 = Unit){
        viewModel.loadTransactions()
    }

    Scaffold(
        topBar = {
            // Listanézettel egységes TopBar
            ScreenNavigatorTopAppBar(
                title = stringResource(id = R.string.transaction_charts_title),
                onSettingsClicked = onSettingsClicked,
                onNavigateToOtherScreen = onNavigateToTransactions,
                otherScreenIcon = Icons.Default.AddCircleOutline,
                otherScreenDescription = stringResource(id = R.string.app_name),
            )
        },
        modifier = Modifier.fillMaxSize()
    ) { padding ->
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxSize().padding(padding)
        ) {
            // Szűréshez és diagram kategória beállításához használható elemek
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = stringResource(R.string.chart_filter_title),
                    fontWeight = FontWeight.Bold
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(selected = state.selectedFilter == ChartFilter.NONE, onClick = {
                        viewModel.updateFilter(ChartFilter.NONE)
                    })
                    Text(text = stringResource(R.string.chart_filter_none))
                    Spacer(modifier = Modifier.width(8.dp))
                    RadioButton(selected = state.selectedFilter == ChartFilter.CATEGORY, onClick = {
                        viewModel.updateFilter(ChartFilter.CATEGORY)
                    })
                    Text(text = stringResource(R.string.chart_filter_category))
                    Spacer(modifier = Modifier.width(8.dp))
                    RadioButton(selected = state.selectedFilter == ChartFilter.PAY_TYPE, onClick = {
                        viewModel.updateFilter(ChartFilter.PAY_TYPE)
                    })
                    Text(text = stringResource(R.string.chart_filter_paytype))
                }
                Spacer(modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = stringResource(R.string.chart_filter_category))
                    Spacer(modifier = Modifier.width(4.dp))
                    CategoryDropDown(
                        categories = Category.categories.map { it.asCategoryUi() },
                        selectedCategory = state.selectedCategory ?: Category.categories.first()
                            .asCategoryUi(),
                        enabled = state.selectedFilter == ChartFilter.CATEGORY,
                        onCategoryChange = { viewModel.updateCategory(it) }
                    )
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = stringResource(R.string.chart_filter_paytype))
                    Spacer(modifier = Modifier.width(4.dp))
                    PayTypeDropDown(
                        payTypes = PayType.payTypes.map { it.asPayTypeUi() },
                        selectedPayType = state.selectedPayType ?: PayType.payTypes.first()
                            .asPayTypeUi(),
                        enabled = state.selectedFilter == ChartFilter.PAY_TYPE,
                        onPayTypeChange = { viewModel.updatePayType(it) }
                    )
                }
            }
            // Diagramok megjelenítéséért szolgáló felület
            Column {
                // Ha nincs filter beállítva, akkor adott időtartományba eső Bevétel/Kiadás arányt jelenít meg kördiagramként
                when (state.selectedFilter) {
                    ChartFilter.NONE -> {
                        val expenseIncomeRate =
                            viewModel.getIncomeAndExpense(viewModel.filterTransactions())
                        if(expenseIncomeRate.isNotEmpty()) {
                            val pieChartData = PieChartData(
                                slices = listOf(
                                    PieChartData.Slice(
                                        stringResource(R.string.textfield_label_expense),
                                        expenseIncomeRate[0].toFloat(),
                                        Color.Red
                                    ),
                                    PieChartData.Slice(
                                        stringResource(R.string.textfield_label_income),
                                        expenseIncomeRate[1].toFloat(),
                                        Color.Green
                                    )
                                ),
                                plotType = PlotType.Pie
                            )
                            val pieChartConfig = PieChartConfig(
                                backgroundColor = Color.Transparent,
                                labelType = PieChartConfig.LabelType.VALUE,
                                isAnimationEnable = true,
                                labelVisible = true,
                                sliceLabelTextSize = TextUnit(20f, TextUnitType.Sp),
                                animationDuration = 1000,
                                sliceLabelTextColor = Color.Black,
                                inActiveSliceAlpha = .8f,
                                activeSliceAlpha = 1.0f
                            )
                            PieChart(
                                modifier = Modifier
                                    .width(320.dp)
                                    .height(320.dp),
                                pieChartData,
                                pieChartConfig
                            )
                        }
                    }
                    // Ha kategóriára szűr, akkor a kiválasztott kategória kiadásait vagy bevételeit jeleníti meg fizetési eszközönként lebontva körcikkdiagramként
                    ChartFilter.CATEGORY -> {
                        val payTypeAmounts = viewModel.getTransactionsGroupByPayType(viewModel.filterTransactions())
                        if(payTypeAmounts.isNotEmpty()) {
                            val sliceList : List<PieChartData.Slice> = payTypeAmounts
                                .map { (payType, amount) ->
                                    PieChartData.Slice(payType, amount.toFloat(), Color(Random.nextFloat(), Random.nextFloat(), Random.nextFloat()))
                                }
                            val pieChartData = PieChartData(
                                slices = sliceList,
                                plotType = PlotType.Donut
                            )
                            val pieChartConfig = PieChartConfig(
                                labelVisible = false,
                                sliceLabelTextSize = TextUnit(22f, TextUnitType.Sp),
                                sliceLabelTextColor = MaterialTheme.colorScheme.onBackground,
                                backgroundColor = Color.Transparent,
                                labelType = PieChartConfig.LabelType.VALUE,
                                isAnimationEnable = true,
                                animationDuration = 1000,
                                inActiveSliceAlpha = .8f,
                                activeSliceAlpha = 1.0f,
                            )
                            val totalAmount = sliceList.sumOf { it.value.toInt() }.toString()
                            Box(contentAlignment = Alignment.Center) {
                                PieChart(
                                    modifier = Modifier
                                        .width(320.dp)
                                        .height(320.dp),
                                    pieChartData,
                                    pieChartConfig
                                )
                                Text(text = totalAmount, fontSize = 30.sp, color = MaterialTheme.colorScheme.onBackground, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                    // Ha a fizetési eszközre szűr akkor megjeleníti a kiválasztott fizetési eszköz általi kiadást/bevételt kategóriánként csoportosítva oszlopdiagram formájában
                    else -> {
                        val categoryAmounts = viewModel.getTransactionsGroupByCategory(viewModel.filterTransactions())
                        if(categoryAmounts.isNotEmpty()) {
                            val indexedCategoryAmounts = categoryAmounts.mapIndexed { index, value -> index to value }
                            val maxRange = categoryAmounts.maxOf { it.second }
                            val barData : List<BarData> = indexedCategoryAmounts.map { (index, value) ->
                                BarData(label = value.first, point = Point(value.second.toFloat(), index.toFloat()), color = Color(0xFF00BCD4))
                            }
                            val xStepSize = 5
                            val xAxisData = AxisData.Builder()
                                .axisLabelColor(MaterialTheme.colorScheme.onBackground)
                                .axisLineColor(MaterialTheme.colorScheme.onBackground)
                                .backgroundColor(Color.Transparent)
                                .steps(xStepSize)
                                .bottomPadding(12.dp)
                                .endPadding(40.dp)
                                .labelData { index -> (index * (maxRange / xStepSize)).toString() }
                                .build()
                            val yAxisData = AxisData.Builder()
                                .axisLabelColor(MaterialTheme.colorScheme.onBackground)
                                .axisLineColor(MaterialTheme.colorScheme.onBackground)
                                .backgroundColor(Color.Transparent)
                                .axisStepSize(30.dp)
                                .steps(barData.size - 1)
                                .labelAndAxisLinePadding(20.dp)
                                .axisOffset(20.dp)
                                .setDataCategoryOptions(
                                    DataCategoryOptions(
                                        isDataCategoryInYAxis = true,
                                        isDataCategoryStartFromBottom = false
                                    )
                                )
                                .startDrawPadding(48.dp)
                                .labelData { index -> barData[index].label }
                                .build()
                            val barChartData = BarChartData(
                                backgroundColor = Color.Transparent,
                                chartData = barData,
                                xAxisData = xAxisData,
                                yAxisData = yAxisData,
                                barStyle = BarStyle(
                                    isGradientEnabled = false,
                                    paddingBetweenBars = 20.dp,
                                    barWidth = 35.dp,
                                    selectionHighlightData = SelectionHighlightData(
                                        highlightBarColor = Color.Cyan,
                                        highlightTextBackgroundColor = Color.LightGray,
                                        popUpLabel = { x, _ -> " Value : ${"%.0f".format(x)} " },
                                        barChartType = BarChartType.HORIZONTAL
                                    ),
                                ),
                                showYAxis = true,
                                showXAxis = true,
                                horizontalExtraSpace = 20.dp,
                                barChartType = BarChartType.HORIZONTAL
                            )
                            BarChart(
                                modifier = Modifier.height(380.dp),
                                barChartData = barChartData
                            )
                        }
                    }
                }
                // További szűrést segítő elemek
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    ExpenseSelector(
                        value = state.isExpenseOnly,
                        onValueChange = { viewModel.updateExpenseOnly(it) },
                        enabled = state.selectedFilter != ChartFilter.NONE
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = stringResource(R.string.chart_start_date))
                        MyDatePicker(
                            pickedDate = state.startDate?.toLocalDate() ?: today,
                            onClick = { showStartDateDialog = true }
                        )
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = stringResource(R.string.chart_end_date))
                        MyDatePicker(
                            pickedDate = state.endDate?.toLocalDate() ?: today,
                            onClick = { showEndDateDialog = true }
                        )
                    }
                }
            }
        }
        // Kezdeti dátum választó dialógus
        if (showStartDateDialog) {
            Box {
                val datePickerState = rememberDatePickerState()
                DatePickerDialog(
                    onDismissRequest = { showStartDateDialog = false },
                    confirmButton = {
                        TextButton(
                            onClick = {
                                showStartDateDialog = false
                                val selectedDate = datePickerState.selectedDateMillis
                                    ?.let { Instant.ofEpochMilli(it).atZone(ZoneId.systemDefault()).toLocalDateTime() }
                                    ?: LocalDateTime.now()
                                val dayDate = LocalDate(
                                    selectedDate.year,
                                    selectedDate.month,
                                    selectedDate.dayOfMonth
                                )
                                viewModel.updateStartDate(dayDate.toString())
                            }
                        ) {
                            Text(stringResource(R.string.dialog_ok_button_text))
                        }
                    },
                    dismissButton = {
                        TextButton(
                            onClick = { showStartDateDialog = false }
                        ) {
                            Text(stringResource(R.string.dialog_cancel_button_text))
                        }
                    }
                ) {
                    DatePicker(state = datePickerState)
                }
            }
        }
        // Befejező dátum választó dialógus
        if (showEndDateDialog) {
            Box {
                val datePickerState = rememberDatePickerState()
                DatePickerDialog(
                    onDismissRequest = { showEndDateDialog = false },
                    confirmButton = {
                        TextButton(
                            onClick = {
                                showEndDateDialog = false
                                val selectedDate = datePickerState.selectedDateMillis
                                    ?.let { Instant.ofEpochMilli(it).atZone(ZoneId.systemDefault()).toLocalDateTime() }
                                    ?: LocalDateTime.now()
                                val dayDate = LocalDate(
                                    selectedDate.year,
                                    selectedDate.month,
                                    selectedDate.dayOfMonth
                                )
                                viewModel.updateEndDate(dayDate.toString())
                            }
                        ) {
                            Text(stringResource(R.string.dialog_ok_button_text))
                        }
                    },
                    dismissButton = {
                        TextButton(
                            onClick = { showEndDateDialog = false }
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
