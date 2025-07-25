package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.activities.tools.converter

import android.content.*
import android.graphics.Color
import android.media.*
import android.net.*
import android.os.*
import androidx.activity.*
import androidx.activity.result.contract.*
import androidx.core.content.*
import androidx.recyclerview.widget.*
import coder.apps.space.library.base.*
import coder.apps.space.library.extension.*
import com.google.android.material.progressindicator.*
import com.google.android.material.textview.*
import com.itextpdf.text.BaseColor
import com.itextpdf.text.Document
import com.itextpdf.text.Element
import com.itextpdf.text.Font
import com.itextpdf.text.PageSize
import com.itextpdf.text.Paragraph
import com.itextpdf.text.Rectangle
import com.itextpdf.text.pdf.PdfWriter
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

class TextToPdfActivity : BaseActivity<ActivityTextToPdfActivityBinding>(ActivityTextToPdfActivityBinding::inflate) {

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
                if (from == TEXT_PICK) {
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

    override fun ActivityTextToPdfActivityBinding.initExtra() {
        documentViewModel = DocumentViewModel.getInstance(application)
        setupAdapter()
        viewNativeSmall(adNative)
    }

    private fun ActivityTextToPdfActivityBinding.setupAdapter() {
        recyclerViewSelected.apply {
            scheduleLayoutAnimation()
            layoutManager = LinearLayoutManager(this@TextToPdfActivity, RecyclerView.VERTICAL, false)
            optPdfAdapter = OptDocAdapter { file, position ->
                val uri: Uri = FileProvider.getUriForFile(
                    this@TextToPdfActivity, "${packageName}.provider", file
                )
                go(
                    ReaderActivity::class.java,
                    listOf(FILE_URI to uri, FILE_PATH to file.path, FILE_NAME to file.name)
                )
            }
            adapter = optPdfAdapter
        }
    }

    override fun ActivityTextToPdfActivityBinding.initListeners() {
        buttonCancel.disable()
        buttonSave.disable()
        actionSelect.setOnClickListener {
            selectedFile = null
            Intent(this@TextToPdfActivity, SelectFileActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                putExtra("FROM", TEXT_PICK)
                putExtra("type", TXT_FILE)
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
                        this@TextToPdfActivity, "${packageName}.provider", file
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
                this@TextToPdfActivity, "$packageName.provider", File(file)
            )
            val pageSize = Rectangle(PageSize.getRectangle("A4"))
            pageSize.backgroundColor = BaseColor(
                Color.red(Color.WHITE), Color.green(Color.WHITE), Color.blue(Color.WHITE)
            )

            val document = Document(pageSize)
            val root = File(defaultStorageLocation, "Text to PDF")
            createOrExistsDir(root)
            val outputFile = File(root, "$name.pdf")
            createOrExistsFile(outputFile)

            val writer = PdfWriter.getInstance(document, FileOutputStream(outputFile))
            writer.setPdfVersion(PdfWriter.VERSION_1_7)
            if (isProtected == true) {
                writer.setEncryption(
                    password?.toByteArray(), getString(R.string.app_name).toByteArray(),
                    PdfWriter.ALLOW_PRINTING or PdfWriter.ALLOW_COPY, PdfWriter.ENCRYPTION_AES_128
                )
            }
            document.open()
            val font = Font(Font.FontFamily.valueOf("UNDEFINED"))
            font.style = Font.NORMAL
            font.size = 16F
            font.color = BaseColor(
                Color.red(Color.BLACK), Color.green(Color.BLACK), Color.blue(Color.BLACK)
            )
            document.add(Paragraph("\n"))
            val inputStream: InputStream?
            try {
                inputStream = contentResolver.openInputStream(uri)
                if (inputStream == null) return@launch
                val reader = BufferedReader(InputStreamReader(inputStream))
                var line: String?

                while (true) {
                    line = reader.readLine()
                    if (line == null) break

                    println("line = $line")
                    val para = Paragraph("""
$line

""".trimIndent(), font)
                    para.alignment = Element.ALIGN_JUSTIFIED
                    document.add(para)
                }
                reader.close()
                inputStream.close()
                document.close()
                MediaScannerConnection.scanFile(this@TextToPdfActivity, arrayOf(outputFile.absolutePath), null, null)
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

    private fun ActivityTextToPdfActivityBinding.resetUI() {
        recyclerViewSelected.beGone()
        buttonSave.disable()
        buttonCancel.disable()
        actionSelect.enable()
        selectedFile = null
    }

    override fun ActivityTextToPdfActivityBinding.initView() {
        toolbar.setNavigationOnClickListener { onBackPressedDispatcher.onBackPressed() }
        onBackPressedDispatcher.addCallback { finish() }
    }
}