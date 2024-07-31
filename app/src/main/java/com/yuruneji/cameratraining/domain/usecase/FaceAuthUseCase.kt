package com.yuruneji.cameratraining.domain.usecase

import com.yuruneji.cameratraining.common.NetworkResponse
import com.yuruneji.cameratraining.data.remote.AppRequest
import com.yuruneji.cameratraining.data.remote.toConvert
import com.yuruneji.cameratraining.domain.model.FaceAuthInfo
import com.yuruneji.cameratraining.domain.repository.AppRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/**
 * @author toru
 * @version 1.0
 */
class FaceAuthUseCase @Inject constructor(
    private val repository: AppRepository,
) {
    operator fun invoke(request: AppRequest): Flow<NetworkResponse<FaceAuthInfo>> = flow {
        try {
            emit(NetworkResponse.Loading())
            val data = repository.faceAuth(request).toConvert()
            emit(NetworkResponse.Success(data))
        } catch (e: Exception) {
            emit(NetworkResponse.Failure(e))
        }
    }
}
