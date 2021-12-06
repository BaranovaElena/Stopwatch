package com.gb.stopwatch.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.gb.stopwatch.usecase.StopwatchStateCalculator
import com.gb.stopwatch.repos.TimestampProviderImpl
import com.gb.stopwatch.usecase.StopwatchListOrchestrator
import com.gb.stopwatch.usecase.StopwatchListOrchestrator.Companion.DEFAULT_TIME
import com.gb.stopwatch.usecase.StopwatchStateHolder
import com.gb.stopwatch.usecase.TimestampMillisecondsFormatter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
    private var mutableTimeLiveData: MutableLiveData<String> = MutableLiveData(DEFAULT_TIME)
    val timeLiveData : LiveData<String> = mutableTimeLiveData

    private val timestampProvider = TimestampProviderImpl()

    private val stopwatchListOrchestrator = StopwatchListOrchestrator(
        StopwatchStateHolder(
            StopwatchStateCalculator(timestampProvider),
            TimestampMillisecondsFormatter()
        ),
        CoroutineScope(Dispatchers.Main + SupervisorJob())
    )

    init {
        CoroutineScope(Dispatchers.Main + SupervisorJob()).launch {
            stopwatchListOrchestrator.ticker.collect {
                mutableTimeLiveData.postValue(it)
            }
        }
    }

    fun onStartClicked() { stopwatchListOrchestrator.start() }
    fun onPauseClicked() { stopwatchListOrchestrator.pause() }
    fun onStopClicked() { stopwatchListOrchestrator.stop() }
}
