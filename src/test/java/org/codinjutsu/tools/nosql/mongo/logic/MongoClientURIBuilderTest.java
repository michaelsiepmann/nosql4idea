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
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.StringContains.containsString;
import static org.hamcrest.core.StringStartsWith.startsWith;
import static org.junit.jupiter.api.Assertions.assertEquals;

class MongoClientURIBuilderTest {

    @Test
    void withoutAuthentication() {
        assertEquals(
                "mongodb://localhost:27017,localhost:27018/",
                MongoClientURIBuilder.builder()
                        .setServerAddresses("localhost:27017,localhost:27018")
                        .build());
    }

    @Test
    void withSimpleAuthentication() {
        assertEquals(
                "mongodb://toto:pass@localhost:27018/?authSource=userdb",
                MongoClientURIBuilder.builder()
                        .setServerAddresses("localhost:27018")
                        .setCredential("toto", "pass", "userdb")
                        .build());
    }

    @Test
    void withSpecificAuthentication() {
        String uri = MongoClientURIBuilder.builder()
                .setServerAddresses("localhost:27018")
                .setCredential("toto", "pass", "userdb")
                .setAuthenticationMecanism(AuthenticationMechanism.MONGODB_CR)
                .build();
        assertThat(uri, startsWith("mongodb://toto:pass@localhost:27018/?"));
        assertThat(uri, containsString("authMechanism=MONGODB-CR"));
        assertThat(uri, containsString("authSource=userdb"));
    }

    @Test
    void addSslOption() {
        assertEquals(
                "mongodb://localhost:27018/?ssl=true",
                MongoClientURIBuilder.builder()
                        .setServerAddresses("localhost:27018")
                        .sslEnabled()
                        .build());
    }
}
