package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.fragments

import android.content.*
import android.net.*
import android.os.*
import android.view.animation.*
import androidx.core.content.*
import androidx.recyclerview.widget.*
import coder.apps.space.library.base.*
import coder.apps.space.library.extension.*
import coder.apps.space.library.helper.TinyDB
import com.google.android.material.progressindicator.*
import com.google.android.material.textview.*
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.*
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.activities.viewer.*
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.adapter.*
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.database.*
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.database.table.*
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.databinding.*
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.ext.*
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.viewmodel.*
import jp.wasabeef.recyclerview.animators.*
import kotlinx.coroutines.*
import java.io.*

class FilesFragment : BaseFragment<FragmentFilesBinding>(FragmentFilesBinding::inflate) {

    private var documentViewModel: DocumentViewModel? = null
    private var commonAdapter: CommonAdapter? = null
    var firstEmissionHandled = false

    override fun FragmentFilesBinding.viewCreated() {
        activity?.apply context@{
            setupAdapter()
        }
    }

    override fun onResume() {
        super.onResume()
        binding?.setupData()
    }

    private fun FragmentFilesBinding.setupData() {
        activity?.apply context@{
            val type = arguments?.getString("type") ?: getString(R.string.tab_all)
            val documentType = when (type) {
                getString(R.string.tab_pdf) -> DocumentType.PDF
                getString(R.string.tab_doc) -> DocumentType.DOC
                getString(R.string.tab_xls) -> DocumentType.XLS
                getString(R.string.tab_ppt) -> DocumentType.PPT
                else -> DocumentType.ALL
            }
            val tinyDB = TinyDB(this)
            val selectedSort = tinyDB.getString(SORT_BY, SORT_MODIFIED)
            val selectedOrder = tinyDB.getString(SORT_ORDER, SORT_DESCENDING)
            animationView.beVisible()
            documentViewModel?.getFilteredDocuments(documentType)?.observe(this@context) { documents ->
                documents.sortWith(Comparator { file1, file2 ->
                    val result = when (selectedSort) {
                        SORT_MODIFIED -> file1.lastModified().compareTo(file2.lastModified())
                        SORT_SIZE -> file1.length().compareTo(file2.length())
                        else -> {
                            file1.name.compareTo(file2.name, ignoreCase = true)
                        }
                    }
                    if (selectedOrder == SORT_ASCENDING) result else -result
                })
                runOnUiThread {
                    if (!firstEmissionHandled && documents.isEmpty()) {
                        firstEmissionHandled = true
                        return@runOnUiThread
                    }
                    if (documents.isNotEmpty()) {
                        commonAdapter?.addAll(files = documents)
                        (parentFragment as HomeFragment).updateAd(true)
                        layoutEmpty.beGone()
                        recyclerView.beVisible()
                        animationView.beGone()
                    } else {
                        (parentFragment as HomeFragment).updateAd(false)
                        layoutEmpty.beVisible()
                        recyclerView.beGone()
                        animationView.beGone()
                    }
                }
            }
        }
    }

    private fun FragmentFilesBinding.setupAdapter() {
        activity?.apply context@{
            recyclerView.apply {
                itemAnimator = SlideInUpAnimator(OvershootInterpolator(1f))
                layoutManager = LinearLayoutManager(this@context, RecyclerView.VERTICAL, false)
                commonAdapter = CommonAdapter(context = context, click = { file: File, position: Int ->
                    val uri: Uri = FileProvider.getUriForFile(this@context, "${packageName}.provider", file)
                    if (file.isPdf) {
                        go(
                            PDFReaderActivity::class.java,
                            listOf(FILE_URI to uri, FILE_PATH to file.path, FILE_NAME to file.name)
                        )
                    } else {
                        go(
                            ReaderActivity::class.java,
                            listOf(FILE_URI to uri, FILE_PATH to file.path, FILE_NAME to file.name)
                        )
                    }
                }, option = { file: File, position: Int ->
                    viewOptionSheet(true, file) {
                        when (it) {
                            Option.FAVORITE -> {
                                CoroutineScope(Dispatchers.IO).launch {
                                    val favorite = favoriteLikeDao.isFavorite(file.absolutePath.toString())
                                    if (favorite == null) {
                                        favoriteLikeDao.insert(Favorite(fullName = file.name, filePath = file.absolutePath.toString()))
                                    } else {
                                        favoriteLikeDao.delete(favorite)
                                    }
                                }
                            }

                            Option.SHARE -> {
                                val uri: Uri = FileProvider.getUriForFile(this@context, "${packageName}.provider", file)
                                Intent(Intent.ACTION_SEND).apply {
                                    type = file.mimeType
                                    putExtra(Intent.EXTRA_STREAM, uri)
                                    startActivity(Intent.createChooser(this, null))
                                }
                            }

                            Option.RENAME -> {
                                viewRenameSheet(title = file.nameWithoutExtension) {
                                    val newFile = file.renameFile("$it.${file.extension}")
                                    commonAdapter?.updateItem(position, newFile)
                                }
                            }

                            Option.DELETE -> {
                                val selected = mutableListOf(file)
                                viewTrashOrDeleteSheet {
                                    val dialog = viewProgressSheet()
                                    val dialogTitle: MaterialTextView? = dialog.findViewById(R.id.dialog_title)
                                    val progressBar: LinearProgressIndicator? = dialog.findViewById(R.id.percentage_progress)
                                    dialogTitle?.text = getString(R.string.sheet_title_deleting, 0)
                                    if (!isFinishing && !isDestroyed) {
                                        dialog.show()
                                    }
                                    CoroutineScope(Dispatchers.IO).launch {
                                        selected.deleteFilesWithProgress(progress = {
                                            dialogTitle?.text = getString(R.string.sheet_title_deleting, it)
                                            progressBar?.setProgress(it, true)
                                        }) {
                                            if (!isFinishing && !isDestroyed) {
                                                dialog.dismiss()
                                                commonAdapter?.removeItems(selected)
                                                documentViewModel?.deleteFromCurrentList(selected)
                                            }
                                        }
                                    }
                                }
                            }

                            else -> {}
                        }
                    }
                })
                adapter = commonAdapter
            }
        }
    }

    override fun FragmentFilesBinding.initListeners() {

    }

    override fun FragmentFilesBinding.initView() {
    }

    override fun create() {
        activity?.apply context@{
            documentViewModel = DocumentViewModel.getInstance(application)
        }
    }

    fun isDataLoaded(): Boolean {
        return commonAdapter?.itemCount != 0
    }

    companion object {

        fun newInstance(type: String) = FilesFragment().apply {
            arguments = Bundle().apply {
                putString("type", type)
            }
        }
    }
}