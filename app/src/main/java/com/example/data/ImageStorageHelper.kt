package com.example.data

import android.content.Context
import android.net.Uri
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.util.UUID

object ImageStorageHelper {
    suspend fun copyPickedImageToInternalStorage(
        context: Context,
        sourceUri: Uri
    ): String? = withContext(Dispatchers.IO) {
        try {
            val directory = File(context.filesDir, "candle_photos").apply {
                if (!exists()) mkdirs()
            }
            val destFile = File(directory, "candle_${UUID.randomUUID()}.jpg")
            context.contentResolver.openInputStream(sourceUri)?.use { input ->
                destFile.outputStream().use { output ->
                    input.copyTo(output)
                }
            } ?: return@withContext null
            destFile.absolutePath
        } catch (_: Exception) {
            null
        }
    }
}
