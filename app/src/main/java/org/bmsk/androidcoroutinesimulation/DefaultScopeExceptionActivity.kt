package org.bmsk.androidcoroutinesimulation

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DefaultScopeExceptionActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val defaultScope = CoroutineScope(Dispatchers.Default)

        defaultScope.launch {
            Log.i("DefaultScope", "Coroutine: Starting coroutine")
            throw RuntimeException("Exception in DefaultScope coroutine")
        }

        setContent {
            Box(Modifier.fillMaxSize()) { }
        }
    }
}