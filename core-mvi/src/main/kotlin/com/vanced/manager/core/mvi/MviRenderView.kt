package com.vanced.manager.origin.core.mvi

import kotlinx.coroutines.flow.Flow

interface MviRenderView<State, Action, SideEffect> {

    fun render(state: State)

    fun actionsFlow(): Flow<Action>

    fun sideEffects(sideEffect: SideEffect)
}