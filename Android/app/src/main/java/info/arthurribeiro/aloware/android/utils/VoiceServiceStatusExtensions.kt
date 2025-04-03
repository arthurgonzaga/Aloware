package info.arthurribeiro.aloware.android.utils

import info.arthurribeiro.aloware.android.services.VoiceService

val VoiceService.Status.currentCall: VoiceService.Status.CallRecord?
    get() = callMap[activeCall]
