package com.example.wechat_sim.presentation.chat

import com.example.wechat_sim.model.ChatRoom
import com.example.wechat_sim.model.Contact
import com.example.wechat_sim.model.Message
import com.example.wechat_sim.presentation.BasePresenter
import com.example.wechat_sim.presentation.BaseView

interface ChatContract {

    interface View : BaseView {
        fun showChatList(chatItems: List<ChatListItem>)
        fun navigateToChatDetail(chatId: String, isGroup: Boolean)
        fun updateChatList()
    }

    interface Presenter : BasePresenter {
        fun attachView(view: View)
        fun loadChatList()
        fun onChatItemClicked(chatId: String, isGroup: Boolean)
        fun refreshChatList()
    }
}

data class ChatListItem(
    val chatId: String,
    val title: String,
    val avatarUrl: String?,
    val lastMessage: String?,
    val lastMessageTime: String?,
    val unreadCount: Int = 0,
    val isGroup: Boolean = false
)