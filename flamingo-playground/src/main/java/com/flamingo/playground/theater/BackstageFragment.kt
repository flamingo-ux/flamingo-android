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
    "SpacingAroundParens"
)

package com.flamingo.playground.theater

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.flamingo.theme.FlamingoTheme
import com.flamingo.playground.fromArguments
import com.theater.TheaterPackage

class BackstageFragment : Fragment() {

    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): ComposeView {
        requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        val theaterPackageClass = fromArguments<String>(THEATER_PACKAGE_CLASS_ARG_NAME)!!
        val theaterPackage = Class.forName(theaterPackageClass).newInstance() as TheaterPackage
        val backstageIndex = fromArguments<Int>(BACKSTAGE_INDEX_ARG_NAME)!!
        val play = theaterPackage.play

        return ComposeView(requireContext()).apply {
            setContent {
                FlamingoTheme(applyDebugColor = false) {
                    play
                        .backstages[backstageIndex]
                        .Backstage(sizeConfig = play.sizeConfig)
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
    }

    companion object {
        @JvmStatic
        fun newInstance(theaterPackageClass: String, backstageIndex: Int) =
            BackstageFragment().apply {
                arguments = bundleOf(
                    THEATER_PACKAGE_CLASS_ARG_NAME to theaterPackageClass,
                    BACKSTAGE_INDEX_ARG_NAME to backstageIndex,
                )
            }

        private const val BACKSTAGE_INDEX_ARG_NAME = "backstageIndex"
        private const val THEATER_PACKAGE_CLASS_ARG_NAME = "theaterPackageClass"
    }
}

