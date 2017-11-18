package org.codinjutsu.tools.nosql.redis;

import org.codinjutsu.tools.nosql.ServerConfiguration;
import org.codinjutsu.tools.nosql.commons.view.DatabaseContext;
import org.codinjutsu.tools.nosql.redis.logic.RedisClient;
import org.jetbrains.annotations.NotNull;

public class RedisContext extends DatabaseContext<RedisClient> {

    public RedisContext(RedisClient redisClient, @NotNull ServerConfiguration serverConfiguration) {
        super(redisClient, serverConfiguration);
    }
}
