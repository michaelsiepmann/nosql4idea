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

package org.codinjutsu.tools.nosql.couchbase.logic;

import com.couchbase.client.java.CouchbaseCluster;
import com.couchbase.client.java.cluster.BucketSettings;
import com.couchbase.client.java.cluster.ClusterManager;
import org.codinjutsu.tools.nosql.ServerConfiguration;
import org.codinjutsu.tools.nosql.commons.model.AuthenticationSettings;
import org.codinjutsu.tools.nosql.commons.model.Database;
import org.codinjutsu.tools.nosql.commons.model.DatabaseServer;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class CouchbaseClientTest {

    private CouchbaseCluster couchbaseCluster;
    private ServerConfiguration configuration;

    @BeforeEach
    void setUp() {
        couchbaseCluster = mock(CouchbaseCluster.class);
        configuration = new ServerConfiguration();
        AuthenticationSettings authenticationSettings = new AuthenticationSettings();
        configuration.setAuthenticationSettings(authenticationSettings);
        ClusterManager clusterManager = mock(ClusterManager.class);
        BucketSettings testBucket = createBucketSettings(clusterManager, "usertest", "usertestname");
        BucketSettings anotherBucket = createBucketSettings(clusterManager, "another", "anothertestname");
        when(clusterManager.getBuckets()).thenReturn(asList(testBucket, anotherBucket));
        when(couchbaseCluster.clusterManager(any(), any())).thenReturn(clusterManager);
    }

    @Test
    void loadServersWithoutUserDatabase() {
        CouchbaseClient couchbaseClient = new CouchbaseClientStub();
        DatabaseServer databaseServer = new DatabaseServer(configuration);
        couchbaseClient.loadServer(databaseServer);
        verify(couchbaseCluster).disconnect();
        List<Database> databases = databaseServer.getDatabases();
        assertEquals(2, databases.size());
        assertEquals("usertestname", databases.get(0).getName());
        assertEquals("anothertestname", databases.get(1).getName());
    }

    @Test
    void loadServersWithUserDatabase() {
        configuration.setUserDatabase("usertest");
        CouchbaseClient couchbaseClient = new CouchbaseClientStub();
        DatabaseServer databaseServer = new DatabaseServer(configuration);
        couchbaseClient.loadServer(databaseServer);
        verify(couchbaseCluster).disconnect();
        List<Database> databases = databaseServer.getDatabases();
        assertEquals(1, databases.size());
        assertEquals("usertestname", databases.get(0).getName());
    }

    private BucketSettings createBucketSettings(ClusterManager clusterManager, String userBucket, String name) {
        BucketSettings settings = mock(BucketSettings.class);
        when(settings.name()).thenReturn(name);
        when(clusterManager.getBucket(userBucket)).thenReturn(settings);
        return settings;
    }

    private class CouchbaseClientStub extends CouchbaseClient {

        @NotNull
        @Override
        CouchbaseCluster createCluster(ServerConfiguration serverConfiguration) {
            return couchbaseCluster;
        }
    }
}
