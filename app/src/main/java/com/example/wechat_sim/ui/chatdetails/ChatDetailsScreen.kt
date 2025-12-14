package com.example.wechat_sim.ui.chatdetails

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.wechat_sim.data.model.Contact
import com.example.wechat_sim.presentation.chatdetails.ChatDetailsContract
import com.example.wechat_sim.presentation.chatdetails.ChatDetailsPresenter
import com.example.wechat_sim.presentation.chatdetails.ChatMessageItem
import com.example.wechat_sim.data.repository.DataRepository
import com.example.wechat_sim.navigation.Routes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatDetailsScreen(
    navController: NavController,
    chatId: String,
    isGroup: Boolean
) {
    val context = LocalContext.current

    // MVP setup
    val repository = remember { DataRepository(context) }
    val presenter = remember { ChatDetailsPresenter(repository) }

    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var chatTitle by remember { mutableStateOf("") }
    var messages by remember { mutableStateOf<List<ChatMessageItem>>(emptyList()) }
    var inputText by remember { mutableStateOf("") }

    val listState = rememberLazyListState()

    // MVP View implementation
    val view = remember {
        object : ChatDetailsContract.View {
            override fun showMessages(messageList: List<ChatMessageItem>) {
                messages = messageList
                isLoading = false
            }

            override fun showChatTitle(title: String) {
                chatTitle = title
            }

            override fun showMenuOptions() {
                // TODO: Implement menu options
            }

            override fun showVoiceInput() {
                // TODO: Implement voice input
            }

            override fun showEmojiPanel() {
                // TODO: Implement emoji panel
            }

            override fun showMoreOptions() {
                // TODO: Implement more options
            }

            override fun scrollToBottom() {
                // Will be implemented in LaunchedEffect
            }

            override fun navigateToContactDetails(contact: Contact) {
                navController.navigate(Routes.contactDetails(contact.userId))
            }

            override fun clearInputText() {
                inputText = ""
            }

            override fun addNewMessage(message: ChatMessageItem) {
                messages = messages + message
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

    // Auto scroll to bottom when new messages are added
    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.size - 1)
        }
    }

    // Initialize MVP
    LaunchedEffect(chatId) {
        presenter.attachView(view)
        presenter.loadMessages(chatId, isGroup)
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
                title = {
                    Text(
                        text = chatTitle,
                        color = Color.Black,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
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
                actions = {
                    IconButton(onClick = { presenter.onMenuClicked() }) {
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
        },
        bottomBar = {
            ChatInputBar(
                inputText = inputText,
                onInputTextChange = { inputText = it },
                onSendMessage = { content ->
                    if (content.isNotBlank()) {
                        presenter.onSendMessage(content)
                    }
                }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color(0xFFEDEDED))
        ) {
            when {
                isLoading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = Color(0xFF07C160))
                    }
                }
                errorMessage != null -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = errorMessage!!,
                            color = Color.Red,
                            textAlign = TextAlign.Center
                        )
                    }
                }
                else -> {
                    MessagesList(
                        messages = messages,
                        listState = listState,
                        onAvatarClick = { userId ->
                            presenter.onAvatarClicked(userId)
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun MessagesList(
    messages: List<ChatMessageItem>,
    listState: androidx.compose.foundation.lazy.LazyListState,
    onAvatarClick: (String) -> Unit
) {
    LazyColumn(
        state = listState,
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(vertical = 16.dp)
    ) {
        items(messages) { message ->
            MessageBubble(
                message = message,
                onAvatarClick = onAvatarClick
            )
        }
    }
}

@Composable
private fun MessageBubble(
    message: ChatMessageItem,
    onAvatarClick: (String) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (message.isFromSelf) Arrangement.End else Arrangement.Start
    ) {
        if (!message.isFromSelf) {
            // Other user's avatar
            if (!message.senderAvatarUrl.isNullOrBlank()) {
                AsyncImage(
                    model = "file:///android_asset/${message.senderAvatarUrl}",
                    contentDescription = message.senderName,
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .clickable { onAvatarClick(message.senderId) },
                    contentScale = ContentScale.Crop
                )
            } else {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .background(Color(0xFF07C160))
                        .clickable { onAvatarClick(message.senderId) },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = message.senderName,
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.width(8.dp))
        }

        // Message content
        Box(
            modifier = Modifier
                .clip(
                    RoundedCornerShape(
                        topStart = 16.dp,
                        topEnd = 16.dp,
                        bottomStart = if (message.isFromSelf) 16.dp else 4.dp,
                        bottomEnd = if (message.isFromSelf) 4.dp else 16.dp
                    )
                )
                .background(
                    if (message.isFromSelf) Color(0xFF95EC69) else Color.White
                )
                .padding(12.dp)
        ) {
            Column {
                if (!message.isFromSelf && message.senderName.isNotBlank()) {
                    Text(
                        text = message.senderName,
                        fontSize = 12.sp,
                        color = Color.Gray,
                        fontWeight = FontWeight.Medium
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                }

                Text(
                    text = message.content,
                    fontSize = 16.sp,
                    color = Color.Black
                )

                if (message.formattedTime.isNotBlank()) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = message.formattedTime,
                        fontSize = 10.sp,
                        color = Color.Gray,
                        modifier = Modifier.align(Alignment.End)
                    )
                }
            }
        }

        if (message.isFromSelf) {
            Spacer(modifier = Modifier.width(8.dp))
            // Current user's avatar (simplified)
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(6.dp))
                    .background(Color(0xFF07C160)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "我",
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

@Composable
private fun ChatInputBar(
    inputText: String,
    onInputTextChange: (String) -> Unit,
    onSendMessage: (String) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.Bottom
        ) {
            // Voice input button
            IconButton(onClick = { /* TODO: Voice input */ }) {
                Icon(
                    imageVector = Icons.Default.Phone,
                    contentDescription = "语音输入",
                    tint = Color.Gray
                )
            }

            // Text input field
            OutlinedTextField(
                value = inputText,
                onValueChange = onInputTextChange,
                modifier = Modifier.weight(1f),
                placeholder = { Text("说点什么...") },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF07C160),
                    unfocusedBorderColor = Color.Gray
                ),
                maxLines = 4
            )

            Spacer(modifier = Modifier.width(8.dp))

            // Emoji button
            IconButton(onClick = { /* TODO: Emoji panel */ }) {
                Icon(
                    imageVector = Icons.Default.Face,
                    contentDescription = "表情",
                    tint = Color.Gray
                )
            }

            // Send button
            IconButton(
                onClick = {
                    onSendMessage(inputText)
                },
                enabled = inputText.isNotBlank()
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Send,
                    contentDescription = "发送",
                    tint = if (inputText.isNotBlank()) Color(0xFF07C160) else Color.Gray
                )
            }
        }
    }
}