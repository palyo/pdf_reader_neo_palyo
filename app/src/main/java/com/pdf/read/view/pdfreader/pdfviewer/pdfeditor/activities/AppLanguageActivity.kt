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
                go(HomeActivity::class.java, finish = true)
            } else {
                tinyDB?.putBoolean(IS_LANGUAGE_ENABLED, false)
                if (isStartFlowRepeat() || tinyDB?.getBoolean(IS_ONBOARDING_ENABLED, true) == true) {
                    go(AppBoardingActivity::class.java, finish = true)
                }
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