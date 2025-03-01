package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.viewmodel

import androidx.lifecycle.*
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.database.dao.*
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.database.table.*

class RecentViewModel(private val recentDao: RecentDao) : ViewModel() {
    private val _recent = MediatorLiveData<MutableList<Recent>>()
    val recent: LiveData<MutableList<Recent>> get() = _recent

    init {
        observeDatabase()
    }

    private fun observeDatabase() {
        _recent.addSource(recentDao.fetchAll()) { recent ->
            val oldRecent = _recent.value
            if (oldRecent == null || !areRecentListsEqual(oldRecent, recent)) {
                _recent.value = recent.toMutableList()
            }
        }
    }

    private fun areRecentListsEqual(oldList: List<Recent>, newList: List<Recent>): Boolean {
        if (oldList.size != newList.size) return false
        return oldList.zip(newList).all { (old, new) ->
            old == new
        }
    }
}

class RecentViewModelFactory(private val recentDao: RecentDao) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RecentViewModel::class.java)) {
            return RecentViewModel(recentDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
