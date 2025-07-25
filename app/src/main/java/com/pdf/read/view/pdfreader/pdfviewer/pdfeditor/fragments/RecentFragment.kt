package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.fragments

import android.content.*
import android.net.*
import android.os.*
import android.view.animation.*
import androidx.core.content.*
import androidx.lifecycle.*
import androidx.recyclerview.widget.*
import coder.apps.space.library.base.*
import coder.apps.space.library.extension.*
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

class RecentFragment : BaseFragment<FragmentRecentBinding>(FragmentRecentBinding::inflate) {
    private var commonAdapter: CommonAdapter? = null
    private var dataViewModel: RecentViewModel? = null

    override fun FragmentRecentBinding.viewCreated() {
        setupAdapter()
        setupData()
    }

    private fun FragmentRecentBinding.setupData() {
        activity?.apply context@{
            progressHorizontal.beVisible()
            dataViewModel?.recent?.observe(this@RecentFragment) { documents ->
                val newList = documents.map { File(it.filePath) }.toMutableList()
                runOnUiThread {
                    progressHorizontal.beGone()
                    if (documents.isNotEmpty()) {
                        commonAdapter?.addAll(files = newList)
                        layoutEmpty.beGone()
                        recyclerView.beVisible()
                    } else {
                        layoutEmpty.beVisible()
                        recyclerView.beGone()
                    }
                }
            }
        }
    }

    private fun FragmentRecentBinding.setupAdapter() {
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
                    viewOptionSheet(false, file) {
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

    override fun FragmentRecentBinding.initListeners() {
        activity?.apply context@{
        }
    }

    override fun FragmentRecentBinding.initView() {
    }

    override fun create() {
        activity?.apply {
            dataViewModel = ViewModelProvider(this, RecentViewModelFactory(recentDao = recentDao))[RecentViewModel::class.java]
        }
    }

    companion object {
        fun newInstance() = RecentFragment().apply {
            arguments = Bundle().apply {}
        }
    }
}