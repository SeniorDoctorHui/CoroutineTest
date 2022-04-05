package com.huige.kthttp

import kotlinx.coroutines.*
import kotlinx.coroutines.Delay
import kotlinx.coroutines.channels.Channel
import org.junit.Test

import org.junit.Assert.*
import java.util.concurrent.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun blockTest(){
        testBlock { stringData, intData ->


        }
    }

    val conversationPool: ThreadPoolExecutor by lazy {
        ThreadPoolExecutor(1, 1,
            0L, TimeUnit.MILLISECONDS,
            LinkedBlockingQueue(), CustomNamedThreadFactory("conversationPool"))
    }
    val randomPool: ThreadPoolExecutor by lazy {
        ThreadPoolExecutor(0, 2147483647, 3L, TimeUnit.SECONDS, SynchronousQueue(),
            CustomNamedThreadFactory("randomPool"))
    }

    @Test
    fun testCoroutineCallback(){
        runBlocking(randomPool.asCoroutineDispatcher()) {
            println("1 ${getThreadName()}")
            coroutine1()
            //因为下面的代码是已经提交到线程执行的任务了，所以在恢复协程任务时，优先执行这个代码
            println("after coroutine1 ${getThreadName()}")
            var startTime = System.currentTimeMillis()
            while(getCurrentTime() - startTime < 5000){

            }
            println("3 ${getThreadName()}")
        }
        conversationPool.execute({
            println("5 ${getThreadName()}")
            Thread.sleep(5000)
            println("51 ${getThreadName()} ${randomPool.activeCount}")
        })
        Thread.sleep(10000)
    }

    suspend fun coroutine1(){
        runBlocking(Dispatchers.Default) {
            println("2 ${getThreadName()}")
//            println("conversationPool size=${conversationPool.queue.size}  ${conversationPool.activeCount}")
//            conversationPool.execute {
//                println("4 ${getThreadName()}")
//                var startTime = System.currentTimeMillis()
//                while(getCurrentTime() - startTime < 5000){
//
//                }
//                println("41 ${getThreadName()}")
//            }
            delay(5000)
            println("21 ${getThreadName()}  ${randomPool.activeCount}")
        }
//        suspendCancellableCoroutine<Int> {
//            println(getThreadName())
//        }

    }

    fun getThreadName() = Thread.currentThread().name

    fun getCurrentTime() = System.currentTimeMillis()
}