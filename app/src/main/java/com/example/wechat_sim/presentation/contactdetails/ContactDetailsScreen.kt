package com.example.wechat_sim.presentation.contactdetails

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.wechat_sim.data.model.Contact
import com.example.wechat_sim.presentation.contactdetails.ContactDetailsContract
import com.example.wechat_sim.presentation.contactdetails.ContactDetailsPresenter
import com.example.wechat_sim.data.repository.DataRepository
import com.example.wechat_sim.navigation.Routes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContactDetailsScreen(
    navController: NavController,
    contactUserId: String
) {
    val context = LocalContext.current

    // MVP setup
    val repository = remember { DataRepository(context) }
    val presenter = remember { ContactDetailsPresenter(repository) }

    var contact by remember { mutableStateOf<Contact?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    // MVP View implementation
    val view = remember {
        object : ContactDetailsContract.View {
            override fun showContactDetails(contactData: Contact) {
                contact = contactData
                isLoading = false
            }

            override fun showFriendProfile() {
                // TODO: Navigate to friend profile edit
            }

            override fun showMoments() {
                navController.navigate(Routes.MOMENTS)
            }

            override fun showSendMessage() {
                contact?.let { contactData ->
                    navController.navigate(Routes.chatDetails(contactData.userId, false))
                }
            }

            override fun showVideoCall() {
                // TODO: Implement video call
            }

            override fun showMoreOptions() {
                // TODO: Implement more options
            }

            override fun showAddToContacts() {
                // TODO: Implement add to contacts
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

    // Handle back button
    BackHandler {
        navController.popBackStack()
    }

    // Initialize MVP
    LaunchedEffect(contactUserId) {
        presenter.attachView(view)

        // Create a temporary contact object with the userId to load details
        val tempContact = Contact(
            userId = contactUserId,
            userName = "", // Will be loaded from repository
            remarkName = null,
            wxId = null,
            region = null,
            avatarUrl = null,
            signature = null,
            isFriend = true
        )
        presenter.loadContactDetails(tempContact)
    }

    // Cleanup
    DisposableEffect(Unit) {
        onDispose {
            presenter.onDestroy()
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "返回",
                            tint = Color.Black
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { presenter.onMoreOptionsClicked() }) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = "更多",
                            tint = Color.Black
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White
                )
            )
        }
    ) { innerPadding ->
        when {
            isLoading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Color(0xFF07C160))
                }
            }
            errorMessage != null -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = errorMessage!!,
                        color = Color.Red
                    )
                }
            }
            contact != null -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .background(Color(0xFFEDEDED))
                ) {
                    ContactInfoSection(contact!!)
                    Spacer(modifier = Modifier.height(8.dp))
                    FunctionSection(presenter)
                    Spacer(modifier = Modifier.height(8.dp))
                    ActionButtonsSection(contact!!, presenter)
                }
            }
        }
    }
}

@Composable
private fun ContactInfoSection(contact: Contact) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(0.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalAlignment = Alignment.Top
        ) {
            if (!contact.avatarUrl.isNullOrBlank()) {
                AsyncImage(
                    model = "file:///android_asset/${contact.avatarUrl}",
                    contentDescription = "头像",
                    modifier = Modifier
                        .size(80.dp)
                        .clip(RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Crop
                )
            } else {
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xFF07C160)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "头像",
                        tint = Color.White,
                        modifier = Modifier.size(40.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(20.dp))

            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = contact.remarkName ?: contact.userName,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )

                if (!contact.remarkName.isNullOrBlank() && contact.remarkName != contact.userName) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "昵称：${contact.userName}",
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                }

                if (!contact.wxId.isNullOrBlank()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "微信号：${contact.wxId}",
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                }
            }
        }
    }
}

@Composable
private fun FunctionSection(presenter: ContactDetailsPresenter) {
    Column {
        FunctionItem(
            title = "朋友资料",
            subtitle = "添加朋友的备注名、电话、标签、备忘，并设置朋友权限。",
            icon = Icons.Default.Person,
            iconColor = Color(0xFF07C160),
            onClick = { presenter.onFriendProfileClicked() }
        )

        FunctionItem(
            title = "朋友圈",
            subtitle = null,
            icon = Icons.Default.Star,
            iconColor = Color(0xFF576B95),
            onClick = { presenter.onMomentsClicked() }
        )
    }
}

@Composable
private fun FunctionItem(
    title: String,
    subtitle: String?,
    icon: ImageVector,
    iconColor: Color,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(0.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = iconColor,
                modifier = Modifier.size(24.dp)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    fontSize = 17.sp,
                    color = Color.Black,
                    fontWeight = FontWeight.Normal
                )

                subtitle?.let {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = it,
                        fontSize = 12.sp,
                        color = Color.Gray,
                        lineHeight = 16.sp
                    )
                }
            }

            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = "进入",
                tint = Color.Gray,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

@Composable
private fun ActionButtonsSection(contact: Contact, presenter: ContactDetailsPresenter) {
    if (contact.isFriend) {
        // 如果是好友，显示发消息和音视频通话按钮
        Column {
            ActionButtonItem(
                icon = Icons.AutoMirrored.Filled.Send,
                text = "发消息",
                onClick = { presenter.onSendMessageClicked() }
            )

            ActionButtonItem(
                icon = Icons.Default.Call,
                text = "音视频通话",
                onClick = { presenter.onVideoCallClicked() }
            )
        }
    } else {
        // 如果不是好友，只显示添加到通讯录按钮
        ActionButtonItem(
            icon = Icons.Default.Add,
            text = "添加到通讯录",
            onClick = { presenter.onAddToContactsClicked() }
        )
    }
}

@Composable
private fun ActionButtonItem(
    icon: ImageVector,
    text: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(0.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = text,
                tint = Color(0xFF576B95),
                modifier = Modifier.size(24.dp)
            )

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = text,
                fontSize = 17.sp,
                color = Color(0xFF576B95),
                fontWeight = FontWeight.Normal
            )
        }
    }
}