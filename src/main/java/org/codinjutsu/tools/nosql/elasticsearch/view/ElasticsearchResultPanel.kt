package org.codinjutsu.tools.nosql.elasticsearch.view

import com.google.gson.JsonObject
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.MessageType
import com.intellij.openapi.ui.popup.Balloon
import org.codinjutsu.tools.nosql.commons.utils.GuiUtils
import org.codinjutsu.tools.nosql.commons.view.ActionCallback
import org.codinjutsu.tools.nosql.commons.view.EditionPanel
import org.codinjutsu.tools.nosql.commons.view.NoSQLResultPanelDocumentOperations
import org.codinjutsu.tools.nosql.commons.view.NoSqlTreeNode
import org.codinjutsu.tools.nosql.commons.view.columninfo.WriteableColumnInfoDecider
import org.codinjutsu.tools.nosql.commons.view.nodedescriptor.AbstractKeyValueDescriptor
import org.codinjutsu.tools.nosql.commons.view.panel.AbstractNoSQLResultPanel
import org.codinjutsu.tools.nosql.elasticsearch.model.ElasticsearchResult

internal class ElasticsearchResultPanel(project: Project, documentOPerations: NoSQLResultPanelDocumentOperations<JsonObject>) :
        AbstractNoSQLResultPanel<ElasticsearchResult, JsonObject>(project, documentOPerations, ElasticsearchTreeModelFactory()) {

    override fun createEditionPanel(): EditionPanel<JsonObject>? {
        val elasticsearchEditionPanel = EditionPanel<JsonObject>(ElasticsearchTreeModelFactory(), object : WriteableColumnInfoDecider {
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