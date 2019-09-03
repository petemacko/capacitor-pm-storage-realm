//
//  ConfigRecord.swift
//  RealmPlugin
//
//  Created by pete on 8/5/19.
//  Copyright © 2019 Max Lynch. All rights reserved.
//

import RealmSwift

@objcMembers
class  ConfigRecord: Object {
    dynamic var name: String = ""
    dynamic var value: String = ""
    
    override static func primaryKey() -> String? {
        return "name"
    }
}
