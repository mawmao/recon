package com.maacro.recon.ui.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.maacro.recon.ui.util.safePadding


@Composable
fun EmptyScreen(
    modifier: Modifier = Modifier,
    text: String = "Hello",
) {
    Column(
        modifier = modifier.fillMaxSize().safePadding(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text)
    }
}