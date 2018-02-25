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

package org.codinjutsu.tools.nosql.mongo.view;

import org.junit.jupiter.api.Disabled;

@Disabled("The JsonTreeTableView was not found by it's name")
class MongoSearchResultPanelTest {

/*
    this code doesn't work anymore. Must be refactored

    // // TODO: 25.02.2018  private MongoResultPanel mongoResultPanel;

    private FrameFixture frameFixture;

    // // TODO: 25.02.2018  private NoSQLResultPanelDocumentOperations<DBObject> noSQLResultPanelDocumentOperations;

    @AfterEach
    void tearDown() {
        frameFixture.cleanUp();
    }

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(MongoSearchResultPanelTest.class);
        // noSQLResultPanelDocumentOperations = mock(NoSQLResultPanelDocumentOperations.class);
// todo        when(noSQLResultPanelDocumentOperations.getDocument(any())).thenReturn(new BasicDBObject());

*/
/*
        mongoResultPanel = GuiActionRunner.execute(new GuiQuery<MongoResultPanel>() {
            protected MongoResultPanel executeInEDT() {
                return new MongoResultPanel(DummyProject.getInstance(), noSQLResultPanelDocumentOperations) {
                    @Override
                    protected void buildPopupMenu() {
                    }
                };
            }
        });
*//*


        frameFixture = Containers.showInFrame(mongoResultPanel);
    }

    @Test
    void displayTreeWithASimpleArray() throws Exception {
        mongoResultPanel.updateResultTableTree(createCollectionResults("simpleArray.json", "mycollec"));

        JTableFixture tableFixture = frameFixture.table("resultTreeTable");
        tableFixture.replaceCellReader(new TableCellReader());
        tableFixture.requireColumnCount(2)
                .requireContents(new String[][]{
                        {"[0]", "\"toto\""},
                        {"[1]", "true"},
                        {"[2]", "10"},
                        {"[3]", "null"},
                });
    }

    @Test
    void testDisplayTreeWithASimpleDocument() throws Exception {
        mongoResultPanel.updateResultTableTree(createCollectionResults("simpleDocument.json", "mycollec"));

        JTableFixture tableFixture = frameFixture.table("resultTreeTable");
        tableFixture.replaceCellReader(new TableCellReader());
        tableFixture.requireColumnCount(2)
                .requireContents(new String[][]{
                        {"[0]", "{ \"_id\" : \"50b8d63414f85401b9268b99\" , \"label\" : \"toto\" , \"visible\" : false , \"image\" :  null }"},
                        {"_id", "\"50b8d63414f85401b9268b99\""},
                        {"label", "\"toto\""},
                        {"visible", "false"},
                        {"image", "null"}
                });
    }


    @Test
    void testDisplayTreeWithAStructuredDocument() throws Exception {
        mongoResultPanel.updateResultTableTree(createCollectionResults("structuredDocument.json", "mycollec"));
        TreeUtil.expandAll(mongoResultPanel.getResultTableView().getTree());
        JTableFixture tableFixture = frameFixture.table("resultTreeTable");
        tableFixture.replaceCellReader(new TableCellReader());
        tableFixture.requireColumnCount(2)
                .requireContents(new String[][]{
                        {"[0]", "{ \"id\" : 0 , \"label\" : \"toto\" , \"visible\" : false , \"doc\" : { \"title\" : \"hello\" , \"nbPages\" : 10 , \"keyWord\" : [ \"toto\" , true , 10]}}"},
                        {"id", "0"},
                        {"label", "\"toto\""},
                        {"visible", "false"},
                        {"doc", "{ \"title\" : \"hello\" , \"nbPages\" : 10 , \"keyWord\" : [ \"toto\" , true , 10]}"},
                        {"title", "\"hello\""},
                        {"nbPages", "10"},
                        {"keyWord", "[ \"toto\" , true , 10]"},
                        {"[0]", "\"toto\""},
                        {"[1]", "true"},
                        {"[2]", "10"},
                });
    }

    @Test
    void testDisplayTreeWithAnArrayOfStructuredDocument() throws Exception {
        mongoResultPanel.updateResultTableTree(createCollectionResults("arrayOfDocuments.json", "mycollec"));

        TreeUtil.expandAll(mongoResultPanel.getResultTableView().getTree());
        JTableFixture tableFixture = frameFixture.table("resultTreeTable");
        tableFixture.replaceCellReader(new TableCellReader());
        tableFixture.requireContents(new String[][]{
                {"[0]", "{ \"id\" : 0 , \"label\" : \"toto\" , \"visible\" : false , \"doc\" : { \"title\" : \"hello\" , \"nbPages\" : 10 , \"keyWord\" : [ \"toto\" , true , 10]}}"},
                {"id", "0"},
                {"label", "\"toto\""},
                {"visible", "false"},
                {"doc", "{ \"title\" : \"hello\" , \"nbPages\" : 10 , \"keyWord\" : [ \"toto\" , true , 10]}"},
                {"title", "\"hello\""},
                {"nbPages", "10"},
                {"keyWord", "[ \"toto\" , true , 10]"},
                {"[0]", "\"toto\""},
                {"[1]", "true"},
                {"[2]", "10"},
                {"[1]", "{ \"id\" : 1 , \"label\" : \"tata\" , \"visible\" : true , \"doc\" : { \"title\" : \"ola\" , \"nbPages\" : 1 , \"keyWord\" : [ \"tutu\" , false , 10]}}"},
                {"id", "1"},
                {"label", "\"tata\""},
                {"visible", "true"},
                {"doc", "{ \"title\" : \"ola\" , \"nbPages\" : 1 , \"keyWord\" : [ \"tutu\" , false , 10]}"},
                {"title", "\"ola\""},
                {"nbPages", "1"},
                {"keyWord", "[ \"tutu\" , false , 10]"},
                {"[0]", "\"tutu\""},
                {"[1]", "false"},
                {"[2]", "10"},
        });
    }

    @Test
    void testCopyMongoObjectNodeValue() throws Exception {
        mongoResultPanel.updateResultTableTree(createCollectionResults("structuredDocument.json", "mycollec"));
        TreeUtil.expandAll(mongoResultPanel.getResultTableView().getTree());

        mongoResultPanel.getResultTableView().setRowSelectionInterval(0, 0);
        assertEquals("{ \"id\" : 0 , \"label\" : \"toto\" , \"visible\" : false , \"doc\" : { \"title\" : \"hello\" , \"nbPages\" : 10 , \"keyWord\" : [ \"toto\" , true , 10]}}", mongoResultPanel.getSelectedNodeStringifiedValue());

        mongoResultPanel.getResultTableView().setRowSelectionInterval(2, 2);
        assertEquals("\"label\" : \"toto\"", mongoResultPanel.getSelectedNodeStringifiedValue());

        mongoResultPanel.getResultTableView().setRowSelectionInterval(4, 4);
        assertEquals("\"doc\" : { \"title\" : \"hello\" , \"nbPages\" : 10 , \"keyWord\" : [ \"toto\" , true , 10]}", mongoResultPanel.getSelectedNodeStringifiedValue());
    }

    @Test
    void copyMongoResults() throws Exception {
        mongoResultPanel.updateResultTableTree(createCollectionResults("arrayOfDocuments.json", "mycollec"));

        TreeUtil.expandAll(mongoResultPanel.getResultTableView().getTree());

        JTableFixture tableFixture = frameFixture.table("resultTreeTable");
        tableFixture.replaceCellReader(new TableCellReader());
        tableFixture.requireContents(new String[][]{
                {"[0]", "{ \"id\" : 0 , \"label\" : \"toto\" , \"visible\" : false , \"doc\" : { \"title\" : \"hello\" , \"nbPages\" : 10 , \"keyWord\" : [ \"toto\" , true , 10]}}"},
                {"id", "0"},
                {"label", "\"toto\""},
                {"visible", "false"},
                {"doc", "{ \"title\" : \"hello\" , \"nbPages\" : 10 , \"keyWord\" : [ \"toto\" , true , 10]}"},
                {"title", "\"hello\""},
                {"nbPages", "10"},
                {"keyWord", "[ \"toto\" , true , 10]"},
                {"[0]", "\"toto\""},
                {"[1]", "true"},
                {"[2]", "10"},
                {"[1]", "{ \"id\" : 1 , \"label\" : \"tata\" , \"visible\" : true , \"doc\" : { \"title\" : \"ola\" , \"nbPages\" : 1 , \"keyWord\" : [ \"tutu\" , false , 10]}}"},
                {"id", "1"},
                {"label", "\"tata\""},
                {"visible", "true"},
                {"doc", "{ \"title\" : \"ola\" , \"nbPages\" : 1 , \"keyWord\" : [ \"tutu\" , false , 10]}"},
                {"title", "\"ola\""},
                {"nbPages", "1"},
                {"keyWord", "[ \"tutu\" , false , 10]"},
                {"[0]", "\"tutu\""},
                {"[1]", "false"},
                {"[2]", "10"},
        });

        assertEquals("[ " +
                        "{ \"id\" : 0 , \"label\" : \"toto\" , \"visible\" : false , \"doc\" : { \"title\" : \"hello\" , \"nbPages\" : 10 , \"keyWord\" : [ \"toto\" , true , 10]}} , " +
                        "{ \"id\" : 1 , \"label\" : \"tata\" , \"visible\" : true , \"doc\" : { \"title\" : \"ola\" , \"nbPages\" : 1 , \"keyWord\" : [ \"tutu\" , false , 10]}}" +
                        " ]",
                mongoResultPanel.getSelectedNodeStringifiedValue());
    }

    private MongoSearchResult createCollectionResults(String data, String collectionName) throws IOException {
        DBObject jsonObject = (DBObject) JSON.parse(IOUtils.toString(getClass().getResourceAsStream(data), Charset.defaultCharset()));

        MongoSearchResult mongoSearchResult = new MongoSearchResult(collectionName);
        mongoSearchResult.add(jsonObject);

        return mongoSearchResult;
    }

*/
}
