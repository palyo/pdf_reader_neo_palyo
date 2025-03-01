package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.ext

import android.content.*
import android.database.*
import android.net.*
import android.os.*
import android.provider.*
import androidx.documentfile.provider.*
import coder.apps.space.library.extension.*
import java.io.*
import java.text.*
import java.util.*

fun Context.realPath(uri: Uri?): String? {
    var realPath: String? = null
    val documentFile = uri?.let { DocumentFile.fromSingleUri(this, it) }
    if (documentFile != null && documentFile.exists()) {
        realPath = documentFile.uri.path
    }
    return realPath
}

fun getPathFromURI(context: Context, uri: Uri?): String? {
    if (uri == null) {
        return null
    }

    // Check if the URI is a document URI (from file picker or external storage)
    if (DocumentsContract.isDocumentUri(context, uri)) {
        // Handle external storage documents (e.g., SD card)
        if (isExternalStorageDocument(uri)) {
            val docId = DocumentsContract.getDocumentId(uri)
            val split = docId.split(":")
            if (split.size >= 2) {
                val type = split[0]
                if ("primary".equals(type, ignoreCase = true)) {
                    return Environment.getExternalStorageDirectory().toString() + "/" + split[1]
                }
            }
        }
        // Handle downloads documents (e.g., files from Downloads folder)
        else if (isDownloadsDocument(uri)) {
            val id = DocumentsContract.getDocumentId(uri)
            val contentUri = ContentUris.withAppendedId(
                Uri.parse("content://downloads/public_downloads"),
                id.toLongOrNull() ?: return null
            )
            return getDataColumn(context, contentUri, null, null)
        }
        // Handle media documents (e.g., images, audio, video)
        else if (isMediaDocument(uri)) {
            val docId = DocumentsContract.getDocumentId(uri)
            val split = docId.split(":")
            if (split.size >= 2) {
                val type = split[0]
                val contentUri = when (type) {
                    "image" -> MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                    "video" -> MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                    "audio" -> MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                    else -> MediaStore.Files.getContentUri("external")
                }
                val selection = "_id=?"
                val selectionArgs = arrayOf(split[1])
                return getDataColumn(context, contentUri, selection, selectionArgs)
            }
        }
    }
    // Handle content scheme URIs (e.g., media content)
    else if ("content".equals(uri.scheme, ignoreCase = true)) {
        return getDataColumn(context, uri, null, null)
    }
    // Handle file scheme URIs (e.g., direct file paths)
    else if ("file".equals(uri.scheme, ignoreCase = true)) {
        return uri.path
    }
    return null
}

fun getDataColumn(context: Context, uri: Uri?, selection: String?, selectionArgs: Array<String>?): String? {
    var cursor: Cursor? = null
    val column = "_data"
    val projection = arrayOf(column)

    try {
        cursor = context.contentResolver.query(uri!!, projection, selection, selectionArgs, null)
        if (cursor != null && cursor.moveToFirst()) {
            val columnIndex = cursor.getColumnIndex(column)
            if (columnIndex != -1) {
                return cursor.getString(columnIndex)
            } else {
                "getDataColumn".log("Column '$column' not found in cursor.")
            }
        }
    } catch (e: Exception) {
        "getDataColumn".log("Column 'Error querying data column $e")
    } finally {
        cursor?.close()
    }

    return null
}


fun isExternalStorageDocument(uri: Uri?): Boolean {
    return "com.android.externalstorage.documents" == uri?.authority
}

fun isDownloadsDocument(uri: Uri?): Boolean {
    return "com.android.providers.downloads.documents" == uri?.authority
}

fun isMediaDocument(uri: Uri?): Boolean {
    return "com.android.providers.media.documents" == uri?.authority
}