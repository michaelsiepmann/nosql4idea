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

package org.codinjutsu.tools.nosql.mongo.model;

import org.codinjutsu.tools.nosql.commons.model.Database;
import org.codinjutsu.tools.nosql.commons.model.Folder;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.SortedSet;
import java.util.TreeSet;

public class MongoDatabase extends Database implements Folder<MongoCollection> {

    private final SortedSet<MongoCollection> collections = new TreeSet<MongoCollection>();

    public MongoDatabase(String name) {
        super(name);
    }

    @NotNull
    public String getName() {
        return super.getName();
    }

    @Override
    @NotNull
    public Collection<MongoCollection> getChildFolders() {
        return collections;
    }

    public void addCollection(MongoCollection mongoCollection) {
        collections.add(mongoCollection);
    }
}
