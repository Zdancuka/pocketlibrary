package com.example.pocketlibrary.ui.screen

import android.util.Patterns
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import com.example.pocketlibrary.R
import com.example.pocketlibrary.ui.screen.element.LabeledField
import com.example.pocketlibrary.ui.theme.Dimens
import com.example.pocketlibrary.ui.viewmodel.AuthViewModel

private const val MIN_PASSWORD_LENGTH = 6

@Composable
fun AuthScreen(
    authViewModel : AuthViewModel,
    onAuthSuccess : () -> Unit
) {
    var isSignUpMode by remember { mutableStateOf(false) }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    //val authState by authViewModel.authState.collectAsState()

    var emailTouched by remember { mutableStateOf(false) }
    var passwordTouched by remember { mutableStateOf(false) }

    val isEmailValid = Patterns.EMAIL_ADDRESS.matcher(email).matches()
    val isPasswordValid = password.length >= MIN_PASSWORD_LENGTH

    val emailError = when {
        !emailTouched || email.isEmpty() -> null
        !isEmailValid -> stringResource(R.string.auth_error_invalid_email)
        else -> null
    }

    val passwordError = when {
        !passwordTouched || password.isEmpty() -> null
        isSignUpMode && !isPasswordValid -> stringResource(R.string.auth_error_weak_password)
        else -> null
    }

    val canSubmit = email.isNotBlank() &&
            password.isNotBlank() &&
            isEmailValid &&
            (!isSignUpMode || isPasswordValid) &&
            !authViewModel.isLoading

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(Dimens.SpaceLarge),
        verticalArrangement = Arrangement.Center
    ) {

        Text(
            text = stringResource(
                if (isSignUpMode) {
                    R.string.auth_create_account
                } else {
                    R.string.auth_welcome_back
                }
            ),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(Dimens.SpaceXLarge))

        LabeledField(
            label = stringResource(R.string.auth_email),
            value = email,
            onValueChange = { email = it },
            placeholder = stringResource(R.string.auth_email),
            keyboardType = KeyboardType.Email
        )

        Spacer(modifier = Modifier.height(Dimens.SpaceMedium))

        LabeledField(
            label = stringResource(R.string.auth_password),
            value = password,
            onValueChange = { password = it },
            placeholder = stringResource(R.string.auth_password),
            keyboardType = KeyboardType.Password
        )


        if (authViewModel.errorMessage != null) {
            Spacer(modifier = Modifier.height(Dimens.SpaceSmall))

            Text(
                text = authViewModel.errorMessage.orEmpty(),
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall
            )
        }

        Spacer(modifier = Modifier.height(Dimens.SpaceXLarge))

        Button(
            onClick = {
                if (isSignUpMode) {
                    authViewModel.signUp(
                        email = email,
                        password = password,
                        onSuccess = onAuthSuccess
                    )
                } else {
                    authViewModel.signIn(
                        email = email,
                        password = password,
                        onSuccess = onAuthSuccess
                    )
                }
            },
            enabled = canSubmit,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ),
            shape = RoundedCornerShape(Dimens.CornerXSmall)
        ) {
            Text(
                text = when {
                    authViewModel.isLoading ->
                        stringResource(R.string.auth_loading)

                    isSignUpMode ->
                        stringResource(R.string.auth_sign_up)

                    else ->
                        stringResource(R.string.auth_sign_in)
                },
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(Dimens.SpaceMedium))

        TextButton(
            onClick = {
                isSignUpMode = ! isSignUpMode
                emailTouched = false
                passwordTouched = false
                authViewModel.clearError()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = stringResource(
                    if (isSignUpMode) {
                        R.string.auth_already_have_account
                    } else {
                        R.string.auth_no_account
                    }
                ),
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}