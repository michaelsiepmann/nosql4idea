package org.codinjutsu.tools.nosql.commons.view.filedialogs

import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.openapi.ui.ValidationInfo
import java.io.File
import javax.swing.JComponent

internal class ImportFileDialog(importPanelSettings: ImportPanelSettings?, project: Project) : DialogWrapper(project) {

    private val importFile = ImportFile(importPanelSettings)

    init {
        init()
        title = "Choose file to import"
    }

    override fun createCenterPanel(): JComponent? {
        return importFile.mainPanel
    }

    override fun doValidate(): ValidationInfo? {
        return importFile.doValidate()
    }

    fun getSelectedFile(): File = importFile.selectedFile
}