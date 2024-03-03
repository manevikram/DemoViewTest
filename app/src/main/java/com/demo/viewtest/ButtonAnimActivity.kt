package com.demo.viewtest

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class ButtonAnimActivity : AppCompatActivity() {

    private lateinit var cardView : CardView
    private lateinit var btnTest : Button
    private val animDuration = 10000L
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_button_anim)

        cardView = findViewById(R.id.cardView)
        btnTest = findViewById(R.id.btn_animation)
        btnTest.viewTreeObserver.addOnGlobalLayoutListener {
            startAnimation(btnTest, btnTest.width, animDuration)
        }
    }

    @SuppressLint("Recycle")
    private fun startAnimation(button : Button, buttonWidth : Int, animDuration: Long){
        val bgDrawable = button.background as GradientDrawable
        val buttonAnimator = ValueAnimator.ofInt(0, buttonWidth).apply {
            duration = animDuration
            addUpdateListener {
                val value = it.animatedValue as Int
                bgDrawable.setBounds(0,0, value, button.height)
                button.background = bgDrawable
            }
        }
        buttonAnimator.start()
        //startTextColorAnimation(button, animDuration)
        lifecycleScope.launch(Dispatchers.Main) {
            button.setTextColor(Color.WHITE)
            delay(2000)
            textColorAnimation(
                button,
                Color.WHITE,
                Color.BLACK,
                View.LAYOUT_DIRECTION_LTR,
                5000
            )
        }
    }

    private fun textColorAnimation (buttonText : Button, fromColor: Int, toColor: Int, direction: Int = View.LAYOUT_DIRECTION_LTR, duration:Long = 5000) {

        var startValue = 0
        var endValue = 0
        if (direction == View.LAYOUT_DIRECTION_LTR){
            startValue = 0
            endValue = buttonText.text.length
        } else if (direction == View.LAYOUT_DIRECTION_RTL) {
            startValue = buttonText.text.length
            endValue = 0
        }
        buttonText.setTextColor(fromColor)
        val valueAnimator = ValueAnimator.ofInt(startValue, endValue)
        valueAnimator.addUpdateListener { animator ->
            val spannableString = SpannableString(buttonText.text)
            if (direction == View.LAYOUT_DIRECTION_LTR) spannableString.setSpan(
                ForegroundColorSpan(toColor), startValue, animator.animatedValue.toString().toInt(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            ) else if (direction == View.LAYOUT_DIRECTION_RTL) spannableString.setSpan(
                ForegroundColorSpan(toColor), animator.animatedValue.toString().toInt(),spannableString.length , Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            buttonText.text = spannableString
        }
        valueAnimator.duration = duration
        valueAnimator.start()
    }

    private fun startTextColorAnimation(button : Button, animationDuration : Long) {
        val textColor = ValueAnimator.ofArgb(Color.WHITE, Color.BLACK).apply {
            duration = animationDuration
            addUpdateListener {
                val textColor = it.animatedValue as Int
                button.setTextColor(textColor)
            }
        }
        textColor.start()
    }

}