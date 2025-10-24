package com.example.catchai.screens

import android.graphics.BitmapFactory
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
            inputStream?.let { stream ->
                imageBitmap = BitmapFactory.decodeStream(stream)
            }
        }
    }

    Scaffold() { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color.Black)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center // ✅ Center content vertically
            ) {
                Icon(
                    Icons.Default.Image,
                    contentDescription = null,
                    tint = Color(0xFF3B82F6),
                    modifier = Modifier.size(48.dp)
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    "AI Media Detector",
                    color = Color.White,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    "Analyze images and videos to detect AI-generated content with advanced algorithms.",
                    color = Color.Gray,
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp)
                )

                Spacer(modifier = Modifier.height(28.dp))

                Text(
                    "Upload Media",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    modifier = Modifier.align(Alignment.Start)
                )

                Spacer(modifier = Modifier.height(12.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .border(1.dp, Color.DarkGray, RoundedCornerShape(12.dp))
                        .background(Color(0xFF1A1A1A), RoundedCornerShape(12.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    if (imageBitmap != null) {
                        Image(
                            bitmap = imageBitmap!!.asImageBitmap(),
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize()
                        )
                    } else {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                Icons.Default.CloudUpload,
                                contentDescription = null,
                                tint = Color.Gray,
                                modifier = Modifier.size(48.dp)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                "Upload Media for Analysis",
                                color = Color.White,
                                fontWeight = FontWeight.Medium
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Button(
                                onClick = { launcher.launch("image/*") },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFF3B82F6)
                                )
                            ) {
                                Text("Browse Files")
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        imageBitmap?.let {
                            mediaDetectorViewModel.onEvent(MediaDetectorEvent.OnAnalyzeClick(it))
                        }
                    },
                    enabled = imageBitmap != null && !uiState.isLoading,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF3B82F6),
                        disabledContainerColor = Color.Gray
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text("Analyze Media", color = Color.White)
                }

                if (uiState.isLoading) {
                    Spacer(modifier = Modifier.height(16.dp))
                    CircularProgressIndicator(color = Color(0xFF3B82F6))
                }

                uiState.detectionResult?.let { result ->
                    Spacer(modifier = Modifier.height(24.dp))
                    DetectionResultContent(result = result)
                }

                uiState.errorMessage?.let { error ->
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(text = error, color = Color.Red)
                }

                Spacer(modifier = Modifier.height(48.dp))
            }
        }
    }
}

@Composable
private fun DetectionResultContent(result: DetectionResult) {
    val uriHandler = LocalUriHandler.current

    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Verification Status: ", color = Color.White)
            Text(
                if (result.isAiGenerated) "AI-Generated" else "Not AI-Generated",
                color = if (result.isAiGenerated) Color.Red else Color(0xFF3B82F6)
            )
        }

        Column {
            Text("Confidence Score: ${result.confidencePercentage}%", color = Color.White)
            Spacer(modifier = Modifier.height(4.dp))
            LinearProgressIndicator(
                progress = result.confidencePercentage / 100f,
                color = Color(0xFF3B82F6),
                trackColor = Color.DarkGray,
                modifier = Modifier.fillMaxWidth()
            )
        }

        Column {
            Text("Description:", color = Color.White)
            Text(result.briefDescription, color = Color.Gray)
        }

        result.originalSource?.let { source ->
            Column {
                Text("Original Source:", color = Color.White)
                ClickableText(
                    text = AnnotatedString(source),
                    onClick = { uriHandler.openUri(source) },
                    style = TextStyle(
                        color = Color(0xFF3B82F6),
                        textDecoration = TextDecoration.Underline
                    )
                )
            }
        }
    }
}
