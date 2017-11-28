package org.codinjutsu.tools.nosql.commons.view.panel

import org.codinjutsu.tools.nosql.commons.view.panel.query.Page

internal interface Pageable {

    fun getCurrentPage() : Page

    fun moveToPage(page: Page)
}