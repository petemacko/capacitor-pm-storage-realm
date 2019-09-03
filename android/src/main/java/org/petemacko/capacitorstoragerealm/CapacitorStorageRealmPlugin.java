package org.petemacko.capacitorstoragerealm;

import android.util.Log;

import com.getcapacitor.JSObject;
import com.getcapacitor.NativePlugin;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.spec.KeySpec;
import java.util.ArrayList;
import java.util.Arrays;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

@NativePlugin()
public class CapacitorStorageRealmPlugin extends Plugin {

    private static final String TAG = "CapacitorStRealmPlugin";
    private static final Long SchemaVersion = 1L;
    private RealmConfiguration _realmConfiguration = null;

    @PluginMethod()
    public void echo(PluginCall call) {
        String value = call.getString("value");

        JSObject ret = new JSObject();
        ret.put("value", value);
        call.success(ret);
    }

    @PluginMethod()
    public void initialize(PluginCall call) {
        if (this._realmConfiguration != null) {
            call.error("This instance has already been initialized");
            return;
        }

        String name = call.getString("name");
        if (name == null || name.length() == 0) {
            String msg = "'name' must be specified and its length must be > 0";
            Log.e(TAG, msg);
            call.error(msg);
            return;
        }

        String encryptionKeyString = call.getString("encryptionKeyString");
        if (encryptionKeyString != null && encryptionKeyString.length() > 0 && encryptionKeyString.length() < 5) {
            String msg = "If 'encryptionKeyString' is provided, it must be at least 5 characters long";
            Log.e(TAG, msg);
            call.error(msg);
            return;
        }

        byte[] encryptionKey = new byte[0];

        if (encryptionKeyString != null && encryptionKeyString.length() > 0) {
            encryptionKey = CapacitorStorageRealmPlugin.attemptToHash(encryptionKeyString);
        }

        try {
            RealmConfiguration.Builder b = new RealmConfiguration.Builder()
                    .name(name + ".realm")
                    .schemaVersion(SchemaVersion)
                    .modules(new CapacitorStorageRealmPluginModule());

            if (encryptionKey.length > 0)
                b.encryptionKey(encryptionKey);

            RealmConfiguration config = b.build();
            if (wasAbleToOpenDatabaseWithThisConfig(config)) {
                this._realmConfiguration = config;
                call.success();
                return;
            }

            this.deleteFileIfExists(config.getPath());
            if (wasAbleToOpenDatabaseWithThisConfig(config)) {
                this._realmConfiguration = config;
                call.success();
                return;
            }

            String msg = "Failed to open realm database after second attempt. Giving up.";
            Log.e(TAG, msg);
            call.error(msg);
        } catch (Error configureEx) {
            String msg = "Failed to configure Realm database" + configureEx.toString();
            Log.e(TAG, msg);
            call.error(msg);
        }
    }

    @PluginMethod()
    public void clear(PluginCall call) {
        RealmConfiguration realmConfiguration = _realmConfiguration;
        if (realmConfiguration == null) {
            call.error("This instance has not been initialized");
            return;
        }

        try {
            try (Realm realm = Realm.getInstance(realmConfiguration)) {

                final RealmResults<ConfigRecord> r = realm.where(ConfigRecord.class).findAll();
                if (r == null) {
                    call.success();
                    return;
                }

                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        r.deleteAllFromRealm();
                    }
                });
                call.success();
            }
        } catch (Error open1Ex) {
            String msg = "Failed to open Realm database: " + open1Ex.toString();
            Log.e(TAG, msg);
            call.error(msg);
        }
    }

    @PluginMethod()
    public void get(PluginCall call) {
        RealmConfiguration realmConfiguration = _realmConfiguration;
        if (realmConfiguration == null) {
            call.error("This instance has not been initialized");
            return;
        }

        String name = call.getString("name");
        if (name == null || name.length() == 0) {
            String msg = "'name' must be specified and its length must be > 0";
            Log.e(TAG, msg);
            call.error(msg);
            return;
        }

        try {
            try (Realm realm = Realm.getInstance(realmConfiguration)) {

                ConfigRecord r = realm.where(ConfigRecord.class)
                        .equalTo("name", name)
                        .findFirst();

                call.success(
                        new JSObject().put("value", r != null ? r.getValue() : "")
                );
            }
        } catch (Error open1Ex) {
            String msg = "Failed to open Realm database: " + open1Ex.toString();
            Log.e(TAG, msg);
            call.error(msg);
        }
    }

    @PluginMethod()
    public void keys(PluginCall call) {
        RealmConfiguration realmConfiguration = _realmConfiguration;
        if (realmConfiguration == null) {
            call.error("This instance has not been initialized");
            return;
        }

        try {
            try (Realm realm = Realm.getInstance(realmConfiguration)) {

                ArrayList<String> keys = new ArrayList<>();

                RealmResults<ConfigRecord> r = realm.where(ConfigRecord.class).sort("name").findAll();
                for (ConfigRecord c : r) {
                    keys.add(c.getName());
                }

                call.success(new JSObject().put("keys", keys.toArray()));
            }
        } catch (Error open1Ex) {
            String msg = "Failed to open Realm database: " + open1Ex.toString();
            Log.e(TAG, msg);
            call.error(msg);
        }
    }

    @PluginMethod()
    public void remove(PluginCall call) {
        RealmConfiguration realmConfiguration = _realmConfiguration;
        if (realmConfiguration == null) {
            call.error("This instance has not been initialized");
            return;
        }

        final String name = call.getString("name");
        if (name == null || name.length() == 0) {
            String msg = "'name' must be specified and its length must be > 0";
            Log.e(TAG, msg);
            call.error(msg);
            return;
        }

        try {
            try (Realm realm = Realm.getInstance(realmConfiguration)) {

                final ConfigRecord r = realm.where(ConfigRecord.class)
                        .equalTo("name", name)
                        .findFirst();

                if (r == null) {
                    call.success();
                    return;
                }
                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        r.deleteFromRealm();
                    }
                });
                call.success();
            }
        } catch (Error open1Ex) {
            String msg = "Failed to open Realm database: " + open1Ex.toString();
            Log.e(TAG, msg);
            call.error(msg);
        }
    }

    @PluginMethod()
    public void set(PluginCall call) {
        RealmConfiguration realmConfiguration = _realmConfiguration;
        if (realmConfiguration == null) {
            call.error("This instance has not been initialized");
            return;
        }

        final String name = call.getString("name");
        if (name == null || name.length() == 0) {
            String msg = "'name' must be specified and its length must be > 0";
            Log.e(TAG, msg);
            call.error(msg);
            return;
        }

        final String value = call.getString("value");
        if (value == null) {
            String msg = "'value' must be specified and its length must be >= 0";
            Log.e(TAG, msg);
            call.error(msg);
            return;
        }

        try {
            try (Realm realm = Realm.getInstance(realmConfiguration)) {
                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        ConfigRecord r = new ConfigRecord();
                        r.setName(name);
                        r.setValue(value);
                        realm.insertOrUpdate(r);
                    }
                });
                call.success();
            }
        } catch (Error open1Ex) {
            String msg = "Failed to open Realm database: " + open1Ex.toString();
            Log.e(TAG, msg);
            call.error(msg);
        }
    }

    private void deleteFileIfExists(String pathAndFilename) {
        try {
            if (!pathAndFilename.endsWith(".realm")) // Naughty naughty...
                return;

            File file = new File(pathAndFilename);

            if (!file.exists())
                return;

            Log.i(TAG, "Deleting realm file: " + pathAndFilename);
            file.delete();

        } catch (Error ex) {
            Log.e(TAG, "Error while trying to delete existing realm file: " + ex.toString());
        }
    }

    private static byte[] attemptToHash(String input) {
        try {
            SecretKeyFactory f = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            String saltString = "naclNaClArrM4t3y!!11!";
            int iterationCount = 4096;
            int keyLength = 64;
            KeySpec spec = new PBEKeySpec(input.toCharArray(), saltString.getBytes(StandardCharsets.UTF_8), iterationCount, keyLength * 8);
            return f.generateSecret(spec).getEncoded();
        } catch (GeneralSecurityException gex) {
            String msg = "Failed to hash string: " + gex.toString();
            Log.e(TAG, msg);
            return new byte[0];
        }
    }

    private boolean wasAbleToOpenDatabaseWithThisConfig(RealmConfiguration thisConfig) {
        try (Realm ignored = Realm.getInstance(thisConfig)) {
            return true;
        } catch (io.realm.exceptions.RealmFileException irer) {
            Log.e(TAG, "Failed to open Realm database: " + irer.toString());
        } catch (Error err) {
            Log.e(TAG, "Failed to open Realm database: " + err.toString());
        }
        return false;
    }
}
