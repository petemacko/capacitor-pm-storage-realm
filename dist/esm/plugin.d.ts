import { KeyOnlyOptions, KeyStringValueOptions, StringValueResults } from './definitions';
export declare class StorageRealm {
    initialize(name?: string, encryptionKeyString?: string): Promise<void>;
    clear(): Promise<void>;
    get(options: KeyOnlyOptions): Promise<StringValueResults | void>;
    keys(): Promise<{
        keys: Array<string>;
    } | void>;
    remove(options: KeyOnlyOptions): Promise<void>;
    set(options: KeyStringValueOptions): Promise<void>;
}
