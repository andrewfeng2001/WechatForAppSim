package com.example.wechat_sim.mvp.me

import com.example.wechat_sim.model.Contact
import com.example.wechat_sim.mvp.BasePresenter
import com.example.wechat_sim.mvp.BaseView

interface MeContract {

    interface View : BaseView {
        fun showUserProfile(user: Contact)
        fun navigateToProfileEdit()
        fun navigateToService()
        fun navigateToFavorites()
        fun navigateToAlbum()
        fun navigateToCardPackage()
        fun navigateToEmoji()
        fun navigateToSettings()
    }

    interface Presenter : BasePresenter {
        fun attachView(view: View)
        fun loadUserProfile()
        fun onProfileEditClicked()
        fun onServiceClicked()
        fun onFavoritesClicked()
        fun onAlbumClicked()
        fun onCardPackageClicked()
        fun onEmojiClicked()
        fun onSettingsClicked()
        fun refreshProfile()
    }
}

data class MenuSection(
    val title: String,
    val items: List<MenuItem>
)

data class MenuItem(
    val title: String,
    val icon: String? = null,
    val badge: String? = null,
    val onClick: () -> Unit
)