package com.example.wechat_sim.mvp.main

import com.example.wechat_sim.mvp.BasePresenter
import com.example.wechat_sim.mvp.BaseView

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