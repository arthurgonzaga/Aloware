//
//  AlowareUiState.swift
//  SwiftVoiceQuickstart
//
//  Created by arthur gonzaga on 03/04/25.
//  Copyright © 2025 Twilio, Inc. All rights reserved.
//
import CallKit
import TwilioVoice

struct AlowareUiState {
    var isOnHold: Bool = false
    var isMuted: Bool = false
    var isLoading: Bool = false
    var callInvite: CallInvite? = nil
    var id: String = ""
}
