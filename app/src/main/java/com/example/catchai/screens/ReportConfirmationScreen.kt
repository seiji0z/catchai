package com.example.catchai.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ReportConfirmationScreen(onNewReportClick: () -> Unit, onBackClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Report Submitted", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(16.dp))
        Text("Your report has been submitted and will be reviewed by our team. Thank you for helping us keep the community safe.")
        Spacer(modifier = Modifier.height(32.dp))

        // Report Preview
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Report Summary", style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(16.dp))
                Text("Report Type: Fake News")
                Text("Content URL: http://example.com")
                Text("Description: This is a fake news article.")
            }
        }

        Spacer(modifier = Modifier.height(32.dp))
        Row {
            Button(onClick = onBackClick) {
                Text("Back")
            }
            Spacer(modifier = Modifier.width(16.dp))
            Button(onClick = onNewReportClick) {
                Text("Submit Another Report")
            }
        }
    }
}