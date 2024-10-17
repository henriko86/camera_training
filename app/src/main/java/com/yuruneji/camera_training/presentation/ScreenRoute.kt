package com.yuruneji.camera_training.presentation

/**
 * @author toru
 * @version 1.0
 */
sealed class ScreenRoute(val route: String) {
    object CameraScreen : ScreenRoute("camera_screen")
    object SettingScreen : ScreenRoute("setting_screen")
    object LogViewState : ScreenRoute("log_view_screen")
}
