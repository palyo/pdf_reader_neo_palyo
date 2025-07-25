package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.database

import android.content.*
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.database.dao.*

val Context.recentDao: RecentDao get() = PDFReaderDatabase.getDatabase(applicationContext).recentDao()
val Context.favoriteLikeDao: FavoriteLikeDao get() = PDFReaderDatabase.getDatabase(applicationContext).favoriteLikeDao()

