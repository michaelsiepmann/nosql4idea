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

import com.intellij.ide.CommonActionsManager;
import com.intellij.ide.TreeExpander;
import com.intellij.openapi.Disposable;
import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.MessageType;
import com.intellij.openapi.ui.popup.Balloon;
import com.intellij.openapi.util.Disposer;
import com.intellij.ui.PopupHandler;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.ui.treeStructure.Tree;
import com.intellij.util.ui.tree.TreeUtil;
import org.codinjutsu.tools.nosql.commons.logic.ConfigurationException;
import org.codinjutsu.tools.nosql.commons.logic.DatabaseClient;
import org.codinjutsu.tools.nosql.commons.logic.FolderDatabaseClient;
import org.codinjutsu.tools.nosql.commons.model.Database;
import org.codinjutsu.tools.nosql.commons.model.DatabaseServer;
import org.codinjutsu.tools.nosql.commons.model.Folder;
import org.codinjutsu.tools.nosql.commons.model.NoSQLCollection;
import org.codinjutsu.tools.nosql.commons.utils.GuiUtils;
import org.codinjutsu.tools.nosql.commons.view.action.DropCollectionAction;
import org.codinjutsu.tools.nosql.commons.view.action.DropDatabaseAction;
import org.codinjutsu.tools.nosql.commons.view.action.NoSqlDatabaseConsoleAction;
import org.codinjutsu.tools.nosql.commons.view.action.OpenPluginSettingsAction;
import org.codinjutsu.tools.nosql.commons.view.action.RefreshServerAction;
import org.codinjutsu.tools.nosql.commons.view.action.ViewCollectionValuesAction;
import org.codinjutsu.tools.nosql.commons.view.editor.NoSqlDatabaseFileSystem;
import org.codinjutsu.tools.nosql.commons.view.editor.NoSqlDatabaseObjectFile;
import org.codinjutsu.tools.nosql.couchbase.model.CouchbaseDatabase;
import org.codinjutsu.tools.nosql.couchbase.view.editor.CouchbaseObjectFile;
import org.codinjutsu.tools.nosql.elasticsearch.model.ElasticsearchDatabase;
import org.codinjutsu.tools.nosql.elasticsearch.model.ElasticsearchServerConfiguration;
import org.codinjutsu.tools.nosql.elasticsearch.model.ElasticsearchVersion;
import org.codinjutsu.tools.nosql.elasticsearch.view.editor.ElasticsearchObjectFile;
import org.codinjutsu.tools.nosql.mongo.model.MongoDatabase;
import org.codinjutsu.tools.nosql.mongo.view.editor.MongoObjectFile;
import org.codinjutsu.tools.nosql.redis.model.RedisDatabase;
import org.codinjutsu.tools.nosql.redis.view.editor.RedisObjectFile;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeSelectionModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;
import java.util.List;

import static org.codinjutsu.tools.nosql.commons.utils.GuiUtils.showNotification;

public class NoSqlExplorerPanel extends JPanel implements Disposable {

    private static final URL pluginSettingsUrl = GuiUtils.isUnderDarcula() ? GuiUtils.getIconResource("pluginSettings_dark.png") : GuiUtils.getIconResource("pluginSettings.png");

    private JPanel rootPanel;
    private JPanel treePanel;
    private JPanel toolBarPanel;
    private Tree databaseTree;

    private final Project project;
    private final DatabaseVendorClientManager databaseVendorClientManager;

    NoSqlExplorerPanel(Project project, DatabaseVendorClientManager databaseVendorClientManager) {
        this.project = project;
        this.databaseVendorClientManager = databaseVendorClientManager;

        treePanel.setLayout(new BorderLayout());

        databaseTree = createTree();
        databaseTree.setCellRenderer(new NoSqlTreeRenderer());
        databaseTree.setName("databaseTree");

        setLayout(new BorderLayout());
        treePanel.add(new JBScrollPane(databaseTree), BorderLayout.CENTER);
        add(rootPanel, BorderLayout.CENTER);

        toolBarPanel.setLayout(new BorderLayout());

        ApplicationManager.getApplication().invokeLater(this::reloadAllServerConfigurations);
    }

    public void reloadAllServerConfigurations() {
        this.databaseVendorClientManager.cleanUpServers();
        databaseTree.setRootVisible(false);

        List<ServerConfiguration> serverConfigurations = getServerConfigurations();
        if (serverConfigurations.size() == 0) {
            databaseTree.setModel(null);
            return;
        }

        final DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode();
        databaseTree.setModel(new DefaultTreeModel(rootNode));

        for (ServerConfiguration serverConfiguration : serverConfigurations) {
            DatabaseServer databaseServer = new DatabaseServer(serverConfiguration);
            databaseVendorClientManager.registerServer(databaseServer);
            DefaultMutableTreeNode serverNode = new DefaultMutableTreeNode(databaseServer);
            rootNode.add(serverNode);
            if (serverConfiguration.isConnectOnIdeStartup()) {
                reloadServerConfiguration(serverNode, false);
            }
        }

        TreeUtil.expand(databaseTree, 2);
    }


    public void reloadServerConfiguration(final DefaultMutableTreeNode serverNode, final boolean expandAfterLoading) {
        databaseTree.setPaintBusy(true);

        ApplicationManager.getApplication().executeOnPooledThread(() -> {
            final DatabaseServer databaseServer = (DatabaseServer) serverNode.getUserObject();
            try {
                databaseVendorClientManager.loadServer(databaseServer);

                GuiUtils.runInSwingThread(() -> {
                    databaseTree.invalidate();

                    serverNode.removeAllChildren();
                    addDatabasesIfAny(databaseServer, serverNode);

                    ((DefaultTreeModel) databaseTree.getModel()).reload(serverNode);

                    databaseTree.revalidate();

                    if (expandAfterLoading) {
                        GuiUtils.expand(databaseTree, TreeUtil.getPathFromRoot(serverNode), 1);
                    }

                });

            } catch (ConfigurationException confEx) {
                databaseServer.setStatus(DatabaseServer.Status.ERROR);
                showNotification(treePanel,
                        MessageType.ERROR,
                        String.format("Error when connecting on %s", databaseServer.getLabel()),
                        Balloon.Position.atLeft);
            } finally {
                databaseTree.setPaintBusy(false);
            }
        });
    }

    private void addDatabasesIfAny(DatabaseServer<ServerConfiguration> databaseServer, DefaultMutableTreeNode serverNode) {
        for (Database database : databaseServer.getDatabases()) {
            DefaultMutableTreeNode databaseNode = new DefaultMutableTreeNode(database);
            if (database instanceof Folder) {
                ((Folder<NoSQLCollection>) database).getChildFolders()
                        .stream()
                        .map(DefaultMutableTreeNode::new)
                        .forEach(databaseNode::add);
            }
            serverNode.add(databaseNode);
        }
    }

    private List<ServerConfiguration> getServerConfigurations() {
        return NoSqlConfiguration.getInstance(project).getServerConfigurations();
    }

    public void installActions() {

        final TreeExpander treeExpander = new TreeExpander() {
            @Override
            public void expandAll() {
                NoSqlExplorerPanel.this.expandAll();
            }

            @Override
            public boolean canExpand() {
                return true;
            }

            @Override
            public void collapseAll() {
                NoSqlExplorerPanel.this.collapseAll();
            }

            @Override
            public boolean canCollapse() {
                return true;
            }
        };

        CommonActionsManager actionsManager = CommonActionsManager.getInstance();

        final AnAction expandAllAction = actionsManager.createExpandAllAction(treeExpander, rootPanel);
        final AnAction collapseAllAction = actionsManager.createCollapseAllAction(treeExpander, rootPanel);

        Disposer.register(this, () -> {
            collapseAllAction.unregisterCustomShortcutSet(rootPanel);
            expandAllAction.unregisterCustomShortcutSet(rootPanel);
        });


        DefaultActionGroup actionGroup = new DefaultActionGroup("NoSqlExplorerGroup", false);
        ViewCollectionValuesAction viewCollectionValuesAction = new ViewCollectionValuesAction(this);
        RefreshServerAction refreshServerAction = new RefreshServerAction(this);
        if (ApplicationManager.getApplication() != null) {
            actionGroup.add(refreshServerAction);
            actionGroup.add(new NoSqlDatabaseConsoleAction(this));
            actionGroup.add(viewCollectionValuesAction);
            actionGroup.add(expandAllAction);
            actionGroup.add(collapseAllAction);
            actionGroup.addSeparator();
            actionGroup.add(new OpenPluginSettingsAction());
        }

        GuiUtils.installActionGroupInToolBar(actionGroup, toolBarPanel, ActionManager.getInstance(), "NoSqlExplorerActions", true);

        DefaultActionGroup actionPopupGroup = new DefaultActionGroup("NoSqlExplorerPopupGroup", true);
        if (ApplicationManager.getApplication() != null) {
            actionPopupGroup.add(refreshServerAction);
            actionPopupGroup.add(viewCollectionValuesAction);
            actionPopupGroup.add(new DropCollectionAction(this));
            actionPopupGroup.add(new DropDatabaseAction(this));
        }

        PopupHandler.installPopupHandler(databaseTree, actionPopupGroup, "POPUP", ActionManager.getInstance());

        databaseTree.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
                if (!(mouseEvent.getSource() instanceof JTree)) {
                    return;
                }

                DefaultMutableTreeNode treeNode = (DefaultMutableTreeNode) databaseTree.getLastSelectedPathComponent();
                if (treeNode == null) {
                    return;
                }

                if (mouseEvent.getClickCount() == 2) {
                    Object userObject = treeNode.getUserObject();
                    if (userObject instanceof DatabaseServer && treeNode.getChildCount() == 0) {
                        reloadServerConfiguration(getSelectedServerNode(), true);
                    }
                    if (userObject instanceof Folder) {
                        loadRecords();
                    }
                    if (userObject instanceof RedisDatabase) {
                        loadRecords();
                    }
                    if (userObject instanceof CouchbaseDatabase) {
                        loadRecords();
                    }
                }
            }
        });
    }

    private void expandAll() {
        TreeUtil.expandAll(databaseTree);
    }

    private void collapseAll() {
        TreeUtil.collapseAll(databaseTree, 1);
    }

    @Override
    public void dispose() {
        databaseTree = null;
    }

    public DefaultMutableTreeNode getSelectedServerNode() {
        DefaultMutableTreeNode treeNode = (DefaultMutableTreeNode) databaseTree.getLastSelectedPathComponent();
        if (treeNode != null) {
            Object userObject = treeNode.getUserObject();
            if (userObject instanceof NoSQLCollection) {
                return (DefaultMutableTreeNode) treeNode.getParent().getParent();
            }

            if (userObject instanceof Database) {
                return (DefaultMutableTreeNode) treeNode.getParent();
            }

            if (userObject instanceof DatabaseServer) {
                return treeNode;
            }
        }
        return null;
    }


    private DefaultMutableTreeNode getSelectedDatabaseNode() {
        DefaultMutableTreeNode treeNode = (DefaultMutableTreeNode) databaseTree.getLastSelectedPathComponent();
        if (treeNode != null) {
            Object userObject = treeNode.getUserObject();

            if (userObject instanceof NoSQLCollection) {
                return (DefaultMutableTreeNode) treeNode.getParent();
            }

            if (userObject instanceof Database) {
                return treeNode;
            }
        }

        return null;
    }

    private DefaultMutableTreeNode getSelectedCollectionNode() {
        DefaultMutableTreeNode treeNode = (DefaultMutableTreeNode) databaseTree.getLastSelectedPathComponent();
        if (treeNode != null) {
            Object userObject = treeNode.getUserObject();
            if (userObject instanceof NoSQLCollection) {
                return treeNode;
            }
        }
        return null;
    }

    public ServerConfiguration getConfiguration() {

        DefaultMutableTreeNode serverNode = getSelectedServerNode();
        if (serverNode == null) {
            return null;
        }

        return ((DatabaseServer) serverNode.getUserObject()).getConfiguration();
    }

    private RedisDatabase getSelectedRedisDatabase() {
        DefaultMutableTreeNode databaseNode = getSelectedDatabaseNode();
        if (databaseNode != null) {
            Object database = databaseNode.getUserObject();
            if (database instanceof RedisDatabase) {
                return (RedisDatabase) database;
            }
        }
        return null;
    }

    private CouchbaseDatabase getSelectedCouchbaseDatabase() {
        DefaultMutableTreeNode databaseNode = getSelectedDatabaseNode();
        if (databaseNode != null) {
            Object database = databaseNode.getUserObject();
            if (database instanceof CouchbaseDatabase) {
                return (CouchbaseDatabase) database;
            }
        }
        return null;
    }

    private ElasticsearchDatabase getSelectedElasticsearchDatabase() {
        DefaultMutableTreeNode databaseNode = getSelectedDatabaseNode();
        if (databaseNode != null) {
            Object database = databaseNode.getUserObject();
            if (database instanceof ElasticsearchDatabase) {
                return (ElasticsearchDatabase) database;
            }
        }
        return null;
    }

    public Database getSelectedDatabase() {
        DefaultMutableTreeNode databaseNode = getSelectedDatabaseNode();
        if (databaseNode != null) {
            Object database = databaseNode.getUserObject();
            if (database instanceof Database) {
                return (Database) databaseNode.getUserObject();
            }
        }
        return null;
    }

    public boolean hasDatabaseConsoleApplication() {
        Database database = getSelectedDatabase();
        return database instanceof MongoDatabase || database instanceof RedisDatabase;
    }

    public <T extends NoSQLCollection> T getSelectedCollection() {
        DefaultMutableTreeNode collectionNode = getSelectedCollectionNode();
        if (collectionNode == null) {
            return null;
        }

        return (T) collectionNode.getUserObject();
    }

    public void loadRecords() {
        NoSqlDatabaseFileSystem.getInstance().openEditor(createNoSqlObjectFile());
    }

    @NotNull
    private NoSqlDatabaseObjectFile createNoSqlObjectFile() { // TODO need to put in the database UI manager
        ServerConfiguration selectedConfiguration = getConfiguration();
        if (DatabaseVendor.MONGO.equals(selectedConfiguration.getDatabaseVendor())) {
            return new MongoObjectFile(project, selectedConfiguration, getSelectedCollection());
        }
        if (DatabaseVendor.COUCHBASE.equals(selectedConfiguration.getDatabaseVendor())) {
            return new CouchbaseObjectFile(project, selectedConfiguration, getSelectedCouchbaseDatabase());
        }
        if (DatabaseVendor.ELASTICSEARCH.equals(selectedConfiguration.getDatabaseVendor())) {
            return new ElasticsearchObjectFile(project, new ElasticsearchServerConfiguration(ElasticsearchVersion.VERSION_20, selectedConfiguration), getSelectedElasticsearchDatabase(), getSelectedCollection());
        }
        return new RedisObjectFile(project, selectedConfiguration, getSelectedRedisDatabase());
    }

    public boolean canCollectionBeDeleted() {
        NoSQLCollection selectedCollection = getSelectedCollection();
        return selectedCollection != null && selectedCollection.canBeDeleted();
    }

    public void dropCollection() {
        ServerConfiguration configuration = getConfiguration();
        DatabaseClient client = databaseVendorClientManager.get(configuration.getDatabaseVendor());
        if (client instanceof FolderDatabaseClient) {
            ((FolderDatabaseClient) client).dropFolder(configuration, getSelectedCollection());
            reloadServerConfiguration(getSelectedServerNode(), true);
        }
    }

    public boolean isDatabaseSelected() {
        return getSelectedCollection() != null || getSelectedDatabase() != null;
    }

    public void dropDatabase() {
        ServerConfiguration configuration = getConfiguration();
        DatabaseClient databaseClient = databaseVendorClientManager.get(configuration.getDatabaseVendor());
        if (databaseClient instanceof FolderDatabaseClient) {
            ((FolderDatabaseClient) databaseClient).dropDatabase(configuration, getSelectedDatabase());
            reloadServerConfiguration(getSelectedServerNode(), true);
        }
    }

    private Tree createTree() {

        Tree tree = new Tree() {

            private final JLabel myLabel = new JLabel(
                    String.format("<html><center>NoSql server list is empty<br><br>You may use <img src=\"%s\"> to add configuration</center></html>", pluginSettingsUrl)
            );

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (!getServerConfigurations().isEmpty()) return;

                myLabel.setFont(getFont());
                myLabel.setBackground(getBackground());
                myLabel.setForeground(getForeground());
                Rectangle bounds = getBounds();
                Dimension size = myLabel.getPreferredSize();
                myLabel.setBounds(0, 0, size.width, size.height);

                int x = (bounds.width - size.width) / 2;
                Graphics g2 = g.create(bounds.x + x, bounds.y + 20, bounds.width, bounds.height);
                try {
                    myLabel.paint(g2);
                } finally {
                    g2.dispose();
                }
            }
        };

        tree.getEmptyText().clear();
        tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);

        return tree;
    }
}
