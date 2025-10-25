
package com.example.catchai.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.catchai.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onMediaDetectorClick: () -> Unit,
    onFactCheckerClick: () -> Unit,
    onReportContentClick: () -> Unit,
    onMenuClick: () -> Unit
) {
    var isVisible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) { isVisible = true }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Home",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFFE0E0E0),
                            fontSize = 18.sp
                        )
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onMenuClick) {
                        Icon(
                            Icons.Default.Menu,
                            contentDescription = "Menu",
                            tint = Color(0xFFE0E0E0)
                        )
                    }
                },
                actions = {
                    Image(
                        painter = painterResource(id = R.drawable.logo),
                        contentDescription = "CatchAI Logo",
                        modifier = Modifier
                            .size(38.dp)
                            .padding(end = 12.dp)
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF121212)
                )
            )
        },
        containerColor = Color(0xFF0A0A0A)
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 20.dp, vertical = 24.dp)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color(0xFF0A0A0A), Color(0xFF1C1C1E))
                    )
                )
        ) {
            AnimatedVisibility(
                visible = isVisible,
                enter = slideInVertically(animationSpec = tween(700)) + fadeIn(animationSpec = tween(700)),
                exit = fadeOut(animationSpec = tween(400))
            ) {
                Column {
                    Text(
                        text = "Welcome back, User!",
                        style = MaterialTheme.typography.headlineSmall.copy(
                            color = Color.White,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.SemiBold
                        ),
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                    Text(
                        text = "Stay informed. Verify content before sharing.",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = Color(0xFFB0B0B0),
                            fontSize = 13.sp
                        ),
                        modifier = Modifier.padding(bottom = 24.dp)
                    )

                    Text(
                        text = "⚡ Quick Actions",
                        style = MaterialTheme.typography.titleMedium.copy(
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        ),
                        modifier = Modifier.padding(bottom = 14.dp)
                    )

                    QuickActionCard(
                        title = "AI Media Detector",
                        description = "Analyze images and videos to detect AI-generated content.",
                        iconRes = R.drawable.image_icon,
                        accentColor = Color(0xFF60A5FA),
                        onClick = onMediaDetectorClick
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    QuickActionCard(
                        title = "AI Chatbot Fact-Checker",
                        description = "Get instant fact-checking for claims and misinformation.",
                        iconRes = R.drawable.fact_icon,
                        accentColor = Color(0xFF34D399),
                        onClick = onFactCheckerClick
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    QuickActionCard(
                        title = "Report Content",
                        description = "Report suspicious or harmful content to improve detection.",
                        iconRes = R.drawable.flag_icon,
                        accentColor = Color(0xFFF87171),
                        onClick = onReportContentClick
                    )
                }
            }
        }
    }
}

@Composable
fun QuickActionCard(
    title: String,
    description: String,
    iconRes: Int,
    accentColor: Color,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .shadow(6.dp, RoundedCornerShape(16.dp))
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1A1A1A))
    ) {
        Row(
            modifier = Modifier
                .background(
                    Brush.horizontalGradient(
                        listOf(Color(0xFF1A1A1A), accentColor.copy(alpha = 0.1f))
                    )
                )
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = iconRes),
                contentDescription = null,
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(8.dp))
            )
            Spacer(modifier = Modifier.width(14.dp))
            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleSmall.copy(
                        color = Color.White,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 15.sp
                    )
                )
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = Color(0xFFB0B0B0),
                        fontSize = 12.sp
                    ),
                    modifier = Modifier.padding(top = 3.dp)
                )
            }
        }
    }
}
