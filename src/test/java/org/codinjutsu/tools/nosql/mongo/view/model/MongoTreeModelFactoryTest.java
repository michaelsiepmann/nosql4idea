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

package org.codinjutsu.tools.nosql.mongo.view.model;

import com.mongodb.DBObject;
import com.mongodb.util.JSON;
import org.apache.commons.io.IOUtils;
import org.bson.types.ObjectId;
import org.codinjutsu.tools.nosql.commons.view.NoSqlTreeNode;
import org.codinjutsu.tools.nosql.commons.view.nodedescriptor.result.StandardResultDescriptor;
import org.junit.Test;

import java.nio.charset.Charset;

import static org.junit.Assert.assertEquals;

public class MongoTreeModelFactoryTest {

    @Test
    public void buildDBObjectFromSimpleTree() throws Exception {
        DBObject jsonObject = (DBObject) JSON.parse(IOUtils.toString(getClass().getResourceAsStream("simpleDocument.json"), Charset.defaultCharset()));

//        Hack to convert _id fron string to ObjectId
        jsonObject.put("_id", new ObjectId(String.valueOf(jsonObject.get("_id"))));

        NoSqlTreeNode treeNode = buildJsonTree(jsonObject);
        NoSqlTreeNode labelNode = (NoSqlTreeNode) treeNode.getChildAt(1);
        labelNode.getDescriptor().setValue("tata");


        DBObject dbObject = new MongoTreeModelFactory().buildDBObject(treeNode);

        assertEquals("{ \"_id\" : { \"$oid\" : \"50b8d63414f85401b9268b99\"} , \"label\" : \"tata\" , \"visible\" : false , \"image\" :  null }",
                dbObject.toString());
    }

    @Test
    public void buildDBObjectFromTreeWithSubNodes() throws Exception {
        DBObject jsonObject = (DBObject) JSON.parse(IOUtils.toString(getClass().getResourceAsStream("simpleDocumentWithInnerNodes.json"), Charset.defaultCharset()));

//        Hack to convert _id fron string to ObjectId
        jsonObject.put("_id", new ObjectId(String.valueOf(jsonObject.get("_id"))));

        NoSqlTreeNode treeNode = buildJsonTree(jsonObject);

//      Simulate updating from the treeNode
        NoSqlTreeNode innerDocNode = (NoSqlTreeNode) treeNode.getChildAt(4);
        NoSqlTreeNode soldOutNode = (NoSqlTreeNode) innerDocNode.getChildAt(2);
        soldOutNode.getDescriptor().setValue("false");

        DBObject dbObject = new MongoTreeModelFactory().buildDBObject(treeNode);

        assertEquals("{ \"_id\" : { \"$oid\" : \"50b8d63414f85401b9268b99\"} , \"label\" : \"toto\" , \"visible\" : false , \"image\" :  null  , \"innerdoc\" : { \"title\" : \"What?\" , \"numberOfPages\" : 52 , \"soldOut\" : false}}",
                dbObject.toString());
    }

    @Test
    public void buildDBObjectFromTreeWithSubList() throws Exception {
        DBObject jsonObject = (DBObject) JSON.parse(IOUtils.toString(getClass().getResourceAsStream("simpleDocumentWithSubList.json"), Charset.defaultCharset()));

//        Hack to convert _id fron string to ObjectId
        jsonObject.put("_id", new ObjectId(String.valueOf(jsonObject.get("_id"))));

        NoSqlTreeNode treeNode = buildJsonTree(jsonObject);
        NoSqlTreeNode tagsNode = (NoSqlTreeNode) treeNode.getChildAt(2);
        NoSqlTreeNode agileTagNode = (NoSqlTreeNode) tagsNode.getChildAt(2);
        agileTagNode.getDescriptor().setValue("a gilles");

        DBObject dbObject = new MongoTreeModelFactory().buildDBObject(treeNode);

        assertEquals("{ \"_id\" : { \"$oid\" : \"50b8d63414f85401b9268b99\"} , \"title\" : \"XP by example\" , \"tags\" : [ \"pair programming\" , \"tdd\" , \"a gilles\"] , \"innerList\" : [ [ 1 , 2 , 3 , 4] , [ false , true] , [ { \"tagName\" : \"pouet\"} , { \"tagName\" : \"paf\"}]]}",
                dbObject.toString());
    }

    @Test
    public void getObjectIdFromANode() throws Exception {
        DBObject jsonObject = (DBObject) JSON.parse(IOUtils.toString(getClass().getResourceAsStream("simpleDocumentWithInnerNodes.json"), Charset.defaultCharset()));
        jsonObject.put("_id", new ObjectId(String.valueOf(jsonObject.get("_id"))));

        NoSqlTreeNode treeNode = buildJsonTree(jsonObject);
        NoSqlTreeNode objectIdNode = (NoSqlTreeNode) treeNode.getChildAt(0);
        assertEquals("_id", objectIdNode.getDescriptor().getFormattedKey());
    }

    private NoSqlTreeNode buildJsonTree(DBObject mongoObject) {
        NoSqlTreeNode rootNode = new NoSqlTreeNode(new StandardResultDescriptor());//TODO crappy
        new MongoTreeModelFactory().processDbObject(rootNode, mongoObject);
        return rootNode;
    }

}
