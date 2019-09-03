package org.petemacko.capacitorstoragerealm;

import io.realm.annotations.RealmModule;

@RealmModule(library = true, classes = { ConfigRecord.class })
public class CapacitorStorageRealmPluginModule {
}
