package com.huige.kthttp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.coroutines.*
import java.util.concurrent.SynchronousQueue
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit
import kotlin.coroutines.resume

class MainActivity : AppCompatActivity() {
    val TAG = "MainActivity"
    val randomPool: ThreadPoolExecutor by lazy {
        ThreadPoolExecutor(0, 2147483647, 3L, TimeUnit.SECONDS, SynchronousQueue(),
            CustomNamedThreadFactory("randomPool"))
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        testCoroutine()
    }

    fun testCoroutine(){
        val scope = CoroutineScope(randomPool.asCoroutineDispatcher())
        scope.launch {
            Log.i(TAG, "1 ${getThreadName()}")
            coroutine1()
            Log.i(TAG, "4 ${getThreadName()}")
        }
        Log.i(TAG, "after coroutine")
    }
    suspend fun coroutine1(){
//        val scope = CoroutineScope(Dispatchers.Default)
//        scope.launch {
//            Log.i(TAG,"2 ${getThreadName()}")
//            delay(10000)
//            Log.i(TAG,"3 ${getThreadName()}  ${randomPool.activeCount}")
//        }
        suspendCancellableCoroutine<Int>() {
            val scope = CoroutineScope(Dispatchers.Default)
            scope.launch {
                Log.i(TAG, "2 ${getThreadName()}")
                delay(5000)
                Log.i(TAG, "3 ${getThreadName()}  ${randomPool.activeCount}")
                it.resume(1)
            }
        }
    }
}