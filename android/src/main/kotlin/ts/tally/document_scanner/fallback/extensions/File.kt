package ts.tally.document_scanner.fallback.extensions

import android.graphics.Bitmap
import java.io.File

fun File.write(
    bitmap: Bitmap, format: Bitmap.CompressFormat = Bitmap.CompressFormat.JPEG, quality: Int = 80
) = apply {
    outputStream().use {
        bitmap.compress(format, quality, it)
        it.flush()
    }
}