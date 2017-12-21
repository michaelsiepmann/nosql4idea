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
import org.codinjutsu.tools.nosql.commons.configuration.ServerConfiguration;
import org.codinjutsu.tools.nosql.commons.logic.ConfigurationException;
import org.codinjutsu.tools.nosql.commons.model.Database;
import org.codinjutsu.tools.nosql.commons.model.DatabaseServer;
import org.codinjutsu.tools.nosql.commons.model.explorer.DatabaseServerFolder;
import org.codinjutsu.tools.nosql.commons.model.explorer.Folder;
import org.codinjutsu.tools.nosql.commons.model.explorer.FolderType;
import org.codinjutsu.tools.nosql.commons.utils.GuiUtils;
import org.codinjutsu.tools.nosql.commons.view.action.AddCollectionAction;
import org.codinjutsu.tools.nosql.commons.view.action.DropCollectionAction;
import org.codinjutsu.tools.nosql.commons.view.action.DropDatabaseAction;
import org.codinjutsu.tools.nosql.commons.view.action.NoSqlDatabaseConsoleAction;
import org.codinjutsu.tools.nosql.commons.view.action.OpenPluginSettingsAction;
import org.codinjutsu.tools.nosql.commons.view.action.RefreshServerAction;
import org.codinjutsu.tools.nosql.commons.view.action.ViewCollectionValuesAction;
import org.codinjutsu.tools.nosql.commons.view.editor.NoSqlDatabaseFileSystem;
import org.codinjutsu.tools.nosql.commons.view.editor.NoSqlDatabaseObjectFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreeSelectionModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;
import java.util.Collection;
import java.util.Enumeration;
import java.util.List;

import static org.codinjutsu.tools.nosql.commons.utils.GuiUtils.showNotification;

public class NoSqlExplorerPanel extends JPanel implements Disposable {

    private static final URL pluginSettingsUrl = GuiUtils.isUnderDarcula() ? GuiUtils.getIconResource("pluginSettings_dark.png") : GuiUtils.getIconResource("pluginSettings.png"); //NON-NLS

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
        databaseTree.setName("databaseTree"); //NON-NLS

        setLayout(new BorderLayout());
        treePanel.add(new JBScrollPane(databaseTree), BorderLayout.CENTER);
        add(rootPanel, BorderLayout.CENTER);

        toolBarPanel.setLayout(new BorderLayout());

        ApplicationManager.getApplication().invokeLater(this::reloadAllServerConfigurations);
    }

    public void reloadAllServerConfigurations() {
        databaseVendorClientManager.cleanUpServers();
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
            DefaultMutableTreeNode serverNode = new DefaultMutableTreeNode(createDatabaseServerFolder(databaseServer));
            rootNode.add(serverNode);
            if (serverConfiguration.isConnectOnIdeStartup()) {
                reloadServerConfiguration(serverNode, false);
            }
        }

        TreeUtil.expand(databaseTree, 2);
    }

    @NotNull
    private DatabaseServerFolder createDatabaseServerFolder(DatabaseServer databaseServer) {
        return databaseServer.getVendor().createDatabaseServerFolder(databaseServer, project);
    }

    private void reloadServerConfiguration(final DefaultMutableTreeNode serverNode, final boolean expandAfterLoading) {
        databaseTree.setPaintBusy(true);

        ApplicationManager.getApplication().executeOnPooledThread(() -> {
            DatabaseServerFolder databaseServerFolder = (DatabaseServerFolder) serverNode.getUserObject();
            final DatabaseServer databaseServer = databaseServerFolder.getData();
            try {
                databaseVendorClientManager.loadServer(databaseServer);

                GuiUtils.runInSwingThread(() -> {
                    databaseTree.invalidate();

                    serverNode.removeAllChildren();
                    addDatabasesIfAny(databaseServerFolder, serverNode);

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

    private void addDatabasesIfAny(Folder databaseServerFolder, DefaultMutableTreeNode serverNode) {
        Collection<Folder<?, ?>> children = databaseServerFolder.getChildren();
        for (Folder child : children) {
            DefaultMutableTreeNode databaseNode = new DefaultMutableTreeNode(child);
            addDatabasesIfAny(child, databaseNode);
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
        AddCollectionAction addCollectionAction = new AddCollectionAction(this);
        if (ApplicationManager.getApplication() != null) {
            actionGroup.add(refreshServerAction);
            actionGroup.addSeparator();
            actionGroup.add(addCollectionAction);
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
            actionPopupGroup.add(addCollectionAction);
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
                    Object folderItem = treeNode.getUserObject();
                    if (folderItem instanceof DatabaseServerFolder && treeNode.getChildCount() == 0) {
                        reloadServerConfiguration(getSelectedServerNode(), true);
                    }
                    if (((Folder) folderItem).isViewableContent()) {
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

    @Nullable
    public Folder getSelectedFolder() {
        DefaultMutableTreeNode treeNode = (DefaultMutableTreeNode) databaseTree.getLastSelectedPathComponent();
        return treeNode != null ? (Folder) treeNode.getUserObject() : null;
    }

    private DatabaseServer getSelectedDatabaseServer() {
        DefaultMutableTreeNode treeNode = (DefaultMutableTreeNode) databaseTree.getLastSelectedPathComponent();
        if (treeNode != null) {
            return ((Folder) treeNode.getUserObject()).getDatabaseServer();
        }
        return null;
    }

    private DefaultMutableTreeNode getSelectedServerNode() {
        return getSelectedServerNode((DefaultMutableTreeNode) databaseTree.getLastSelectedPathComponent());
    }

    private DefaultMutableTreeNode getSelectedServerNode(DefaultMutableTreeNode treeNode) {
        if (treeNode != null) {
            Object object = treeNode.getUserObject();
            if (object != null && object instanceof DatabaseServerFolder) {
                return treeNode;
            }
            TreeNode parent = treeNode.getParent();
            if (parent instanceof DefaultMutableTreeNode) {
                return getSelectedServerNode((DefaultMutableTreeNode) parent);
            }
        }
        return null;
    }

    public boolean isDatabaseServerSelected() {
        return getSelectedDatabaseServer() != null;
    }

    public boolean hasDatabaseServerChildren() {
        DatabaseServer selectedDatabaseServer = getSelectedDatabaseServer();
        return selectedDatabaseServer != null && !selectedDatabaseServer.getDatabases().isEmpty();
    }

    public ServerConfiguration getConfiguration() {
        Folder folder = getSelectedFolder();
        return folder != null ? folder.getDatabaseServer().getConfiguration() : null;
    }

    public Database getSelectedDatabase() {
        Folder folder = getSelectedFolder();
        return folder != null ? folder.getDatabase() : null;
    }

    public boolean hasDatabaseConsoleApplication() {
        Folder folder = getSelectedFolder();
        return folder != null && folder.canShowConsoleApplication();
    }

    public void loadRecords() {
        NoSqlDatabaseObjectFile noSqlObjectFile = createNoSqlObjectFile();
        if (noSqlObjectFile != null) {
            NoSqlDatabaseFileSystem.getInstance().openEditor(noSqlObjectFile);
        }
    }

    public boolean canFolderBeDeleted(FolderType folderType) {
        Folder selectedFolder = getSelectedFolder();
        return selectedFolder != null && selectedFolder.canBeDeleted(folderType);
    }

    public boolean isDatabaseSelected() {
        Folder selectedFolder = getSelectedFolder();
        return selectedFolder != null && selectedFolder.isViewableContent();
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

    @Nullable
    private NoSqlDatabaseObjectFile createNoSqlObjectFile() {
        Folder folder = getSelectedFolder();
        return folder != null ? folder.createNoSqlObjectFile(project) : null;
    }

    public void dropFolder() {
        Folder selectedFolder = getSelectedFolder();
        if (selectedFolder != null) {
            selectedFolder.getParent().deleteChild(selectedFolder);
            reloadServerConfiguration(getSelectedServerNode(), true);
        }
    }

    public boolean canCreateChildAtSelectedFolder(FolderType folderType) {
        Folder selectedFolder = getSelectedFolder();
        return selectedFolder != null && selectedFolder.canCreateChild(folderType);
    }

    public void createCollection() {
        Folder selectedFolder = getSelectedFolder();
        if (selectedFolder != null) {
            Folder child = selectedFolder.createChild();
            if (child != null) {
                getParentOf(selectedFolder).add(new DefaultMutableTreeNode(child));
            }
        }
    }

    private DefaultMutableTreeNode getParentOf(Folder folder) {
        return getParentOf((DefaultMutableTreeNode) databaseTree.getModel().getRoot(), folder);
    }

    private DefaultMutableTreeNode getParentOf(DefaultMutableTreeNode node, Folder folder) {
        if (node != null) {
            Enumeration children = node.children();
            while (children.hasMoreElements()) {
                DefaultMutableTreeNode childNode = (DefaultMutableTreeNode) children.nextElement();
                if (childNode.getUserObject() == folder) {
                    return node;
                }
                DefaultMutableTreeNode result = getParentOf(childNode, folder);
                if (result != null) {
                    return result;
                }
            }
        }
        return null;
    }

    public void refreshSelectedServer() {
        reloadServerConfiguration(getSelectedServerNode(), true);
    }
}
