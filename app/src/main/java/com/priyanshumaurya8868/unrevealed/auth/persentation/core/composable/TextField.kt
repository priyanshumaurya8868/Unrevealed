package com.priyanshumaurya8868.unrevealed.auth.persentation.core

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.outlined.AlternateEmail
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp

@Composable
fun BorderedTextField(
    modifier: Modifier = Modifier,
    value: String,
    hint: String,
    isError: Boolean = false,
    leadingIcon: ImageVector,
    trailingIcon: @Composable() (() -> Unit),
    onValueChange: (String) -> Unit,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None
) {
//    https://stackoverflow.com/questions/64542659/jetpack-compose-custom-textfield-design
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        colors = TextFieldDefaults.textFieldColors(
            backgroundColor = MaterialTheme.colors.onSecondary,
            focusedIndicatorColor = Color.Transparent, //hide the indicator
            unfocusedIndicatorColor = Color.Transparent,
            cursorColor = MaterialTheme.colors.onSecondary
        ),
        placeholder = { Text(text = hint) },
        trailingIcon = trailingIcon,
        leadingIcon = {
            Icon(
                imageVector = leadingIcon,
                contentDescription = null
            )
        },
        modifier = modifier
            .border(
                1.dp,
                color = MaterialTheme.colors.onSecondary,
                shape = RoundedCornerShape(5.dp)
            )
            .background(MaterialTheme.colors.onSurface.copy(alpha = 0.05f)),
        isError = isError,
        keyboardOptions = keyboardOptions,
        visualTransformation = visualTransformation
    )
}

@Composable
fun UsernameTextField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    isError: Boolean,
) {
    BorderedTextField(
        modifier = modifier,
        value = value,
        hint = "Username",
        leadingIcon = Icons.Outlined.AlternateEmail,
        trailingIcon = {
            Icon(
                imageVector = Icons.Default.Close,
                modifier = Modifier.clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }) {
                    //action
                },
                contentDescription = null
            )
        },
        onValueChange = onValueChange,
        isError = isError
    )
}

@Composable
fun PasswordTextField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    isError: Boolean, passwordVisible: Boolean,
    togglePassword: (Boolean) -> Unit,
    hint: String
) {
    BorderedTextField(
        modifier = modifier,
        value = value,
        hint = hint,
        leadingIcon = Icons.Outlined.Lock,
        isError = isError,
        onValueChange = onValueChange,
        visualTransformation = if (passwordVisible) VisualTransformation.None
        else PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        trailingIcon = {
            val image = if (passwordVisible)
                Icons.Filled.Visibility
            else Icons.Filled.VisibilityOff

            // Please provide localized description for accessibility services
            val description = if (passwordVisible) "Hide password" else "Show password"

            IconButton(onClick = { togglePassword(passwordVisible) }) {
                Icon(imageVector = image, description)
            }
        }
    )

}