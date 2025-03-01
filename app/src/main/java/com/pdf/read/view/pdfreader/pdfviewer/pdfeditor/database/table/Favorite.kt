package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.database.table

import android.os.*
import androidx.room.*
import kotlinx.parcelize.*

@Parcelize
@Entity(tableName = "favorite", indices = [Index(value = ["file_path"], unique = true)])
data class Favorite(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Long? = null,
    @ColumnInfo(name = "full_name")
    val fullName: String = "",
    @ColumnInfo(name = "file_path")
    val filePath: String = "",
    @ColumnInfo(name = "timestamp")
    val timestamp: Long? = System.currentTimeMillis(),
) : Parcelable