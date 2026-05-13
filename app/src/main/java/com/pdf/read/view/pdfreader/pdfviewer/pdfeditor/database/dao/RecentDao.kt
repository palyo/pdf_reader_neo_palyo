package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.database.dao

import androidx.lifecycle.*
import androidx.room.*
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.database.table.*

@Dao
interface RecentDao {
    @Query("SELECT * FROM recent ORDER BY timestamp DESC")
    fun fetchAll(): LiveData<MutableList<Recent>>

    /**
     * Synchronous top-N query used by the after-call screen to populate
     * the default tool grid with the user's most recent PDFs. The list
     * needs to be available before the fragment's view is created, so a
     * blocking variant is simpler than wiring LiveData through the
     * AfterCall module's config object.
     */
    @Query("SELECT * FROM recent ORDER BY timestamp DESC LIMIT :limit")
    fun fetchRecent(limit: Int): List<Recent>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(recent: Recent)

    @Update
    suspend fun update(recent: Recent)

    @Delete
    suspend fun delete(recent: Recent)
}