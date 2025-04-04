//
//  SwiftUIViewController.swift
//  Banese
//
//  Created by arthur gonzaga on 17/03/24.
//  Copyright © 2024 Popcode Mobile Solutions. All rights reserved.
//

import Foundation
import SwiftUI

open class SwiftUIViewController<Content>: UIViewController where Content : View {
    private var swiftViewBottomConstraint: NSLayoutConstraint? = nil

    deinit {
        UIScrollView.appearance().bounces = true
    }
    
    func setView(
        shouldObserveKeyboardChanges: Bool = true,
        backgroundColor: UIColor = .clear,
        keyboardPaddingBottom: CGFloat = 0.0,
        createContent: () -> Content
    ) {
        UIScrollView.appearance().bounces = false
        
        let swiftUIView = SwiftUIView(shouldObserveKeyboardChanges: shouldObserveKeyboardChanges)
        swiftUIView.setView(
            backgroundColor: backgroundColor,
            keyboardPaddingBottom: keyboardPaddingBottom,
            createContent
        )
        
        view.addSubview(swiftUIView)
        setupContraints(swiftUIView)
    }

    private func setupContraints(_ swiftUIView: SwiftUIView) {
        NSLayoutConstraint.activate([
            swiftUIView.leadingAnchor.constraint(equalTo: view.leadingAnchor),
            swiftUIView.trailingAnchor.constraint(equalTo: view.trailingAnchor),
            swiftUIView.topAnchor.constraint(equalTo: view.topAnchor),
            swiftUIView.bottomAnchor.constraint(equalTo: view.bottomAnchor)
        ])
    }

}

