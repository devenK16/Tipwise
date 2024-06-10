package com.example.tipwise.ui

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.startActivity
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.tipwise.R
import com.example.tipwise.ui.theme.PacificBridge
import com.example.tipwise.ui.theme.TipzonnBlack
import com.example.tipwise.ui.theme.TipzonnLight
import com.example.tipwise.ui.user.AuthViewModel
import com.example.tipwise.utils.ProfileSetupManager
import com.example.tipwise.utils.TokenManager

@Composable
fun SettingsScreen(
    navController: NavHostController
) {
    Column {
        ProfileCardUI(navController)
        GeneralUI(navController)
        SupportOptionsUI()
    }

}

@Composable
fun ProfileCardUI(navController: NavHostController) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp)
            .padding(10.dp),
        colors = CardDefaults.cardColors(Color.White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(20.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                Text(
                    text = "Check Your Profile",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = TipzonnBlack
                )

//                Text(
//                    text = "UI.Stack.YT@gmail.com",
//                    color = Color.Gray,
//                    fontSize = 10.sp,
//                    fontWeight = FontWeight.SemiBold,
//                )

                Button(
                    modifier = Modifier.padding(top = 10.dp),
                    onClick = {
                        navController.popBackStack()
                        navController.navigate("profile")
                    },
                    contentPadding = PaddingValues(horizontal = 30.dp),
                    elevation = ButtonDefaults.buttonElevation(
                        defaultElevation = 0.dp,
                        pressedElevation = 2.dp
                    ),
                    colors = ButtonDefaults.buttonColors(containerColor = TipzonnLight)
                ) {
                    Text(
                        text = "Update",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }

            Image(
                painter = painterResource(id = R.drawable.ic_profile_card_image),
                contentDescription = "",
                modifier = Modifier.height(120.dp)
            )
        }
    }
}

@Composable
fun GeneralUI(navController: NavHostController) {

    val context = LocalContext.current

    Column(
        modifier = Modifier
            .padding(horizontal = 14.dp)
            .padding(top = 10.dp)
    ) {
        Text(
            text = "General",
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .padding(vertical = 8.dp),
            color = TipzonnBlack
        )

        SupportItem(
            icon = R.drawable.ic_logout,
            mainText = "Log Out",
            onClick = { logOut( context , navController) }
        )
    }
}

@Composable
fun SupportOptionsUI() {

    val context = LocalContext.current

    Column(
        modifier = Modifier
            .padding(horizontal = 14.dp)
            .padding(top = 10.dp)
    ) {
        Text(
            text = "Support",
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .padding(vertical = 8.dp),
            color = TipzonnBlack
        )
        SupportItem(
            icon = R.drawable.ic_whatsapp,
            mainText = "Contact",
            onClick = { openWhatsApp(context, "9322136520") }
        )
        SupportItem(
            icon = R.drawable.ic_mail,
            mainText = "Feedback",
            onClick = { openEmail(context, "contact@tipzonn.com") }
        )
        SupportItem(
            icon = R.drawable.ic_authenticaiton,
            mainText = "Privacy Policy",
            onClick = { openWebsite(context , "https://www.tipzonn.com/privacy")  }
        )
        SupportItem(
            icon = R.drawable.ic_info,
            mainText = "About Us",
            onClick = { openWebsite(context , "https://www.tipzonn.com") }
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SupportItem(icon: Int, mainText: String, onClick: () -> Unit) {
    Card(
        onClick = { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        modifier = Modifier
            .padding(bottom = 8.dp)
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(2.dp),
    ) {
        Row(
            modifier = Modifier
                .padding(vertical = 10.dp, horizontal = 14.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(34.dp)
                        .background(Color.White)
                ) {
                    Icon(
                        painter = painterResource(id = icon),
                        contentDescription = "",
                        tint = Color.Unspecified,
                        modifier = Modifier.padding(8.dp)
                    )
                }

                Spacer(modifier = Modifier.width(14.dp))

                Text(
                    text = mainText,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = TipzonnBlack
                )
            }
            Icon(
                painter = painterResource(id = R.drawable.ic_right_arrow),
                contentDescription = "",
                modifier = Modifier.size(16.dp)
            )
        }
    }
}

fun openWhatsApp(context: Context, phoneNumber: String) {
    val uri = Uri.parse("https://wa.me/$phoneNumber")
    val intent = Intent(Intent.ACTION_VIEW, uri)
    intent.setPackage("com.whatsapp")
    startActivity(context, intent, null)
}

fun openEmail(context: Context, email: String) {
    val intent = Intent(Intent.ACTION_SENDTO).apply {
        data = Uri.parse("mailto:")
        putExtra(Intent.EXTRA_EMAIL, arrayOf(email))
    }
    startActivity(context, intent, null)
}

fun openWebsite(context: Context , websiteUrl : String) {
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(websiteUrl))
    startActivity(context, intent, null)
}

fun logOut(context: Context  , navController: NavHostController){
    // Clear the user token
    val tokenManager = TokenManager(context)
    tokenManager.saveToken(null)
    val profileManager = ProfileSetupManager(context)
    profileManager.saveUserId(null)
    // Navigate to the login screen
    navController.navigate("login") {
        popUpTo(navController.graph.startDestinationId) {
            inclusive = true
        }
    }
}