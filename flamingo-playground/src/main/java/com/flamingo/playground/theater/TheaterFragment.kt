@file:Suppress(
    "ModifierOrdering",
    "CommentSpacing",
    "NoBlankLineBeforeRbrace",
    "ModifierOrdering",
    "NoConsecutiveBlankLines",
    "MagicNumber",
    "FunctionName",
    "MatchingDeclarationName",
    "LongParameterList",
    "LongMethod",
    "ComplexMethod",
    "SpacingAroundParens"
)

package com.flamingo.playground.theater

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowInsets.Type.navigationBars
import android.view.WindowInsets.Type.statusBars
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.flamingo.playground.Compose
import com.flamingo.playground.fromArguments
import com.flamingo.utils.SDK_30
import com.theater.Theater
import com.theater.TheaterPackage

/**
 * Used to play or screen capture (if renderMode is true) the video of [TheaterPackage]
 */
@SuppressLint("ClearingOfSystemUiVisibilityFlags")
class TheaterFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): ComposeView {
        requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE
        hideSystemUI()

        val renderMode = fromArguments<Boolean>(RENDER_MODE_ARG_NAME)!!
        val theaterPackageClass = fromArguments<String>(THEATER_PACKAGE_CLASS_ARG_NAME)!!
        val theaterPackage = Class.forName(theaterPackageClass).newInstance() as TheaterPackage
        return Compose {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black)
            ) {
                Theater(
                    theaterPackage = theaterPackage,
                    onPlayEnd = { activity?.onBackPressed() },
                    enforceSize = renderMode,
                    showStageBorder = false,
                )
            }
        }
    }

    private fun hideSystemUI() {
        (requireActivity() as AppCompatActivity).supportActionBar?.hide()
        val window = requireActivity().window
        if (Build.VERSION.SDK_INT >= SDK_30) {
            window.insetsController?.hide(statusBars() or navigationBars())
        } else {
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
    }

    private fun showSystemUI() {
        (requireActivity() as AppCompatActivity).supportActionBar?.show()
        val window = requireActivity().window
        if (Build.VERSION.SDK_INT >= SDK_30) {
            window.insetsController?.show(statusBars() or navigationBars())
        } else {
            window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
        showSystemUI()
    }

    companion object {
        @JvmStatic
        fun newInstance(theaterPackageClass: String, renderMode: Boolean) =
            TheaterFragment().apply {
                arguments = bundleOf(
                    THEATER_PACKAGE_CLASS_ARG_NAME to theaterPackageClass,
                    RENDER_MODE_ARG_NAME to renderMode,
                )
            }

        private const val RENDER_MODE_ARG_NAME = "renderMode"
        private const val THEATER_PACKAGE_CLASS_ARG_NAME = "theaterPackageClass"
    }
}

