package com.example.catchai.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun DetectionResultsScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Preview of the file
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Info, contentDescription = null, tint = Color.Red)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("AI Generated", style = MaterialTheme.typography.titleLarge, color = Color.Red)
                }
                Spacer(modifier = Modifier.height(16.dp))
                Box(modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)) {
                    // Image/Video Preview
                    Text("File Preview", modifier = Modifier.align(Alignment.Center))
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text("File_Name.jpg", style = MaterialTheme.typography.bodyMedium)
                Text("1.2 MB", style = MaterialTheme.typography.bodySmall)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Detection Results
        Text("Detection Results", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(16.dp))
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                ResultRow("Verification Status:", "AI-Generated")
                ResultRow("Confidence Score:", "95%")
                // Add the progress bar here
                Text("This content is likely AI-generated. Our analysis detected patterns consistent with known AI-generation techniques.", style = MaterialTheme.typography.bodyMedium)
                TextButton(onClick = { /* Handle view original file */ }) {
                    Text("View Original File")
                }
            }
        }
    }
}

@Composable
fun ResultRow(label: String, value: String) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(label, style = MaterialTheme.typography.bodyLarge)
        Text(value, style = MaterialTheme.typography.bodyLarge)
    }
    Spacer(modifier = Modifier.height(8.dp))
}