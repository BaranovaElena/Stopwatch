package com.gb.stopwatch

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.gb.stopwatch.StopwatchListOrchestrator.Companion.DEFAULT_TIME
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
    private var mutableTimeLiveData: MutableLiveData<String> = MutableLiveData(DEFAULT_TIME)
    val timeLiveData : LiveData<String> = mutableTimeLiveData

    private val timestampProvider = object : TimestampProvider {
        override fun getMilliseconds() = System.currentTimeMillis()
    }

    private val stopwatchListOrchestrator = StopwatchListOrchestrator(
        StopwatchStateHolder(
            StopwatchStateCalculator(timestampProvider, ElapsedTimeCalculator(timestampProvider)),
            ElapsedTimeCalculator(timestampProvider),
            TimestampMillisecondsFormatter()
        ),
        CoroutineScope(Dispatchers.Main + SupervisorJob())
    )

    init {
        CoroutineScope(Dispatchers.Main + SupervisorJob()).launch {
            stopwatchListOrchestrator.ticker.collect { mutableTimeLiveData.postValue(it) }
        }
    }

    fun onStartClicked() { stopwatchListOrchestrator.start() }
    fun onPauseClicked() { stopwatchListOrchestrator.pause() }
    fun onStopClicked() { stopwatchListOrchestrator.stop() }
}

interface TimestampProvider {
    fun getMilliseconds(): Long
}
