package com.yuruneji.camera_training.domain.model

/**
 * @author toru
 * @version 1.0
 */
data class FaceAuthInfo(
    val result: Int,
    val name: String,
    val rect: String,
)
