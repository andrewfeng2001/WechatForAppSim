package com.example.wechat_sim.presentation.chatdetails

import com.example.wechat_sim.data.model.JsonMessage
import com.example.wechat_sim.data.model.Contact
import com.example.wechat_sim.presentation.BasePresenter
import com.example.wechat_sim.presentation.BaseView

interface ChatDetailsContract {

    interface View : BaseView {
        fun showMessages(messages: List<ChatMessageItem>)
        fun showChatTitle(title: String)
        fun showMenuOptions()
        fun showVoiceInput()
        fun showEmojiPanel()
        fun showMoreOptions()
        fun scrollToBottom()
        fun navigateToContactDetails(contact: Contact)
        fun clearInputText()
        fun addNewMessage(message: ChatMessageItem)
    }

    interface Presenter : BasePresenter {
        fun attachView(view: View)
        fun loadMessages(chatId: String, isGroup: Boolean)
        fun onMenuClicked()
        fun onVoiceClicked()
        fun onEmojiClicked()
        fun onMoreClicked()
        fun onSendMessage(content: String)
        fun onAvatarClicked(userId: String)
    }
}

data class ChatMessageItem(
    val messageId: String,
    val senderId: String,
    val senderName: String,
    val senderAvatarUrl: String?,
    val content: String,
    val type: String,
    val timestamp: String,
    val isFromSelf: Boolean,
    val mediaPath: String?,
    val formattedTime: String
)