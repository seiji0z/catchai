package com.example.catchai

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.catchai.screens.*
import com.example.catchai.ui.theme.CATCHAITheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CATCHAITheme(darkTheme = true) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    CatchAiApp()
                }
            }
        }
    }
}

@Composable
fun CatchAiApp() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "login") {
        composable("login") {
            LoginScreen(onLoginClick = { navController.navigate("home") })
        }
        composable("home") {
            HomeScreen(
                onMediaDetectorClick = { navController.navigate("media_detector") },
                onFactCheckerClick = { navController.navigate("fact_checker") },
                onReportContentClick = { navController.navigate("report_content") }
            )
        }
        composable("media_detector") {
            AiMediaDetectorScreen(onAnalyzeClick = { navController.navigate("detection_results") })
        }
        composable("detection_results") {
            DetectionResultsScreen()
        }
        composable("fact_checker") {
            AiChatbotFactCheckerScreen()
        }
        composable("report_content") {
            ReportContentScreen(onSubmitReportClick = { navController.navigate("report_confirmation") })
        }
        composable("report_confirmation") {
            ReportConfirmationScreen(
                onNewReportClick = { navController.navigate("report_content") },
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}