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

import com.intellij.openapi.ui.ComboBox;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.ui.ColoredListCellRenderer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Component;

import static java.awt.BorderLayout.CENTER;
import static java.awt.BorderLayout.WEST;

public class SelectDatabaseVendorDialog extends DialogWrapper {

    private JPanel mainPanel = new JPanel();
    private ComboBox<DatabaseVendor> databaseVendorCombobox = new ComboBox<>();

    SelectDatabaseVendorDialog(Component parent) {
        super(parent, true);
        databaseVendorCombobox.setName("databaseVendorCombobox"); //NON-NLS

        mainPanel.setLayout(new BorderLayout());
        mainPanel.add(new JLabel("Database Vendor:"), WEST);
        mainPanel.add(databaseVendorCombobox, CENTER);

        init();
    }

    @Nullable
    @Override
    protected JComponent createCenterPanel() {
        return mainPanel;
    }

    @Override
    protected void init() {
        super.init();
        initCombobox();
    }

    private void initCombobox() {

        databaseVendorCombobox.setModel(new DefaultComboBoxModel<>(DatabaseVendor.values()));
        databaseVendorCombobox.setRenderer(new ColoredListCellRenderer<DatabaseVendor>() {
            @Override
            protected void customizeCellRenderer(@NotNull JList list, DatabaseVendor databaseVendor, int index, boolean selected, boolean hasFocus) {
                setIcon(databaseVendor.icon);
                append(databaseVendor.name);
            }
        });

        databaseVendorCombobox.setSelectedItem(DatabaseVendor.MONGO);
    }

    DatabaseVendor getSelectedDatabaseVendor() {
        return (DatabaseVendor) databaseVendorCombobox.getSelectedItem();
    }
}
