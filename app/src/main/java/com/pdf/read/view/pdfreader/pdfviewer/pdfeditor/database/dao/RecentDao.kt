package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.database.dao

import androidx.lifecycle.*
import androidx.room.*
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.database.table.*

@Dao
interface RecentDao {
    @Query("SELECT * FROM recent ORDER BY timestamp DESC")
    fun fetchAll(): LiveData<MutableList<Recent>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(recent: Recent)

    @Update
    suspend fun update(recent: Recent)

    @Delete
    suspend fun delete(recent: Recent)
}