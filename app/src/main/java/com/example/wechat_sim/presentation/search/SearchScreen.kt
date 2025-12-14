package com.example.wechat_sim.presentation.search

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.wechat_sim.data.model.Contact
import com.example.wechat_sim.data.model.ChatRoom
import com.example.wechat_sim.presentation.search.SearchContract
import com.example.wechat_sim.presentation.search.SearchPresenter
import com.example.wechat_sim.presentation.search.SearchResult
import com.example.wechat_sim.data.repository.DataRepository
import com.example.wechat_sim.navigation.Routes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(navController: NavController) {
    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current

    // MVP状态
    val repository = remember { DataRepository(context) }
    val presenter = remember { SearchPresenter(repository) }

    var searchQuery by remember { mutableStateOf("") }
    var searchResults by remember { mutableStateOf<List<SearchResult>>(emptyList()) }
    var isLoading by remember { mutableStateOf(false) }

    // MVP View实现
    val view = remember {
        object : SearchContract.View {
            override fun showSearchResults(results: List<SearchResult>) {
                searchResults = results
                isLoading = false
            }

            override fun showLoading() {
                isLoading = true
            }

            override fun hideLoading() {
                isLoading = false
            }

            override fun showError(message: String) {
                // 可以显示Toast或Snackbar
                isLoading = false
            }
        }
    }

    // 处理返回按键
    BackHandler {
        navController.popBackStack()
    }

    // 初始化MVP
    LaunchedEffect(Unit) {
        presenter.attachView(view)
    }

    // 清理
    DisposableEffect(Unit) {
        onDispose {
            presenter.onDestroy()
        }
    }

    // 搜索逻辑
    LaunchedEffect(searchQuery) {
        if (searchQuery.isNotEmpty()) {
            presenter.searchContacts(searchQuery)
        } else {
            searchResults = emptyList()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // 顶部搜索栏
        TopAppBar(
            title = {
                SearchBar(
                    searchQuery = searchQuery,
                    onQueryChanged = { searchQuery = it },
                    onSearch = { keyboardController?.hide() }
                )
            },
            navigationIcon = {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "返回",
                        tint = Color.Black
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.White
            ),
            modifier = Modifier.fillMaxWidth()
        )

        // 搜索结果列表
        if (isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Color(0xFF07C160))
            }
        } else if (searchQuery.isNotEmpty() && searchResults.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "未找到相关结果",
                        color = Color.Gray,
                        fontSize = 16.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "可搜索联系人或群聊",
                        color = Color.Gray,
                        fontSize = 14.sp
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(vertical = 8.dp)
            ) {
                items(searchResults) { searchResult ->
                    if (searchResult.isContact) {
                        ContactSearchItem(
                            contact = searchResult.contact!!,
                            onClick = {
                                navController.navigate(Routes.contactDetails(searchResult.contact.userId))
                            }
                        )
                    } else if (searchResult.isChatRoom) {
                        ChatRoomSearchItem(
                            chatRoom = searchResult.chatRoom!!,
                            onClick = {
                                navController.navigate(Routes.chatDetails(searchResult.chatRoom.chatRoomId, true))
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SearchBar(
    searchQuery: String,
    onQueryChanged: (String) -> Unit,
    onSearch: () -> Unit
) {
    OutlinedTextField(
        value = searchQuery,
        onValueChange = onQueryChanged,
        placeholder = {
            Text(
                text = "搜索",
                color = Color.Gray
            )
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "搜索",
                tint = Color.Gray,
                modifier = Modifier.size(20.dp)
            )
        },
        trailingIcon = {
            if (searchQuery.isNotEmpty()) {
                IconButton(onClick = { onQueryChanged("") }) {
                    Icon(
                        imageVector = Icons.Default.Clear,
                        contentDescription = "清除",
                        tint = Color.Gray,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        },
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Search
        ),
        keyboardActions = KeyboardActions(
            onSearch = { onSearch() }
        ),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color(0xFF07C160),
            unfocusedBorderColor = Color.LightGray,
            cursorColor = Color(0xFF07C160),
            focusedTextColor = Color.Black,
            unfocusedTextColor = Color.Black
        ),
        shape = RoundedCornerShape(20.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(40.dp)
    )
}

@Composable
private fun ContactSearchItem(
    contact: Contact,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 头像
        AsyncImage(
            model = "file:///android_asset/${contact.avatarUrl}",
            contentDescription = contact.userName,
            modifier = Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(8.dp)),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.width(12.dp))

        // 联系人信息
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = if (!contact.remarkName.isNullOrEmpty()) contact.remarkName else contact.userName,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Black
            )
            if (!contact.remarkName.isNullOrEmpty()) {
                Text(
                    text = "微信号：${contact.wxId}",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }
        }
    }
}

@Composable
private fun ChatRoomSearchItem(
    chatRoom: ChatRoom,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 群头像
        AsyncImage(
            model = "file:///android_asset/${chatRoom.avatarUrl}",
            contentDescription = chatRoom.name,
            modifier = Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(8.dp)),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.width(12.dp))

        // 群聊信息
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = chatRoom.name,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Black
            )
            Text(
                text = "${chatRoom.memberIds.size}人",
                fontSize = 12.sp,
                color = Color.Gray
            )
        }
    }
}