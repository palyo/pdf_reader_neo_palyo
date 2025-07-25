package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.activities.viewer

import android.content.*
import android.content.ClipboardManager
import android.content.pm.*
import android.content.res.*
import android.graphics.*
import android.net.*
import android.os.*
import android.print.*
import android.text.*
import android.view.*
import android.widget.*
import androidx.activity.*
import androidx.core.content.*
import androidx.core.view.*
import coder.apps.space.library.base.*
import coder.apps.space.library.extension.*
import coder.apps.space.library.helper.*
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.App.Companion.isOpenInter
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.R
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.adapter.*
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.admodule.*
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.database.*
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.database.table.*
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.databinding.*
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.ext.*
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.utils.*
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.utils.viewer.*
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.utils.viewer.model.*
import com.tom_roush.pdfbox.pdmodel.*
import com.tom_roush.pdfbox.pdmodel.interactive.annotation.*
import kotlinx.coroutines.*
import java.io.*
import java.util.*

class PDFReaderActivity : BaseActivity<ActivityPdfReaderBinding>(ActivityPdfReaderBinding::inflate) {

    private var favorite: Favorite? = null
    private var isFavorite: Boolean = false
    private var fileName: String? = null
    private var filePath: String? = null
    private var fileUri: Uri? = null
    private var isFullScreenView = false
    private var isHorizontalLock = false
    private var isZoomLock = false
    private var pdfRenderScope: CoroutineScope = CoroutineScope(Dispatchers.IO)

    override fun ActivityPdfReaderBinding.initExtra() {
        if (tinyDB?.getBoolean("is_reader_rated", false) == true) {
            viewRateDialog(isFinish = false) { isRated ->
                tinyDB?.putBoolean("is_reader_rated", true)
                isOpenInter = true
                if (isRated) {
                    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=$packageName")))
                } else {
                    try {
                        val emailIntent = Intent(Intent.ACTION_SEND)
                        emailIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                        emailIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf("sheldrames@gmail.com"))
                        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Feedback for Daily Notes: Notebook")
                        emailIntent.putExtra(Intent.EXTRA_TEXT, "")
                        emailIntent.setType("message/rfc822")
                        emailIntent.setPackage("com.google.android.gm")
                        startActivity(emailIntent)
                    } catch (ignored: ActivityNotFoundException) {
                        Toast.makeText(this@PDFReaderActivity, "Couldn't find an email app and account", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }

    private var primaryBaseActivity: Context? = null

    override fun attachBaseContext(newBase: Context) {
        primaryBaseActivity = newBase
        val language = newBase.currentLanguage ?: "en"
        val localeToSwitchTo = Locale(language)
        val localeUpdatedContext: ContextWrapper =
            ContextUtils.updateLocale(newBase, localeToSwitchTo)
        super.attachBaseContext(localeUpdatedContext)
    }

    override fun ActivityPdfReaderBinding.initListeners() {
        toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.action_share -> {
                    val file = File(filePath.toString())
                    val uri: Uri = FileProvider.getUriForFile(this@PDFReaderActivity, "${packageName}.provider", file)
                    Intent(Intent.ACTION_SEND).apply {
                        type = file.mimeType
                        putExtra(Intent.EXTRA_STREAM, uri)
                        startActivity(Intent.createChooser(this, null))
                    }
                    return@setOnMenuItemClickListener true
                }

                R.id.action_favorite -> {
                    CoroutineScope(Dispatchers.IO).launch {
                        if (favorite == null) {
                            isFavorite = true
                            favorite = Favorite(fullName = fileName.toString(), filePath = filePath.toString())
                            favorite?.let { favoriteLikeDao.insert(it) }
                        } else {
                            favorite?.let {
                                favorite = null
                                isFavorite = false
                                favoriteLikeDao.delete(it)
                            }
                        }
                        launch(Dispatchers.Main) {
                            toolbar.menu.findItem(R.id.action_favorite).icon = ContextCompat.getDrawable(
                                this@PDFReaderActivity, if (isFavorite) R.drawable.ic_navigation_favorite_active else R.drawable.ic_navigation_favorite_normal
                            )
                        }
                    }
                    return@setOnMenuItemClickListener true
                }

                R.id.action_print -> {
                    printPdfUsingPrintManager(File(filePath.toString()))
                }
            }
            return@setOnMenuItemClickListener false
        }

        actionEdit.setOnClickListener {
            go(EditPdfActivity::class.java, listOf("file_path" to filePath))
        }

        actionTheme.setOnClickListener {
            tinyDB?.putBoolean(NIGHT_MODE, tinyDB?.getBoolean(NIGHT_MODE, false)?.not() == true)
            pdfView.setNightMode(tinyDB?.getBoolean(NIGHT_MODE, false) == true)
            pdfView.redraw()
            updateTheme()
        }
        actionCopy.setOnClickListener {
            val highlighter = PDFHighlighter()
            val text = highlighter.extractTextFromPdf(File(filePath.toString()))
            copyTextToClipboard(text)
        }

        actionFullscreen.setOnClickListener { toggleFullscreen() }

        buttonExit.setOnClickListener { toggleFullscreen() }
        buttonRotate.setOnClickListener { toggleOrientation() }
        buttonRotate.beVisible()
        buttonScreenshot.setOnClickListener {
            val highlighter = PDFHighlighter()
            if (highlighter.capturePdfPage(File(filePath.toString()), pdfView.getCurrentPdfPage())) {
                Toast.makeText(this@PDFReaderActivity, "Screenshot saved..", Toast.LENGTH_SHORT).show()
            }
        }
        buttonHorizontalLock.setOnClickListener {
            isHorizontalLock = !isHorizontalLock
            pdfView.enableSwipeHorizontal(isHorizontalLock)
        }
        buttonZoomLock.setOnClickListener {
            isZoomLock = !isZoomLock
            pdfView.isPinchZoom(isZoomLock)
        }
    }

    private fun toggleOrientation() {
        val newOrientation = if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
            ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        } else {
            ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        }
        requestedOrientation = newOrientation
    }

    private fun copyTextToClipboard(text: String) {
        val copyManager = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
        copyManager.setPrimaryClip(ClipData.newPlainText("text", text))
        Toast.makeText(this, "Copied to clipboard", Toast.LENGTH_SHORT).show()
    }

    private fun printPdfUsingPrintManager(pdfFile: File) {
        if (isFinishing || isDestroyed) {
            return
        }
        if (Looper.myLooper() != Looper.getMainLooper()) {
            runOnUiThread { printPdfUsingPrintManager(pdfFile) }
            return
        }

        try {
            val printManager = primaryBaseActivity?.getSystemService(PRINT_SERVICE) as PrintManager
            val printAdapter = PdfDocumentAdapter(pdfFile)
            val jobName = "${getString(R.string.app_name)} - ${pdfFile.name}"
            printManager.print(jobName, printAdapter, null)
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(primaryBaseActivity, "Printing failed: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    private val pdfCallBack = object : PDFView.Listener {
        override fun onPreparationStarted() {
            binding?.progressHorizontal?.visibility = View.VISIBLE
        }

        override fun onPreparationSuccess() {
        }

        override fun onPreparationFailed(error: String, e: Exception?) {
        }

        override fun onPageChanged(pageIndex: Int, paginationPageIndex: Int) {
            val page = pageIndex + 1
            binding?.apply {
                textPage.text = "$page/${pdfView.pdfFile?.pagesCount}"
            }
        }

        override fun onTextSelected(selection: TextSelectionData, rawPoint: PointF) {}

        override fun hideTextSelectionOptionWindow() {}

        override fun onTextSelectionCleared() {}

        override fun onNotesStampsClicked(comments: List<UnderlineModel>, pointOfNote: PointF) {}

        override fun loadTopPdfChunk(mergeId: Int, pageIndexToLoad: Int) {}

        override fun loadBottomPdfChunk(mergedId: Int, pageIndexToLoad: Int) {}

        override fun onScrolling() {}

        override fun onTap() {}

        override fun onMergeStart(mergeId: Int, mergeType: PdfFile.MergeType) {}

        override fun onMergeEnd(mergeId: Int, mergeType: PdfFile.MergeType) {}

        override fun onMergeFailed(
            mergeId: Int,
            mergeType: PdfFile.MergeType,
            message: String,
            exception: java.lang.Exception?,
        ) {
        }
    }

    private fun ActivityPdfReaderBinding.toggleFullscreen() {
        isFullScreenView = !isFullScreenView
        checkSystemUI()
    }

    private fun ActivityPdfReaderBinding.checkSystemUI() {
        if (isFullScreenView) {
            hideSystemUi()
        } else {
            showSystemUi()
        }
    }

    private fun ActivityPdfReaderBinding.showSystemUi() {
        layoutTopBar.beVisible()
        adBanner.beVisible()
        layoutFullscreenControls.beGone()
    }

    private fun ActivityPdfReaderBinding.hideSystemUi() {
        layoutTopBar.beGone()
        adBanner.beGone()
        layoutFullscreenControls.beVisible()
    }

    override fun ActivityPdfReaderBinding.initView() {
        init()

        if (filePath.isNullOrEmpty() || !File(filePath).exists()) {
            Toast.makeText(this@PDFReaderActivity, "File not access: $filePath", Toast.LENGTH_LONG).show()
            finish()
            return
        }

        pdfView.attachCoroutineScope(pdfRenderScope)
            .setListener(pdfCallBack)
        pdfView
            .fromFile(File(filePath.toString()))
            .defaultPage(0)
            .nightMode(tinyDB?.getBoolean(NIGHT_MODE, false) == true)
            .enableAnnotationRendering(true)
            .autoSpacing(true)
            .spacing(12)
            .pageSnap(true)
            .pageFling(false)
            .onLoadComplete {
                "onLoadComplete".log("onLoadComplete")
                delayed(1000) {
                    PDDocument.load(File(filePath.toString())).use { document ->
                        var pageIndex = 0
                        // Iterate over every page in the document.
                        for (page in document.pages) {
                            page.annotations.forEach { annotation ->
                                val subtype = annotation.subtype
                                if (annotation is PDAnnotationTextMarkup) {
                                    val quadPoints = annotation.quadPoints

                                    if (quadPoints.size >= 8) {
                                        val xValues = listOf(quadPoints[0], quadPoints[2], quadPoints[4], quadPoints[6])
                                        val yValues = listOf(quadPoints[1], quadPoints[3], quadPoints[5], quadPoints[7])
                                        val left = xValues.minOrNull() ?: 0f
                                        val right = xValues.maxOrNull() ?: 0f
                                        val top = yValues.maxOrNull() ?: 0f
                                        val bottom = yValues.minOrNull() ?: 0f
                                        val coordinates = Coordinates(left.toDouble(), top.toDouble(), right.toDouble(), bottom.toDouble())

                                        when (subtype) {
                                            PDAnnotationTextMarkup.SUB_TYPE_HIGHLIGHT ->
                                                pdfView.addHighlightExisting(
                                                    HighlightModel(System.currentTimeMillis(), "", "#FFFF00", pageIndex + 1, System.currentTimeMillis(), coordinates, listOf())
                                                )

                                            PDAnnotationTextMarkup.SUB_TYPE_UNDERLINE ->
                                                pdfView.addUnderlineExisting(UnderlineModel(System.currentTimeMillis(), "", pageIndex + 1, System.currentTimeMillis(), coordinates, listOf()))

                                            PDAnnotationTextMarkup.SUB_TYPE_STRIKEOUT ->
                                                pdfView.addStrikeThroughExisting(StrikeThroughModel(System.currentTimeMillis(), "", pageIndex + 1, System.currentTimeMillis(), coordinates, listOf()))
                                        }
                                    }
                                }
                            }
                        }

                        pageIndex++
                    }

                    binding?.progressHorizontal?.visibility = View.GONE
                    "onLoadComplete".log("onLoadComplete")
                }
            }
            .load()


        toolbar.menu?.findItem(R.id.action_share)?.isVisible = Intent.ACTION_VIEW != intent.action
        toolbar.menu?.findItem(R.id.action_favorite)?.isVisible = Intent.ACTION_VIEW != intent.action
        toolbar.menu?.findItem(R.id.action_print)?.isVisible = Intent.ACTION_VIEW != intent.action
        layoutControls.beVisibleIf(Intent.ACTION_VIEW != intent.action)
        updateTheme()
        viewBanner(adBanner)
        try {
            toolbar.title = fileName ?: ""
        } catch (e: Exception) {
            toolbar.title = ""
        }
        toolbar.setNavigationOnClickListener { onBackPressedDispatcher.onBackPressed() }
        if (Intent.ACTION_VIEW == intent.action) {
            toolbar.setNavigationIcon(R.drawable.ic_icon_cross)
        }
        onBackPressedDispatcher.addCallback(this@PDFReaderActivity, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                proceedToNextLevel()
            }
        })
        if (tinyDB?.getBoolean(IS_DEFAULT_APP_ENABLED, true) == true) {
            viewDefaultSheet {
                tinyDB?.putBoolean(IS_DEFAULT_APP_ENABLED, false)
                if (it) {
                    val intent = Intent(Intent.ACTION_VIEW).apply {
                        setDataAndType(fileUri, File(filePath.toString()).mimeType)
                        flags = Intent.FLAG_ACTIVITY_NO_HISTORY
                    }

                    if (intent.resolveActivity(packageManager) != null) {
                        startActivity(intent)
                    }
                }
            }
        }

        CoroutineScope(Dispatchers.IO).launch {
            favorite = favoriteLikeDao.isFavorite(filePath.toString())
            isFavorite = favorite != null
            launch(Dispatchers.Main) {
                toolbar.menu.findItem(R.id.action_favorite).icon = ContextCompat.getDrawable(
                    this@PDFReaderActivity, if (favorite != null) R.drawable.ic_navigation_favorite_active else R.drawable.ic_navigation_favorite_normal
                )
            }
            recentDao.insert(Recent(fullName = fileName.toString(), filePath = filePath.toString()))
        }
    }

    fun proceedToNextLevel() {
        currentAdLevel++
        if (currentAdLevel % 2 == 0) {
            viewInterAd {
                finish()
            }
        } else {
            finish()
        }
    }

    private fun ActivityPdfReaderBinding.updateTheme() {
        if (tinyDB?.getBoolean(NIGHT_MODE, false) == true) {
            toolbar.menu?.findItem(R.id.action_theme)?.title = getString(R.string.title_light)
            toolbar.overflowIcon = ContextCompat.getDrawable(this@PDFReaderActivity, R.drawable.ic_menu_options_dark)
            val iconColor = ContextCompat.getColor(this@PDFReaderActivity, R.color.colorPrimary)
            val cardColor = ContextCompat.getColor(this@PDFReaderActivity, R.color.colorIcon)
            toolbar.menu?.findItem(R.id.action_share)?.iconTintList = ColorStateList.valueOf(iconColor)
            toolbar.menu?.findItem(R.id.action_favorite)?.iconTintList = ColorStateList.valueOf(iconColor)
            toolbar.menu?.findItem(R.id.action_print)?.iconTintList = ColorStateList.valueOf(iconColor)
            toolbar.setTitleTextColor(iconColor)
            layoutTopBar.setBackgroundColor(ContextCompat.getColor(this@PDFReaderActivity, coder.apps.space.library.R.color.colorBlack))
            toolbar.popupTheme = R.style.Theme_PdfReaderNeo_PopupMenu_Dark
            invalidateOptionsMenu()
            actionEdit.iconTint = ColorStateList.valueOf(iconColor)
            actionFullscreen.iconTint = ColorStateList.valueOf(iconColor)
            actionCopy.iconTint = ColorStateList.valueOf(iconColor)
            actionTheme.iconTint = ColorStateList.valueOf(iconColor)

            buttonExit.iconTint = ColorStateList.valueOf(iconColor)
            buttonExit.backgroundTintList = ColorStateList.valueOf(cardColor)
            buttonRotate.iconTint = ColorStateList.valueOf(iconColor)
            buttonRotate.backgroundTintList = ColorStateList.valueOf(cardColor)
            buttonHorizontalLock.iconTint = ColorStateList.valueOf(iconColor)
            buttonHorizontalLock.backgroundTintList = ColorStateList.valueOf(cardColor)
            buttonZoomLock.iconTint = ColorStateList.valueOf(iconColor)
            buttonZoomLock.backgroundTintList = ColorStateList.valueOf(cardColor)
            buttonScreenshot.iconTint = ColorStateList.valueOf(iconColor)
            buttonScreenshot.backgroundTintList = ColorStateList.valueOf(cardColor)
            updateNavigationBarColor(coder.apps.space.library.R.color.colorBlack)
            updateStatusBarColor(coder.apps.space.library.R.color.colorBlack)
            updateWindows(isLight = false)
        } else {
            toolbar.menu?.findItem(R.id.action_theme)?.title = getString(R.string.title_night)
            toolbar.overflowIcon = ContextCompat.getDrawable(this@PDFReaderActivity, R.drawable.ic_menu_options)
            val iconColor = ContextCompat.getColor(this@PDFReaderActivity, R.color.colorIcon)
            val cardColor = ContextCompat.getColor(this@PDFReaderActivity, R.color.colorCardBackground)
            toolbar.menu?.findItem(R.id.action_share)?.iconTintList = ColorStateList.valueOf(iconColor)
            toolbar.menu?.findItem(R.id.action_favorite)?.iconTintList = ColorStateList.valueOf(iconColor)
            toolbar.menu?.findItem(R.id.action_print)?.iconTintList = ColorStateList.valueOf(iconColor)
            toolbar.setTitleTextColor(iconColor)
            layoutTopBar.setBackgroundColor(ContextCompat.getColor(this@PDFReaderActivity, coder.apps.space.library.R.color.colorPrimary))
            toolbar.popupTheme = R.style.Theme_PdfReaderNeo_PopupMenu_Light
            invalidateOptionsMenu()

            actionEdit.iconTint = ColorStateList.valueOf(iconColor)
            actionFullscreen.iconTint = ColorStateList.valueOf(iconColor)
            actionCopy.iconTint = ColorStateList.valueOf(iconColor)
            actionTheme.iconTint = ColorStateList.valueOf(iconColor)

            buttonExit.iconTint = ColorStateList.valueOf(iconColor)
            buttonExit.backgroundTintList = ColorStateList.valueOf(cardColor)
            buttonRotate.iconTint = ColorStateList.valueOf(iconColor)
            buttonRotate.backgroundTintList = ColorStateList.valueOf(cardColor)
            buttonHorizontalLock.iconTint = ColorStateList.valueOf(iconColor)
            buttonHorizontalLock.backgroundTintList = ColorStateList.valueOf(cardColor)
            buttonZoomLock.iconTint = ColorStateList.valueOf(iconColor)
            buttonZoomLock.backgroundTintList = ColorStateList.valueOf(cardColor)
            buttonScreenshot.iconTint = ColorStateList.valueOf(iconColor)
            buttonScreenshot.backgroundTintList = ColorStateList.valueOf(cardColor)

            updateNavigationBarColor(coder.apps.space.library.R.color.colorBackground)
            updateStatusBarColor(coder.apps.space.library.R.color.colorBackground)
            updateWindows(isLight = true)
        }
    }

    private fun updateWindows(isLight: Boolean) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            WindowInsetsControllerCompat(window, window.decorView).apply {
                isAppearanceLightStatusBars = isLight
                isAppearanceLightNavigationBars = isLight
            }
        } else {
            val flags = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR or View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
            if (isLight) window.decorView.systemUiVisibility = window.decorView.systemUiVisibility or flags
            else window.decorView.systemUiVisibility = (window.decorView.systemUiVisibility.inv() or flags).inv()
        }
    }

    private fun init() {
        if (Intent.ACTION_VIEW == intent.action) {
            val uri: Uri? = intent.data
            if (uri != null && !isDestroyed && !isFinishing && applicationContext != null) {
                fileUri = uri
                var path = getPathFromURI(this@PDFReaderActivity, uri)
                var name: String? = ""
                if (path != null && File(path).exists()) {
                    if (path.isEmpty() && !intent.dataString.isNullOrEmpty()) {
                        path = intent.dataString
                        val indexOf = path?.indexOf(":")
                        if (indexOf != null && indexOf > 0) {
                            path = path?.substring(indexOf + 3)
                        }
                        path = Uri.decode(path)
                    }

                    if (path?.contains("/raw:") == true) {
                        path = path.substringAfter("/raw:")
                    }
                    filePath = path

                    if (name.isNullOrEmpty()) {
                        name = path?.substringAfterLast(File.separator) ?: uri.path?.substringAfterLast(File.separator)
                    }
                    fileName = name
                    invalidateOptionsMenu()
                    if (path.isNullOrEmpty() || (path?.endsWith(".pdf")?.not() == true && intent.type != "application/pdf")) {
                        Toast.makeText(this@PDFReaderActivity, "Failed to load file", Toast.LENGTH_SHORT).show()
                        return
                    }
                } else finish()
            }
        } else {
            filePath = intent.getStringExtra(FILE_PATH) // fileUri = intent.extras?.get(FILE_URI) as Uri?
            fileName = intent.getStringExtra(FILE_NAME)
            fileUri = FileProvider.getUriForFile(this@PDFReaderActivity, "${packageName}.provider", File(filePath.toString()))
            if (filePath?.let { File(it).exists() } == true) {
                if (TextUtils.isEmpty(filePath)) {
                    filePath = intent.dataString
                    val indexOf: Int = filePath?.indexOf(":") ?: 0
                    if (indexOf > 0) {
                        filePath = filePath?.substring(indexOf + 3)
                    }
                    filePath = Uri.decode(filePath)
                }
                if (!TextUtils.isEmpty(filePath) && filePath?.contains("/raw:") == true) {
                    filePath = filePath?.substring((filePath?.indexOf("/raw:") ?: 0) + 5)
                }
                if (TextUtils.isEmpty(fileName)) {
                    val lastIndexOf = filePath?.lastIndexOf(File.separator) ?: 0
                    fileName = if (lastIndexOf > 0) {
                        filePath?.substring(lastIndexOf + 1)
                    } else {
                        filePath
                    }
                }
            } else finish()
        }
    }
}