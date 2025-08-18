package dev.dr10.ping.ui.screens.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.dr10.ping.ui.theme.AppTheme

@Composable
fun TextFieldComponent(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    height: Dp = 55.dp,
    background: Color = AppTheme.colors.background,
    iconId: Int? = null,
    horizontalPadding: Dp = 20.dp,
    capitalization: Boolean = false,
    isEmail: Boolean = false,
    isPassword: Boolean = false,
    isNext: Boolean = false,
    isDone: Boolean = true,
    isSearch: Boolean = false
) {

    val keyboardType = when {
        isEmail -> KeyboardType.Email
        isPassword -> KeyboardType.Password
        else -> KeyboardType.Text
    }

    val visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None

    val imeAction = when {
        isNext -> ImeAction.Next
        isDone -> ImeAction.Done
        isSearch -> ImeAction.Search
        else -> ImeAction.Default
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(height)
            .padding(horizontal = horizontalPadding)
            .background(color = background, shape = RoundedCornerShape(10.dp)),
        verticalAlignment = Alignment.CenterVertically
    ) {

        if (iconId != null) {

            Spacer(Modifier.width(8.dp))

            Icon(
                painter = painterResource(iconId),
                contentDescription = null,
                tint = AppTheme.colors.textSecondary,
                modifier = Modifier.size(23.dp)
            )
        }

        BasicTextField(
            value = value,
            onValueChange = { onValueChange(it) },
            modifier = Modifier.fillMaxWidth().padding(horizontal = 10.dp),
            textStyle = TextStyle(
                color = AppTheme.colors.text,
                fontSize = 16.sp,
                fontFamily = AppTheme.robotoFont,
                fontWeight = FontWeight.Normal
            ),
            singleLine = true,
            cursorBrush = SolidColor(AppTheme.colors.complementary),
            keyboardOptions = KeyboardOptions(
                keyboardType = keyboardType,
                imeAction = imeAction,
                capitalization = if (capitalization) KeyboardCapitalization.Sentences else KeyboardCapitalization.Unspecified
            ),
            visualTransformation = visualTransformation,
            decorationBox = { innerTextField ->
                if (value.isEmpty()) {
                    Text(
                        text = placeholder,
                        color = AppTheme.colors.textSecondary,
                        fontSize = 16.sp,
                        fontFamily = AppTheme.robotoFont,
                        fontWeight = FontWeight.Normal
                    )
                }
                innerTextField()
            }
        )
    }
}