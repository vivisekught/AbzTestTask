package com.abz.agency.testtask.presentation.screens.sign_up

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsIgnoringVisibility
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.SheetState
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.core.content.FileProvider
import com.abz.agency.testtask.R
import com.abz.agency.testtask.core.FileUtils.createImageFile
import com.abz.agency.testtask.presentation.screens.sign_up.dialog.UserRegisterDialog
import com.abz.agency.testtask.presentation.screens.sign_up.state.RegistrationDialogType
import com.abz.agency.testtask.presentation.screens.sign_up.state.SignUpScreenState
import com.abz.agency.testtask.presentation.screens.sign_up.vm.SignUpScreenEvent
import com.abz.agency.testtask.presentation.ui.elements.PrimaryButton
import com.abz.agency.testtask.presentation.ui.elements.PrimaryInputField
import com.abz.agency.testtask.presentation.ui.elements.SecondaryButton
import com.abz.agency.testtask.presentation.ui.theme.Black48
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.accompanist.permissions.rememberPermissionState
import kotlinx.coroutines.launch
import java.util.Objects

// You also need to wrap the ModalBottomSheet in an if condition or it will always show
// and when you call hide() without the if condition, it will be invisible
// but still block any interaction with the UI
@ExperimentalMaterial3Api
val SheetState.shouldShowModalBottomSheet
    get() = isVisible || targetValue == SheetValue.Expanded


@OptIn(
    ExperimentalPermissionsApi::class, ExperimentalMaterial3Api::class,
    ExperimentalLayoutApi::class
)
@Composable
fun SignUpScreen(uiState: SignUpScreenState, onEvent: (SignUpScreenEvent) -> Unit, navigateToUsersScreen: () -> Unit) {
    val modalBottomSheetState =
        rememberStandardBottomSheetState(initialValue = SheetValue.Hidden, skipHiddenState = false)
    val coroutineScope = rememberCoroutineScope()

    val context = LocalContext.current
    var imageUri by remember { mutableStateOf<Uri?>(Uri.EMPTY) }
    val uri = remember {
        FileProvider.getUriForFile(
            Objects.requireNonNull(context),
            "${context.packageName}.fileProvider", context.createImageFile()
        )
    }

    val cameraPermissionState = rememberPermissionState(Manifest.permission.CAMERA)

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture(),
        onResult = { success ->
            if (success) imageUri = uri
            if (imageUri.toString().isNotEmpty()) {
                onEvent(SignUpScreenEvent.OnPhotoChange(imageUri.toString()))
            }
        }
    )

    val mediaPermissionState = rememberMultiplePermissionsState(
        permissions = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) listOf(
            Manifest.permission.READ_MEDIA_IMAGES
        ) else listOf(
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
        )
    )

    val galleryLauncher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.StartActivityForResult()
        ) {
            if (it.resultCode == Activity.RESULT_OK) {
                val data: Intent? = it.data
                if (data != null) {
                    imageUri = Uri.parse(data.data.toString())
                    if (imageUri.toString().isNotEmpty()) {
                        onEvent(SignUpScreenEvent.OnPhotoChange(imageUri.toString()))
                    }
                }
            }
        }
    val hasCameraPermission = cameraPermissionState.status.isGranted
    val hasMediaPermission = mediaPermissionState.allPermissionsGranted

    // Check if the registration dialog should be shown
    if (uiState.registrationDialogState.shouldShowDialog) {
        UserRegisterDialog(
            painter = painterResource(uiState.registrationDialogState.type.iconResId),
            text = stringResource(uiState.registrationDialogState.type.messageResId),
            btnText = stringResource(uiState.registrationDialogState.type.btnTextResId),
//            Different behavior when clicking on the dialog button,
//            If registration is successful, then we go to the page with users
            onButtonClick = {
                when (uiState.registrationDialogState.type) {
                    RegistrationDialogType.NONE -> Unit
                    RegistrationDialogType.FAILURE -> onEvent(SignUpScreenEvent.OnCloseDialogClick)
                    RegistrationDialogType.SUCCESS -> {
                        onEvent(SignUpScreenEvent.OnCloseDialogClick)
                        navigateToUsersScreen()
                    }
                    RegistrationDialogType.USER_ALREADY_EXISTS -> {
                        onEvent(SignUpScreenEvent.OnCloseDialogClick)
                    }
                }
            },
            onClose = {
                onEvent(SignUpScreenEvent.OnCloseDialogClick)
            }
        )
    }
     Column(
         Modifier
             .fillMaxSize()
             .padding(16.dp)
             // For scrolling content on small screens
             .verticalScroll(rememberScrollState())
    ) {
        PrimaryInputField(
            modifier = Modifier.fillMaxWidth(),
            text = uiState.name,
            label = stringResource(R.string.name),
            supportingText = uiState.nameErrorMessage.ifEmpty { "" },
            isError = uiState.nameErrorMessage.isNotEmpty()
        ) {
            onEvent(SignUpScreenEvent.OnNameChange(it))
        }

        PrimaryInputField(
            modifier = Modifier.fillMaxWidth(),
            text = uiState.email,
            label = stringResource(R.string.email),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            supportingText = uiState.emailErrorMessage.ifEmpty { "" },
            isError = uiState.emailErrorMessage.isNotEmpty()
        ) {
            onEvent(SignUpScreenEvent.OnEmailChange(it))
        }

        PrimaryInputField(
            modifier = Modifier.fillMaxWidth(),
            text = uiState.phone,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
            label = stringResource(R.string.phone),
            supportingText = uiState.phoneErrorMessage.ifEmpty { stringResource(R.string._38_xxx_xxx_xx_xx) },
            isError = uiState.phoneErrorMessage.isNotEmpty(),
        ) {
            onEvent(SignUpScreenEvent.OnPhoneChange(it))
        }

        Text(
            text = stringResource(R.string.select_yor_position),
            modifier = Modifier.padding(top = 32.dp),
            style = MaterialTheme.typography.bodyMedium
        )
        uiState.positions.forEach { positionEntity ->
            Row(
                Modifier
                    .fillMaxWidth()
                    .selectable(
                        selected = (uiState.positionId == positionEntity.id),
                        onClick = {
                            onEvent(SignUpScreenEvent.OnPositionChange(positionEntity.id))
                        }
                    )
                    .padding(vertical = 8.dp),
                verticalAlignment = CenterVertically
            ) {
                RadioButton(
                    colors = RadioButtonDefaults.colors(
                        selectedColor = MaterialTheme.colorScheme.secondary,
                        unselectedColor = MaterialTheme.colorScheme.secondary
                    ),
                    selected = (uiState.positionId == positionEntity.id),
                    onClick = { onEvent(SignUpScreenEvent.OnPositionChange(positionEntity.id)) }
                )
                Text(
                    text = positionEntity.name,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(start = 16.dp)
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        PrimaryInputField(
            modifier = Modifier.fillMaxWidth(),
            text = uiState.photoName,
            label = stringResource(R.string.upload_your_photo),
            supportingText = uiState.photoErrorMessage.ifEmpty { "" },
            isError = uiState.photoErrorMessage.isNotEmpty(),
            singleLine = true,
            enabled = uiState.photoErrorMessage.isNotEmpty(),
            trailingIcon = {
                SecondaryButton(text = stringResource(R.string.upload)) {
                    coroutineScope.launch { modalBottomSheetState.show() }
                }
            }
        ) {}
        PrimaryButton(
            text = stringResource(R.string.sign_up),
            enabled = true,
            modifier = Modifier.align(CenterHorizontally)
        ) {
            onEvent(SignUpScreenEvent.OnSignUpClick)
        }
        if (modalBottomSheetState.shouldShowModalBottomSheet) {
            cameraPermissionState.launchPermissionRequest()
            mediaPermissionState.launchMultiplePermissionRequest()
            ModalBottomSheet(
                sheetState = modalBottomSheetState,
                onDismissRequest = {
                    coroutineScope.launch { modalBottomSheetState.hide() }
                },
            ) {
                AddingPhotoDialog(onCameraSelected = {
                    if (hasCameraPermission) {
                        cameraLauncher.launch(uri)
                    } else {
                        cameraPermissionState.launchPermissionRequest()
                    }
                    coroutineScope.launch { modalBottomSheetState.hide() }
                }, onGallerySelected = {
                    if (hasMediaPermission) {
                        galleryLauncher.launch(
                            Intent(
                                Intent.ACTION_PICK,
                                MediaStore.Images.Media.INTERNAL_CONTENT_URI
                            )
                        )
                    } else {
                        mediaPermissionState.launchMultiplePermissionRequest()
                    }
                    coroutineScope.launch { modalBottomSheetState.hide() }
                })
                // Spacer for bottom bar height
                Spacer(
                    Modifier.windowInsetsBottomHeight(
                        WindowInsets.navigationBarsIgnoringVisibility
                    )
                )
            }
        }
    }
}

@Composable
fun AddingPhotoDialog(onCameraSelected: () -> Unit, onGallerySelected: () -> Unit) {
    ConstraintLayout(Modifier.fillMaxWidth()) {
        val (cameraRef, galleryRef, titleRef) = createRefs()
        Text(
            text = stringResource(R.string.choose_how_you_want_to_add_a_photo),
            color = Black48,
            modifier = Modifier.constrainAs(titleRef) {
                top.linkTo(parent.top, margin = 8.dp)
                centerHorizontallyTo(parent)
            })
        Column(modifier = Modifier.constrainAs(cameraRef) {
            top.linkTo(titleRef.bottom, margin = 32.dp)
            start.linkTo(parent.start)
            end.linkTo(galleryRef.start)
        }) {
            Image(
                painter = painterResource(id = R.drawable.ic_camera),
                contentDescription = stringResource(R.string.camera),
                modifier = Modifier
                    .size(64.dp)
                    .clickable {
                        onCameraSelected()
                    })
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = stringResource(R.string.camera),
                modifier = Modifier.align(CenterHorizontally)
            )
        }

        Column(modifier = Modifier.constrainAs(galleryRef) {
            top.linkTo(titleRef.bottom, margin = 32.dp)
            start.linkTo(cameraRef.end)
            end.linkTo(parent.end)
        }) {
            Image(
                painter = painterResource(id = R.drawable.ic_gallery),
                contentDescription = stringResource(R.string.gallery),
                modifier = Modifier
                    .size(64.dp)
                    .clickable {
                        onGallerySelected()
                    })
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = stringResource(R.string.gallery),
                modifier = Modifier.align(CenterHorizontally)
            )
        }
    }
}