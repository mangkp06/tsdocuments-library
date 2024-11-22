package ts.tally.document_scanner.fallback.utils

import android.net.Uri
import android.os.Environment
import android.util.Log
import android.webkit.MimeTypeMap
import androidx.activity.ComponentActivity
import androidx.core.text.htmlEncode
import java.io.File
import java.io.IOException
import java.net.URLEncoder
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


/**
 * This class contains a helper function creating temporary files
 *
 * @constructor creates file util
 */
class FileUtil {
    /**
     * create a temporary file
     *
     * @param activity the current activity
     * @param pageNumber the current document page number
     */
    @Throws(IOException::class)
    fun createImageFile(activity: ComponentActivity, pageNumber: Int): File {
        // use current time to make file name more unique
        val dateTime: String = SimpleDateFormat(
            "yyyyMMdd_HHmmss",
            Locale.US
        ).format(Date())

        // create file in pictures directory
        val storageDir: File? = activity.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "IMG_${dateTime}",
            ".jpg",
            storageDir
        )
    }

    // ORIGINAL TO BE FIXED
//    fun getMimeType(url: String?): String? {
//        var type: String? = null
//        val extension = url.let {
//            val encodedPath = URLEncoder.encode(it, "UTF-8")
//            val fileUri = Uri.parse("file://$encodedPath")
//            MimeTypeMap.getFileExtensionFromUrl(fileUri.toString())
//        }
//
//        Log.e("FROM ANDROID extension", extension);
//        if (extension != null) {
//            val sanitizedExtension = extension.replace("[^a-zA-Z0-9]".toRegex(), "")
//            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(sanitizedExtension)
//        }
//        Log.e("FROM ANDROID type", type!!);
//
//        return type
//    }

    fun getMimeType(url: String?): String {
        var type: String? = null
        val extension = url.let {
            val encodedPath = URLEncoder.encode(it, "UTF-8")
            val fileUri = Uri.parse("file://$encodedPath")
            File(fileUri.toString()).extension
        }
        Log.e("FROM ANDROID extension", extension);
        type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension)
        Log.e("FROM ANDROID type", type!!);

        return type
    }
}