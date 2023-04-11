package com.daniel.ping.ui.components

import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.ZeroCornerSize
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.sp
import com.daniel.ping.ui.theme.RangoonGreen
import com.daniel.ping.ui.theme.SilverFoil
import com.daniel.ping.ui.theme.UltramarineBlue
import com.daniel.ping.ui.theme.White

@Composable
fun InputComponent(
    hilt: String,
    modifier: Modifier,
    keyboardOptions: KeyboardOptions,
    visualTransformation: VisualTransformation = VisualTransformation.None
) {

    TextField(
        value = "",
        onValueChange = { },
        textStyle = TextStyle(color = White, fontSize = 12.sp),
        modifier = modifier,
        placeholder = { Text(
            text = hilt,
            color = SilverFoil,
            fontSize = 12.sp
        ) },
        keyboardOptions = keyboardOptions,
        visualTransformation = visualTransformation,
        shape = CutCornerShape(ZeroCornerSize),
        colors = TextFieldDefaults.textFieldColors(
            backgroundColor = RangoonGreen,
            cursorColor = UltramarineBlue,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        )
    )

}