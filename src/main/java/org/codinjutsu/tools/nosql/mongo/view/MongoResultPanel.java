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

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.MessageType;
import com.intellij.openapi.ui.popup.Balloon;
import com.mongodb.DBObject;
import org.apache.commons.lang.StringUtils;
import org.codinjutsu.tools.nosql.commons.utils.GuiUtils;
import org.codinjutsu.tools.nosql.commons.view.ActionCallback;
import org.codinjutsu.tools.nosql.commons.view.EditionPanel;
import org.codinjutsu.tools.nosql.commons.view.NoSQLResultPanelDocumentOperations;
import org.codinjutsu.tools.nosql.commons.view.NoSqlTreeNode;
import org.codinjutsu.tools.nosql.commons.view.nodedescriptor.keyvalue.TypedKeyValueDescriptor;
import org.codinjutsu.tools.nosql.commons.view.nodedescriptor.NodeDescriptor;
import org.codinjutsu.tools.nosql.commons.view.panel.AbstractNoSQLResultPanel;
import org.codinjutsu.tools.nosql.mongo.model.MongoResult;
import org.codinjutsu.tools.nosql.mongo.view.columninfo.MongoWriteableColumnInfoDecider;
import org.codinjutsu.tools.nosql.mongo.view.model.MongoTreeModelFactory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class MongoResultPanel extends AbstractNoSQLResultPanel<MongoResult, DBObject> {

    MongoResultPanel(Project project, NoSQLResultPanelDocumentOperations<DBObject> noSQLResultPanelDocumentOperations) {
        super(project, noSQLResultPanelDocumentOperations, new MongoTreeModelFactory(), "_id"); //NON-NLS
    }

    @Nullable
    @Override
    protected EditionPanel<DBObject> createEditionPanel() {
        EditionPanel<DBObject> mongoEditionPanel = new EditionPanel<>(new MongoTreeModelFactory(), new MongoWriteableColumnInfoDecider());
        mongoEditionPanel.init(getDocumentOperations(), new ActionCallback() {
            public void onOperationSuccess(@NotNull String message) {
                hideEditionPanel();
                GuiUtils.showNotification(getResultTreePanel(), MessageType.INFO, message, Balloon.Position.above);
            }

            @Override
            public void onOperationFailure(@NotNull Exception exception) {
                GuiUtils.showNotification(getResultTreePanel(), MessageType.ERROR, exception.getMessage(), Balloon.Position.above);
            }

            @Override
            public void onOperationCancelled(@NotNull String message) {
                hideEditionPanel();
            }
        });
        return mongoEditionPanel;
    }

    @Override
    protected boolean isSelectedNodeId(@NotNull NoSqlTreeNode treeNode) {
        NodeDescriptor descriptor = treeNode.getDescriptor();
        return descriptor instanceof TypedKeyValueDescriptor && StringUtils.equals(((TypedKeyValueDescriptor) descriptor).getKey(), "_id");
    }
}
