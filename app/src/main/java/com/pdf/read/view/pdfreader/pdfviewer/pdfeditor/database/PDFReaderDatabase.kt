package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.database

import android.content.*
import androidx.room.*
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.database.dao.*
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.database.table.*

@Database(entities = [Recent::class, Favorite::class], version = 1)
abstract class PDFReaderDatabase : RoomDatabase() {
    abstract fun recentDao(): RecentDao
    abstract fun favoriteLikeDao(): FavoriteLikeDao

    companion object {
        @Volatile
        private var INSTANCE: PDFReaderDatabase? = null
        fun getDatabase(context: Context): PDFReaderDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    PDFReaderDatabase::class.java,
                    "pdf_reader_database.db"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}