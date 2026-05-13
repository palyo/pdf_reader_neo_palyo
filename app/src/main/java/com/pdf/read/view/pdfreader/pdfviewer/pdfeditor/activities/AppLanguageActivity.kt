package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.activities

import androidx.activity.*
import androidx.recyclerview.widget.*
import coder.apps.space.library.base.*
import coder.apps.space.library.extension.*
import coder.apps.space.library.helper.*
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.R
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.adapter.*
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.admodule.*
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.databinding.*
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.ext.*

class AppLanguageActivity : BaseActivity<ActivityAppLanguageBinding>(ActivityAppLanguageBinding::inflate) {
    private var language: String = "en"
    override fun ActivityAppLanguageBinding.initExtra() {
        updateNavigationBarColor(coder.apps.space.library.R.color.colorTransparent)
        language = currentLanguage ?: "en"
        initAdapter()
        viewNativeMedium(adNative)
    }

    private fun ActivityAppLanguageBinding.initAdapter() {
        recyclerView.apply {
            layoutManager = GridLayoutManager(this@AppLanguageActivity, 1)
            adapter = LanguageAdapter(this@AppLanguageActivity) {
                language = it
            }
        }
    }

    override fun ActivityAppLanguageBinding.initListeners() {
        buttonGo.setOnClickListener {
            currentLanguage = language
            val fromHome = intent?.getBooleanExtra(IS_SETTINGS, false)
            if (fromHome == true) {
                // User opened Language from the settings menu — return there directly,
                // don't drag them through the first-run permission funnel again.
                go(HomeActivity::class.java, finish = true)
                return@setOnClickListener
            }

            tinyDB?.putBoolean(IS_LANGUAGE_ENABLED, false)

            // First-run flow: Splash → Language → Permission → Onboarding → Dashboard.
            // Gate on live OS permission state — if any of the after-call
            // permissions is still missing (incl. when the language picker
            // re-fires periodically via isStartFlowRepeat()), funnel the user
            // through the permission screen again.
            if (!hasAllAfterCallPermissions() && !isPremium) {
                go(AfterCallPermissionActivity::class.java, finish = true)
            } else {
                go(HomeActivity::class.java, finish = true)
            }
        }
    }

    override fun ActivityAppLanguageBinding.initView() {
        updateStatusBarColor(R.color.colorPrimary)
        onBackPressedDispatcher.addCallback(this@AppLanguageActivity, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val fromHome = intent?.getBooleanExtra(IS_SETTINGS, false)
                if (fromHome == true) {
                    go(HomeActivity::class.java, finish = true)
                } else {
                    finish()
                }
            }
        })
    }
}