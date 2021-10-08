package com.iso.pinblockgenerator.view

import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.view.ViewTreeObserver
import android.view.WindowInsets
import android.view.WindowInsetsController
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.iso.pinblockgenerator.R
import com.iso.pinblockgenerator.databinding.ActivityMainBinding
import com.iso.pinblockgenerator.viewmodel.PosViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PosActivity : AppCompatActivity() {

    private val viewModel: PosViewModel by viewModels()
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.viewmodel = viewModel

        hideSystemUI()
        setupKeypad()
    }

    private fun setupKeypad() {
        viewModel.pinLiveData.observe(this) {
            binding.pinTextview.apply {
                text = getString(R.string.pin, it)
            }
        }

        viewModel.invalidPinPrompt.observe(this) {
            binding.promptTextview.visibility = if (it) View.VISIBLE else View.GONE
        }
    }

    /**
     * Hide the system UI to build the immersive UX of POS
     */
    private fun hideSystemUI() {
        Handler(Looper.getMainLooper()).postDelayed(
            {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    window.setDecorFitsSystemWindows(false)
                    window.insetsController?.let {
                        it.hide(WindowInsets.Type.statusBars() or WindowInsets.Type.navigationBars())
                        it.systemBarsBehavior =
                            WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
                    }
                } else {
                    @Suppress("DEPRECATION")
                    window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE
                            // Set the content to appear under the system bars so that the
                            // content doesn't resize when the system bars hide and show.
                            or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            // Hide the nav bar and status bar
                            or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            or View.SYSTEM_UI_FLAG_FULLSCREEN)
                }
            }, HIDE_SYSTEM_UI_DELAY
        )
    }

    companion object {
        private const val HIDE_SYSTEM_UI_DELAY = 200L
    }
}