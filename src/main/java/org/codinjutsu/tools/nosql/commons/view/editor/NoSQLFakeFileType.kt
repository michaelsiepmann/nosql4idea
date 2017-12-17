package org.codinjutsu.tools.nosql.commons.view.editor

import com.intellij.openapi.fileTypes.ex.FakeFileType
import com.intellij.openapi.vfs.VirtualFile
import javax.swing.Icon

class NoSQLFakeFileType(
        private val name: String,
        private val icon: Icon?
) : FakeFileType() {

    override fun getName() = name

    override fun getDescription() = name

    override fun isMyFileType(file: VirtualFile) = false

    override fun getIcon() = icon

    override fun getDefaultExtension() = "json"
}
