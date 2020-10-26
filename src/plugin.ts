import {Plugins} from "@capacitor/core";
import {
    KeyOnlyOptions,
    KeyStringValueOptions,
    StringValueResults
} from './definitions';

const {CapacitorStorageRealmPlugin} = Plugins;

export class StorageRealm {
    async initialize(name: string = 'settings', encryptionKeyString: string = ''): Promise<void> {
        return CapacitorStorageRealmPlugin.initialize({name, encryptionKeyString});
    }

    async clear(): Promise<void> {
        return CapacitorStorageRealmPlugin.clear();
    }

    async get(options: KeyOnlyOptions): Promise<StringValueResults | void> {
        return CapacitorStorageRealmPlugin.get(options);
    }

    async keys(): Promise<{ keys: Array<string> } | void> {
        return CapacitorStorageRealmPlugin.keys();
    }

    async remove(options: KeyOnlyOptions): Promise<void> {
        return CapacitorStorageRealmPlugin.remove(options);
    }

    async set(options: KeyStringValueOptions): Promise<void> {
        return CapacitorStorageRealmPlugin.set(options);
    }
}
