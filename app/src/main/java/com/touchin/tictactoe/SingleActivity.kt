package com.touchin.tictactoe

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.touchin.tictactoe.databinding.ActivityMainBinding

class SingleActivity : AppCompatActivity() {

    private val binding by lazy(LazyThreadSafetyMode.NONE) { ActivityMainBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        window.apply {
            decorView.systemUiVisibility = decorView.systemUiVisibility or View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            statusBarColor = Color.TRANSPARENT
        }
    }

}
