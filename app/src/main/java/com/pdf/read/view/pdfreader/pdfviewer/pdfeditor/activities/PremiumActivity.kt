package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.activities

import android.util.*
import android.widget.*
import androidx.activity.*
import androidx.lifecycle.*
import coder.apps.space.library.base.*
import coder.apps.space.library.extension.*
import com.limurse.iap.*
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.admodule.*
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.databinding.*

class PremiumActivity : BaseActivity<ActivityPremiumBinding>(ActivityPremiumBinding::inflate) {

    private var iapConnector: IapConnector? = null
    private val subsList = listOf("subscription_lifetime")
    val isBillingClientConnected: MutableLiveData<Boolean> = MutableLiveData()
    private var restoreKey: String? = null

    override fun ActivityPremiumBinding.initView() {
        updateStatusBarColor(coder.apps.space.library.R.color.colorWhite)
        onBackPressedDispatcher.addCallback(this@PremiumActivity, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                goNext()
            }
        })
    }

    override fun ActivityPremiumBinding.initListeners() {
        buttonRestore.setOnClickListener {
            if (restoreKey.isNullOrEmpty()) {
                Toast.makeText(this@PremiumActivity, "You don't have any Subscription", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            updatePremium()
        }

        buttonClose.setOnClickListener {
            goNext()
        }
    }

    override fun ActivityPremiumBinding.initExtra() {
        isBillingClientConnected.value = false
        iapConnector = IapConnector(
            context = this@PremiumActivity,
            nonConsumableKeys = subsList,
            consumableKeys = arrayListOf(),
            subscriptionKeys = arrayListOf(),
            key = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAoU2V+jOBlz47MMp2UIxn6ji0kgRJw0AGdGX5K7nBcWh/869RufxLnqo+RpuLFLgMHcVlcRW2d8vLDq6zzlt3iTvrqbThswBI1IlkZ22ot+UTV2RP4no4jJF7eqcJSPwoKIM6L8t1aLa2rvlwI8K58G5x03tvpNQHp4sD8SLWFKrhXFuET9CCMIiSr4FKoo0YWtK1yprMOu3s+ddKjp/iWYNv3iX5SU7FhJ1uXuxIeZShX7Ulgy7Oji+ACJfoQgYU6xpODoCA7ymEtih4SI3Tojzwg2nMfMvGH3KzUC2Uizd9UAg19PN9H4WHQVjKlRlzGRBR791a3ufDil9vYMl8UwIDAQAB",
            enableLogging = true
        )

        iapConnector?.addBillingClientConnectionListener(object : BillingClientConnectionListener {
            override fun onConnected(status: Boolean, billingResponseCode: Int) {
                isBillingClientConnected.value = status
            }
        })
        isBillingClientConnected.observe(this@PremiumActivity) { connected ->
            when (connected) {
                true -> {
                    actionPremium.isEnabled = true
                    actionPremium.setOnClickListener {
                        iapConnector?.purchase(this@PremiumActivity, "subscription_lifetime")
                    }
                }

                else -> {
                    actionPremium.isEnabled = false
                }
            }
        }

        iapConnector?.addPurchaseListener(object : PurchaseServiceListener {
            override fun onPricesUpdated(iapKeyPrices: Map<String, DataWrappers.ProductDetails>) {
                iapKeyPrices.forEach { (key, u) ->
                    u.offers?.forEach { offers ->
                        offers.pricingPhases.forEach {
                            when (key) {
                                "subscription_lifetime" -> planPrice.text = it.price
                            }
                        }
                    }
                }
            }

            override fun onProductPurchased(purchaseInfo: DataWrappers.PurchaseInfo) {
                purchased()
            }

            override fun onProductRestored(purchaseInfo: DataWrappers.PurchaseInfo) {
                restored(purchaseInfo)
            }

            override fun onPurchaseFailed(purchaseInfo: DataWrappers.PurchaseInfo?, billingResponseCode: Int?) {
                Log.e("TAG", "onPurchaseFailed: $billingResponseCode")
            }
        })

        buttonClose.apply {
            delayed(3000) {
                beVisible()
            }
        }
    }

    fun purchased() {
        updatePremium()
    }

    fun restored(purchaseInfo: DataWrappers.PurchaseInfo) {
        restoreKey = purchaseInfo.sku
    }

    private fun updatePremium() {
        isPremium = true
        Toast.makeText(this@PremiumActivity, "You are successfully Subscribed", Toast.LENGTH_SHORT).show()
        onGo()
    }

    private fun goNext() {
        finish()
    }

    private fun onGo() {
        go(HomeActivity::class.java, finishAll = true)
    }
}