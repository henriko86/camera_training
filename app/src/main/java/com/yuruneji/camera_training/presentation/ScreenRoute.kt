package com.yuruneji.camera_training.presentation

/**
 * @author toru
 * @version 1.0
 */
sealed class ScreenRoute(val route: String) {
    object CameraScreen : ScreenRoute("camera_screen")
    object SettingState : ScreenRoute("setting_screen")
}
