package org.bmsk.androidcoroutinesimulation

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class DefaultWithSupervisorJobActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        test2()

        setContent { }
    }

    /**
     * test1()
     *
     * - 이 함수에서는 parentJob이 일반 Job을 포함한 CoroutineScope를 사용한다(명시적으론 Job 미지정 시 기본 Job 생성).
     * - 내부에서 launch 두 개를 생성하는데, 둘 중 하나(child 2)에서 예외가 발생한다.
     * - 일반 Job의 경우 자식 코루틴 중 하나가 실패(예외)하면 부모와 다른 자식도 모두 취소된다.
     * - 즉 child 2에서 예외가 발생하면 child 1 역시 아직 완료되지 않았다면 취소되고, 부모 코루틴도 취소된다.
     * - CoroutineExceptionHandler는 예외를 잡고 로깅하지만, 예외 전파는 막지 못한다.
     *
     * 정리:
     * - Child 2가 예외 발생으로 취소 -> 예외가 전파됨 -> Child 1도 취소
     */
    private fun test1() {
        val exceptionHandler = CoroutineExceptionHandler { _, exception ->
            Log.e("DefaultWithSupervisorJobActivity", "Caught exception: ${exception.message}")
        }

        val parentJob = CoroutineScope(Dispatchers.Default + exceptionHandler)

        parentJob.launch {
            launch {
                delay(1000)
                Log.e("Child 1", "Child 1 completed")
            }

            launch {
                throw RuntimeException("Child 2 failed")
            }
        }
    }

    // CoroutineExceptionHandler 는 예외만 처리하는 것이지 예외 전파는 막지 못합니다.
    // 예외도 처리하고 예외 전파를 SupervisorJob으로 제한
    // 결국 두번째 자식은 취소되지 않습니다.
    private fun test2() {
        val supervisor = SupervisorJob()
        val exceptionHandler = CoroutineExceptionHandler { _, exception ->
            Log.e("DefaultWithSupervisorJobActivity", "Caught exception: ${exception.message}")
        }
        val parentJob = CoroutineScope(Dispatchers.IO + supervisor + exceptionHandler)

        parentJob.launch {
            launch {
                throw AssertionError("첫 째 Job이 AssertionError로 인해 취소됩니다.")
            }
            launch {
                delay(1000)
                println("둘 째 Job이 살아있습니다.")
            }
        }
    }
}