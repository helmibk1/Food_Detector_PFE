package com.HBKstudio.FoodDetector

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.HBKstudio.FoodDetector.ImageClassification.ImageClassificationActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //stating ImageClassification activity
        findViewById<View>(R.id.camera_view).setOnClickListener {
            val intent = Intent(this@MainActivity, ImageClassificationActivity::class.java)
            startActivity(intent)
        }
        //stating About activity
        findViewById<View>(R.id.about_view).setOnClickListener {
            val intent = Intent(this@MainActivity, AboutActivity::class.java)
            startActivity(intent)
        }
    }
}