package info.arthurribeiro.aloware.android.ui

import android.content.Intent
import androidx.core.util.Consumer
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.twilio.voice.Call
import com.twilio.voice.CallException
import com.twilio.voice.CallInvite
import com.twilio.voice.ConnectOptions
import com.twilio.voice.RegistrationException
import info.arthurribeiro.aloware.android.AlowareApplication.Companion.voiceService
import info.arthurribeiro.aloware.android.services.VoiceService
import info.arthurribeiro.aloware.android.ui.enums.Screen
import info.arthurribeiro.aloware.android.utils.Constants
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID

abstract class VoiceObserverViewModel :
    ViewModel(),
    DefaultLifecycleObserver,
    VoiceService.Observer,
    Consumer<Intent> {
    protected val _uiState: MutableStateFlow<AlowareUiState> = MutableStateFlow(AlowareUiState())
    protected val _currentScreen = MutableStateFlow(Screen.Initial)

    init {
        withVoiceService {
            registerObserver(this@VoiceObserverViewModel)
        }
    }

    override fun connectCall(
        callId: UUID,
        options: ConnectOptions,
    ) {
        _currentScreen.value = Screen.Call
    }

    override fun disconnectCall(callId: UUID) {
        _currentScreen.value = Screen.Initial
        _uiState.update { AlowareUiState() }
    }

    override fun acceptIncomingCall(callId: UUID) {
        _currentScreen.value = Screen.Call
    }

    override fun rejectIncomingCall(callId: UUID) {
        _currentScreen.value = Screen.Initial
    }

    override fun incomingCall(
        callId: UUID,
        callInvite: CallInvite,
    ) {
        _uiState.update {
            it.copy(
                callInvite = callInvite,
            )
        }
    }

    override fun cancelledCall(callId: UUID) {
        _currentScreen.value = Screen.Initial
    }

    override fun muteCall(
        callId: UUID,
        isMuted: Boolean,
    ) {
        _uiState.update { it.copy(isMuted = isMuted) }
    }

    override fun holdCall(
        callId: UUID,
        isOnHold: Boolean,
    ) {
        _uiState.update { it.copy(isOnHold = isOnHold) }
    }

    override fun onConnectFailure(
        callId: UUID,
        callException: CallException,
    ) {
        _currentScreen.value = Screen.Initial
        _uiState.update { it.copy(error = callException.message) }
    }

    override fun onConnected(callId: UUID) {
        _currentScreen.value = Screen.Call
        _uiState.update { it.copy(isLoading = false) }
    }

    override fun onReconnecting(
        callId: UUID,
        callException: CallException,
    ) {
        _currentScreen.value = Screen.Call
        _uiState.update { it.copy(isLoading = true) }
    }

    override fun onReconnected(callId: UUID) {
        _uiState.update { it.copy(isLoading = false) }
    }

    override fun onDisconnected(
        callId: UUID,
        callException: CallException?,
    ) {
        _currentScreen.value = Screen.Initial
        _uiState.update {
            it.copy(isLoading = true, error = callException?.message)
        }
    }

    override fun onCallQualityWarningsChanged(
        callId: UUID,
        currentWarnings: MutableSet<Call.CallQualityWarning>,
        previousWarnings: MutableSet<Call.CallQualityWarning>,
    ) {
        // Not used
    }

    override fun accept(value: Intent) {
        if (value.action != null) {
            val action = value.action
            val pendingCallId = value.getSerializableExtra(Constants.CALL_UUID) as UUID? ?: return
            val callInvite =
                value.getParcelableExtra(Constants.INCOMING_CALL_INVITE) as CallInvite? ?: return

            when (action) {
                Constants.ACTION_INCOMING_CALL_NOTIFICATION -> {
                    incomingCall(pendingCallId, callInvite)
                }

                Constants.ACTION_ACCEPT_CALL -> {
                    withVoiceService {
                        acceptCall(pendingCallId)
                    }
                }

                else -> {}
            }
        }
    }

    override fun registrationSuccessful(fcmToken: String) {
        // Not used
    }

    override fun registrationFailed(registrationException: RegistrationException) {
        // Not used
    }

    override fun onRinging(callId: UUID) {
        // Not used
    }

    protected fun withVoiceService(block: suspend VoiceService.() -> Unit) {
        voiceService { voiceService ->
            viewModelScope.launch {
                voiceService?.block()
            }
        }
    }
}
