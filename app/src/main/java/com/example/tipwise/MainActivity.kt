package com.example.tipwise

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
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
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var tokenManager: TokenManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TipwiseTheme {
                setBarColor(Color.White)
                // A surface container using the 'background' color from the theme
                val navController = rememberNavController()

                val profileSetupManager = ProfileSetupManager(this)

                NavHost(
                    navController = navController,
                    startDestination = "signup"
                ) {
                    composable("login") {
                        LoginScreen(
                            navController = navController, tokenManager = tokenManager,

                            profileSetupManager = profileSetupManager
                        )
                    }
                    composable("signup") {
                        SignupScreen(
                            navController = navController, tokenManager = tokenManager,
                            profileSetupManager = profileSetupManager
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
                            navController = navController,
                            authToken = tokenManager.getToken(),
                            profileSetupManager = profileSetupManager
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

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    TipwiseTheme {
        Greeting("Android")
    }
}