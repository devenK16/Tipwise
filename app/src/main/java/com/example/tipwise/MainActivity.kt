package com.example.tipwise

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.tipwise.ui.HomeScreen
import com.example.tipwise.ui.SettingsScreen
import com.example.tipwise.ui.theme.TipwiseTheme
import com.example.tipwise.ui.user.LoginScreen
import com.example.tipwise.ui.user.ProfileScreen
import com.example.tipwise.ui.user.SignupScreen
import com.example.tipwise.ui.worker.AddWorkerScreen
import com.example.tipwise.ui.worker.FeedScreen
import com.example.tipwise.utils.ProfileSetupManager
import com.example.tipwise.utils.TokenManager
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var tokenManager: TokenManager

    @SuppressLint("CoroutineCreationDuringComposition")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TipwiseTheme {
                setBarColor(Color.White)
                // A surface container using the 'background' color from the theme
                val navController = rememberNavController()

                val token = tokenManager.getToken()
                if (token != null) {
                    Log.d("mainToken", token)
                }
                var start = "home"
                start = if (token != null) {
                    "home"
                } else {
                    "signup"
                }
                NavHost(
                    navController = navController,
                    startDestination = start
                ) {
                    composable("login") {
                        LoginScreen(
                            navController = navController
                        )
                    }
                    composable("signup") {
                        SignupScreen(
                            navController = navController
                        )
                    }
                    composable("feed") {
                        FeedScreen(navController = navController)
                    }
                    composable("home") {
                        HomeScreen(navController = navController)
                    }
                    composable("add_worker") {
                        AddWorkerScreen(navController = navController)
                    }
                    composable("add_worker/{workerId}") { backStackEntry ->
                        val workerId = backStackEntry.arguments?.getString("workerId")
                        AddWorkerScreen(navController = navController, workerId = workerId)
                    }
                    composable("settings") {
                        SettingsScreen(navController = navController)
                    }
                    composable("profile") {
                        ProfileScreen(
                            navController = navController
                        )
                    }
                }
            }
        }
    }

    @Composable
    private fun setBarColor(color: Color) {
        val systemUiController = rememberSystemUiController()
        LaunchedEffect(key1 = color) {
            systemUiController.setSystemBarsColor(color)
        }
    }
}

