package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.activities.tools.converter

import android.net.*
import android.os.*
import androidx.activity.*
import androidx.activity.result.*
import androidx.activity.result.contract.*
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.*
import coder.apps.space.library.base.*
import coder.apps.space.library.extension.*
import com.google.android.material.progressindicator.*
import com.google.android.material.textview.*
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.adapter.*
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.databinding.*
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.ext.*
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.R
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.activities.viewer.*
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.admodule.viewNativeSmall
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.viewmodel.DocumentViewModel
import kotlinx.coroutines.*
import java.io.*
import kotlin.text.isNullOrEmpty

class ImageToPdfActivity : BaseActivity<ActivityImageToPdfBinding>(ActivityImageToPdfBinding::inflate) {

    private var documentViewModel: DocumentViewModel? = null
    private var optImageAdapter: OptImageAdapter? = null
    private var captures: MutableList<String> = mutableListOf()
    private var galleryPickerImage: ActivityResultLauncher<String>? = registerForActivityResult(
        ActivityResultContracts.GetMultipleContents()
    ) {
        it.forEach {
            val path = getPathFromURI(this@ImageToPdfActivity, it)
            captures.add(0, path.toString())
        }
        if (captures.isEmpty())  {
            binding?.resetUI()
            return@registerForActivityResult
        }
        binding?.apply {
            recyclerViewSelected.beVisible()
            buttonCancel.enable()
            buttonSave.enable()
            actionSelectImages.disable()
        }
        optImageAdapter?.updateList(captures)
    }

    override fun ActivityImageToPdfBinding.initExtra() {
        documentViewModel = DocumentViewModel.getInstance(application)
        setupAdapter()
        viewNativeSmall(adNative)
    }

    private fun ActivityImageToPdfBinding.setupAdapter() {
        recyclerViewSelected.apply {
            layoutManager = LinearLayoutManager(this@ImageToPdfActivity, RecyclerView.VERTICAL, false)
            optImageAdapter = OptImageAdapter { file, position ->

            }
            adapter = optImageAdapter
        }
    }

    override fun ActivityImageToPdfBinding.initListeners() {
        buttonCancel.disable()
        buttonSave.disable()
        actionSelectImages.setOnClickListener {
            galleryPickerImage?.launch("image/*")
        }
        buttonCancel.setOnClickListener {
            resetUI()
        }
        buttonSave.setOnClickListener {
            viewCreateDialog {
                actionSave(
                    it, files = optImageAdapter?.getSources(), start = {
                    buttonSave.disable()
                    buttonCancel.disable()
                    actionSelectImages.disable()
                }, complete = {
                    if (it.isEmpty()) {
                        return@actionSave
                    }
                    resetUI()
                    val file = File(it)
                    val uri: Uri = FileProvider.getUriForFile(
                        this@ImageToPdfActivity, "${packageName}.provider", file
                    )
                    documentViewModel?.insertToCurrentList(mutableListOf(file))
                    go(
                        PDFReaderActivity::class.java,
                        listOf(FILE_URI to uri, FILE_PATH to file.path, FILE_NAME to file.name),
                        finish = true
                    )
                },
                    error = {
                        resetUI()
                    })
            }
        }
    }

    private fun actionSave(
        name: String?,
        files: MutableList<String>?,
        start: (() -> Unit)? = null,
        complete: ((String) -> Unit)? = null,
        error: ((String) -> Unit)? = null,
    ) {
        val dialog = showProcessingDialog()
        val dialogTitle = dialog.findViewById<MaterialTextView>(R.id.dialog_title)
        val progressBar = dialog.findViewById<LinearProgressIndicator>(R.id.progress_horizontal)
        progressBar?.isIndeterminate = true
        dialogTitle?.text = getString(R.string.progress_label_converting_pdf)
        if (!isFinishing || !isDestroyed) dialog.show()
        val startTime = System.currentTimeMillis()
        CoroutineScope(Dispatchers.IO).launch {
            withContext(Dispatchers.Main) {
                start?.invoke()
            }
            val root = File(defaultStorageLocation, "Image to PDF")
            createOrExistsDir(root)
            val outputFile = File(root, "$name.pdf")
            createOrExistsFile(outputFile)
            try {
                if (files != null) {
                    saveImagesToPdf(imagePaths = files, destinationDir = root, fileName = "$name", complete = {
                        runOnUiThread {
                            if (System.currentTimeMillis() > (startTime + 5000)) {
                                Handler(mainLooper).postDelayed({
                                    if (!isFinishing) dialog.dismiss()
                                    complete?.invoke(outputFile.absolutePath)
                                }, 1000)
                            } else {
                                Handler(mainLooper).postDelayed({
                                    if (!isFinishing) dialog.dismiss()
                                    complete?.invoke(outputFile.absolutePath)
                                }, 3000)
                            }
                        }
                    })
                }
            } catch (e: Exception) {
                outputFile.delete()
                e.localizedMessage?.let { "TAG".log(it) }
                withContext(Dispatchers.Main) {
                    if (!isFinishing) dialog.dismiss()
                    error?.invoke("")
                }
            }
        }
    }

    private fun ActivityImageToPdfBinding.resetUI() {
        recyclerViewSelected.beGone()
        buttonSave.disable()
        buttonCancel.disable()
        actionSelectImages.enable()
        captures = mutableListOf()
        optImageAdapter?.clear()
    }

    override fun ActivityImageToPdfBinding.initView() {
        toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
        onBackPressedDispatcher.addCallback {
            finish()
        }
    }
}