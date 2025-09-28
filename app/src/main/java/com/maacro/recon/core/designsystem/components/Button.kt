package com.maacro.recon.core.designsystem.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

object ReconButtonDefaults {
    val CornerShape: RoundedCornerShape = RoundedCornerShape(8.dp)
    val ContentPadding: PaddingValues = PaddingValues(0.dp)
}

@Composable
fun ReconButton(
    modifier: Modifier = Modifier,
    text: String,
    onClick: () -> Unit,
    prefixIcon: @Composable () -> Unit = {},
    suffixIcon: @Composable () -> Unit = {},
    horizontalArrangement: Arrangement.Horizontal = Arrangement.spacedBy(4.dp),
    containerColor: Color = MaterialTheme.colorScheme.surface,
    contentColor: Color = MaterialTheme.colorScheme.onSurface,
    shape: Shape = ReconButtonDefaults.CornerShape,
) {
    Button(
        onClick = onClick,
        modifier = modifier,
        shape = shape,
        contentPadding = ReconButtonDefaults.ContentPadding,
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor = MaterialTheme.colorScheme.onSurface
        ),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = horizontalArrangement,
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp)
        ) {
            prefixIcon()
            Text(
                text = text,
                color = contentColor,
                style = MaterialTheme.typography.labelLarge.copy(
                    fontSize = 15.sp
                )
            )
            suffixIcon()
        }
    }
}

@Composable
fun ReconTextButton(
    modifier: Modifier = Modifier,
    text: String,
    onClick: () -> Unit,
    containerColor: Color = Color.Transparent,
    contentColor: Color = MaterialTheme.colorScheme.onSurface,
    shape: Shape = RectangleShape,
) {
    ReconButton(
        modifier = modifier,
        text = text,
        onClick = onClick,
        horizontalArrangement = Arrangement.Center,
        containerColor = containerColor,
        contentColor = contentColor,
        shape = shape
    )
}

@Composable
fun ReconIconButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    imageVector: ImageVector,
    contentDescription: String?,
    tint: Color = LocalContentColor.current,
    iconSize: Dp = 24.dp
) {
    IconButton(
        modifier = modifier,
        onClick = onClick,
    ) {
        Icon(
            modifier = Modifier.size(iconSize),
            imageVector = imageVector,
            contentDescription = contentDescription,
            tint = tint
        )
    }
}