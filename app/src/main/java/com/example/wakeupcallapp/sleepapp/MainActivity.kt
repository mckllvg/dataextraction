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
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Calendar

class MainActivity : AppCompatActivity() {

    private lateinit var fitnessRepository: FitnessRepository
    private lateinit var btnRequestPermission: Button
    private lateinit var btnGetSteps: Button
    private lateinit var btnGetSleep: Button
    private lateinit var tvResults: TextView

    companion object {
        private const val TAG = "MainActivity"
    }

    // --- Permissions ---
    private val REQUIRED_PERMISSIONS = mutableListOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    ).apply {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            add(Manifest.permission.ACTIVITY_RECOGNITION)
        }
    }.toTypedArray()

    private val permissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val allGranted = permissions.all { it.value }
        if (allGranted) {
            Log.d(TAG, "All required permissions granted")
            requestFitnessPermissions()
        } else {
            Toast.makeText(this, "Permissions denied. Some features may not work.", Toast.LENGTH_LONG).show()
        }
    }

    private val fitnessPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            Log.d(TAG, "Google Fit permissions granted")
            Toast.makeText(this, "Fitness permissions granted!", Toast.LENGTH_SHORT).show()
            updateUIState(true)
        } else {
            Toast.makeText(this, "Fitness permissions denied", Toast.LENGTH_LONG).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fitnessRepository = FitnessRepository(this)

        btnRequestPermission = findViewById(R.id.btnRequestPermission)
        btnGetSteps = findViewById(R.id.btnGetSteps)
        btnGetSleep = findViewById(R.id.btnGetSleep)
        tvResults = findViewById(R.id.tvResults)

        btnRequestPermission.setOnClickListener { checkAndRequestPermissions() }
        btnGetSteps.setOnClickListener { extractStepCount() }
        btnGetSleep.setOnClickListener { extractSleepDuration() }

        lifecycleScope.launch { updateUI() }
    }

    // --- Check & request runtime permissions ---
    private fun checkAndRequestPermissions() {
        val notGranted = REQUIRED_PERMISSIONS.filter {
            ContextCompat.checkSelfPermission(this, it) != PackageManager.PERMISSION_GRANTED
        }

        if (notGranted.isNotEmpty()) {
            permissionLauncher.launch(notGranted.toTypedArray())
        } else {
            requestFitnessPermissions()
        }
    }

    private fun requestFitnessPermissions() {
        val fitnessOptions = fitnessRepository.getFitnessOptions()
        val account = GoogleSignIn.getAccountForExtension(this, fitnessOptions)

        if (!GoogleSignIn.hasPermissions(account, fitnessOptions)) {
            val signInOptions = GoogleSignInOptions.Builder()
                .addExtension(fitnessOptions)
                .build()
            val intent = GoogleSignIn.getClient(this, signInOptions).signInIntent
            fitnessPermissionLauncher.launch(intent)
        } else {
            Toast.makeText(this, "Already have permissions!", Toast.LENGTH_SHORT).show()
            lifecycleScope.launch { updateUI() }
        }
    }

    private suspend fun updateUI() {
        val hasPermissions = withContext(Dispatchers.IO) {
            try {
                fitnessRepository.hasPermissions()
            } catch (e: Exception) {
                Log.e(TAG, "Error checking permissions", e)
                false
            }
        }
        withContext(Dispatchers.Main) {
            updateUIState(hasPermissions)
        }
    }

    private fun updateUIState(hasPermissions: Boolean) {
        btnRequestPermission.isEnabled = !hasPermissions
        btnGetSteps.isEnabled = hasPermissions
        btnGetSleep.isEnabled = hasPermissions

        tvResults.text = if (hasPermissions) {
            "✅ Permissions granted!\n\nTap buttons below to extract data."
        } else {
            "ℹ️ Please grant permissions to access fitness data."
        }
    }

    private fun extractStepCount() {
        if (!fitnessRepository.hasPermissions()) {
            Toast.makeText(this, "Please grant permissions first", Toast.LENGTH_SHORT).show()
            return
        }

        tvResults.text = "Loading step count..."

        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val startDate = Calendar.getInstance().apply { add(Calendar.DAY_OF_YEAR, -7) }
                val totalSteps = fitnessRepository.getStepCount(startDate)
                val avgSteps = fitnessRepository.getAverageDailySteps(7)

                val resultText = if (totalSteps != null && avgSteps != null) {
                    """
                    ✅ STEP COUNT EXTRACTED

                    Last 7 days total: $totalSteps steps
                    Average per day: $avgSteps steps

                    Date range: ${startDate.time} → ${Calendar.getInstance().time}
                    """.trimIndent()
                } else {
                    "❌ Failed to extract step count. Make sure you have step data in Google Fit."
                }

                withContext(Dispatchers.Main) {
                    tvResults.text = resultText
                }

            } catch (e: Exception) {
                Log.e(TAG, "Error extracting steps", e)
                withContext(Dispatchers.Main) {
                    tvResults.text = "❌ Error: ${e.message}"
                }
            }
        }
    }

    private fun extractSleepDuration() {
        if (!fitnessRepository.hasPermissions()) {
            Toast.makeText(this, "Please grant permissions first", Toast.LENGTH_SHORT).show()
            return
        }

        tvResults.text = "Loading sleep data..."

        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val startDate = Calendar.getInstance().apply { add(Calendar.DAY_OF_YEAR, -7) }
                val totalSleep = fitnessRepository.getSleepDuration(startDate)
                val avgSleep = fitnessRepository.getAverageSleepDuration(7)

                val resultText = if (totalSleep != null && avgSleep != null) {
                    """
                    ✅ SLEEP DURATION EXTRACTED

                    Last 7 days total: %.1f hours
                    Average per night: %.1f hours

                    Date range: ${startDate.time} → ${Calendar.getInstance().time}
                    """.trimIndent().format(totalSleep, avgSleep)
                } else {
                    "❌ Failed to extract sleep data. Make sure you have sleep data in Google Fit."
                }

                withContext(Dispatchers.Main) {
                    tvResults.text = resultText
                }

            } catch (e: Exception) {
                Log.e(TAG, "Error extracting sleep", e)
                withContext(Dispatchers.Main) {
                    tvResults.text = "❌ Error: ${e.message}"
                }
            }
        }
    }
}
