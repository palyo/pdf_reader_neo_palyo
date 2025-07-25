package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.activities.tools.modify

import android.app.Activity.RESULT_CANCELED
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.view.animation.OvershootInterpolator
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ItemTouchHelper.Callback.makeMovementFlags
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coder.apps.space.library.base.BaseActivity
import coder.apps.space.library.extension.beGone
import coder.apps.space.library.extension.beVisible
import coder.apps.space.library.extension.disable
import coder.apps.space.library.extension.enable
import coder.apps.space.library.extension.go
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
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.databinding.ActivityMergeBinding
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.ext.FILE_NAME
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.ext.FILE_PATH
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.ext.FILE_URI
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.ext.MERGE
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.ext.PDF_FILE
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.ext.SELECTED_FILE
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.ext.createOrExistsDir
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.ext.createOrExistsFile
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.ext.defaultStorageLocation
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
import java.util.Collections

class MergeActivity : BaseActivity<ActivityMergeBinding>(ActivityMergeBinding::inflate) {

    private var documentViewModel: DocumentViewModel? = null
    private var optPdfAdapter: OptDocAdapter? = null
    private var touchHelper: ItemTouchHelper? = null
    private var resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        when (result.resultCode) {
            RESULT_OK -> {
                val resultData = result.data
                val from = resultData?.getStringExtra("FOR")
                if (from == MERGE) {
                    val pdfs: java.util.ArrayList<String> = resultData.getStringArrayListExtra(SELECTED_FILE) as java.util.ArrayList<String>
                    if (pdfs.isEmpty()) {
                        resetUI()
                        return@registerForActivityResult
                    }
                    binding?.apply {
                        recyclerViewSelected.beVisible()
                        textNote.beVisible()
                        optPdfAdapter?.updateList(pdfs.toMutableList())
                        if (pdfs.size >= 2) {
                            buttonSave.enable()
                            buttonCancel.enable()
                        }
                    }
                } else resetUI()
            }

            RESULT_CANCELED -> {
                resetUI()
            }
        }
    }

    private fun resetUI() {
        binding?.apply {
            buttonChooseFile.enable()
            recyclerViewSelected.beGone()
            textNote.beGone()
            buttonSave.disable()
            buttonCancel.disable()
            optPdfAdapter?.clear()
        }
    }

    override fun ActivityMergeBinding.initExtra() {
        documentViewModel = DocumentViewModel.getInstance(application)
        initTouch()
        initAdapter()
        viewNativeSmall(adNative)
    }

    private fun ActivityMergeBinding.initAdapter() {
        recyclerViewSelected.apply {
            itemAnimator = FadeInUpAnimator(OvershootInterpolator(1f))
            scheduleLayoutAnimation()
            layoutManager = LinearLayoutManager(this@MergeActivity, RecyclerView.VERTICAL, false)
            optPdfAdapter = OptDocAdapter { file, position ->
                val uri: Uri = FileProvider.getUriForFile(this@MergeActivity, "${packageName}.provider", file)
                go(PDFReaderActivity::class.java, listOf(FILE_URI to uri, FILE_PATH to file.absolutePath, FILE_NAME to file.name))
            }
            adapter = optPdfAdapter
            touchHelper?.attachToRecyclerView(this)
        }
    }

    private fun ActivityMergeBinding.initTouch() {
        touchHelper = ItemTouchHelper(object : ItemTouchHelper.Callback() {
            override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {
                return makeMovementFlags(
                    ItemTouchHelper.UP or ItemTouchHelper.DOWN,
                    ItemTouchHelper.END or ItemTouchHelper.START
                )
            }

            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                val draggedItemIndex = viewHolder.adapterPosition
                val targetIndex = target.adapterPosition
                optPdfAdapter?.apply {
                    Collections.swap(getSources(), draggedItemIndex, targetIndex)
                    notifyItemMoved(draggedItemIndex, targetIndex)
                }
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                optPdfAdapter?.apply {
                    when (direction) {
                        ItemTouchHelper.END -> {
                            getSources().removeAt(viewHolder.adapterPosition)
                            notifyItemRemoved(viewHolder.adapterPosition)
                            if (itemCount == 0) {
                                recyclerViewSelected.beGone()
                                textNote.beGone()
                            }
                        }

                        ItemTouchHelper.START -> {
                            getSources().removeAt(viewHolder.adapterPosition)
                            notifyItemRemoved(viewHolder.adapterPosition)
                            if (itemCount == 0) {
                                recyclerViewSelected.beGone()
                                textNote.beGone()
                            }
                        }
                    }
                }
            }
        })
    }

    override fun ActivityMergeBinding.initListeners() {
        buttonSave.disable()
        buttonCancel.disable()
        buttonChooseFile.setOnClickListener {
            Intent(this@MergeActivity, SelectFileActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                putExtra("FROM", MERGE)
                putExtra("type", PDF_FILE)
                putExtra("selected", optPdfAdapter?.getSources() as ArrayList)
                resultLauncher.launch(this)
            }
        }
        buttonCancel.setOnClickListener {
            resetUI()
        }
        buttonSave.setOnClickListener {
            viewCreateDialog {
                actionMerge(name = it, files = optPdfAdapter?.getSources(), start = {
                    buttonSave.disable()
                    buttonCancel.disable()
                    buttonChooseFile.disable()
                }, complete = {
                    resetUI()
                    val file = File(it)
                    val uri: Uri = FileProvider.getUriForFile(this@MergeActivity, "${packageName}.provider", file)
                    documentViewModel?.insertToCurrentList(mutableListOf(file))
                    go(PDFReaderActivity::class.java, listOf(FILE_URI to uri, FILE_PATH to file.absolutePath, FILE_NAME to file.name), finish = true)
                }, error = {
                    resetUI()
                })
            }
        }
    }

    private fun actionMerge(
        name: String?,
        password: String? = "",
        isProtected: Boolean? = false,
        files: MutableList<String>?,
        start: (() -> Unit)? = null,
        progress: ((String) -> Unit)? = null,
        complete: ((String) -> Unit)? = null,
        error: ((String) -> Unit)? = null,
    ) {
        val dialog = showProcessingDialog()
        val dialogTitle = dialog.findViewById<MaterialTextView>(R.id.dialog_title)
        dialogTitle?.text = getString(R.string.progress_label_merging)
        if (!isFinishing || !isDestroyed) dialog.show()
        val startTime = System.currentTimeMillis()
        CoroutineScope(Dispatchers.IO).launch {
            try {
                withContext(Dispatchers.Main) {
                    start?.invoke()
                }
                val root = File(defaultStorageLocation, "Merge")
                createOrExistsDir(root)
                val outputFile = File(root, "$name.pdf")
                createOrExistsFile(outputFile)
                val document = Document()
                val copy = PdfCopy(document, FileOutputStream(outputFile))
                if (isProtected == true) {
                    copy.setEncryption(
                        password?.toByteArray(),
                        getString(R.string.app_name).toByteArray(),
                        PdfWriter.ALLOW_PRINTING or PdfWriter.ALLOW_COPY,
                        PdfWriter.ENCRYPTION_AES_128
                    )
                }

                document.open()
                var numOfPages: Int
                files?.forEach { filePath ->
                    val pdfReader = PdfReader(filePath)
                    PdfReader.unethicalreading = true
                    numOfPages = pdfReader.numberOfPages
                    for (page in 1..numOfPages) {
                        copy.addPage(copy.getImportedPage(pdfReader, page))
                    }
                    pdfReader.close()  // Ensure the PdfReader is closed after processing each file
                }
                document.close()

                MediaScannerConnection.scanFile(this@MergeActivity, arrayOf(outputFile.absolutePath), null, null)
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

    override fun ActivityMergeBinding.initView() {
        toolbar.title = getString(R.string.action_merge_pdf)
        toolbar.setNavigationOnClickListener { onBackPressedDispatcher.onBackPressed() }
        onBackPressedDispatcher.addCallback(this@MergeActivity, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                finish()
            }
        })
    }
}