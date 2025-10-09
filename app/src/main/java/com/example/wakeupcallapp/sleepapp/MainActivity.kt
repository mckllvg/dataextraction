package com.example.wakeupcallapp.sleepapp

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.auth.api.signin.GoogleSignIn
import kotlinx.coroutines.launch
import java.util.Calendar
import com.google.android.gms.auth.api.signin.GoogleSignInOptions


class MainActivity : AppCompatActivity() {

    private lateinit var fitnessRepository: FitnessRepository

    private lateinit var btnRequestPermission: Button
    private lateinit var btnGetSteps: Button
    private lateinit var btnGetSleep: Button
    private lateinit var tvResults: TextView

    companion object {
        private const val TAG = "MainActivity"
    }

    // Permission launcher for ACTIVITY_RECOGNITION
    private val activityRecognitionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            Log.d(TAG, "Activity Recognition permission granted")
            requestFitnessPermissions()
        } else {
            Toast.makeText(this, "Activity Recognition permission denied", Toast.LENGTH_LONG).show()
        }
    }

    // Launcher for Google Fitness API permissions
    // In MainActivity.kt

    private val fitnessPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            Log.d(TAG, "Fitness permissions granted")
            Toast.makeText(this, "Fitness permissions granted!", Toast.LENGTH_SHORT).show()

            // --- THIS IS THE FIX ---
            // DO NOT call updateUI() here. It will try to get the account too early.
            // Instead, manually update the UI state now that you know permissions are granted.
            btnRequestPermission.isEnabled = false
            btnGetSteps.isEnabled = true
            btnGetSleep.isEnabled = true
            tvResults.text = "✅ Permissions granted!\n\nTap buttons below to extract data."

        } else {
            Toast.makeText(this, "Fitness permissions denied", Toast.LENGTH_LONG).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize repository
        fitnessRepository = FitnessRepository(this)

        // Initialize views
        btnRequestPermission = findViewById(R.id.btnRequestPermission)
        btnGetSteps = findViewById(R.id.btnGetSteps)
        btnGetSleep = findViewById(R.id.btnGetSleep)
        tvResults = findViewById(R.id.tvResults)

        // Set click listeners
        btnRequestPermission.setOnClickListener {
            requestPermissions()
        }

        btnGetSteps.setOnClickListener {
            extractStepCount()
        }

        btnGetSleep.setOnClickListener {
            extractSleepDuration()
        }

        // Check initial permission state
        updateUI()
    }

    /**
     * Request all necessary permissions
     */
    private fun requestPermissions() {
        // First check Activity Recognition permission (required for Android Q+)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            when {
                ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACTIVITY_RECOGNITION
                ) == PackageManager.PERMISSION_GRANTED -> {
                    // Permission already granted, proceed to Fitness permissions
                    requestFitnessPermissions()
                }
                else -> {
                    // Request Activity Recognition permission
                    activityRecognitionLauncher.launch(Manifest.permission.ACTIVITY_RECOGNITION)
                }
            }
        } else {
            // For Android P and below, go directly to Fitness permissions
            requestFitnessPermissions()
        }
    }

    /**
     * Request Google Fitness API permissions
     */
    private fun requestFitnessPermissions() {
        val fitnessOptions = fitnessRepository.getFitnessOptions()
        val account = GoogleSignIn.getAccountForExtension(this, fitnessOptions)

        if (!GoogleSignIn.hasPermissions(account, fitnessOptions)) {
            // Build GoogleSignInOptions and add the FitnessOptions
            val signInOptions = GoogleSignInOptions.Builder()
                .addExtension(fitnessOptions)
                .build()

            // Get the client using the new signInOptions
            val intent = GoogleSignIn.getClient(this, signInOptions).signInIntent
            fitnessPermissionLauncher.launch(intent)
        } else {
            Toast.makeText(this, "Already have permissions!", Toast.LENGTH_SHORT).show()
            updateUI()
        }
    }

    /**
     * Extract step count data
     */
    private fun extractStepCount() {
        if (!fitnessRepository.hasPermissions()) {
            Toast.makeText(this, "Please grant permissions first", Toast.LENGTH_SHORT).show()
            return
        }

        tvResults.text = "Loading step count..."

        lifecycleScope.launch {
            try {
                // Get steps for last 7 days
                val startDate = Calendar.getInstance().apply {
                    add(Calendar.DAY_OF_YEAR, -7)
                }
                val totalSteps = fitnessRepository.getStepCount(startDate)

                // Get average daily steps
                val avgSteps = fitnessRepository.getAverageDailySteps(7)

                val resultText = if (totalSteps != null && avgSteps != null) {
                    """
                    ✅ STEP COUNT EXTRACTED
                    
                    Last 7 days total: $totalSteps steps
                    Average per day: $avgSteps steps
                    
                    Date range: ${startDate.time} to ${Calendar.getInstance().time}
                    """.trimIndent()
                } else {
                    "❌ Failed to extract step count. Make sure you have step data in Google Fit."
                }

                tvResults.text = resultText

            } catch (e: Exception) {
                Log.e(TAG, "Error extracting steps", e)
                tvResults.text = "❌ Error: ${e.message}"
            }
        }
    }

    /**
     * Extract sleep duration data
     */
    private fun extractSleepDuration() {
        if (!fitnessRepository.hasPermissions()) {
            Toast.makeText(this, "Please grant permissions first", Toast.LENGTH_SHORT).show()
            return
        }

        tvResults.text = "Loading sleep data..."

        lifecycleScope.launch {
            try {
                // Get sleep for last 7 days
                val startDate = Calendar.getInstance().apply {
                    add(Calendar.DAY_OF_YEAR, -7)
                }
                val totalSleep = fitnessRepository.getSleepDuration(startDate)

                // Get average sleep per night
                val avgSleep = fitnessRepository.getAverageSleepDuration(7)

                val resultText = if (totalSleep != null && avgSleep != null) {
                    """
                    ✅ SLEEP DURATION EXTRACTED
                    
                    Last 7 days total: %.1f hours
                    Average per night: %.1f hours
                    
                    Date range: ${startDate.time} to ${Calendar.getInstance().time}
                    """.trimIndent().format(totalSleep, avgSleep)
                } else {
                    "❌ Failed to extract sleep data. Make sure you have sleep data in Google Fit."
                }

                tvResults.text = resultText

            } catch (e: Exception) {
                Log.e(TAG, "Error extracting sleep", e)
                tvResults.text = "❌ Error: ${e.message}"
            }
        }
    }

    /**
     * Update UI based on permission state
     */
    private fun updateUI() {
        val hasPermissions = fitnessRepository.hasPermissions()

        btnRequestPermission.isEnabled = !hasPermissions
        btnGetSteps.isEnabled = hasPermissions
        btnGetSleep.isEnabled = hasPermissions

        if (hasPermissions) {
            tvResults.text = "✅ Permissions granted!\n\nTap buttons below to extract data."
        } else {
            tvResults.text = "ℹ️ Please grant permissions to access fitness data"
        }
    }
}