package com.gb.stopwatch.usecase

import com.gb.stopwatch.entites.StopwatchState

class StopwatchStateHolder(
    private val stopwatchStateCalculator: StopwatchStateCalculator,
    private val timestampMillisecondsFormatter: TimestampMillisecondsFormatter
) {
    var currentState: StopwatchState = StopwatchState.Paused(0)
        private set

    fun start() {
        currentState = stopwatchStateCalculator.calculateRunningState(currentState)
    }

    fun pause() {
        currentState = stopwatchStateCalculator.calculatePausedState(currentState)
    }

    fun stop() {
        currentState = StopwatchState.Paused(0)
    }

    fun getStringTimeRepresentation(): String {
        val elapsedTime = when (val currentState = currentState) {
            is StopwatchState.Paused -> currentState.elapsedTime
            is StopwatchState.Running -> stopwatchStateCalculator.calculateElapsed(currentState)
        }
        return timestampMillisecondsFormatter.format(elapsedTime)
    }
}