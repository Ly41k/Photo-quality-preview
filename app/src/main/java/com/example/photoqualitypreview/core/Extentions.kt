package com.example.photoqualitypreview.core

import androidx.activity.ComponentActivity
import androidx.lifecycle.Lifecycle.State
import androidx.lifecycle.Lifecycle.State.STARTED
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

inline fun ComponentActivity.launchAndRepeatWithViewLifecycle(
    minActiveState: State = STARTED,
    crossinline block: suspend CoroutineScope.() -> Unit
) {
    lifecycleScope.launch { lifecycle.repeatOnLifecycle(minActiveState) { block() } }
}