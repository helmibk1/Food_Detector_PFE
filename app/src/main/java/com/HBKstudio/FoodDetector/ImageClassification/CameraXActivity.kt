package com.HBKstudio.FoodDetector.ImageClassification

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.SystemClock
import android.util.Size
import android.view.TextureView
import android.widget.Toast
import androidx.annotation.UiThread
import androidx.annotation.WorkerThread
import androidx.camera.core.*
import androidx.camera.core.Preview.OnPreviewOutputUpdateListener
import androidx.camera.core.Preview.PreviewOutput
import androidx.core.app.ActivityCompat
import com.HBKstudio.FoodDetector.R

abstract class CameraXActivity : MainModuleActivity() {
    //time out for image analysis
    private var lastAnalysisResultTime: Long = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_classification)
        //checking camera permission before starting camera
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    this,
                    PERMISSIONS,
                    CAMERA_PERMISSION_CODE)
        } else {
            setupCamera()
        }
    }

    //requesting permission
    override fun onRequestPermissionsResult(
            requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == CAMERA_PERMISSION_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                Toast.makeText(
                        this,
                        "CAMERA permission is needed for the app to work",
                        Toast.LENGTH_LONG)
                        .show()
                finish()
            } else {
                setupCamera()
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setupCamera() {
        val textureView = findViewById<TextureView>(R.id.image_classification_texture_view)
        val previewConfig = PreviewConfig.Builder().build()
        val preview = Preview(previewConfig)
        //showing camera preview in the image classification activity
        preview.onPreviewOutputUpdateListener = OnPreviewOutputUpdateListener { output: PreviewOutput -> textureView.surfaceTexture = output.surfaceTexture }
        val imageAnalysisConfig = ImageAnalysisConfig.Builder()
                .setTargetResolution(Size(224, 224)) //handling image analysis on background thread
                .setCallbackHandler(backgroundHandler!!)
                .setImageReaderMode(ImageAnalysis.ImageReaderMode.ACQUIRE_LATEST_IMAGE)
                .build()
        val imageAnalysis = ImageAnalysis(imageAnalysisConfig)
        imageAnalysis.analyzer = ImageAnalysis.Analyzer setAnalyzer@{ image: ImageProxy?, rotationDegrees: Int ->
            //if time out return
            if (SystemClock.elapsedRealtime() - lastAnalysisResultTime < 500) {
                return@setAnalyzer
            }
            val result = imagePredictionResult(image, rotationDegrees)
            if (result != null) {
                lastAnalysisResultTime = SystemClock.elapsedRealtime()
                //running on UI thread to be able to update UI views
                runOnUiThread { imagePredictionResultToUi(result) }
            }
        }
        CameraX.bindToLifecycle(this, preview, imageAnalysis)
    }

    @WorkerThread
    protected abstract fun imagePredictionResult(image: ImageProxy?, rotationDegrees: Int): FloatArray?

    @UiThread
    protected abstract fun imagePredictionResultToUi(result: FloatArray?)

    companion object {
        //list of permissions to request from user
        private val PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val CAMERA_PERMISSION_CODE = 200
    }
}