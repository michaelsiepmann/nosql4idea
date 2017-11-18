package org.codinjutsu.tools.nosql.couchbase.view;

import org.codinjutsu.tools.nosql.ServerConfiguration;
import org.codinjutsu.tools.nosql.commons.view.DatabaseContext;
import org.codinjutsu.tools.nosql.couchbase.logic.CouchbaseClient;
import org.codinjutsu.tools.nosql.couchbase.model.CouchbaseDatabase;
import org.jetbrains.annotations.NotNull;

public class CouchbaseContext extends DatabaseContext<CouchbaseClient> {

    private final CouchbaseDatabase database;

    public CouchbaseContext(CouchbaseClient couchbaseClient, @NotNull ServerConfiguration serverConfiguration, CouchbaseDatabase database) {
        super(couchbaseClient, serverConfiguration);
        this.database = database;
    }

    public CouchbaseDatabase getDatabase() {
        return database;
    }
}
