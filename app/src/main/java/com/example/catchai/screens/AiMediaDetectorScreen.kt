package com.example.catchai.screens

import android.graphics.BitmapFactory
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.catchai.viewmodel.DetectionResult
import com.example.catchai.viewmodel.MediaDetectorEvent
import com.example.catchai.viewmodel.MediaDetectorViewModel
import java.io.InputStream

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AiMediaDetectorScreen(
    mediaDetectorViewModel: MediaDetectorViewModel = viewModel(),
) {
    val uiState by mediaDetectorViewModel.uiState.collectAsState()
    val context = LocalContext.current
    var imageBitmap by remember { mutableStateOf<android.graphics.Bitmap?>(null) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let {
            val inputStream: InputStream? = context.contentResolver.openInputStream(it)
            inputStream?.let {
                imageBitmap = BitmapFactory.decodeStream(it)
            }
        }
    }

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
                .verticalScroll(rememberScrollState()) // Make content scrollable
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Search, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "AI Media Detector", style = MaterialTheme.typography.headlineSmall)
            }
            Spacer(modifier = Modifier.height(16.dp))

            // Section 1: Preview of the file
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        if (imageBitmap != null) {
                            Image(
                                bitmap = imageBitmap!!.asImageBitmap(),
                                contentDescription = "Selected image",
                                modifier = Modifier.fillMaxSize()
                            )
                        } else {
                            Button(onClick = { launcher.launch("image/*") }) {
                                Text("Select Image")
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))

                    uiState.detectionResult?.let { result ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(top = 8.dp)
                        ) {
                            Icon(
                                imageVector = if (result.isAiGenerated) Icons.Default.Warning else Icons.Default.CheckCircle,
                                contentDescription = "Detection Status",
                                tint = if (result.isAiGenerated) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = if (result.isAiGenerated) "AI-Generated" else "Not AI-Generated",
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        Button(
                            onClick = {
                                imageBitmap?.let {
                                    mediaDetectorViewModel.onEvent(MediaDetectorEvent.OnAnalyzeClick(it))
                                }
                            },
                            enabled = imageBitmap != null && !uiState.isLoading
                        ) {
                            Text("Analyze Media")
                        }
                    }
                }
            }

            if (uiState.isLoading) {
                Spacer(modifier = Modifier.height(16.dp))
                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
            }

            // Section 2: Detection Results
            uiState.detectionResult?.let { result ->
                Spacer(modifier = Modifier.height(16.dp))
                Text("Detection Results", style = MaterialTheme.typography.headlineSmall)
                Spacer(modifier = Modifier.height(8.dp))
                DetectionResultContent(result = result)
            }

            uiState.errorMessage?.let { error ->
                 Spacer(modifier = Modifier.height(16.dp))
                 Text(text = error, color = MaterialTheme.colorScheme.error)
            }
        }
    }
}

@Composable
private fun DetectionResultContent(result: DetectionResult) {
    val uriHandler = LocalUriHandler.current

    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        // Verification Status
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Verification Status: ", style = MaterialTheme.typography.bodyLarge)
            Text(
                if (result.isAiGenerated) "AI-Generated" else "Not AI-Generated",
                color = if (result.isAiGenerated) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.bodyLarge
            )
        }

        // Confidence Score
        Column {
            Text("Confidence Score: ${result.confidencePercentage}%", style = MaterialTheme.typography.bodyLarge)
            Spacer(modifier = Modifier.height(4.dp))
            LinearProgressIndicator(
                progress = result.confidencePercentage / 100f,
                modifier = Modifier.fillMaxWidth()
            )
        }

        // Brief Description
        Column {
            Text("Description:", style = MaterialTheme.typography.bodyLarge)
            Text(result.briefDescription, style = MaterialTheme.typography.bodyMedium)
        }

        // Original Source
        result.originalSource?.let { sourceUrl ->
            Column {
                Text("Original Source:", style = MaterialTheme.typography.bodyLarge)
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
                    )
                )
            }
        }
    }
}