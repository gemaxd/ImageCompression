package com.example.imagecompressionapp.domain

import android.net.Uri
import java.io.File

data class UIState(
    val sourceFile: File? = null,
    val destinyFile: File? = null,
    val sourceFileUri: Uri? = null,
    val destinyFileUri: Uri? = null,
    val compressionLog: String = ""
)
