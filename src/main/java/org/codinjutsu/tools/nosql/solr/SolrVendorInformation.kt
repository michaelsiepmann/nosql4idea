package org.codinjutsu.tools.nosql.solr

import com.intellij.openapi.project.Project
import org.codinjutsu.tools.nosql.DatabaseVendorInformation
import org.codinjutsu.tools.nosql.commons.logic.DatabaseClient
import org.codinjutsu.tools.nosql.commons.model.DataType.NUMBER
import org.codinjutsu.tools.nosql.commons.model.DataType.STRING
import org.codinjutsu.tools.nosql.commons.model.DatabaseServer
import org.codinjutsu.tools.nosql.solr.logic.SolrClient
import org.codinjutsu.tools.nosql.solr.model.explorer.SolrDatabaseServerFolder

internal object SolrVendorInformation : DatabaseVendorInformation() {

    override fun availableDataTypes() = arrayOf(STRING, NUMBER)

    override fun createDatabaseServerFolder(databaseServer: DatabaseServer, project: Project) =
            SolrDatabaseServerFolder(databaseServer)

    override fun getDatabaseUIClass() = SolrUI::class.java

    override fun getDatabaseClientClass(): Class<out DatabaseClient> = SolrClient::class.java

}