package hu.bme.aut.android.moneytracker

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.provider.Telephony
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import dagger.hilt.android.AndroidEntryPoint
import hu.bme.aut.android.moneytracker.domain.model.Category
import hu.bme.aut.android.moneytracker.domain.model.PayType
import hu.bme.aut.android.moneytracker.domain.model.Transaction
import hu.bme.aut.android.moneytracker.domain.usecases.LoadTransactionsUseCase
import hu.bme.aut.android.moneytracker.domain.usecases.SaveTransactionUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import java.time.Instant
import java.time.ZoneId
import javax.inject.Inject

// Broadcast receiver SMS-ek fogadására, feldolgozására és ez alapján cselekvésekre
@AndroidEntryPoint
class SmsReceiver: BroadcastReceiver() {

    // Nincs szükség az összes UseCase-re ezért csak ezeket injektáltam
    @Inject
    lateinit var saveTransactionUseCase: SaveTransactionUseCase

    @Inject
    lateinit var loadTransactionsUseCase: LoadTransactionsUseCase

    // SMS-ek fogadása
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Telephony.Sms.Intents.SMS_RECEIVED_ACTION) {
            val smsMessages = Telephony.Sms.Intents.getMessagesFromIntent(intent)
            // SharedPreferences a Settings képernyőhöz hasonlóan lehetne injektálva is.
            val sharedPreferences =
                context.getSharedPreferences(context.getString(R.string.shared_preferences_name), Context.MODE_PRIVATE)
            val smsPhoneNumber = sharedPreferences.getString(context.getString(R.string.shared_preferences_sms_number), "") ?: ""
            val minBalance = sharedPreferences.getInt(context.getString(R.string.shared_preferences_min_balance), 0)
            smsPhoneNumber.replace("\\s".toRegex(), "")
            val smsTemplatePostfix = sharedPreferences.getString(context.getString(R.string.shared_preferences_sms_postfix), "")

            for (sms in smsMessages) {
                val sender = sms.displayOriginatingAddress
                val messageBody = sms.messageBody
                // Szűrés az előre megadott telefonszámra
                if (sender == smsPhoneNumber && !smsTemplatePostfix.isNullOrEmpty()) {
                    // Postfix alapján összeg keresése üzenetben
                    val amount = extractAmountFromMessage(messageBody, smsTemplatePostfix)
                    if (amount != null) {
                        CoroutineScope(Dispatchers.IO).launch {
                            //Amennyiben talált, akkor kezeli az új tranzakciót
                            handleTransaction(amount, minBalance, context)
                        }
                    }
                }
            }
        }
    }

    // Üzenet feldolgozása, postfix segítségével összeg kinyerése
    private fun extractAmountFromMessage(message: String, smsTemplatePostfix: String): Int? {
        val postfixIndex = message.indexOf(smsTemplatePostfix)
        if (postfixIndex == -1) {
            return null
        }

        var amountString = ""
        var currentIndex = postfixIndex - 1

        while (currentIndex >= 0) {
            val currentChar = message[currentIndex]
            if (currentChar.isDigit() || currentChar == ' ') {
                amountString = currentChar + amountString
                currentIndex--
            } else {
                break
            }
        }

        return amountString.replace(" ", "").toIntOrNull()
    }

    // Új tranzakció meglétének kezelése
    private suspend fun handleTransaction(amount: Int, minBalance: Int, context: Context) {
        val date =
            Instant.ofEpochMilli(Clock.System.now().toEpochMilliseconds())
                .atZone(ZoneId.systemDefault()).toLocalDateTime()
        // Tranzakció létrehozása
        val transaction = Transaction(
            id = 0,
            price = amount,
            isExpense = true,
            category = Category.OTHER,
            payType = PayType.CARD,
            date = LocalDate(date.year, date.month, date.dayOfMonth),
            description = context.getString(R.string.sms_transaction_default_description)
        )
        // Tranzakció mentése
        saveTransactionUseCase(transaction)
        // Ha lehet értesítést küldeni
        if (ContextCompat.checkSelfPermission(
                context, android.Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            try {
                val transactions = loadTransactionsUseCase().getOrThrow()
                val balance = getBalance(transactions)
                // És az egyenleg elérte a kritikus szintet
                if (balance <= minBalance) {
                    // Akkor értesítést küldünk
                    sendNotification(balance, context)
                }
            } catch (e: Exception) {
                Log.e("SmsReceiver", "Error loading transactions", e)
            }
        }
    }

    // Egyenleg lekérdezése
    private fun getBalance(transactions: List<Transaction>): Int {
        return transactions.sumOf {
            if (it.isExpense) -it.price else it.price
        }
    }

    // Értesítés küldése alacsony egyenlegről
    // Hasonlóan lehetne implementálni a createTransaction-be is,
    // de mivel ott manuálisan hozza létre úgyis látja az egyenlegét
    // Ha viszont kártyás tranzakció miatt történik ez, akkor van értelme értesítést küldeni
    private fun sendNotification(balance: Int, context: Context) {
        // Értesítéskezelő létrehozása vagy lekérdezése ha már létezik
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        // Értesítés üzenete
        val notificationText = context.getString(R.string.notification_text, balance)+" "+context.getString(R.string.currency_symbol)
        // Kommunikációs csatorna beállítása
        val channel = NotificationChannel(
            context.getString(R.string.balance_notification_channel_id),
            context.getString(R.string.balance_notification_channel_name),
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description = context.getString(R.string.balance_notification_channel_description)
        }
        // és létrehozása a kezelővel
        notificationManager.createNotificationChannel(channel)

        // Értesítés felépítésének meghatározása
        val notification = NotificationCompat.Builder(context, context.getString(R.string.balance_notification_channel_id))
            .setContentTitle(context.getString(R.string.balance_notification_title))
            .setContentText(notificationText)
            .setSmallIcon(R.drawable.ic_notification)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .build()

        // Értesítés küldése a felhasználónak
        notificationManager.notify(1, notification)
    }
}