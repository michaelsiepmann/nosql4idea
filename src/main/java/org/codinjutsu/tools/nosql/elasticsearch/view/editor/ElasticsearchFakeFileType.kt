package org.codinjutsu.tools.nosql.elasticsearch.view.editor

import com.intellij.openapi.fileTypes.ex.FakeFileType
import com.intellij.openapi.vfs.VirtualFile

internal object ElasticsearchFakeFileType : FakeFileType() {

    override fun getName() = "ELASTICSEARCH"

    override fun getDescription() = name

    override fun isMyFileType(file: VirtualFile) = false

    override fun getDefaultExtension() = "json"
}