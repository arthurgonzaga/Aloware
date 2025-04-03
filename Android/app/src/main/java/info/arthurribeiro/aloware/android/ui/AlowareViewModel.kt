package info.arthurribeiro.aloware.android.ui

import com.twilio.voice.ConnectOptions
import info.arthurribeiro.aloware.android.AlowareApplication.Companion.ACCESS_TOKEN
import kotlinx.coroutines.flow.asStateFlow

class AlowareViewModel : VoiceObserverViewModel() {
    val uiState = _uiState.asStateFlow()
    val currentScreen = _currentScreen.asStateFlow()

    fun call(to: String) {
        withVoiceService {
            connectCall(
                ConnectOptions.Builder(ACCESS_TOKEN)
                    .params(mapOf("to" to to))
                    .build(),
            )
        }
    }

    fun hangup() {
        withVoiceService { disconnectCall(status.activeCall) }
    }

    fun toggleMute() {
        withVoiceService { muteCall(status.activeCall) }
    }

    fun toggleHold() {
        withVoiceService { holdCall(status.activeCall) }
    }

    fun setFCMToken(token: String?) {
        withVoiceService { registerFCMToken(token ?: return@withVoiceService) }
    }
}
