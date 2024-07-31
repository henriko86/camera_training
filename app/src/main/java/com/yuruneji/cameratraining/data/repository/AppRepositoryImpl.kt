package com.yuruneji.cameratraining.data.repository

import com.yuruneji.cameratraining.data.remote.AppRequest
import com.yuruneji.cameratraining.data.remote.AppResponse
import com.yuruneji.cameratraining.data.remote.AppService
import com.yuruneji.cameratraining.domain.repository.AppRepository
import javax.inject.Inject

/**
 * @author toru
 * @version 1.0
 */
class AppRepositoryImpl @Inject constructor(
    private val api: AppService
) : AppRepository {
    override suspend fun faceAuth(request: AppRequest): AppResponse {
        return api.faceAuth(request)
    }
}
