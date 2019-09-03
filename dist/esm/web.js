var __awaiter = (this && this.__awaiter) || function (thisArg, _arguments, P, generator) {
    return new (P || (P = Promise))(function (resolve, reject) {
        function fulfilled(value) { try { step(generator.next(value)); } catch (e) { reject(e); } }
        function rejected(value) { try { step(generator["throw"](value)); } catch (e) { reject(e); } }
        function step(result) { result.done ? resolve(result.value) : new P(function (resolve) { resolve(result.value); }).then(fulfilled, rejected); }
        step((generator = generator.apply(thisArg, _arguments || [])).next());
    });
};
import { WebPlugin } from '@capacitor/core';
export class CapacitorStorageRealmPluginWeb extends WebPlugin {
    constructor() {
        super({
            name: 'CapacitorStorageRealmPlugin',
            platforms: ['web']
        });
    }
    // @ts-ignore
    initialize(options) {
        return __awaiter(this, void 0, void 0, function* () {
            return this.createNotAvailablePromise();
        });
    }
    clear() {
        return __awaiter(this, void 0, void 0, function* () {
            return this.createNotAvailablePromise();
        });
    }
    // @ts-ignore
    get(options) {
        return __awaiter(this, void 0, void 0, function* () {
            return this.createNotAvailablePromise();
        });
    }
    // @ts-ignore
    keys() {
        return __awaiter(this, void 0, void 0, function* () {
            return this.createNotAvailablePromise();
        });
    }
    // @ts-ignore
    remove(options) {
        return __awaiter(this, void 0, void 0, function* () {
            return this.createNotAvailablePromise();
        });
    }
    // @ts-ignore
    set(options) {
        return __awaiter(this, void 0, void 0, function* () {
            return this.createNotAvailablePromise();
        });
    }
    createNotAvailablePromise() {
        return new Promise((_resolve, reject) => {
            const msg = "The CapacitorStorageRealmPlugin plugin doesn't currently support 'web'. It only works on iOS/Android native.";
            console.log(msg);
            reject(msg);
        });
    }
}
const CapacitorStorageRealmPlugin = new CapacitorStorageRealmPluginWeb();
export { CapacitorStorageRealmPlugin };
import { registerWebPlugin } from '@capacitor/core';
registerWebPlugin(CapacitorStorageRealmPlugin);
//# sourceMappingURL=web.js.map