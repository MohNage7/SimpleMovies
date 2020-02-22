package com.mohnage7.movies.utils

import android.os.SystemClock
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * this class adds the loaded data key to the map and check later
 * to determine if we need to refresh data or not.
 * key in @shouldFetch method is the key of the data that needs to be loaded.
 */
class RefreshRateLimiter<KEY>(timeUnit: TimeUnit, timeout: Long) {
    private val timeout: Long = timeUnit.toMillis(timeout)
    private val timestamps: HashMap<KEY, Long> = HashMap()

    @Synchronized
    fun shouldFetch(key: KEY): Boolean {
        val lastFetched = timestamps[key]
        val now = SystemClock.uptimeMillis()
        // if there's no data added before. add it and return true
        if (lastFetched == null) {
            timestamps[key] = now
            return true
        }
        if (now - lastFetched > timeout) {
            timestamps[key] = now
            return true
        }
        return false
    }

    @Synchronized
    fun remove(key: KEY) {
        timestamps.remove(key)
    }
}
