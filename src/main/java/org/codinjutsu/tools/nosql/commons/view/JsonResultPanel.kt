package org.codinjutsu.tools.nosql.commons.view

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.MessageType
import com.intellij.openapi.ui.popup.Balloon
import org.codinjutsu.tools.nosql.commons.model.JsonSearchResult
import org.codinjutsu.tools.nosql.commons.utils.GuiUtils
import org.codinjutsu.tools.nosql.commons.view.columninfo.WriteableColumnInfoDecider
import org.codinjutsu.tools.nosql.commons.view.nodedescriptor.json.JsonTreeModelFactory
import org.codinjutsu.tools.nosql.commons.view.nodedescriptor.keyvalue.KeyValueDescriptor
import org.codinjutsu.tools.nosql.commons.view.panel.AbstractNoSQLResultPanel

internal class JsonResultPanel(
        project: Project,
        documentOPerations: NoSQLResultPanelDocumentOperations<JsonObject>,
        private val idDescriptorKey: String
) :
        AbstractNoSQLResultPanel<JsonSearchResult, JsonObject>(project, documentOPerations, JsonTreeModelFactory(), idDescriptorKey) {

    override fun createEditionPanel(): EditionPanel<JsonObject>? {
        val editionPanel = EditionPanel<JsonObject>(JsonTreeModelFactory(), object : WriteableColumnInfoDecider {
            override fun isNodeWriteable(treeNode: NoSqlTreeNode) = treeNode.descriptor.value !is JsonObject && treeNode.descriptor.value !is JsonArray
        })
        editionPanel.init(documentOperations, object : ActionCallback {
            override fun onOperationSuccess(message: String) {
                hideEditionPanel()
                GuiUtils.showNotification(resultTreePanel, MessageType.INFO, message, Balloon.Position.above)
            }

            override fun onOperationFailure(exception: Exception) {
                GuiUtils.showNotification(resultTreePanel, MessageType.ERROR, exception.message, Balloon.Position.above)
            }

            override fun onOperationCancelled(message: String) {
                hideEditionPanel()
            }
        })
        return editionPanel
    }

    override fun isSelectedNodeId(treeNode: NoSqlTreeNode): Boolean {
        val descriptor = treeNode.descriptor
        return descriptor is KeyValueDescriptor && descriptor.key == idDescriptorKey
    }
}