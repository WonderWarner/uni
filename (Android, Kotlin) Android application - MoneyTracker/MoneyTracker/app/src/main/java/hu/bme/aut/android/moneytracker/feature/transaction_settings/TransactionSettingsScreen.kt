package hu.bme.aut.android.moneytracker.feature.transaction_settings

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import hu.bme.aut.android.moneytracker.R

// Extra funkciók beállításának felülete
@Composable
fun TransactionSettingsScreen(
    onSaved: () -> Unit,
    settingsViewModel: TransactionSettingsViewModel = hiltViewModel(),
    permissionsViewModel: PermissionsViewModel = hiltViewModel()
) {
    val state by settingsViewModel.state.collectAsState()
    val context = LocalContext.current

    var minBalance by remember { mutableStateOf(state.minBalance.toString()) }
    var smsPhoneNumber by remember { mutableStateOf(state.smsPhoneNumber) }
    var smsTemplatePostfix by remember { mutableStateOf(state.smsTemplatePostfix) }

    // A képernyő megnyitásakor betölti a beállításokat
    LaunchedEffect(Unit) {
        settingsViewModel.loadSettings()
    }

    // Minden állapotváltozáskor frissíti a képernyőt
    LaunchedEffect(state) {
        minBalance = state.minBalance.toString()
        smsPhoneNumber = state.smsPhoneNumber
        smsTemplatePostfix = state.smsTemplatePostfix
    }

    // Szükséges elemek az adatok megadásához
    Box(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
        Column(modifier = Modifier.padding(16.dp).fillMaxSize()) {
            Text(
                text = stringResource(R.string.transaction_settings_title),
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(bottom = 8.dp),
                maxLines = 1,
                fontStyle = MaterialTheme.typography.titleLarge.fontStyle,
                fontSize = MaterialTheme.typography.titleLarge.fontSize,
                fontWeight = MaterialTheme.typography.titleLarge.fontWeight,
            )
            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = stringResource(R.string.settings_min_balance_description),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onBackground
            )
            TextField(
                value = minBalance,
                onValueChange = { minBalance = it },
                label = { Text(stringResource(R.string.settings_textlabel_min_balance)) },
                trailingIcon = { Text(stringResource(R.string.currency_symbol)) },
                placeholder = { Text(stringResource(R.string.settings_placeholder_min_balance)) }
            )
            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = stringResource(R.string.settings_sms_phone_number_description),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onBackground
            )
            TextField(
                value = smsPhoneNumber,
                onValueChange = { smsPhoneNumber = it },
                label = { Text(stringResource(R.string.settings_textlabel_sms_phone_number)) },
                placeholder = { Text(stringResource(R.string.settings_placeholder_sms_phone_number)) }
            )
            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = stringResource(R.string.settings_sms_template_postfix_description),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onBackground
            )
            TextField(
                value = smsTemplatePostfix,
                onValueChange = { smsTemplatePostfix = it },
                label = { Text(stringResource(R.string.settings_textlabel_sms_template_postfix)) },
                placeholder = { Text(stringResource(R.string.settings_placeholder_sms_template_postfix)) }
            )
            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    settingsViewModel.saveSettings(
                        minBalance = minBalance.toIntOrNull() ?: 0,
                        smsPhoneNumber = smsPhoneNumber,
                        smsTemplatePostfix = smsTemplatePostfix
                    )
                    permissionsViewModel.checkAndRequestPermissions(context as Activity)
                    onSaved()
                },
                modifier = Modifier
                    .padding(bottom = 8.dp)
                    .height(60.dp)
                    .width(120.dp)
                    .align(Alignment.End),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                )
            ) {
                Text(
                    text = stringResource(R.string.settings_save_button),
                    fontWeight = FontWeight.Bold,
                    fontSize = MaterialTheme.typography.titleLarge.fontSize
                )
            }
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = stringResource(R.string.transaction_settings_permission_alert),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onBackground
            )
        }
    }

}
