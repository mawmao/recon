package com.maacro.recon.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.maacro.recon.ui.theme.ReconTokens

object ReconButtonDefaults {
    val CornerShape = ReconTokens.RoundedCornerShape
    val ContentPadding: PaddingValues = PaddingValues(horizontal = 20.dp, vertical = 16.dp)

    val filledContainerColor: Color
        @Composable get() = MaterialTheme.colorScheme.onBackground

    val filledContentColor: Color
        @Composable get() = MaterialTheme.colorScheme.background
}


@Composable
fun ReconButton(
    modifier: Modifier = Modifier,
    text: String,
    prefixIcon: @Composable () -> Unit = {},
    suffixIcon: @Composable () -> Unit = {},
    enabled: Boolean = true,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.spacedBy(4.dp),
    containerColor: Color = ReconButtonDefaults.filledContainerColor,
    contentColor: Color = ReconButtonDefaults.filledContentColor,
    shape: Shape = ReconButtonDefaults.CornerShape,
    contentPadding: PaddingValues = ReconButtonDefaults.ContentPadding,
    textStyle: TextStyle = MaterialTheme.typography.labelLarge.copy(fontSize = 15.sp),
    onClick: () -> Unit,
) {
    Button(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        shape = shape,
        contentPadding = PaddingValues(0.dp), // reset
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor = MaterialTheme.colorScheme.onSurface
        ),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = horizontalArrangement,
            modifier = Modifier.padding(contentPadding)
        ) {
            prefixIcon()
            Text(
                text = text,
                color = contentColor,
                style = textStyle
            )
            suffixIcon()
        }
    }
}


@Composable
fun ReconLoadingButton(
    modifier: Modifier = Modifier,
    text: String,
    onClick: () -> Unit,
    prefixIcon: @Composable () -> Unit = {},
    isLoading: Boolean,
    enabled: Boolean = true,
    containerColor: Color = ReconButtonDefaults.filledContainerColor,
    contentColor: Color = ReconButtonDefaults.filledContentColor,
    shape: Shape = ReconButtonDefaults.CornerShape,
    contentPadding: PaddingValues = ReconButtonDefaults.ContentPadding,
    textStyle: TextStyle = MaterialTheme.typography.labelLarge.copy(fontSize = 15.sp)
) {
    ReconButton(
        text = text,
        modifier = modifier,
        containerColor = if (isLoading) {
            MaterialTheme.colorScheme.onBackground.copy(alpha = 0.75F)
        } else containerColor,
        enabled = enabled,
        contentColor = contentColor,
        prefixIcon = prefixIcon,
        shape = shape,
        textStyle = textStyle,
        contentPadding = contentPadding,
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        suffixIcon = {
            AnimatedVisibility(isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(16.dp),
                    color = Color.White,
                    strokeWidth = 2.dp
                )
            }
        },
        onClick = onClick,
    )
}

@Composable
fun ReconTextButton(
    modifier: Modifier = Modifier,
    text: String,
    isLoading: Boolean = false,
    textStyle: TextStyle = MaterialTheme.typography.labelSmall.copy(fontSize = 13.sp),
    onClick: () -> Unit,
) {
    AnimatedVisibility(visible = isLoading) {
        CircularProgressIndicator(
            modifier = Modifier.size(16.dp),
            color = Color.White,
            strokeWidth = 2.dp
        )
    }

    AnimatedVisibility(visible = !isLoading) {
        Text(
            text = text,
            style = textStyle,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = modifier
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = onClick
                )
        )
    }
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