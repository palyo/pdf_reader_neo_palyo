package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.utils.viewer.model

import android.os.Parcel
import android.os.Parcelable

open class PdfAnnotationModel(var type: Type, var paginationPageIndex: Int) : Parcelable {
    open var charDrawSegments = arrayListOf<CharDrawSegments>()

    constructor(parcel: Parcel) : this(
        Type.valueOf(parcel.readString() ?: Type.Underline.name),
        parcel.readInt(),
    )

    enum class Type {
        Underline, Highlight, StrikeThrough, Signature
    }

    fun asUnderline(): UnderlineModel? {
        if (this is UnderlineModel) {
            return this
        }
        return null
    }

    fun asStrikeThrough(): StrikeThroughModel? {
        if (this is StrikeThroughModel) {
            return this
        }
        return null
    }

    fun asHighlight(): HighlightModel? {
        if (this is HighlightModel) {
            return this
        }
        return null
    }

    fun asSignature(): SignatureModel? {
        if (this is SignatureModel) {
            return this
        }
        return null
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(type.name)
        parcel.writeInt(paginationPageIndex)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<PdfAnnotationModel> {
        override fun createFromParcel(parcel: Parcel): PdfAnnotationModel {
            return PdfAnnotationModel(parcel)
        }

        override fun newArray(size: Int): Array<PdfAnnotationModel?> {
            return arrayOfNulls(size)
        }
    }
}
