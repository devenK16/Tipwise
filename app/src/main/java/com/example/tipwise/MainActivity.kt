package com.example.tipwise

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.tipwise.ui.SettingsScreen
import com.example.tipwise.ui.theme.TipwiseTheme
import com.example.tipwise.ui.user.LoginScreen
import com.example.tipwise.ui.user.SignupScreen
import com.example.tipwise.ui.worker.AddWorkerScreen
import com.example.tipwise.ui.worker.HomeScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TipwiseTheme {
                // A surface container using the 'background' color from the theme
                val navController = rememberNavController()

                NavHost(
                    navController = navController,
                    startDestination = "signup"
                ) {
                    composable("login") {
                        LoginScreen(navController = navController)
                    }
                    composable("signup") {
                        SignupScreen(navController = navController)
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
                }
            }
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