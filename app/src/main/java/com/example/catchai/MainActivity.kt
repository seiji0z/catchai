package com.example.catchai

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.catchai.screens.*
import com.example.catchai.ui.theme.CATCHAITheme
import kotlinx.coroutines.delay

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
    var isSplashShown by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        delay(2000) // Splash screen delay of 2 seconds
        isSplashShown = false
        navController.navigate("login") {
            popUpTo("splash") { inclusive = true }
        }
    }

    NavHost(navController = navController, startDestination = "splash") {
        composable("splash") {
            SplashScreen()
        }
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

@Composable
fun SplashScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.catchai_logo),
            contentDescription = "Catch AI Logo",
            modifier = Modifier.fillMaxSize(0.5f),
            contentScale = ContentScale.Fit
        )
    }
}