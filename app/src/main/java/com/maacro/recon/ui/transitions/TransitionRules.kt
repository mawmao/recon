package com.maacro.recon.ui.transitions

import androidx.compose.runtime.Stable
import androidx.compose.ui.util.fastFirstOrNull
import com.maacro.recon.ui.transitions.Transitions.Horizontal

fun buildTransitionTable(block: TransitionTableBuilder.() -> Unit): TransitionTable {
    return TransitionTableBuilder().apply(block).build()
}

@Stable
data class TransitionRule(val predicate: (String?) -> Boolean, val transition: TransitionSpec)

@Stable
class TransitionTable(private val rules: List<TransitionRule>) {
    fun resolveTransition(defaultTransition: TransitionSpec = Horizontal): TransitionSpec {
        fun pick(route: String?) = findTransitionOrNull(rules, route) ?: defaultTransition
        return TransitionSpec(
            enterTransition = { pick(initialState.destination.route).enterTransition?.invoke(this) },
            exitTransition = { pick(targetState.destination.route).exitTransition?.invoke(this) },
            popEnterTransition = {
                pick(initialState.destination.route).popEnterTransition?.invoke(
                    this
                )
            },
            popExitTransition = { pick(targetState.destination.route).popExitTransition?.invoke(this) }
        )
    }


    private fun findTransitionOrNull(rules: List<TransitionRule>, route: String?): TransitionSpec? =
        rules.fastFirstOrNull { it.predicate(route) }?.transition
}

class TransitionTableBuilder {
    private val rules = mutableListOf<TransitionRule>()

    fun indexed(vararg routes: String) =
        apply {
            val rule = { route: String? -> route != null && route in routes.toSet() }
            with(transition = Transitions.indexedHorizontal(routes.toList()), rule = rule)
        }

    fun with(route: String, transition: TransitionSpec) = with(transition) { it == route }
    fun with(transition: TransitionSpec, rule: (String?) -> Boolean) = apply {
        rules += TransitionRule(rule, transition)
    }

    fun build(): TransitionTable = TransitionTable(rules.toList())
}

