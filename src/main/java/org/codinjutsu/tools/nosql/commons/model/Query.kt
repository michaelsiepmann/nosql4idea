package org.codinjutsu.tools.nosql.commons.model

import org.codinjutsu.tools.nosql.commons.view.panel.query.QueryOptions

interface Query<out OPTIONS : QueryOptions> {

    val queryOptions: OPTIONS
}
