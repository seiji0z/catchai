package com.example.catchai.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onMediaDetectorClick: () -> Unit,
    onFactCheckerClick: () -> Unit,
    onReportContentClick: () -> Unit
) {
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
            Text(text = "Welcome back, User!", style = MaterialTheme.typography.headlineSmall)
            Spacer(modifier = Modifier.height(32.dp))
            Text(text = "Quick Actions", style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(16.dp))
            QuickActionCard(
                title = "AI Media Detector",
                description = "Detect AI-generated images, videos, or voices.",
                icon = Icons.Default.Search,
                shadowColor = Color.Blue,
                onClick = onMediaDetectorClick
            )
            Spacer(modifier = Modifier.height(16.dp))
            QuickActionCard(
                title = "AI Chatbot Fact-Checker",
                description = "Verify headlines, quotes, or URLs.",
                icon = Icons.Default.CheckCircle,
                shadowColor = Color.Green,
                onClick = onFactCheckerClick
            )
            Spacer(modifier = Modifier.height(16.dp))
            QuickActionCard(
                title = "Report Content",
                description = "Flag malicious or abusive AI use.",
                icon = Icons.Default.Info,
                shadowColor = Color.Red,
                onClick = onReportContentClick
            )
        }
    }
}

@Composable
fun QuickActionCard(
    title: String,
    description: String,
    icon: ImageVector,
    shadowColor: Color,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(elevation = 4.dp, spotColor = shadowColor)
            .clickable(onClick = onClick),
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(imageVector = icon, contentDescription = null, modifier = Modifier.size(40.dp))
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(text = title, style = MaterialTheme.typography.titleMedium)
                Text(text = description, style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}