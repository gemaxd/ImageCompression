package com.example.imagecompressionapp

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import com.example.imagecompressionapp.presentation.CoroutineTests
import com.example.imagecompressionapp.presentation.FileCompressorComponent
import com.example.imagecompressionapp.presentation.ImageCompressorComponent
import com.example.imagecompressionapp.ui.theme.ImageCompressionAPPTheme

class MainActivity : ComponentActivity() {

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ImageCompressionAPPTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    //CoroutineTests()
                    ImageCompressorComponent()
                    //FileCompressorComponent()
                }
            }
        }
    }
}
