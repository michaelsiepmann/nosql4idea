package org.codinjutsu.tools.nosql.commons.view

import com.intellij.openapi.project.Project
import org.codinjutsu.tools.nosql.commons.model.internal.layer.DatabaseArray
import org.codinjutsu.tools.nosql.commons.model.internal.layer.DatabaseObject
import org.codinjutsu.tools.nosql.commons.view.columninfo.WriteableColumnInfoDecider
import org.codinjutsu.tools.nosql.commons.view.nodedescriptor.internal.JsonNodeDescriptorFactory
import org.codinjutsu.tools.nosql.commons.view.panel.NoSQLResultPanel

internal class DatabaseResultPanel(
        project: Project,
        databasePanel: DatabasePanel<DatabaseObject>,
        idDescriptorKey: String
) : NoSQLResultPanel<DatabaseObject>(project, databasePanel, true, JsonNodeDescriptorFactory(), idDescriptorKey) {

    override fun writeableColumnInfoDecider() = object : WriteableColumnInfoDecider {
        override fun isNodeWriteable(treeNode: NoSqlTreeNode) =
                treeNode.descriptor.value !is DatabaseObject && treeNode.descriptor.value !is DatabaseArray
    }
}