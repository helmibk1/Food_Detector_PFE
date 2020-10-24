package com.HBKstudio.FoodDetector.ImageClassification

import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.annotation.UiThread
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.HBKstudio.FoodDetector.R

open class MainModuleActivity : AppCompatActivity() {
    private var backgroundThread: HandlerThread? = null

    @JvmField
    protected var backgroundHandler: Handler? = null
    private var mainUIHandler: Handler? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainUIHandler = Handler(mainLooper)
        startBackgroundThread()
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        //showing toolbar at the top of the screen in ImageClassificationActivity
        if (toolbar != null) {
            setSupportActionBar(toolbar)
            supportActionBar!!.setDisplayShowTitleEnabled(false)
        }
        //in case onCreate() misses the call of startBackgroundThread();
        startBackgroundThread()
    }

    private fun startBackgroundThread() {
        backgroundThread = HandlerThread("ModuleActivity")
        backgroundThread!!.start()
        backgroundHandler = Handler(backgroundThread!!.looper)
    }

    private fun stopBackgroundThread() {
        backgroundThread!!.quitSafely()
        try {
            backgroundThread!!.join()
            backgroundThread = null
            backgroundHandler = null
        } catch (e: InterruptedException) {
            Log.e(ImageClassificationActivity.TAG, "Error on stopping background thread", e)
        }
    }

    //stopping background thread when activity is destroyed
    override fun onDestroy() {
        stopBackgroundThread()
        super.onDestroy()
    }

    //function to show error during thread loop
    @UiThread
    protected fun showErrorDialog(clickListener: View.OnClickListener) {
        val inflater = LayoutInflater.from(this)
        val view = inflater.inflate(R.layout.error_dialog, null, false)
        val builder = AlertDialog.Builder(this, R.style.DialogStyle)
                .setCancelable(false)
                .setView(view)
        val alertDialog = builder.show()
        view.setOnClickListener { v: View? ->
            clickListener.onClick(v)
            alertDialog.dismiss()
        }
    }
}