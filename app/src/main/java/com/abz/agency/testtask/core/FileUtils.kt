package com.abz.agency.testtask.core

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import com.abz.agency.testtask.core.api.Constants.PHOTO_EXTENSION
import com.abz.agency.testtask.core.api.Constants.PHOTO_NAME_PATTERN
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object FileUtils {

    fun Context.createImageFile(): File {
        // Create an image file name
        val timeStamp = SimpleDateFormat(PHOTO_NAME_PATTERN, Locale.getDefault()).format(Date())
        val imageFileName = "JPEG_" + timeStamp + "_"
        return File.createTempFile(
            imageFileName, /* prefix */
            PHOTO_EXTENSION, /* suffix */
            externalCacheDir /* directory */
        )
    }

    // https://nobanhasan.medium.com/get-picked-image-actual-path-android-11-12-180d1fa12692
    fun getRealPathFromURI(uri: Uri, context: Context): String? {
        val returnCursor = context.contentResolver.query(uri, null, null, null, null)
        val nameIndex = returnCursor!!.getColumnIndex(OpenableColumns.DISPLAY_NAME)
        returnCursor.moveToFirst()
        val name = returnCursor.getString(nameIndex)
        val file = File(context.filesDir, name)
        try {
            val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
            val outputStream = FileOutputStream(file)
            var read = 0
            val maxBufferSize = 1 * 1024 * 1024
            val bytesAvailable: Int = inputStream?.available() ?: 0
            val bufferSize = bytesAvailable.coerceAtMost(maxBufferSize)
            val buffers = ByteArray(bufferSize)
            while (inputStream?.read(buffers).also {
                    if (it != null) {
                        read = it
                    }
                } != -1) {
                outputStream.write(buffers, 0, read)
            }
            inputStream?.close()
            outputStream.close()

        } catch (e: java.lang.Exception) {
            return null
        }
        return file.path
    }

    fun isPhotoFromGallery(context: Context, uri: Uri): Boolean {
        // Check the authority of the URI
        val authority = uri.authority ?: return false
        return !authority.startsWith("${context.packageName}.fileProvider")
    }
}