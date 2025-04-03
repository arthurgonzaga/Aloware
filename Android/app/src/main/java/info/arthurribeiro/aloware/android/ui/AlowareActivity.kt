package info.arthurribeiro.aloware.android.ui

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Build.VERSION_CODES
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.core.app.ActivityCompat
import com.google.android.gms.tasks.Task
import com.google.firebase.messaging.FirebaseMessaging
import info.arthurribeiro.aloware.android.ui.enums.Screen
import info.arthurribeiro.aloware.android.ui.screens.call.CallScreen
import info.arthurribeiro.aloware.android.ui.screens.initial.InitialScreen
import info.arthurribeiro.aloware.android.ui.theme.AlowareTheme

class AlowareActivity : ComponentActivity() {
    private val viewModel = AlowareViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        addWindowFlags()
        registerObservers()
        registerFCMToken()
        requestPermissions()
        setContent()
        viewModel.accept(intent)
    }

    private fun setContent() {
        setContent {
            AlowareTheme {
                val currentScreen by viewModel.currentScreen.collectAsState()
                val uiState by viewModel.uiState.collectAsState()

                when (currentScreen) {
                    Screen.Initial -> {
                        InitialScreen(
                            uiState = uiState,
                            onCallButtonClick = { id ->
                                viewModel.call(to = id)
                            },
                        )
                    }

                    Screen.Call -> {
                        CallScreen(
                            uiState = uiState,
                        )
                    }
                }
            }
        }
    }

    /**
     *  These flags ensure that the activity can be launched when the screen is locked.
     */
    private fun addWindowFlags() =
        window.addFlags(
            WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                or WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
                or WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
        )

    /**
     *  Make sure the viewmodel receives all the events to handle logic.
     */
    private fun registerObservers() {
        lifecycle.addObserver(viewModel)
        addOnNewIntentListener(viewModel)
    }

    private fun registerFCMToken() {
        FirebaseMessaging.getInstance().token
            .addOnCompleteListener { task: Task<String?> ->
                if (task.isSuccessful) {
                    viewModel.setFCMToken(token = task.result)
                }
            }
    }

    private fun requestPermissions() {
        if (!hasPermissions()) {
            ActivityCompat.requestPermissions(
                this,
                permissionsNeeded().toTypedArray(),
                PERMISSIONS_ALL,
            )
        }
    }

    private fun hasPermissions(): Boolean {
        for (permission in permissionsNeeded()) {
            if (PackageManager.PERMISSION_GRANTED !=
                ActivityCompat.checkSelfPermission(
                    applicationContext,
                    permission,
                )
            ) {
                return false
            }
        }
        return true
    }

    private fun permissionsNeeded() =
        buildList {
            add(Manifest.permission.RECORD_AUDIO)
            if (Build.VERSION.SDK_INT >= VERSION_CODES.TIRAMISU) {
                add(Manifest.permission.POST_NOTIFICATIONS)
            }
        }

    companion object {
        const val PERMISSIONS_ALL = 100
    }
}
