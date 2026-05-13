package coder.apps.aftercall.ui

import coder.apps.aftercall.R
import coder.apps.aftercall.core.DurationFormatter
import coder.apps.aftercall.core.HomeWatcher
import coder.apps.aftercall.ads.PostCallAdState
import coder.apps.aftercall.ads.PostCallAdState.Companion.adBannerExpirationTime
import coder.apps.aftercall.ads.PostCallAdState.Companion.adBannerLoadTimeValue
import coder.apps.aftercall.ads.PostCallAdState.Companion.adNativeExpirationTime
import coder.apps.aftercall.ads.PostCallAdState.Companion.adNativeLoadTimeValue
import coder.apps.aftercall.ads.PostCallAdState.Companion.bannerAdViewWidget
import coder.apps.aftercall.ads.PostCallAdState.Companion.destroyUnusedNative
import coder.apps.aftercall.ads.PostCallAdState.Companion.getBannerAdView
import coder.apps.aftercall.ads.PostCallAdState.Companion.isBannerAdLoading
import coder.apps.aftercall.ads.PostCallAdState.Companion.isCallingStart
import coder.apps.aftercall.ads.PostCallAdState.Companion.isNativeFailedGooglePostCallValue
import coder.apps.aftercall.ads.PostCallAdState.Companion.isNativeGooglePostCallValue
import coder.apps.aftercall.ads.PostCallAdState.Companion.isNativePostCallLoadingValue
import coder.apps.aftercall.ads.PostCallAdState.Companion.nativeAdsPostCallAd
import coder.apps.aftercall.ads.PostCallBannerAd
import coder.apps.aftercall.ads.PostCallNativeAd
import coder.apps.aftercall.extensions.CALL_COUNTER
import coder.apps.aftercall.extensions.CALL_TIME
import coder.apps.aftercall.extensions.CALL_TYPE
import coder.apps.aftercall.extensions.END_TIME
import coder.apps.aftercall.extensions.EXTRA_MOBILE_NUMBER
import coder.apps.aftercall.extensions.IS_OPEN_FROM_NOTIFICATION
import coder.apps.aftercall.extensions.START_TIME
import coder.apps.aftercall.extensions.isBannerLoad
import coder.apps.aftercall.receivers.PhoneCallStateReceiver
import coder.apps.aftercall.ui.tab.DefaultMsgFragment
import coder.apps.aftercall.ui.tab.MessagesFragment
import coder.apps.aftercall.ui.tab.OptionsFragment
import coder.apps.aftercall.AfterCallConfig
import coder.apps.aftercall.databinding.ActivityPostCallBinding
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.IBinder
import coder.apps.aftercall.core.AfterCallLog
import coder.apps.aftercall.core.AfterCallLog.Area.ACTIVITY
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import androidx.core.graphics.Insets
import androidx.core.net.toUri
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isEmpty
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import coder.apps.space.library.base.BaseActivity
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class PostCallActivity : BaseActivity<ActivityPostCallBinding>(ActivityPostCallBinding::inflate) {
    private lateinit var bannerAds: PostCallBannerAd
    private lateinit var nativeADs: PostCallNativeAd
    private var homeWatcher: HomeWatcher? = null
    private var callTime: String = "null"
    private var callType: String = "null"
    private var isBannerPostCall = false
    private var isOpenFromNotification = false
    private var mobileNumber: String = "null"
    private var isFirstOpenScreen = false

    companion object {
        private var postCallActivity: PostCallActivity? = null

        fun getPostCallActivity(): PostCallActivity? {
            return postCallActivity
        }
    }

    override fun ActivityPostCallBinding.initExtra() {
        try {
            setLockScreen()
        } catch (unused: Exception) {
        }
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { view, windowInsetsCompat ->
            applyWindowInsets(view, windowInsetsCompat)
        }
        disabledOpenAds()
        postCallActivity = this@PostCallActivity
        manageNavigationBar()
        initData()
        removeActivity()
        initAds()

        supportFragmentManager.setFragmentResultListener(
            MessagesFragment.RESULT_KEY,
            this@PostCallActivity
        ) { _, result ->
            handleQuickMessage(result.getString(MessagesFragment.RESULT_TEXT))
        }
    }

    override fun ActivityPostCallBinding.initListeners() {
        callerAppIcon.setOnClickListener { goToApp() }
        mobileIcon.setOnClickListener { callingMethod() }
    }

    override fun ActivityPostCallBinding.initView() {

    }

    private fun applyWindowInsets(view: View, insets: WindowInsetsCompat): WindowInsetsCompat {
        val systemInsets: Insets = insets.getInsets(WindowInsetsCompat.Type.systemBars())
        view.setPadding(
            systemInsets.left,
            systemInsets.top,
            systemInsets.right,
            systemInsets.bottom
        )
        return insets
    }

    private fun handleQuickMessage(text: String?) {
        try {
            val intent = Intent("android.intent.action.VIEW", "sms:".toUri())
            intent.putExtra("sms_body", text)
            disabledOpenAds()
            startActivity(intent)
        } catch (unused: Exception) {
        }
    }

    private fun disabledOpenAds() {
        PostCallAdState.setOpenAdHide(true)
    }

    fun finishActivity() {
        PhoneCallStateReceiver.setCallEnded(false)
        nativeAdDestroy()
        postCallActivity = null
        try {
            PostCallAdState.setNativeListener(null, null)
        } catch (ignored: Exception) {
        }
        finishAffinity()
    }

    public override fun onResume() {
        super.onResume()
        disabledOpenAds()
        if (!isFirstOpenScreen) {
            isFirstOpenScreen = true
        } else if (isNativeGooglePostCallValue && !isBannerPostCall) {
            val hasNative = nativeAdsPostCallAd != null
            AfterCallLog.d(ACTIVITY, "onResume: nativePresent=$hasNative")
            if (nativeAdsPostCallAd != null) {
                if (System.currentTimeMillis() - adNativeLoadTimeValue >= adNativeExpirationTime) {
                    AfterCallLog.i(ACTIVITY, "onResume: native expired — falling back to banner")
                    nativeADs?.onDestroyAdView()
                    binding?.loadBannerADs()
                    return
                }
                AfterCallLog.d(ACTIVITY, "onResume: native still fresh")
            }
        } else {
            val isReLoad = PostCallAdState.isReLoadBannerAdsValue
            AfterCallLog.d(ACTIVITY, "onResume: bannerReload=$isReLoad")
            if (PostCallAdState.isReLoadBannerAdsValue) binding?.loadBannerADs()
        }
    }

    override fun onPause() {
        runCatching {
            bannerAdViewWidget?.pause()
        }.onFailure {
            AfterCallLog.w(ACTIVITY, "onPause: banner ad pause failed", it)
        }
        super.onPause()
    }

    private fun ActivityPostCallBinding.initAds() {
        isBannerPostCall = isBannerLoad(intent.getIntExtra(CALL_COUNTER, 0))
        bannerAds = PostCallBannerAd()
        nativeADs = PostCallNativeAd(this@PostCallActivity)
        cardViewNative.visibility = View.VISIBLE
        nativeADs.showLoadingLayoutNative(frameNative)
        if (isBannerPostCall) {
            nativeADs.initNativeListener(
                onLoad = {
                    AfterCallLog.d(ACTIVITY, "native listener (banner-mode): loaded")
                    cardViewNative.visibility = View.VISIBLE
                    nativeADs.showNative(frameNative, nativeAdsPostCallAd, false)
                },
                onFailed = {
                    AfterCallLog.w(ACTIVITY, "native listener (banner-mode): failed — hiding slot")
                    frameNative.removeAllViews()
                    frameNative.visibility = View.GONE
                    cardViewNative.visibility = View.GONE
                    if (isCallingStart) return@initNativeListener
                    loadBannerADs()
                }
            )
        }
        AfterCallLog.d(
            ACTIVITY,
            "initAds: bannerMode=$isBannerPostCall nativeCached=${nativeAdsPostCallAd != null} nativeFailed=$isNativeFailedGooglePostCallValue"
        )
        if (isOpenFromNotification && !isBannerPostCall && nativeAdsPostCallAd == null && !isNativeFailedGooglePostCallValue) {
            AfterCallLog.d(ACTIVITY, "initAds: requesting native (from notification)")
            isNativeGooglePostCallValue = true
            nativeADs.loadNativeAds()
        }
        if (isNativeGooglePostCallValue && !isBannerPostCall) {
            callBannerNativeAd()
        } else {
            loadBannerADs()
        }
    }


    private fun ActivityPostCallBinding.callBannerNativeAd() {
        if (nativeAdsPostCallAd != null &&
            !isNativePostCallLoadingValue &&
            System.currentTimeMillis() - adNativeLoadTimeValue >= adNativeExpirationTime
        ) {
            adNativeLoadTimeValue = 0L
            nativeAdsPostCallAd = null
            nativeADs.loadPostNative()
            AfterCallLog.i(ACTIVITY, "native cache expired — reloading")
        }
        loadNativeAds(frameNative, cardViewNative)
    }

    fun ActivityPostCallBinding.loadNativeAds(frameLayout: FrameLayout, cardViewADs: View) {
        cardViewADs.visibility = View.VISIBLE
        val isLoading = isNativePostCallLoadingValue
        AfterCallLog.d(ACTIVITY, "loadNativeAds: loading=$isLoading hasAd=${nativeAdsPostCallAd != null}")
        if (isNativePostCallLoadingValue) {
            return
        }
        frameLayout.removeAllViews()
        if (nativeAdsPostCallAd != null) {
            nativeADs.showNative(frameLayout, nativeAdsPostCallAd, false)
            return
        }
        cardViewADs.visibility = View.GONE
        loadBannerADs()
    }

    fun ActivityPostCallBinding.loadBannerADs() {
        AfterCallLog.d(ACTIVITY, "loadBannerADs: nativeFailed=$isNativeFailedGooglePostCallValue")
        if (isFinishing || isDestroyed) return
        if (isNativeFailedGooglePostCallValue || !isNativePostCallLoadingValue || nativeAdsPostCallAd == null) {
            val postCallBannerAds = PostCallBannerAd()
            if (frameNative.isEmpty()) {
                nativeADs.showLoadingLayoutNative(frameNative)
            }
            if (isBannerAdExpired()) {
                postCallBannerAds.onAdExpired(this@PostCallActivity)
            } else {
                postCallBannerAds.loadBanner(this@PostCallActivity, frameNative, cardViewNative)
            }
            postCallBannerAds.initBannerListener({
                AfterCallLog.i(ACTIVITY, "banner loaded — replacing native slot")
                destroyUnusedNative()
                frameNative.removeAllViews()
                frameNative.addView(bannerAdViewWidget)
            }, {
                AfterCallLog.w(ACTIVITY, "banner failed — falling back to native")
                if (isCallingStart) return@initBannerListener
                nativeADs.loadPostNative()
                nativeADs.initNativeListener(
                    {
                        AfterCallLog.d(ACTIVITY, "native listener (after banner-fail): loaded")
                        cardViewNative.visibility = View.VISIBLE
                        nativeADs.showNative(
                            frameNative,
                            nativeAdsPostCallAd,
                            false
                        )
                    },

                    {
                        AfterCallLog.w(ACTIVITY, "native listener (after banner-fail): also failed — hiding slot")
                        frameNative.removeAllViews()
                        frameNative.visibility = View.GONE
                        cardViewNative.visibility = View.GONE
                    }
                )
            }
            )
        }
    }

    private fun isBannerAdExpired(): Boolean {
        if (bannerAdViewWidget == null ||
            isBannerAdLoading ||
            System.currentTimeMillis() - adBannerLoadTimeValue < adBannerExpirationTime
        ) {
            return false
        }
        AfterCallLog.i(ACTIVITY, "banner cache expired")
        return true
    }

    class ViewPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
        FragmentStateAdapter(fragmentManager, lifecycle) {
        override fun containsItem(itemId: Long): Boolean {
            return itemId in 0..<3
        }

        override fun getItemCount(): Int {
            return 3
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun createFragment(position: Int): Fragment {
            return when (position) {
                0 -> DefaultMsgFragment()
                1 -> MessagesFragment()
                2 -> OptionsFragment()
                else -> DefaultMsgFragment()
            }
        }
    }

    private fun removeActivity() {
        homeWatcher = HomeWatcher(this).also { watcher ->
            watcher.setOnHomePressedListener(object : HomeWatcher.OnHomePressedListener {
            override fun onHomeLongPressed() {
            }

            override fun onHomePressed() {
                nativeAdDestroy()
                finishAffinity()
            }
        })
            watcher.startWatch()
        }
    }

    private fun ActivityPostCallBinding.initData() {
        try {
            isOpenFromNotification = intent.getBooleanExtra(IS_OPEN_FROM_NOTIFICATION, false)
            mobileNumber = intent.getStringExtra(EXTRA_MOBILE_NUMBER) ?: "null"
            callTime = intent.getSerializableExtra(CALL_TIME)?.toString() ?: "null"
            callType = intent.getStringExtra(CALL_TYPE) ?: "null"
            val timeDiff = DurationFormatter.getTimeDiff(
                intent.getLongExtra(START_TIME, 0L),
                intent.getLongExtra(END_TIME, 0L)
            )
            try {
                viewPager.adapter = ViewPagerAdapter(supportFragmentManager, lifecycle)
                viewPager.isSaveEnabled = false
            } catch (unused: Exception) {
            }
            callerTime.text = DurationFormatter.extractTime(callTime)
            callerDuration.text = timeDiff
            callerType.text = callType
            TabLayoutMediator(tbCallerCategory, viewPager) { tab, position ->
                when (position) {
                    0 -> tab.icon =
                        ContextCompat.getDrawable(
                            this@PostCallActivity,
                            R.drawable.post_ic_menu_tab_post_call
                        )

                    1 -> tab.icon =
                        ContextCompat.getDrawable(
                            this@PostCallActivity,
                            R.drawable.post_ic_message_tab_post_call
                        )

                    2 -> tab.icon =
                        ContextCompat.getDrawable(
                            this@PostCallActivity,
                            R.drawable.post_ic_more_tab_post_call
                        )
                }
            }.attach()
            tbCallerCategory.addOnTabSelectedListener(object :
                TabLayout.OnTabSelectedListener {
                override fun onTabReselected(tab: TabLayout.Tab) {
                }

                override fun onTabUnselected(tab: TabLayout.Tab) {
                }

                override fun onTabSelected(tab: TabLayout.Tab) {
                    viewPager.currentItem = tab.position
                }
            })
        } catch (e: Exception) {
            AfterCallLog.e(ACTIVITY, "initData failed", e)
        }
    }

    private fun goToApp() {
        val intent = AfterCallConfig.buildHostLaunchIntent(this)
        if (intent != null) startActivity(intent)
        finish()
    }

    private fun callingMethod() {
        val intent = Intent(Intent.ACTION_DIAL, "tel:".toUri())
        if (intent.resolveActivity(packageManager) != null) {
            startActivity(intent)
        } else {
            startActivity(Intent.createChooser(intent, null))
        }
    }

    @SuppressLint("GestureBackNavigation")
    override fun onBackPressed() {
        nativeAdDestroy()
        super.onBackPressed()
        finish()
    }

    override fun onDestroy() {
        runCatching {
            homeWatcher?.stopWatch()
        }.onFailure {
            AfterCallLog.w(ACTIVITY, "stopWatch failed", it)
        }
        homeWatcher = null
        nativeAdDestroy()
        super.onDestroy()
    }

    fun nativeAdDestroy() {
        PostCallAdState.setNativeFailedGooglePostCall(false)
        PostCallAdState.setReLoadBannerAds(false)
        PostCallAdState.setNativeListener(null, null)
        PostCallAdState.setBannerListener(null, null)
        if (this::nativeADs.isInitialized) {
            nativeADs.onDestroyAd()
        }
        val hasBanner = getBannerAdView() != null
        AfterCallLog.d(ACTIVITY, "nativeAdDestroy: hadBanner=$hasBanner")
        if (this::bannerAds.isInitialized) {
            bannerAds.onDestroyAd()
        }
    }

    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        return try {
            val focusView = currentFocus
            val result = super.dispatchTouchEvent(event)
            if (focusView is EditText) {
                val currentFocusView = currentFocus
                if (currentFocusView != null) {
                    val location = IntArray(2)
                    currentFocusView.getLocationOnScreen(location)
                    val rawX = event.rawX
                    val rawY = event.rawY
                    val adjustedX = (rawX + currentFocusView.left) - location[0]
                    val adjustedY = (rawY + currentFocusView.top) - location[1]
                    if (event.action == MotionEvent.ACTION_UP &&
                        (adjustedX < currentFocusView.left ||
                                adjustedX >= currentFocusView.right ||
                                adjustedY < currentFocusView.top ||
                                adjustedY > currentFocusView.bottom)
                    ) {
                        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                        val token: IBinder? = window?.currentFocus?.windowToken
                        imm.hideSoftInputFromWindow(token, 0)
                        focusView.clearFocus()
                    }
                }
            }
            result
        } catch (unused: Exception) {
            super.dispatchTouchEvent(event)
        }
    }

    private fun setLockScreen() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            setShowWhenLocked(true)
            setTurnScreenOn(true)
        }

        window.addFlags(6815873)
        return
    }

    private fun manageNavigationBar() {
        window.decorView.systemUiVisibility = 514
    }
}
