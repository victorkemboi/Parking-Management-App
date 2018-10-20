package com.park254.app.park254.widgets

import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout

class BouncingDotsView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0

) : LinearLayout(context, attrs, defStyleAttr) {
    private val OBJECT_SIZE = 3
    private val POST_DIV = 5
    private val DURATION = 500
    private val circle = GradientDrawable()
    private lateinit var img: Array<ImageView?>
    private var onLayoutReach = false


    init {
        initView()
    }

    private fun initView() {
        var color = Color.GRAY
        val background = background
        if (background is ColorDrawable) {
            color = background.color
        }
        setBackgroundColor(Color.TRANSPARENT)

        removeAllViews()
        img = arrayOfNulls(OBJECT_SIZE)
        circle.shape = GradientDrawable.OVAL
        circle.setColor(color)
        circle.setSize(200, 200)

        val layoutParams2 = LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT)
        layoutParams2.weight = 1f

        val rel = arrayOfNulls<LinearLayout>(OBJECT_SIZE)
        for (i in 0 until OBJECT_SIZE) {
            rel[i] = LinearLayout(context)
            rel[i]?.gravity = Gravity.CENTER
            rel[i]?.layoutParams = layoutParams2
            img[i] = ImageView(context)
            img[i]?.background   = circle

            rel[i]?.addView(img[i])
            addView(rel[i])
        }
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        super.onLayout(changed, l, t, r, b)
        if (!onLayoutReach) {
            onLayoutReach = true
            val lp = LinearLayout.LayoutParams(width / 5, width / 5)
            for (i in 0 until OBJECT_SIZE) {
                img[i]?.layoutParams = lp
            }
            animateView()
        }
    }

    private fun animateView() {
        val animator = arrayOfNulls<ObjectAnimator>(OBJECT_SIZE)
        for (i in 0 until OBJECT_SIZE) {
            img[i]?.translationY = (height / POST_DIV).toFloat()
            val Y = PropertyValuesHolder.ofFloat(View.TRANSLATION_Y, (-height / POST_DIV).toFloat())
            val X = PropertyValuesHolder.ofFloat(View.TRANSLATION_X, 0.toFloat())
            animator[i] = ObjectAnimator.ofPropertyValuesHolder(img[i], X, Y)
            animator[i]?.repeatCount = -1
            animator[i]?.repeatMode = ValueAnimator.REVERSE
            animator[i]?.duration = DURATION.toLong()
            animator[i]?.startDelay = (DURATION / 3 * i).toLong()
            animator[i]?.start()
        }

    }


}