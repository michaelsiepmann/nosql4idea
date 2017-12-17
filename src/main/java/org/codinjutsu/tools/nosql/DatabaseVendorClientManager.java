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

package org.codinjutsu.tools.nosql;

import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.project.Project;
import org.codinjutsu.tools.nosql.commons.model.DatabaseServer;

import static java.util.Arrays.asList;

public class DatabaseVendorClientManager {

    private final Project project;

    public DatabaseVendorClientManager(Project project) {
        this.project = project;
    }

    public static DatabaseVendorClientManager getInstance(Project project) {
        return ServiceManager.getService(project, DatabaseVendorClientManager.class);
    }

    public void cleanUpServers() {
        asList(DatabaseVendor.values()).forEach(databaseVendor -> databaseVendor.getClient(project).cleanUpServers());
    }

    public void registerServer(DatabaseServer databaseServer) {
        databaseServer.getVendor().getClient(project).registerServer(databaseServer);
    }

    public void loadServer(DatabaseServer databaseServer) {
        databaseServer.getVendor().getClient(project).loadServer(databaseServer);
    }
}
