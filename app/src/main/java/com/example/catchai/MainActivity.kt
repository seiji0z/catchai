
package com.example.catchai

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Surface
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.catchai.screens.*
import com.example.catchai.ui.theme.CATCHAITheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

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
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            NavDrawer(
                navController = navController,
                drawerState = drawerState,
                scope = scope
            )
        }
    ) {
        NavHost(navController = navController, startDestination = "splash") {

            // SPLASH SCREEN
            composable("splash") {
                SplashScreen(
                    onTimeout = {
                        navController.navigate("login") {
                            popUpTo("splash") { inclusive = true }
                        }
                    }
                )
            }

            // LOGIN SCREEN
            composable("login") {
                LoginScreen(onLoginClick = { navController.navigate("home") })
            }

            // HOME
            composable("home") {
                HomeScreen(
                    onMediaDetectorClick = { navController.navigate("media_detector") },
                    onFactCheckerClick = { navController.navigate("fact_checker") },
                    onReportContentClick = { navController.navigate("report_content") },
                    onMenuClick = { scope.launch { drawerState.open() } }
                )
            }

            composable("media_detector") {
                AiMediaDetectorScreen(
                    onMenuClick = { scope.launch { drawerState.open() } },
                    onBackToHome = { navController.navigate("home") }
                )
            }
            composable("fact_checker") {
                AiChatbotFactCheckerScreen(
                    onMenuClick = { scope.launch { drawerState.open() } },
                    onBackToHome = { navController.navigate("home") }
                )
            }
            composable("report_content") {
                ReportContentScreen(
                    onSubmitReportClick = { navController.navigate("report_confirmation") },
                    onMenuClick = { scope.launch { drawerState.open() } }
                )
            }
            composable("report_confirmation") {
                ReportConfirmationScreen(
                    onNewReportClick = { navController.navigate("report_content") },
                    onBackClick = { navController.navigate("home") }
                )
            }
        }
    }
}

@Composable
fun SplashScreen(onTimeout: () -> Unit) {
    // Automatically go to login after 3 seconds
    LaunchedEffect(Unit) {
        delay(3000)
        onTimeout()
    }

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
