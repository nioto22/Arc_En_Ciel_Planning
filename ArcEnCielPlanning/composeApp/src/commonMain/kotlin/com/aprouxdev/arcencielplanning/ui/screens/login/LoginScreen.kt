package com.aprouxdev.arcencielplanning.ui.screens.login

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.aprouxdev.arcencielplanning.ui.screens.home.HomeScreen

/**
 * Login screen
 * Handles user authentication
 */
class LoginScreen : Screen {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow

        var username by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }
        var isLoading by remember { mutableStateOf(false) }
        var errorMessage by remember { mutableStateOf<String?>(null) }

        fun login() {
            if (username.isBlank() || password.isBlank()) {
                errorMessage = "Veuillez remplir tous les champs"
                return
            }

            isLoading = true
            errorMessage = null

            // TODO: Implement actual authentication with repository
            // For now, simulate login
            if (username == "admin" && password == "admin") {
                navigator.replace(HomeScreen())
            } else {
                errorMessage = "Nom d'utilisateur ou mot de passe incorrect"
                isLoading = false
            }
        }

        LoginScreenContent(
            username = username,
            onUsernameChange = { username = it },
            password = password,
            onPasswordChange = { password = it },
            isLoading = isLoading,
            errorMessage = errorMessage,
            onLoginClick = { login() }
        )
    }
}

@Composable
private fun LoginScreenContent(
    username: String,
    onUsernameChange: (String) -> Unit,
    password: String,
    onPasswordChange: (String) -> Unit,
    isLoading: Boolean,
    errorMessage: String?,
    onLoginClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.widthIn(max = 400.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Title
            Text(
                text = "Connexion",
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center
            )

            Text(
                text = "Arc En Ciel Planning",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Username field
            OutlinedTextField(
                value = username,
                onValueChange = onUsernameChange,
                label = { Text("Nom d'utilisateur") },
                singleLine = true,
                enabled = !isLoading,
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                )
            )

            // Password field
            OutlinedTextField(
                value = password,
                onValueChange = onPasswordChange,
                label = { Text("Mot de passe") },
                singleLine = true,
                enabled = !isLoading,
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = { onLoginClick() }
                )
            )

            // Error message
            if (errorMessage != null) {
                Text(
                    text = errorMessage,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.error,
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Login button
            Button(
                onClick = onLoginClick,
                enabled = !isLoading,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.onPrimary,
                        strokeWidth = 2.dp
                    )
                } else {
                    Text(
                        text = "Se connecter",
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }
        }
    }
}
