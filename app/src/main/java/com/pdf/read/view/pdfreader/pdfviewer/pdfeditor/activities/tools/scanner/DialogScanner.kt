package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.activities.tools.scanner

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.app.Activity
import android.graphics.Color
import android.graphics.Point
import android.graphics.Rect
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.KeyEvent
import android.view.Surface
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.BounceInterpolator
import android.widget.ImageView
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraControl
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import coder.apps.space.library.base.BaseDialog
import coder.apps.space.library.extension.beGone
import coder.apps.space.library.extension.beInvisible
import coder.apps.space.library.extension.beInvisibleIf
import coder.apps.space.library.extension.beVisible
import coder.apps.space.library.extension.disable
import coder.apps.space.library.extension.enable
import coder.apps.space.library.extension.go
import coder.apps.space.library.extension.goResult
import coder.apps.space.library.extension.log
import coder.apps.space.library.extension.showToast
import coder.apps.space.library.helper.TinyDB
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.databinding.LayoutDialogScannerBinding
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.ext.deleteDirectory
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.ext.getPathFromURI
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.ext.singleClick
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.R
import java.io.File
import java.util.concurrent.Executor

class DialogScanner : BaseDialog<LayoutDialogScannerBinding>(LayoutDialogScannerBinding::inflate, isLightModified = true, isLight = false) {

    private var cameraControl: CameraControl? = null
    private var angle = 90
    private var executor: Executor? = null
    private var isDocumentCapture: Boolean = true
    private var imageCapture: ImageCapture? = null
    private var captures: MutableList<String> = mutableListOf()
    private var capturesNew: MutableList<String> = mutableListOf()
    private var callback: OnBackPressedCallback? = null
    private var isNew: Boolean = true
    private var torchEnabled = false
    private var galleryPickerImage: ActivityResultLauncher<String>? = registerForActivityResult(
        ActivityResultContracts.GetMultipleContents()
    ) {
        activity?.apply context@{
            it.forEach {
                val path = getPathFromURI(this@context, it)
                captures.add(0, path.toString())
            }
        }
        if (captures.size > 0) {
            binding?.resultPicker()
        }
    }
    val resultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.apply {
                captures = getStringArrayListExtra("paths")?.toMutableList() ?: mutableListOf()
                if (captures.size > 0) {
                    activity?.apply {
                        binding?.apply {
                            groupCaptureCount.enable()
                            pageCount.text = "${captures.size}"
                            Glide.with(applicationContext).load(captures.get(0)).thumbnail(0.1f).apply(
                                RequestOptions().centerCrop().skipMemoryCache(
                                    false
                                ).dontTransform().dontAnimate()
                            ).into(viewCaptured)
                        }
                    }
                }
            }
        }
    }

    private fun LayoutDialogScannerBinding.resultPicker() {
        activity?.apply context@{
            progressCircular.beGone()
            if (!isNew) {
                if (!isDocumentCapture) {
                    listener?.invoke(captures)
                    Handler(mainLooper).postDelayed({
                        dialog?.dismiss()
                    }, 500)
                }
            }
            if (!isDocumentCapture) {
                groupCaptureCount.enable()
            } else {
                pageCount.text = "${captures.size}"
                Glide.with(applicationContext).load(captures[0]).thumbnail(0.1f).apply(
                    RequestOptions().centerCrop().skipMemoryCache(
                        false
                    ).dontTransform().dontAnimate()
                ).into(viewCaptured)
            }
        }
    }

    companion object {
        const val TAG = "DialogScanner"
        var listener: ((captures: MutableList<String>) -> Unit)? = null
        fun newInstance(
            isNew: Boolean = true, isTakeMultiple: Boolean = true,
            captured: MutableList<String>? = null,
            callback: (captures: MutableList<String>) -> Unit,
        ): DialogScanner {
            listener = callback
            return DialogScanner().apply {
                arguments = Bundle().apply {
                    putBoolean("isNew", isNew)
                    putBoolean("isTakeMultiple", isTakeMultiple)
                    if (!captured.isNullOrEmpty()) putStringArrayList("paths", ArrayList(captured))
                }
            }
        }
    }

    override fun create() {
        arguments?.apply {
            isNew = getBoolean("isNew", true)
            isDocumentCapture = getBoolean("isTakeMultiple", true)
            captures = getStringArrayList("paths")?.toMutableList() ?: mutableListOf()
        }
    }

    override fun LayoutDialogScannerBinding.viewCreated() {
        activity?.apply {
            if (isNew) {
                deleteDirectory(File(activity?.cacheDir, "camera_cache"))
                buttonHistory.beVisible()
            }
            if (!isNew) {
                viewCaptured.beInvisibleIf(isSingle.isChecked)
                pageCount.beInvisibleIf(isSingle.isChecked)
                groupCaptureCount.beInvisible()
                isMulti.isChecked = isDocumentCapture
                isSingle.isChecked = !isDocumentCapture
                if (captures.isNotEmpty()) {
                    pageCount.text = "${captures.size}"
                    Glide.with(applicationContext).load(captures[0]).thumbnail(0.1f).apply(
                        RequestOptions().centerCrop().skipMemoryCache(
                            false
                        ).dontTransform().dontAnimate()
                    ).into(viewCaptured)
                }
            }
        }
        cameraChaluKarein()
    }

    override fun LayoutDialogScannerBinding.initListeners() {
        activity?.apply context@{
            buttonHome.setOnClickListener {
                dialog?.dismiss()
                listener?.invoke(mutableListOf())
            }
            buttonCapture.singleClick {
                if (!isDocumentCapture) {
                    if (captures.size == 2) showToast("You can't capture more then 2 in ID Card")
                    return@singleClick
                }
                groupCaptureCount.disable()
                buttonCapture.disable()
                progressCircular.beVisible()
                val file = createTempFile()
                executor?.let { ex ->
                    imageCapture?.takePicture(ImageCapture.OutputFileOptions.Builder(file).build(), ex, object : ImageCapture.OnImageSavedCallback {
                        override fun onImageSaved(
                            outputFileResults: ImageCapture.OutputFileResults,
                        ) {
                            if (!isNew) {
                                captures.add(file.absolutePath)
                                capturesNew.add(file.absolutePath)
                            } else {
                                captures.add(0, file.absolutePath)

                                if (TinyDB(this@context).getBoolean("isShowcase", true) == true) {
                                    Handler(mainLooper).postDelayed({
                                        layoutShowcase.beVisible()
                                        val animator = ObjectAnimator.ofFloat(layoutShowcaseBubble, "translationY", 0f, -50f, 0f)
                                        animator.duration = 2000
                                        animator.interpolator = BounceInterpolator()
                                        animator.repeatCount = ValueAnimator.INFINITE
                                        animator.start()
                                    }, 1000)
                                    Glide.with(applicationContext).load(file).thumbnail(0.1f).apply(
                                        RequestOptions().centerCrop().skipMemoryCache(
                                            false
                                        ).dontTransform().dontAnimate()
                                    ).into(viewCaptured3)
                                }
                            }
                            Glide.with(applicationContext).load(file).thumbnail(0.1f).apply(
                                RequestOptions().centerCrop().skipMemoryCache(
                                    false
                                ).dontTransform().dontAnimate()
                            ).into(largeImageView)
                            largeImageView.visibility = View.VISIBLE
                            largeImageView.post {
                                animateImageView(
                                    largeImageView, viewCaptured, file.absolutePath
                                )
                            }
                            progressCircular.beGone()
                            if (!isNew) {
                                if (!isDocumentCapture) {
                                    listener?.invoke(captures)
                                    Handler(mainLooper).postDelayed({
                                        dialog?.dismiss()
                                    }, 500)
                                }
                            }
                            if (!isDocumentCapture) {
                                if (captures.size == 2) {
                                    listener?.invoke(captures)
                                } else {
                                    textNote.beVisible()
                                    textNote.text = getString(R.string.note_back_page)
                                    Handler(mainLooper).postDelayed({
                                        textNote.beGone()
                                    }, 3000)
                                }
                            }
                            pageCount.text = "${captures.size}"
                            pageCount3.text = "${captures.size}"
                            buttonCapture.enable()
                        }

                        override fun onError(error: ImageCaptureException) {
                            TAG.log(Log.getStackTraceString(error))
                        }
                    })
                }
            }
            groupCaptureCount.addOnButtonCheckedListener { group, checkedId, isChecked ->
                isDocumentCapture = isMulti.isChecked
                if (!isMulti.isChecked) {
                    textNote.beVisible()
                    textNote.text = getString(R.string.note_front_page)
                    Handler(mainLooper).postDelayed({
                        textNote.beGone()
                    }, 3000)
                }
            }
            buttonFlash.setOnClickListener {
                torchEnabled = !torchEnabled
                setTorch(torchEnabled)
            }
            viewCaptured.singleClick {
                if (captures.isEmpty()) {
                    showToast(getString(R.string.message_please_scan_one_document))
                    return@singleClick
                }
                if (isNew) {
                    TinyDB(this@context).putBoolean("isShowcase", false)
                    resultLauncher.launch(
                        goResult(
                            EditScannedActivity::class.java, listOf("paths" to ArrayList(captures), "isIDCard" to !isDocumentCapture)
                        )
                    )
                    Handler(mainLooper).postDelayed({
                        dialog?.dismiss()
                    }, 500)
                } else {
                    listener?.invoke(capturesNew)
                    Handler(mainLooper).postDelayed({
                        dialog?.dismiss()
                    }, 500)
                }
            }
            viewCaptured3.singleClick {
                if (captures.isEmpty()) {
                    showToast(getString(R.string.message_please_scan_one_document))
                    return@singleClick
                }
                if (isNew) {
                    TinyDB(this@context).putBoolean("isShowcase", false)
                    resultLauncher.launch(
                        goResult(
                            EditScannedActivity::class.java, listOf("paths" to ArrayList(captures), "isIDCard" to !isDocumentCapture)
                        )
                    )
                    Handler(mainLooper).postDelayed({
                        dialog?.dismiss()
                    }, 500)
                } else {
                    listener?.invoke(capturesNew)
                    Handler(mainLooper).postDelayed({
                        dialog?.dismiss()
                    }, 500)
                }
            }
            buttonGallery.singleClick {
                galleryPickerImage?.launch("image/*")
            }
            buttonHistory.singleClick {
                go(ListDocScannedActivity::class.java)
            }
        }
    }

    private fun animateImageView(startView: ImageView, endView: ImageView, file: String) {
        activity?.apply {
            val startBounds = Rect()
            val finalBounds = Rect()
            val globalOffset = Point()

            startView.getGlobalVisibleRect(startBounds)
            endView.getGlobalVisibleRect(finalBounds, globalOffset)

            startBounds.offset(-globalOffset.x, -globalOffset.y)
            finalBounds.offset(-globalOffset.x, -globalOffset.y)
            val startScale: Float = if ((finalBounds.width() / finalBounds.height()) > (startBounds.width() / startBounds.height())) {
                finalBounds.height().toFloat() / startBounds.height()
            } else {
                finalBounds.width().toFloat() / startBounds.width()
            }

            startView.pivotX = 0f
            startView.pivotY = 0f

            startView.animate().scaleX(startScale).scaleY(startScale).translationX(
                (finalBounds.left - startBounds.left).toFloat()
            ).translationY((finalBounds.top - startBounds.top).toFloat()).setDuration(
                500
            ).setInterpolator(AccelerateDecelerateInterpolator()).withEndAction {
                startView.visibility = View.GONE
                Glide.with(applicationContext).load(file).thumbnail(0.1f).apply(
                    RequestOptions().centerCrop().skipMemoryCache(
                        false
                    ).dontTransform().dontAnimate()
                ).into(endView)
                resetViews()
            }
        }
    }

    private fun resetViews() {
        binding?.largeImageView?.apply {
            scaleX = 1f
            scaleY = 1f
            translationX = 0f
            translationY = 0f
            visibility = View.GONE
        }
    }

    override fun LayoutDialogScannerBinding.initView() {
        executor = ContextCompat.getMainExecutor(requireActivity())
        handleBackPressed()
    }

    private fun cameraChaluKarein() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireActivity())
        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()
            bindPreview(cameraProvider)
        }, ContextCompat.getMainExecutor(requireActivity()))
    }

    private fun bindPreview(cameraProvider: ProcessCameraProvider) {
        val preview = Preview.Builder().build()
        val cameraSelector = CameraSelector.Builder().requireLensFacing(
            CameraSelector.LENS_FACING_BACK
        ).build()
        val builder = ImageCapture.Builder().setTargetRotation(
            binding?.viewFinder?.display?.rotation ?: Surface.ROTATION_0
        )
        imageCapture = builder.build()
        val imageAnalysis = ImageAnalysis.Builder().build()
        preview.setSurfaceProvider(binding?.viewFinder?.surfaceProvider)
        val camera = cameraProvider.bindToLifecycle(
            this, cameraSelector, preview, imageCapture, imageAnalysis
        )
        imageAnalysis.setAnalyzer(
            ContextCompat.getMainExecutor(requireActivity())
        ) { imageProxy: ImageProxy ->
            angle = imageProxy.imageInfo.rotationDegrees
            imageProxy.close()
        }
        cameraControl = camera.cameraControl
        cameraControl?.enableTorch(torchEnabled)
        binding?.buttonFlash?.isSelected = torchEnabled
    }

    private fun setTorch(enable: Boolean) {
        cameraControl?.enableTorch(enable)
        binding?.buttonFlash?.isSelected = torchEnabled
    }

    private fun handleBackPressed() {
        callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                listener?.invoke(mutableListOf())
                dialog?.dismiss()
            }
        }
        callback?.let { requireActivity().onBackPressedDispatcher.addCallback(this, it) }

        dialog?.setOnKeyListener { _, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_BACK && event.action == KeyEvent.ACTION_UP) {
                callback?.handleOnBackPressed()
                return@setOnKeyListener true
            }
            false
        }
    }

    override fun onStop() {
        super.onStop()
        torchEnabled = false
        setTorch(false)
    }

    override fun onDetach() {
        callback?.remove()
        super.onDetach()
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.apply {
            statusBarColor = Color.BLACK
        }
    }
}