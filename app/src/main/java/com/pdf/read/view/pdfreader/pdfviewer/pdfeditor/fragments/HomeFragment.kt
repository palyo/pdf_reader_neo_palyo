package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.fragments

import android.annotation.*
import android.app.*
import android.content.*
import android.net.*
import android.os.*
import android.provider.*
import androidx.activity.result.contract.*
import androidx.annotation.*
import androidx.viewpager2.widget.*
import coder.apps.space.library.base.*
import coder.apps.space.library.extension.*
import coder.apps.space.library.helper.*
import com.google.android.gms.ads.nativead.*
import com.google.android.material.button.*
import com.google.android.material.tabs.*
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.*
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.activities.*
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.admodule.*
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.databinding.*
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.ext.*
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.pager.*
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.viewmodel.*

class HomeFragment : BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate) {

    private var nativeAd: NativeAd? = null
    private var documentViewModel: DocumentViewModel? = null
    private var pager: FragmentTabPager? = null
    private var handlerSetting: HandleSettingPreview? = null
    private var permissionFlow: Int = -1
    private var managePermissionLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            activity?.apply {
                if (isPermissionAllowed()) {
                    appOpenCount += 1
                    binding?.setupViewPager()
                    documentViewModel = DocumentViewModel.getInstance(application)
                    documentViewModel?.loadPreload()
                    return@registerForActivityResult
                }
                binding?.layoutPermission?.root?.beVisibleIf(!isPermissionAllowed())
            }
        }
    }
    private var storagePermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
        activity?.apply {
            if (isPermissionAllowed()) {
                binding?.setupViewPager()
                documentViewModel = DocumentViewModel.getInstance(application)
                documentViewModel?.loadPreload()
                return@registerForActivityResult
            }

            binding?.layoutPermission?.root?.beVisibleIf(!isPermissionAllowed())
        }
    }

    override fun FragmentHomeBinding.viewCreated() {
        activity?.apply {
            if (!isPermissionAllowed()) {
                layoutPermission.root.beVisible()
            } else {
                setupViewPager()
            }
        }
    }

    private var pagerListener = object : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)
            //binding?.updateAd()
        }
    }

    private fun FragmentHomeBinding.setupViewPager() {
        viewPager.apply {
            pager = FragmentTabPager(childFragmentManager, lifecycle)
            adapter = pager
            pager?.addTabs(mutableListOf<String>().apply {
                add(getString(R.string.tab_all))
                add(getString(R.string.tab_pdf))
                add(getString(R.string.tab_doc))
                add(getString(R.string.tab_ppt))
                add(getString(R.string.tab_xls))
            })
            offscreenPageLimit = pager?.itemCount ?: ViewPager2.OFFSCREEN_PAGE_LIMIT_DEFAULT
            TabLayoutMediator(tabLayout, viewPager) { tab, position ->
                tab.text = pager?.tabs?.get(position) ?: ""
            }.attach()
            viewPager.registerOnPageChangeCallback(pagerListener)
            //updateAd()
        }
    }

    fun updateAd(b: Boolean) {
        binding?.apply {
            if (b) {
                if (nativeAd == null) {
                    activity?.viewNativeBanner(adNative) { nativeAd: NativeAd? ->
                        this@HomeFragment.nativeAd = nativeAd
                    }
                } else {
                    nativeAd?.let { activity?.viewPopulateNativeBanner(it, adNative) }
                }
            } else {
                activity?.viewLoadingBanner(adNative)
            }
            //updateSort()
        }
    }

    override fun FragmentHomeBinding.initListeners() {
        activity?.apply context@{
            layoutPermission.apply {
                buttonAllow.setOnClickListener {
                    initPermissions()
                }
            }
            requireActivity().findViewById<MaterialButton>(R.id.button_sort).setOnClickListener {
                viewSortDialog {
                    updateSort()
                }
            }
        }
    }

    private fun updateSort() {
        activity?.apply {
            val tinyDB = TinyDB(this)
            val selectedSort = tinyDB.getString(SORT_BY, SORT_MODIFIED)
            val selectedOrder = tinyDB.getString(SORT_ORDER, SORT_DESCENDING)
            documentViewModel?.setSorting(
                when (selectedSort) {
                    SORT_MODIFIED -> SortBy.DATE_MODIFIED
                    SORT_NAME -> SortBy.NAME
                    else -> SortBy.SIZE
                }, when (selectedOrder) {
                SORT_ASCENDING -> SortOrder.ASCENDING
                else -> SortOrder.DESCENDING
            }
            )
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

    private fun initPermissions() {
        activity?.apply context@{
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) if (!Environment.isExternalStorageManager()) initManagePermission() else binding?.setupViewPager()
            else if (!hasPermissions(STORAGE_PERMISSION)) initRWPermission() else binding?.setupViewPager()
        }
    }

    @RequiresApi(Build.VERSION_CODES.R)
    private fun initManagePermission() {
        permissionFlow = 1
        activity?.apply context@{
            try {
                handlerSetting?.startPollingImeSettings()
                Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION).apply {
                    addCategory("android.intent.category.DEFAULT")
                    data = Uri.parse(String.format("package:%s", applicationContext.packageName))
                    managePermissionLauncher.launch(this)
                }
            } catch (e: Exception) {
                handlerSetting?.startPollingImeSettings()
                Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION).apply {
                    managePermissionLauncher.launch(this)
                }
            }
        }
    }

    private fun initRWPermission() {
        storagePermissionLauncher.launch(STORAGE_PERMISSION)
    }

    override fun FragmentHomeBinding.initView() {
    }

    override fun create() {
        handlerSetting = HandleSettingPreview(this@HomeFragment)
        activity?.apply context@{
            documentViewModel = DocumentViewModel.getInstance(application)
        }
    }

    companion object {

        fun newInstance() = HomeFragment().apply {
            arguments = Bundle().apply {}
        }
    }

    class HandleSettingPreview internal constructor(context: HomeFragment) : LeakGuardHandlerWrapper<HomeFragment>(context) {

        fun cancelPollingImeSettings() {
            removeMessages(0)
        }

        @SuppressLint("NewApi")
        override fun handleMessage(message: Message) {
            val ownerInstance = ownerInstance
            if (ownerInstance != null && message.what == 0) {
                if (ownerInstance.permissionFlow == 1 && Environment.isExternalStorageManager()) {
                    ownerInstance.invokeSetupWizardOfThisIme()
                } else {
                    startPollingImeSettings()
                }
            }
        }

        fun startPollingImeSettings() {
            sendMessageDelayed(obtainMessage(0), 200L)
        }
    }

    private fun invokeSetupWizardOfThisIme() {
        activity?.apply context@{
            handlerSetting?.cancelPollingImeSettings()
            go(HomeActivity::class.java, finishAll = true)
        }
    }
}

