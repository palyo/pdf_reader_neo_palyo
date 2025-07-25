package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.adapter

import android.view.*
import androidx.recyclerview.widget.*
import coder.apps.space.library.extension.beVisibleIf
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.databinding.*
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.ext.*
import java.io.*

class SelectPDFAdapter(
    private val isMultiple: Boolean,
    val listener: (document: File, position: Int) -> Unit,
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var fileType: String? = PDF_PICK
    private var documents: MutableList<File> = mutableListOf()
    var selectedDocuments: MutableList<String> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return DataViewHolder(LayoutRowItemDocumentFileBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    class DataViewHolder(var binding: LayoutRowItemDocumentFileBinding) :
        RecyclerView.ViewHolder(binding.root)

    fun addAll(documents: MutableList<File>, fileType: String) {
        this.documents = documents
        this.fileType = fileType
        notifyItemRangeChanged(0, documents.size - 1)
    }

    fun add(media: File?) {
        media?.let { documents.add(it) }
        notifyItemChanged(documents.size - 1)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is DataViewHolder -> {
                val document = documents[position]
                holder.binding.apply {
                    textTitle.text = document.nameWithoutExtension
                    textType.text = document.extension.uppercase()
                    textFileSize.text = document.length().formatSizeTwoDecimal()
                    iconSelected.isSelected = selectedDocuments.contains(document.absolutePath)
                    imageFile.setImageResource(document.iconFile)
                    iconSelected.beVisibleIf(isMultiple)
                    root.setOnClickListener {
                        if (!isMultiple) {
                            listener.invoke(document, position)
                            return@setOnClickListener
                        }
                        if (selectedDocuments.contains(document.absolutePath)) {
                            selectedDocuments.remove(document.absolutePath)
                        } else {
                            selectedDocuments.add(document.absolutePath)
                        }
                        notifyItemChanged(position)
                    }
                }
            }
        }
    }

    fun clear() {
        documents = mutableListOf()
        notifyDataSetChanged()
    }

    fun getSources(): MutableList<File> = documents

    override fun getItemCount(): Int = documents.size
}