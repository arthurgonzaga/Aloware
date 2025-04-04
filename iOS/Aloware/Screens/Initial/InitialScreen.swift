//
//  InitialScreen.swift
//  SwiftVoiceQuickstart
//
//  Created by arthur gonzaga on 03/04/25.
//  Copyright © 2025 Twilio, Inc. All rights reserved.
//
import SwiftUI

struct InitialScreen: View {
    
    @ObservedObject private var viewModel: AlowareViewModel
    
    private let onCallButtonClick: (String) -> Void
    
    init(viewModel: AlowareViewModel, onCallButtonClick: @escaping (String) -> Void) {
        self.viewModel = viewModel
        self.onCallButtonClick = onCallButtonClick
    }
    
    var body: some View {
        VStack(alignment: .leading) {
            Text("Welcome to")
                .foregroundColor(.white)
                .font(.system(size: 32.0))
                .padding(.top, 80.0)
            
            Text("Aloware")
                .foregroundColor(Color.green)
                .font(.system(size: 50.0, weight: .black))
                .bold()
            
            TextField("", text: $viewModel.state.id)
                .padding()
                .background(Color.white.opacity(0.1))
                .cornerRadius(50)
                .foregroundColor(.white)
            
            Text("Quick Call")
                .padding(.top, 16)
                .foregroundColor(.white)
                .font(.system(size: 18, weight: .medium))
            
            HStack(spacing: 20) {
                QuickCallButton(name: "Arthur\nAndroid") {
                    onCallButtonClick("arthur-android")
                }
                
                QuickCallButton(name: "Adrielly\nAndroid") {
                    onCallButtonClick("drielly-android")
                }
            }
            
            Spacer()
            
            Button(
                action: {
                    onCallButtonClick(viewModel.state.id)
                }
            ) {
                Text("Start Call")
                    .frame(maxWidth: .infinity)
                    .padding()
                    .background(Color.blue)
                    .foregroundColor(.white)
                    .cornerRadius(50)
                    .padding(.bottom, 40)
            }
        }
        .padding(.horizontal, 16)
        .ignoresSafeArea()
        .background(Color.darkBlue)
    }
}

struct QuickCallButton: View {
    let name: String
    let onClick: () -> Void
    
    var body: some View {
        VStack {
            Circle()
                .fill(Color.white.opacity(0.2))
                .frame(width: 50, height: 50)
            
            Text(name)
                .foregroundColor(.white)
                .multilineTextAlignment(.center)
                .font(.system(size: 14))
        }
        .onTapGesture {
            onClick()
        }
    }
}

struct InitialScreen_Previews: PreviewProvider {
    static var previews: some View {
        InitialScreen(
            viewModel: AlowareViewModel(),
            onCallButtonClick: { _ in }
        )
    }
}
