package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.activities

import android.app.*
import android.content.*
import android.net.*
import android.os.*
import android.widget.*
import androidx.activity.*
import androidx.core.content.*
import androidx.fragment.app.Fragment
import coder.apps.space.library.base.*
import coder.apps.space.library.extension.*
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.App.Companion.isOpenInter
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.R
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.activities.viewer.*
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.admodule.*
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.databinding.*
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.ext.*
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.fragments.*
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.viewmodel.*

class HomeActivity : BaseActivity<ActivityHomeBinding>(ActivityHomeBinding::inflate) {

    private var documentViewModel: DocumentViewModel? = null
    override fun ActivityHomeBinding.initExtra() {
        setupNavigation()
        if (isPermissionAllowed()) {
            documentViewModel = DocumentViewModel.getInstance(application)
            documentViewModel?.loadPreload()
            appOpenCount += 1
        }
        viewCollapsibleBanner(adBanner)
    }

    private fun ActivityHomeBinding.setupNavigation() {
        openDefaultNavigation()
        bottomNavigationView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.navigation_home -> {
                    buttonSort.beVisible()
                    buttonSearch.beVisible()
                    updateFragment(HomeFragment.newInstance(), HomeFragment::class.java.name)
                    return@setOnItemSelectedListener true
                }

                R.id.navigation_recent -> {
                    buttonSort.beGone()
                    buttonSearch.beVisible()
                    updateFragment(RecentFragment.newInstance(), RecentFragment::class.java.name)
                    return@setOnItemSelectedListener true
                }

                R.id.navigation_favorite -> {
                    buttonSort.beGone()
                    buttonSearch.beVisible()
                    updateFragment(FavoriteFragment.newInstance(), FavoriteFragment::class.java.name)
                    return@setOnItemSelectedListener true
                }

                R.id.navigation_tool -> {
                    buttonSort.beGone()
                    buttonSearch.beGone()
                    updateFragment(ToolFragment.newInstance(), ToolFragment::class.java.name)
                    return@setOnItemSelectedListener true
                }
            }
            return@setOnItemSelectedListener false
        }
    }

    private fun ActivityHomeBinding.openDefaultNavigation() {
        when (intent?.getIntExtra(CURRENT_TAB, 0)) {
            3 -> {
                buttonSort.beGone()
                buttonSearch.beGone()
                bottomNavigationView.selectedItemId = R.id.navigation_tool
                updateFragment(ToolFragment.newInstance(), ToolFragment::class.java.name)
            }
            2 -> {
                buttonSort.beGone()
                buttonSearch.beVisible()
                bottomNavigationView.selectedItemId = R.id.navigation_favorite
                updateFragment(FavoriteFragment.newInstance(), FavoriteFragment::class.java.name)
            }

            1 -> {
                buttonSort.beGone()
                buttonSearch.beVisible()
                bottomNavigationView.selectedItemId = R.id.navigation_recent
                updateFragment(RecentFragment.newInstance(), RecentFragment::class.java.name)
            }

            else -> {
                buttonSort.beVisible()
                buttonSearch.beVisible()
                bottomNavigationView.selectedItemId = R.id.navigation_home
                updateFragment(HomeFragment.newInstance(), HomeFragment::class.java.name)
            }
        }
    }

    private fun updateFragment(fragment: Fragment, tag: String) {
        try {
            var fragmentNew = fragment
            val supportFragmentManager = supportFragmentManager
            val beginTransaction = supportFragmentManager.beginTransaction()
            val primaryNavigationFragment = supportFragmentManager.primaryNavigationFragment
            if (primaryNavigationFragment != null) {
                beginTransaction.hide(primaryNavigationFragment)
            }
            val existingFragment = supportFragmentManager.findFragmentByTag(tag)
            if (existingFragment == null) {
                beginTransaction.add(R.id.container_fragment, fragmentNew, tag)
            } else {
                beginTransaction.show(existingFragment)
                fragmentNew = existingFragment
            }

            beginTransaction.setPrimaryNavigationFragment(fragmentNew)
            beginTransaction.setReorderingAllowed(true)
            beginTransaction.commitNowAllowingStateLoss()
        } catch (e: Exception) {
        }
    }

    override fun ActivityHomeBinding.initListeners() {
        buttonSettings.setOnClickListener {
            SettingsDialogFragment.newInstance().apply {
                isCancelable = true
                show(supportFragmentManager.beginTransaction(), SettingsDialogFragment::class.java.simpleName)
            }
        }
        buttonSearch.setOnClickListener {
            FindFiles.newInstance { file ->
                val uri: Uri = FileProvider.getUriForFile(this@HomeActivity, "${packageName}.provider", file)
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
            }.apply {
                isCancelable = true
                show(supportFragmentManager.beginTransaction(), FindFiles::class.java.simpleName)
            }
        }
    }

    private fun Activity.isPermissionAllowed(): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (Environment.isExternalStorageManager()) {
                return true
            }
        } else {
            if (hasPermissions(STORAGE_PERMISSION)) {
                return true
            }
        }
        return false
    }

    override fun ActivityHomeBinding.initView() {
        onBackPressedDispatcher.addCallback {
            if (tinyDB?.getBoolean("is_reader_rated", false) == true && tinyDB?.getBoolean("isRated", false)?.not() == true) {
                viewRateDialog(isFinish = true, { isRated ->
                    isOpenInter = true
                    if (isRated) {
                        tinyDB?.putBoolean("isRated", true)
                        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=$packageName")))
                    } else {
                        try {
                            val emailIntent = Intent(Intent.ACTION_SEND)
                            emailIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                            emailIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf("sheldrames@gmail.com"))
                            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Feedback for Pdf Reader - Pdf Viewer")
                            emailIntent.putExtra(Intent.EXTRA_TEXT, "")
                            emailIntent.setType("message/rfc822")
                            emailIntent.setPackage("com.google.android.gm")
                            startActivity(emailIntent)
                        } catch (ignored: ActivityNotFoundException) {
                            Toast.makeText(this@HomeActivity, "Couldn't find an email app and account", Toast.LENGTH_LONG).show()
                        }
                    }
                })
            } else {
                finishAffinity()
            }
        }
    }
}