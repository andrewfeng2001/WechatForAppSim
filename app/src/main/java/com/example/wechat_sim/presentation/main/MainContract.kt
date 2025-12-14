package com.example.wechat_sim.presentation.main

import com.example.wechat_sim.presentation.BasePresenter
import com.example.wechat_sim.presentation.BaseView

interface MainContract {

    interface View : BaseView {
        fun showMainContent()
        fun navigateToChat()
        fun navigateToContacts()
        fun navigateToDiscover()
        fun navigateToMe()
    }

    interface Presenter : BasePresenter {
        fun attachView(view: View)
        fun onTabSelected(tabIndex: Int)
        fun loadInitialData()
    }
}