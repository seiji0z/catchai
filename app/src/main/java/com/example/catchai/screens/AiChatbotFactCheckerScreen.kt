package com.example.catchai.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.filled.Mic


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AiChatbotFactCheckerScreen() {
    var message by remember { mutableStateOf("") }

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
            Box(modifier = Modifier.weight(1f)){

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
                    value = message,
                    onValueChange = { message = it },
                    placeholder = { Text("Type your query...") },
                    modifier = Modifier.weight(1f)
                )
                IconButton(onClick = { /* Handle send */ }) {
                    Icon(Icons.Default.Send, contentDescription = null)
                }
            }
        }
    }
}