package com.example.wechat_sim.presentation

interface BasePresenter {
    fun onDestroy()
}

interface BaseView {
    fun showLoading()
    fun hideLoading()
    fun showError(message: String)
}