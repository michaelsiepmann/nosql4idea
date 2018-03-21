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

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;
import org.apache.commons.lang.StringUtils;
import org.codinjutsu.tools.nosql.NoSqlException;
import org.codinjutsu.tools.nosql.commons.view.panel.query.QueryOptions;

import java.util.LinkedList;
import java.util.List;

import static org.apache.commons.lang.StringUtils.isNotBlank;

public class MongoQueryOptions {

    private static final BasicDBObject EMPTY_FILTER = new BasicDBObject();
    private final List<Object> operations = new LinkedList<>();

    private DBObject filter = EMPTY_FILTER;
    private DBObject projection = null;
    private DBObject sort;

    private int resultLimit;

    public MongoQueryOptions(QueryOptions queryOptions) {
        String operations = queryOptions.getOperations();
        if (operations != null) {
            this.operations.clear();
            this.operations.addAll((BasicDBList) JSON.parse(operations));
        }
        setFilter(queryOptions.getFilter());
        String projection = queryOptions.getProjection();
        if (isNotBlank(projection)) {
            try {
                this.projection = (DBObject) JSON.parse(projection);
            } catch (Exception e) {
                throw new NoSqlException("", e);
            }
        }
        String sort = queryOptions.getSort();
        if (isNotBlank(sort)) {
            this.sort = (DBObject) JSON.parse(sort);
        }
        resultLimit = queryOptions.getResultLimit();
    }

    public boolean isAggregate() {
        return !operations.isEmpty();
    }

    public List getOperations() {
        return operations;
    }

    public void setFilter(String query) {
        if (!StringUtils.isBlank(query)) {
            filter = (DBObject) JSON.parse(query);
        }
    }

    public DBObject getFilter() {
        return filter;
    }

    public DBObject getProjection() {
        return projection;
    }

    public DBObject getSort() {
        return sort;
    }

    public int getResultLimit() {
        return resultLimit;
    }
}
