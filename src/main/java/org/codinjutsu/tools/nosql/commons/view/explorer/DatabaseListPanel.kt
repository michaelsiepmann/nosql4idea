package org.codinjutsu.tools.nosql.commons.view.explorer

import com.intellij.openapi.actionSystem.ActionManager
import com.intellij.openapi.actionSystem.DefaultActionGroup
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.MessageType
import com.intellij.openapi.ui.popup.Balloon
import com.intellij.ui.PopupHandler
import com.intellij.util.ui.tree.TreeUtil
import org.codinjutsu.tools.nosql.DatabaseVendorClientManager
import org.codinjutsu.tools.nosql.NoSqlConfiguration
import org.codinjutsu.tools.nosql.commons.configuration.ServerConfiguration
import org.codinjutsu.tools.nosql.commons.exceptions.ConfigurationException
import org.codinjutsu.tools.nosql.commons.exceptions.DatabaseException
import org.codinjutsu.tools.nosql.commons.model.Database
import org.codinjutsu.tools.nosql.commons.model.DatabaseServer
import org.codinjutsu.tools.nosql.commons.model.explorer.DatabaseServerFolder
import org.codinjutsu.tools.nosql.commons.model.explorer.Folder
import org.codinjutsu.tools.nosql.commons.model.explorer.FolderType
import org.codinjutsu.tools.nosql.commons.utils.GuiUtils
import org.codinjutsu.tools.nosql.commons.utils.GuiUtils.showNotification
import org.codinjutsu.tools.nosql.commons.view.action.AddCollectionAction
import org.codinjutsu.tools.nosql.commons.view.action.DropCollectionAction
import org.codinjutsu.tools.nosql.commons.view.action.DropDatabaseAction
import org.codinjutsu.tools.nosql.commons.view.action.NoSqlDatabaseConsoleAction
import org.codinjutsu.tools.nosql.commons.view.action.OpenPluginSettingsAction
import org.codinjutsu.tools.nosql.commons.view.action.RefreshServerAction
import org.codinjutsu.tools.nosql.commons.view.action.ViewCollectionValuesAction
import org.codinjutsu.tools.nosql.commons.view.editor.NoSqlDatabaseFileSystem
import java.lang.String.format
import javax.swing.tree.DefaultMutableTreeNode
import javax.swing.tree.DefaultTreeModel

internal class DatabaseListPanel(private val project: Project, private val databaseVendorClientManager: DatabaseVendorClientManager) : AbstractListPanel() {

    private val serverConfigurations: List<ServerConfiguration>
        get() = NoSqlConfiguration.getInstance(project).serverConfigurations

    val selectedFolder: Folder<*, *>?
        get() = getSelectedItem<Folder<*, *>>()

    private val selectedServerNode: DefaultMutableTreeNode?
        get() = getSelectedServerNode(databaseTree?.lastSelectedPathComponent as DefaultMutableTreeNode)

    private val selectedDatabaseServer: DatabaseServer?
        get() = selectedFolder?.databaseServer

    val isDatabaseServerSelected: Boolean
        get() = selectedDatabaseServer != null

    val configuration: ServerConfiguration?
        get() = selectedDatabaseServer?.configuration

    val selectedDatabase: Database?
        get() = selectedFolder?.database

    val isDatabaseSelected: Boolean
        get() = selectedFolder?.isViewableContent() ?: false

    override fun initializeTree(databaseTree: ExplorerTree) {
        databaseVendorClientManager.cleanUpServers()

        val serverConfigurations = serverConfigurations
        if (serverConfigurations.isEmpty()) {
            databaseTree.model = null
            return
        }

        val rootNode = DefaultMutableTreeNode()
        databaseTree.model = DefaultTreeModel(rootNode)

        serverConfigurations.forEach { serverConfiguration ->
            val databaseServer = DatabaseServer(serverConfiguration)
            databaseVendorClientManager.registerServer(databaseServer)
            val serverNode = DefaultMutableTreeNode(createDatabaseServerFolder(databaseServer))
            rootNode.add(serverNode)
            if (serverConfiguration.isConnectOnIdeStartup) {
                reloadServerConfiguration(serverNode, false)
            }
        }

        TreeUtil.expand(databaseTree, 2)
    }

    private fun createDatabaseServerFolder(databaseServer: DatabaseServer): DatabaseServerFolder<*> {
        return databaseServer.vendor.databaseVendorInformation.createDatabaseServerFolder(databaseServer, project)
    }

    fun refreshSelectedServer() {
        reloadServerConfiguration(selectedServerNode, true)
    }

    private fun reloadServerConfiguration(serverNode: DefaultMutableTreeNode?, expandAfterLoading: Boolean) {
        val databaseTree = databaseTree ?: return
        databaseTree.setPaintBusy(true)

        ApplicationManager.getApplication().executeOnPooledThread {
            val databaseServerFolder = serverNode!!.userObject as DatabaseServerFolder<*>
            val databaseServer = databaseServerFolder.data
            try {
                databaseVendorClientManager.loadServer(databaseServer)

                GuiUtils.runInSwingThread {
                    databaseTree.invalidate()

                    serverNode.removeAllChildren()
                    addDatabasesIfAny(databaseServerFolder, serverNode)

                    (databaseTree.model as DefaultTreeModel).reload(serverNode)

                    databaseTree.revalidate()

                    if (expandAfterLoading) {
                        GuiUtils.expand(databaseTree, TreeUtil.getPathFromRoot(serverNode), 1)
                    }
                }
            } catch (confEx: ConfigurationException) {
                databaseServer.status = DatabaseServer.Status.ERROR
                showNotification(treePanel,
                        MessageType.ERROR,
                        format("Error when connecting on %s", databaseServer.label),
                        Balloon.Position.atLeft)
            } catch (confEx: DatabaseException) {
                databaseServer.status = DatabaseServer.Status.ERROR
                showNotification(treePanel, MessageType.ERROR, format("Error when connecting on %s", databaseServer.label), Balloon.Position.atLeft)
            } finally {
                databaseTree.setPaintBusy(false)
            }
        }
    }

    private fun addDatabasesIfAny(databaseServerFolder: Folder<*, *>, serverNode: DefaultMutableTreeNode?) {
        databaseServerFolder.children
                .forEach {
                    val databaseNode = DefaultMutableTreeNode(it)
                    addDatabasesIfAny(it, databaseNode)
                    serverNode!!.add(databaseNode)
                }
    }

    override fun installActions(databaseTree: ExplorerTree) {
        val expandCollapseButtons = createExpandCollapseButtons()
        val expandAllAction = expandCollapseButtons.component1()
        val collapseAllAction = expandCollapseButtons.component2()

        val actionGroup = DefaultActionGroup("NoSqlExplorerGroup", false) //NON-NLS
        val viewCollectionValuesAction = ViewCollectionValuesAction(this)
        val refreshServerAction = RefreshServerAction(this)
        val addCollectionAction = AddCollectionAction(this)
        if (ApplicationManager.getApplication() != null) {
            actionGroup.add(refreshServerAction)
            actionGroup.addSeparator()
            actionGroup.add(addCollectionAction)
            actionGroup.add(NoSqlDatabaseConsoleAction(this))
            actionGroup.add(viewCollectionValuesAction)
            actionGroup.add(expandAllAction)
            actionGroup.add(collapseAllAction)
            actionGroup.addSeparator()
            actionGroup.add(OpenPluginSettingsAction())
        }

        GuiUtils.installActionGroupInToolBar(actionGroup, toolBarPanel, ActionManager.getInstance(), "NoSqlExplorerActions", true) //NON-NLS

        val actionPopupGroup = DefaultActionGroup("NoSqlExplorerPopupGroup", true) //NON-NLS
        if (ApplicationManager.getApplication() != null) {
            actionPopupGroup.add(refreshServerAction)
            actionPopupGroup.add(viewCollectionValuesAction)
            actionPopupGroup.add(addCollectionAction)
            actionPopupGroup.add(DropCollectionAction(this))
            actionPopupGroup.add(DropDatabaseAction(this))
        }

        PopupHandler.installPopupMenu(databaseTree, actionPopupGroup, "POPUP") //NON-NLS

        databaseTree.addDoubleClickListener {
            val folderItem = it.userObject
            if (folderItem is DatabaseServerFolder<*> && it.childCount == 0) {
                reloadServerConfiguration(selectedServerNode, true)
            }
            if ((folderItem as Folder<*, *>).isViewableContent()) {
                loadRecords()
            }
        }
    }

    private fun getSelectedServerNode(treeNode: DefaultMutableTreeNode?): DefaultMutableTreeNode? {
        if (treeNode != null) {
            if (treeNode.userObject is DatabaseServerFolder<*>) {
                return treeNode
            }
            val parent = treeNode.parent
            if (parent is DefaultMutableTreeNode) {
                return getSelectedServerNode(parent)
            }
        }
        return null
    }

    fun hasDatabaseServerChildren() = selectedDatabaseServer?.databases?.isNotEmpty() ?: false

    fun hasDatabaseConsoleApplication() = selectedFolder?.canShowConsoleApplication() ?: false

    fun loadRecords() {
        createNoSqlObjectFile()?.let {
            NoSqlDatabaseFileSystem.getInstance().openEditor(it)
        }
    }

    fun canFolderBeDeleted(folderType: FolderType) = selectedFolder?.canBeDeleted(folderType) ?: false

    private fun createNoSqlObjectFile() = selectedFolder?.createNoSqlObjectFile(project)

    fun dropFolder() {
        selectedFolder?.apply {
            parent?.deleteChild(this)
            reloadServerConfiguration(selectedServerNode, true)
        }
    }

    fun canCreateChildAtSelectedFolder(folderType: FolderType) =
            selectedFolder?.canCreateChild(folderType) ?: false

    fun createCollection() {
        selectedFolder?.apply {
            val child = createChild()
            if (child != null) {
                databaseTree!!.getParentOf(this)!!.add(DefaultMutableTreeNode(child))
            }
        }
    }
}
