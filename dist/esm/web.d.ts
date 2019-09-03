import { WebPlugin } from '@capacitor/core';
import { InitializationOptions, IRealmPlugin, KeyOnlyOptions, KeyStringValueOptions, StringValueResults } from './definitions';
export declare class CapacitorStorageRealmPluginWeb extends WebPlugin implements IRealmPlugin {
    constructor();
    initialize(options: InitializationOptions): Promise<void>;
    clear(): Promise<void>;
    get(options: KeyOnlyOptions): Promise<StringValueResults | void>;
    keys(): Promise<{
        keys: Array<string>;
    } | void>;
    remove(options: KeyOnlyOptions): Promise<void>;
    set(options: KeyStringValueOptions): Promise<void>;
    private createNotAvailablePromise;
}
declare const CapacitorStorageRealmPlugin: CapacitorStorageRealmPluginWeb;
export { CapacitorStorageRealmPlugin };
