//
//  SwiftUIView.swift
//  Banese
//
//  Created by arthur gonzaga on 09/04/24.
//  Copyright © 2024 Popcode Mobile Solutions. All rights reserved.
//

import SwiftUI

open class SwiftUIView: UIView {
    private var swiftViewBottomConstraint: NSLayoutConstraint? = nil
    
    private var view: UIView? = nil
    private var shouldObserveKeyboardChanges: Bool = true
    private var keyboardPaddingBottom: CGFloat = 0.0
        
    init(shouldObserveKeyboardChanges: Bool = true) {
        super.init(frame: .zero)
        if shouldObserveKeyboardChanges {
            registerNotifications()
        }
    }
    
    
    deinit {
        NotificationCenter.default.removeObserver(self)
    }
    
    func setView(
        backgroundColor: UIColor = .clear,
        keyboardPaddingBottom: CGFloat = 0.0,
        _ setView: () -> some View
    ) {
        self.keyboardPaddingBottom = keyboardPaddingBottom
        self.view = UIHostingController(rootView: setView()).view
        
        if let view {
            self.backgroundColor = backgroundColor
            view.backgroundColor = backgroundColor
            self.addSubview(view)
            
            self.translatesAutoresizingMaskIntoConstraints = false
            view.translatesAutoresizingMaskIntoConstraints = false
            
            swiftViewBottomConstraint = view.bottomAnchor.constraint(equalTo: bottomAnchor)
            
            NSLayoutConstraint.activate([
                view.centerYAnchor.constraint(equalTo: centerYAnchor),
                view.centerXAnchor.constraint(equalTo: centerXAnchor),
                view.topAnchor.constraint(equalTo: safeAreaLayoutGuide.topAnchor),
                swiftViewBottomConstraint!,
                view.leadingAnchor.constraint(equalTo: leadingAnchor),
                view.trailingAnchor.constraint(equalTo: trailingAnchor)
            ])
        }
    }
    
    public required init?(coder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }
    
    
    private func registerNotifications() {
        NotificationCenter.default.addObserver(self, selector: #selector(self.keyboardWillShow), name: UIResponder.keyboardWillShowNotification, object: nil)
        NotificationCenter.default.addObserver(self, selector: #selector(self.keyboardWillHide), name: UIResponder.keyboardWillHideNotification, object: nil)
    }

    @objc func keyboardWillShow(notification: NSNotification) {
        let keyboardSize = (notification.userInfo? [UIResponder.keyboardFrameEndUserInfoKey] as? NSValue)?.cgRectValue

        let keyboardHeight = keyboardSize?.height

        if let view = self.view {
            swiftViewBottomConstraint?.constant = (keyboardHeight! - view.safeAreaInsets.bottom + keyboardPaddingBottom) * -1

            view.setNeedsLayout()
        }
    }

    @objc func keyboardWillHide(notification: NSNotification) {
        self.swiftViewBottomConstraint?.constant = 0 // or change according to your logic

        self.view?.setNeedsLayout()
    }
}
