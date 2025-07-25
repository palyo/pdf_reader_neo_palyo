package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.activities.tools.converter

import android.app.*
import android.content.*
import android.media.*
import android.net.*
import android.os.*
import androidx.activity.*
import androidx.activity.result.contract.*
import androidx.core.content.*
import androidx.recyclerview.widget.*
import coder.apps.space.library.base.*
import coder.apps.space.library.extension.*
import com.aspose.words.*
import com.google.android.material.progressindicator.*
import com.google.android.material.textview.*
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.adapter.*
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.databinding.*
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.ext.*
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.R
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.activities.picker.*
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.activities.viewer.*
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.admodule.viewNativeSmall
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.viewmodel.DocumentViewModel
import kotlinx.coroutines.*
import java.io.*

class WordToPdfActivity : BaseActivity<ActivityWordToPdfActivityBinding>(ActivityWordToPdfActivityBinding::inflate) {

    private var documentViewModel: DocumentViewModel? = null
    private var selectedFile: File? = null
    private var optPdfAdapter: OptDocAdapter? = null
    private var resultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        when (result.resultCode) {
            RESULT_OK -> {
                val resultData = result.data
                val from = resultData?.getStringExtra("FOR")
                if (from == WORD_PICK) {
                    optPdfAdapter?.clear()
                    val pdf = resultData.getStringExtra(SELECTED_FILE)
                    if (pdf.isNullOrEmpty()) {
                        binding?.resetUI()
                        return@registerForActivityResult
                    }
                    selectedFile = File(pdf.toString())
                    binding?.apply {
                        recyclerViewSelected.beVisible()
                        layoutButtons.beVisible()
                        optPdfAdapter?.addItem(selectedFile?.absolutePath.toString())
                        buttonSave.enable()
                        buttonCancel.enable()
                    }
                } else {
                    binding?.resetUI()
                }
            }

            RESULT_CANCELED -> {
                binding?.resetUI()
            }
        }
    }

    override fun ActivityWordToPdfActivityBinding.initExtra() {
        documentViewModel = DocumentViewModel.getInstance(application)
        setupAdapter()
        viewNativeSmall(adNative)
    }

    private fun ActivityWordToPdfActivityBinding.setupAdapter() {
        recyclerViewSelected.apply {
            scheduleLayoutAnimation()
            layoutManager = LinearLayoutManager(this@WordToPdfActivity, RecyclerView.VERTICAL, false)
            optPdfAdapter = OptDocAdapter { file, position ->
                val uri: Uri = FileProvider.getUriForFile(
                    this@WordToPdfActivity, "${packageName}.provider", file
                )
                go(
                    ReaderActivity::class.java,
                    listOf(FILE_URI to uri, FILE_PATH to file.path, FILE_NAME to file.name)
                )
            }
            adapter = optPdfAdapter
        }
    }

    override fun ActivityWordToPdfActivityBinding.initListeners() {
        buttonCancel.disable()
        buttonSave.disable()
        actionSelect.setOnClickListener {
            selectedFile = null
            Intent(this@WordToPdfActivity, SelectFileActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                putExtra("FROM", WORD_PICK)
                putExtra("type", DOC_FILE)
                resultLauncher.launch(this)
            }
        }
        buttonCancel.setOnClickListener {
            resetUI()
        }

        buttonSave.setOnClickListener {
            viewCreateDialog {
                actionSave(it, file = selectedFile?.absolutePath.toString(), start = {
                    buttonSave.disable()
                    buttonCancel.disable()
                    actionSelect.disable()
                }, complete = {
                    if (it.isEmpty()) {
                        return@actionSave
                    }
                    resetUI()
                    val file = File(it)
                    val uri: Uri = FileProvider.getUriForFile(
                        this@WordToPdfActivity, "${packageName}.provider", file
                    )
                    documentViewModel?.insertToCurrentList(mutableListOf(file))
                    go(
                        PDFReaderActivity::class.java,
                        listOf(FILE_URI to uri, FILE_PATH to file.path, FILE_NAME to file.name),
                        finish = true
                    )
                }, error = {
                    resetUI()
                })
            }
        }
    }

    private fun actionSave(
        name: String?,
        password: String? = "",
        isProtected: Boolean? = false,
        file: String,
        start: (() -> Unit)? = null,
        progress: ((String) -> Unit)? = null,
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
            val uri: Uri = FileProvider.getUriForFile(
                this@WordToPdfActivity, "$packageName.provider", File(file)
            )
            val root = File(defaultStorageLocation, "Word to PDF")
            createOrExistsDir(root)
            val outputFile = File(root, "$name.pdf")
            createOrExistsFile(outputFile)

            try {
                contentResolver.openInputStream(uri).use { inputStream ->
                    val doc = Document(inputStream)
                    doc.fontInfos.embedTrueTypeFonts = true
                    doc.fontInfos.embedSystemFonts = false
                    doc.save(outputFile.absolutePath, SaveFormat.PDF)
                    MediaScannerConnection.scanFile(
                        this@WordToPdfActivity, arrayOf(outputFile.absolutePath), null, null
                    )
                    withContext(Dispatchers.Main) {
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

    private fun ActivityWordToPdfActivityBinding.resetUI() {
        recyclerViewSelected.beGone()
        buttonSave.disable()
        buttonCancel.disable()
        actionSelect.enable()
        selectedFile = null
    }

    override fun ActivityWordToPdfActivityBinding.initView() {
        toolbar.setNavigationOnClickListener { onBackPressedDispatcher.onBackPressed() }
        onBackPressedDispatcher.addCallback { finish() }
    }
}