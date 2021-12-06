package com.gb.stopwatch.repos

interface TimestampProvider {
    fun getMilliseconds(): Long
}

class TimestampProviderImpl : TimestampProvider {
    override fun getMilliseconds() = System.currentTimeMillis()

}