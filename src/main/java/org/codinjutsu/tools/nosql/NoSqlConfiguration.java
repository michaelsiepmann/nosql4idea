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

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.project.Project;
import com.intellij.util.xmlb.XmlSerializer;
import org.codinjutsu.tools.nosql.commons.configuration.ServerConfiguration;
import org.codinjutsu.tools.nosql.commons.history.HistoryList;
import org.codinjutsu.tools.nosql.commons.history.HistoryPanelMessages;
import org.codinjutsu.tools.nosql.couchbase.configuration.CouchbaseServerConfiguration;
import org.codinjutsu.tools.nosql.elasticsearch.configuration.ElasticsearchServerConfiguration;
import org.codinjutsu.tools.nosql.mongo.configuration.MongoServerConfiguration;
import org.codinjutsu.tools.nosql.redis.configuration.RedisServerConfiguration;
import org.codinjutsu.tools.nosql.solr.configuration.SolrServerConfiguration;
import org.jdom.Element;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unused")
@State(
        name = "NoSqlConfiguration",
        storages = {
                @Storage("$PROJECT_FILE$"),
                @Storage("$PROJECT_CONFIG_DIR$/noSqlSettings.xml")
        }
)
public class NoSqlConfiguration implements PersistentStateComponent<Element> {

    private static final String ROOTTAGNAME_NOSQL = "nosql"; //NON-NLS
    private static final String TAGNAME_CONFIGURATIONS = "configurations"; //NON-NLS
    private static final String TAGNAME_SHELL = "shell"; //NON-NLS
    private static final String TAGNAME_HISTORIES = "histories"; //NON-NLS
    private List<ServerConfiguration> serverConfigurations = new LinkedList<>();
    private Map<DatabaseVendor, String> shellPathByDatabaseVendor = new HashMap<>();
    private final List<HistoryList> historyLists = new LinkedList<>();

    public static NoSqlConfiguration getInstance(Project project) {
        return ServiceManager.getService(project, NoSqlConfiguration.class);
    }

    @Override
    public Element getState() {
        Element rootElement = new Element(ROOTTAGNAME_NOSQL);
        Element configurations = new Element(TAGNAME_CONFIGURATIONS);
        rootElement.addContent(configurations);
        for (ServerConfiguration configuration : serverConfigurations) {
            configurations.addContent(XmlSerializer.serialize(configuration));
        }
        Element shell = new Element(TAGNAME_SHELL);
        rootElement.addContent(shell);
        shell.addContent(XmlSerializer.serialize(shellPathByDatabaseVendor));
        Element histories = new Element(TAGNAME_HISTORIES);
        rootElement.addContent(histories);
        for (HistoryList historyList : historyLists) {
            histories.addContent(historyList.createElement());
        }
        return rootElement;
    }

    @Override
    public void loadState(Element element) {
        for (Element child : element.getChild(TAGNAME_CONFIGURATIONS).getChildren()) {
            Class<? extends ServerConfiguration> clazz = getServerConfigurationClass(child.getName());
            serverConfigurations.add(XmlSerializer.deserialize(child, clazz));
        }
        shellPathByDatabaseVendor = XmlSerializer.deserialize(element.getChild(TAGNAME_SHELL), HashMap.class);
        Element historyElements = element.getChild(TAGNAME_HISTORIES);
        if (historyElements != null) {
            for (Element child : historyElements.getChildren()) {
                HistoryList list = new HistoryList();
                list.readElement(child);
                historyLists.add(list);
            }
        }
    }

    @NotNull
    private Class<? extends ServerConfiguration> getServerConfigurationClass(String name) {
        switch (name.toLowerCase()) {
            case "elasticsearchserverconfiguration": //NON-NLS
                return ElasticsearchServerConfiguration.class;
            case "mongoserverconfiguration": //NON-NLS
                return MongoServerConfiguration.class;
            case "redisserverconfiguration": //NON-NLS
                return RedisServerConfiguration.class;
            case "couchbaseserverconfiguration": //NON-NLS
                return CouchbaseServerConfiguration.class;
            case "solrserverconfiguration": //NON-NLS
                return SolrServerConfiguration.class;
            default:
                return MongoServerConfiguration.class;
        }
    }

    void setServerConfigurations(List<ServerConfiguration> serverConfigurations) {
        this.serverConfigurations = serverConfigurations;
    }

    public List<ServerConfiguration> getServerConfigurations() {
        return serverConfigurations;
    }

    public String getShellPath(DatabaseVendor databaseVendor) {
        return shellPathByDatabaseVendor.get(databaseVendor);
    }

    public void setShellPathByDatabaseVendor(Map<DatabaseVendor, String> shellPathByDatabaseVendor) {
        this.shellPathByDatabaseVendor = shellPathByDatabaseVendor;
    }

    public Map<DatabaseVendor, String> getShellPathByDatabaseVendor() {
        return shellPathByDatabaseVendor;
    }

    void setShellPath(DatabaseVendor databaseVendor, String shellPath) {
        shellPathByDatabaseVendor.put(databaseVendor, shellPath);
    }

    public List<HistoryList> getHistoryLists() {
        return historyLists;
    }

    @NotNull
    public HistoryList findListByVendor(DatabaseVendor vendor, Project project) {
        String vendorName = vendor.getVendorName();
        for (HistoryList historyList : historyLists) {
            if (historyList.getVendor().equals(vendorName)) {
                return historyList;
            }
        }
        HistoryList historyList = new HistoryList(vendorName);
        historyLists.add(historyList);
        project.getMessageBus()
               .syncPublisher(HistoryPanelMessages.Companion.getTOPIC())
               .add(historyList);
        return historyList;
    }
}
