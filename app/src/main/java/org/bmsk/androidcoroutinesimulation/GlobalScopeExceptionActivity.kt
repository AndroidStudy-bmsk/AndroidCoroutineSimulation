package org.bmsk.androidcoroutinesimulation

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class GlobalScopeExceptionActivity: ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        GlobalScope.launch {
            Log.i("GlobalScope", "Starting coroutine")
            throw RuntimeException("Exception in GlobalScope coroutine")
        }

        setContent {
            Box(modifier = Modifier.fillMaxSize().background(Color.Black)) {

            }
        }
    }
}