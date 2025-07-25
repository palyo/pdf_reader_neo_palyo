package documentreader.pdfviewer.filereader.documenttools.model

import android.graphics.*
import android.os.*
import kotlinx.parcelize.*

@Parcelize
data class DocSave(val path: String, var filter: Int, val bitmap: Bitmap? = null) : Parcelable
