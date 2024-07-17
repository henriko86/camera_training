package com.yuruneji.cameratraining.presentation.camera

import android.Manifest
import android.util.Log
import android.widget.LinearLayout
import android.widget.ListPopupWindow
import androidx.camera.core.AspectRatio
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import timber.log.Timber

/**
 * @author toru
 * @version 1.0
 */
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun CameraScreen(
    navController: NavController,
    viewModel: CameraViewModel = hiltViewModel()
) {
    val permissionState: PermissionState =
        rememberPermissionState(permission = Manifest.permission.CAMERA)

    if (permissionState.status.isGranted) {
        CametaStert()
    } else {
        NoPermission(onRequestPermission = permissionState::launchPermissionRequest)
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
            Text(text = "カメラの許可を与えてください")
        }
    }
}

@Composable
fun CametaStert() {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val imageCapture = remember {
        ImageCapture.Builder()
            .setFlashMode(ImageCapture.FLASH_MODE_AUTO) // フラッシュモードを設定
            .setTargetAspectRatio(AspectRatio.RATIO_4_3) // ターゲットのアスペクト比を設定
            .build()
    }

    val previewView = PreviewView(context).apply {
        layoutParams =
            LinearLayout.LayoutParams(ListPopupWindow.MATCH_PARENT, ListPopupWindow.MATCH_PARENT)
        scaleType = PreviewView.ScaleType.FILL_CENTER
    }

    val cameraProviderFuture = ProcessCameraProvider.getInstance(context)
    DisposableEffect(Unit) {
        val cameraProvider = cameraProviderFuture.get()
        Timber.d(cameraProvider.toString())
        val preview = androidx.camera.core.Preview.Builder().build()

        val cameraSelector = CameraSelector.Builder()
            .requireLensFacing(CameraSelector.LENS_FACING_FRONT)
            .build()

        preview.setSurfaceProvider(previewView.surfaceProvider)
        cameraProvider.bindToLifecycle(
            lifecycleOwner,
            cameraSelector,
            preview,
            imageCapture
        )
        onDispose {
            cameraProvider.unbindAll()
        }
    }

    Scaffold(floatingActionButton = {
        FloatingActionButton(onClick = {
            Timber.i("ボタンクリック")
        }) {
            Icon(imageVector = Icons.Default.Add, contentDescription = "Clothesadd")
        }
    }) { paddingValues: PaddingValues ->
        AndroidView(
            factory = { previewView },
            modifier = Modifier.padding(paddingValues)
        )
    }
}
