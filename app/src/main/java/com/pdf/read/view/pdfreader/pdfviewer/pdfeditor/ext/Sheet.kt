package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.ext

import android.app.*
import android.graphics.*
import android.graphics.drawable.*
import android.os.*
import android.view.*
import android.widget.*
import androidx.core.content.*
import androidx.core.view.*
import androidx.core.widget.*
import coder.apps.space.library.*
import coder.apps.space.library.extension.*
import coder.apps.space.library.helper.*
import com.google.android.material.bottomsheet.*
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.database.*
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.databinding.*
import kotlinx.coroutines.*
import java.io.*
import androidx.core.graphics.drawable.toDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

fun Activity.showDocumentReaderWarn(message: String, listener: () -> Unit?): BottomSheetDialog {
    val dialog = BottomSheetDialog(this, R.style.Theme_Space_BottomSheetDialogTheme)
    val bind = LayoutDialogDocumentReaderWarnBinding.inflate(layoutInflater)
    dialog.setContentView(bind.root)
    dialog.setCancelable(false)

    bind.apply {
        textWarn.text = message
        buttonPositive.setOnClickListener {
            listener.invoke()
        }
    }
    dialog.window?.applyDialogConfig()
    return dialog
}

fun Activity.showProgressDialogLoad(): Dialog {
    val dialog = Dialog(this, com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.R.style.Theme_PdfReaderNeo_Dialog)
    dialog.setCancelable(false)
    dialog.setContentView(com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.R.layout.layout_dialog_progress_load)
    dialog.window?.apply {
        setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        statusBarColor = getColor(R.color.colorBlack34)
        navigationBarColor = getColor(R.color.colorBlack)
        setBackgroundDrawable(getColor(R.color.colorBlack).toDrawable())
        setDimAmount(0.0F)
        dialog.window?.apply {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                WindowInsetsControllerCompat(this, decorView).apply { isAppearanceLightStatusBars = false }
            } else {
                window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            }
        }
    }
    try {
        if (!isFinishing) {
            dialog.show()
        }
    } catch (e: java.lang.Exception) {
        e.printStackTrace()
    }
    return dialog
}

fun Activity.discardDialog(listener: () -> Unit?) {
    val dialog = BottomSheetDialog(this, R.style.Theme_Space_BottomSheetDialogTheme)
    val bind = LayoutDialogDiscardBinding.inflate(layoutInflater)
    dialog.setContentView(bind.root)

    bind.apply {
        buttonDiscard.setOnClickListener {
            dialog.dismiss()
            listener.invoke()
        }
        buttonCancel.setOnClickListener {
            dialog.dismiss()
        }
    }
    dialog.window?.apply {
        applyDialogConfig()
    }
    dialog.window?.apply {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            WindowInsetsControllerCompat(this, decorView).apply { isAppearanceLightStatusBars = false }
        } else {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }
    }
    if (!isFinishing) dialog.show()
}

fun Activity.showProgressDialog(): Dialog {
    val dialog = Dialog(this, com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.R.style.Theme_PdfReaderNeo_Dialog)
    val bind = LayoutDialogProgressBinding.inflate(layoutInflater)
    dialog.setContentView(bind.root)
    dialog.setCancelable(false)
    dialog.apply {
        val params = window?.attributes
        params?.width = resources.getDimension(com.intuit.sdp.R.dimen._100sdp).toInt()
        params?.height = resources.getDimension(com.intuit.sdp.R.dimen._100sdp).toInt()
        params?.gravity = Gravity.CENTER
        window?.attributes = params
        window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)

        window?.setDimAmount(.34f)
        window?.navigationBarColor = ContextCompat.getColor(this@showProgressDialog, R.color.colorBlack)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }
    return dialog
}

fun Activity.showOtherPermissionDialog(listener: () -> Unit?) {
    val dialog = Dialog(this@showOtherPermissionDialog, R.style.Theme_Space_Dialog)
    val bind = LayoutDialogCameraPermissionBinding.inflate(layoutInflater)
    dialog.setContentView(bind.root)
    dialog.setCancelable(false)
    dialog.setCanceledOnTouchOutside(false)
    dialog.apply {
        val params = window?.attributes
        params?.width = resources.getDimension(com.intuit.sdp.R.dimen._260sdp).toInt()
        params?.height = WindowManager.LayoutParams.WRAP_CONTENT
        params?.gravity = Gravity.CENTER
        window?.attributes = params
        window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)

        window?.setDimAmount(.34f)
        window?.navigationBarColor = ContextCompat.getColor(context, R.color.colorBlack)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }
    bind.apply {
        buttonPositive.setOnClickListener {
            dialog.dismiss()
            listener.invoke()
        }
    }

    if (!isFinishing) dialog.show()
}

fun Activity.viewDocumentOption(isDocScanner: Boolean? = false, listener: (Int) -> Unit) {
    val dialog = BottomSheetDialog(this, R.style.Theme_Space_BottomSheetDialogTheme)
    val bind = LayoutDialogDocumentOptionBinding.inflate(layoutInflater)
    dialog.setContentView(bind.root)

    bind.apply {
        menuShare.beGoneIf(isDocScanner == true)
        menuInfo.beGoneIf(isDocScanner == true)
        buttonShareJpg.beVisibleIf(isDocScanner == true)
        buttonSharePdf.beVisibleIf(isDocScanner == true)
        menuShare.setOnClickListener {
            dialog.dismiss()
            listener.invoke(1)
        }
        menuRename.setOnClickListener {
            dialog.dismiss()
            listener.invoke(2)
        }
        menuInfo.setOnClickListener {
            dialog.dismiss()
            listener.invoke(3)
        }
        menuDelete.setOnClickListener {
            dialog.dismiss()
            listener.invoke(4)
        }
        buttonShareJpg.setOnClickListener {
            dialog.dismiss()
            listener.invoke(5)
        }
        buttonSharePdf.setOnClickListener {
            dialog.dismiss()
            listener.invoke(6)
        }
    }
    dialog.window?.apply {
        applyDialogConfig()
    }
    if (!isFinishing) dialog.show()
}

fun Activity.viewOptionSheet(isAll: Boolean? = true, file: File, listener: (Option) -> Unit) {
    val dialog = BottomSheetDialog(this, R.style.Theme_Space_BottomSheetDialogTheme)
    val binding = LayoutSheetOptionsBinding.inflate(layoutInflater)
    dialog.setContentView(binding.root)
    dialog.window?.apply {
        applyDialogConfig()
    }

    binding.apply {
        CoroutineScope(Dispatchers.IO).launch {
            val favorite = favoriteLikeDao.isFavorite(file.absolutePath.toString())
            launch(Dispatchers.Main) {
                if (favorite != null) {
                    buttonFavorite.icon = ContextCompat.getDrawable(this@viewOptionSheet, com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.R.drawable.ic_navigation_favorite_normal)
                    buttonFavorite.text = getString(com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.R.string.menu_remove_favorite)
                } else {
                    buttonFavorite.icon = ContextCompat.getDrawable(this@viewOptionSheet, com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.R.drawable.ic_navigation_favorite_active)
                    buttonFavorite.text = getString(com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.R.string.menu_add_favorite)
                }
            }
        }
        if (isAll == false) {
            buttonRename.beGone()
            buttonDeletePermanently.beGone()
        }
        buttonFavorite.setOnClickListener {
            listener(Option.FAVORITE)
            dialog.dismiss()
        }
        buttonShare.setOnClickListener {
            listener(Option.SHARE)
            dialog.dismiss()
        }
        buttonRename.setOnClickListener {
            listener(Option.RENAME)
            dialog.dismiss()
        }
        buttonDeletePermanently.setOnClickListener {
            listener(Option.DELETE)
            dialog.dismiss()
        }
    }

    if (!isFinishing) dialog.show()
}

fun Activity.viewTrashOrDeleteSheet(listener: () -> Unit) {
    val dialog = BottomSheetDialog(this, R.style.Theme_Space_BottomSheetDialogTheme)
    val binding = LayoutSheetDeleteBinding.inflate(layoutInflater)
    dialog.setContentView(binding.root)
    dialog.window?.apply {
        applyDialogConfig()
    }
    binding.apply {
        buttonRemove.setOnClickListener {
            dialog.dismiss()
            listener.invoke()
        }
        buttonCancel.setOnClickListener {
            dialog.dismiss()
        }
    }
    if (!isFinishing) dialog.show()
}

fun Activity.viewSavedDocumentOption(viewDir: File?, thumb: String, type: String, listener: (Int) -> Unit) {
    val dialog = BottomSheetDialog(this, R.style.Theme_Space_BottomSheetDialogTheme)
    val bind = LayoutDialogScannedMoreBinding.inflate(layoutInflater)
    dialog.setContentView(bind.root)

    bind.apply {
        layoutOptionMore.beVisibleIf(type == "MORE")
        layoutOptionShare.beVisibleIf(type == "SHARE")
        Glide.with(iconFile.context).load(thumb).thumbnail(0.1f).apply(RequestOptions().centerCrop().skipMemoryCache(false).dontTransform().dontAnimate()).into(iconFile)
        textFileTitle.text = viewDir?.name ?: ""
        textFileCreation.text = (viewDir?.lastModified() ?: 0L).fromMilliToDate()
        buttonExportPdf.setOnClickListener {
            dialog.dismiss()
            listener.invoke(1)
        }
        buttonExportJpg.setOnClickListener {
            dialog.dismiss()
            listener.invoke(2)
        }
        buttonSharePdf.setOnClickListener {
            dialog.dismiss()
            listener.invoke(4)
        }
        buttonShareJpg.setOnClickListener {
            dialog.dismiss()
            listener.invoke(5)
        }
    }
    dialog.window?.apply {
        applyDialogConfig()
    }
    if (!isFinishing) dialog.show()
}

fun Activity.viewProgressSheet(): BottomSheetDialog {
    val dialog = BottomSheetDialog(this, R.style.Theme_Space_BottomSheetDialogTheme)
    val binding = LayoutSheetProgressBinding.inflate(layoutInflater)
    dialog.setContentView(binding.root)
    dialog.setCancelable(false)
    dialog.window?.apply {
        applyDialogConfig()
    }
    dialog.setCanceledOnTouchOutside(false)
    return dialog
}

fun Activity.viewRenameSheet(title: String, listener: (String) -> Unit) {
    val dialog = BottomSheetDialog(this, R.style.Theme_Space_BottomSheetDialogTheme)
    val binding = LayoutSheetRenameBinding.inflate(layoutInflater)
    dialog.setContentView(binding.root)
    dialog.setCancelable(false)
    dialog.window?.apply {
        applyDialogConfig()
    }
    dialog.setCanceledOnTouchOutside(false)
    binding.apply {
        editTitle.setText(title)
        buttonRename.setOnClickListener {
            val text = editTitle.text.toString()
            if (text.isNotEmpty()) {
                dialog.dismiss()
                listener.invoke(text)
            } else {
                Toast.makeText(this@viewRenameSheet, "File name not be empty", Toast.LENGTH_SHORT).show()
            }
        }
        buttonCancel.setOnClickListener {
            dialog.dismiss()
        }
    }
    if (!isFinishing) dialog.show()
}

fun Activity.viewSaveSheet(title: String, isDiscardEnable: Boolean, listener: (action: Int, value: String) -> Unit) {
    val dialog = BottomSheetDialog(this, R.style.Theme_Space_BottomSheetDialogTheme)
    val binding = LayoutSheetSaveAsBinding.inflate(layoutInflater)
    dialog.setContentView(binding.root)
    dialog.setCancelable(false)
    dialog.window?.apply {
        applyDialogConfig()
    }
    dialog.setCanceledOnTouchOutside(false)
    binding.apply {
        editTitle.setText(title)
        buttonSave.setOnClickListener {
            val text = editTitle.text.toString()
            if (text.isNotEmpty()) {
                dialog.dismiss()
                listener.invoke(1, text)
            } else {
                Toast.makeText(this@viewSaveSheet, "File name not be empty", Toast.LENGTH_SHORT).show()
            }
        }
        buttonDiscard.beVisibleIf(isDiscardEnable)
        buttonDiscard.setOnClickListener {
            dialog.dismiss()
            listener.invoke(2, "")
        }
        buttonCancel.setOnClickListener {
            dialog.dismiss()
            listener.invoke(3, "")
        }
    }
    if (!isFinishing) dialog.show()
}

fun Activity.viewDefaultSheet(listener: (Boolean) -> Unit) {
    val dialog = BottomSheetDialog(this, R.style.Theme_Space_BottomSheetDialogTheme)
    val binding = LayoutSheetDefaultAppBinding.inflate(layoutInflater)
    dialog.setContentView(binding.root)
    dialog.setCancelable(false)
    dialog.window?.apply {
        applyDialogConfig()
    }
    binding.apply {
        buttonPositive.setOnClickListener {
            dialog.dismiss()
            listener.invoke(true)
        }
        buttonNegative.setOnClickListener {
            dialog.dismiss()
            listener.invoke(false)
        }
    }
    if (!isFinishing) dialog.show()
}

fun Activity.viewSortDialog(listener: (Boolean) -> Unit) {
    val dialog = BottomSheetDialog(this, R.style.Theme_Space_BottomSheetDialogTheme)
    val bindDialog = LayoutSheetSortBinding.inflate(layoutInflater)
    dialog.setContentView(bindDialog.root)
    dialog.window?.apply {
        applyDialogConfig()
    }
    val tinyDB = TinyDB(this)
    var selectedSort = tinyDB.getString(SORT_BY, SORT_MODIFIED)
    var selectedOrder = tinyDB.getString(SORT_ORDER, SORT_DESCENDING)
    bindDialog.apply {
        fun updateSort() {
            sortDateModified.isSelected = selectedSort == SORT_MODIFIED
            sortName.isSelected = selectedSort == SORT_NAME
            sortSize.isSelected = selectedSort == SORT_SIZE

            sortAscending.isSelected = selectedOrder == SORT_ASCENDING
            sortDescending.isSelected = selectedOrder == SORT_DESCENDING
        }
        updateSort()
        sortDateModified.setOnClickListener {
            selectedSort = SORT_MODIFIED
            updateSort()
        }
        sortName.setOnClickListener {
            selectedSort = SORT_NAME
            updateSort()
        }
        sortSize.setOnClickListener {
            selectedSort = SORT_SIZE
            updateSort()
        }

        sortAscending.setOnClickListener {
            selectedOrder = SORT_ASCENDING
            updateSort()
        }
        sortDescending.setOnClickListener {
            selectedOrder = SORT_DESCENDING
            updateSort()
        }

        buttonPositive.setOnClickListener {
            tinyDB.putString(SORT_BY, selectedSort)
            tinyDB.putString(SORT_ORDER, selectedOrder)
            listener.invoke(true)
            dialog.dismiss()
        }
        buttonNegative.setOnClickListener {
            dialog.dismiss()
        }
    }
    if (!isFinishing) dialog.show()
}

fun Activity.viewRateDialog(isFinish: Boolean, listener: (Boolean) -> Unit) {
    var isRating = false
    val dialog = BottomSheetDialog(this, R.style.Theme_Space_BottomSheetDialogTheme)
    val bindDialog: LayoutSheetRateBinding = LayoutSheetRateBinding.inflate(layoutInflater)
    dialog.setContentView(bindDialog.root)
    dialog.window?.apply {
        val params = attributes
        params?.width = WindowManager.LayoutParams.MATCH_PARENT
        params?.height = WindowManager.LayoutParams.WRAP_CONTENT
        params?.gravity = Gravity.CENTER
        attributes = params
        setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)

        setDimAmount(.50f)
        setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        setDimAmount(.24f)
    }

    bindDialog.apply {
        ratingBar.setOnRatingChangeListener { ratingBar, rating, fromUser ->
            isRating = true
        }
        buttonReview.setOnClickListener {
            if (isRating) {
                dialog.dismiss()
                listener.invoke(ratingBar.rating >= 4F)
            } else {
                Toast.makeText(this@viewRateDialog, "Please rate us", Toast.LENGTH_SHORT).show()
            }
        }
        buttonCancel.setOnClickListener {
            dialog.dismiss()
            if (isFinish) finishAffinity()
        }
    }

    if (!isFinishing || !isDestroyed) {
        dialog.show()
    }
}

fun Activity.viewCreateDialog(listener: (String) -> Unit): BottomSheetDialog {
    val dialog = BottomSheetDialog(this, R.style.Theme_Space_BottomSheetDialogTheme)
    val bindDialog: LayoutSheetDocumentCreateBinding = LayoutSheetDocumentCreateBinding.inflate(layoutInflater)
    dialog.setContentView(bindDialog.root)
    bindDialog.buttonPositive.setOnClickListener {
        dialog.dismiss()
        val text = bindDialog.editFileName.text.toString()
        if (text.isNotEmpty()) {
            listener.invoke(text)
        } else {
            bindDialog.inputFileName.isErrorEnabled = true
            bindDialog.inputFileName.error = getString(com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.R.string.message_rename_validation)
        }
    }
    bindDialog.buttonPositive.beEnableIf(bindDialog.editFileName.text?.isNotEmpty() == true)
    bindDialog.editFileName.doOnTextChanged { text, start, before, count ->
        if (text?.isNotEmpty() == true) {
            bindDialog.inputFileName.isErrorEnabled = false
            bindDialog.inputFileName.error = ""
        }
        bindDialog.buttonPositive.beEnableIf(text?.trim()?.isNotEmpty() == true)
    }

    dialog.window?.apply {
        applyDialogConfig()
    }
    if (!isFinishing || !isDestroyed) {
        dialog.show()
    }

    return dialog
}

fun Activity.showProcessingDialog(): BottomSheetDialog {
    val dialog = BottomSheetDialog(this, R.style.Theme_Space_BottomSheetDialogTheme)
    val bindDialog: LayoutSheetProcessingBinding = LayoutSheetProcessingBinding.inflate(layoutInflater)
    dialog.setContentView(bindDialog.root)
    dialog.setCancelable(false)
    dialog.window?.apply {
        applyDialogConfig()
    }
    dialog.setCanceledOnTouchOutside(false)
    dialog.window?.apply {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            WindowInsetsControllerCompat(this, decorView).apply { isAppearanceLightStatusBars = false }
        } else {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }
    }
    return dialog
}