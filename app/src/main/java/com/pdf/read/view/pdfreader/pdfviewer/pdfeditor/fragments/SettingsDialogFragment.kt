package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.fragments

import android.content.*
import android.os.*
import android.view.*
import coder.apps.space.library.base.*
import coder.apps.space.library.extension.*
import coder.apps.space.library.helper.*
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.*
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.activities.*
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.databinding.*
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.ext.*

class SettingsDialogFragment : BaseDialog<FragmentSettingsBinding>(FragmentSettingsBinding::inflate) {
    var tinyDB: TinyDB? = null
    override fun create() {
        tinyDB = TinyDB(requireContext())
    }

    override fun FragmentSettingsBinding.initView() {

    }

    override fun FragmentSettingsBinding.initListeners() {
        toolbar.setNavigationOnClickListener {
            handleBackPress()
        }
        dialog?.setOnKeyListener { _, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_BACK && event.action == KeyEvent.ACTION_UP) {
                handleBackPress()
                true
            } else {
                false
            }
        }
    }

    private fun handleBackPress() {
        dismiss()
    }

    override fun FragmentSettingsBinding.viewCreated() {
        activity?.apply context@{
            buttonLanguage.setOnClickListener { activity?.go(AppLanguageActivity::class.java, listOf(IS_SETTINGS to true), finish = true) }
            buttonShare.setOnClickListener {
                val app = getString(R.string.app_name)
                Intent(Intent.ACTION_SEND).apply {
                    type = "text/plain"
                    putExtra(
                        "android.intent.extra.TEXT",
                        "$app\n\nOpen this Link on Play Store\n\nhttps://play.google.com/store/apps/details?id=${activity?.packageName}"
                    )
                    startActivity(Intent.createChooser(this, "Share Application"))
                }
            }

            buttonPrivacy.setOnClickListener {
                activity?.launchUrl("https://sites.google.com/view/filemanager-by-aani-brothers/home")
            }
//            val consentManager = GoogleMobileAdsConsentManager.getInstance(requireActivity())
//            consentManager.isPrivacyOptionsRequired.let {
//                buttonManageConsent.beVisibleIf(it)
//            }
//
//            buttonManageConsent.setOnClickListener {
//                consentManager.showPrivacyOptionsForm(this@context) {}
//            }
//
//            buttonCallerId.setOnClickListener {
//                activity?.go(SettingsActivity::class.java)
//            }
        }
    }

    companion object {
        const val TAG = "SettingsFragment"
        fun newInstance() = SettingsDialogFragment().apply {
            arguments = Bundle().apply {}
        }
    }
}