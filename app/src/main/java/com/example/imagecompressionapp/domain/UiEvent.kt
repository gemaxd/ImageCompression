package com.example.imagecompressionapp.domain

import java.io.File

sealed class UIEvent {
    data class SetDestinyFile(val destinyFile: File): UIEvent()
    data class SetSourceFile(val sourceFile: File): UIEvent()
    data class CompressImage(val sourceFile: File, val destinationFile: File): UIEvent()
    data class ChangeLog(val compressionLog: String, val destinyFile: File): UIEvent()
}
