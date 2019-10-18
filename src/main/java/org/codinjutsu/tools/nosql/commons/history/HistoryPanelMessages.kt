package org.codinjutsu.tools.nosql.commons.history

import com.intellij.util.messages.Topic

internal interface HistoryPanelMessages {

    fun add(historyList: HistoryList)

    fun add(vendorName: String, index: Int, element: HistoryItem)

    fun removeAt(vendorName: String, index: Int)

    companion object {
        val TOPIC = Topic.create("Messages for history panel", HistoryPanelMessages::class.java)
    }
}