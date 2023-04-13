package com.daniel.ping.ui.screens

import android.app.Activity
import android.content.Context
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.shape.ZeroCornerSize
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import com.daniel.ping.R
import com.daniel.ping.domain.commons.ProviderGoogleSignInClient
import com.daniel.ping.ui.components.*
import com.daniel.ping.ui.theme.Onyx
import com.daniel.ping.ui.theme.RangoonGreen
import com.daniel.ping.ui.theme.UltramarineBlue
import com.daniel.ping.ui.viewModels.AuthViewModelBase
import com.daniel.ping.ui.viewModels.SignUpViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import kotlinx.coroutines.launch

@Composable
fun SignUpScreen(
    viewModel: SignUpViewModel = hiltViewModel(),
    state: AuthViewModelBase.AuthState,
    context: Context,
    activity: Activity,
    onRedirect: () -> Unit,
    onRedirectToProfileSetup: () -> Unit
) {

    val scaffoldState = rememberScaffoldState() // Remember the state of the scaffold
    val scope = rememberCoroutineScope() // Remember the coroutine scope

    Scaffold(
        topBar = { AppBarComponent() },
        scaffoldState = scaffoldState, // Use the remembered scaffold state
        snackbarHost = { // Define the snackBar host
            SnackbarHost(it){ data ->
                Snackbar(
                    snackbarData = data,
                    backgroundColor = RangoonGreen,
                    contentColor = White,
                    elevation = 10.dp
                )
            }
        }
    ) { padding ->

        ConstraintLayout(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {

            val (descriptionScreen, container) = createRefs()

            var email by remember { mutableStateOf("") } // Remember the email value
            var password by remember { mutableStateOf("") } // Remember the password value

            LaunchedEffect(state.message){ // Execute a side effect when the message state changes
                if(state.message.isNotEmpty()){ // Check if the message is not empty
                    scope.launch { // Launch a coroutine
                        scaffoldState.snackbarHostState.showSnackbar( // Show a snackBar with the message
                            message = state.message
                        )
                        viewModel.setMessage("") // Clear the message
                    }
                }
            }

            LaunchedEffect(state.registrationCompleted){ // Execute a side effect when the registrationCompleted state changes
                if(state.registrationCompleted){ // Check if registration is completed
                    onRedirectToProfileSetup() // Call the onRedirectToProfileSetup function
                    viewModel.setRegistrationCompleted(false) // Clear the registrationCompleted state
                    viewModel.setEmailAndPassword("", "") // Clear the email and password values

                }
            }

            val launcher = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()){ result -> // Remember the launcher for the StartActivityForResult function
                if(result.resultCode == Activity.RESULT_OK){ // Check if the result is OK
                    val task = GoogleSignIn.getSignedInAccountFromIntent(result.data) // Get the signed-in account from the result data
                    viewModel.handleResults(task, context) // Call the handleResults function with the task and context
                }
            }

            DescriptionScreenComponent(
                title = stringResource(id = R.string.sign_up),
                description =  stringResource(id = R.string.sign_up_description),
                modifier = Modifier
                    .constrainAs(descriptionScreen) {
                        top.linkTo(parent.top, margin = 10.dp)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
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
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Spacer(
                    modifier = Modifier
                        .fillMaxSize()
                        .height(30.dp)
                )

                InputComponent(
                    value = email,
                    hilt = stringResource(id = R.string.email),
                    onValueChange = { value -> email = value },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(53.dp),
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Email,
                        imeAction = ImeAction.Next
                    )
                )

                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(10.dp)
                )

                InputComponent(
                    value = password,
                    hilt = stringResource(id = R.string.password),
                    onValueChange = { value -> password = value },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(53.dp),
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Done
                    ),
                    visualTransformation = PasswordVisualTransformation()
                )

                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(15.dp)
                )

                AnimatedVisibility(visible = state.isLoading) {
                    CircularProgressIndicator(
                        color = UltramarineBlue,
                        strokeWidth = 3.dp,
                        modifier = Modifier.size(25.dp)
                    )
                }

                AnimatedVisibility(visible = !state.isLoading) {
                    Button(
                        onClick = {
                            viewModel.setEmailAndPassword(email, password)
                            viewModel.signUp()
                        },
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
                            text = stringResource(id = R.string.sign_up),
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily(Font(R.font.roboto))
                        )
                    }
                }

                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(20.dp)
                )

                AuthenticationAlternativeComponent(
                    onGoogleClickListener = {
                        launcher.launch(
                            ProviderGoogleSignInClient().invoke(context)
                        )
                    },
                    onFacebookClickListener = {
                        viewModel.withFacebook(activity)
                    }
                )

                Spacer(modifier = Modifier.size(20.dp))

                QuestionComponent(
                    question = stringResource(id = R.string.doYouAlreadyHaveAnAccount),
                    textButton = stringResource(id = R.string.sign_in),
                    redirect = { onRedirect() }
                )

            }
        }
    }
}