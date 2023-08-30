package com.example.priorityclick.sign_in

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Text
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.priorityclick.R

@Composable
fun SignInScreen(
    state: SignInState,
    onSignInClick: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.primary

    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "Profile",
                fontSize = 30.sp,
                modifier = Modifier.padding(top = 30.dp, bottom = 20.dp),
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.tertiary
            )
            Spacer(modifier = Modifier.fillMaxHeight(0.2f))
           Text(
                text = "You are not authorized \uD83D\uDE22",
                fontSize = 25.sp,
                modifier = Modifier
                    .padding(top = 30.dp, bottom = 20.dp)
                    .width(250.dp),
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.tertiary,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(15.dp))
            Card(
                modifier = Modifier
                    .height(50.dp)
                    .width(150.dp)
                    .clickable { onSignInClick() },
                shape = MaterialTheme.shapes.extraLarge
            ) {
                Row(
                    modifier = Modifier.fillMaxSize(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Sign in",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(start = 30.dp)
                    )
                    Icon(
                        painter = painterResource(id = R.drawable.login),
                        contentDescription = "Login",
                        modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp)
                    )
                }
            }
        }
    }
}