package org.codinjutsu.tools.nosql.commons.history

import com.intellij.util.messages.Topic

internal interface HistorySelectedMessage {
    fun historyItemSelected(vendor: String, historyItem: HistoryItem)

    companion object {
        val TOPIC: Topic<HistorySelectedMessage> = Topic.create("History Message", HistorySelectedMessage::class.java)
    }
}