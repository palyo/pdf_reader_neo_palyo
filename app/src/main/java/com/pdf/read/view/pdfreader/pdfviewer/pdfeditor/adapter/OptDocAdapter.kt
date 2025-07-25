package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.adapter

import coder.apps.space.library.base.*
import com.bumptech.glide.*
import com.bumptech.glide.load.engine.*
import com.bumptech.glide.request.*
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.databinding.LayoutRowItemDocumentFileOptBinding
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.ext.*
import java.io.*

class OptDocAdapter(val listener: (file: File, position: Int) -> Unit) : BaseAdapter<String, LayoutRowItemDocumentFileOptBinding>(bindingFactory = { inflater, parent, attachToRoot, viewType ->
    LayoutRowItemDocumentFileOptBinding.inflate(inflater, parent, attachToRoot)
}) {

    fun remove(position: Int) {
        if (position in 0..<itemCount) {
            items.removeAt(position)
            notifyItemRemoved(position)
            notifyDataSetChanged()
        }
    }

    override fun LayoutRowItemDocumentFileOptBinding.bind(item: String, position: Int) {
        Glide.with(imageFile.context)
            .load(item)
            .apply(
                RequestOptions()
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .override(250)
                    .centerCrop()
                    .placeholder(File(item).iconFile)
                    .dontTransform()
            )
            .into(imageFile)
        textTitle.text = File(item).nameWithoutExtension
        textFileSize.text = File(item).length().formatSizeTwoDecimal()
        iconRemove.setOnClickListener {
            remove(position)
            listener.invoke(File(item), position)
        }
    }

    fun clear() {
        items.clear()
        notifyDataSetChanged()
    }

    fun getSources(): MutableList<String> {
        return items
    }
}