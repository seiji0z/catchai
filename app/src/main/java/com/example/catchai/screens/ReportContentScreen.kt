
package com.example.catchai.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportContentScreen(onSubmitReportClick: () -> Unit, onMenuClick: () -> Unit) {
    var reportType by remember { mutableStateOf("") }
    var contentUrl by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var additionalInfo by remember { mutableStateOf("") }
    var selectedFileName by remember { mutableStateOf<String?>(null) }

    val blueAccent = Color(0xFF4A90E2)
    val darkBg = Color.Black
    val inputBg = Color(0xFF0D0D0D)
    val borderGray = Color(0xFF444444)

    val filePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri: Uri? ->
        uri?.let {
            val name = uri.lastPathSegment?.substringAfterLast('/') ?: "Selected File"
            selectedFileName = name
        }
    }

    Scaffold(
        containerColor = darkBg,
        topBar = {
            TopAppBar(
                title = { },
                navigationIcon = {
                    IconButton(onClick = onMenuClick) {
                        Icon(Icons.Default.Menu, contentDescription = "Menu", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = darkBg)
            )
        }
    ) { inner ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(inner)
                .padding(horizontal = 24.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(8.dp))

            // Title with red warning icon
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    "Report Content",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Help keep the internet safe by reporting deepfakes,\nAI scams, and malicious content",
                color = Color.Gray,
                fontSize = 13.sp,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(28.dp))

            // Section Header
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Security,
                    contentDescription = null,
                    tint = blueAccent
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    "Content Report Details",
                    color = blueAccent,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Report Type
            FieldLabel("Report Type")
            OutlinedTextField(
                value = reportType,
                onValueChange = { reportType = it },
                placeholder = { Text("Select the type of content you're reporting", color = Color.Gray) },
                trailingIcon = {
                    Icon(Icons.Default.ArrowDropDown, contentDescription = "Dropdown", tint = Color.Gray)
                },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = inputBg,
                    unfocusedContainerColor = inputBg,
                    focusedBorderColor = Color.White,
                    unfocusedBorderColor = borderGray,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    cursorColor = blueAccent
                ),
                shape = RoundedCornerShape(8.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            FieldLabel("Content Url (Optional)")
            OutlinedTextField(
                value = contentUrl,
                onValueChange = { contentUrl = it },
                placeholder = { Text("https://example.com/malicious-content", color = Color.Gray) },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = inputBg,
                    unfocusedContainerColor = inputBg,
                    focusedBorderColor = Color.White,
                    unfocusedBorderColor = borderGray,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    cursorColor = blueAccent
                ),
                shape = RoundedCornerShape(8.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            FieldLabel("Content Description")
            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                placeholder = { Text("Describe the malicious content in detail...", color = Color.Gray) },
                modifier = Modifier.fillMaxWidth(),
                maxLines = 5,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = inputBg,
                    unfocusedContainerColor = inputBg,
                    focusedBorderColor = Color.White,
                    unfocusedBorderColor = borderGray,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    cursorColor = blueAccent
                ),
                shape = RoundedCornerShape(8.dp)
            )

            Spacer(modifier = Modifier.height(20.dp))

            FieldLabel("Evidence (Optional)")

            // Dotted Upload Box
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(130.dp)
                    .border(
                        BorderStroke(1.dp, Color.Gray),
                        shape = RoundedCornerShape(12.dp)
                    )
                    .clickable {
                        filePickerLauncher.launch(arrayOf("image/*", "application/pdf"))
                    },
                contentAlignment = Alignment.Center
            ) {
                Canvas(modifier = Modifier.matchParentSize()) {
                    drawRoundRect(
                        color = Color.Gray,
                        style = Stroke(width = 1.dp.toPx(), pathEffect = PathEffect.dashPathEffect(floatArrayOf(12f, 8f))),
                        cornerRadius = androidx.compose.ui.geometry.CornerRadius(12f)
                    )
                }

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        Icons.Default.CloudUpload,
                        contentDescription = "Upload",
                        tint = blueAccent,
                        modifier = Modifier.size(28.dp)
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Upload Screenshots or Evidence\nPNG, JPG, or PDF files",
                        color = Color.Gray,
                        textAlign = TextAlign.Center,
                        fontSize = 12.sp
                    )
                }
            }

            if (selectedFileName != null) {
                Spacer(modifier = Modifier.height(8.dp))
                Text("Selected: $selectedFileName", color = Color.LightGray, fontSize = 13.sp)
            }

            Spacer(modifier = Modifier.height(20.dp))

            FieldLabel("Additional Information")
            OutlinedTextField(
                value = additionalInfo,
                onValueChange = { additionalInfo = it },
                placeholder = { Text("Any additional context that might help...", color = Color.Gray) },
                modifier = Modifier.fillMaxWidth(),
                maxLines = 3,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = inputBg,
                    unfocusedContainerColor = inputBg,
                    focusedBorderColor = Color.White,
                    unfocusedBorderColor = borderGray,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    cursorColor = blueAccent
                ),
                shape = RoundedCornerShape(8.dp)
            )

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = onSubmitReportClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = blueAccent)
            ) {
                Icon(Icons.Default.Send, contentDescription = null, tint = Color.White)
                Spacer(modifier = Modifier.width(6.dp))
                Text("Submit Report", color = Color.White, fontWeight = FontWeight.SemiBold)
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun FieldLabel(text: String) {
    Text(
        text = text,
        color = Color.White,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 4.dp)
    )
}
