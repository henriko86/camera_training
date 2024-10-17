package com.yuruneji.camera_training.presentation.camera

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.Manifest.permission.CAMERA
import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.util.Size
import android.widget.LinearLayout
import android.widget.ListPopupWindow
import android.widget.TextClock
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.UseCase
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.takeOrElse
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.yuruneji.camera_training.common.CommonUtil
import com.yuruneji.camera_training.common.service.LocationService
import com.yuruneji.camera_training.common.service.SoundService
import com.yuruneji.camera_training.domain.usecase.FaceAnalyzer
import com.yuruneji.camera_training.domain.usecase.QrCodeAnalyzer
import com.yuruneji.camera_training.presentation.ScreenRoute
import com.yuruneji.camera_training.presentation.camera.view.DrawRectView
import com.yuruneji.camera_training.presentation.ui.theme.CameraTrainingTheme
import timber.log.Timber
import java.util.concurrent.Executors

/**
 * @author toru
 * @version 1.0
 */
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun CameraScreen(
    navController: NavController
) {
    val permissionState: MultiplePermissionsState = rememberMultiplePermissionsState(
        listOf(
            CAMERA,
            ACCESS_FINE_LOCATION,
            ACCESS_COARSE_LOCATION
        )
    )

    if (permissionState.allPermissionsGranted) {
        CameraStart(navController)
    } else {
        NoPermission(onRequestPermission = permissionState::launchMultiplePermissionRequest)
    }
}

@Composable
fun NoPermission(onRequestPermission: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Button(onClick = onRequestPermission) {
            Text(text = "権限許可")
        }
    }
}

@Composable
fun CameraStart(
    navController: NavController,
    viewModel: CameraViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    var location by remember { mutableStateOf("") }
    val resultMessage by remember { mutableStateOf("") }


    val systemUiController = rememberSystemUiController()
    val useDarkIcons = !isSystemInDarkTheme()
    DisposableEffect(systemUiController, useDarkIcons) {
        // systemUiController.setSystemBarsColor(
        //     color = Color.Transparent,
        //     darkIcons = useDarkIcons
        // )
        enableFullScreen(context)
        onDispose {
            disableFullScreen(context)
        }
    }


    // 効果音
    val soundService = SoundService(context)
    viewModel.setSoundService(soundService)

    // 位置情報
    val locationService = LocationService(context) {
        Timber.i("location: ${it.latitude}, ${it.longitude}")
        location = "${it.latitude}, ${it.longitude}"
    }

    val lifecycle = LocalLifecycleOwner.current.lifecycle
    DisposableEffect(Unit) {
        lifecycle.addObserver(soundService)
        lifecycle.addObserver(locationService)
        onDispose {
            lifecycle.removeObserver(soundService)
            lifecycle.removeObserver(locationService)
        }
    }


    val windowSize = CommonUtil.getWindowSize()
    val cameraImageSize = if (windowSize.width > windowSize.height) {
        Size(640, 480)
    } else {
        Size(480, 640)
    }


    val drawFaceView by remember { mutableStateOf(DrawRectView(context)) }
    drawFaceView.setImageSize(cameraImageSize.width, cameraImageSize.height)

    val drawQrView by remember { mutableStateOf(DrawRectView(context)) }
    drawQrView.setImageSize(cameraImageSize.width, cameraImageSize.height)


    val faceAnalyzer = FaceAnalyzer { items ->
        drawFaceView.draw(items.map { it.faceRect })
    }
    val faceImageAnalysis = ImageAnalysis.Builder()
        .setOutputImageRotationEnabled(true)
        // .setTargetRotation(rotation)
        // .setTargetResolution(android.util.Size(480, 640))
        .build()
    faceImageAnalysis.setAnalyzer(Executors.newSingleThreadExecutor(), faceAnalyzer)


    val qrCodeAnalyzer = QrCodeAnalyzer { items ->
        drawQrView.draw(items.map { it.rect })
    }
    val qrCodeImageAnalysis = ImageAnalysis.Builder()
        .setOutputImageRotationEnabled(true)
        // .setTargetRotation(rotation)
        .build()
    qrCodeImageAnalysis.setAnalyzer(Executors.newSingleThreadExecutor(), qrCodeAnalyzer)


    val previewView = PreviewView(context).apply {
        layoutParams = LinearLayout.LayoutParams(ListPopupWindow.MATCH_PARENT, ListPopupWindow.MATCH_PARENT)
        scaleType = PreviewView.ScaleType.FILL_CENTER
    }


    // val useCaseGroup = UseCaseGroup.Builder()
    //     .addUseCase(preview)
    //     .addUseCase(faceImageAnalysis)
    //     .addUseCase(qrCodeImageAnalysis)
    //     .build()


    // val cameraProviderFuture = ProcessCameraProvider.getInstance(context)

    // DisposableEffect(Unit) {
    // val cameraProvider = cameraProviderFuture.get()

    // val cameraSelector = CameraSelector.Builder()
    //     .requireLensFacing(CameraSelector.LENS_FACING_FRONT)
    //     .build()

    // preview.surfaceProvider = previewView.surfaceProvider
    // cameraProvider.bindToLifecycle(
    //     lifecycleOwner,
    //     cameraSelector,
    //     // preview,
    //     // imageAnalysis1,
    //     // imageAnalysis2
    //     useCaseGroup
    // )
    // onDispose {
    //     cameraProvider.unbindAll()
    // }

    // context.startCamera(
    //     lifecycleOwner = lifecycleOwner,
    //     preview
    // )
    // }

    // // カメラを設定
    // val cameraSelector = CameraSelector.Builder()
    //     .requireLensFacing(CameraSelector.LENS_FACING_FRONT)
    //     .build()


    Scaffold { paddingValues: PaddingValues ->
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            AndroidView(
                modifier = Modifier.padding(paddingValues),
                factory = {
                    val preview = androidx.camera.core.Preview.Builder().build()
                    preview.surfaceProvider = previewView.surfaceProvider

                    previewView.setOnLongClickListener {
                        navController.navigate(ScreenRoute.LogViewState.route)
                        true
                    }

                    context.startCamera(
                        lifecycleOwner = lifecycleOwner,
                        preview,
                        faceImageAnalysis,
                        qrCodeImageAnalysis
                    )

                    return@AndroidView previewView
                }
            )
            AndroidView(
                factory = { drawFaceView },
                modifier = Modifier.padding(paddingValues)
            )
            AndroidView(
                factory = { drawQrView },
                modifier = Modifier.padding(paddingValues)
            )
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Black.copy(alpha = 0.5f))
                .padding(4.dp),
            contentAlignment = Alignment.TopStart
        ) {
            Column {
                Text(
                    text = location,
                    color = Color.White
                )
            }
        }
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.BottomCenter
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White.copy(alpha = 0.5f)),
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(4.dp)
                ) {
                    Text(
                        text = resultMessage
                    )
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp, 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextClock(
                        format12Hour = "hh:mm:ss",
                        format24Hour = "hh:mm:ss",
                        color = Color.White,
                        style = TextStyle(
                            fontSize = 24.sp
                        )
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Image(
                        painter = painterResource(com.yuruneji.camera_training.R.drawable.baseline_wifi_24_on),
                        contentDescription = ""
                    )
                    Image(
                        painter = painterResource(com.yuruneji.camera_training.R.drawable.baseline_person_24_on),
                        contentDescription = ""
                    )
                    Image(
                        painter = painterResource(com.yuruneji.camera_training.R.drawable.baseline_credit_card_24_on),
                        contentDescription = ""
                    )
                    Image(
                        painter = painterResource(com.yuruneji.camera_training.R.drawable.baseline_qr_code_24_on),
                        contentDescription = ""
                    )
                }
            }
        }
    }
}

@Composable
private fun TextClock(
    modifier: Modifier = Modifier,
    format12Hour: String? = null,
    format24Hour: String? = null,
    timeZone: String? = null,
    color: Color = Color.Unspecified,
    style: TextStyle = MaterialTheme.typography.labelLarge,
) {
    val textColor = color.takeOrElse {
        style.color.takeOrElse {
            LocalContentColor.current
        }
    }

    AndroidView(
        factory = { context ->
            TextClock(context).apply {
                format12Hour?.let { this.format12Hour = it }
                format24Hour?.let { this.format24Hour = it }
                timeZone?.let { this.timeZone = it }

                textSize.let { this.textSize = style.fontSize.value }
                setTextColor(textColor.toArgb())
            }
        },
        modifier = modifier
    )
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun CameraScreenPreview() {
    val navController = rememberNavController()
    CameraTrainingTheme {
        CameraStart(navController)
    }
}

fun enableFullScreen(context: Context) {
    val window = context.findActivity().window
    WindowCompat.setDecorFitsSystemWindows(window, false)
    WindowInsetsControllerCompat(window, window.decorView).apply {
        systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        hide(WindowInsetsCompat.Type.systemBars())
    }
}

fun disableFullScreen(context: Context) {
    val window = context.findActivity().window
    WindowCompat.setDecorFitsSystemWindows(window, true)
    WindowInsetsControllerCompat(window, window.decorView).apply {
        show(WindowInsetsCompat.Type.systemBars())
        // systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_BARS_BY_TOUCH
    }
}

fun Context.findActivity(): Activity {
    var context = this
    while (context is ContextWrapper) {
        if (context is Activity) return context
        context = context.baseContext
    }
    throw IllegalStateException("no activity")
}

private fun Context.startCamera(
    lifecycleOwner: LifecycleOwner,
    vararg useCases: UseCase,
) {
    val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
    cameraProviderFuture.addListener({
        val cameraProvider = cameraProviderFuture.get()
        val cameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA

        try {
            cameraProvider.unbindAll()
            cameraProvider.bindToLifecycle(lifecycleOwner, cameraSelector, *useCases)
        } catch (e: Exception) {
            Timber.e(e)
        }
    }, ContextCompat.getMainExecutor(this))
}
