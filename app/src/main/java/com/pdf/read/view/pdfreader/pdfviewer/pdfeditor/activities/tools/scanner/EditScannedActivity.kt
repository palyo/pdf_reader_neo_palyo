package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.activities.tools.scanner

import android.annotation.*
import android.content.*
import android.os.*
import androidx.activity.*
import androidx.lifecycle.*
import androidx.viewpager2.widget.*
import coder.apps.space.library.base.*
import coder.apps.space.library.extension.beEnableIf
import coder.apps.space.library.extension.beInvisible
import coder.apps.space.library.extension.beVisible
import coder.apps.space.library.extension.disable
import coder.apps.space.library.extension.enable
import coder.apps.space.library.extension.go
import coder.apps.space.library.extension.log
import com.google.android.material.progressindicator.*
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.activities.tools.scanner.adapter.*
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.databinding.*
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.ext.correctImageRotation
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.ext.showProcessingDialog
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.ext.showProgressDialogLoad
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.R
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.admodule.viewBanner
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.ext.createOrExistsDir
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.ext.discardDialog
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.ext.saveBitmapsToDirectoryWithProgress
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.viewmodel.DocumentViewModel
import documentreader.pdfviewer.filereader.documenttools.model.DocSave
import kotlinx.coroutines.*
import java.io.*

class EditScannedActivity : BaseActivity<ActivityEditScannedBinding>(ActivityEditScannedBinding::inflate, isLightModified = true, isLight = false) {

    private var isApplyToAll: Boolean = false
    private var isIDCard: Boolean = false
    private var captured: MutableList<String> = mutableListOf()
    private var pagerAdapter: PagerScannedAdapter? = null
    private val TAG = "EditScannedActivity"

    override fun ActivityEditScannedBinding.initExtra() {
        initPager()
        viewBanner(adNative)
    }

    private fun ActivityEditScannedBinding.initPager() {
        captured = intent.getStringArrayListExtra("paths")?.toMutableList() ?: mutableListOf()
        isIDCard = intent?.getBooleanExtra("isIDCard", false) ?: false
        viewPager.apply {
            pagerAdapter = PagerScannedAdapter(this@EditScannedActivity, isIDCard) {
                scanDoc(isNew = false, isTakeMultiple = true, captured)
            }
            adapter = pagerAdapter
            isUserInputEnabled = false
            registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    updatePageCount(position)
                    if (menuCrop.isSelected) pagerAdapter?.updateTool(viewPager = viewPager, tool = 2)
                    else if (menuFilter.isSelected) pagerAdapter?.updateTool(viewPager = viewPager, tool = 1)
                }
            })
            val progressDialog = showProgressDialogLoad()
            lifecycleScope.launch(Dispatchers.IO) {
                val docs = mutableListOf<DocSave>()
                captured.forEach { docs.add(DocSave(it, FILTER_ORIGINAL, correctImageRotation(it))) }
                runOnUiThread {
                    pagerAdapter?.addAll(docs)
                    offscreenPageLimit = docs.size
                    Handler(mainLooper).postDelayed({ progressDialog.dismiss() }, 1500)
                }
            }
        }
    }

    private fun ActivityEditScannedBinding.scanDoc(isNew: Boolean, isTakeMultiple: Boolean, captured: MutableList<String>) {
        val dialog = DialogScanner.newInstance(isNew = isNew, isTakeMultiple = isTakeMultiple, captured = captured) {
            if (it.isNotEmpty()) {
                val progressDialog = showProgressDialogLoad()
                lifecycleScope.launch(Dispatchers.IO) {
                    val docs = mutableListOf<DocSave>()
                    it.forEach { file -> docs.add(DocSave(file, FILTER_ORIGINAL, correctImageRotation(file))) }
                    this@EditScannedActivity.captured.addAll(it)
                    runOnUiThread {
                        pagerAdapter?.addPages(docs)
                        viewPager.post {
                            viewPager.offscreenPageLimit = pagerAdapter?.itemCount ?: ViewPager2.OFFSCREEN_PAGE_LIMIT_DEFAULT
                            viewPager.setCurrentItem((pagerAdapter?.itemCount ?: 0) - 1, true)
                        }
                        Handler(mainLooper).postDelayed({ progressDialog.dismiss() }, 1500)
                    }
                }
            }
        }
        dialog.isCancelable = true
        dialog.show(supportFragmentManager.beginTransaction(), DialogScanner.TAG)
    }

    @SuppressLint("SetTextI18n")
    override fun ActivityEditScannedBinding.initListeners() {
        menuAutoCrop.setOnClickListener { pagerAdapter?.autoCrop(viewPager = viewPager) }
        menuFullCrop.setOnClickListener { pagerAdapter?.fullCrop(viewPager = viewPager) }
        menuCrop.isSelected = true
        menuFilter.isSelected = false
        menuCrop.setOnClickListener {
            menuCrop.isSelected = true
            menuFilter.isSelected = false
            layoutCropExtra.beVisible()
            layoutEffectExtra.beInvisible()
            buttonApplyAll.beInvisible()
            pagerAdapter?.updateTool(viewPager = viewPager, tool = 2)
        }

        menuFilter.setOnClickListener {
            menuCrop.isSelected = false
            menuFilter.isSelected = true
            layoutCropExtra.beInvisible()
            layoutEffectExtra.beVisible()
            buttonApplyAll.beVisible()
            pagerAdapter?.updateTool(viewPager = viewPager, tool = 1)
        }
        buttonApplyAll.isChecked = isApplyToAll
        buttonApplyAll.setOnCheckedChangeListener { buttonView, isChecked ->
            isApplyToAll = isChecked
        }
        effectOriginal.setOnClickListener {
            pagerAdapter?.applyFilter(viewPager = viewPager, tool = FILTER_ORIGINAL, isApplyToAll = isApplyToAll)
            updateFilterSelection()
        }
        effectBlackWhite.setOnClickListener {
            pagerAdapter?.applyFilter(viewPager = viewPager, tool = FILTER_BW, isApplyToAll = isApplyToAll)
            updateFilterSelection()
        }
        effectSketch.setOnClickListener {
            pagerAdapter?.applyFilter(viewPager = viewPager, tool = FILTER_SKETCH, isApplyToAll = isApplyToAll)
            updateFilterSelection()
        }
        effectMonochrome.setOnClickListener {
            pagerAdapter?.applyFilter(viewPager = viewPager, tool = FILTER_MONOCHROME, isApplyToAll = isApplyToAll)
            updateFilterSelection()
        }
        effectToon.setOnClickListener {
            pagerAdapter?.applyFilter(viewPager = viewPager, tool = FILTER_TOON, isApplyToAll = isApplyToAll)
            updateFilterSelection()
        }
        effectSharpen.setOnClickListener {
            pagerAdapter?.applyFilter(viewPager = viewPager, tool = FILTER_SHARPEN, isApplyToAll = isApplyToAll)
            updateFilterSelection()
        }
        effectContrast.setOnClickListener {
            pagerAdapter?.applyFilter(viewPager = viewPager, tool = FILTER_CONTRAST, isApplyToAll = isApplyToAll)
            updateFilterSelection()
        }
        effectBrightness.setOnClickListener {
            pagerAdapter?.applyFilter(viewPager = viewPager, tool = FILTER_BRIGHTNESS, isApplyToAll = isApplyToAll)
            updateFilterSelection()
        }

        menuRetake.setOnClickListener {
            val dialog = DialogScanner.newInstance(isNew = false, isTakeMultiple = false) {
                if (it.isNotEmpty()) {
                    pagerAdapter?.updateItem(viewPager = viewPager, newPath = it.get(0))
                }
            }
            dialog.isCancelable = true
            dialog.show(supportFragmentManager.beginTransaction(), DialogScanner.TAG)
        }

        menuDelete.setOnClickListener {
            pagerAdapter?.updateTool(viewPager = viewPager, tool = 4)
            menuDelete.beEnableIf(captured.size > 1)
        }

        menuDelete.beEnableIf(captured.size > 1)

        updatePageCount(viewPager.currentItem)
        pageNext.setOnClickListener {
            val currentItem = viewPager.currentItem
            if (currentItem < (pagerAdapter?.itemCount ?: 0) - 1) {
                viewPager.setCurrentItem(currentItem + 1, true)
                updatePageCount(viewPager.currentItem)
            }
        }
        pagePrev.setOnClickListener {
            val currentItem = viewPager.currentItem
            if (currentItem > 0) {
                viewPager.setCurrentItem(currentItem - 1, true)
                updatePageCount(viewPager.currentItem)
            }
        }

        buttonSave.disable()
        Handler(mainLooper).postDelayed({ buttonSave.enable() }, 2000)

        buttonSave.setOnClickListener {
            val dialog = showProcessingDialog()
            val progressBar = dialog.findViewById<LinearProgressIndicator>(R.id.progress_horizontal)
            progressBar?.isIndeterminate = false
            if (!isFinishing || !isDestroyed) dialog.show()
            lifecycleScope.launch {
                val startTime = System.currentTimeMillis()
                val bitmaps = pagerAdapter?.fetchAllBitmaps(viewPager)
                val stamp = if (isIDCard) "id_" else "document_"
                val directory = File(filesDir, "scanner_docs${File.separator}$stamp${System.currentTimeMillis()}")
                createOrExistsDir(directory)
                bitmaps?.let { bm ->
                    saveBitmapsToDirectoryWithProgress(bm, directory, progress = { current: Int, total: Int, percentage: Int, bitmapIndex: Int ->
                        TAG.log("==> $percentage")
                        runOnUiThread {
                            progressBar?.setProgress(percentage, true)
                        }
                    }, complete = {
                        runOnUiThread {
                            if (System.currentTimeMillis() > (startTime + 5000)) {
                                Handler(mainLooper).postDelayed({
                                    if (!isFinishing) dialog.dismiss()
                                    go(ViewScannedDocumentActivity::class.java, listOf("path" to directory.absolutePath), finish = true)
                                }, 1000)
                            } else {
                                Handler(mainLooper).postDelayed({
                                    if (!isFinishing) dialog.dismiss()
                                    go(ViewScannedDocumentActivity::class.java, listOf("path" to directory.absolutePath), finish = true)
                                }, 3000)
                            }
                        }
                    })
                }
            }
        }
        buttonKeepScanning.setOnClickListener {
            scanDoc(isNew = false, isTakeMultiple = true, captured)
        }
    }

    private fun ActivityEditScannedBinding.updatePageCount(currentItem: Int) {
        val actualItemCount = (pagerAdapter?.itemCount ?: 0) - 1
        pageCount.text = getString(R.string.label_page_count, currentItem + 1, (pagerAdapter?.itemCount ?: 0))
        pagePrev.beEnableIf(currentItem > 0)
        pageNext.beEnableIf(currentItem < actualItemCount)

        updateFilterSelection()
    }

    private fun ActivityEditScannedBinding.updateFilterSelection() {
        if (viewPager.currentItem < ((pagerAdapter?.itemCount ?: 0))) {
            pagerAdapter?.apply {
                val doc = allDocs[viewPager.currentItem]
                val effects = listOf(effectOriginal, effectBlackWhite, effectSketch, effectMonochrome, effectToon, effectSharpen, effectContrast, effectBrightness)

                effects.forEach { it.isSelected = false }
                when (doc.filter) {
                    FILTER_ORIGINAL -> effectOriginal.isSelected = true
                    FILTER_BW -> effectBlackWhite.isSelected = true
                    FILTER_SKETCH -> effectSketch.isSelected = true
                    FILTER_MONOCHROME -> effectMonochrome.isSelected = true
                    FILTER_TOON -> effectOriginal.isSelected = true
                    FILTER_SHARPEN -> effectSharpen.isSelected = true
                    FILTER_CONTRAST -> effectContrast.isSelected = true
                    FILTER_BRIGHTNESS -> effectBrightness.isSelected = true
                    else -> effectOriginal.isSelected = true
                }
            }
        }
    }

    override fun ActivityEditScannedBinding.initView() {
        updateStatusBarColor(coder.apps.space.library.R.color.colorBlack)
        toolbar.title = ""
        setSupportActionBar(toolbar)
        toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
        onBackPressedDispatcher.addCallback(this@EditScannedActivity, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                discardDialog {
                    setResult(RESULT_OK, Intent().apply {
                        putExtra("paths", ArrayList(captured))
                    })
                    finish()
                }
            }
        })
    }
}