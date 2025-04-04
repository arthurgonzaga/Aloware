//
//  Screens.swift
//  SwiftVoiceQuickstart
//
//  Created by arthur gonzaga on 03/04/25.
//  Copyright © 2025 Twilio, Inc. All rights reserved.
//
import SwiftUI

struct CallScreen: View {
    
    @ObservedObject private var viewModel: AlowareViewModel
    
    private let onHangUpButtonClick: () -> Void
    private let onToggleMuteClick: () -> Void
    
    init(viewModel: AlowareViewModel, onHangUpButtonClick: @escaping () -> Void, onToggleMuteClick: @escaping () -> Void) {
        self.viewModel = viewModel
        self.onHangUpButtonClick = onHangUpButtonClick
        self.onToggleMuteClick = onToggleMuteClick
    }
    
    var body: some View {
        VStack {
            Spacer()
            Text(viewModel.activeCall?.from ?? "Unknown")
                .foregroundColor(.darkBlue)
                .font(.title)
                .bold()
            Spacer()
            HStack(spacing: 80) {
                CircleButton(color: .black, icon: viewModel.state.isMuted ? "mic.slash.fill" : "mic.fill") {
                    onToggleMuteClick()
                }
                CircleButton(color: .red, icon: "phone.down.fill") {
                    onHangUpButtonClick()
                }
            }
            .padding(.bottom, 16)
        }
        .frame(maxWidth: .infinity, maxHeight: .infinity)
        .background(Color.white.edgesIgnoringSafeArea(.all))
    }
}

struct CircleButton: View {
    let color: Color
    @State var icon: String
    
    let onClick: () -> Void
    
    var body: some View {
        ZStack {
            Circle()
                .fill(color)
                .frame(width: 50, height: 50)
                .onTapGesture {
                    onClick()
                }
        
            Image(systemName: icon)
                .renderingMode(.template)
                .foregroundColor(.white)
                .frame(width: 24, height: 24)
        }
    }
}

struct CallScreen_Previews: PreviewProvider {
    static var previews: some View {
        CallScreen(
            viewModel: AlowareViewModel(),
            onHangUpButtonClick: {},
            onToggleMuteClick: {}
        )
    }
}
