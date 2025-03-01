package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.activities.viewer

import android.app.*
import android.content.*
import android.graphics.*
import android.net.*
import android.os.*
import android.text.*
import android.view.*
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.*
import androidx.activity.*
import androidx.core.content.*
import coder.apps.space.library.base.*
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.R
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.admodule.*
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.database.*
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.database.table.*
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.databinding.*
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.ext.*
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.common.*
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.constant.*
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.res.ResKit.instance
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.system.*
import kotlinx.coroutines.*
import java.io.*

class ReaderActivity : BaseActivity<ActivityReaderBinding>(ActivityReaderBinding::inflate), IMainFrame {
    private var favorite: Favorite? = null
    private var isFavorite: Boolean = false
    private var control: MainControl? = null
    private var fileName: String? = null
    private var filePath: String? = null
    private var fileUri: Uri? = null
    private var tempFilePath: String? = null
    private var writeLog = true
    private var isThumb = false
    private val bg: Any = -3355444
    private var isDispose = false

    override fun ActivityReaderBinding.initExtra() {
    }

    override fun ActivityReaderBinding.initListeners() {

    }

    override fun ActivityReaderBinding.initView() {
        control = MainControl(this@ReaderActivity)
        layoutContainer.post {
            init()
            CoroutineScope(Dispatchers.IO).launch {
                favorite = favoriteDao.isFavorite(filePath.toString())
                isFavorite = favorite != null
                launch(Dispatchers.Main) {
                    toolbar.menu.findItem(R.id.action_favorite).icon = ContextCompat.getDrawable(this@ReaderActivity, if (favorite != null) R.drawable.ic_navigation_favorite_active else R.drawable.ic_navigation_favorite_normal)
                }
                recentDao.insert(Recent(fullName = fileName.toString(), filePath = filePath.toString()))
            }
        }
        control?.setOffictToPicture(object : IOfficeToPicture {
            private var bitmap: Bitmap? = null
            override fun getModeType(): Byte {
                return 1
            }

            override fun setModeType(modeType: Byte) {

            }

            override fun getBitmap(visibleWidth: Int, visibleHeight: Int): Bitmap? {
                if (visibleWidth == 0 || visibleHeight == 0) {
                    return null
                }
                if (!(bitmap != null && bitmap?.width == visibleWidth && bitmap?.height == visibleHeight)) {
                    if (bitmap != null) {
                        bitmap?.recycle()
                    }
                    this.bitmap = Bitmap.createBitmap(visibleWidth, visibleHeight, Bitmap.Config.ARGB_8888)
                }
                return this.bitmap
            }

            override fun callBack(bitmap: Bitmap?) {
                saveBitmapToFile(bitmap)
            }

            override fun isZoom(): Boolean {
                return false
            }

            override fun dispose() {

            }
        })
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

    private fun ActivityReaderBinding.init() {
        val intent = intent
        if (Intent.ACTION_VIEW == intent.action) {
            val uri: Uri? = intent.data
            if (uri != null && !isDestroyed && !isFinishing && applicationContext != null) {
                fileUri = uri
                var path: String? = getPathFromURI(this@ReaderActivity, uri)
                var name: String? = ""

                if (path.isNullOrEmpty() && !intent.dataString.isNullOrEmpty()) {
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
                if (path.isNullOrEmpty()) {
                    Toast.makeText(this@ReaderActivity, "Failed to load file", Toast.LENGTH_SHORT).show()
                    return
                }
            }
        } else {
            fileUri = intent.extras?.get(FILE_URI) as Uri?
            filePath = intent.getStringExtra(FILE_PATH)
            fileName = intent.getStringExtra(FILE_NAME)
        }
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
        toolbar.title = fileName
        setSupportActionBar(toolbar)
        toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
        if (Intent.ACTION_VIEW == intent.action) {
            toolbar.setNavigationIcon(R.drawable.ic_icon_cross)
        }
        if (tinyDB?.getBoolean(IS_DEFAULT_APP_ENABLED, true) == true) {
            viewDefaultSheet {
                tinyDB?.putBoolean(IS_DEFAULT_APP_ENABLED, false)
                if (it) {
                    Intent(Intent.ACTION_VIEW).apply {
                        setDataAndType(fileUri, File(filePath.toString()).mimeType)
                        flags = Intent.FLAG_ACTIVITY_NO_HISTORY
                    }.apply {
                        if (resolveActivity(packageManager) != null) {
                            startActivity(this)
                        }
                    }
                }
            }
        }
        onBackPressedDispatcher.addCallback(this@ReaderActivity, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val actionValue = control?.getActionValue(EventConstant.PG_SLIDESHOW, null)
                if (actionValue == null || !(actionValue as Boolean)) {
                    if (control?.reader != null) {
                        control?.reader?.abortReader()
                    }
                    if (control == null || control?.isAutoTest?.not() == true) {
                        proceedToNextLevel()
                        return
                    }
                    return
                }
                fullScreen(false)
                control?.actionEvent(EventConstant.PG_SLIDESHOW_END, null)
            }
        })
        CoroutineScope(Dispatchers.IO).launch {
            control?.openFile(filePath)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_reader, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_share -> {
                val file = File(filePath.toString())
                val uri: Uri = FileProvider.getUriForFile(this@ReaderActivity, "${packageName}.provider", file)
                Intent(Intent.ACTION_SEND).apply {
                    type = file.mimeType
                    putExtra(Intent.EXTRA_STREAM, uri)
                    startActivity(Intent.createChooser(this, null))
                }
                return true
            }

            R.id.action_favorite -> {
                CoroutineScope(Dispatchers.IO).launch {
                    if (favorite == null) {
                        isFavorite = true
                        favoriteDao.insert(Favorite(fullName = fileName.toString(), filePath = filePath.toString()))
                    } else {
                        favorite?.let {
                            favorite = null
                            isFavorite = false
                            favoriteDao.delete(it)
                        }
                    }
                    launch(Dispatchers.Main) {
                        binding?.toolbar?.menu?.findItem(R.id.action_favorite)?.icon = ContextCompat.getDrawable(this@ReaderActivity, if (isFavorite) R.drawable.ic_navigation_favorite_active else R.drawable.ic_navigation_favorite_normal)
                    }
                }
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        menu?.findItem(R.id.action_share)?.setVisible(Intent.ACTION_VIEW != intent.action)
        menu?.findItem(R.id.action_favorite)?.setVisible(Intent.ACTION_VIEW != intent.action)
        return super.onPrepareOptionsMenu(menu)
    }

    private fun saveBitmapToFile(bitmap: Bitmap?) {
        if (bitmap == null) {
            return
        }

        if (tempFilePath == null) {
            val state = Environment.getExternalStorageState()
            if (Environment.MEDIA_MOUNTED == state) {
                tempFilePath = defaultStorageLocation
            }
            val file = File(tempFilePath, "tempPic")
            if (!file.exists()) {
                file.mkdir()
            }
            tempFilePath = file.absolutePath
        }
        createOrExistsDir(File(tempFilePath.toString()))
        val file = File(tempFilePath + File.separatorChar + "export_image.jpg")
        try {
            if (file.exists()) {
                file.delete()
            }
            file.createNewFile()
            val fOut = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut)
            fOut.flush()
            fOut.close()
        } catch (e: IOException) {
        }
    }

    override fun getActivity(): Activity {
        return this
    }

    override fun doActionEvent(actionID: Int, obj: Any?): Boolean {
        return false
    }

    override fun openFileFinish() {
        val gapView = View(applicationContext)
        gapView.setBackgroundColor(ContextCompat.getColor(this, R.color.colorBackground))
        binding?.apply {
            layoutContainer.addView(gapView, LinearLayout.LayoutParams(MATCH_PARENT, 1))
            layoutContainer.addView(control?.view, LinearLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT))
        }
    }

    override fun updateToolsbarStatus() {

    }

    override fun setFindBackForwardState(state: Boolean) {

    }

    override fun getBottomBarHeight(): Int {
        return 0
    }

    override fun getTopBarHeight(): Int {
        return 0
    }

    override fun getAppName(): String {
        return getString(R.string.app_name)
    }

    override fun getTemporaryDirectory(): File {
        val externalFilesDir = getExternalFilesDir(null)
        if (externalFilesDir != null) {
            return externalFilesDir
        }
        return filesDir
    }

    override fun onEventMethod(v: View?, e1: MotionEvent?, e2: MotionEvent?, xValue: Float, yValue: Float, eventMethodType: Byte): Boolean {
        return false
    }

    override fun isDrawPageNumber(): Boolean {
        return true
    }

    override fun isShowZoomingMsg(): Boolean {
        return true
    }

    override fun isPopUpErrorDlg(): Boolean {
        return true
    }

    override fun isShowPasswordDlg(): Boolean {
        return true
    }

    override fun isShowProgressBar(): Boolean {
        return true
    }

    override fun isShowFindDlg(): Boolean {
        return true
    }

    override fun isShowTXTEncodeDlg(): Boolean {
        return true
    }

    override fun getTXTDefaultEncode(): String {
        return "GBK"
    }

    override fun isTouchZoom(): Boolean {
        return true
    }

    override fun isZoomAfterLayoutForWord(): Boolean {
        return true
    }

    override fun getWordDefaultView(): Byte {
        return 0
    }

    override fun getLocalString(resName: String?): String {
        return instance().getLocalString(resName)
    }

    override fun changeZoom() {

    }

    override fun changePage() {

    }

    override fun completeLayout() {

    }

    override fun error(errorCode: Int) {

    }

    override fun fullScreen(fullscreen: Boolean) {

    }

    override fun showProgressBar(visible: Boolean) {
        setProgressBarIndeterminateVisibility(visible)
    }

    override fun updateViewImages(viewList: MutableList<Int>?) {

    }

    override fun isChangePage(): Boolean {
        return true
    }

    override fun isWriteLog(): Boolean {
        return writeLog
    }

    override fun setWriteLog(saveLog: Boolean) {
        writeLog = saveLog
    }

    override fun isThumbnail(): Boolean {
        return isThumb
    }

    override fun setThumbnail(isThumbnail: Boolean) {
        isThumb = isThumbnail
    }

    override fun getViewBackground(): Any {
        return bg
    }

    override fun isIgnoreOriginalSize(): Boolean {
        return false
    }

    override fun setIgnoreOriginalSize(ignoreOriginalSize: Boolean) {

    }

    override fun getPageListViewMovingPosition(): Byte {
        return 0
    }

    override fun dispose() {
        isDispose = true
        if (control != null) {
            control?.dispose()
            control = null
        }
    }

    override fun onDestroy() {
        dispose()
        super.onDestroy()
    }
}