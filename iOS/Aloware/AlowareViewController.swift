//
//  AlowareViewController.swift
//  Twilio Voice Quickstart - Swift
//
//  Copyright © 2016 Twilio, Inc. All rights reserved.
//

import UIKit
import AVFoundation
import PushKit
import CallKit
import TwilioVoice

let kRegistrationTTLInDays = 365

let kCachedDeviceToken = "CachedDeviceToken"
let kCachedBindingDate = "CachedBindingDate"

class AlowareViewController: SwiftUIViewController<AlowareScreen> {

    var viewModel: AlowareViewModel = AlowareViewModel()

    override func viewDidLoad() {
        super.viewDidLoad()
        viewModel.configure()
        
        setView {
            AlowareScreen(
                viewModel: viewModel,
                showMicrophoneAccessRequest: showMicrophoneAccessRequest
            )
        }

    }
    
    func showMicrophoneAccessRequest() {
        let alertController = UIAlertController(title: "Voice Quick Start",
                                                message: "Microphone permission not granted",
                                                preferredStyle: .alert)
        
        let goToSettings = UIAlertAction(title: "Settings", style: .default) { _ in
            UIApplication.shared.open(URL(string: UIApplication.openSettingsURLString)!,
                                      options: [UIApplication.OpenExternalURLOptionsKey.universalLinksOnly: false],
                                      completionHandler: nil)
        }
        
        
        [goToSettings].forEach { alertController.addAction($0) }
        
        present(alertController, animated: true, completion: nil)
    }
}
