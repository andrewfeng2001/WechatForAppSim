package com.example.wechat_sim.presentation.contactdetails

import com.example.wechat_sim.data.model.Contact
import com.example.wechat_sim.presentation.BasePresenter
import com.example.wechat_sim.presentation.BaseView

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