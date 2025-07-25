package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.fragments

import android.os.*
import coder.apps.space.library.base.*
import coder.apps.space.library.extension.*
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.activities.tools.converter.ExcelToPdfActivity
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.activities.tools.converter.ImageToPdfActivity
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.activities.tools.converter.TextToPdfActivity
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.activities.tools.converter.WordToPdfActivity
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.activities.tools.modify.CompressActivity
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.activities.tools.modify.MergeActivity
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.activities.tools.modify.SplitActivity
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.activities.tools.scanner.ListDocScannedActivity
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.databinding.*

class ToolFragment : BaseFragment<FragmentToolBinding>(FragmentToolBinding::inflate) {

    override fun FragmentToolBinding.viewCreated() {}

    override fun FragmentToolBinding.initListeners() {
        activity?.apply context@{
            scanPdf.setOnClickListener {
                go(ListDocScannedActivity::class.java)
            }
            imageToPdf.setOnClickListener { go(ImageToPdfActivity::class.java) }
            wordToPdf.setOnClickListener { go(WordToPdfActivity::class.java) }
            excelToPdf.setOnClickListener { go(ExcelToPdfActivity::class.java) }
            textToPdf.setOnClickListener { go(TextToPdfActivity::class.java) }
            compressPdf.setOnClickListener { go(CompressActivity::class.java) }
            mergePdf.setOnClickListener { go(MergeActivity::class.java) }
            splitPdf.setOnClickListener { go(SplitActivity::class.java) }
        }
    }

    override fun FragmentToolBinding.initView() {}

    override fun create() {}

    companion object {

        fun newInstance() = ToolFragment().apply {
            arguments = Bundle().apply {}
        }
    }
}