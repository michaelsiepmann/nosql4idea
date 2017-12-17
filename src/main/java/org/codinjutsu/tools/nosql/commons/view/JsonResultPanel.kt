package org.codinjutsu.tools.nosql.commons.view

import com.google.gson.JsonObject
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.MessageType
import com.intellij.openapi.ui.popup.Balloon
import org.codinjutsu.tools.nosql.commons.model.JsonSearchResult
import org.codinjutsu.tools.nosql.commons.utils.GuiUtils
import org.codinjutsu.tools.nosql.commons.view.columninfo.WriteableColumnInfoDecider
import org.codinjutsu.tools.nosql.commons.view.nodedescriptor.AbstractKeyValueDescriptor
import org.codinjutsu.tools.nosql.commons.view.nodedescriptor.json.JsonTreeModelFactory
import org.codinjutsu.tools.nosql.commons.view.panel.AbstractNoSQLResultPanel

internal class JsonResultPanel(project: Project, documentOPerations: NoSQLResultPanelDocumentOperations<JsonObject>) :
        AbstractNoSQLResultPanel<JsonSearchResult, JsonObject>(project, documentOPerations, JsonTreeModelFactory()) {

    override fun createEditionPanel(): EditionPanel<JsonObject>? {
        val elasticsearchEditionPanel = EditionPanel<JsonObject>(JsonTreeModelFactory(), object : WriteableColumnInfoDecider {
            override fun isNodeWriteable(treeNode: NoSqlTreeNode) = treeNode.descriptor.value is JsonObject
        })
        elasticsearchEditionPanel.init(documentOperations, object : ActionCallback {
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
        return elasticsearchEditionPanel
    }

    override fun isSelectedNodeId(treeNode: NoSqlTreeNode): Boolean {
        val descriptor = treeNode.descriptor
        return descriptor is AbstractKeyValueDescriptor && descriptor.key == "_id"
    }
}