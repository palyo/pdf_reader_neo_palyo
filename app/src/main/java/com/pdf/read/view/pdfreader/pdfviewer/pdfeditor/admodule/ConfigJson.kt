package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.admodule

import android.os.*
import com.google.gson.annotations.*
import kotlinx.parcelize.*

@Parcelize
data class ConfigJson(

    @field:SerializedName("app_name")
    val appName: String? = null,

    @field:SerializedName("package_name")
    val packageName: String? = null,

    @field:SerializedName("policy_url")
    val policyUrl: String? = null,

    @field:SerializedName("app_id")
    val appId: String? = null,

    @field:SerializedName("open")
    val openId: String? = null,

    @field:SerializedName("inter")
    val interId: String? = null,

    @field:SerializedName("native")
    val nativeId: String? = null,

    @field:SerializedName("banner")
    val bannerId: String? = null,

    @field:SerializedName("is_start_flow_repeat")
    val isStartFlowRepeat: Boolean? = null
) : Parcelable

