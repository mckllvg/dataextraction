package com.example.wakeupcallapp.sleepapp

import android.content.Context
import android.util.Log
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.fitness.Fitness
import com.google.android.gms.fitness.FitnessOptions
import com.google.android.gms.fitness.data.DataType
import com.google.android.gms.fitness.data.Field
import com.google.android.gms.fitness.request.DataReadRequest
import kotlinx.coroutines.tasks.await
import java.util.Calendar
import java.util.concurrent.TimeUnit

class FitnessRepository(private val context: Context) {

    companion object {
        private const val TAG = "FitnessRepository"
    }

    private val fitnessOptions = FitnessOptions.builder()
        .addDataType(DataType.TYPE_STEP_COUNT_DELTA, FitnessOptions.ACCESS_READ)
        .addDataType(DataType.AGGREGATE_STEP_COUNT_DELTA, FitnessOptions.ACCESS_READ)
        .addDataType(DataType.TYPE_SLEEP_SEGMENT, FitnessOptions.ACCESS_READ)
        .build()

    fun hasPermissions(): Boolean {
        val account = getGoogleAccount()
        return account != null && GoogleSignIn.hasPermissions(account, fitnessOptions)
    }

    fun getGoogleAccount(): GoogleSignInAccount? {
        return GoogleSignIn.getAccountForExtension(context, fitnessOptions)
    }

    fun getFitnessOptions(): FitnessOptions = fitnessOptions

    suspend fun getStepCount(
        startDate: Calendar = Calendar.getInstance().apply { add(Calendar.DAY_OF_YEAR, -7) },
        endDate: Calendar = Calendar.getInstance()
    ): Int? {
        return try {
            val account = getGoogleAccount() ?: run {
                Log.e(TAG, "No Google account found")
                return null
            }

            val readRequest = DataReadRequest.Builder()
                .aggregate(DataType.AGGREGATE_STEP_COUNT_DELTA)
                .bucketByTime(1, TimeUnit.DAYS)
                .setTimeRange(startDate.timeInMillis, endDate.timeInMillis, TimeUnit.MILLISECONDS)
                .build()

            val response = Fitness.getHistoryClient(context, account)
                .readData(readRequest)
                .await()

            var totalSteps = 0
            for (bucket in response.buckets) {
                for (dataSet in bucket.dataSets) {
                    for (dataPoint in dataSet.dataPoints) {
                        val steps = dataPoint.getValue(Field.FIELD_STEPS).asInt()
                        totalSteps += steps
                        Log.d(TAG, "Steps: $steps on ${dataPoint.getStartTime(TimeUnit.MILLISECONDS)}")
                    }
                }
            }

            Log.i(TAG, "Total steps: $totalSteps")
            totalSteps

        } catch (e: Exception) {
            Log.e(TAG, "Error getting step count", e)
            null
        }
    }

    suspend fun getSleepDuration(
        startDate: Calendar = Calendar.getInstance().apply { add(Calendar.DAY_OF_YEAR, -7) },
        endDate: Calendar = Calendar.getInstance()
    ): Double? {
        return try {
            val account = getGoogleAccount() ?: run {
                Log.e(TAG, "No Google account found")
                return null
            }

            val readRequest = DataReadRequest.Builder()
                .read(DataType.TYPE_SLEEP_SEGMENT)
                .setTimeRange(startDate.timeInMillis, endDate.timeInMillis, TimeUnit.MILLISECONDS)
                .build()

            val response = Fitness.getHistoryClient(context, account)
                .readData(readRequest)
                .await()

            var totalSleepMillis = 0L
            for (dataSet in response.dataSets) {
                for (dataPoint in dataSet.dataPoints) {
                    val sleepType = dataPoint.getValue(Field.FIELD_SLEEP_SEGMENT_TYPE).asInt()
                    val start = dataPoint.getStartTime(TimeUnit.MILLISECONDS)
                    val end = dataPoint.getEndTime(TimeUnit.MILLISECONDS)
                    val duration = end - start

                    if (sleepType in listOf(2, 4, 5, 6)) {
                        totalSleepMillis += duration
                    }
                }
            }

            val totalSleepHours = totalSleepMillis / 1000.0 / 60.0 / 60.0
            Log.i(TAG, "Total sleep: $totalSleepHours hours")
            totalSleepHours

        } catch (e: Exception) {
            Log.e(TAG, "Error getting sleep duration", e)
            null
        }
    }

    suspend fun getAverageDailySteps(days: Int = 7): Int? {
        val startDate = Calendar.getInstance().apply {
            add(Calendar.DAY_OF_YEAR, -days)
        }
        val totalSteps = getStepCount(startDate) ?: return null
        return totalSteps / days
    }

    suspend fun getAverageSleepDuration(days: Int = 7): Double? {
        val startDate = Calendar.getInstance().apply {
            add(Calendar.DAY_OF_YEAR, -days)
        }
        val totalSleep = getSleepDuration(startDate) ?: return null
        return totalSleep / days
    }
}