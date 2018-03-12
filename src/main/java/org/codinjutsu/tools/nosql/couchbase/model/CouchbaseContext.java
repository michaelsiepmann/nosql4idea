package org.codinjutsu.tools.nosql.couchbase.model;

import org.codinjutsu.tools.nosql.commons.configuration.ServerConfiguration;
import org.codinjutsu.tools.nosql.commons.model.AbstractDatabaseContext;
import org.codinjutsu.tools.nosql.commons.model.Database;
import org.codinjutsu.tools.nosql.couchbase.logic.CouchbaseClient;
import org.jetbrains.annotations.NotNull;

public class CouchbaseContext extends AbstractDatabaseContext {

    private final Database database;

    public CouchbaseContext(CouchbaseClient couchbaseClient, @NotNull ServerConfiguration serverConfiguration, Database database) {
        super(couchbaseClient, serverConfiguration);
        this.database = database;
    }

    public Database getDatabase() {
        return database;
    }
}
