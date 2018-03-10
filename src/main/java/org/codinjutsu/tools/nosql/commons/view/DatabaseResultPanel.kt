package org.codinjutsu.tools.nosql.commons.view

import com.intellij.openapi.project.Project
import org.codinjutsu.tools.nosql.commons.model.DataType
import org.codinjutsu.tools.nosql.commons.model.internal.layer.DatabaseNodeDescriptorFactory
import org.codinjutsu.tools.nosql.commons.model.internal.layer.DatabaseArray
import org.codinjutsu.tools.nosql.commons.model.internal.layer.DatabaseObject
import org.codinjutsu.tools.nosql.commons.view.columninfo.WriteableColumnInfoDecider
import org.codinjutsu.tools.nosql.commons.view.panel.NoSQLResultPanel

internal class DatabaseResultPanel(
        project: Project,
        databasePanel: DatabasePanel,
        idDescriptorKey: String,
        dataTypes: Array<DataType>
) : NoSQLResultPanel(project, databasePanel, true, DatabaseNodeDescriptorFactory(), idDescriptorKey, dataTypes) {

    override fun writeableColumnInfoDecider() = object : WriteableColumnInfoDecider {
        override fun isNodeWriteable(treeNode: NoSqlTreeNode) =
                treeNode.descriptor.value !is DatabaseObject && treeNode.descriptor.value !is DatabaseArray
    }
}