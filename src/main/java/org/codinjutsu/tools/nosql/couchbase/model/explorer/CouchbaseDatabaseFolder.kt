package org.codinjutsu.tools.nosql.couchbase.model.explorer

import com.intellij.openapi.project.Project
import org.codinjutsu.tools.nosql.commons.model.DatabaseServer
import org.codinjutsu.tools.nosql.commons.model.explorer.DatabaseFolder
import org.codinjutsu.tools.nosql.couchbase.model.CouchbaseDatabase
import org.codinjutsu.tools.nosql.couchbase.view.editor.CouchbaseObjectFile

internal class CouchbaseDatabaseFolder(override val data: CouchbaseDatabase, override val parent: CouchbaseDatabaseServerFolder) :
        DatabaseFolder(data) {

    override val database: CouchbaseDatabase?
        get() = data

    override val databaseServer: DatabaseServer<*>
        get() = parent.data

    override fun createNoSqlObjectFile(project: Project) =
            CouchbaseObjectFile(project, databaseServer.configuration, data)

    override fun isViewableContent() = true
}