package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.activities.tools.modify

import android.app.Activity
import android.content.Intent
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.view.animation.OvershootInterpolator
import androidx.activity.addCallback
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coder.apps.space.library.base.BaseActivity
import coder.apps.space.library.extension.beGone
import coder.apps.space.library.extension.beVisible
import coder.apps.space.library.extension.disable
import coder.apps.space.library.extension.enable
import coder.apps.space.library.extension.go
import coder.apps.space.library.extension.showToast
import com.google.android.material.textview.MaterialTextView
import com.itextpdf.text.Document
import com.itextpdf.text.DocumentException
import com.itextpdf.text.pdf.BadPdfFormatException
import com.itextpdf.text.pdf.PdfCopy
import com.itextpdf.text.pdf.PdfReader
import com.itextpdf.text.pdf.PdfWriter
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.R
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.activities.picker.SelectFileActivity
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.activities.tools.modify.CompressActivity
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.activities.viewer.PDFReaderActivity
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.adapter.OptDocAdapter
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.admodule.viewNativeSmall
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.databinding.ActivitySplitBinding
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.ext.ERROR_INVALID_INPUT
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.ext.ERROR_PAGE_NUMBER
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.ext.ERROR_PAGE_RANGE
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.ext.FILE_NAME
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.ext.FILE_PATH
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.ext.FILE_URI
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.ext.NO_ERROR
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.ext.PDF_FILE
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.ext.SELECTED_FILE
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.ext.SPLIT
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.ext.createOrExistsDir
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.ext.createOrExistsFile
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.ext.defaultStorageLocation
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.ext.isProtected
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.ext.showProcessingDialog
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.ext.viewCreateDialog
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.viewmodel.DocumentViewModel
import jp.wasabeef.recyclerview.animators.FadeInUpAnimator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException

class SplitActivity : BaseActivity<ActivitySplitBinding>(ActivitySplitBinding::inflate) {

    private var documentViewModel: DocumentViewModel? = null
    private var selectedPDF: File? = null
    private var optPdfAdapter: OptDocAdapter? = null
    private var resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        when (result.resultCode) {
            Activity.RESULT_OK -> {
                val resultData = result.data
                val from = resultData?.getStringExtra("FOR")
                if (from == SPLIT) {
                    optPdfAdapter?.clear()
                    val pdf = resultData.getStringExtra(SELECTED_FILE)
                    if (pdf.isNullOrEmpty()) {
                        resetUI()
                        return@registerForActivityResult
                    }
                    selectedPDF = File(pdf.toString())
                    binding?.apply {
                        if (pdf.isProtected() == true) {
                            resetUI()
                            return@registerForActivityResult
                        }
                        val splitConfig = getDefaultSplitConfig(selectedPDF)
                        if (splitConfig?.isNotEmpty() == true) {
                            layoutButtons.beVisible()
                            recyclerViewSelected.beVisible()
                            inputText.beVisible()
                            optPdfAdapter?.addItem(selectedPDF?.absolutePath.toString())
                            if (selectedPDF?.exists() == true) {
                                buttonSave.enable()
                                buttonCancel.enable()
                            }
                            editText.setText(splitConfig)
                        }
                    }
                } else {
                    resetUI()
                }
            }

            Activity.RESULT_CANCELED -> {
                resetUI()
            }
        }
    }

    private fun getDefaultSplitConfig(doc: File?): String? {
        val splitConfig = StringBuilder()
        try {
            val reader = PdfReader(doc?.absolutePath)
            val pageCount = reader.numberOfPages
            for (i in 1..pageCount) {
                splitConfig.append(i)
                if (i < pageCount) splitConfig.append(",")
            }
            reader.close()
        } catch (e: IOException) {
            resetUI()
        }
        return splitConfig.toString()
    }

    fun resetUI() {
        binding?.apply {
            buttonChooseFile.enable()
            recyclerViewSelected.beGone()
            inputText.beGone()
            textNote.beGone()
            buttonSave.disable()
            buttonCancel.disable()
            selectedPDF = null
        }
    }

    override fun ActivitySplitBinding.initExtra() {
        documentViewModel = DocumentViewModel.getInstance(application)
        initAdapter()
        viewNativeSmall(adNative)
    }

    private fun ActivitySplitBinding.initAdapter() {
        recyclerViewSelected.apply {
            itemAnimator = FadeInUpAnimator(OvershootInterpolator(1f))
            scheduleLayoutAnimation()
            layoutManager = LinearLayoutManager(this@SplitActivity, RecyclerView.VERTICAL, false)
            optPdfAdapter = OptDocAdapter { file, position ->
                val uri: Uri = FileProvider.getUriForFile(this@SplitActivity, "${packageName}.provider", file)
                go(PDFReaderActivity::class.java, listOf(FILE_URI to uri, FILE_PATH to file.absolutePath, FILE_NAME to file.name))
            }
            adapter = optPdfAdapter
        }
    }

    override fun ActivitySplitBinding.initListeners() {
        buttonSave.disable()
        buttonCancel.disable()
        buttonChooseFile.setOnClickListener {
            optPdfAdapter?.clear()
            selectedPDF = null
            Intent(this@SplitActivity, SelectFileActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                putExtra("FROM", SPLIT)
                putExtra("type", PDF_FILE)
                resultLauncher.launch(this)
            }
        }
        buttonCancel.setOnClickListener {
            resetUI()
        }
        buttonSave.setOnClickListener {
            viewCreateDialog {
                actionSplit(it, file = selectedPDF?.absolutePath.toString(), start = {
                    buttonSave.disable()
                    buttonCancel.disable()
                    buttonChooseFile.disable()
                    inputText.beGone()
                }, complete = {
                    if (it.isEmpty()) {
                        return@actionSplit
                    }
                    resetUI()
                    val file = File(it.first())
                    val uri: Uri = FileProvider.getUriForFile(this@SplitActivity, "${packageName}.provider", file)
                    documentViewModel?.insertToCurrentList(mutableListOf(file))
                    go(PDFReaderActivity::class.java, listOf(FILE_URI to uri, FILE_PATH to file.absolutePath, FILE_NAME to file.name), finish = true)
                }, error = {
                    resetUI()
                })
            }
        }
    }

    private fun actionSplit(
        name: String?,
        password: String? = "",
        isProtected: Boolean? = false,
        file: String,
        start: (() -> Unit)? = null,
        progress: ((String) -> Unit)? = null,
        complete: ((MutableList<String>) -> Unit)? = null,
        error: ((String) -> Unit)? = null,
    ) {
        val dialog = showProcessingDialog()
        val dialogTitle = dialog.findViewById<MaterialTextView>(R.id.dialog_title)
        dialogTitle?.text = getString(R.string.progress_label_splitting)
        if (!isFinishing || !isDestroyed) dialog.show()
        val startTime = System.currentTimeMillis()
        CoroutineScope(Dispatchers.IO).launch {
            try {
                withContext(Dispatchers.Main) {
                    start?.invoke()
                }
                val splitConfig: String = binding?.editText?.text.toString().replace("\\s+".toRegex(), "")
                val outputPaths = mutableListOf<String>()
                val ranges: Array<String> = splitConfig.split(",").toTypedArray()

                withContext(Dispatchers.Main) {
                    if (!isInputValid(file, ranges)) {
                        return@withContext
                    }
                }
                val root = File(defaultStorageLocation, "Split")
                createOrExistsDir(root)
                val reader = PdfReader(file)
                PdfReader.unethicalreading = true
                var copy: PdfCopy
                var newDocument: Document
                var startPage: Int
                var endPage: Int
                ranges.forEach { range ->
                    if (reader.numberOfPages > 1) {
                        if (!range.contains("-")) {
                            startPage = range.toInt()
                            newDocument = Document()
                            val outputFile = File(root, "${name}_$startPage.pdf")
                            createOrExistsFile(outputFile)
                            copy = PdfCopy(newDocument, FileOutputStream(outputFile))
                            if (isProtected == true) {
                                copy.setEncryption(
                                    "$password".toByteArray(),
                                    getString(R.string.app_name).toByteArray(),
                                    PdfWriter.ALLOW_PRINTING or PdfWriter.ALLOW_COPY,
                                    PdfWriter.ENCRYPTION_AES_128
                                )
                            }
                            newDocument.open()
                            copy.addPage(copy.getImportedPage(reader, startPage))
                            newDocument.close()
                            outputPaths.add(outputFile.absolutePath)
                        } else {
                            startPage = range.substring(0, range.indexOf("-")).toInt()
                            endPage = range.substring(range.indexOf("-") + 1).toInt()
                            if (reader.numberOfPages == endPage - startPage + 1) {
                                withContext(Dispatchers.Main) {
                                    showToast(getString(R.string.message_range_selected_pdf_again))
                                }
                            } else {
                                newDocument = Document()
                                val outputFile = File(root, "${name}_$startPage-$endPage.pdf")
                                createOrExistsFile(outputFile)
                                copy = PdfCopy(newDocument, FileOutputStream(outputFile))
                                newDocument.open()
                                for (page in startPage..endPage) {
                                    copy.addPage(copy.getImportedPage(reader, page))
                                }
                                newDocument.close()
                                outputPaths.add(outputFile.absolutePath)
                            }
                        }
                    } else {
                        withContext(Dispatchers.Main) {
                            showToast(getString(R.string.message_has_only_1_page))
                        }
                    }
                }

                MediaScannerConnection.scanFile(this@SplitActivity, outputPaths.toTypedArray(), null, null)
                withContext(Dispatchers.Main) {
                    if (System.currentTimeMillis() > (startTime + 5000)) {
                        Handler(mainLooper).postDelayed({
                            if (!isFinishing) dialog.dismiss()
                            complete?.invoke(outputPaths)
                        }, 1000)
                    } else {
                        Handler(mainLooper).postDelayed({
                            if (!isFinishing) dialog.dismiss()
                            complete?.invoke(outputPaths)
                        }, 3000)
                    }
                }
            } catch (e: FileNotFoundException) {
                withContext(Dispatchers.Main) {
                    error?.invoke(e.localizedMessage)
                }
            } catch (e: BadPdfFormatException) {
                withContext(Dispatchers.Main) {
                    error?.invoke(e.localizedMessage)
                }
            } catch (e: IOException) {
                withContext(Dispatchers.Main) {
                    error?.invoke(e.localizedMessage)
                }
            } catch (e: DocumentException) {
                withContext(Dispatchers.Main) {
                    error?.invoke(e.localizedMessage)
                }
            }
        }
    }

    private fun isInputValid(path: String?, ranges: Array<String>): Boolean {
        try {
            val reader = PdfReader(path)
            val numOfPages = reader.numberOfPages
            when (checkRangeValidity(numOfPages, ranges)) {
                ERROR_PAGE_NUMBER -> {
                    showToast(getString(R.string.message_invalid_page_number))
                    return false
                }

                ERROR_PAGE_RANGE -> {
                    showToast(getString(R.string.message_invalid_range_input))
                    return false
                }

                ERROR_INVALID_INPUT -> {
                    showToast(getString(R.string.message_invalid_input))
                    return false
                }

                else -> return true
            }
        } catch (e: IOException) {
        }

        return false
    }

    private fun checkRangeValidity(numOfPages: Int, ranges: Array<String>): Int {
        var startPage = 0
        var endPage = 0
        var returnValue: Int = NO_ERROR
        if (ranges.isEmpty()) {
            returnValue = ERROR_INVALID_INPUT
        } else {
            ranges.forEach {
                if (!it.contains("-")) {
                    try {
                        startPage = it.toInt()
                    } catch (e: NumberFormatException) {
                        returnValue = ERROR_INVALID_INPUT
                        return@forEach
                    }
                    if (startPage > numOfPages || startPage == 0) {
                        returnValue = ERROR_PAGE_NUMBER
                        return@forEach
                    }
                } else {
                    try {
                        startPage = it.substring(0, it.indexOf("-")).toInt()
                        endPage = it.substring(it.indexOf("-") + 1).toInt()
                    } catch (e: NumberFormatException) {
                        returnValue = ERROR_INVALID_INPUT
                        return@forEach
                    } catch (e: StringIndexOutOfBoundsException) {
                        returnValue = ERROR_INVALID_INPUT
                        return@forEach
                    }
                    if (startPage > numOfPages || endPage > numOfPages || startPage == 0 || endPage == 0) {
                        returnValue = ERROR_PAGE_NUMBER
                        return@forEach
                    } else if (startPage >= endPage) {
                        returnValue = ERROR_PAGE_RANGE
                        return@forEach
                    }
                }
            }
        }
        return returnValue
    }

    override fun ActivitySplitBinding.initView() {
        toolbar.title = getString(R.string.action_split_pdf)
        toolbar.setNavigationOnClickListener { onBackPressedDispatcher.onBackPressed() }
        onBackPressedDispatcher.addCallback {
            finish()
        }
    }
}