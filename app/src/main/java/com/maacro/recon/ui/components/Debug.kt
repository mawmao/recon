package com.maacro.recon.ui.components

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.isImeVisible
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.TextAutoSize
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.fastForEach
import com.maacro.recon.ui.util.debug
import com.maacro.recon.ui.util.safeHorizontalPadding

// if possible, use only for logging
@Composable
fun Monitor(
    onCompose: () -> Unit,
    onDispose: (() -> Unit)? = null,
    content: @Composable () -> Unit
) {
    DisposableEffect(Unit) {
        onCompose()
        onDispose { onDispose?.invoke() }
    }

    content()
}

@Composable
fun DebugText(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.75F),
    autoSize: TextAutoSize? = null,
    fontSize: TextUnit = 12.sp,
    fontStyle: FontStyle? = null,
    fontWeight: FontWeight? = null,
    fontFamily: FontFamily? = null,
    letterSpacing: TextUnit = TextUnit.Unspecified,
    textDecoration: TextDecoration? = null,
    textAlign: TextAlign? = null,
    lineHeight: TextUnit = TextUnit.Unspecified,
    overflow: TextOverflow = TextOverflow.Clip,
    softWrap: Boolean = true,
    maxLines: Int = Int.MAX_VALUE,
    minLines: Int = 1,
    onTextLayout: ((TextLayoutResult) -> Unit)? = null,
    style: TextStyle = LocalTextStyle.current,
) {
    Text(
        text = text,
        modifier = modifier,
        color = color,
        autoSize = autoSize,
        fontSize = fontSize,
        fontStyle = fontStyle,
        fontWeight = fontWeight,
        fontFamily = fontFamily,
        letterSpacing = letterSpacing,
        textDecoration = textDecoration,
        textAlign = textAlign,
        lineHeight = lineHeight,
        overflow = overflow,
        softWrap = softWrap,
        maxLines = maxLines,
        minLines = minLines,
        onTextLayout = onTextLayout,
        style = style
    )
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun DebugArea(
    modifier: Modifier = Modifier,
    title: String = "Debug",
    info: List<Pair<String, Any>> = emptyList(),
    hideOnKeyboardShow: Boolean = false,
    block: (@Composable () -> Unit)? = null,
) {
    if (!hideOnKeyboardShow || !WindowInsets.isImeVisible) {
        Spacer(modifier = Modifier.height(20.dp))
        Column(
            modifier = modifier
                .fillMaxWidth()
                .safeHorizontalPadding()
                .debug(color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.75F))
                .padding(8.dp)
                .animateContentSize()
        ) {
            Text(title, fontWeight = FontWeight.SemiBold)
            Spacer(modifier = Modifier.height(8.dp))

            info.fastForEach { pair ->
                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    DebugText("${pair.first}:")
                    DebugText(
                        "${pair.second}",
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.9F),
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            if (block != null) {
                block()
            }
        }
        Spacer(modifier = Modifier.height(20.dp))
    }
}

fun debugList(block: MutableList<Pair<String, Any>>.() -> Unit): List<Pair<String, Any>> {
    val list = mutableListOf<Pair<String, Any>>()
    list.block()
    return list
}

fun MutableList<Pair<String, Any>>.debug(label: String, value: Any) {
    add(label to value)
}


@Composable
fun DebugButton(
    text: String,
    modifier: Modifier = Modifier.fillMaxWidth(),
    onClick: () -> Unit
) {
    ReconButton(
        text = text,
        modifier = modifier,
        onClick = onClick,
        textStyle = MaterialTheme.typography.labelLarge.copy(fontSize = 12.sp),
        containerColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.75F),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
    )
}