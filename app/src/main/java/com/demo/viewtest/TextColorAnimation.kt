package com.demo.viewtest

import android.animation.ArgbEvaluator
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Shader
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.os.CountDownTimer
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.graphics.ColorUtils


class TextColorAnimation : AppCompatActivity() {


    private lateinit var changeColorButton: Button
    lateinit var textColor : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_text_color_animation)

        changeColorButton = findViewById(R.id.changeColor)
        textColor = findViewById(R.id.textColor)

        // Set an initial color
        /*updateButtonBackground(ContextCompat.getColor(this, R.color.black))

        changeColorButton.setOnClickListener {
            startColorAnimation()
        }*/
        updateButtonTextColor(ContextCompat.getColor(this, R.color.white))

        changeColorButton.setOnClickListener {
            startTextColorAnimation()
        }

        changeTextColor1(
            textColor,
            Color.BLACK,
            Color.WHITE,
            View.LAYOUT_DIRECTION_LTR,
            10000
        )
        //changeTextColor(textColor, Color.BLACK, Color.WHITE, 1000, 100)

        //test()
    }

    fun test(){
        val valueAnimator = ValueAnimator.ofFloat(0.0f, 1.0f)
        valueAnimator.setDuration(5000)
        valueAnimator.addUpdateListener { valueAnimator ->
            val fractionAnim = valueAnimator.animatedValue as Float
            textColor.setTextColor(
                ColorUtils.blendARGB(
                    Color.parseColor("#FFFFFF"),
                    Color.parseColor("#000000"),
                    fractionAnim
                )
            )
        }
        valueAnimator.start()
    }

    fun changeTextColor1(textView: TextView, fromColor: Int, toColor: Int, direction: Int = View.LAYOUT_DIRECTION_LTR, duration:Long = 200) {

        var startValue = 0
        var endValue = 0
        if(direction == View.LAYOUT_DIRECTION_LTR){
            startValue = 0
            endValue = textView.text.length
        } else if(direction == View.LAYOUT_DIRECTION_RTL) {
            startValue = textView.text.length
            endValue = 0
        }

        textView.setTextColor(fromColor)
        val valueAnimator = ValueAnimator.ofInt(startValue, endValue)
        valueAnimator.addUpdateListener { animator ->
            val spannableString = SpannableString(textView.text)
            if (direction == View.LAYOUT_DIRECTION_LTR) spannableString.setSpan(
                ForegroundColorSpan(toColor), startValue, animator.animatedValue.toString().toInt(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            ) else if (direction == View.LAYOUT_DIRECTION_RTL) spannableString.setSpan(
                ForegroundColorSpan(toColor), animator.animatedValue.toString().toInt(),spannableString.length , Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            textView.text = spannableString
        }
        valueAnimator.duration = duration
        valueAnimator.start()
    }

    private fun changeTextColor(
        textView: TextView?, startColor: Int, endColor: Int,
        animDuration: Int, animUnit: Long
    ) {
        if (textView == null) return
        val startRed = Color.red(startColor)
        val startBlue = Color.blue(startColor)
        val startGreen = Color.green(startColor)
        val endRed = Color.red(endColor)
        val endBlue = Color.blue(endColor)
        val endGreen = Color.green(endColor)
        object : CountDownTimer(animDuration.toLong(), animUnit) {
            //animDuration is the time in ms over which to run the animation
            //animUnit is the time unit in ms, update color after each animUnit
            override fun onTick(l: Long) {
                val red = (endRed + l * (startRed - endRed) / animDuration).toInt()
                val blue = (endBlue + l * (startBlue - endBlue) / animDuration).toInt()
                val green = (endGreen + l * (startGreen - endGreen) / animDuration).toInt()
                textView.setTextColor(Color.rgb(red, green, blue))
            }

            override fun onFinish() {
                textView.setTextColor(Color.rgb(endRed, endGreen, endBlue))
            }
        }.start()
    }

    @SuppressLint("ObjectAnimatorBinding")
    private fun startColorAnimation() {
        val startColor = ContextCompat.getColor(this, R.color.white)
        val endColor = ContextCompat.getColor(this, R.color.white)

        val gradientDrawable = changeColorButton.background as GradientDrawable

        val animator = ObjectAnimator.ofFloat(gradientDrawable, "gradient", 0f, 1f)
        animator.duration = 3000 // Change the duration as needed

        animator.addUpdateListener { animation ->
            val fraction = animation.animatedValue as Float
            val color = interpolateColor(startColor, endColor, fraction)
            updateButtonBackground(color)
        }

        animator.start()
    }

    private fun updateButtonBackground(color: Int) {
        val gradientDrawable = GradientDrawable(
            GradientDrawable.Orientation.LEFT_RIGHT,
            intArrayOf(color, ContextCompat.getColor(this, R.color.white))
        )

        // Set corner radius and other properties if needed
        gradientDrawable.cornerRadius = 10f

        changeColorButton.background = gradientDrawable
    }

    private fun interpolateColor(startColor: Int, endColor: Int, fraction: Float): Int {
        val startA = (startColor shr 24 and 0xff) / 255.0f
        val startR = (startColor shr 16 and 0xff) / 255.0f
        val startG = (startColor shr 8 and 0xff) / 255.0f
        val startB = (startColor and 0xff) / 255.0f

        val endA = (endColor shr 24 and 0xff) / 255.0f
        val endR = (endColor shr 16 and 0xff) / 255.0f
        val endG = (endColor shr 8 and 0xff) / 255.0f
        val endB = (endColor and 0xff) / 255.0f

        val interpolatedA = startA + fraction * (endA - startA)
        val interpolatedR = startR + fraction * (endR - startR)
        val interpolatedG = startG + fraction * (endG - startG)
        val interpolatedB = startB + fraction * (endB - startB)

        return ((interpolatedA * 255.0f + 0.5f).toInt() shl 24) or
                ((interpolatedR * 255.0f + 0.5f).toInt() shl 16) or
                ((interpolatedG * 255.0f + 0.5f).toInt() shl 8) or
                (interpolatedB * 255.0f + 0.5f).toInt()
    }

    private fun startTextColorAnimation() {
        val startColor = ContextCompat.getColor(this, R.color.white)
        val endColor = ContextCompat.getColor(this, R.color.black)

        val animator = ValueAnimator.ofObject(ArgbEvaluator(), startColor, endColor)
        animator.duration = 10000 // Change the duration as needed

        animator.addUpdateListener { animation ->
            val color = animation.animatedValue as Int
            updateButtonTextColor(color)
        }

        animator.start()
    }

    private fun updateButtonTextColor(color: Int) {
        val shader = LinearGradient(0f, 0f, changeColorButton.width.toFloat(), 0f, color, 0, Shader.TileMode.CLAMP)
        changeColorButton.paint.shader = shader
        changeColorButton.invalidate()
    }
}