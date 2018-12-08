package com.messy.pictorial

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters

class UpdateReadingWork(context: Context, workerParams: WorkerParameters) : Worker(context, workerParams) {

    override fun doWork(): Result {
        if (Config.isNeedToUpdate()) {

        }
        return Result.SUCCESS
    }

}