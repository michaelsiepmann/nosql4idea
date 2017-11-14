package org.codinjutsu.tools.nosql.elasticsearch.view

import com.google.gson.JsonObject
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.MessageType
import com.intellij.openapi.ui.popup.Balloon
import org.codinjutsu.tools.nosql.commons.utils.GuiUtils
import org.codinjutsu.tools.nosql.commons.view.AbstractNoSQLResultPanel
import org.codinjutsu.tools.nosql.commons.view.ActionCallback
import org.codinjutsu.tools.nosql.commons.view.EditionPanel
import org.codinjutsu.tools.nosql.commons.view.NoSQLResultPanelDocumentOperations
import org.codinjutsu.tools.nosql.commons.view.NoSqlTreeNode
import org.codinjutsu.tools.nosql.elasticsearch.model.ElasticsearchResult

internal class ElasticsearchResultPanel(project: Project, documentOPerations: NoSQLResultPanelDocumentOperations<JsonObject>) : AbstractNoSQLResultPanel<ElasticsearchResult, JsonObject>(project, documentOPerations, ElasticsearchTreeModelFactory()) {

    override fun createEditionPanel(): EditionPanel<JsonObject> {
        val elasticsearchEditionPanel = EditionPanel<JsonObject>(ElasticsearchTreeModelFactory())
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
        val obj = treeNode.descriptor
        return super.isSelectedNodeId(treeNode)
    }
}