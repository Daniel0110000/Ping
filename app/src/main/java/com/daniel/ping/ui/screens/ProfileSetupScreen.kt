package com.daniel.ping.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.shape.ZeroCornerSize
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberImagePainter
import com.daniel.ping.R
import com.daniel.ping.ui.components.AppBarComponent
import com.daniel.ping.ui.components.InputComponent
import com.daniel.ping.ui.theme.Onyx
import com.daniel.ping.ui.theme.RangoonGreen
import com.daniel.ping.ui.theme.UltramarineBlue
import com.daniel.ping.ui.theme.White
import com.daniel.ping.ui.viewModels.ProfileSetupViewModel
import kotlinx.coroutines.launch

@Composable
fun ProfileSetupScreen(
    viewModel: ProfileSetupViewModel = hiltViewModel(),
    state: ProfileSetupViewModel.ProSetupState,
    onRedirectToMainActivity: () -> Unit
) {

    val scaffoldState = rememberScaffoldState() // Remember the state of the scaffold
    val scope = rememberCoroutineScope() // Remember the coroutines scope

    Scaffold(
        topBar = { AppBarComponent() },
        scaffoldState = scaffoldState,
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

            val (imagePreview, openGallery, container) = createRefs()

            var name by remember { mutableStateOf("") } // Variable for profile name
            var description by remember { mutableStateOf("") } // Variable for profile description
            var imageProfilePreview by remember { mutableStateOf<Uri?>(null) } // Variable for profile image preview

            LaunchedEffect(state.message){  // Effect launched when the state message changes
                if(state.message.isNotEmpty()){
                    scope.launch {
                        scaffoldState.snackbarHostState.showSnackbar( // Shows a snackBar on the screen
                            message = state.message
                        )
                        viewModel.setMessage("") // Resets the state message in the viewModel
                    }
                }
            }

            LaunchedEffect(state.completed){ // Effect launched when the profile iss completed
                if(state.completed){
                    onRedirectToMainActivity() // Redirects to the MainActivity
                    viewModel.setCompleted(false) // Resets the completed variable in the viewModel
                    viewModel.setNameAndDescription("", "") // Resets the name and description values in the viewModel
                }
            }

            val galleryLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()){ result: Uri? -> // Launcher to open the image gallery
                result?.let { imageProfilePreview = result } // Updates the profile image preview variable if an image is selected
            }

            Image(
                painter = rememberImagePainter(imageProfilePreview),
                contentDescription = "",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(110.dp)
                    .clip(CircleShape)
                    .background(Onyx)
                    .constrainAs(imagePreview) {
                        top.linkTo(parent.top, margin = 10.dp)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
            )

            Card(
                modifier = Modifier
                    .size(30.dp)
                    .clickable { galleryLauncher.launch("image/*") }
                    .constrainAs(openGallery) {
                        bottom.linkTo(imagePreview.bottom)
                        end.linkTo(imagePreview.end)
                    },
                backgroundColor = UltramarineBlue,
                shape = CircleShape
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_add_photo),
                    contentDescription = "Icon add photo",
                    tint = White,
                    modifier = Modifier
                        .scale(0.5f)
                )
            }
            
            Column(
                modifier = Modifier
                    .constrainAs(container) {
                        width = Dimension.fillToConstraints
                        height = Dimension.fillToConstraints
                        top.linkTo(imagePreview.bottom, 30.dp)
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
                
                Spacer(modifier = Modifier.height(30.dp))
                
                InputComponent(
                    value = name,
                    hilt = stringResource(id = R.string.name),
                    onValueChange = { value -> name = value },
                    modifier = Modifier
                        .fillMaxSize()
                        .height(53.dp),
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Next
                    )
                )
                
                Spacer(modifier = Modifier.height(10.dp))

                InputComponent(
                    value = description,
                    hilt = stringResource(id = R.string.description),
                    onValueChange = { value -> description = value },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(53.dp),
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Done
                    )
                )
                
                Spacer(modifier = Modifier.height(50.dp))

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
                            viewModel.setProfileImage(imageProfilePreview)
                            viewModel.setNameAndDescription(name, description)
                            viewModel.finish()
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
                            text = stringResource(id = R.string.finish),
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily(Font(R.font.roboto))
                        )
                    }
                }
                
            }

        }
    }
}