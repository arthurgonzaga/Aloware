//
//  NKConfigurations.swift
//  Aloware
//
//  Created by arthur gonzaga on 03/04/25.
//  Copyright © 2025 Twilio, Inc. All rights reserved.
//

import Foundation

final class AccessToken {
    
    private static var dictionary: NSDictionary {
        guard let path = Bundle.main.path(forResource: "AccessTokens", ofType: "plist") else {
            fatalError("Not found")
        }
        guard let plist = NSDictionary(contentsOfFile: path) else {
            fatalError("Plist with errors")
        }
        return plist
    }
    
    static func get() -> String {
        dictionary["ARTHUR_IOS_ACCESS_TOKEN"] as? String ?? ""
    }
}
