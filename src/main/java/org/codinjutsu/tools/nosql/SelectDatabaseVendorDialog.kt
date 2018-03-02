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

package org.codinjutsu.tools.nosql

import com.intellij.openapi.ui.ComboBox
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.ui.ColoredListCellRenderer
import org.codinjutsu.tools.nosql.i18n.getResourceString
import java.awt.BorderLayout
import java.awt.BorderLayout.CENTER
import java.awt.BorderLayout.WEST
import java.awt.Component
import javax.swing.DefaultComboBoxModel
import javax.swing.JLabel
import javax.swing.JList
import javax.swing.JPanel

class SelectDatabaseVendorDialog internal constructor(parent: Component) : DialogWrapper(parent, true) {

    private val mainPanel = JPanel()
    private val databaseVendorCombobox = ComboBox<DatabaseVendor>()

    val selectedDatabaseVendor: DatabaseVendor
        get() = databaseVendorCombobox.selectedItem as DatabaseVendor

    init {
        databaseVendorCombobox.name = "databaseVendorCombobox" //NON-NLS

        mainPanel.layout = BorderLayout()
        mainPanel.add(JLabel(getResourceString("settings.dialog.selectdatabase.label")), WEST)
        mainPanel.add(databaseVendorCombobox, CENTER)

        init()
    }

    override fun createCenterPanel() = mainPanel

    override fun init() {
        super.init()
        initCombobox()
    }

    private fun initCombobox() {
        databaseVendorCombobox.apply {
            model = DefaultComboBoxModel(DatabaseVendor.values())
            renderer = object : ColoredListCellRenderer<DatabaseVendor>() {
                override fun customizeCellRenderer(list: JList<out DatabaseVendor>, databaseVendor: DatabaseVendor?, index: Int, selected: Boolean, hasFocus: Boolean) {
                    databaseVendor?.let {
                        icon = it.icon
                        append(it.vendorName)
                    }
                }
            }
            selectedItem = DatabaseVendor.MONGO
        }
    }
}
