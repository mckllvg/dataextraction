package com.example.wakeupcallapp.sleepapp

import android.os.Bundle
import com.example.wakeupcallapp.sleepapp.R
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class HealthHistory : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HealthHistoryScreenContent()
        }
    }
}

@Composable
fun HealthHistoryScreenContent() {
    var hypertension by remember { mutableStateOf("") }
    var diabetes by remember { mutableStateOf("") }
    var smoking by remember { mutableStateOf("") }
    var alcohol by remember { mutableStateOf("") }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        // Background Image
        Image(
            painter = painterResource(id = R.drawable.background),
            contentDescription = "Background",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // Scrollable content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(40.dp))

            // Card container
            Card(
                modifier = Modifier
                    .fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0x50FFFFFF)
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp)
                ) {
                    // Title
                    Text(
                        text = "Health & Lifestyle",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // Question 1: High Blood Pressure
                    Text(
                        text = "Have you been diagnosed with high blood pressure (hypertension)?",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.White,
                        lineHeight = 22.sp
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    HealthRadioButtonOption(
                        text = "Yes",
                        selected = hypertension == "Yes",
                        onClick = { hypertension = "Yes" }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    HealthRadioButtonOption(
                        text = "No",
                        selected = hypertension == "No",
                        onClick = { hypertension = "No" }
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    // Question 2: Diabetes
                    Text(
                        text = "Have you been diagnosed Diabetes?",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.White,
                        lineHeight = 22.sp
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    HealthRadioButtonOption(
                        text = "Yes",
                        selected = diabetes == "Yes",
                        onClick = { diabetes = "Yes" }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    HealthRadioButtonOption(
                        text = "No",
                        selected = diabetes == "No",
                        onClick = { diabetes = "No" }
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    // Question 3: Smoking
                    Text(
                        text = "Do you smoke?",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.White,
                        lineHeight = 22.sp
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    HealthRadioButtonOption(
                        text = "Yes",
                        selected = smoking == "Yes",
                        onClick = { smoking = "Yes" }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    HealthRadioButtonOption(
                        text = "No",
                        selected = smoking == "No",
                        onClick = { smoking = "No" }
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    // Question 4: Alcohol
                    Text(
                        text = "Do you drink alcohol?",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.White,
                        lineHeight = 22.sp
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    HealthRadioButtonOption(
                        text = "Yes",
                        selected = alcohol == "Yes",
                        onClick = { alcohol = "Yes" }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    HealthRadioButtonOption(
                        text = "No",
                        selected = alcohol == "No",
                        onClick = { alcohol = "No" }
                    )
                }
            }

            Spacer(modifier = Modifier.height(80.dp))
        }

        // Back button at bottom left
        IconButton(
            onClick = { /* Handle back navigation */ },
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(24.dp)
                .size(48.dp)
                .background(
                    color = Color(0x40FFFFFF),
                    shape = CircleShape
                )
        ) {
            Icon(
                painter = painterResource(android.R.drawable.ic_media_play),
                contentDescription = "Back",
                tint = Color.White,
                modifier = Modifier
                    .size(20.dp)
                    .graphicsLayer(rotationZ = 180f)
            )
        }
    }
}

@Composable
fun HealthRadioButtonOption(
    text: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(24.dp)
                .background(
                    color = if (selected) Color.White else Color.Transparent,
                    shape = CircleShape
                )
                .border(
                    width = 2.dp,
                    color = Color.White,
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            if (selected) {
                Box(
                    modifier = Modifier
                        .size(12.dp)
                        .background(
                            color = Color(0xFF6B8DD6),
                            shape = CircleShape
                        )
                )
            }
        }

        Spacer(modifier = Modifier.width(12.dp))

        Text(
            text = text,
            fontSize = 16.sp,
            color = Color.White,
            fontWeight = FontWeight.Normal
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewHealthHistoryScreen() {
    HealthHistoryScreenContent()
}