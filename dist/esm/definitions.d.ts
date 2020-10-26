declare module "@capacitor/core" {
    interface PluginRegistry {
        CapacitorStorageRealmPlugin: IRealmPlugin;
    }
}
export interface InitializationOptions {
    name: string;
    encryptionKeyString: string;
}
export interface KeyOnlyOptions {
    name: string;
}
export interface KeyStringValueOptions {
    name: string;
    value: string;
}
export interface StringValueResults {
    value: string;
}
export interface IRealmPlugin {
    initialize(options: InitializationOptions): Promise<void>;
    clear(): Promise<void>;
    get(options: KeyOnlyOptions): Promise<StringValueResults | void>;
    keys(): Promise<{
        keys: Array<string>;
    } | void>;
    remove(options: KeyOnlyOptions): Promise<void>;
    set(options: KeyStringValueOptions): Promise<void>;
}
