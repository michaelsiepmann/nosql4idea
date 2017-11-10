package org.codinjutsu.tools.nosql.elasticsearch.view;

import org.codinjutsu.tools.nosql.commons.model.AuthenticationSettings;
import org.codinjutsu.tools.nosql.commons.view.AuthenticationView;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class ElasticsearchAuthenticationPanel implements AuthenticationView {

    private JPanel rootPanel;

    @NotNull
    @Override
    public JPanel getComponent() {
        return rootPanel;
    }

    @NotNull
    @Override
    public AuthenticationSettings create() {
        return new AuthenticationSettings();
    }

    @Override
    public void load(@NotNull AuthenticationSettings settings) {
    }
}
