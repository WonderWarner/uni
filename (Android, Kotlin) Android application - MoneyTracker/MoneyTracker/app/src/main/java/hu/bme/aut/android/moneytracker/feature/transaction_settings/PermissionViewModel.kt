package hu.bme.aut.android.moneytracker.feature.transaction_settings

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import hu.bme.aut.android.moneytracker.R
import javax.inject.Inject

// Beállítások engedélykezelését elérhetővé tévő viewmodel
@HiltViewModel
class PermissionsViewModel @Inject constructor(
    @ApplicationContext private val context: Context
) : ViewModel() {

    // Megadták-e az engedélyt -> megfigyelhető
    private val _permissionGranted = MutableLiveData<Boolean>()
    // Szükséges engedélyek
    private val permissions = arrayOf(
        Manifest.permission.READ_SMS,
        Manifest.permission.RECEIVE_SMS,
        Manifest.permission.POST_NOTIFICATIONS
    )

    // Engedélyek meglétének ellenőrzése
    fun checkAndRequestPermissions(activity: Activity) {

        val permissionsToRequest = permissions.filter {
            ContextCompat.checkSelfPermission(context, it) != PackageManager.PERMISSION_GRANTED
        }

        // Ha még van nem engedélyezett permission
        if (permissionsToRequest.isNotEmpty()) {
            val permissionsToAskAgain = permissionsToRequest.filter {
                ActivityCompat.shouldShowRequestPermissionRationale(activity, it)
            }
            // Amihez a Rationale megjelenítése még nem lett letiltva
            if(permissionsToAskAgain.isNotEmpty()) {
                // Akkor megjelenítjük neki
                showPermissionRationaleDialog(activity)
            }
            // Elkérjük az engedélyt
            ActivityCompat.requestPermissions(
                activity,
                permissionsToRequest.toTypedArray(),
                SMS_PERMISSION_REQUEST_CODE
            )
        } else {
            _permissionGranted.value = true
        }
    }

    companion object {
        const val SMS_PERMISSION_REQUEST_CODE = 101
    }

    // Figyelmeztető ablak az engedélyek jelentősségéről
    private fun showPermissionRationaleDialog(activity: Activity) {
        AlertDialog.Builder(activity)
            .setTitle(context.getString(R.string.permission_rationale_title))
            .setMessage(context.getString(R.string.permission_rationale_message))
            .setPositiveButton(context.getString(R.string.permission_rationale_approve)) { _, _ ->
                // Ha elfogadja a figyelmeztető ablak üzenetét, újra megkérdezzük
                ActivityCompat.requestPermissions(
                    activity,
                    permissions,
                    SMS_PERMISSION_REQUEST_CODE
                )
            }
            .setNegativeButton(context.getString(R.string.permission_rationale_decline)) { dialog, _ ->
                dialog.dismiss()
            }
            .setCancelable(false)
            .show()
    }
}
