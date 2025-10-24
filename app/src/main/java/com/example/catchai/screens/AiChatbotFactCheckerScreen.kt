package com.example.catchai.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.catchai.viewmodel.ChatEvent
import com.example.catchai.viewmodel.ChatMessage
import com.example.catchai.viewmodel.ChatViewModel
import com.example.catchai.viewmodel.FactCheckResult

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AiChatbotFactCheckerScreen(
    chatViewModel: ChatViewModel = viewModel()
) {
    val uiState by chatViewModel.uiState.collectAsState()

    Scaffold(
        containerColor = Color.Black,
        bottomBar = {
            BottomInputBarWhite(
                message = uiState.message,
                isLoading = uiState.isLoading,
                onMessageChange = { chatViewModel.onEvent(ChatEvent.OnMessageChange(it)) },
                onSend = { chatViewModel.onEvent(ChatEvent.OnSendMessage(uiState.message)) },
                onUploadClick = { /* TODO: Handle file upload */ }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
                .padding(innerPadding)
                .padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (uiState.chatHistory.isEmpty()) {
                // 🟢 Welcome screen
                Spacer(modifier = Modifier.weight(1f))
                Icon(
                    Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint = Color(0xFF00E676),
                    modifier = Modifier.size(48.dp)
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "AI Fact Checker",
                    color = Color.White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Verify headlines, quotes, and claims with AI-powered\nfact-checking using trusted sources.",
                    color = Color(0xFFB0B0B0),
                    textAlign = TextAlign.Center,
                    lineHeight = 20.sp,
                    fontSize = 14.sp
                )
                Spacer(modifier = Modifier.height(28.dp))
                Text(
                    text = "When verifying, keep these in mind:",
                    color = Color.White,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(20.dp))
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        FactTipPill("Be specific")
                        FactTipPill("Include context")
                    }
                    FactTipPill("Focus on one claim at a time")
                }
                Spacer(modifier = Modifier.weight(1f))
            } else {
                // 🟡 Chat + results
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(uiState.chatHistory) { message ->
                        when (message) {
                            is ChatMessage.UserMessage -> {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp),
                                    horizontalArrangement = Arrangement.End // ✅ Align to the right
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .background(Color(0xFF3578E5), RoundedCornerShape(16.dp))
                                            .padding(horizontal = 14.dp, vertical = 10.dp)
                                    ) {
                                        Text(
                                            text = message.text,
                                            color = Color.White,
                                            fontSize = 16.sp
                                        )
                                    }
                                }
                            }

                            is ChatMessage.ModelFactCheck -> {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp),
                                    horizontalArrangement = Arrangement.Start // ✅ Align to the left
                                ) {
                                    FactCheckResultContent(result = message.result)
                                }
                            }

                            is ChatMessage.ModelError -> {
                                Text(
                                    text = "Error: ${message.message}",
                                    color = MaterialTheme.colorScheme.error,
                                    modifier = Modifier.padding(8.dp)
                                )
                            }
                        }
                    }

                }
            }

            if (uiState.isLoading) {
                CircularProgressIndicator(
                    color = Color.White,
                    modifier = Modifier.padding(top = 16.dp)
                )
            }

            uiState.errorMessage?.let {
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }
    }
}

@Composable
fun FactTipPill(text: String, isHighlighted: Boolean = false) {
    val borderColor = if (isHighlighted) Color(0xFF3578E5) else Color.White
    val textColor = if (isHighlighted) Color(0xFF3578E5) else Color.White

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .border(1.dp, borderColor, RoundedCornerShape(20.dp))
            .padding(horizontal = 20.dp, vertical = 10.dp)
    ) {
        Text(
            text = text,
            color = textColor,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
private fun FactCheckResultContent(result: FactCheckResult) {
    val uriHandler = LocalUriHandler.current

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF1E1E1E), RoundedCornerShape(12.dp))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Title Row
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.Close, contentDescription = null, tint = Color.Red, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                "Fact-Check Results",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
        }

        // Verification Status
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Verification Status:", color = Color.White)
            Box(
                modifier = Modifier
                    .background(
                        if (result.isReal) Color(0xFF00E676) else Color(0xFFB00020),
                        RoundedCornerShape(12.dp)
                    )
                    .padding(horizontal = 10.dp, vertical = 4.dp)
            ) {
                Text(
                    if (result.isReal) "TRUE" else "FALSE",
                    color = Color.White,
                    fontSize = 12.sp
                )
            }
        }

        // Confidence Score
        Column {
            Text("Confidence Score:", color = Color.White)
            LinearProgressIndicator(
                progress = result.confidenceScore / 100f,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(RoundedCornerShape(3.dp)),
                color = Color(0xFF00E676)
            )
            Text("${result.confidenceScore}%", color = Color.White, fontSize = 13.sp, modifier = Modifier.align(Alignment.End))
        }

        // Detailed Analysis
        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Info, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("Detailed Analysis", color = Color.White, fontWeight = FontWeight.SemiBold)
            }
            Text(
                result.detailedAnalysis,
                color = Color.White.copy(alpha = 0.9f),
                fontSize = 14.sp,
                lineHeight = 20.sp
            )
        }

        // Trusted Sources
        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.CheckCircle, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("Trusted Sources", color = Color.White, fontWeight = FontWeight.SemiBold)
            }

            result.trustedSources.forEach { url ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFF2C2C2E), RoundedCornerShape(10.dp))
                        .padding(12.dp)
                ) {
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(url, color = Color.White, fontSize = 13.sp, modifier = Modifier.weight(1f))
                        TextButton(onClick = { uriHandler.openUri(url) }) {
                            Text("View", color = Color.White, fontSize = 13.sp)
                        }
                    }
                }
            }
        }

        Text(
            "← Back to Dashboard",
            color = Color.White,
            fontWeight = FontWeight.SemiBold
        )
    }
}

// 🟢 White input bar
@Composable
private fun BottomInputBarWhite(
    message: String,
    isLoading: Boolean,
    onMessageChange: (String) -> Unit,
    onSend: () -> Unit,
    onUploadClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp)
            .clip(RoundedCornerShape(50))
            .background(Color.White)
            .padding(horizontal = 12.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onUploadClick) {
            Icon(Icons.Default.AttachFile, contentDescription = "Upload file", tint = Color.DarkGray)
        }

        IconButton(onClick = { /* TODO: Mic */ }) {
            Icon(Icons.Default.Mic, contentDescription = "Mic", tint = Color.Gray)
        }

        BasicTextField(
            value = message,
            onValueChange = onMessageChange,
            singleLine = true,
            textStyle = TextStyle(color = Color.Black, fontSize = 16.sp),
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 8.dp),
            decorationBox = { innerTextField ->
                if (message.isEmpty()) {
                    Text("Verify another", color = Color.Gray, fontSize = 16.sp)
                }
                innerTextField()
            }
        )

        IconButton(onClick = onSend, enabled = !isLoading) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF3578E5)),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.ArrowUpward, contentDescription = "Send", tint = Color.White, modifier = Modifier.size(18.dp))
            }
        }
    }
}
