package com.yuruneji.cameratraining.presentation

/**
 * @author toru
 * @version 1.0
 */
sealed class ScreenRoute(val route: String) {
    object CameraScreen : ScreenRoute("camera_screen")
    object SettingState : ScreenRoute("setting_screen")
}
