package org.codinjutsu.tools.nosql.couchbase

import com.intellij.openapi.project.Project
import org.codinjutsu.tools.nosql.DatabaseVendorInformation
import org.codinjutsu.tools.nosql.commons.model.DataType.NUMBER
import org.codinjutsu.tools.nosql.commons.model.DataType.STRING
import org.codinjutsu.tools.nosql.commons.model.DatabaseServer
import org.codinjutsu.tools.nosql.couchbase.logic.CouchbaseClient
import org.codinjutsu.tools.nosql.couchbase.model.explorer.CouchbaseDatabaseServerFolder

internal object CouchbaseVendorInformation : DatabaseVendorInformation() {

    override fun availableDataTypes() = arrayOf(STRING, NUMBER)

    override fun createDatabaseServerFolder(databaseServer: DatabaseServer, project: Project) =
            CouchbaseDatabaseServerFolder(databaseServer)

    override fun getDatabaseUIClass() = CouchbaseUI::class.java

    override fun getDatabaseClientClass() = CouchbaseClient::class.java


}