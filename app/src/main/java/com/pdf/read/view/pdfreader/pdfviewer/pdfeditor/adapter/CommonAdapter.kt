package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.adapter

import android.content.Context
import android.util.*
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.google.android.gms.ads.nativead.NativeAd
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.admodule.*
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.databinding.LayoutRowItemDirectoryBinding
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.databinding.LayoutRowItemNativeAdBannerBinding
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.ext.formatSizeTwoDecimal
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.ext.fromMilliToDate
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.ext.*
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.fragments.*
import java.io.File

class CommonAdapter(
    private val context: Context,
    private var click: (File, Int) -> Unit,
    private var option: (File, Int) -> Unit,
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {

        private const val VIEW_TYPE_FILE = 0
        private const val VIEW_TYPE_AD = 1
        private const val AD_INDEX = 2
    }

    private var nativeAd: NativeAd? = null
    private val items: MutableList<Any> = mutableListOf()

    override fun getItemCount(): Int = items.size

    override fun getItemViewType(position: Int): Int {
        return if (items[position] is File) VIEW_TYPE_FILE else VIEW_TYPE_AD
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            VIEW_TYPE_FILE -> {
                val binding = LayoutRowItemDirectoryBinding.inflate(inflater, parent, false)
                DefaultViewHolder(binding)
            }

            VIEW_TYPE_AD -> {
                val binding = LayoutRowItemNativeAdBannerBinding.inflate(inflater, parent, false)
                NativeAdViewHolder(binding)
            }

            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is DefaultViewHolder -> {
                val file = items[position] as File
                holder.binding.apply {
                    val fileInfo = file.length().formatSizeTwoDecimal()
                    Glide.with(context)
                        .load(file.absolutePath)
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .apply(
                            RequestOptions()
                                .dontTransform()
                                .dontAnimate()
                                .placeholder(file.iconFile)
                                .centerCrop()
                                .skipMemoryCache(false)
                        ).into(fileIcon)

                    fileCard.isSelected = file.isDirectory
                    textTitle.isSelected = true
                    textTitle.text = file.name
                    textModified.text = file.lastModified().fromMilliToDate()
                    textFileSize.text = fileInfo

                    buttonOption.setOnClickListener { option.invoke(file, position) }
                    root.setOnClickListener { click.invoke(file, position) }
                }
            }

            is NativeAdViewHolder -> {
                holder.binding.apply {
                    if (nativeAd != null) {
                        nativeAd?.let { context.viewPopulateNativeBanner(it, adNative) }
                    } else {
                        context.viewLoadingBanner(adNative)
                    }
                }
            }
        }
    }

    fun addAll(files: MutableList<File>?) {
        items.clear()
        files?.let {
            val showAds = !context.isPremium
            val adInterval = 10
            var i = 0
            var actualPosition = 0

            while (i < it.size) {
                // Insert ad at position 1 (2nd item), then every 10th item after that
                if (showAds && (actualPosition == 1 || (actualPosition > 1 && (actualPosition - 1) % adInterval == 0))) {
                    items.add("AD_PLACEHOLDER")
                    actualPosition++
                }

                items.add(it[i])
                i++
                actualPosition++
            }
        }
        notifyDataSetChanged()
    }

    fun updateNativeAd(ad: NativeAd?) {
        nativeAd = ad
        for (i in items.indices) {
            if (getItemViewType(i) == VIEW_TYPE_AD) {
                notifyItemChanged(i)
            }
        }
    }

    fun add(file: File) {
        val insertIndex = items.size
        items.add(file)
        notifyItemInserted(insertIndex)
    }

    fun addAt(file: File, index: Int) {
        val insertIndex = if (index >= items.size) items.size else index
        items.add(insertIndex, file)
        notifyItemInserted(insertIndex)
    }

    fun updateItem(position: Int, file: File) {
        if (items[position] is File) {
            items[position] = file
            notifyItemChanged(position)
        }
    }

    fun allFile(): MutableList<File> {
        return items.filterIsInstance<File>().toMutableList()
    }

    fun remove(position: Int) {
        if (position in items.indices) {
            items.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    fun removeItems(selected: MutableList<File>) {
        items.removeAll { it is File && selected.contains(it) }
        notifyDataSetChanged()
    }

    class DefaultViewHolder(val binding: LayoutRowItemDirectoryBinding) :
        RecyclerView.ViewHolder(binding.root)

    class NativeAdViewHolder(val binding: LayoutRowItemNativeAdBannerBinding) :
        RecyclerView.ViewHolder(binding.root)
}
