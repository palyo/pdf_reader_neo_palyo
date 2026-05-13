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

        /**
         * AdMob policy guard rails for the single inline native ad slot:
         *
         *  - [MIN_FILES_FOR_ADS] — Skip the ad entirely on near-empty lists
         *    (≤ 3 items); without enough organic content around it, the
         *    ad would dominate the visible area.
         *  - [AD_POSITION] — One ad, inserted after this many organic
         *    items. Keeps the top of the list ad-free so the screen above
         *    the fold is content, satisfying AdMob's "primarily ads" check.
         */
        private const val MIN_FILES_FOR_ADS = 4
        private const val AD_POSITION = 3
        private const val AD_PLACEHOLDER = "AD_PLACEHOLDER"
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

    /**
     * Rebuilds [items] from the supplied file list and inserts the single
     * inline native ad after the [AD_POSITION]-th organic item.
     *
     * Layout when ads are enabled and the list is long enough:
     *   file[0] · file[1] · file[2] · **AD** · file[3] · file[4] · …
     *
     * Premium users, sub-[MIN_FILES_FOR_ADS] lists, and `null` input skip
     * ad insertion entirely.
     */
    fun addAll(files: MutableList<File>?) {
        items.clear()
        files?.let { input ->
            val showAds = !context.isPremium && input.size >= MIN_FILES_FOR_ADS
            input.forEachIndexed { index, file ->
                if (showAds && index == AD_POSITION) {
                    items.add(AD_PLACEHOLDER)
                }
                items.add(file)
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
