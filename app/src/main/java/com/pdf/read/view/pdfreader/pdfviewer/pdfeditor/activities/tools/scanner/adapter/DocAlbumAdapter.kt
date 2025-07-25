package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.activities.tools.scanner.adapter

import android.view.*
import androidx.recyclerview.widget.*
import com.bumptech.glide.*
import com.bumptech.glide.request.*
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.databinding.LayoutRowItemDocAlbumBinding
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.ext.formatSizeTwoDecimal
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.ext.fromMilliToDate
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.ext.singleClick
import documentreader.pdfviewer.filereader.documenttools.model.*
import java.io.*

class DocAlbumAdapter(private var click: (DocAlbum, Int) -> Unit, private var option: (DocAlbum, View, Int, Int) -> Unit) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var files: MutableList<DocAlbum>? = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return DefaultViewHolder(LayoutRowItemDocAlbumBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    fun addAll(files: MutableList<DocAlbum>?) {
        this.files = files
        notifyItemRangeInserted(0, (files?.size ?: itemCount) - 1)
    }

    fun add(file: DocAlbum) {
        this.files?.add(file)
        notifyItemInserted((files?.size ?: itemCount) - 1)
    }

    fun addAt(file: DocAlbum, index: Int) {
        this.files?.add(file)
        notifyItemInserted((files?.size ?: itemCount) - 1)
    }

    fun allFile(): MutableList<DocAlbum> {
        val newFiles = mutableListOf<DocAlbum>()
        files?.forEach {
            if (it != null) {
                newFiles.add(it)
            }
        }
        return newFiles
    }

    fun updateItem(position: Int, file: DocAlbum) {
        files?.set(position, file)
        notifyItemChanged(position)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is DefaultViewHolder -> {
                holder.binding.apply {
                    val file = files?.get(position)
                    file?.apply {
                        Glide.with(iconFile.context)
                                .load(file.thumb)
                                .thumbnail(0.1f)
                                .apply(RequestOptions()
                                        .centerCrop()
                                        .skipMemoryCache(false)
                                        .dontTransform()
                                        .dontAnimate())
                                .into(iconFile)
                        val album = File(file.albumPath)
                        textTitle.isSelected = true
                        textTitle.text = "${album.name}"
                        textInfo.text = "${file.count ?: 0} Pages \u2022 ${file.size?.formatSizeTwoDecimal()}"
                        textDate.text = File(file.thumb).lastModified().fromMilliToDate()
                        root.singleClick {
                            click.invoke(file, position)
                        }
                        buttonOption.singleClick { option.invoke(file, buttonOption, position, 3) }
                    }
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return files?.size ?: 0
    }

    fun clear() {
        files = ArrayList()
        notifyDataSetChanged()
    }

    fun remove(position: Int) {
        if (position in 0..<itemCount) {
            files?.removeAt(position)
            notifyItemRemoved(position)
            notifyDataSetChanged()
        }
    }

    class DefaultViewHolder(var binding: LayoutRowItemDocAlbumBinding) : RecyclerView.ViewHolder(binding.root)
}