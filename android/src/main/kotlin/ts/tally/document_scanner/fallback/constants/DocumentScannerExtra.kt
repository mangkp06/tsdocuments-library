package ts.tally.document_scanner.fallback.constants

/**
 * This class contains constants meant to be used as intent extras
 */
class DocumentScannerExtra {
    companion object {
        const val EXTRA_CROPPED_IMAGE_QUALITY = "croppedImageQuality"
        const val EXTRA_MAX_NUM_DOCUMENTS = "maxNumDocuments"
        const val EXTRA_IMPORT_FILEPATH = "importFilePath"
    }
}

class DocumentScannerAction {
    companion object {
        const val NONE = "NONE"
        const val RETAKE = "RETAKE"
        const val RESELECT = "RESELECT"
        const val DELETE = "DELETE"
        const val ADD = "ADD"
        const val EDIT = "EDIT"
    }
}