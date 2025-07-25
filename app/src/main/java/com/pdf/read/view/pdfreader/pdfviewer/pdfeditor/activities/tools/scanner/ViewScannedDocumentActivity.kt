package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.activities.tools.scanner

import android.content.*
import android.media.*
import android.net.*
import android.os.*
import androidx.activity.*
import androidx.core.content.*
import androidx.lifecycle.*
import androidx.viewpager2.widget.*
import coder.apps.space.library.base.*
import coder.apps.space.library.extension.*
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.R
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.activities.tools.scanner.adapter.*
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.admodule.viewBanner
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.databinding.*
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.ext.*
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.viewmodel.DocumentViewModel
import kotlinx.coroutines.*
import java.io.*

class ViewScannedDocumentActivity : BaseActivity<ActivityViewScannedDocumentBinding>(ActivityViewScannedDocumentBinding::inflate) {

    private var documentViewModel: DocumentViewModel? = null
    private val TAG = "ViewScannedDocument"
    private var viewDir: File? = null
    private var pagerAdapter: PagerSavedAdapter? = null

    override fun ActivityViewScannedDocumentBinding.initExtra() {
        documentViewModel = DocumentViewModel.getInstance(application)
        initPager()
        viewBanner(adNative)
    }

    private fun ActivityViewScannedDocumentBinding.initPager() {
        viewPager.apply {
            pagerAdapter = PagerSavedAdapter(this@ViewScannedDocumentActivity, isIDCard = viewDir?.name?.startsWith("id_") == true) {
                pagerAdapter?.allPaths?.let { scanDoc(isNew = false, isTakeMultiple = true, it) }
            }
            adapter = pagerAdapter
            registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    updatePageCount(currentItem = position)
                }
            })
        }
    }

    private fun scanDoc(isNew: Boolean, isTakeMultiple: Boolean, captured: MutableList<String>) {
        val dialog = DialogScanner.newInstance(isNew = isNew, isTakeMultiple = isTakeMultiple, captured = captured) {
            if (it.isNotEmpty()) {
                go(EditScannedActivity::class.java, listOf("paths" to ArrayList(it), "isIDCard" to viewDir?.name?.startsWith("id_")), finish = true)
            }
        }
        dialog.isCancelable = true
        dialog.show(supportFragmentManager.beginTransaction(), DialogScanner.TAG)
    }

    private fun ActivityViewScannedDocumentBinding.updatePageCount(currentItem: Int) {
        val actualItemCount = (pagerAdapter?.itemCount ?: 0) - 1
        if (currentItem < actualItemCount) {
            pageCount.text = getString(R.string.label_page_count, currentItem + 1, actualItemCount)
        } else {
            pageCount.text = getString(R.string.label_page_count, actualItemCount, actualItemCount)
        }
        pagePrev.beEnableIf(currentItem > 0)
        pageNext.beEnableIf(currentItem < actualItemCount)
    }

    override fun ActivityViewScannedDocumentBinding.initListeners() {
        pageNext.setOnClickListener {
            val currentItem = viewPager.currentItem
            if (currentItem < (pagerAdapter?.itemCount ?: 0) - 1) {
                viewPager.setCurrentItem(currentItem + 1, true)
                updatePageCount(currentItem = viewPager.currentItem)
            }
        }
        pagePrev.setOnClickListener {
            val currentItem = viewPager.currentItem
            if (currentItem > 0) {
                viewPager.setCurrentItem(currentItem - 1, true)
                updatePageCount(currentItem = viewPager.currentItem)
            }
        }
        menuShare.setOnClickListener {
            viewSavedDocumentOption(viewDir = viewDir, thumb = pagerAdapter?.allPaths?.get(0) ?: "", type = "SHARE") {
                performOperation(it)
            }
        }
        menuDelete.setOnClickListener {
            viewTrashOrDeleteSheet {
                viewDir?.let { file ->
                    deleteDirectory(file)
                    showToast(getString(R.string.message_document_deleted_successfully))
                    MediaScannerConnection.scanFile(this@ViewScannedDocumentActivity, arrayOf(file.absolutePath), null) { path, uri -> finish() }
                }
            }
        }
        menuMore.setOnClickListener {
            viewSavedDocumentOption(viewDir = viewDir, thumb = pagerAdapter?.allPaths?.get(0) ?: "", type = "MORE") {
                performOperation(it)
            }
        }
        buttonHistory.setOnClickListener {
            go(ListDocScannedActivity::class.java, finish = true)
        }
    }

    private fun performOperation(opt: Int) {
        when (opt) {
            1 -> { //Export PDF
                lifecycleScope.launch {
                    val directory = File(defaultStorageLocation, "DocScanner${File.separator}PDFs")
                    createOrExistsDir(directory)
                    pagerAdapter?.allPaths?.let { paths ->
                        saveImagesToPdf(
                            imagePaths = paths, destinationDir = directory, fileName = "_${System.currentTimeMillis()}", progress = { current: Int, total: Int, percentage: Int, imagePath: String ->
                            TAG.log("==> $percentage")
                        }, complete = {
                            val list = it.map { File(it.toString()) }.toMutableList()
                            documentViewModel?.insertToCurrentList(list)
                            runOnUiThread { showToast(getString(R.string.message_pdf_saved_successfully)) }
                        })
                    }
                }
            }

            2 -> { //Export JPEG
                lifecycleScope.launch {
                    val directory = File(defaultStorageLocation, "DocScanner${File.separator}Images")
                    createOrExistsDir(directory)
                    pagerAdapter?.allPaths?.let { paths ->
                        copyFilesWithProgress(paths, destinationDir = directory, progress = { current: Int, total: Int, percentage: Int, imagePath: String ->
                            TAG.log("==> $percentage")
                        }, complete = {
                            runOnUiThread { showToast(getString(R.string.message_images_saved_successfully)) }
                        })
                    }
                }
            }

            3 -> { //Print
                lifecycleScope.launch {
                    val directory = File(defaultStorageLocation, "DocScanner${File.separator}Images")
                    createOrExistsDir(directory)
                    pagerAdapter?.allPaths?.let { paths ->
                        copyFilesWithProgress(paths, destinationDir = directory, progress = { current: Int, total: Int, percentage: Int, imagePath: String ->
                            TAG.log("==> $percentage")
                        }, complete = {
                            runOnUiThread { showToast(getString(R.string.message_images_saved_successfully)) }
                        })
                    }
                }
            }

            4 -> { //Share PDF
                lifecycleScope.launch {
                    val directory = File(cacheDir, "temporary_share")
                    createOrExistsDir(directory)
                    pagerAdapter?.allPaths?.let { paths ->
                        saveImagesToPdf(imagePaths = paths, destinationDir = directory, fileName = "_temporary_pdf_", progress = { current: Int, total: Int, percentage: Int, imagePath: String ->
                            TAG.log("==> $percentage")
                        }, complete = {
                            runOnUiThread {
                                val file = File(it[0].toString())
                                val uri: Uri = FileProvider.getUriForFile(this@ViewScannedDocumentActivity, "${packageName}.provider", file)
                                Intent(Intent.ACTION_SEND).apply {
                                    type = file.mimeType
                                    putExtra(Intent.EXTRA_STREAM, uri)
                                    startActivity(Intent.createChooser(this, "Share PDFs"))
                                }
                            }
                        })
                    }
                }
            }

            5 -> { // Share JPEG
                val files: ArrayList<Uri> = ArrayList()
                pagerAdapter?.allPaths?.forEach {
                    val uri: Uri = FileProvider.getUriForFile(this@ViewScannedDocumentActivity, "${packageName}.provider", File(it))
                    files.add(uri)
                }
                Intent(Intent.ACTION_SEND_MULTIPLE).apply {
                    type = "image/*"
                    putParcelableArrayListExtra(Intent.EXTRA_STREAM, files)
                    startActivity(Intent.createChooser(this, "Share Images"))
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        fetchData()
    }

    private fun fetchData() {
        lifecycleScope.launch {
            val files = viewDir?.listFiles()?.map { it.absolutePath ?: "" }?.toMutableList()
            runOnUiThread {
                files?.let { pagerAdapter?.addAll(it) }
                Handler(mainLooper).postDelayed({
                    binding?.apply { viewPager.post { updatePageCount(viewPager.currentItem) } }
                }, 500)
            }
        }
    }

    override fun ActivityViewScannedDocumentBinding.initView() {
        viewDir = File(intent?.getStringExtra("path") ?: "")
        toolbar.title = viewDir?.name ?: ""
        setSupportActionBar(toolbar)
        toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
        onBackPressedDispatcher.addCallback {
            finish()
        }
    }
}