package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.viewmodel

import android.app.*
import androidx.lifecycle.*
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.ext.*
import kotlinx.coroutines.*
import java.io.*

class DocumentViewModel private constructor(private val application: Application) : AndroidViewModel(application) {
    private val _allDocuments = MutableLiveData<MutableList<File>>(mutableListOf())
    private val filteredDocumentsMap = mutableMapOf<DocumentType, MutableLiveData<MutableList<File>>>()

    init {
        DocumentType.entries.forEach { type ->
            filteredDocumentsMap[type] = MutableLiveData(mutableListOf())
        }
    }

    fun getFilteredDocuments(type: DocumentType): LiveData<MutableList<File>> {
        return filteredDocumentsMap[type] ?: MutableLiveData(mutableListOf())
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
        filteredDocumentsMap[type]?.postValue(filteredList)
        if (filteredList.isEmpty()) {
            filteredDocumentsMap[type]?.postValue(mutableListOf())
        }
    }

    fun deleteFromCurrentList(mediaList: MutableList<File>) {
        viewModelScope.launch(Dispatchers.IO) {
            val updatedList = _allDocuments.value?.filter { it !in mediaList }?.toMutableList()
            updatedList?.let {
                _allDocuments.postValue(it)
                DocumentType.values().forEach { type -> filterDocuments(type) }
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

