package org.bmsk.androidcoroutinesimulation

import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ExceptionSimulationTest {
    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        println("Caught exception: ${throwable.message}")
    }

    @Test
    fun testDefaultScopeExceptionHandling() = runTest {
        val scope = CoroutineScope(Dispatchers.Default)

        scope.launch {
            println("DefaultScope: Starting coroutine")
            throw RuntimeException("Exception in DefaultScope coroutine")
        }

        // Allow time for exception propagation
        delay(100)

        println("DefaultScope: Test completed")
    }

    @Test
    fun testGlobalScopeExceptionHandling() = runTest {
        GlobalScope.launch {
            println("GlobalScope: Starting coroutine")
            throw RuntimeException("Exception in GlobalScope coroutine")
        }

        // Allow time for exception propagation
        delay(100)

        println("GlobalScope: Test completed")
    }

    @Test
    fun testRunBlockingExceptionHandling() {
        try {
            runBlocking {
                println("runBlocking: Starting coroutine")
                throw RuntimeException("Exception in runBlocking coroutine")
            }
        } catch (e: Exception) {
            println("runBlocking: Caught exception: ${e.message}")
        }
    }

    @Test
    fun testSupervisorScopeExceptionHandling() = runTest {
        val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default + exceptionHandler)

        scope.launch {
            println("SupervisorScope: Child 1 started")
            throw RuntimeException("Exception in Child 1")
        }

        scope.launch {
            println("SupervisorScope: Child 2 started")
            delay(50)
            println("SupervisorScope: Child 2 completed")
        }

        // Allow time for all coroutines to complete
        delay(100)

        println("SupervisorScope: Test completed")
    }
}