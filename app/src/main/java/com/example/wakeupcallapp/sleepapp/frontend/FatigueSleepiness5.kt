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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class FatigueSleepiness5 : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FatigueSleepiness5ScreenContent()
        }
    }
}

@Composable
fun FatigueSleepiness5ScreenContent() {
    var carStoppedChoice by remember { mutableStateOf("") }

    Box(modifier = Modifier.fillMaxSize()) {
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
            Spacer(modifier = Modifier.height(36.dp))

            // Card container
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0x50FFFFFF))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Title
                    Text(
                        text = "Fatigue & Sleepiness",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // Question
                    Text(
                        text = "Sitting in a car, stopped for a few minutes due to traffic",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Radio options
                    Fatigue5RadioOption("No chance of dozing", carStoppedChoice) { carStoppedChoice = it }
                    Spacer(modifier = Modifier.height(8.dp))
                    Fatigue5RadioOption("Slight chance of dozing", carStoppedChoice) { carStoppedChoice = it }
                    Spacer(modifier = Modifier.height(8.dp))
                    Fatigue5RadioOption("Moderate chance of dozing", carStoppedChoice) { carStoppedChoice = it }
                    Spacer(modifier = Modifier.height(8.dp))
                    Fatigue5RadioOption("High chance of dozing", carStoppedChoice) { carStoppedChoice = it }

                    Spacer(modifier = Modifier.height(36.dp))

                    // Submit Button
                    Button(
                        onClick = { /* Handle submit */ },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0x40FFFFFF)),
                        shape = RoundedCornerShape(28.dp)
                    ) {
                        Text(
                            text = "Submit",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.White
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(60.dp))
        }
    }
}

@Composable
fun Fatigue5RadioOption(
    text: String,
    selectedValue: String,
    onSelect: (String) -> Unit
) {
    val selected = selectedValue == text
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onSelect(text) }
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
                .border(2.dp, Color.White, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            if (selected) {
                Box(
                    modifier = Modifier
                        .size(12.dp)
                        .background(Color(0xFF6B8DD6), CircleShape)
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
fun PreviewFatigueSleepiness5Screen() {
    FatigueSleepiness5ScreenContent()
}
