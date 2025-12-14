package com.example.wechat_sim.presentation.contact

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import com.example.wechat_sim.data.model.Contact
import com.example.wechat_sim.presentation.contact.ContactContract
import com.example.wechat_sim.presentation.contact.ContactPresenter
import com.example.wechat_sim.data.repository.DataRepository
import com.example.wechat_sim.navigation.Routes
import com.example.wechat_sim.presentation.contact.components.*
import com.example.wechat_sim.ui.components.LoadingIndicator
import com.example.wechat_sim.ui.components.ErrorView
import com.example.wechat_sim.ui.components.EmptyView

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContactScreen(navController: NavController) {
    val context = LocalContext.current
    val repository = remember { DataRepository(context) }
    val presenter = remember { ContactPresenter(repository) }

    var friends by remember { mutableStateOf<List<Contact>>(emptyList()) }
    var groupedFriends by remember { mutableStateOf<Map<String, List<Contact>>>(emptyMap()) }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    val view = remember {
        object : ContactContract.View {
            override fun showContactList(contacts: List<com.example.wechat_sim.presentation.contact.ContactGroup>) {
                // 只显示好友，按字母分组
                val allContacts = contacts.flatMap { it.contacts }
                val friendsOnly = allContacts.filter { it.isFriend }
                friends = friendsOnly

                // 按首字母分组
                val grouped = friendsOnly.groupBy { contact ->
                    val displayName = contact.remarkName ?: contact.userName
                    getPinyin(displayName).first().uppercaseChar().toString()
                }

                // 自定义排序：A-Z在前，#在最后
                groupedFriends = grouped.toSortedMap { a, b ->
                    when {
                        a == "#" && b != "#" -> 1  // #排在其他字母后面
                        a != "#" && b == "#" -> -1 // 其他字母排在#前面
                        else -> a.compareTo(b)     // 其他情况按字母顺序
                    }
                }
            }

            override fun navigateToContactDetail(contact: Contact) {
                navController.navigate(Routes.contactDetails(contact.userId))
            }

            override fun showSearchResults(results: List<Contact>) {
                // 暂不处理搜索结果
            }

            override fun showLoading() {
                isLoading = true
                errorMessage = null
            }

            override fun hideLoading() {
                isLoading = false
            }

            override fun showError(message: String) {
                errorMessage = message
                isLoading = false
            }
        }
    }

    LaunchedEffect(Unit) {
        presenter.attachView(view)
        presenter.loadContacts()
    }

    DisposableEffect(Unit) {
        onDispose {
            presenter.onDestroy()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFEDEDED))
        ) {
            // 功能区
            item {
                ContactFunctionSection()
            }

            when {
                isLoading -> {
                    item {
                        LoadingIndicator(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp)
                        )
                    }
                }
                errorMessage != null -> {
                    item {
                        ErrorView(
                            message = errorMessage!!,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp)
                        )
                    }
                }
                groupedFriends.isEmpty() -> {
                    item {
                        EmptyView(
                            message = "暂无好友",
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp)
                        )
                    }
                }
                else -> {
                    // 好友列表按字母分组
                    groupedFriends.forEach { (letter, contacts) ->
                        item {
                            // 字母分组标题
                            Text(
                                text = letter,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(Color(0xFFE8E8E8))
                                    .padding(horizontal = 16.dp, vertical = 8.dp),
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color(0xFF666666)
                            )
                        }

                        items(contacts) { contact ->
                            FriendItemRow(
                                contact = contact,
                                onClick = {
                                    presenter.onContactClicked(contact)
                                }
                            )
                        }
                    }
                }
            }

            // 底部统计
            if (friends.isNotEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "${friends.size}个朋友",
                            fontSize = 14.sp,
                            color = Color.Gray
                        )
                    }
                }
            }
        }

        // 右侧字母导航
        AlphabetNavigator(
            letters = groupedFriends.keys.toList(),
            onLetterClick = { letter ->
                coroutineScope.launch {
                    // 找到对应字母的位置并滚动
                    val position = calculateScrollPosition(groupedFriends, letter)
                    if (position >= 0) {
                        listState.animateScrollToItem(position)
                    }
                }
            },
            modifier = Modifier.align(Alignment.CenterEnd)
        )
    }
}