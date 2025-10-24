package com.example.catchai.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.catchai.viewmodel.ChatEvent
import com.example.catchai.viewmodel.ChatViewModel
import com.google.ai.client.generativeai.type.TextPart

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
            LazyColumn(modifier = Modifier.weight(1f).padding(vertical = 8.dp)) {
                items(uiState.chatHistory) { content ->
                    val text = (content.parts.first() as? TextPart)?.text ?: "Error: Could not load text"
                    val role = if (content.role == "user") "User" else "Gemini"

                    Text(text = "$role: $text")
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