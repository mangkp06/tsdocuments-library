package ts.tally.document_scanner.fallback.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

/**
 * This class contains a helper function for opening the camera.
 *
 * @param activity current activity
 * @param onPhotoCaptureSuccess gets called with photo file path when photo is ready
 * @param onCancelPhoto gets called when user cancels out of camera
 * @constructor creates camera util
 */
class DocumentProviderUtil(
    private val activity: ComponentActivity,
    private val onDocumentSelectSuccess: (filePath: String?) -> Unit,
    private val onCancelDocumentSelect: () -> Unit
) {
    /**
     * @property documentPath the photo file path
     */
    private lateinit var documentPath: String
    private val REQUEST_CODE_OPEN_DOCUMENT = 1001
    private val documentUtil = DocumentUtil()

    /**
     * @property startForResult used to launch the document provider
     */
    private val startForResult = activity.registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result: ActivityResult ->
        when (result.resultCode) {
            Activity.RESULT_OK -> {
                result.data?.data?.also { uri ->
                    Log.e("URI",uri.path!!);
                    val mimeType = activity.contentResolver.getType(uri)
                   val cachedFile = copyFileToCache(activity.baseContext, uri)
                   val filePath = cachedFile?.absolutePath

                    // val filePaths = documentUtil.getPathFromUri(activity.applicationContext, uri)
                   Log.e("URI PATH 1: ", filePath!!)
                    // Log.e("PATH 2: ", filePaths!!)
                    // send back photo file path on capture success
                    onDocumentSelectSuccess(filePath)
                }
            }
            Activity.RESULT_CANCELED -> {
                // delete the photo since the user didn't finish taking the photo
                File(documentPath).delete()
                onCancelDocumentSelect()
            }
        }
    }

    @SuppressLint("Range")
    private fun getFileNameFromUri(contentResolver: ContentResolver, uri: Uri): String? {
        var name: String? = null
        val cursor = contentResolver.query(uri, null, null, null, null)
        cursor?.use {
            if (it.moveToFirst()) {
                name = it.getString(it.getColumnIndex(OpenableColumns.DISPLAY_NAME))
            }
        }
        return name
    }

    private fun copyFileToCache(context: Context?, uri: Uri): File? {
        val cacheDir = context?.cacheDir  // Get the cache directory
        val fileName = getFileNameFromUri(context?.contentResolver!!, uri) ?: return null

        // Create a new file in the cache directory with the same name
        val tempFile = File(cacheDir, fileName)

        return try {
            // Open an InputStream from the Uri
            val inputStream = context.contentResolver?.openInputStream(uri) ?: return null

            // Create an OutputStream to write to the cache file
            val outputStream = FileOutputStream(tempFile)

            // Copy the file content to the cache file
            inputStream.use { input ->
                outputStream.use { output ->
                    input.copyTo(output)
                }
            }
            tempFile  // Return the temp file
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    /**
     * open the camera by launching an image capture intent
     *
     * @param pageNumber the current document page number
     */
    @Throws(IOException::class)
    fun openDocumentProvider(pageNumber: Int) {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            // Set the type to allow only images and PDFs
            type = "*/*"
            // Specify the MIME types for image and PDF
            putExtra(Intent.EXTRA_MIME_TYPES, arrayOf("image/*", "application/pdf"))
            // Allow the user to select content that can be opened
            addCategory(Intent.CATEGORY_OPENABLE)
        }
        // open document provider
        startForResult.launch(intent)
    }
}