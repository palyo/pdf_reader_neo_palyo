package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.adapter

import android.content.*
import android.view.*
import androidx.recyclerview.widget.*
import coder.apps.space.library.extension.*
import com.bumptech.glide.*
import com.bumptech.glide.load.resource.drawable.*
import com.bumptech.glide.request.*
import com.bumptech.glide.request.target.Target
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.databinding.*
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.ext.*
import java.io.*

class SearchAdapter(
    private val context: Context,
    private var click: (File, Int) -> Unit,
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var files: MutableList<File>? = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return DefaultViewHolder(LayoutRowItemDirectoryBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    fun addAll(files: MutableList<File>?) {
        this.files = files
        notifyDataSetChanged()
    }

    fun add(file: File) {
        this.files?.add(file)
        notifyItemInserted((files?.size ?: itemCount) - 1)
    }

    fun addAt(file: File, index: Int) {
        this.files?.add(file)
        notifyItemInserted((files?.size ?: itemCount) - 1)
    }

    fun allFile(): MutableList<File> {
        val newFiles = mutableListOf<File>()
        files?.forEach {
            if (it != null) {
                newFiles.add(it)
            }
        }
        return newFiles
    }

    fun updateItem(position: Int, file: File) {
        files?.set(position, file)
        notifyItemChanged(position)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is DefaultViewHolder -> {
                holder.binding.apply {
                    val file = files?.get(position)
                    file?.apply {
                        val fileInfo = length().formatSizeTwoDecimal()
                        Glide.with(context)
                            .load(absolutePath)
                            .transition(DrawableTransitionOptions.withCrossFade())
                            .apply(
                                RequestOptions()
                                    .dontTransform()
                                    .dontAnimate()
                                    .placeholder(iconFile)
                                    .centerCrop()
                                    .override(Target.SIZE_ORIGINAL)
                                    .placeholder(iconFile)
                                    .skipMemoryCache(false)
                            ).into(fileIcon)
                        fileCard.isSelected = isDirectory
                        textTitle.isSelected = true
                        textTitle.text = name
                        textModified.text = lastModified().fromMilliToDate()
                        textFileSize.text = fileInfo
                        root.setOnClickListener {
                            click.invoke(file, position)
                        }
                    }
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return files?.size ?: 0
    }

    fun remove(position: Int) {
        if (position in 0..<itemCount) {
            files?.removeAt(position)
            notifyItemRemoved(position)
            notifyDataSetChanged()
        }
    }

    fun removeItems(selected: MutableList<File>) {
        files?.removeAll(selected)
        notifyDataSetChanged()
    }

    class DefaultViewHolder(var binding: LayoutRowItemDirectoryBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.buttonOption.beGone()
        }
    }
}