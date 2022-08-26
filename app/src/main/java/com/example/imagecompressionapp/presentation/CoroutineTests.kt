package com.example.imagecompressionapp.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun CoroutineTests() {

    var noCoroutine by remember { mutableStateOf("") }
    var mainScopeResult by remember { mutableStateOf("") }
    var ioScopeResult by remember { mutableStateOf("") }
    var defaultResult by remember { mutableStateOf("") }
    var unconfinedResult by remember { mutableStateOf("") }

    val scope = rememberCoroutineScope()

    Column(modifier = Modifier.fillMaxSize()) {
        Row(modifier = Modifier.fillMaxWidth()) {
            Button(
                modifier = Modifier.weight(1f),
                onClick = {
                    Thread.sleep(5000)
                    noCoroutine = "Done"
                }
            ) {
                Text(
                    text = "DO without coroutine"
                )
            }
            Text(
                modifier = Modifier.weight(1f),
                text = noCoroutine
            )
        }

        Row(modifier = Modifier.fillMaxWidth()) {
            Button(
                modifier = Modifier.weight(1f),
                onClick = {
                    scope.launch(Dispatchers.Main) {
                        Thread.sleep(5000)
                        mainScopeResult = "Done"
                    }
                }
            ) {
                Text(
                    text = "DO main Thread"
                )
            }
            Text(
                modifier = Modifier.weight(1f),
                text = mainScopeResult
            )
        }

        Row(modifier = Modifier.fillMaxWidth()) {
            Button(
                modifier = Modifier.weight(1f),
                onClick = {
                    scope.launch(Dispatchers.IO) {
                        Thread.sleep(5000)
                        ioScopeResult = "Done"
                    }
                }
            ) {
                Text(
                    text = "DO IO Thread"
                )
            }
            Text(
                modifier = Modifier.weight(1f),
                text = ioScopeResult
            )
        }

        Row(modifier = Modifier.fillMaxWidth()) {
            Button(
                modifier = Modifier.weight(1f),
                onClick = {
                    scope.launch(Dispatchers.Default) {
                        Thread.sleep(5000)
                        defaultResult = "Done"
                    }
                }
            ) {
                Text(
                    text = "DO default Thread"
                )
            }
            Text(
                modifier = Modifier.weight(1f),
                text = defaultResult
            )
        }

        Row(modifier = Modifier.fillMaxWidth()) {
            Button(
                modifier = Modifier.weight(1f),
                onClick = {
                    scope.launch(Dispatchers.Unconfined) {
                        Thread.sleep(5000)
                        unconfinedResult = "Done"
                    }
                }
            ) {
                Text(
                    text = "DO unconfined Thread"
                )
            }
            Text(
                modifier = Modifier.weight(1f),
                text = unconfinedResult
            )
        }
    }
}
