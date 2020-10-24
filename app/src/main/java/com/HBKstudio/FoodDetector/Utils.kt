package com.HBKstudio.FoodDetector

import android.content.Context
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.media.Image
import android.util.Log
import com.HBKstudio.FoodDetector.ImageClassification.ImageClassificationActivity
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.*

object Utils {
    //getting module path from user phone
    @JvmStatic
    fun assetFilePath(context: Context, assetName: String): String? {
        val file = File(context.filesDir, assetName)
        if (file.exists() && file.length() > 0) {
            Log.v("wtf", file.absolutePath)
            return file.absolutePath
        }
        try {
            context.assets.open(assetName).use { `is` ->
                FileOutputStream(file).use { os ->
                    val buffer = ByteArray(4 * 1024)
                    var read: Int
                    while (`is`.read(buffer).also { read = it } != -1) {
                        os.write(buffer, 0, read)
                    }
                    os.flush()
                }
                return file.absolutePath
            }
        } catch (e: IOException) {
            Log.e(ImageClassificationActivity.TAG, "Error process asset $assetName to file path")
        }
        return null
    }

    //return top k elements index in a target list
    @JvmStatic
    fun topK(list: FloatArray, topk: Int): IntArray {
        val values = FloatArray(topk)
        Arrays.fill(values, -Float.MAX_VALUE)
        val indexList = IntArray(topk)
        Arrays.fill(indexList, -1)
        for (i in list.indices) {
            for (j in 0 until topk) {
                if (list[i] > values[j]) {
                    for (k in topk - 1 downTo j + 1) {
                        values[k] = values[k - 1]
                        indexList[k] = indexList[k - 1]
                    }
                    values[j] = list[i]
                    indexList[j] = i
                    break
                }
            }
        }
        return indexList
    }

    // round bitmap image of a view
    fun getRoundedCornerBitmap(r: Int, c: Context): Bitmap {
        val mbitmap = (c.resources.getDrawable(r) as BitmapDrawable).bitmap
        val imageRounded = Bitmap.createBitmap(mbitmap.width, mbitmap.height, mbitmap.config)
        val canvas = Canvas(imageRounded)
        val mpaint = Paint()
        mpaint.isAntiAlias = true
        mpaint.shader = BitmapShader(mbitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
        canvas.drawRoundRect(RectF(0F, 0F, mbitmap.width.toFloat(), mbitmap.height.toFloat()), 50f, 50f, mpaint) // Round Image Corner 50 50 50 50
        return imageRounded
    }

    //transform image to a bitmap
    @JvmStatic
    fun toBitmap(image: Image): Bitmap {
        val planes = image.planes
        val yBuffer = planes[0].buffer
        val uBuffer = planes[1].buffer
        val vBuffer = planes[2].buffer
        val ySize = yBuffer.remaining()
        val uSize = uBuffer.remaining()
        val vSize = vBuffer.remaining()
        val nv21 = ByteArray(ySize + uSize + vSize)
        //U and V are swapped
        yBuffer[nv21, 0, ySize]
        vBuffer[nv21, ySize, vSize]
        uBuffer[nv21, ySize + vSize, uSize]
        val yuvImage = YuvImage(nv21, ImageFormat.NV21, image.width, image.height, null)
        val out = ByteArrayOutputStream()
        yuvImage.compressToJpeg(Rect(0, 0, yuvImage.width, yuvImage.height), 75, out)
        val imageBytes = out.toByteArray()
        return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
    }
}