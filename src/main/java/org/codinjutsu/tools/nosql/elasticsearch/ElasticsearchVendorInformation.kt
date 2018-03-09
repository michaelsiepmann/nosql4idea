package org.codinjutsu.tools.nosql.elasticsearch

import com.intellij.openapi.project.Project
import org.codinjutsu.tools.nosql.DatabaseVendorInformation
import org.codinjutsu.tools.nosql.commons.model.DataType
import org.codinjutsu.tools.nosql.commons.model.DatabaseServer
import org.codinjutsu.tools.nosql.elasticsearch.logic.ElasticsearchClient
import org.codinjutsu.tools.nosql.elasticsearch.model.explorer.ElasticsearchDatabaseServerFolder

internal object ElasticsearchVendorInformation : DatabaseVendorInformation() {

    override fun availableDataTypes() = DataType.values()

    override fun createDatabaseServerFolder(databaseServer: DatabaseServer, project: Project) =
            ElasticsearchDatabaseServerFolder(databaseServer, project)

    override fun getDatabaseUIClass() = ElasticsearchUI::class.java

    override fun getDatabaseClientClass() = ElasticsearchClient::class.java


}