package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.database.dao

import androidx.lifecycle.*
import androidx.room.*
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.database.table.*

@Dao
interface FavoriteDao {
    @Query("SELECT * FROM favorite ORDER BY timestamp DESC")
    fun fetchAll(): LiveData<MutableList<Favorite>>

    @Query("SELECT * FROM favorite WHERE file_path = :path LIMIT 1")
    fun isFavorite(path: String): Favorite?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(recent: Favorite)

    @Update
    suspend fun update(recent: Favorite)

    @Delete
    suspend fun delete(recent: Favorite)
}