package com.gb.stopwatch

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import by.kirich1409.viewbindingdelegate.viewBinding
import com.gb.stopwatch.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(R.layout.activity_main) {
    private val binding: ActivityMainBinding by viewBinding(ActivityMainBinding::bind, R.id.container)
    private val model: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        model.timeLiveData.observe(this, { setTime(it) })

        binding.buttonStart.setOnClickListener { model.onStartClicked() }
        binding.buttonPause.setOnClickListener { model.onPauseClicked() }
        binding.buttonStop.setOnClickListener { model.onStopClicked() }
    }

    private fun setTime(it: String) {
        binding.textTime.text = it
    }
}
