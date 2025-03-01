package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.adapter

import android.content.*
import android.view.*
import androidx.viewpager.widget.*
import com.bumptech.glide.*
import com.google.android.material.imageview.*
import com.google.android.material.textview.MaterialTextView
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.R

class IntroViewPagerAdapter(var context: Context) : PagerAdapter() {
    var screens: MutableList<Int> = mutableListOf()
    var titles: MutableList<Int> = mutableListOf()
    var descriptions: MutableList<Int> = mutableListOf()

    init {
        screens = mutableListOf(R.drawable.ic_intro_1, R.drawable.ic_intro_2, R.drawable.ic_intro_3)
        titles = mutableListOf(R.string.ic_intro_title_1, R.string.ic_intro_title_2, R.string.ic_intro_title_3)
        descriptions = mutableListOf(R.string.ic_intro_body_1, R.string.ic_intro_body_2, R.string.ic_intro_body_3)
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val layoutScreen: View = inflater.inflate(R.layout.layout_intro_screen, null)
        val imgSlide = layoutScreen.findViewById<ShapeableImageView>(R.id.image_view)
        val title = layoutScreen.findViewById<MaterialTextView>(R.id.text_title)
        val body = layoutScreen.findViewById<MaterialTextView>(R.id.text_body)
        Glide.with(layoutScreen.context).load(screens[position]).into(imgSlide)
        title.text = context.getString(titles[position])
        body.text = context.getString(descriptions[position])
        container.addView(layoutScreen)
        return layoutScreen
    }

    override fun getCount(): Int {
        return screens.size
    }

    override fun isViewFromObject(view: View, o: Any): Boolean {
        return view == o
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }
}
