package com.flamingo.playground.view

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.core.view.forEach
import androidx.core.view.forEachIndexed
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.google.android.material.textview.MaterialTextView
import com.flamingo.demoapi.AllPossibleStatesDemo
import com.flamingo.playground.R
import com.flamingo.playground.databinding.FontsDemoFragmentBinding
import com.flamingo.playground.utils.viewBinding
import com.flamingo.roboto.dsTextStyle
import com.flamingo.roboto.R as FlamingoR

@AllPossibleStatesDemo
/** @see R.string.fonts_demo_docs */
class FontsDemoFragment : Fragment(R.layout.fonts_demo_fragment) {
    private val b by viewBinding(FontsDemoFragmentBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        b.enabledTextViews.tripleText()
        b.programmaticallyStyledTextViews.tripleText()
        b.disabledTextViews.apply {
            tripleText()
            forEach { it.isEnabled = false }
        }
        programmaticallyStyleTextViews()

        b.enabledTextViewsSwitch.setOnCheckedChangeListener { _, isChecked ->
            b.enabledTextViews.isVisible = isChecked
        }
        b.disabledTextViewsSwitch.setOnCheckedChangeListener { _, isChecked ->
            b.disabledTextViews.isVisible = isChecked
        }
        b.programmaticallyStyledTextViewsSwitch.setOnCheckedChangeListener { _, isChecked ->
            b.programmaticallyStyledTextViews.isVisible = isChecked
        }
        b.programmaticallyStyledTextViewsSwitch.isChecked = false
    }

    @Suppress("ComplexMethod")
    private fun programmaticallyStyleTextViews() {
        b.programmaticallyStyledTextViews.forEachIndexed { index, textView ->
            textView as MaterialTextView
            @Suppress("MagicNumber") when (index) {
                0 -> textView.dsTextStyle(FlamingoR.style.TextStyle_Flamingo_Headline1)
                1 -> textView.dsTextStyle(FlamingoR.style.TextStyle_Flamingo_Headline2)
                2 -> textView.dsTextStyle(FlamingoR.style.TextStyle_Flamingo_Headline3)
                3 -> textView.dsTextStyle(FlamingoR.style.TextStyle_Flamingo_Headline4)
                4 -> textView.dsTextStyle(FlamingoR.style.TextStyle_Flamingo_Headline5)
                5 -> textView.dsTextStyle(FlamingoR.style.TextStyle_Flamingo_Headline6)
                6 -> textView.dsTextStyle(FlamingoR.style.TextStyle_Flamingo_Body1)
                7 -> textView.dsTextStyle(FlamingoR.style.TextStyle_Flamingo_Body2)
                8 -> textView.dsTextStyle(FlamingoR.style.TextStyle_Flamingo_Subtitle1)
                9 -> textView.dsTextStyle(FlamingoR.style.TextStyle_Flamingo_Subtitle2)
                10 -> textView.dsTextStyle(FlamingoR.style.TextStyle_Flamingo_Caption)
                11 -> textView.dsTextStyle(FlamingoR.style.TextStyle_Flamingo_Overline)
                12 -> textView.dsTextStyle(FlamingoR.style.TextStyle_Flamingo_Button)
            }
        }
    }

    private fun ViewGroup.tripleText() = forEach { textView ->
        if (textView !is MaterialTextView) return@forEach
        val capacity = LINES * textView.text.length + LINES // `+ LINES` is for `\n`s
        textView.text = buildString(capacity) { repeat(LINES) { appendLine(textView.text) } }
    }
}

private const val LINES = 3
