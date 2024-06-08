package com.example.tipwise.ui

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.tipwise.ui.theme.PacificBridge
import com.example.tipwise.ui.theme.TipzonnBlack
import com.example.tipwise.ui.theme.TipzonnLight
import com.example.tipwise.ui.user.AuthViewModel
import com.example.tipwise.ui.user.ProfileScreen
import com.example.tipwise.ui.worker.FeedScreen
import com.example.tipwise.utils.ProfileSetupManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavHostController
) {

    val bottomNavController = rememberNavController()


    Scaffold(
        bottomBar = {
            BottomNavigationBar(
                bottomNavController = bottomNavController
            )
        },
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Tipzonn",
                        color = TipzonnBlack,
                        fontSize = 24.sp
                    )
                },
                modifier = Modifier.shadow(2.dp),
                colors = TopAppBarDefaults.topAppBarColors(
                    Color.White
                )
            )
        },
        containerColor = Color.White
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(it)
                .background(color = Color.White)
        ) {
            NavHost(
                navController = bottomNavController,
                startDestination = "feed"
            ) {
                composable("feed"){
                    FeedScreen(
                        navController = navController
                    )
                }
                composable("settings"){
                    SettingsScreen(
                        navController = navController
                    )
                }
                composable("profile"){
                    ProfileScreen(
                        navController = navController
                    )
                }
            }
        }
    }
}

@Composable
fun BottomNavigationBar(
    bottomNavController: NavHostController
) {
    val items = listOf(
        BottomItem(
            title = "home",
            icon = Icons.Rounded.Home
        ),
        BottomItem(
            title = "settings",
            icon = Icons.Rounded.Settings
        )
    )

    val selected = rememberSaveable {
        mutableIntStateOf(0)
    }

    NavigationBar(
        containerColor = Color.White,
        contentColor = Color.Black,
        modifier = Modifier.shadow(10.dp),
    ){
        Row {
            items.forEachIndexed { index, bottomItem ->
                NavigationBarItem(
                    selected = selected.intValue == index,
                    onClick = {
                        selected.intValue = index
                        when (selected.intValue) {
                            0 -> {
                                bottomNavController.popBackStack()
                                bottomNavController.navigate("feed")
                            }

                            1 -> {
                                bottomNavController.popBackStack()
                                bottomNavController.navigate("settings")
                            }
                        }
                    },
                    icon = {
                        Icon(
                            imageVector = bottomItem.icon,
                            contentDescription = bottomItem.title,
                            tint = if (selected.value == index) Color(0xFF00BFFF) else TipzonnBlack
                        )
                    },
                    label = {
                        Text(
                            text = bottomItem.title,
                            color = if (selected.value == index) Color(0xFF00BFFF) else TipzonnBlack
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color(0xFF5CE4E4),
                        unselectedIconColor = TipzonnBlack,
                        selectedTextColor = Color(0xFF5CE4E4),
                        unselectedTextColor = TipzonnBlack,
                        indicatorColor = Color.White // Change this to your desired background color
                    )

                )
            }
        }
    }
}

data class BottomItem(
    val title: String,
    val icon: ImageVector
)