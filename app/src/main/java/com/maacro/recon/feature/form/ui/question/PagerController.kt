package com.maacro.recon.feature.form.ui.question

import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.CoroutineScope

@Composable
fun rememberPagerController(
    page: () -> Int,
    pageCount: () -> Int,
//    onPageUpdate: ((Int) -> Unit)? = null,
    scope: CoroutineScope = rememberCoroutineScope()
): PagerController {
    val pagerState = rememberPagerState(initialPage = page(), pageCount = pageCount)
    val controller = remember(pagerState) {
        PagerController(
            pagerState = pagerState, scope = scope
        )
    }

    /**
     * NOTE: currently page updates happen in the view model only (currentPage)
     * But if a requirement arises where updates from the UI also happen, put this back
     *
     * ```
     * snapshotFlow { page() }
     *     .distinctUntilChanged()
     *     .collectLatest { page ->
     *       onPageUpdate(page)
     * }
     * ```
     */
    LaunchedEffect(page()) {
        controller.scrollToPage(page())
    }

    return controller
}

class PagerController internal constructor(
    private val pagerState: PagerState,
    private val scope: CoroutineScope,
) {

    suspend fun scrollToPage(page: Int) = pagerState.animateScrollToPage(page)
    fun asPagerState(): PagerState = pagerState
}
