package info.arthurribeiro.aloware.android.ui

import com.twilio.voice.ConnectOptions
import info.arthurribeiro.aloware.android.BuildConfig
import kotlinx.coroutines.flow.asStateFlow

class AlowareViewModel : VoiceObserverViewModel() {
    val uiState = _uiState.asStateFlow()
    val currentScreen = _currentScreen.asStateFlow()

    fun call(to: String) {
        withVoiceService {
            connectCall(
                ConnectOptions.Builder(BuildConfig.ACCESS_TOKEN)
                    .params(mapOf("to" to to))
                    .build(),
            )
        }
    }

    fun answer() {
        withVoiceService { acceptCall(status.pendingCalls.keys.first()) }
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
