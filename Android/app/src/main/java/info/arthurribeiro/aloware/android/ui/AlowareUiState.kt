package info.arthurribeiro.aloware.android.ui

import com.twilio.voice.CallInvite
import java.io.Serializable

public data class AlowareUiState(
    val isOnHold: Boolean = false,
    val isMuted: Boolean = false,
    val callInvite: CallInvite? = null,
    val isLoading: Boolean = false,
    val error: String? = null
) : Serializable {
    val isRinging get() = callInvite != null
}
