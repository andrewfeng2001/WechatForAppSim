package com.example.wechat_sim.presentation.moments

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.wechat_sim.data.model.Contact
import com.example.wechat_sim.data.model.MomentsPost
import com.example.wechat_sim.presentation.moments.MomentsContract
import com.example.wechat_sim.presentation.moments.MomentsPresenter
import com.example.wechat_sim.data.repository.DataRepository
import com.example.wechat_sim.navigation.Routes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MomentsScreen(navController: NavController) {
    val context = LocalContext.current

    // MVP setup
    val repository = remember { DataRepository(context) }
    val presenter = remember { MomentsPresenter(repository) }

    var momentsPosts by remember { mutableStateOf<List<MomentsPost>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var currentUser by remember { mutableStateOf<Contact?>(null) }
    var contacts by remember { mutableStateOf<List<Contact>>(emptyList()) }

    // MVP View implementation
    val view = remember {
        object : MomentsContract.View {
            override fun showMoments(posts: List<MomentsPost>) {
                momentsPosts = posts
                isLoading = false
            }

            override fun showLoading() {
                isLoading = true
            }

            override fun hideLoading() {
                isLoading = false
            }

            override fun showError(message: String) {
                isLoading = false
            }


            override fun navigateToContactDetails(contact: Contact) {
                navController.navigate(Routes.contactDetails(contact.userId))
            }
        }
    }

    // Handle back button
    BackHandler {
        navController.popBackStack()
    }

    // Initialize MVP
    LaunchedEffect(Unit) {
        presenter.attachView(view)
        presenter.loadMoments()

        // Load user and contacts data
        currentUser = repository.getCurrentUser()
        contacts = repository.getContacts()
    }

    // Cleanup
    DisposableEffect(Unit) {
        onDispose {
            presenter.onDestroy()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // Top bar
        TopAppBar(
            title = { Text("朋友圈", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold) },
            navigationIcon = {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "返回",
                        tint = Color.White
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color(0xFF07C160)
            )
        )

        // Content
        if (isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Color(0xFF07C160))
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(vertical = 16.dp)
            ) {
                // User info header
                item {
                    currentUser?.let { user ->
                        UserInfoHeader(user = user)
                        Divider(
                            color = Color(0xFFF5F5F5),
                            thickness = 8.dp,
                            modifier = Modifier.padding(vertical = 16.dp)
                        )
                    }
                }

                // Moments posts
                items(momentsPosts) { post ->
                    val author = contacts.find { it.userId == post.authorId }
                    if (author != null) {
                        MomentsPostItem(
                            post = post,
                            author = author,
                            onAuthorClick = { view.navigateToContactDetails(author) },
                            onLikeClick = { presenter.toggleLike(post.postId) },
                            onCommentClick = { /* Handle comment */ }
                        )

                        Divider(
                            color = Color(0xFFF0F0F0),
                            thickness = 1.dp,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun UserInfoHeader(user: Contact) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // User avatar
        AsyncImage(
            model = "file:///android_asset/${user.avatarUrl}",
            contentDescription = user.userName,
            modifier = Modifier
                .size(50.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.width(12.dp))

        // User name
        Text(
            text = user.userName,
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium,
            color = Color.Black
        )
    }
}

@Composable
private fun MomentsPostItem(
    post: MomentsPost,
    author: Contact,
    onAuthorClick: () -> Unit,
    onLikeClick: () -> Unit,
    onCommentClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        // Author info
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.clickable { onAuthorClick() }
        ) {
            AsyncImage(
                model = "file:///android_asset/${author.avatarUrl}",
                contentDescription = author.userName,
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(8.dp))

            Column {
                Text(
                    text = if (!author.remarkName.isNullOrEmpty()) author.remarkName else author.userName,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Black
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Post content
        if (!post.content.isNullOrEmpty()) {
            Text(
                text = post.content,
                fontSize = 14.sp,
                color = Color.Black,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
        }

        // Post images
        if (post.images.isNotEmpty()) {
            post.images.forEach { imageUrl ->
                AsyncImage(
                    model = "file:///android_asset/$imageUrl",
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .clip(RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
        }

        // Action buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            TextButton(onClick = onLikeClick) {
                Icon(
                    imageVector = Icons.Default.Favorite,
                    contentDescription = "点赞",
                    tint = if (post.likes.contains("current_user")) Color.Red else Color.Gray,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = if (post.likes.isNotEmpty()) "${post.likes.size}" else "",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }

            TextButton(onClick = onCommentClick) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "评论",
                    tint = Color.Gray,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = if (post.comments.isNotEmpty()) "${post.comments.size}" else "",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }
        }
    }
}