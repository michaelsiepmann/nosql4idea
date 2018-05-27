package org.codinjutsu.tools.nosql.commons.history

import com.intellij.util.messages.Topic
import org.codinjutsu.tools.nosql.DatabaseVendor

internal interface CreateHistoryMessage {
    fun createHistoryItem(vendor: DatabaseVendor, historyItem: HistoryItem)

    companion object {
        val TOPIC: Topic<CreateHistoryMessage> = Topic.create("Create history item", CreateHistoryMessage::class.java)
    }
}