package com.tomtruyen.budgettracker

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import androidx.viewpager2.widget.ViewPager2


class MainActivity : AppCompatActivity() {
    private lateinit var mPager : ViewPager2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mPager = findViewById(R.id.card_pager)

        val pagerAdapter = CardSlidePagerAdapter(this)
        mPager.adapter = pagerAdapter
    }

    override fun onBackPressed() {
        if(mPager.currentItem == 0) {
            super.onBackPressed()
        } else {
            mPager.currentItem = mPager.currentItem - 1
        }
    }
}
