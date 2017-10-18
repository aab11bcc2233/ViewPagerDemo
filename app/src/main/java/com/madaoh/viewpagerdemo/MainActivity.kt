package com.madaoh.viewpagerdemo

import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.util.SparseArray
import android.view.ViewTreeObserver
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_main.*

/**
 *       - - - - - -
 * - - - - - -
 *
 * 有 6个 分类，上面每个 "-" 代表一个分类，
 * 但是只有上面 上下相交的部分，是屏幕中显示的真实分类。
 *
 * 其它左右各 3 个是假的，只是为了计算分类底下的 Indicator(指示符) 的滑动距离。
 */
class MainActivity : AppCompatActivity() {
    var width = 0
    var textViews: Array<TextView>? = null
    var lastSelectedIndex = 0

    val listener = object : ViewTreeObserver.OnGlobalLayoutListener {
        @RequiresApi(Build.VERSION_CODES.JELLY_BEAN)
        override fun onGlobalLayout() {
            width = text0.measuredWidth
            ld("width = $width")

            line1.layoutParams.width = width
            line2.layoutParams.width = width

            line2.translationX = -(width * 3).toFloat()

            text0.viewTreeObserver.removeOnGlobalLayoutListener(this)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        textViews = arrayOf(text0, text1, text2, text3, text4, text5)

        text0.viewTreeObserver.addOnGlobalLayoutListener(listener)

        viewPager.adapter = MyAdapter(supportFragmentManager)

        textViews!![lastSelectedIndex].isSelected = true



        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

                val line1TranslationX = (position + positionOffset) * width

                line2.translationX += line1TranslationX - line1.translationX

                line1.translationX = line1TranslationX


            }

            override fun onPageSelected(position: Int) {
                textViews!![lastSelectedIndex].isSelected = false
                textViews!![position].isSelected = true
                lastSelectedIndex = position
            }
        })
    }


    private fun ld(msg: String) {
        Log.d("MainActivity", msg)
    }

    class MyAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {
        var fragments: SparseArray<ContentFragment> = SparseArray()

        init {
            for (i in 0..5) {
                var f = ContentFragment()
                f.position = i
                fragments.put(i, f)
            }
        }

        override fun getItem(position: Int): Fragment = fragments[position]

        override fun getCount(): Int = fragments.size()

    }
}
