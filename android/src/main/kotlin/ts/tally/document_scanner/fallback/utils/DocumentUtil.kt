package ts.tally.document_scanner.fallback.utils

import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.util.Log
import java.io.File

class DocumentUtil {
    fun getPathFromUri(context: Context, uri: Uri): String? {
        // DocumentProvider
        if (DocumentsContract.isDocumentUri(context, uri)) {
            if (isExternalStorageDocument(uri)) {
                // ExternalStorageProvider
                val docId = DocumentsContract.getDocumentId(uri)
                val split = docId.split(":")
                val type = split[0]
                if ("primary".equals(type, true)) {
                    return "${context.getExternalFilesDir(null)}/${split[1]}"
                } else {
                    // Handle non-primary volumes (SD cards or USB drives)
                    return "/storage/${type}/${split[1]}"
                }
            } else if (isDownloadsDocument(uri)) {
                // DownloadsProvider
                val id = DocumentsContract.getDocumentId(uri)
                val contentUri = Uri.parse("content://downloads/public_downloads")
                return getDataColumn(context, contentUri, "_id=?", arrayOf(id))
            } else if (isMediaDocument(uri)) {
                // MediaProvider
                val docId = DocumentsContract.getDocumentId(uri)
                val split = docId.split(":")
                val type = split[0]
                var contentUri: Uri? = null
                when (type) {
                    "image" -> contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                    "video" -> contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                    "audio" -> contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                }
                val selection = "_id=?"
                val selectionArgs = arrayOf(split[1])
                return getDataColumn(context, contentUri, selection, selectionArgs)
            }
        }
        // MediaStore (and general)
        else if ("content".equals(uri.scheme, true)) {
            // Return the remote address for some types of content URIs (e.g. Google Drive)
            return getDataColumn(context, uri, null, null)
        }
        // File
        else if ("file".equals(uri.scheme, true)) {
            return uri.path
        }
        return null
    }

    private fun getDataColumn(
        context: Context,
        uri: Uri?,
        selection: String?,
        selectionArgs: Array<String>?
    ): String? {
        var cursor: Cursor? = null
        val column = "_data"
        val projection = arrayOf(column)
        try {
            cursor = uri?.let {
                context.contentResolver.query(
                    it,
                    projection,
                    selection,
                    selectionArgs,
                    null
                )
            }
            if (cursor != null && cursor.moveToFirst()) {
                val columnIndex = cursor.getColumnIndexOrThrow(column)
                return cursor.getString(columnIndex)
            }
        } catch (e: Exception) {
            Log.e("FileUtils", "Error getting data column", e)
        } finally {
            cursor?.close()
        }
        return null
    }

    private fun isExternalStorageDocument(uri: Uri): Boolean {
        return "com.android.externalstorage.documents" == uri.authority
    }

    private fun isDownloadsDocument(uri: Uri): Boolean {
        return "com.android.providers.downloads.documents" == uri.authority
    }

    private fun isMediaDocument(uri: Uri): Boolean {
        return "com.android.providers.media.documents" == uri.authority
    }
}