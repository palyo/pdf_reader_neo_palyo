package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.activities.tools.scanner

import android.content.Intent
import android.media.MediaScannerConnection
import android.net.Uri
import android.view.animation.OvershootInterpolator
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coder.apps.space.library.base.BaseActivity
import coder.apps.space.library.extension.beGone
import coder.apps.space.library.extension.beVisible
import coder.apps.space.library.extension.beVisibleIf
import coder.apps.space.library.extension.go
import coder.apps.space.library.extension.log
import coder.apps.space.library.extension.mimeType
import coder.apps.space.library.extension.showToast
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.R
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.activities.tools.scanner.adapter.DocAlbumAdapter
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.admodule.viewBanner
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.databinding.ActivityListDocScannedBinding
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.ext.CAMERA_PERMISSION
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.ext.createOrExistsDir
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.ext.deleteDirectory
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.ext.hasPermissions
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.ext.saveImagesToPdf
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.ext.showOtherPermissionDialog
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.ext.viewDocumentOption
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.ext.viewRenameSheet
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.ext.viewTrashOrDeleteSheet
import documentreader.pdfviewer.filereader.documenttools.model.DocAlbum
import jp.wasabeef.recyclerview.animators.FadeInUpAnimator
import kotlinx.coroutines.launch
import java.io.File

class ListDocScannedActivity : BaseActivity<ActivityListDocScannedBinding>(ActivityListDocScannedBinding::inflate) {

    private val TAG = "ListDocScannedActivity"
    private var permissionLauncher = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
        if (!hasPermissions(CAMERA_PERMISSION)) {
            showOtherPermissionDialog { initCameraPermission() }
        } else {
            scanDoc()
        }
    }

    private var fileAdapter: DocAlbumAdapter? = null

    override fun ActivityListDocScannedBinding.initExtra() {
        initAdapter()
    }

    private fun ActivityListDocScannedBinding.initAdapter() {
        recyclerView.apply {
            itemAnimator = FadeInUpAnimator(OvershootInterpolator())
            layoutManager = LinearLayoutManager(this@ListDocScannedActivity, RecyclerView.VERTICAL, false)
            fileAdapter = DocAlbumAdapter(click = { file, position ->
                go(ViewScannedDocumentActivity::class.java, listOf("path" to file.albumPath))
            }, option = { file, view, position, opt ->
                when (opt) {
                    3 -> {
                        viewDocumentOption(isDocScanner = true) {
                            when (it) {
                                2 -> {
                                    val renameFile = File(file.albumPath)
                                    viewRenameSheet(renameFile.name) {
                                        val newFile = File(renameFile.parentFile, it)
                                        file.albumPath = newFile.absolutePath
                                        fileAdapter?.updateItem(position, file)
                                        renameFile.renameTo(newFile)
                                        MediaScannerConnection.scanFile(this@ListDocScannedActivity, arrayOf(renameFile.absolutePath, newFile.absolutePath), null, null)
                                    }
                                }

                                4 -> {
                                    viewTrashOrDeleteSheet {
                                        val deleteFile = File(file.albumPath)
                                        fileAdapter?.remove(position)
                                        deleteDirectory(deleteFile)
                                        MediaScannerConnection.scanFile(this@ListDocScannedActivity, arrayOf(deleteFile.absolutePath), null, null)
                                        layoutEmpty.beVisibleIf(fileAdapter?.itemCount == 0)
                                    }
                                }
                                5 -> {
                                    showToast("Please wait..")
                                    val files = File(file.albumPath).listFiles()?.map { it.absolutePath ?: "" }?.toMutableList()
                                    val uris: ArrayList<Uri> = ArrayList()
                                    files?.forEach {
                                        val uri: Uri = FileProvider.getUriForFile(this@ListDocScannedActivity, "${packageName}.provider", File(it))
                                        uris.add(uri)
                                    }
                                    Intent(Intent.ACTION_SEND_MULTIPLE).apply {
                                        type = "image/*"
                                        putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris)
                                        startActivity(Intent.createChooser(this, "Share Images"))
                                    }
                                }
                                6 -> {
                                    showToast("Please wait..")
                                    lifecycleScope.launch {
                                        val files = File(file.albumPath).listFiles()?.map { it.absolutePath ?: "" }?.toMutableList()
                                        val directory = File(cacheDir, "temporary_share")
                                        if (File(directory, "_temporary_pdf_.pdf").exists()) {
                                            runOnUiThread {
                                                val file = File(directory, "_temporary_pdf_.pdf")
                                                val uri: Uri = FileProvider.getUriForFile(this@ListDocScannedActivity, "${packageName}.provider", file)
                                                Intent(Intent.ACTION_SEND).apply {
                                                    type = file.mimeType
                                                    putExtra(Intent.EXTRA_STREAM, uri)
                                                    startActivity(Intent.createChooser(this, "Share PDFs"))
                                                }
                                            }
                                            return@launch
                                        }

                                        createOrExistsDir(directory)
                                        files?.let { paths ->
                                            saveImagesToPdf(
                                                imagePaths = paths, destinationDir = directory, fileName = "_temporary_pdf_", progress = { current: Int, total: Int, percentage: Int, imagePath: String ->
                                                TAG.log("==> $percentage")
                                            }, complete = {
                                                runOnUiThread {
                                                    val file = File(it[0].toString())
                                                    val uri: Uri = FileProvider.getUriForFile(this@ListDocScannedActivity, "${packageName}.provider", file)
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
                            }
                        }
                    }
                }
            })
            adapter = fileAdapter
        }
    }

    override fun onResume() {
        super.onResume()
        binding?.fetchDocumentList()
    }

    private fun ActivityListDocScannedBinding.fetchDocumentList() {
        progressHorizontal.beVisible()
        fileAdapter?.clear()
        lifecycleScope.launch {
            val albums = mutableListOf<DocAlbum>()
            val directory = File(filesDir, "scanner_docs")
            createOrExistsDir(directory)
            val files = directory.listFiles()?.toMutableList()?.sortedByDescending { it.lastModified() }
            files?.forEach {
                val childs = it.listFiles()
                val size = getDirectorySize(childs?.toMutableList())
                if (childs?.isNotEmpty() == true) {
                    albums.add(DocAlbum(it.absolutePath, childs[0]?.absolutePath ?: "", childs.size, size = size))
                }
            }
            runOnUiThread {
                fileAdapter?.addAll(albums)
                progressHorizontal.beGone()
                layoutEmpty.beVisibleIf(fileAdapter?.itemCount == 0)
                if ((fileAdapter?.itemCount ?: 0) > 0) {
                    viewBanner(adNative)
                }

            }
        }
    }

    private fun getDirectorySize(files: MutableList<File>?): Long {
        if (files.isNullOrEmpty()) {
            return 0
        }
        var size: Long = 0
        for (file in files) {
            size += file.length()
        }
        return size
    }

    override fun ActivityListDocScannedBinding.initListeners() {
        buttonScanDoc.setOnClickListener {
            if (!hasPermissions(CAMERA_PERMISSION)) {
                initCameraPermission()
            } else {
                scanDoc()
            }
        }
        buttonScanDocEmpty.setOnClickListener {
            if (!hasPermissions(CAMERA_PERMISSION)) {
                initCameraPermission()
            } else {
                scanDoc()
            }
        }
    }

    private fun scanDoc() {
        val dialog = DialogScanner.newInstance {}
        dialog.isCancelable = true
        dialog.show(supportFragmentManager.beginTransaction(), DialogScanner.TAG)
    }

    private fun initCameraPermission() {
        permissionLauncher.launch(CAMERA_PERMISSION)
    }

    override fun ActivityListDocScannedBinding.initView() {
        toolbar.title = getString(R.string.title_document_scanner)
        setSupportActionBar(toolbar)
        toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
        onBackPressedDispatcher.addCallback(this@ListDocScannedActivity, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                setResult(RESULT_OK, Intent().apply {
                    putExtra("isDocScannerEmpty", fileAdapter?.itemCount == 0)
                })
                finish()
            }
        })
    }
}
