package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.fragments

import android.os.*
import android.view.*
import android.view.animation.*
import android.view.inputmethod.*
import androidx.core.widget.*
import androidx.recyclerview.widget.*
import coder.apps.space.library.base.*
import coder.apps.space.library.extension.*
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.adapter.*
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.databinding.*
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.ext.*
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.viewmodel.*
import jp.wasabeef.recyclerview.animators.*
import java.io.*

class FindFiles : BaseDialog<DialogFindFilesBinding>(DialogFindFilesBinding::inflate) {
    private var originalFiles = mutableListOf<File>()
    private var searchAdapter: SearchAdapter? = null
    private var documentViewModel: DocumentViewModel? = null
    private var files = mutableListOf<File>()

    override fun create() {
        activity?.apply context@{
            documentViewModel = DocumentViewModel.getInstance(application)
        }
    }

    private fun handleBackPress() {
        dismiss()
    }

    override fun DialogFindFilesBinding.viewCreated() {
        setupAdapter()
        fetchData()
        setupData()
        dialog?.setOnKeyListener { _, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_BACK && event.action == KeyEvent.ACTION_UP) {
                handleBackPress()
                true
            } else {
                false
            }
        }

        buttonClose.setOnClickListener { handleBackPress() }
        editText.doOnTextChanged { text, start, before, count ->
            searchFilesByName(text.toString())
        }
        editText.setOnEditorActionListener { _, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE ||
                (event != null && event.keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_DOWN)
            ) {
                val inputText = editText.text.toString()
                searchFilesByName(inputText)
                true
            } else {
                false
            }
        }
    }

    private fun fetchData() {
        activity?.apply context@{
            documentViewModel?.getFilteredDocuments(DocumentType.ALL)?.observe(this@context) { documents ->
                runOnUiThread {
                    originalFiles = documents
                }
            }
        }
    }

    private fun searchFilesByName(text: String) {
        activity?.apply context@{
            files = originalFiles.filter { it.name.contains(text) }.toMutableList()
            binding?.setupData()
        }
    }

    private fun DialogFindFilesBinding.setupData() {
        progressHorizontal.beGone()
        if (files.isNotEmpty()) {
            searchAdapter?.addAll(files = files)
            layoutEmpty.beGone()
            recyclerView.beVisible()
        } else {
            layoutEmpty.beVisible()
            recyclerView.beGone()
        }
    }

    private fun DialogFindFilesBinding.setupAdapter() {
        activity?.apply context@{
            recyclerView.apply {
                itemAnimator = SlideInUpAnimator(OvershootInterpolator(1f))
                layoutManager = LinearLayoutManager(this@context, RecyclerView.VERTICAL, false)
                searchAdapter = SearchAdapter(context = context, click = { file: File, position: Int ->
                    listener?.invoke(file)
                    delayed(500L) {
                        handleBackPress()
                    }
                })
                adapter = searchAdapter
            }
        }
    }

    override fun DialogFindFilesBinding.initView() {
        toolbar.setNavigationOnClickListener {
            handleBackPress()
        }
    }

    override fun DialogFindFilesBinding.initListeners() {}

    companion object {
        const val TAG = "DirectoryPicker"
        private var listener: ((file: File) -> Unit)? = null
        fun newInstance(callback: ((file: File) -> Unit)? = null): FindFiles {
            listener = callback
            return FindFiles().apply {
                arguments = Bundle().apply {
                }
            }
        }
    }
}