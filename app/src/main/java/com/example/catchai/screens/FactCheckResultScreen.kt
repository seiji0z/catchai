package com.example.catchai.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun FactCheckResultScreen() {
    Column(modifier = Modifier.padding(16.dp)) {
        Text("Your Query:", style = MaterialTheme.typography.titleMedium)
        Text("Is this headline real? 'New Study Finds Chocolate Cures All Diseases'", style = MaterialTheme.typography.bodyLarge)
        Spacer(modifier = Modifier.height(24.dp))

        Text("Fact Check Results", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(16.dp))

        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                ResultRow("Verification Status:", "False")
                ResultRow("Confidence Score:", "99%")
                Text("Detailed Analysis:", style = MaterialTheme.typography.titleMedium)
                Text(
                    "This headline is false. There is no scientific evidence to support the claim that chocolate cures all diseases. While some studies have shown that dark chocolate in moderation may have certain health benefits, it is not a cure-all. Be wary of sensationalist headlines and always consult with a healthcare professional for medical advice.",
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text("Trusted Sources:", style = MaterialTheme.typography.titleMedium)
                Text("• World Health Organization", style = MaterialTheme.typography.bodyMedium)
                Text("• Mayo Clinic", style = MaterialTheme.typography.bodyMedium)
                Text("• National Institutes of Health", style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}
