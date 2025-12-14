package com.example.wechat_sim.mvp.contactdetails

import com.example.wechat_sim.model.Contact
import com.example.wechat_sim.mvp.BasePresenter
import com.example.wechat_sim.mvp.BaseView

interface ContactDetailsContract {

    interface View : BaseView {
        fun showContactDetails(contact: Contact)
        fun showFriendProfile()
        fun showMoments()
        fun showSendMessage()
        fun showVideoCall()
        fun showMoreOptions()
        fun showAddToContacts()
    }

    interface Presenter : BasePresenter {
        fun attachView(view: View)
        fun loadContactDetails(contact: Contact)
        fun onFriendProfileClicked()
        fun onMomentsClicked()
        fun onSendMessageClicked()
        fun onVideoCallClicked()
        fun onMoreOptionsClicked()
        fun onAddToContactsClicked()
    }
}