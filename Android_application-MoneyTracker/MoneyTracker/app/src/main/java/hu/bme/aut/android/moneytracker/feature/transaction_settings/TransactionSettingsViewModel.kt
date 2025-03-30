package hu.bme.aut.android.moneytracker.feature.transaction_settings

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import hu.bme.aut.android.moneytracker.R
import hu.bme.aut.android.moneytracker.domain.di.StringProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

// Beállítások ideiglenes és perzisztens tárolásához szükséges viewmodel
@HiltViewModel
class TransactionSettingsViewModel @Inject constructor(
    private val sharedPreferences: SharedPreferences,
    private val stringProvider: StringProvider
) : ViewModel() {
    private val _state = MutableStateFlow(SettingsState())
    val state = _state.asStateFlow()

    // SharedPreferences-ből a beállításokat betölti a képernyőre
    fun loadSettings() {
        val minBalance = sharedPreferences.getInt(stringProvider.getString(R.string.shared_preferences_min_balance), 0)
        val smsPhoneNumber = sharedPreferences.getString(stringProvider.getString(R.string.shared_preferences_sms_number), "") ?: ""
        val smsTemplatePostfix = sharedPreferences.getString(stringProvider.getString(R.string.shared_preferences_sms_postfix), "") ?: ""

        _state.update {
            it.copy(
                minBalance = minBalance,
                smsPhoneNumber = smsPhoneNumber,
                smsTemplatePostfix = smsTemplatePostfix
            )
        }
    }

    // A képernyőn megadott beállításokat elmenti SharedPreferences-be
    fun saveSettings(minBalance: Int, smsPhoneNumber: String, smsTemplatePostfix: String) {
        sharedPreferences.edit()
            .putInt(stringProvider.getString(R.string.shared_preferences_min_balance), minBalance)
            .putString(stringProvider.getString(R.string.shared_preferences_sms_number), smsPhoneNumber)
            .putString(stringProvider.getString(R.string.shared_preferences_sms_postfix), smsTemplatePostfix)
            .apply()

        _state.update {
            it.copy(
                minBalance = minBalance,
                smsPhoneNumber = smsPhoneNumber,
                smsTemplatePostfix = smsTemplatePostfix
            )
        }
    }
}

// Beállítások állapotát tároló osztály
data class SettingsState(
    val minBalance: Int = 0,
    val smsPhoneNumber: String = "",
    val smsTemplatePostfix: String = ""
)