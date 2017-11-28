package org.codinjutsu.tools.nosql.commons.view.panel.query

interface QueryOptions {
    val operations: String?
    val resultLimit: Int
    val filter: String?
    val projection: String?
    val sort: String?
    val page: Page?
}