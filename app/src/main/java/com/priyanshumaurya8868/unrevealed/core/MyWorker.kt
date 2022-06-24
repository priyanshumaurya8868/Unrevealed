package com.priyanshumaurya8868.unrevealed.core

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.priyanshumaurya8868.unrevealed.core.utils.Constants.KEY_D_TOKEN
import com.priyanshumaurya8868.unrevealed.core.utils.Constants.KEY_JWT_TOKEN
import com.priyanshumaurya8868.unrevealed.secrets_sharing.domain.repo.Repository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class MyWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParameters: WorkerParameters,
    private val repo: Repository
) :
    CoroutineWorker(context, workerParameters) {
    override suspend fun doWork(): Result {

        val inData = inputData
        val jwtToken = inData.getString(KEY_JWT_TOKEN)
        val dToken = inData.getString(KEY_D_TOKEN)

        dToken?.let {
            val res = repo.sendDeviceToken(it, jwtToken)
            if (res.isFailure) {
              return Result.retry()
            }
        }
        return  Result.success()
    }
}