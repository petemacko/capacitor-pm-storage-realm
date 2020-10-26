var __awaiter = (this && this.__awaiter) || function (thisArg, _arguments, P, generator) {
    function adopt(value) { return value instanceof P ? value : new P(function (resolve) { resolve(value); }); }
    return new (P || (P = Promise))(function (resolve, reject) {
        function fulfilled(value) { try { step(generator.next(value)); } catch (e) { reject(e); } }
        function rejected(value) { try { step(generator["throw"](value)); } catch (e) { reject(e); } }
        function step(result) { result.done ? resolve(result.value) : adopt(result.value).then(fulfilled, rejected); }
        step((generator = generator.apply(thisArg, _arguments || [])).next());
    });
};
import { Plugins } from "@capacitor/core";
const { CapacitorStorageRealmPlugin } = Plugins;
export class StorageRealm {
    initialize(name = 'settings', encryptionKeyString = '') {
        return __awaiter(this, void 0, void 0, function* () {
            return CapacitorStorageRealmPlugin.initialize({ name, encryptionKeyString });
        });
    }
    clear() {
        return __awaiter(this, void 0, void 0, function* () {
            return CapacitorStorageRealmPlugin.clear({});
        });
    }
    get(options) {
        return __awaiter(this, void 0, void 0, function* () {
            return CapacitorStorageRealmPlugin.get(options);
        });
    }
    keys() {
        return __awaiter(this, void 0, void 0, function* () {
            return CapacitorStorageRealmPlugin.keys({});
        });
    }
    remove(options) {
        return __awaiter(this, void 0, void 0, function* () {
            return CapacitorStorageRealmPlugin.remove(options);
        });
    }
    set(options) {
        return __awaiter(this, void 0, void 0, function* () {
            return CapacitorStorageRealmPlugin.set(options);
        });
    }
}
//# sourceMappingURL=plugin.js.map