package com.demo.viewtest

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val buttonAnimation = findViewById<Button>(R.id.btn_animation)
        buttonAnimation.setOnClickListener {
            startActivity(Intent(this, ButtonAnimation::class.java))
        }

        val buttonTextColorAnimation = findViewById<Button>(R.id.btn_text_color_animation)
        buttonTextColorAnimation.setOnClickListener {
            startActivity(Intent(this, TextColorAnimation::class.java))
        }

    }
}