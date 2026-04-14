package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.fragments

import android.os.*
import android.widget.*
import coder.apps.space.library.base.*
import coder.apps.space.library.extension.*
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.activities.*
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.activities.tools.converter.ExcelToPdfActivity
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.activities.tools.converter.ImageToPdfActivity
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.activities.tools.converter.TextToPdfActivity
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.activities.tools.converter.WordToPdfActivity
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.activities.tools.modify.CompressActivity
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.activities.tools.modify.MergeActivity
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.activities.tools.modify.SplitActivity
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.admodule.*
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.databinding.*
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.ext.*

class ToolFragment : BaseFragment<FragmentToolBinding>(FragmentToolBinding::inflate) {

    override fun FragmentToolBinding.viewCreated() {}

    override fun FragmentToolBinding.initListeners() {
        activity?.apply context@{
            imageToPdf.setOnClickListener { goto(ImageToPdfActivity::class.java) }
            wordToPdf.setOnClickListener { goto(WordToPdfActivity::class.java) }
            excelToPdf.setOnClickListener { goto(ExcelToPdfActivity::class.java) }
            textToPdf.setOnClickListener { goto(TextToPdfActivity::class.java) }
            compressPdf.setOnClickListener { goto(CompressActivity::class.java) }
            mergePdf.setOnClickListener { goto(MergeActivity::class.java) }
            splitPdf.setOnClickListener { goto(SplitActivity::class.java) }
        }
    }

    private fun goto(destination: Class<*>) {
        activity?.apply context@{
            showPremiumContentDialog(watchAd = {
                viewRewardAd(onClosed = {
                   if(it) go(destination)
                    else Toast.makeText(this, "Please see full video ad to earn reward", Toast.LENGTH_SHORT).show()
                })
            }, premium = {
                go(PremiumActivity::class.java)
            })
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