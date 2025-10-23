package com.example.catchai.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportContentScreen(onSubmitReportClick: () -> Unit) {
    var reportType by remember { mutableStateOf("Fake News") }
    var contentUrl by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var additionalInfo by remember { mutableStateOf("") }

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
                Icon(Icons.Default.Info, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "Report Content", style = MaterialTheme.typography.headlineSmall)
            }
            Spacer(modifier = Modifier.height(16.dp))

            // Report Type
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Report Type")
                IconButton(onClick = { /* show dropdown */ }) {
                    Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                }
            }
            OutlinedTextField(value = reportType, onValueChange = {}, readOnly = true, modifier = Modifier.fillMaxWidth())

            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(value = contentUrl, onValueChange = { contentUrl = it }, label = { Text("Content URL (Optional)") }, modifier = Modifier.fillMaxWidth())

            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(value = description, onValueChange = { description = it }, label = { Text("Content Description") }, modifier = Modifier.fillMaxWidth(), maxLines = 5)

            Spacer(modifier = Modifier.height(16.dp))
            // Evidence upload
            Text("Evidence (Optional)")
            Spacer(modifier = Modifier.height(8.dp))
            Box(modifier = Modifier.fillMaxWidth().height(100.dp)){
                Text("Upload Box for PNG, JPG, or PDF files", modifier = Modifier.align(Alignment.Center))
            }

            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(value = additionalInfo, onValueChange = { additionalInfo = it }, label = { Text("Additional Information") }, modifier = Modifier.fillMaxWidth(), maxLines = 3)

            Spacer(modifier = Modifier.height(32.dp))
            Button(onClick = onSubmitReportClick, modifier = Modifier.align(Alignment.End)) {
                Text("Submit Report")
            }
        }
    }
}