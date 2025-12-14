package com.example.wechat_sim.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

/**
 * 可复用的头像组件
 *
 * @param avatarUrl 头像URL，可以为null
 * @param contentDescription 内容描述
 * @param modifier 修饰符
 * @param size 头像大小，默认40dp
 * @param shape 头像形状，默认为圆角矩形
 * @param onClick 点击事件回调，如果为null则不可点击
 */
@Composable
fun AvatarImage(
    avatarUrl: String?,
    contentDescription: String,
    modifier: Modifier = Modifier,
    size: Dp = 40.dp,
    shape: Shape = RoundedCornerShape(8.dp),
    onClick: (() -> Unit)? = null
) {
    val clickModifier = if (onClick != null) {
        modifier.clickable { onClick() }
    } else {
        modifier
    }

    if (!avatarUrl.isNullOrBlank()) {
        AsyncImage(
            model = "file:///android_asset/$avatarUrl",
            contentDescription = contentDescription,
            modifier = clickModifier
                .size(size)
                .clip(shape),
            contentScale = ContentScale.Crop
        )
    } else {
        Box(
            modifier = clickModifier
                .size(size)
                .clip(shape)
                .background(Color(0xFF07C160)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = contentDescription,
                tint = Color.White,
                modifier = Modifier.size(size * 0.6f)
            )
        }
    }
}

/**
 * 圆形头像的便捷方法
 */
@Composable
fun CircularAvatarImage(
    avatarUrl: String?,
    contentDescription: String,
    modifier: Modifier = Modifier,
    size: Dp = 40.dp,
    onClick: (() -> Unit)? = null
) {
    AvatarImage(
        avatarUrl = avatarUrl,
        contentDescription = contentDescription,
        modifier = modifier,
        size = size,
        shape = CircleShape,
        onClick = onClick
    )
}