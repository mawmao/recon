package com.maacro.recon.ui.theme

import androidx.compose.ui.graphics.Color

object ReconColors {

    val HumayGreen = Color(0XFF00A63E)
    val White = Color.White

    val Green100 = Color(0xCDAFFFE0)

    val Cyan100 = Color(0xFFE0FFFF)
    val Gray200 = Color(0x40888888)

    val Gray100 = Color(0xFFF2F2F7) // background
    val Gray300 = Color(0xFFD9D9D9) // secondary
    val Gray500 = Color(0xFFA3A3A4) // onSurfaceVariant
    val Gray700 = Color(0xFF555556) // onSurface
    val Gray900 = Color(0xFF1C1C1E) // onBackground

    val Blue100 = Color(0xFFE0EEFF) // primary
    val Blue500 = Color(0xFF3B82F6) // onPrimary

    val Red100 = Color(0xFFFEECEC) // error
    val Red500 = Color(0xFFC22323) // OnError

    val LightGreen600 = Color(0xFF22C55E) // Battery

    val Yellow200 = Color(0xFFFFF6DD) // sun

    val defaultGradient = Gradient(
        0.2f to Color(0X40b6d5b6),
        1f to White
    )
}

