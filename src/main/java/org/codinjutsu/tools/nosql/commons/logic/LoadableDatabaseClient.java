package org.codinjutsu.tools.nosql.commons.logic;

import org.codinjutsu.tools.nosql.ServerConfiguration;
import org.codinjutsu.tools.nosql.commons.model.Database;
import org.codinjutsu.tools.nosql.commons.model.Query;
import org.codinjutsu.tools.nosql.commons.model.SearchResult;
import org.codinjutsu.tools.nosql.commons.view.DatabaseContext;

public interface LoadableDatabaseClient<CONTEXT extends DatabaseContext, RESULT extends SearchResult, QUERY extends Query, DOCUMENT> extends DatabaseClient<CONTEXT, DOCUMENT> {
    RESULT loadRecords(ServerConfiguration configuration, Database database, QUERY query);
}
