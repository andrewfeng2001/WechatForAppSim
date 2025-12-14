package com.example.wechat_sim.navigation

object Routes {
    const val SEARCH = "search"
    const val MOMENTS = "moments"
    const val CONTACT_DETAILS = "contact_details/{contactId}"
    const val CHAT_DETAILS = "chat_details/{chatId}/{isGroup}"

    fun contactDetails(contactId: String): String {
        return "contact_details/$contactId"
    }

    fun chatDetails(chatId: String, isGroup: Boolean): String {
        return "chat_details/$chatId/$isGroup"
    }
}