package com.HBKstudio.FoodDetector

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout

class WelcomeActivity : AppCompatActivity() {

    //using viewPager to show a simple guide
    private lateinit var viewPager: ViewPager
    private lateinit var pagerAdapter: PagerAdapter
    private lateinit var tabLayout: TabLayout

    private class PageData(val titleResId: Int, val imageResId: Int, val textResId: Int)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)
        //stating Main Activity when skip Button clicked
        findViewById<View>(R.id.skip_button).setOnClickListener {
            startActivity(Intent(this@WelcomeActivity, MainActivity::class.java))
        }
        viewPager = findViewById(R.id.welcome_view_pager)
        pagerAdapter = WelcomeViewPagerAdapter()
        viewPager.adapter = pagerAdapter
        tabLayout = findViewById(R.id.welcome_tab_layout)
        tabLayout.setupWithViewPager(viewPager)
    }

    //PageAdapter class to handle instantiating items
    private inner class WelcomeViewPagerAdapter : PagerAdapter() {
        //number of pages
        override fun getCount(): Int {
            return PAGES_DATA.size
        }

        //update items method
        override fun isViewFromObject(view: View, `object`: Any): Boolean {
            return `object` === view
        }

        //instantiating a page with correct data
        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            val inflater = LayoutInflater.from(this@WelcomeActivity)
            val pageView = inflater.inflate(R.layout.welcome_pager_page, container, false)
            val titleTextView = pageView.findViewById<TextView>(R.id.welcome_pager_page_title)
            val descriptionTextView = pageView.findViewById<TextView>(R.id.welcome_pager_page_description)
            val imageView = pageView.findViewById<ImageView>(R.id.welcome_pager_page_image)
            val pageData = PAGES_DATA[position]
            titleTextView.setText(pageData.titleResId)
            descriptionTextView.setText(pageData.textResId)
            imageView.setImageResource(pageData.imageResId)
            container.addView(pageView)
            return pageView
        }

        //handel destroyed page
        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
            container.removeView(`object` as View)
        }
    }

    //stating object holding pages data
    companion object {
        private val PAGES_DATA = arrayOf(
                PageData(
                        R.string.welcome_page_title,
                        R.drawable.ic_food,
                        R.string.welcome_page_description),
                PageData(
                        R.string.welcome_page_Guide_title_1,
                        R.drawable.ic_plate,
                        R.string.welcome_page_Guide_description_1),
                PageData(
                        R.string.welcome_page_Guide_title_2,
                        R.drawable.ic_foodguide,
                        R.string.welcome_page_Guide_description_2)
        )
    }
}