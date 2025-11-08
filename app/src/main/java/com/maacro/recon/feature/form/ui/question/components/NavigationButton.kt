package com.maacro.recon.feature.form.ui.question.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.isImeVisible
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.maacro.recon.ui.components.ReconButton
import com.maacro.recon.ui.components.ReconOutlinedButton
import com.maacro.recon.ui.util.safeHorizontalPadding

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun QuestionNavigationButtons(
    hasNextPage: Boolean,
    repeatableTitle: String? = null,
    instance: Int? = null,
    onAddRepeatable: (() -> Unit)? = null,
    onRemoveRepeatable: (() -> Unit)? = null,
    onNext: () -> Unit,
    onReview: () -> Unit
) {
    val isImeVisible = WindowInsets.isImeVisible

    Column(
        modifier = Modifier
            .safeHorizontalPadding()
            .padding(bottom = 32.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        AnimatedVisibility(
            visible = onAddRepeatable != null && !isImeVisible,
            enter = fadeIn(tween(200)) + slideInVertically(initialOffsetY = { it }, animationSpec = tween(200)),
            exit = fadeOut(tween(200)) + slideOutVertically(targetOffsetY = { it }, animationSpec = tween(200))
        ) {
            ReconOutlinedButton(
                modifier = Modifier.fillMaxWidth(),
                text = "New $repeatableTitle ${instance ?: ""}",
                onClick = onAddRepeatable ?: {}
            )
        }

        AnimatedVisibility(
            visible = onRemoveRepeatable != null && !isImeVisible,
            enter = fadeIn(tween(200)) + slideInVertically(initialOffsetY = { it }, animationSpec = tween(200)),
            exit = fadeOut(tween(200)) + slideOutVertically(targetOffsetY = { it }, animationSpec = tween(200))
        ) {
            ReconOutlinedButton(
                modifier = Modifier.fillMaxWidth(),
                text = "Remove $repeatableTitle ${(instance?.minus(1)) ?: ""}",
                onClick = onRemoveRepeatable ?: {}
            )
        }

        ReconButton(
            modifier = Modifier.fillMaxWidth(),
            text = if (hasNextPage) "Continue" else "Review",
            onClick = if (hasNextPage) onNext else onReview
        )
    }
}
