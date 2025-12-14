package com.example.wechat_sim.presentation.search

import com.example.wechat_sim.data.model.Contact
import com.example.wechat_sim.data.model.ChatRoom
import com.example.wechat_sim.presentation.BaseView
import com.example.wechat_sim.presentation.BasePresenter

// 搜索结果的封装类
data class SearchResult(
    val contact: Contact? = null,
    val chatRoom: ChatRoom? = null
) {
    val isContact: Boolean get() = contact != null
    val isChatRoom: Boolean get() = chatRoom != null
}

interface SearchContract {

    interface View : BaseView {
        fun showSearchResults(results: List<SearchResult>)
    }

    interface Presenter : BasePresenter {
        fun attachView(view: View)
        fun detachView()
        fun searchContacts(query: String)
    }
}