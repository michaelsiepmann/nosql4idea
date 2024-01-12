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

package org.codinjutsu.tools.nosql.commons.view.authentication;

import org.apache.commons.lang3.StringUtils;
import org.codinjutsu.tools.nosql.commons.model.AuthenticationSettings;
import org.jetbrains.annotations.NotNull;

import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class DefaultAuthenticationPanel implements AuthenticationView {

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JPanel mainPanel;

    public DefaultAuthenticationPanel() {
        usernameField.setName("usernameField"); //NON-NLS
        passwordField.setName("passwordField"); //NON-NLS
    }

    @NotNull
    @Override
    public JPanel getComponent() {
        return mainPanel;
    }

    @NotNull
    @Override
    public AuthenticationSettings create() {
        AuthenticationSettings authenticationSettings = new AuthenticationSettings();
        authenticationSettings.setUsername(getUsername());
        authenticationSettings.setPassword(getPassword());
        return authenticationSettings;
    }

    @Override
    public void load(@NotNull AuthenticationSettings settings) {
        usernameField.setText(settings.getUsername());
        passwordField.setText(settings.getPassword());
    }

    private String getUsername() {
        String username = usernameField.getText();
        if (StringUtils.isNotBlank(username)) {
            return username;
        }
        return null;
    }

    private String getPassword() {
        char[] password = passwordField.getPassword();
        if (password != null && password.length != 0) {
            return String.valueOf(password);
        }
        return null;
    }
}
