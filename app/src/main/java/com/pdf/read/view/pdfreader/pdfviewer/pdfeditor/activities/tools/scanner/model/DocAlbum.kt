package documentreader.pdfviewer.filereader.documenttools.model

import android.os.*
import kotlinx.parcelize.*

@Parcelize
data class DocAlbum(var albumPath: String, val thumb: String, val count: Int? = 0, val size: Long? = 0L) : Parcelable