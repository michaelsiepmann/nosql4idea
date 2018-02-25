package org.codinjutsu.tools.nosql.solr.view.editor

import com.intellij.openapi.project.Project
import org.codinjutsu.tools.nosql.commons.configuration.ServerConfiguration
import org.codinjutsu.tools.nosql.commons.model.Database
import org.codinjutsu.tools.nosql.commons.utils.GuiUtils
import org.codinjutsu.tools.nosql.commons.view.editor.NoSQLFakeFileType
import org.codinjutsu.tools.nosql.commons.view.editor.NoSqlDatabaseObjectFile
import javax.swing.Icon

internal class SolrObjectFile(project: Project, serverConfiguration: ServerConfiguration, val solrDatabase: Database) :
        NoSqlDatabaseObjectFile(project, serverConfiguration, serverConfiguration.label) {

    override fun getFileType() = FILE_TYPE

    companion object {
        val icon: Icon = GuiUtils.loadIcon("solr.png")
        private val FILE_TYPE = NoSQLFakeFileType("SOLR", icon)
    }
}