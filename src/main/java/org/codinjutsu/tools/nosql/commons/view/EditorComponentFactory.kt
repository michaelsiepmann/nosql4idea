package org.codinjutsu.tools.nosql.commons.view

import com.intellij.lang.Language
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.editor.EditorFactory
import com.intellij.openapi.editor.EditorSettings
import com.intellij.openapi.editor.colors.EditorColors
import com.intellij.openapi.editor.colors.EditorColorsScheme
import com.intellij.openapi.editor.ex.EditorEx
import com.intellij.openapi.editor.ex.util.LexerEditorHighlighter
import com.intellij.openapi.editor.highlighter.EditorHighlighter
import com.intellij.openapi.fileTypes.PlainTextSyntaxHighlighterFactory
import com.intellij.openapi.project.Project

internal fun createJSONEditor(project:Project): Editor {
    val editorFactory = EditorFactory.getInstance()
    val editorDocument = editorFactory.createDocument("")
    val editor = editorFactory.createEditor(editorDocument, project)
    fillEditorSettings(editor.settings)
    val editorEx = editor as EditorEx
    editor.highlighter = editorEx.attachHighlighter()
    return editor
}

private fun fillEditorSettings(editorSettings: EditorSettings) {
    editorSettings.apply {
        isWhitespacesShown = true
        isLineMarkerAreaShown = false
        isIndentGuidesShown = false
        isLineNumbersShown = false
        isAllowSingleLogicalLineFolding = true
        additionalColumnsCount = 0
        additionalLinesCount = 1
        isUseSoftWraps = true
        setUseTabCharacter(false)
        isCaretInsideTabs = false
        isVirtualSpace = false
    }
}

private fun EditorEx.attachHighlighter(): EditorHighlighter{
    val scheme = colorsScheme
    scheme.setColor(EditorColors.CARET_ROW_COLOR, null)
    return createHighlighter(scheme)
}

private fun createHighlighter(settings: EditorColorsScheme): EditorHighlighter {
    val language = Language.findLanguageByID("JSON") ?: Language.ANY
    return LexerEditorHighlighter(PlainTextSyntaxHighlighterFactory.getSyntaxHighlighter(language!!, null, null), settings)
}
