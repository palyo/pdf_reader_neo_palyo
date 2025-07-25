package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.viewmodel

import android.app.*
import androidx.lifecycle.*
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.ext.*
import kotlinx.coroutines.*
import java.io.*

class DocumentViewModel private constructor(private val application: Application) : AndroidViewModel(application) {

    private val _allDocuments = MutableLiveData<MutableList<File>>(mutableListOf())
    private val filteredDocumentsMap = mutableMapOf<DocumentType, MutableLiveData<MutableList<File>>>()

    private val _sortBy = MutableLiveData<SortBy>(SortBy.NAME)
    private val _sortOrder = MutableLiveData<SortOrder>(SortOrder.ASCENDING)

    val sortBy: LiveData<SortBy> = _sortBy
    val sortOrder: LiveData<SortOrder> = _sortOrder

    init {
        DocumentType.entries.forEach { type ->
            filteredDocumentsMap[type] = MutableLiveData(mutableListOf())
        }
    }

    fun getFilteredDocuments(type: DocumentType): LiveData<MutableList<File>> {
        return filteredDocumentsMap[type] ?: MutableLiveData(mutableListOf())
    }

    fun setSorting(sortBy: SortBy, sortOrder: SortOrder) {
        _sortBy.value = sortBy
        _sortOrder.value = sortOrder
        DocumentType.entries.forEach { filterDocuments(it) }
    }

    fun loadPreload() {
        if (_allDocuments.value?.isNotEmpty() == true) {
            DocumentType.entries.forEach { filterDocuments(it) }
            return
        }
        viewModelScope.launch(Dispatchers.IO) {
            val files = application.fetchDocument()
            if (files.isNotEmpty()) {
                _allDocuments.postValue(files)
                withContext(Dispatchers.Main) {
                    DocumentType.entries.forEach { filterDocuments(it) }
                }
            }
        }
    }

    private fun filterDocuments(type: DocumentType) {
        val allDocs = _allDocuments.value ?: return

        val filteredList = when (type) {
            DocumentType.ALL -> allDocs
            DocumentType.DOC -> allDocs.filter { it.extension.equals("doc", true) || it.extension.equals("docx", true) }
            DocumentType.XLS -> allDocs.filter { it.extension.equals("xls", true) || it.extension.equals("xlsx", true) }
            DocumentType.PDF -> allDocs.filter { it.extension.equals("pdf", true) }
            DocumentType.PPT -> allDocs.filter { it.extension.equals("ppt", true) || it.extension.equals("pptx", true) }
        }.toMutableList()

        sortDocuments(filteredList)
        filteredDocumentsMap[type]?.postValue(filteredList)
    }

    private fun sortDocuments(list: MutableList<File>) {
        val sortBy = _sortBy.value ?: SortBy.NAME
        val sortOrder = _sortOrder.value ?: SortOrder.ASCENDING

        list.sortWith(Comparator { file1, file2 ->
            val result = when (sortBy) {
                SortBy.NAME -> file1.name.compareTo(file2.name, ignoreCase = true)
                SortBy.DATE_MODIFIED -> file1.lastModified().compareTo(file2.lastModified())
                SortBy.SIZE -> file1.length().compareTo(file2.length())
            }
            if (sortOrder == SortOrder.ASCENDING) result else -result
        })
    }

    fun deleteFromCurrentList(mediaList: MutableList<File>) {
        viewModelScope.launch(Dispatchers.IO) {
            val updatedList = _allDocuments.value?.filter { it !in mediaList }?.toMutableList()
            updatedList?.let {
                _allDocuments.postValue(it)
                DocumentType.entries.forEach { type -> filterDocuments(type) }
            }
        }
    }

    fun insertToCurrentList(mediaList: MutableList<File>) {
        viewModelScope.launch(Dispatchers.IO) {
            val updatedList = _allDocuments.value ?: mutableListOf()
            updatedList.addAll(mediaList)
            _allDocuments.postValue(updatedList)
            DocumentType.entries.forEach { type -> filterDocuments(type) }
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: DocumentViewModel? = null

        fun getInstance(application: Application): DocumentViewModel {
            return INSTANCE ?: synchronized(this) {
                val instance = DocumentViewModel(application)
                INSTANCE = instance
                instance
            }
        }
    }
}

enum class SortBy {
    NAME, DATE_MODIFIED, SIZE
}

enum class SortOrder {
    ASCENDING, DESCENDING
}


