package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.pager

import androidx.fragment.app.*
import androidx.lifecycle.*
import androidx.viewpager2.adapter.*
import com.google.android.gms.ads.nativead.NativeAd
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.fragments.*

class FragmentTabPager(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {
    var tabs: MutableList<String> = mutableListOf()

    companion object {
        var sharedNativeAd: NativeAd? = null
    }

    fun addTabs(newTabTitles: MutableList<String>?) {
        if (newTabTitles != null) {
            tabs.clear()
            tabs.addAll(newTabTitles)
            notifyDataSetChanged()
        }
    }

    override fun getItemCount(): Int {
        return tabs.size
    }

    override fun createFragment(position: Int): Fragment {
        return FilesFragment.newInstance(tabs[position])
    }
}
