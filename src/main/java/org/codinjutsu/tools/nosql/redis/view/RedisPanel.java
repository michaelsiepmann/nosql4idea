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

package org.codinjutsu.tools.nosql.redis.view;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import com.intellij.openapi.project.Project;
import com.intellij.ui.components.JBTextField;
import com.intellij.ui.components.panels.NonOpaquePanel;
import org.codinjutsu.tools.nosql.commons.model.DataType;
import org.codinjutsu.tools.nosql.commons.model.SearchResult;
import org.codinjutsu.tools.nosql.commons.view.DatabasePanel;
import org.codinjutsu.tools.nosql.commons.view.action.ExecuteQuery;
import org.codinjutsu.tools.nosql.commons.view.panel.NoSQLResultPanel;
import org.codinjutsu.tools.nosql.commons.view.panel.query.Page;
import org.codinjutsu.tools.nosql.commons.view.panel.query.QueryOptions;
import org.codinjutsu.tools.nosql.commons.view.panel.query.QueryOptionsImpl;
import org.codinjutsu.tools.nosql.redis.model.RedisContext;
import org.codinjutsu.tools.nosql.redis.view.action.EnableGroupingAction;
import org.codinjutsu.tools.nosql.redis.view.action.SetSeparatorAction;
import org.codinjutsu.tools.nosql.redis.view.nodedescriptor.RedisNodeDescriptorFactory;
import org.jetbrains.annotations.NotNull;

import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.BorderLayout;

import static org.apache.commons.lang.StringUtils.isNotBlank;

public class RedisPanel extends DatabasePanel {

    private RedisTreePreparator treePreparator;
    private JBTextField filterField;
    private boolean groupData;
    private String groupSeparator;

    public RedisPanel(Project project, RedisContext context) {
        super(project, context, "id"); //NON-NLS
    }

    @Override
    protected JPanel initToolBar() {
        JPanel toolBar = super.initToolBar();

        filterField = new JBTextField("*");
        filterField.setColumns(10);

        NonOpaquePanel westPanel = new NonOpaquePanel();

        NonOpaquePanel filterPanel = new NonOpaquePanel();
        filterPanel.add(new JLabel("Filter: "), BorderLayout.WEST);
        filterPanel.add(filterField, BorderLayout.CENTER);
        filterPanel.add(Box.createHorizontalStrut(5), BorderLayout.EAST);
        westPanel.add(filterPanel, BorderLayout.WEST);

        toolBar.add(westPanel, BorderLayout.WEST);

        return toolBar;
    }

    @Override
    protected void addActions(DefaultActionGroup actionResultGroup, AnAction expandAllAction, AnAction collapseAllAction) {
        actionResultGroup.add(new ExecuteQuery(this));
        actionResultGroup.addSeparator();
        actionResultGroup.add(new EnableGroupingAction(this));
        actionResultGroup.add(new SetSeparatorAction(this));
        super.addActions(actionResultGroup, expandAllAction, collapseAllAction);
    }

    private String getFilter() {
        String filter = filterField.getText();
        return isNotBlank(filter) ? filter : "*";
    }

    private void updateResultTableTree(SearchResult redisSearchResult, boolean groupByPrefix, String separator) {
        treePreparator.prepareTable(groupByPrefix, separator);
        getResultPanel().updateResultTableTree(redisSearchResult);
    }

    @Override
    public SearchResult getRecords() {
        return getSearchResult(getContext(), new QueryOptionsImpl());
    }

    @Override
    protected NoSQLResultPanel createResultPanel(Project project, String idDescriptor, DataType[] dataTypes) {
        treePreparator = new RedisTreePreparator();
        return new NoSQLResultPanel(project, this, false, new RedisNodeDescriptorFactory(), idDescriptor, dataTypes, new RedisTreeBuilder(), treePreparator);
    }

    @NotNull
    @Override
    protected QueryOptions createQueryOptions(Page currentPage) {
        QueryOptionsImpl queryOptions = (QueryOptionsImpl) super.createQueryOptions(currentPage);
        queryOptions.setFilter(getFilter());
        return queryOptions;
    }

    public boolean isGroupDataEnabled() {
        return groupData;
    }

    public void toggleGroupData(boolean enabled) {
        groupData = enabled;
        updateResultTableTree(getSearchResult(), groupData, groupSeparator);
    }

    public String getGroupSeparator() {
        return groupSeparator;
    }

    public void setGroupSeparator(String groupSeparator) {
        this.groupSeparator = groupSeparator;
        updateResultTableTree(getSearchResult(), groupData, groupSeparator);
    }

    @Override
    public void closeFindEditor() {
    }

    @Override
    public boolean isFindEditorOpened() {
        return false;
    }

    @Override
    public void openFindEditor() {
    }

    @Override
    public void focusOnEditor() {
    }
}
