package com.yuruneji.cameratraining.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.yuruneji.cameratraining.presentation.camera.CameraScreen
import com.yuruneji.cameratraining.presentation.setting.SettingState
import com.yuruneji.cameratraining.presentation.ui.theme.CameraTrainingTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // enableEdgeToEdge()
        setContent {
            CameraTrainingTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    NavHost(
                        navController = navController,
                        startDestination = ScreenRoute.CameraScreen.route,
                    ) {
                        composable(route = ScreenRoute.CameraScreen.route) {
                            CameraScreen(navController = navController)
                        }
                        composable(route = ScreenRoute.SettingState.route) {
                            SettingState(navController = navController)
                        }
                    }
                }
            }
        }
    }
}
