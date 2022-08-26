package com.example.imagecompressionapp.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.File
import java.io.FileOutputStream
import kotlin.system.measureTimeMillis

object CompressUtil {

    suspend fun compressImage(sourceFile: File, destinationFile: File):Long{
        return measureTimeMillis {
            val currentBitmap = BitmapFactory.decodeFile(sourceFile.absolutePath)
            currentBitmap.compress(
                Bitmap.CompressFormat.JPEG,
                CompressFileUtils.COMPRESS_QUALITY,
                FileOutputStream(destinationFile)
            )
        }
    }

    suspend fun compressPDF(sourceFile: File, destinationFile: File):Long{
        return measureTimeMillis {
            val currentBitmap = BitmapFactory.decodeFile(sourceFile.absolutePath)
            currentBitmap.compress(
                Bitmap.CompressFormat.JPEG,
                CompressFileUtils.COMPRESS_QUALITY,
                FileOutputStream(destinationFile)
            )
        }
    }
}