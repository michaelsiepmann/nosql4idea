package org.codinjutsu.tools.nosql.couchbase.model;

import org.codinjutsu.tools.nosql.commons.configuration.ServerConfiguration;
import org.codinjutsu.tools.nosql.commons.model.DatabaseContext;
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
