package com.example.wakeupcallapp.sleepapp

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.wakeupcallapp.sleepapp.R

@Composable
fun DashboardScreen(navController: NavController) {
    Box(modifier = Modifier.fillMaxSize()) {

        // Background Image
        Image(
            painter = painterResource(id = R.drawable.background),
            contentDescription = "Dashboard Background",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // Scrollable Content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // Header
            Text(
                text = "WakeUp Call",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Sleep Apnea Risk Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0x50FFFFFF))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Sleep Apnea Risk",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White
                    )
                    Text(
                        text = "High Risk",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFE53935)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Risk Description
            Text(
                text = "Based on your demographic and lifestyle data, your predicted risk for Obstructive Sleep Apnea is high.",
                fontSize = 15.sp,
                color = Color.White,
                lineHeight = 22.sp
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Take Questionnaire Button
            Button(
                onClick = { navController.navigate("fatiguesleepiness1") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0x60FFFFFF)),
                shape = RoundedCornerShape(28.dp)
            ) {
                Text(
                    text = "Retake Questionnaire",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Personal Health Summary Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0x50FFFFFF))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                ) {
                    Text(
                        text = "Personal Health Summary",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            HealthDataItem("Age", "45", Modifier.weight(1f))
                            HealthDataItem("BMI", "32.5", Modifier.weight(1f))
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            HealthDataItem("Sex", "Male", Modifier.weight(1f))
                            HealthDataItem("Weight Category", "Obese", Modifier.weight(1f))
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            HealthDataItem("Neck Circumference", "44 cm", Modifier.weight(1f))
                            HealthDataItem("Sleep Duration", "6.0 hrs", Modifier.weight(1f))
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            HealthDataItem("Physical Activity", "Low", Modifier.weight(1f))
                            HealthDataItem("Blood Pressure", "140/95 mmHg", Modifier.weight(1f))
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Feature Importance Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0x50FFFFFF))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                ) {
                    Text(
                        text = "Feature Importance",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    FeatureBar("BMI", 0.9f, Color(0xFFE53935))
                    Spacer(modifier = Modifier.height(12.dp))
                    FeatureBar("Neck Circumference", 0.6f, Color(0xFFE53935))
                    Spacer(modifier = Modifier.height(12.dp))
                    FeatureBar("Sleep Duration", 0.4f, Color(0xFF42A5F5))
                    Spacer(modifier = Modifier.height(12.dp))
                    FeatureBar("Physical Activity", 0.3f, Color(0xFF42A5F5))
                    Spacer(modifier = Modifier.height(12.dp))
                    FeatureBar("Stress Level", 0.1f, Color(0xFF42A5F5))

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Influence on apnea risk",
                        fontSize = 14.sp,
                        color = Color(0xCCFFFFFF),
                        textAlign = TextAlign.End,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            // Model Explanation Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0x50FFFFFF))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Model Explanation for Your Risk Level",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        modifier = Modifier.padding(bottom = 20.dp)
                    )

                    // Top row of factors
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("BMI", fontSize = 14.sp, color = Color.White, fontWeight = FontWeight.Medium)
                        }
                        Text("+", fontSize = 16.sp, color = Color.White)
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("Neck", fontSize = 14.sp, color = Color.White, fontWeight = FontWeight.Medium)
                            Text("Circumference", fontSize = 12.sp, color = Color(0xCCFFFFFF))
                        }
                        Text("+", fontSize = 16.sp, color = Color.White)
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("Stress", fontSize = 14.sp, color = Color.White, fontWeight = FontWeight.Medium)
                            Text("Level", fontSize = 12.sp, color = Color(0xCCFFFFFF))
                        }
                        Text("+", fontSize = 16.sp, color = Color.White)
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("Physical", fontSize = 14.sp, color = Color.White, fontWeight = FontWeight.Medium)
                            Text("Activity", fontSize = 12.sp, color = Color(0xCCFFFFFF))
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Arrow
                    Text("↓", fontSize = 28.sp, color = Color.White)

                    Spacer(modifier = Modifier.height(12.dp))

                    // Bottom row - result
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("Drivinet", fontSize = 14.sp, color = Color.White, fontWeight = FontWeight.Medium)
                        }
                        Text(" + ", fontSize = 16.sp, color = Color.White)
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("Physical Accrual", fontSize = 14.sp, color = Color.White, fontWeight = FontWeight.Medium)
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Final arrow
                    Text("↓", fontSize = 28.sp, color = Color.White)

                    Spacer(modifier = Modifier.height(12.dp))

                    // Risk Result
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFE53935))
                    ) {
                        Text(
                            text = "HIGH RISK",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            modifier = Modifier.padding(horizontal = 24.dp, vertical = 10.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Recommendations Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0x50FFFFFF))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                ) {
                    Text(
                        text = "Recommendations",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )

                    RecommendationItem("• Seek professional evaluation from a sleep specialist")
                    RecommendationItem("• Maintain a healthy weight through diet and exercise")
                    RecommendationItem("• Avoid alcohol and sedatives before bedtime")
                    RecommendationItem("• Sleep on your side instead of your back")
                    RecommendationItem("• Consider using a CPAP machine if prescribed")
                }
            }

            Spacer(modifier = Modifier.height(120.dp)) // Extra space for footer
        }



        // Footer with glass panel and logo
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp)
                .padding(horizontal = 32.dp, vertical = 16.dp)
                .clip(RoundedCornerShape(24.dp))
                .background(Color.White.copy(alpha = 0.1f))
                .blur(16.dp)
                .align(Alignment.BottomCenter)
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "App Logo",
                modifier = Modifier
                    .size(50.dp)
                    .align(Alignment.Center),
                contentScale = ContentScale.Fit
            )
        }
    }
}

@Composable
fun HealthDataItem(label: String, value: String, modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        Text(text = label, fontSize = 14.sp, color = Color(0xCCFFFFFF))
        Text(text = value, fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = Color.White)
    }
}

@Composable
fun FeatureBar(label: String, progress: Float, color: Color) {
    Column {
        Text(text = label, fontSize = 14.sp, color = Color.White, modifier = Modifier.padding(bottom = 4.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .background(Color(0x30FFFFFF), RoundedCornerShape(4.dp))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(progress)
                    .fillMaxHeight()
                    .background(color, RoundedCornerShape(4.dp))
            )
        }
    }
}

@Composable
fun RecommendationItem(text: String) {
    Text(text = text, fontSize = 15.sp, color = Color.White, lineHeight = 22.sp, modifier = Modifier.padding(vertical = 4.dp))
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewDashboard() {
    val navController = rememberNavController()
    DashboardScreen(navController)
}
