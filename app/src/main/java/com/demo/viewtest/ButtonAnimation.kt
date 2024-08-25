package com.demo.viewtest

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.demo.viewtest.databinding.ActivityButtonAnimationBinding
import com.google.android.material.card.MaterialCardView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.w3c.dom.Text


class ButtonAnimation : AppCompatActivity() {

    private var _binding : ActivityButtonAnimationBinding? = null
    private val binding get() = _binding!!

    private val animDuration = 10000L

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityButtonAnimationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        lifecycleScope.launch {

            delay(500)
            startAnimation(binding.btnTestAnimation, binding.btnTestAnimation.width, animDuration)
            animateButton(binding.btnFrame, binding.btnWhite, animDuration)
        }
    }

    @SuppressLint("Recycle")
    fun animateButton(frameLayout: FrameLayout, button: TextView, animDuration: Long){
        Log.d("Vikram", "Button width - ${button.width}")
        val valueAnimator = ValueAnimator.ofInt(0, button.width).apply {
            duration = animDuration
            addUpdateListener {
                val value = it.animatedValue as Int
                Log.d("Vikram", "animate value - $value")
                frameLayout.layoutParams = frameLayout.layoutParams.apply {
                    this.width = value
                }
            }
        }
        valueAnimator.start()
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
        //startTextColorAnimation(button, animDuration)
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

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}