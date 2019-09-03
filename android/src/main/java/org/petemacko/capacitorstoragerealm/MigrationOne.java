package org.petemacko.capacitorstoragerealm;

import io.realm.DynamicRealm;
import io.realm.FieldAttribute;
import io.realm.RealmMigration;
import io.realm.RealmSchema;

public class MigrationOne implements RealmMigration {
    @Override
    public void migrate(DynamicRealm realm, long oldVersion, long newVersion) {

        RealmSchema schema = realm.getSchema();

        if (oldVersion == 0) {
            schema.create("ConfigRecord")
                    .addField("name", String.class, FieldAttribute.PRIMARY_KEY)
                    .addField("value", String.class);
            oldVersion++;
        }
    }
}
