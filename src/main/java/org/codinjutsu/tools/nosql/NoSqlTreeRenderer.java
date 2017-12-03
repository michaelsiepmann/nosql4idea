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

package org.codinjutsu.tools.nosql;

import com.intellij.icons.AllIcons;
import com.intellij.ui.ColoredTreeCellRenderer;
import org.codinjutsu.tools.nosql.commons.model.explorer.Folder;
import org.codinjutsu.tools.nosql.commons.utils.GuiUtils;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;

public class NoSqlTreeRenderer extends ColoredTreeCellRenderer {

    public static final Icon DATABASE = GuiUtils.loadIcon("database.png");
    public static final Icon MONGO_COLLECTION = AllIcons.Nodes.Folder;

    @Override
    public void customizeCellRenderer(@NotNull JTree mongoTree, Object value, boolean isSelected, boolean isExpanded, boolean isLeaf, int row, boolean focus) {
        Object object = ((DefaultMutableTreeNode) value).getUserObject();
        if ((object instanceof Folder)) {
            ((Folder) object).updateTreeCell(this);
        }
    }
}
