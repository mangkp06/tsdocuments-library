package ts.tally.document_scanner.fallback.models

import android.net.Uri

/**
 * This class contains the original document photo, and a cropper. The user can drag the corners
 * to make adjustments to the detected corners.
 *
 * @param originalPhotoFilePath the photo file path before cropping
 * @param originalPhotoWidth the original photo width
 * @param originalPhotoHeight the original photo height
 * @param corners the document's 4 corner points
 * @constructor creates a document
 */
class Document(
    var originalPhotoFilePath: String,
    var croppedPhotoUri: String?,
    var originalPhotoWidth: Int,
    var originalPhotoHeight: Int,
    var corners: Quad
) {
}