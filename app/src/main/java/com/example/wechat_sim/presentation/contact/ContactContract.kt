package com.example.wechat_sim.presentation.contact

import com.example.wechat_sim.model.Contact
import com.example.wechat_sim.presentation.BasePresenter
import com.example.wechat_sim.presentation.BaseView

interface ContactContract {

    interface View : BaseView {
        fun showContactList(contacts: List<ContactGroup>)
        fun navigateToContactDetail(contact: Contact)
        fun showSearchResults(results: List<Contact>)
    }

    interface Presenter : BasePresenter {
        fun attachView(view: View)
        fun loadContacts()
        fun onContactClicked(contact: Contact)
        fun searchContacts(query: String)
        fun refreshContacts()
    }
}

data class ContactGroup(
    val title: String,
    val contacts: List<Contact>
)