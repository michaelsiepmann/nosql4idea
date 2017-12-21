package org.codinjutsu.tools.nosql.commons.logic;

import org.codinjutsu.tools.nosql.commons.model.SearchResult;
import org.codinjutsu.tools.nosql.commons.model.DatabaseContext;
import org.codinjutsu.tools.nosql.commons.view.panel.query.QueryOptions;

public interface LoadableDatabaseClient<CONTEXT extends DatabaseContext, RESULT extends SearchResult, DOCUMENT> extends
        DatabaseClient<CONTEXT, DOCUMENT> {
    RESULT loadRecords(CONTEXT context, QueryOptions query);
}
