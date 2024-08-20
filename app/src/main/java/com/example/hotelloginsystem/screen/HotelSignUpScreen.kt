package com.example.hotelloginsystem.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.hotelloginsystem.R
import com.example.hotelloginsystem.nav.Screens
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

lateinit var auth: FirebaseAuth

@Composable
fun HotelLoginScreen(navController: NavController) {
    // Initialize Firebase Auth
    auth = FirebaseAuth.getInstance()

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var showPasswordToggle by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) } //checkbox state
    var successMessage by remember { mutableStateOf<String?>(null) }

    val visualTransformation = if (passwordVisible) {
        VisualTransformation.None
    } else {
        PasswordVisualTransformation()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(8.dp)
        ) {
            Row(
                modifier = Modifier
                    .padding(start = 15.dp, top = 15.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Start
            ) {
                IconButton(
                    onClick = { navController.popBackStack() }
                ) {
                    Icon(imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "back_button",
                        tint = Color.Black
                    )
                }
            }
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Create an Account",
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.headlineMedium,
                    fontSize = 24.sp
                )

                Spacer(modifier = Modifier.height(16.dp))

                Image(
                    modifier = Modifier
                        .size(100.dp)
                        .clip(RoundedCornerShape(56.dp)),
                    painter = painterResource(R.drawable.attack_6806140_1280),
                    contentDescription = "Login"
                )

                Spacer(modifier = Modifier.height(16.dp))

                TextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(16.dp))

                TextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Password") },
                    visualTransformation = visualTransformation,
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Checkbox for toggling password visibility
                Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked = showPasswordToggle,
                        onCheckedChange = { isChecked ->
                            showPasswordToggle = isChecked
                            passwordVisible = isChecked
                        }
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "Show Password",color = Color.Black)
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Forgot Password?",
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        // Validate email and password
                        if (email.isNotEmpty() && password.isNotEmpty()) {
                            signUp(
                                email,
                                password,
                                { user ->
                                    successMessage = "Verification email sent to ${user?.email}"
                                },
                                { error ->
                                    errorMessage = error
                                }
                            )
                        } else {
                            errorMessage = "Please enter both email and password"
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Register")
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Display error or success message
                errorMessage?.let {
                    Text(
                        text = it,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                successMessage?.let {
                    Text(
                        text = it,
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}

private fun signUp(
    email: String,
    password: String,
    onSuccess: (FirebaseUser?) -> Unit,
    onFailure: (String) -> Unit
) {
    auth.createUserWithEmailAndPassword(email, password)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val user = auth.currentUser
                user?.sendEmailVerification()
                    ?.addOnCompleteListener { verificationTask ->
                        if (verificationTask.isSuccessful) {
                            onSuccess(user)
                        } else {
                            onFailure(verificationTask.exception?.message ?: "Failed to send verification email")
                        }
                    }
            } else {
                onFailure(task.exception?.message ?: "Sign up failed")
            }
        }
}
