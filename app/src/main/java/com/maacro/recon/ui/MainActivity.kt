package com.maacro.recon.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.maacro.recon.R
import com.maacro.recon.core.designsystem.components.ReconBackground
import com.maacro.recon.core.designsystem.theme.ReconColors
import com.maacro.recon.core.designsystem.theme.ReconTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ReconTheme {
                ReconApp()
            }
        }
    }
}

@Composable
fun TestScreen(modifier: Modifier = Modifier) {
    ReconBackground(gradient = ReconColors.defaultGradient) {
        Box(
            modifier = Modifier.Companion.fillMaxSize(),
            contentAlignment = Alignment.Companion.Center
        ) {
            Column(
                horizontalAlignment = Alignment.Companion.CenterHorizontally,
                modifier = Modifier.Companion.padding(horizontal = 16.dp)
            ) {
                Image(
                    painter = painterResource(R.drawable.logo),
                    contentDescription = "Logo",
                    modifier = Modifier.Companion.size(72.dp)
                )
                Spacer(modifier = Modifier.Companion.height(4.dp))
                Column {
                    Text(
                        text = "Recon",
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Companion.Bold
                    )
                    Text(text = "0.1.0")
                }
            }
        }
    }
}