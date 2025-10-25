
package com.example.catchai.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerState
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.catchai.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun NavDrawer(
    navController: NavController,
    drawerState: DrawerState,
    scope: CoroutineScope
) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp

    Column(
        modifier = Modifier
            .fillMaxHeight()
            .width(screenWidth * 2 / 3)
            .background(Color(0xFF121212))
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 24.dp),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "CatchAI Logo",
                modifier = Modifier.size(50.dp)
            )
        }

        Divider(modifier = Modifier.padding(bottom = 8.dp), color = Color.Gray.copy(alpha = 0.5f))

        Text(
            text = "Verification Tools",
            color = Color.White,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )

        NavigationDrawerItem(
            icon = R.drawable.dashboard,
            text = "Dashboard",
            onClick = {
                navController.navigate("home")
                scope.launch { drawerState.close() }
            }
        )

        NavigationDrawerItem(
            icon = R.drawable.image_icon,
            text = "AI Media Detector",
            onClick = {
                navController.navigate("media_detector")
                scope.launch { drawerState.close() }
            }
        )
        NavigationDrawerItem(
            icon = R.drawable.fact_icon,
            text = "AI Chatbot Fact-Checker",
            onClick = {
                navController.navigate("fact_checker")
                scope.launch { drawerState.close() }
            }
        )
        NavigationDrawerItem(
            icon = R.drawable.flag_icon,
            text = "Report Content",
            onClick = {
                navController.navigate("report_content")
                scope.launch { drawerState.close() }
            }
        )
    }
}

@Composable
private fun NavigationDrawerItem(
    icon: Int,
    text: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(id = icon),
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = text,
            color = Color.White,
            fontSize = 14.sp
        )
    }
}
