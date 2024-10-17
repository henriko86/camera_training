package com.yuruneji.camera_training.presentation

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
import com.yuruneji.camera_training.presentation.camera.CameraScreen
import com.yuruneji.camera_training.presentation.log_view.LogViewScreen
import com.yuruneji.camera_training.presentation.setting.SettingScreen
import com.yuruneji.camera_training.presentation.ui.theme.CameraTrainingTheme
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        Timber.i(Throwable().stackTrace[0].methodName)
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
                        composable(route = ScreenRoute.SettingScreen.route) {
                            SettingScreen(navController = navController)
                        }
                        composable(route = ScreenRoute.LogViewState.route) {
                            LogViewScreen(navController = navController)
                        }
                    }
                }

                // LogViewScreen()
            }
        }
    }
}
