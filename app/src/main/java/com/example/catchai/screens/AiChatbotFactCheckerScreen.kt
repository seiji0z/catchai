package com.example.catchai.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
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
        topBar = {
            TopAppBar(
                title = { Text("CATCH AI") },
                navigationIcon = {
                    IconButton(onClick = { /* Handle dashboard toggle */ }) {
                        Icon(Icons.Default.Menu, contentDescription = "Dashboard")
                    }
                }
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .padding(16.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.CheckCircle, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "AI Fact Checker", style = MaterialTheme.typography.headlineSmall)
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text("Keep in mind:")
            Text("  • Be specific")
            Text("  • Include context")
            Text("  • Focus on one claim at a time")

            // Chat history
            LazyColumn(modifier = Modifier.weight(1f).padding(vertical = 8.dp), reverseLayout = true) {
                items(uiState.chatHistory.reversed()) { message ->
                    when (message) {
                        is ChatMessage.ModelFactCheck -> {
                            FactCheckResultContent(result = message.result)
                        }
                        else -> {
                            ChatBubble(message = message)
                        }
                    }
                }
            }

            if (uiState.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
            }

            uiState.errorMessage?.let {
                Text(text = it, color = MaterialTheme.colorScheme.error)
                Spacer(modifier = Modifier.height(8.dp))
            }

            // Message input
            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = { /* Handle file upload */ }) {
                    Icon(Icons.Default.Add, contentDescription = null)
                }
                IconButton(onClick = { /* Handle microphone */ }) {
                    Icon(Icons.Default.Mic, contentDescription = null)
                }
                OutlinedTextField(
                    value = uiState.message,
                    onValueChange = { chatViewModel.onEvent(ChatEvent.OnMessageChange(it)) },
                    placeholder = { Text("Type your query...") },
                    modifier = Modifier.weight(1f)
                )
                IconButton(
                    onClick = { chatViewModel.onEvent(ChatEvent.OnSendMessage(uiState.message)) },
                    enabled = !uiState.isLoading
                ) {
                    Icon(Icons.Default.Send, contentDescription = null)
                }
            }
        }
    }
}

@Composable
private fun ChatBubble(message: ChatMessage) {
    val isUserMessage = message is ChatMessage.UserMessage
    val backgroundColor = if (isUserMessage) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant
    val textColor = if (isUserMessage) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        contentAlignment = if (isUserMessage) Alignment.CenterEnd else Alignment.CenterStart
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .clip(RoundedCornerShape(12.dp))
                .background(backgroundColor)
                .padding(16.dp)
        ) {
            when (message) {
                is ChatMessage.UserMessage -> {
                    Text(text = message.text, color = textColor)
                }
                is ChatMessage.ModelError -> {
                    Text(text = message.message, color = textColor)
                }
                else -> { /* ModelFactCheck is handled separately */ }
            }
        }
    }
}

@Composable
private fun FactCheckResultContent(result: FactCheckResult) {
    val uriHandler = LocalUriHandler.current

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Verification Status
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Verification Status: ", style = MaterialTheme.typography.bodyLarge)
                Text(
                    if (result.isReal) "Real" else "Not Real",
                    color = if (result.isReal) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyLarge
                )
            }

            // Confidence Score
            Column {
                Text("Confidence Score: ${result.confidenceScore}%", style = MaterialTheme.typography.bodyLarge)
                Spacer(modifier = Modifier.height(4.dp))
                LinearProgressIndicator(
                    progress = result.confidenceScore / 100f,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            // Detailed Analysis
            Column {
                Text("Detailed Analysis:", style = MaterialTheme.typography.bodyLarge)
                Text(result.detailedAnalysis, style = MaterialTheme.typography.bodyMedium)
            }

            // Trusted Sources
            Column {
                Text("Trusted Sources:", style = MaterialTheme.typography.bodyLarge)
                result.trustedSources.forEach { sourceUrl ->
                    ClickableText(
                        text = AnnotatedString(sourceUrl),
                        onClick = {
                            try {
                                uriHandler.openUri(sourceUrl)
                            } catch (e: Exception) {
                                // Handle cases where URI is invalid or no app can handle it
                            }
                        },
                        style = TextStyle(
                            color = MaterialTheme.colorScheme.secondary,
                            textDecoration = TextDecoration.Underline
                        ),
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }
        }
    }
}