package org.codinjutsu.tools.nosql.elasticsearch.view.editor

import com.intellij.openapi.fileTypes.ex.FakeFileType
import com.intellij.openapi.vfs.VirtualFile
import org.codinjutsu.tools.nosql.commons.utils.GuiUtils
import javax.swing.Icon

internal object ElasticsearchFakeFileType : FakeFileType() {

    private val ICON : Icon = GuiUtils.loadIcon("elasticsearch.png")

    override fun getName() = "ELASTICSEARCH"

    override fun getDescription() = name

    override fun isMyFileType(file: VirtualFile) = false

    override fun getDefaultExtension() = "json"

    override fun getIcon() = ICON
}