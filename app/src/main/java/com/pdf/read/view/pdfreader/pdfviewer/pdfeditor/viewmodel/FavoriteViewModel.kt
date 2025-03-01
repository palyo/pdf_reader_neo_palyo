package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.viewmodel

import androidx.lifecycle.*
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.database.dao.*
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.database.table.*

class FavoriteViewModel(private val favoriteDao: FavoriteDao) : ViewModel() {
    private val _favorites = MediatorLiveData<MutableList<Favorite>>()
    val favorites: LiveData<MutableList<Favorite>> get() = _favorites

    init {
        observeDatabase()
    }

    private fun observeDatabase() {
        _favorites.addSource(favoriteDao.fetchAll()) { favorite ->
            val favorites = _favorites.value
            if (favorites == null || !areListsEqual(favorites, favorite)) {
                _favorites.value = favorite.toMutableList()
            }
        }
    }

    private fun areListsEqual(oldList: List<Favorite>, newList: List<Favorite>): Boolean {
        if (oldList.size != newList.size) return false
        return oldList.zip(newList).all { (old, new) ->
            old == new
        }
    }
}

class FavoriteViewModelFactory(private val favoriteDao: FavoriteDao) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FavoriteViewModel::class.java)) {
            return FavoriteViewModel(favoriteDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
