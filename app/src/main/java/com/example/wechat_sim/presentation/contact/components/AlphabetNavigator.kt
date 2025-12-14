package com.example.wechat_sim.presentation.contact.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * 字母导航组件
 */
@Composable
fun AlphabetNavigator(
    letters: List<String>,
    onLetterClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    // 按A-Z，最后#的顺序排列
    val allLetters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray().map { it.toString() } + "#"

    Column(
        modifier = modifier
            .background(Color.Transparent)
            .padding(vertical = 16.dp, horizontal = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        allLetters.forEach { letter ->
            Text(
                text = letter,
                fontSize = 12.sp,
                color = Color(0xFF999999),
                modifier = Modifier
                    .clickable {
                        // 所有字母都可以点击，包括#
                        onLetterClick(letter)
                    }
                    .padding(vertical = 0.1.dp),
                fontWeight = if (letters.contains(letter)) FontWeight.Bold else FontWeight.Normal
            )
        }
    }
}