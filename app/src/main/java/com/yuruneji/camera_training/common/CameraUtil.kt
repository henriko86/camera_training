package com.yuruneji.camera_training.common

import android.hardware.camera2.CameraCharacteristics
import com.yuruneji.camera_training.App

/**
 * @author toru
 * @version 1.0
 */
object CameraUtil {

    /**
     * カメラIDを取得
     */
    fun getCameraID(): CameraID {
        val cameraManager = App.applicationContext().getSystemService(android.content.Context.CAMERA_SERVICE) as android.hardware.camera2.CameraManager
        val cameraID = CameraID()
        cameraManager.cameraIdList.forEach { _ ->
            val backCameraId = cameraManager.cameraIdList.first { cameraManager.getCameraCharacteristics(it).get(CameraCharacteristics.LENS_FACING) == CameraCharacteristics.LENS_FACING_BACK }
            val frontCameraId = cameraManager.cameraIdList.first { cameraManager.getCameraCharacteristics(it).get(CameraCharacteristics.LENS_FACING) == CameraCharacteristics.LENS_FACING_FRONT }
            cameraID.back = backCameraId
            cameraID.front = frontCameraId
        }
        return cameraID
    }

}
