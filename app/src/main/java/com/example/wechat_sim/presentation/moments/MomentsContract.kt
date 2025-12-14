package com.example.wechat_sim.presentation.moments

import com.example.wechat_sim.model.MomentsPost
import com.example.wechat_sim.model.Contact
import com.example.wechat_sim.presentation.BaseView
import com.example.wechat_sim.presentation.BasePresenter

interface MomentsContract {
    interface View : BaseView {
        fun showMoments(moments: List<MomentsPost>)
        fun navigateToContactDetails(contact: Contact)
    }

    interface Presenter : BasePresenter {
        fun attachView(view: View)
        fun detachView()
        fun loadMoments()
        fun toggleLike(postId: String)
        fun onUserClicked(userId: String)
        fun addComment(postId: String, content: String)
    }
}