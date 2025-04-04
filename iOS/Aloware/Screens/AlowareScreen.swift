//
//  AlowareScreen.swift
//  SwiftVoiceQuickstart
//
//  Created by arthur gonzaga on 03/04/25.
//  Copyright © 2025 Twilio, Inc. All rights reserved.
//

import SwiftUI
import Combine
import AVFAudio

struct AlowareScreen: View {
    
    @ObservedObject private var viewModel: AlowareViewModel
    
    var showMicrophoneAccessRequest: () -> Void
    
    init(viewModel: AlowareViewModel, showMicrophoneAccessRequest: @escaping () -> Void) {
        _viewModel = ObservedObject(wrappedValue: viewModel)
        self.showMicrophoneAccessRequest = showMicrophoneAccessRequest
    }
    
    var body: some View {
        
        switch viewModel.currentNavigation {
        case .initial:
            InitialScreen(
                viewModel: viewModel,
                onCallButtonClick: { id in
                    checkRecordPermission { permissionGranted in
                        guard permissionGranted else {
                            showMicrophoneAccessRequest()
                            return
                        }
                        
                        viewModel.call(to: id)
                    }
                }
            )
        case .call:
            CallScreen(
                viewModel: viewModel,
                onHangUpButtonClick: {
                    viewModel.hangup()
                },
                onToggleMuteClick: {
                    viewModel.toggleMute()
                }
            )
        }
    }
    
    func checkRecordPermission(completion: @escaping (_ permissionGranted: Bool) -> Void) {
        let permissionStatus = AVAudioSession.sharedInstance().recordPermission
        
        switch permissionStatus {
        case .granted:
            // Record permission already granted.
            completion(true)
        case .denied:
            // Record permission denied.
            completion(false)
        case .undetermined:
            // Requesting record permission.
            // Optional: pop up app dialog to let the users know if they want to request.
            AVAudioSession.sharedInstance().requestRecordPermission { granted in completion(granted) }
        default:
            completion(false)
        }
    }
    
}
