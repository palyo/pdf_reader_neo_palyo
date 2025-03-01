package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.activities

import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import coder.apps.space.library.base.*
import coder.apps.space.library.extension.*
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.adapter.*
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.admodule.*
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.databinding.*
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.ext.*

class AppBoardingActivity : BaseActivity<ActivityAppBoardingBinding>(ActivityAppBoardingBinding::inflate) {
    private var introViewPagerAdapter: IntroViewPagerAdapter? = null

    override fun ActivityAppBoardingBinding.initExtra() {
        setupPager()
        viewNativeSmall(adNative)
    }

    private fun ActivityAppBoardingBinding.setupPager() {
        introViewPagerAdapter = IntroViewPagerAdapter(this@AppBoardingActivity)
        viewPager.adapter = introViewPagerAdapter
        tabIndicator.setupWithViewPager(viewPager)
        buttonNext.setOnClickListener {
            if (viewPager.currentItem < 3 - 1) {
                viewPager.currentItem++
            } else {
                initNext()
            }
        }
    }

    private fun initNext() {
        tinyDB?.putBoolean(IS_ONBOARDING_ENABLED, false)
        go(HomeActivity::class.java, finish = true)
    }

    override fun ActivityAppBoardingBinding.initListeners() {
        viewPager.addOnPageChangeListener(object : OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

            }

            override fun onPageSelected(position: Int) {
                when (position) {
                    0, 1 -> {
                        adNative.beGone()
                    }

                    2 -> {
                        adNative.beVisible()
                    }
                }
            }

            override fun onPageScrollStateChanged(state: Int) {

            }
        })
    }

    override fun ActivityAppBoardingBinding.initView() {

    }
}