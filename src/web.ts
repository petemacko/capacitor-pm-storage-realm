import {WebPlugin} from '@capacitor/core';
import {
    InitializationOptions,
    IRealmPlugin,
    KeyOnlyOptions,
    KeyStringValueOptions,
    StringValueResults
} from './definitions';

export class CapacitorStorageRealmPluginWeb extends WebPlugin implements IRealmPlugin {
    constructor() {
        super({
            name: 'CapacitorStorageRealmPlugin',
            platforms: ['web']
        });
    }

    // @ts-ignore
    async initialize(options: InitializationOptions): Promise<void> {
        return this.createNotAvailablePromise();
    }

    async clear(): Promise<void> {
        return this.createNotAvailablePromise();
    }

    // @ts-ignore
    async get(options: KeyOnlyOptions): Promise<StringValueResults  | void> {
        return this.createNotAvailablePromise();
    }

    // @ts-ignore
    async keys(): Promise<{ keys: Array<string> } | void> {
        return this.createNotAvailablePromise();
    }

    // @ts-ignore
    async remove(options: KeyOnlyOptions): Promise<void> {
        return this.createNotAvailablePromise();
    }

    // @ts-ignore
    async set(options: KeyStringValueOptions): Promise<void> {
        return this.createNotAvailablePromise();
    }

    private createNotAvailablePromise(): Promise<void> {
        return new Promise((_resolve, reject) => {
            const msg = "The CapacitorStorageRealmPlugin plugin doesn't currently support 'web'. It only works on iOS/Android native.";
            console.log(msg);
            reject(msg);
        });
    }
}

const CapacitorStorageRealmPlugin = new CapacitorStorageRealmPluginWeb();

export {CapacitorStorageRealmPlugin};

import {registerWebPlugin} from '@capacitor/core';

registerWebPlugin(CapacitorStorageRealmPlugin);
