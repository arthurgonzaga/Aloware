//
//  AlowareViewModel.swift
//  SwiftVoiceQuickstart
//
//  Created by arthur gonzaga on 03/04/25.
//  Copyright © 2025 Twilio, Inc. All rights reserved.
//

import Combine

final class AlowareViewModel: VoiceObserver {
    
    func call(to id: String) {
        state.id = id
        performStartCallAction()
    }
    
    func answer() {
        // Nothing for now
    }

    func hangup() {
        userInitiatedDisconnect = true
        performEndCallAction(uuid: activeCall?.uuid!)
    }

    func toggleMute() {
        guard let activeCall = getActiveCall() else { return }

        activeCall.isMuted.toggle()
        state.isMuted = activeCall.isMuted
    }

    func toggleHold() {
        // Nothing for now
    }
}
