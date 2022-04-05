package com.huige.kthttp

import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.suspendCancellableCoroutine
import okhttp3.Call
import okhttp3.OkHttpClient
import okhttp3.Request
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class KtCall<T> {
}


// 代码段1

// 扩展函数
suspend fun <T : Any> KtCall<T>.await(): T =
//      暴露挂起函数的continuation
//              ↓
    suspendCancellableCoroutine { continuation ->
        val call = call(object : Callback<T> {
            override fun onSuccess(data: T) {
                println("Request success!")
                continuation.resume(data)
            }

            override fun onFail(throwable: Throwable) {
                println("Request fail!：$throwable")
                continuation.resumeWithException(throwable)
            }
        })

//          响应取消事件
//              ↓
        continuation.invokeOnCancellation {
            println("Call cancelled!")
            call.cancel()
        }
    }


// 代码段2

fun <T : Any> KtCall<T>.asFlow(): Flow<T> = callbackFlow {
    // 调用Callback
    val call = call(object : Callback<T> {
        override fun onSuccess(data: T) {
            // 1，传递成功数据，报错！
            offer(data)
        }

        override fun onFail(throwable: Throwable) {
            // 2，传递失败数据
            close(throwable)
        }

    })

    // 3，响应协程取消
    awaitClose {
        call.cancel()
    }
}

suspend fun testFlow(){
    val call = KtCall<String>()
    call.asFlow()
        .catch { println("Catch $it") }
        .collect {
            println(it)
        }

}


fun call(callback: Callback<*>): Call {
    val call: Call = OkHttpClient().newCall(Request.Builder().build())
    return call
}

interface Callback<T> {
    fun onSuccess(data: T)

    fun onFail(throwable: Throwable)
}


fun testBlock(block: (String,Int) -> Unit) {
    block("你好", 1)
}
