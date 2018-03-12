package org.codinjutsu.tools.nosql.commons.view

import com.intellij.openapi.project.Project
import org.codinjutsu.tools.nosql.commons.model.DataType
import org.codinjutsu.tools.nosql.commons.model.DatabaseContext
import org.codinjutsu.tools.nosql.commons.model.internal.layer.DatabaseArray
import org.codinjutsu.tools.nosql.commons.model.internal.layer.DatabaseNodeDescriptorFactory
import org.codinjutsu.tools.nosql.commons.model.internal.layer.DatabaseObject
import org.codinjutsu.tools.nosql.commons.view.columninfo.WriteableColumnInfoDecider
import org.codinjutsu.tools.nosql.commons.view.panel.NoSQLResultPanel
import org.codinjutsu.tools.nosql.commons.view.panel.Pageable
import org.codinjutsu.tools.nosql.commons.view.panel.query.QueryPanel

internal open class SimpleDatabasePanel(project: Project, context: DatabaseContext, idDescriptor: String, queryPanelFactory: (Project) -> QueryPanel? = { _ -> null }) :
        DatabasePanel(project, context, idDescriptor, queryPanelFactory), Pageable {

    override fun createResultPanel(project: Project, idDescriptor: String, dataTypes: Array<DataType>): NoSQLResultPanel? =
            object : NoSQLResultPanel(project, this, true, DatabaseNodeDescriptorFactory(), idDescriptor, dataTypes) {
                override fun writeableColumnInfoDecider() = object : WriteableColumnInfoDecider {
                    override fun isNodeWriteable(treeNode: NoSqlTreeNode) =
                            treeNode.descriptor.value !is DatabaseObject && treeNode.descriptor.value !is DatabaseArray
                }
            }
}