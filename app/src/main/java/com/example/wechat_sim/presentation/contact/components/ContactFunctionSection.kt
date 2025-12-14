package com.example.wechat_sim.presentation.contact.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * 联系人功能区组件
 */
@Composable
fun ContactFunctionSection() {
    val functionItems = listOf(
        FunctionItem("新的朋友", Icons.Default.Person, Color(0xFFFF8A00)),
        FunctionItem("群聊", Icons.Default.Person, Color(0xFF07C160)),
        FunctionItem("标签", Icons.Default.Star, Color(0xFF576B95)),
        FunctionItem("服务号", Icons.Default.Settings, Color(0xFFE74C3C))
    )

    Column {
        functionItems.forEach { item ->
            FunctionItemRow(
                item = item,
                onClick = {
                    // TODO: 处理功能点击
                }
            )
        }

        // 分隔间距
        Spacer(modifier = Modifier.height(8.dp))
    }
}

/**
 * 功能项数据类
 */
data class FunctionItem(
    val title: String,
    val icon: ImageVector,
    val iconColor: Color
)

/**
 * 功能项行组件
 */
@Composable
private fun FunctionItemRow(
    item: FunctionItem,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        shape = RoundedCornerShape(0.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 0.dp
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 图标
            Icon(
                imageVector = item.icon,
                contentDescription = item.title,
                tint = item.iconColor,
                modifier = Modifier.size(24.dp)
            )

            Spacer(modifier = Modifier.width(16.dp))

            // 标题
            Text(
                text = item.title,
                fontSize = 17.sp,
                color = Color.Black,
                modifier = Modifier.weight(1f)
            )

            // 右箭头
            Icon(
                imageVector = Icons.Default.KeyboardArrowRight,
                contentDescription = "进入",
                tint = Color.Gray,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}