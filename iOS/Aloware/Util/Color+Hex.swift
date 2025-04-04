//
//  Color+Hex.swift
//  SwiftVoiceQuickstart
//
//  Created by arthur gonzaga on 03/04/25.
//  Copyright © 2025 Twilio, Inc. All rights reserved.
//
import SwiftUI

extension Color {
    init?(hex: String, alpha: Double? = nil) {
        var hexDigits = hex.first == "#" ? String(hex.dropFirst()) : hex
        for digit in hexDigits {
            guard "0123456789abcdefABCDEF".contains(digit) else { return nil }
        }
        if hexDigits.count == 2 { // grayscale
            hexDigits += hexDigits + hexDigits
        }
        if hexDigits.count <= 4 { // #rgb or #rgba
            hexDigits = hexDigits.map { "\($0)\($0)" } .joined(separator: "")
        }
        if hexDigits.count == 6 { // #rrggbb
            hexDigits += "ff"
        }
        // #rrggbbaa
        guard hexDigits.count == 8 else { return nil }
        let digits = Array(hexDigits)
        let rgba = stride(from: 0, to: 8, by: 2).map {
            Double(Int("\(digits[$0])\(digits[$0 + 1])", radix: 16)!) / 255.0
        }
        self = Color(.sRGB, red: rgba[0], green: rgba[1], blue: rgba[2], opacity: alpha ?? rgba[3])
    }
}
