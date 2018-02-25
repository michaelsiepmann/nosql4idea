package org.codinjutsu.tools.nosql.commons.view

import com.intellij.openapi.project.Project
import org.codinjutsu.tools.nosql.commons.model.DatabaseContext
import org.codinjutsu.tools.nosql.commons.view.panel.NoSQLResultPanel
import org.codinjutsu.tools.nosql.commons.view.panel.Pageable
import org.codinjutsu.tools.nosql.commons.view.panel.query.QueryPanel

internal open class SimpleDatabasePanel(project: Project, context: DatabaseContext, idDescriptor: String, queryPanelFactory: (Project) -> QueryPanel? = { p -> null }) :
        DatabasePanel(project, context, idDescriptor, queryPanelFactory), Pageable {

    override fun createResultPanel(project: Project, idDescriptor: String): NoSQLResultPanel? =
            DatabaseResultPanel(project, this, idDescriptor)
}