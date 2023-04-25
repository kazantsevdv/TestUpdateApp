package com.kazantcev.testupdateapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.model.ActivityResult
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.UpdateAvailability

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        checkAppUpdateIMMEDIATE()
    }

    fun checkAppUpdateIMMEDIATE() {

        val appUpdateManager = AppUpdateManagerFactory.create(this)

// Returns an intent object that you use to check for an update.
        val appUpdateInfoTask = appUpdateManager.appUpdateInfo

// Checks that the platform will allow the specified type of update.
        appUpdateInfoTask.addOnSuccessListener { appUpdateInfo ->
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                // This example applies an immediate update. To apply a flexible update
                // instead, pass in AppUpdateType.FLEXIBLE
                && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)
            ) {
                // Request the update.
                Toast.makeText(this, "Доступно обновление", Toast.LENGTH_LONG).show()
                appUpdateManager.startUpdateFlowForResult(
                    // Pass the intent that is returned by 'getAppUpdateInfo()'.
                    appUpdateInfo,
                    // Or 'AppUpdateType.FLEXIBLE' for flexible updates.
                    AppUpdateType.IMMEDIATE,
                    // The current activity making the update request.
                    this,
                    // Include a request code to later monitor this update request.
                    MY_REQUEST_CODE
                )
            }
        }
    }

    companion object {
        const val MY_REQUEST_CODE = 99999
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == MY_REQUEST_CODE) {
            when (resultCode) {
                RESULT_OK -> Toast.makeText(this, "Update OK", Toast.LENGTH_LONG).show()
                RESULT_CANCELED -> Toast.makeText(this, "Update CANCELED", Toast.LENGTH_LONG).show()
                ActivityResult.RESULT_IN_APP_UPDATE_FAILED -> Toast.makeText(
                    this,
                    "Update IN_APP_UPDATE_FAILED",
                    Toast.LENGTH_LONG
                ).show()
                else -> Toast.makeText(this, "Update  $resultCode", Toast.LENGTH_LONG).show()
                // If the update is cancelled or fails,
                // you can request to start the update again.
            }
        }
    }
}

