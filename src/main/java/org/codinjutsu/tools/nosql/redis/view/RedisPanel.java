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
import org.apache.commons.lang.StringUtils;
import org.codinjutsu.tools.nosql.ServerConfiguration;
import org.codinjutsu.tools.nosql.commons.model.NoSQLCollection;
import org.codinjutsu.tools.nosql.commons.view.DatabasePanel;
import org.codinjutsu.tools.nosql.commons.view.NoSQLResultPanelDocumentOperationsImpl;
import org.codinjutsu.tools.nosql.commons.view.action.ExecuteQuery;
import org.codinjutsu.tools.nosql.commons.view.panel.AbstractNoSQLResultPanel;
import org.codinjutsu.tools.nosql.commons.view.panel.query.QueryOptions;
import org.codinjutsu.tools.nosql.commons.view.panel.query.QueryOptionsImpl;
import org.codinjutsu.tools.nosql.redis.RedisContext;
import org.codinjutsu.tools.nosql.redis.logic.RedisClient;
import org.codinjutsu.tools.nosql.redis.model.RedisResult;
import org.codinjutsu.tools.nosql.redis.view.action.EnableGroupingAction;
import org.codinjutsu.tools.nosql.redis.view.action.SetSeparatorAction;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;

public class RedisPanel extends DatabasePanel<ServerConfiguration, RedisClient, RedisContext, RedisResult, Object> {

    private JBTextField filterField;
    private boolean groupData;
    private String groupSeparator;

    public RedisPanel(Project project, RedisContext context) {
        super(project, context);
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
        if (StringUtils.isNotBlank(filter)) {
            return filter;
        }
        return "*";
    }

    void updateResultTableTree(RedisResult redisResult, boolean groupByPrefix, String separator) {
        RedisResultPanel resultPanel = (RedisResultPanel) getResultPanel();
        resultPanel.prepareTable(groupByPrefix, separator);
        resultPanel.updateResultTableTree(redisResult);
    }

    @Override
    public Object getRecords() {
        return getSearchResult(getContext(), new QueryOptionsImpl());
    }

    @Override
    protected AbstractNoSQLResultPanel<RedisResult, Object> createResultPanel(Project project, RedisContext context) {
        return new RedisResultPanel(project, new NoSQLResultPanelDocumentOperationsImpl<ServerConfiguration, NoSQLCollection, RedisClient, RedisContext, RedisResult, Object>(this));
    }

    @Override
    protected RedisResult getSearchResult(RedisContext context, QueryOptions queryOptions) {
        return context.getClient().loadRecords(context, queryOptions);
    }

    @NotNull
    @Override
    protected QueryOptions createQueryOptions() {
        QueryOptionsImpl queryOptions = (QueryOptionsImpl) super.createQueryOptions();
        queryOptions.setFilter(getFilter());
        return queryOptions;
    }

    public boolean isGroupDataEnabled() {
        return this.groupData;
    }

    public void toggleGroupData(boolean enabled) {
        this.groupData = enabled;
        updateResultTableTree(getSearchResult(), this.groupData, this.groupSeparator);
    }

    public String getGroupSeparator() {
        return groupSeparator;
    }

    public void setGroupSeparator(String groupSeparator) {
        this.groupSeparator = groupSeparator;
        updateResultTableTree(getSearchResult(), this.groupData, this.groupSeparator);
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
