/*
 * Copyright (c) 2015 David Boissier
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.codinjutsu.tools.nosql.redis.model;

import org.apache.commons.lang3.StringUtils;
import org.codinjutsu.tools.nosql.commons.model.internal.layer.DatabaseElement;
import org.codinjutsu.tools.nosql.redis.model.internal.RedisDatabaseHash;
import org.codinjutsu.tools.nosql.redis.model.internal.RedisDatabaseList;
import org.codinjutsu.tools.nosql.redis.model.internal.RedisDatabaseSet;
import org.codinjutsu.tools.nosql.redis.model.internal.RedisDatabaseSortedSet;
import org.codinjutsu.tools.nosql.redis.model.internal.RedisDatabaseString;
import redis.clients.jedis.Jedis;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Collectors;

public enum RedisKeyType {

    LIST("list") {
        @Override
        public DatabaseElement toDatabaseElement(Jedis jedis, String key) {
            List<RedisDatabaseString> list = jedis.lrange(key, 0, -1)
                    .stream()
                    .map(RedisDatabaseString::new)
                    .collect(Collectors.toList());
            return new RedisDatabaseList(list);
        }
    },
    SET("set") { //NON-NLS

        @Override
        public DatabaseElement toDatabaseElement(Jedis jedis, String key) {
            Set<? extends DatabaseElement> values = jedis.smembers(key)
                    .stream()
                    .map(RedisDatabaseString::new)
                    .collect(Collectors.toSet());
            return new RedisDatabaseSet(values);
        }
    },
    ZSET("zset") { //NON-NLS

        @Override
        public DatabaseElement toDatabaseElement(Jedis jedis, String key) {
            SortedSet<DatabaseElement> values = jedis.zrangeByScoreWithScores(key, "-inf", "+inf") //NON-NLS
                    .stream()
                    .map(tuple -> new RedisDatabaseString(tuple.getElement()))
                    .collect(Collectors.toCollection(TreeSet::new));
            return new RedisDatabaseSortedSet(values);
        }
    },
    HASH("hash") { //NON-NLS

        @Override
        public DatabaseElement toDatabaseElement(Jedis jedis, String key) {
            Map<String, DatabaseElement> result = jedis.hgetAll(key)
                    .entrySet()
                    .stream()
                    .collect(Collectors.toMap(Map.Entry::getKey, entry -> new RedisDatabaseString(entry.getValue()), (a, b) -> b));
            return new RedisDatabaseHash(result);
        }
    },
    STRING("string") { //NON-NLS

        @Override
        public DatabaseElement toDatabaseElement(Jedis jedis, String key) {
            return new RedisDatabaseString(jedis.get(key));
        }
    };
    public final String label;

    RedisKeyType(String label) {
        this.label = label;
    }

    public abstract DatabaseElement toDatabaseElement(Jedis jedis, String key);

    public static RedisKeyType getKeyType(String type) {
        for (RedisKeyType keyType : RedisKeyType.values()) {
            if (StringUtils.equals(type, keyType.label)) {
                return keyType;
            }
        }
        return null;
    }
}
