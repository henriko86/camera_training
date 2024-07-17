package com.yuruneji.cameratraining.domain.repository

import com.yuruneji.cameratraining.data.remote.AppRequest
import com.yuruneji.cameratraining.data.remote.AppResponse

/**
 * @author toru
 * @version 1.0
 */
interface AppRepository {
    suspend fun faceAuth(request: AppRequest): AppResponse
}
