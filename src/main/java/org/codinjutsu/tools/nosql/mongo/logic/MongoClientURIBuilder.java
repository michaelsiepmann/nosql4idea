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

package org.codinjutsu.tools.nosql.mongo.logic;

import com.mongodb.AuthenticationMechanism;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

class MongoClientURIBuilder {

    private static final String DEFAULT_AUTH_DB = "admin"; //NON-NLS

    private String serverUrls;
    private String username;
    private String password;
    private String authDatabase;
    private AuthenticationMechanism authenticationMecanism;
    private boolean sslEnabled = false;

    private MongoClientURIBuilder() {
    }

    static MongoClientURIBuilder builder() {
        return new MongoClientURIBuilder();
    }

    MongoClientURIBuilder setServerAddresses(String serverUrls) {
        this.serverUrls = serverUrls;
        return this;
    }

    MongoClientURIBuilder setCredential(String username, String password, String authDatabase) {
        this.username = username;
        this.password = password;
        this.authDatabase = StringUtils.isNotEmpty(authDatabase) ? authDatabase : DEFAULT_AUTH_DB;
        return this;
    }

    String build() {
        StringBuilder strBuilder = new StringBuilder();
        Map<String, String> options = new HashMap<>();
        if (StringUtils.isEmpty(username)) {
            strBuilder.append(String.format("mongodb://%s/", serverUrls)); //NON-NLS
        } else {
            strBuilder.append(String.format("mongodb://%s:%s@%s/", username, password, serverUrls)); //NON-NLS
        }
        if (authDatabase != null) {
            options.put("authSource", authDatabase); //NON-NLS
        }

        if (authenticationMecanism != null) {
            options.put("authMechanism", authenticationMecanism.getMechanismName()); //NON-NLS
        }

        if (sslEnabled) {
            options.put("ssl", Boolean.TRUE.toString()); //NON-NLS
        }
        if (options.isEmpty()) {
            return strBuilder.toString();
        }
        return strBuilder.append(buildOptions(options)).toString();
    }

    MongoClientURIBuilder setAuthenticationMecanism(@NotNull AuthenticationMechanism authenticationMecanism) {
        this.authenticationMecanism = authenticationMecanism;
        return this;
    }

    MongoClientURIBuilder sslEnabled() {
        sslEnabled = true;
        return this;
    }

    private static String buildOptions(Map<String, String> options) {
        List<String> optionList = new LinkedList<>();
        for (Map.Entry<String, String> keyValue : options.entrySet()) {
            optionList.add(String.format("%s=%s", keyValue.getKey(), keyValue.getValue())); //NON-NLS
        }
        return "?" + StringUtils.join(optionList, "&");
    }
}
