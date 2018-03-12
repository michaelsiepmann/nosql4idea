package org.codinjutsu.tools.nosql.redis.model;

import org.codinjutsu.tools.nosql.commons.configuration.ServerConfiguration;
import org.codinjutsu.tools.nosql.commons.model.AbstractDatabaseContext;
import org.codinjutsu.tools.nosql.commons.model.Database;
import org.codinjutsu.tools.nosql.redis.logic.RedisClient;
import org.jetbrains.annotations.NotNull;

public class RedisContext extends AbstractDatabaseContext {

    private final Database database;

    public RedisContext(RedisClient redisClient, @NotNull ServerConfiguration serverConfiguration, Database database) {
        super(redisClient, serverConfiguration);
        this.database = database;
    }

    public Database getDatabase() {
        return database;
    }
}
