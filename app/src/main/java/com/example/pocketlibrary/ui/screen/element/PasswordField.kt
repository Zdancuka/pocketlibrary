package com.example.pocketlibrary.ui.screen.element

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.input.TextObfuscationMode
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SecureTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import com.example.pocketlibrary.R
import com.example.pocketlibrary.ui.theme.Dimens

@Composable
fun PasswordField(
    label : String ,
    value : String ,
    onValueChange : (String) -> Unit ,
    placeholder : String ,
    modifier : Modifier = Modifier ,
    keyboardType : KeyboardType = KeyboardType.Password ,
    isError : Boolean = false ,
    errorText : String? = null ,
) {
    var passwordHidden by rememberSaveable { mutableStateOf(true) }
    val passwordState = rememberTextFieldState()

    LaunchedEffect(passwordState) {
        snapshotFlow { passwordState.text.toString() }
            .collect { text -> if (text != value) onValueChange(text) }
    }

    Column(modifier = modifier) {
        Text(
            text = label ,
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.height(Dimens.SpaceXSmall))

        SecureTextField(
            state = passwordState ,
            placeholder = {
                Text(
                    placeholder ,
                    color = MaterialTheme.colorScheme.onSurfaceVariant ,
                )
            } ,
            textObfuscationMode =
                if (passwordHidden) TextObfuscationMode.RevealLastTyped
                else TextObfuscationMode.Visible ,
            isError = isError ,
            trailingIcon = {
                val description =
                    stringResource(
                        id =
                            if (passwordHidden) R.string.show_password
                            else R.string.hide_password
                    )
                IconButton(onClick = { passwordHidden = ! passwordHidden }) {
                    Icon(
                        painter = painterResource(
                            id =
                                if (passwordHidden) R.drawable.ic_visibility
                                else R.drawable.ic_visibility_off
                        ) ,
                        contentDescription = description ,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } ,
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(Dimens.CornerXSmall)) ,
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = MaterialTheme.colorScheme.surface ,
                focusedContainerColor = MaterialTheme.colorScheme.surface ,
                unfocusedIndicatorColor = Color.Transparent ,
                focusedIndicatorColor = Color.Transparent ,
                errorIndicatorColor = MaterialTheme.colorScheme.error ,
                errorContainerColor = MaterialTheme.colorScheme.surface
            )
        )

        if (isError && errorText != null) {
            Spacer(modifier = Modifier.height(Dimens.SpaceSmall))
            Text(
                text = errorText ,
                color = MaterialTheme.colorScheme.error ,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}
