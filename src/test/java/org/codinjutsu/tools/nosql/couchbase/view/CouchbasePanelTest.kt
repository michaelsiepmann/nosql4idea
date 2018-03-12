/*
 * Copyright (c) 2015 David Boissier
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.codinjutsu.tools.nosql.couchbase.view

import com.intellij.openapi.command.impl.DummyProject
import org.codinjutsu.tools.nosql.commons.model.Database
import org.codinjutsu.tools.nosql.commons.model.SearchResult
import org.codinjutsu.tools.nosql.commons.view.TableCellReader
import org.codinjutsu.tools.nosql.commons.view.panel.query.QueryOptions
import org.codinjutsu.tools.nosql.couchbase.configuration.CouchbaseServerConfiguration
import org.codinjutsu.tools.nosql.couchbase.logic.CouchbaseClient
import org.codinjutsu.tools.nosql.couchbase.model.CouchbaseContext
import org.fest.swing.edt.GuiActionRunner
import org.fest.swing.edt.GuiQuery
import org.fest.swing.fixture.Containers
import org.fest.swing.fixture.FrameFixture
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Matchers.any
import org.mockito.Mockito
import org.mockito.Mockito.`when`

internal class CouchbasePanelTest {

    private var frameFixture: FrameFixture? = null

    private val couchbaseClientMock = Mockito.mock(CouchbaseClient::class.java)
    private var couchbasePanelWrapper: CouchbasePanel? = null

    @BeforeEach
    fun setUp() {
        `when`(couchbaseClientMock.loadRecords(any(CouchbaseContext::class.java), any(QueryOptions::class.java))).thenReturn(SearchResult("dummy", emptyList(), 0))

        couchbasePanelWrapper = GuiActionRunner.execute(object : GuiQuery<CouchbasePanel>() {
            override fun executeInEDT(): CouchbasePanel? {
                return object : CouchbasePanel(DummyProject.getInstance(),
                        CouchbaseContext(couchbaseClientMock,
                                CouchbaseServerConfiguration(),
                                Database("default"))) {

                }
            }
        })

        frameFixture = Containers.showInFrame(couchbasePanelWrapper!!)
    }

    @AfterEach
    fun tearDown() {
        frameFixture!!.cleanUp()
    }

    @Test
    fun displayJsonObjects() {
        // TODO: 18.11.2017  couchbasePanelWrapper.updateResultTableTree(createResults());
        couchbasePanelWrapper!!.expandAll()

        val tableFixture = frameFixture!!.table("resultTreeTable")
        tableFixture.replaceCellReader(TableCellReader())
        tableFixture.requireColumnCount(2)
                .requireContents(arrayOf(arrayOf("amount", "123456764"), arrayOf("mad", "true"), arrayOf("address", "{\"City\":\"Paris\",\"ZIP Code\":75016,\"Street\":\"Av. Champs Elysées\"}"), arrayOf("Street", "\"Av. Champs Elysées\""), arrayOf("ZIP Code", "75016"), arrayOf("City", "\"Paris\""), arrayOf("interests", "[\"programming\",\"XP\",\"TDD\"]"), arrayOf("[0]", "\"programming\""), arrayOf("[1]", "\"XP\""), arrayOf("[2]", "\"TDD\""), arrayOf("movies", "[{\"title\":\"Fight Club\",\"critic\":8.2},{\"title\":\"Blade Runner\",\"critic\":9.3},{\"title\":\"Toys Story\",\"critic\":8.7}]"), arrayOf("[0]", "{\"title\":\"Fight Club\",\"critic\":8.2}"), arrayOf("title", "\"Fight Club\""), arrayOf("critic", "8.2"), arrayOf("[1]", "{\"title\":\"Blade Runner\",\"critic\":9.3}"), arrayOf("title", "\"Blade Runner\""), arrayOf("critic", "9.3"), arrayOf("[2]", "{\"title\":\"Toys Story\",\"critic\":8.7}"), arrayOf("title", "\"Toys Story\""), arrayOf("critic", "8.7"), arrayOf("age", "25"), arrayOf("score", "12345.12121"), arrayOf("firstname", "\"Jojo\"")))
    }

/*    private fun createResults(): CouchbaseSearchResult {
        val result = CouchbaseSearchResult("test")
        result.add(JsonObject.create().put("firstname", "Jojo")
                .put("age", 25)
                .put("mad", true)
                .put("interests", Arrays.asList("programming", "XP", "TDD"))
                .put("amount", 123456764L)
                .put("score", 12345.12121)
                .put("address", JsonObject.create()
                        .put("Street", "Av. Champs Elysées")
                        .put("City", "Paris")
                        .put("ZIP Code", 75016))
                .put("movies", Arrays.asList(
                        JsonObject.create()
                                .put("title", "Fight Club")
                                .put("critic", 8.2),
                        JsonObject.create()
                                .put("title", "Blade Runner")
                                .put("critic", 9.3),
                        JsonObject.create()
                                .put("title", "Toys Story")
                                .put("critic", 8.7)
                )))
        return result
    } */
}