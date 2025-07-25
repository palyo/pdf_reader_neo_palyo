package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.activities.tools.modify

import android.app.Activity.RESULT_CANCELED
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coder.apps.space.library.base.BaseActivity
import coder.apps.space.library.extension.beGone
import coder.apps.space.library.extension.beVisible
import coder.apps.space.library.extension.disable
import coder.apps.space.library.extension.enable
import coder.apps.space.library.extension.go
import coder.apps.space.library.extension.showToast
import com.google.android.material.progressindicator.LinearProgressIndicator
import com.google.android.material.textview.MaterialTextView
import com.itextpdf.text.DocumentException
import com.itextpdf.text.pdf.PRStream
import com.itextpdf.text.pdf.PdfName
import com.itextpdf.text.pdf.PdfNumber
import com.itextpdf.text.pdf.PdfReader
import com.itextpdf.text.pdf.PdfStamper
import com.itextpdf.text.pdf.parser.PdfImageObject
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.R
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.activities.picker.SelectFileActivity
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.activities.viewer.PDFReaderActivity
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.adapter.OptDocAdapter
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.admodule.viewNativeSmall
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.databinding.ActivityCompressBinding
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.ext.COMPRESS
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.ext.FILE_NAME
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.ext.FILE_PATH
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.ext.FILE_URI
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.ext.PDF_FILE
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.ext.SELECTED_FILE
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
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class CompressActivity : BaseActivity<ActivityCompressBinding>(ActivityCompressBinding::inflate) {

    private var documentViewModel: DocumentViewModel? = null
    private var selectedPDF: File? = null
    private var optPdfAdapter: OptDocAdapter? = null
    private var resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        when (result.resultCode) {
            RESULT_OK -> {
                val resultData = result.data
                val from = resultData?.getStringExtra("FOR")
                if (from == COMPRESS) {
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
                        recyclerViewSelected.beVisible()
                        layoutButtons.beVisible()
                        inputText.beVisible()
                        textNote.beVisible()
                        optPdfAdapter?.addItem(selectedPDF?.absolutePath.toString())
                        buttonSave.enable()
                        buttonCancel.enable()
                    }
                } else {
                    resetUI()
                }
            }
            RESULT_CANCELED -> {
                resetUI()
            }
        }
    }

    private fun resetUI() {
        binding?.apply {
            optPdfAdapter?.clear()
            recyclerViewSelected.beGone()
            textNote.beGone()
            inputText.beGone()
            buttonSave.disable()
            buttonCancel.disable()
            buttonChooseFile.enable()
            selectedPDF = null
            editText.setText("80")
        }
    }

    override fun ActivityCompressBinding.initExtra() {
        documentViewModel = DocumentViewModel.getInstance(application)
        initAdapter()
        viewNativeSmall(adNative)
    }

    private fun ActivityCompressBinding.initAdapter() {
        recyclerViewSelected.apply {
            itemAnimator = FadeInUpAnimator(OvershootInterpolator(1f))
            scheduleLayoutAnimation()
            layoutManager = LinearLayoutManager(this@CompressActivity, RecyclerView.VERTICAL, false)
            optPdfAdapter = OptDocAdapter { file, position ->
                val uri: Uri = FileProvider.getUriForFile(this@CompressActivity, "${packageName}.provider", file)
                go(PDFReaderActivity::class.java, listOf(FILE_URI to uri, FILE_PATH to file.absolutePath, FILE_NAME to file.name))
            }
            adapter = optPdfAdapter
        }
    }

    override fun ActivityCompressBinding.initListeners() {
        buttonSave.disable()
        buttonCancel.disable()
        buttonChooseFile.setOnClickListener {
            selectedPDF = null
            Intent(this@CompressActivity, SelectFileActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                putExtra("FROM", COMPRESS)
                putExtra("type", PDF_FILE)
                resultLauncher.launch(this)
            }
        }
        buttonCancel.setOnClickListener {
            resetUI()
        }
        buttonSave.setOnClickListener {
            viewCreateDialog {
                actionCompress(it, file = selectedPDF?.absolutePath.toString(), start = {
                    buttonSave.disable()
                    buttonCancel.disable()
                    buttonChooseFile.disable()
                }, complete = {
                    if (it.isEmpty()) {
                        return@actionCompress
                    }
                    resetUI()
                    val file = File(it)
                    val uri: Uri = FileProvider.getUriForFile(this@CompressActivity, "${packageName}.provider", file)
                    documentViewModel?.insertToCurrentList(mutableListOf(file))
                    go(PDFReaderActivity::class.java, listOf(FILE_URI to uri, FILE_PATH to file.absolutePath, FILE_NAME to file.name), finish = true)
                }, error = {
                    resetUI()
                })
            }
        }
    }

    private fun actionCompress(
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
        dialogTitle?.text = getString(R.string.progress_label_compressing)
        if (!isFinishing || !isDestroyed) dialog.show()
        val startTime = System.currentTimeMillis()
        val percentage: Int = binding?.editText?.text.toString().trim().toInt()
        CoroutineScope(Dispatchers.IO).launch {
            try {
                withContext(Dispatchers.Main) {
                    start?.invoke()
                }
                if (percentage > 100 || percentage <= 0) {
                    withContext(Dispatchers.Main) {
                        showToast("Invalid compress (%)")
                        error?.invoke("Invalid compress (%)")
                    }
                    return@launch
                }

                val root = File(defaultStorageLocation, "Compress")
                createOrExistsDir(root)
                val outputFile = File(root, "$name.pdf")
                createOrExistsFile(outputFile)

                val reader = PdfReader(file)
                val xrefSize = reader.xrefSize
                for (i in 0 until xrefSize) {
                    val pdfObject = reader.getPdfObject(i) ?: continue
                    if (!pdfObject.isStream) continue

                    val stream = pdfObject as PRStream
                    val pdfSubType = stream[PdfName.SUBTYPE]
                    if (pdfSubType != null && pdfSubType.toString() == PdfName.IMAGE.toString()) {
                        val image = PdfImageObject(stream)
                        val imageBytes = image.imageAsBytes
                        val bmp = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size) ?: continue
                        val width = bmp.width
                        val height = bmp.height

                        val outBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
                        val outCanvas = Canvas(outBitmap)
                        outCanvas.drawBitmap(bmp, 0f, 0f, null)
                        val imgBytes = ByteArrayOutputStream()
                        outBitmap.compress(Bitmap.CompressFormat.JPEG, 100 - percentage, imgBytes)
                        stream.clear()
                        stream.setData(imgBytes.toByteArray(), false, PRStream.BEST_COMPRESSION)
                        stream.put(PdfName.TYPE, PdfName.XOBJECT)
                        stream.put(PdfName.SUBTYPE, PdfName.IMAGE)
                        stream.put(PdfName.FILTER, PdfName.DCTDECODE)
                        stream.put(PdfName.WIDTH, PdfNumber(width))
                        stream.put(PdfName.HEIGHT, PdfNumber(height))
                        stream.put(PdfName.BITSPERCOMPONENT, PdfNumber(8))
                        stream.put(PdfName.COLORSPACE, PdfName.DEVICERGB)
                    }
                }
                reader.removeUnusedObjects()
                val stamper = PdfStamper(reader, FileOutputStream(outputFile))
                stamper.setFullCompression()
                stamper.close()
                reader.close()

                MediaScannerConnection.scanFile(this@CompressActivity, arrayOf(outputFile.absolutePath), null, null)
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
            } catch (e: IOException) {
                withContext(Dispatchers.Main) {
                    error?.invoke(e.localizedMessage)
                }
            } catch (e: DocumentException) {
                withContext(Dispatchers.Main) {
                    error?.invoke(e.localizedMessage)
                }
            } catch (e: NumberFormatException) {
                withContext(Dispatchers.Main) {
                    error?.invoke(e.localizedMessage)
                }
            }
        }
    }

    override fun ActivityCompressBinding.initView() {
        toolbar.title = getString(R.string.action_compress_pdf)
        toolbar.setNavigationOnClickListener { onBackPressedDispatcher.onBackPressed() }
        onBackPressedDispatcher.addCallback(this@CompressActivity, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                finish()
            }
        })
    }
}