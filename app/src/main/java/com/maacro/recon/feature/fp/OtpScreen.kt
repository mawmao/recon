package com.maacro.recon.feature.fp

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.maacro.recon.ui.components.ReconTopAppBar
import com.maacro.recon.ui.util.safePadding

@Composable
fun OtpScreen(onNavigateBack: () -> Unit) {
    Column(
        modifier = Modifier.safePadding(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        ReconTopAppBar(
            onBackTap = onNavigateBack,
            actions = { }
        )
        Text("You are in OTP screen")
    }
}
