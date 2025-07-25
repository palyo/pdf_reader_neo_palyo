package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.activities.picker

import android.content.Intent
import androidx.activity.*
import androidx.lifecycle.*
import androidx.recyclerview.widget.*
import coder.apps.space.library.base.*
import coder.apps.space.library.extension.*
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.databinding.*
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.ext.*
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.R
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.adapter.*
import kotlinx.coroutines.*

class SelectFileActivity : BaseActivity<ActivitySelectFileBinding>(ActivitySelectFileBinding::inflate) {

    private var selectPdfAdapter: SelectPDFAdapter? = null

    override fun ActivitySelectFileBinding.initExtra() {
        initAdapter()
    }

    private fun ActivitySelectFileBinding.initAdapter() {
        recyclerView.apply {
            layoutManager = LinearLayoutManager(this@SelectFileActivity, RecyclerView.VERTICAL, false)
        }

        selectPdfAdapter = SelectPDFAdapter(isMultiple = isMultiple()) { document, position ->
            setResult(RESULT_OK, Intent().apply {
                putExtra("FOR", intent.getStringExtra("FROM"))
                putExtra(SELECTED_FILE, document.absolutePath)
            })
            finish()
        }
        recyclerView.adapter = selectPdfAdapter
        if (intent?.getStringArrayListExtra("selected")?.isNotEmpty() == true){
            selectPdfAdapter?.selectedDocuments = intent?.getStringArrayListExtra("selected") as MutableList<String>
        }
    }

    override fun onResume() {
        super.onResume()
        if (selectPdfAdapter?.itemCount == 0) binding?.initData()
    }

    private fun ActivitySelectFileBinding.initData() {
        lifecycleScope.launch {
            progressHorizontal.beVisible()
            delay(100)
            fetchDocument(load = {
                lifecycleScope.launch { selectPdfAdapter?.add(it) }
            }, complete = {
                lifecycleScope.launch {
                    progressHorizontal.beGone()
                    layoutEmpty.beVisibleIf(selectPdfAdapter?.itemCount == 0)
                }
            }, type = intent?.getStringExtra("type") ?: ALL_FILE)
        }
    }

    override fun ActivitySelectFileBinding.initListeners() {
        toolbar.menu.findItem(R.id.action_done).isVisible = isMultiple()
        toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.action_done -> {
                    selectPdfAdapter?.apply {
                        if (selectedDocuments.size >= 2) {
                            setResult(RESULT_OK, Intent().apply {
                                putExtra("FOR", intent.getStringExtra("FROM"))
                                putStringArrayListExtra(
                                    SELECTED_FILE,
                                    selectedDocuments as ArrayList<String>?
                                )
                            })
                            finish()
                        } else {
                            showToast("Please select at least 2 files")
                        }
                    }
                }
            }
            return@setOnMenuItemClickListener false
        }
    }

    override fun ActivitySelectFileBinding.initView() {
        toolbar.title = getCurrentTitle()
        toolbar.setNavigationOnClickListener { onBackPressedDispatcher.onBackPressed() }
        onBackPressedDispatcher.addCallback(this@SelectFileActivity, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                setResult(RESULT_CANCELED)
                finish()
            }
        })
    }

    private fun isMultiple(): Boolean {
        return when (intent.getStringExtra("FROM")) {
            MERGE -> true
            SPLIT, COMPRESS, EXTRACT_PAGE, REMOVE_PAGE, EXCEL_PICK, WORD_PICK, TEXT_PICK, PPT_PICK -> false
            else -> false
        }
    }

    private fun getCurrentTitle(): String {
        val from = intent.getStringExtra("FROM")
        return when (from) {
            WORD_PICK -> "Select Word File"
            EXCEL_PICK -> "Select Excel File"
            PPT_PICK -> "Select PPT File"
            TEXT_PICK -> "Select Text File"
            else -> if (isMultiple()) "Select PDFs" else "Select PDF"
        }
    }
}