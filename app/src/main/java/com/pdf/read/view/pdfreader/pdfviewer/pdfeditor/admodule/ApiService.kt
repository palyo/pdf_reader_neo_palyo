package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.admodule

import retrofit2.*
import retrofit2.http.*

interface ApiService {

    @GET
    fun getConfig(@Url url: String): Call<ConfigJson>
}