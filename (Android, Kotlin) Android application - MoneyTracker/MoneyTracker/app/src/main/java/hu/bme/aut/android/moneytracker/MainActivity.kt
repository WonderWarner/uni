package hu.bme.aut.android.moneytracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.ExperimentalMaterial3Api
import hu.bme.aut.android.moneytracker.navigation.NavGraph
import dagger.hilt.android.AndroidEntryPoint
import hu.bme.aut.android.moneytracker.ui.theme.MoneyTrackerTheme

// Az alkalmazás fő képernyője, ez felel a navigáció létrehozásáért, hogy a megfelelő nézet legyen látható
@ExperimentalMaterial3Api
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MoneyTrackerTheme {
                NavGraph()
            }
        }
    }

    //Próbálkoztam az engedélyek eredményének feldolgozásával, több-kevesebb sikerrel.
    // Végül szerintem felhasználó-barát maradt az applikáció enélkül is, sőt talán még jobb is így.
    /*
    @Deprecated("This method has been deprecated in favor of using the Activity Result API\n      which brings increased type safety via an {@link ActivityResultContract} and the prebuilt\n      contracts for common intents available in\n      {@link androidx.activity.result.contract.ActivityResultContracts}, provides hooks for\n      testing, and allow receiving results in separate, testable classes independent from your\n      activity. Use\n      {@link #registerForActivityResult(ActivityResultContract, ActivityResultCallback)} passing\n      in a {@link RequestMultiplePermissions} object for the {@link ActivityResultContract} and\n      handling the result in the {@link ActivityResultCallback#onActivityResult(Object) callback}.")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == SMS_PERMISSION_REQUEST_CODE) {
            for ((index, permission) in permissions.withIndex()) {
                if (grantResults[index] != PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
                        showPermissionRationaleDialog(this, permission)
                    }
                }
            }
        }
    }

    private fun showPermissionRationaleDialog(activity: Activity, permission: String) {
        AlertDialog.Builder(activity)
            .setTitle(getString(R.string.permission_rationale_title)+": $permission")
            .setMessage(getString(R.string.permission_rationale_message))
            .setPositiveButton(getString(R.string.permission_rationale_approve)) { _, _ ->
                ActivityCompat.requestPermissions(
                    activity,
                    arrayOf(permission),
                    SMS_PERMISSION_REQUEST_CODE
                )
            }
            .setNegativeButton(getString(R.string.permission_rationale_decline)) { dialog, _ ->
                dialog.dismiss()
            }
            .setCancelable(false)
            .show()
    }*/

}
