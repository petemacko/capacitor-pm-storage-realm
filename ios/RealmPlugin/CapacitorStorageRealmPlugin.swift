import Foundation
import Capacitor
import RealmSwift
import CryptoSwift

/**
 * Please read the Capacitor iOS Plugin Development Guide
 * here: https://capacitor.ionicframework.com/docs/plugins/ios
 */
@objc(CapacitorStorageRealmPlugin)
public class CapacitorStorageRealmPlugin: CAPPlugin {
    private var _realmConfiguration: Realm.Configuration?
    private static let SchemaVersion: UInt64 = 1
    
    @objc func initialize(_ call: CAPPluginCall) {
        if let _ = _realmConfiguration {
            call.error("This instance has already been initialized")
            return
        }
        
        let name = call.getString("name") ?? ""
        guard name.count > 0 else {
            call.error("'name' must be specified and its length must be > 0")
            return
        }

        let encryptionKeyString = call.getString("encryptionKeyString") ?? ""
        guard encryptionKeyString.count == 0 || encryptionKeyString.count > 4 else {
            call.error("If 'encryptionKeyString' is provided, it must be at least 5 characters long")
            return
        }

        var encryptionKey: Data?
        if encryptionKeyString.count > 0 {
            let password: Array<UInt8> = Array(encryptionKeyString.utf8)
            let salt: Array<UInt8> = Array("naclNaClArrM4t3y!!11!".utf8)
            do {
                encryptionKey = Data(try PKCS5.PBKDF2(password: password, salt: salt, iterations: 4096, keyLength: 64, variant: .sha256).calculate())
            } catch let e {
                NSLog("Failed to calculate sha256 key array: \(e)")
                call.error("Failed to calculate sha256 key array", e)
                return
            }
        }

        let directories = FileManager().urls(for: FileManager.SearchPathDirectory.documentDirectory, in: FileManager.SearchPathDomainMask.userDomainMask)
        if directories.count < 1 {
            call.error("could not retrieve user documents directory")
            return
        }
        
        let newURL = directories[0].appendingPathComponent("\(name).realm")
        
        let config = Realm.Configuration(
            fileURL: newURL,
            inMemoryIdentifier: nil,
            syncConfiguration: nil,
            encryptionKey: encryptionKey,
            readOnly: false,
            schemaVersion: CapacitorStorageRealmPlugin.SchemaVersion,
            migrationBlock: self.migration,
            deleteRealmIfMigrationNeeded: false,
            objectTypes: [ConfigRecord.self]
        )
        
        // NSLog("initialized with realmdb: \(config.fileURL!)")
        
        do {
            let _ = try Realm(configuration: config)
        } catch let e {
            NSLog("Failed to open Realm database - deleting and trying again: \(e)")
            
            do {
                if FileManager().isWritableFile(atPath: newURL.path) {
                    try FileManager().removeItem(at: newURL)
                }
                
                do {
                    let _ = try Realm(configuration: config)
                } catch let e2 {
                    call.error("Failed to open Realm database", e2)
                    return
                }
                
            } catch let fe {
                NSLog("Failed to remove erroneous realm file: \(fe)")
                return
            }
        }
        
        _realmConfiguration = config
        
        call.success()
    }
    
    @objc func clear(_ call: CAPPluginCall) {
        guard let realmConfiguration = _realmConfiguration else {
            call.error("This instance has not been initialized")
            return
        }
        
        let r: Realm;
        do {
            r = try Realm(configuration: realmConfiguration)
        } catch let e {
            call.error("Failed to open Realm database", e)
            return
        }
        
        let oo = r.objects(ConfigRecord.self).sorted(byKeyPath: "name")
        do {
            try r.write {
                for o in oo {
                    r.delete(o)
                }
            }
        } catch let e {
            call.error("Failed to delete record(s)", e)
            return
        }
        
        call.success()
    }
    
    @objc func get(_ call: CAPPluginCall) {        
        let name = call.getString("name") ?? ""
        guard name.count > 0 else {
            call.error("'name' must be specified and its length must be > 0")
            return
        }
        
        guard let realmConfiguration = _realmConfiguration else {
            call.error("This instance has not been initialized")
            return
        }
        
        let r: Realm;
        do {
            r = try Realm(configuration: realmConfiguration)
        } catch let e {
            call.error("Failed to open Realm database", e)
            return
        }
        
        let o = r.objects(ConfigRecord.self).filter(NSPredicate(format: "name = %@", name)).first
        
        call.success([
            "value": o?.value ?? ""
            ])
    }
    
    @objc func keys(_ call: CAPPluginCall) {
        guard let realmConfiguration = _realmConfiguration else {
            call.error("This instance has not been initialized")
            return
        }
        
        let r: Realm;
        do {
            r = try Realm(configuration: realmConfiguration)
        } catch let e {
            call.error("Failed to open Realm database", e)
            return
        }
        
        let keys = Array(r.objects(ConfigRecord.self).sorted(byKeyPath: "name").compactMap { $0.name })
        
        call.success([
            "keys": keys
            ])
    }
    
    @objc func remove(_ call: CAPPluginCall) {
        let name = call.getString("name") ?? ""
        guard name.count > 0 else {
            call.error("'name' must be specified and its length must be > 0")
            return
        }
        
        guard let realmConfiguration = _realmConfiguration else {
            call.error("This instance has not been initialized")
            return
        }
        
        let r: Realm;
        do {
            r = try Realm(configuration: realmConfiguration)
        } catch let e {
            call.error("Failed to open Realm database", e)
            return
        }
        
        let oo = r.objects(ConfigRecord.self).filter(NSPredicate(format: "name = %@", name))
        
        do {
            try r.write {
                for o in oo {
                    r.delete(o)
                }
            }
        } catch let e {
            call.error("Failed to delete record(s)", e)
            return
        }
        
        call.success()
    }
    
    @objc func set(_ call: CAPPluginCall) {
        
        let name = call.getString("name") ?? ""
        guard name.count > 0 else {
            call.error("'name' must be specified and its length must be > 0")
            return
        }
        
        let value = call.getString("value") ?? ""
        guard value.count >= 0 else {
            call.error("'value' must be specified and its length must be >= 0")
            return
        }
        
        guard let realmConfiguration = _realmConfiguration else {
            call.error("This instance has not been initialized")
            return
        }
        
        let r: Realm;
        do {
            r = try Realm(configuration: realmConfiguration)
        } catch let e {
            call.error("Failed to open Realm database", e)
            return
        }
        
        let o = ConfigRecord()
        o.name = name
        o.value = value
        
        do {
            try r.write {
                r.add(o, update: .modified)
            }
        } catch let e {
            call.error("Failed to open Realm database", e)
            return
        }
        
        call.success()
    }
    
    private func migration(_ migration: Migration, _ oldSchemaVersion: UInt64) {
        if (oldSchemaVersion < 1) {
            
        }
        
        if (oldSchemaVersion < 2) {
            
        }
    }
}
