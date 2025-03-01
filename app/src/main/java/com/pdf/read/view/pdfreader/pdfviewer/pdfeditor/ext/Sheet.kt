package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.ext

import android.app.*
import android.graphics.*
import android.graphics.drawable.*
import android.view.*
import android.widget.*
import androidx.core.content.*
import coder.apps.space.library.*
import coder.apps.space.library.extension.*
import com.google.android.material.bottomsheet.*
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.database.*
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.databinding.*
import kotlinx.coroutines.*
import java.io.*

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

fun Activity.viewOptionSheet(isAll: Boolean? = true,file: File, listener: (Option) -> Unit) {
    val dialog = BottomSheetDialog(this, R.style.Theme_Space_BottomSheetDialogTheme)
    val binding = LayoutSheetOptionsBinding.inflate(layoutInflater)
    dialog.setContentView(binding.root)
    dialog.window?.apply {
        applyDialogConfig()
    }

    binding.apply {
        CoroutineScope(Dispatchers.IO).launch {
            val favorite = favoriteDao.isFavorite(file.absolutePath.toString())
            if (favorite != null) {
                buttonFavorite.icon = ContextCompat.getDrawable(this@viewOptionSheet, com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.R.drawable.ic_navigation_favorite_normal)
                buttonFavorite.text = getString(com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.R.string.menu_remove_favorite)
            } else {
                buttonFavorite.icon = ContextCompat.getDrawable(this@viewOptionSheet, com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.R.drawable.ic_navigation_favorite_active)
                buttonFavorite.text = getString(com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.R.string.menu_add_favorite)
            }
        }
        if (isAll == false){
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

fun Activity.viewSaveSheet(title: String, listener: (action: Int, value: String) -> Unit) {
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