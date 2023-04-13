package com.daniel.ping.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.shape.ZeroCornerSize
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.daniel.ping.R
import com.daniel.ping.ui.components.AppBarComponent
import com.daniel.ping.ui.components.AuthenticationAlternativeComponent
import com.daniel.ping.ui.components.DescriptionScreenComponent
import com.daniel.ping.ui.components.QuestionComponent
import com.daniel.ping.ui.theme.Onyx
import com.daniel.ping.ui.theme.UltramarineBlue
import com.daniel.ping.ui.theme.White


@Composable
fun SignInScreen(
    onRedirectClickListener: () -> Unit
) {
    Scaffold(
        topBar = { AppBarComponent() }
    ) { paddingValues ->
        BodySignInScreen(
            padding = paddingValues,
            onRedirect = { onRedirectClickListener() }
        )
    }
}

@Composable
fun BodySignInScreen(
    padding: PaddingValues,
    onRedirect: () -> Unit
) {
    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding)
    ) {

        val (descriptionScreen, container) = createRefs()

        DescriptionScreenComponent(
            title = stringResource(id = R.string.sign_in),
            description = stringResource(id = R.string.sign_in_description),
            modifier = Modifier
                .constrainAs(descriptionScreen){
                    top.linkTo(parent.top, margin = 10.dp)
                    start.linkTo(parent.start, margin = 15.dp)
                    end.linkTo(parent.end, margin = 15.dp)
                }
        )

        Column(
            modifier = Modifier
                .constrainAs(container) {
                    width = Dimension.fillToConstraints
                    height = Dimension.fillToConstraints
                    top.linkTo(descriptionScreen.bottom, margin = 40.dp)
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
                .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
                .background(Onyx)
                .padding(15.dp)
                .verticalScroll(rememberScrollState())
        ) {

            Spacer(modifier = Modifier
                .fillMaxSize()
                .height(30.dp))

            /*InputComponent(
                hilt = stringResource(id = R.string.email),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(53.dp),
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next
                )
            )*/

            Spacer(modifier = Modifier
                .fillMaxWidth()
                .height(10.dp))

            /*InputComponent(
                hilt = stringResource(id = R.string.password),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(53.dp),
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                ),
                visualTransformation = PasswordVisualTransformation()
            )*/

            Spacer(modifier = Modifier
                .fillMaxWidth()
                .height(15.dp))

            Button(
                onClick = { },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(45.dp),
                shape = CutCornerShape(ZeroCornerSize),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = UltramarineBlue,
                    contentColor = White
                )
            ) {
                Text(
                    text = stringResource(id = R.string.sign_in),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily(Font(R.font.roboto))
                )
            }

            Spacer(modifier = Modifier
                .fillMaxWidth()
                .height(20.dp))

            AuthenticationAlternativeComponent(
                onGoogleClickListener = {},
                onFacebookClickListener = {}
            )

            Spacer(modifier = Modifier.size(20.dp))

            QuestionComponent(
                question = stringResource(id = R.string.youDoNotHaveAnAccount),
                textButton = stringResource(id = R.string.sign_up),
                redirect = { onRedirect() }
            )

        }
    }
}