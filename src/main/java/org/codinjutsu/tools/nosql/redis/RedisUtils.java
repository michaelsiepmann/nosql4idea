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

package org.codinjutsu.tools.nosql.redis;

import redis.clients.jedis.Tuple;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import static java.lang.String.format;
import static java.util.stream.Collectors.toCollection;
import static org.apache.commons.lang3.StringUtils.join;

public class RedisUtils {

    public static String stringifySortedSet(Set<Tuple> sortedSet) {
        List<String> stringifiedTuples = sortedSet.stream().map(RedisUtils::stringifyTuple).collect(toCollection(LinkedList::new));
        return format("{%s}", join(stringifiedTuples, ", ")); //NON-NLS
    }

    public static String stringifySet(Set set) {
        return format("{%s}", join(set, ", ")); //NON-NLS
    }

    public static String stringifyTuple(Tuple tuple) {
        return format("(%s, %s)", tuple.getElement(), tuple.getScore()); //NON-NLS
    }
}
