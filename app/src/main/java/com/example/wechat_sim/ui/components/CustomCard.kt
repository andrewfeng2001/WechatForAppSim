package com.example.wechat_sim.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * 可复用的卡片组件
 *
 * @param modifier 修饰符
 * @param onClick 点击事件回调，如果为null则不可点击
 * @param shape 卡片形状，默认为无圆角（微信风格）
 * @param backgroundColor 背景颜色，默认为白色
 * @param elevation 阴影高度，默认为0（微信风格）
 * @param content 卡片内容
 */
@Composable
fun CustomCard(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    shape: Shape = RoundedCornerShape(0.dp),
    backgroundColor: Color = Color.White,
    elevation: Dp = 0.dp,
    content: @Composable ColumnScope.() -> Unit
) {
    val clickModifier = if (onClick != null) {
        modifier.clickable { onClick() }
    } else {
        modifier
    }

    Card(
        modifier = clickModifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = backgroundColor
        ),
        shape = shape,
        elevation = CardDefaults.cardElevation(
            defaultElevation = elevation
        )
    ) {
        Column(content = content)
    }
}