package com.HBKstudio.FoodDetector.ImageClassification

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.annotation.WorkerThread
import androidx.camera.core.ImageProxy
import com.HBKstudio.FoodDetector.DescriptionActivity
import com.HBKstudio.FoodDetector.R
import com.HBKstudio.FoodDetector.Utils.assetFilePath
import com.HBKstudio.FoodDetector.Utils.toBitmap
import com.HBKstudio.FoodDetector.Utils.topK
import org.pytorch.IValue
import org.pytorch.Module
import org.pytorch.torchvision.TensorImageUtils
import java.io.File
import java.util.*

class ImageClassificationActivity : CameraXActivity() {
    //stopping image analysis if an error accrued
    private var analyzeImageError = false

    //ui views and resource id to update when we get analysis result
    private val resultViews = arrayOfNulls<TextView>(TOP_K)
    private val resultViewsID = intArrayOf(R.id.result_row_1_name_text, R.id.result_row_2_name_text, R.id.result_row_3_name_text)
    private val scoreViews = arrayOfNulls<TextView>(TOP_K)
    private val scoreViewsID = intArrayOf(R.id.result_row_1_score_text, R.id.result_row_2_score_text, R.id.result_row_3_score_text)
    private val rowViews = arrayOfNulls<RelativeLayout>(TOP_K)
    private val rowViewsID = intArrayOf(R.id.result_row_1, R.id.result_row_2, R.id.result_row_3)
    private var module: Module? = null

    @SuppressLint("CutPasteId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val headerResultRowView = findViewById<TextView>(R.id.header_row_name_text)
        headerResultRowView.setText(R.string.results_header_row_name)
        val headerNameRowView = findViewById<TextView>(R.id.header_row_score_text)
        headerNameRowView.setText(R.string.results_header_row_score)
        for (i in 0 until TOP_K) {
            resultViews[i] = findViewById(resultViewsID[i])
            scoreViews[i] = findViewById(scoreViewsID[i])
            rowViews[i] = findViewById(rowViewsID[i])
        }
    }

    //this function will be executed in background thread
    @WorkerThread
    override fun imagePredictionResult(image: ImageProxy?, rotationDegrees: Int): FloatArray? {
        //exit function if there is an error
        return if (analyzeImageError) {
            null
        } else try {
            if (module == null) {
                //loading trained model from assets folder
                val moduleFileAbsoluteFilePath = File(assetFilePath(this, MODEL_NAME)).absolutePath
                module = Module.load(moduleFileAbsoluteFilePath)
            }
            //transforming imageProxy from camera api to bitmap
            val bitmap = toBitmap(image?.image!!)
            //getting normalized tensor from bitmap image
            val mInputTensor = TensorImageUtils.bitmapToFloat32Tensor(bitmap,
                    TORCHVISION_NORM_MEAN_RGB, TORCHVISION_NORM_STD_RGB)
            //tensor forward pass in module
            val outputTensor = module!!.forward(IValue.from(mInputTensor)).toTensor()
            outputTensor.dataAsFloatArray
        } catch (e: Exception) {
            Log.e(TAG, "Error during image analysis", e)
            analyzeImageError = true
            //showing error on Ui thread
            runOnUiThread {
                if (!isFinishing) {
                    showErrorDialog {
                        finish()
                    }
                }
            }
            null
        }
    }

    //this function will be executed in Ui thread
    override fun imagePredictionResultToUi(result: FloatArray?) {
        //getting top k index list in scores array
        val indexList = result?.let { topK(it, TOP_K) }
        //top class names ans scores array holders
        val topKClassNames = arrayOfNulls<String>(TOP_K)
        val topKScores = FloatArray(TOP_K)
        for (i in 0 until TOP_K) {
            val ix = indexList?.get(i)
            topKClassNames[i] = FOOD_CLASSES[ix!!]
            topKScores[i] = result.get(index = ix)
        }

        //applying result to ui views
        for (i in 0 until TOP_K) {
            resultViews[i]!!.text = topKClassNames[i]
            scoreViews[i]!!.text = String.format(Locale.US, SCORES_FORMAT,
                    topKScores[i])
            //setting on click listener to launch description activity and passing selected class
            rowViews[i]!!.setOnClickListener { v: View ->
                val text = (v as RelativeLayout).getChildAt(0) as TextView
                val intent = Intent(this@ImageClassificationActivity, DescriptionActivity::class.java)
                intent.putExtra("FOODNAME", text.text)
                startActivity(intent)
            }
        }
    }

    //clear model memory when activity destroyed
    override fun onDestroy() {
        super.onDestroy()
        if (module != null) {
            module!!.destroy()
        }
    }

    companion object {
        const val MODEL_NAME = "Food_model.pt"
        const val TAG = "image_classification"
        var FOOD_CLASSES = arrayOf(
                "molokhia", "kosksi", "fricassé", "lablabi", "brik", "chappati", "zlabia",
                "baklawa", "makroudh", "kaak warka"
        )

        //normalisation for images from camera
        var TORCHVISION_NORM_MEAN_RGB = floatArrayOf(0.485f, 0.456f, 0.406f)
        var TORCHVISION_NORM_STD_RGB = floatArrayOf(0.229f, 0.224f, 0.225f)
        const val SCORES_FORMAT = "%.2f"

        //showing only 3 top results
        private const val TOP_K = 3
    }
}