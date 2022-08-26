package com.flamingo.playground.view

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.preference.DropDownPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.flamingo.cardElevationDarkBackgroundColors
import com.flamingo.demoapi.DemoPreference
import com.flamingo.demoapi.configurePreference
import com.flamingo.demoapi.findPreference
import com.flamingo.demoapi.initPref
import com.flamingo.demoapi.onChange
import com.flamingo.demoapi.tintIcons
import com.flamingo.playground.R
import com.flamingo.playground.utils.Boast
import com.flamingo.setCardElevationWithBackground

/** @see R.string.card_demo_docs */
class CardElevationDemoFragment : PreferenceFragmentCompat() {
    private val figmaUrl = "https://f.com/file/siTvBrYyUPigpjkZfOhJ1h/2.Core-Styles-and-I" +
            "cons?node-id=1920%3A1363"
    private val elevations: Array<String> = cardElevationDarkBackgroundColors.keys
        .sorted()
        .map(Int::toString)
        .toTypedArray()

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.card_elevation_demo, rootKey)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val cardView = layoutInflater.inflate(R.layout.card, view as ViewGroup, false) as CardView
        findPreference<DemoPreference>("component")?.setDesignComponent(cardView)

        configurePreference<DropDownPreference>("elevation") {
            entries = elevations
            entryValues = elevations
            onChange { newValue ->
                val elevation = newValue.toIntOrNull() ?: return@onChange false
                cardView.setCardElevationWithBackground(elevation) // ⬆️ elevation is set here
                true
            }
            initPref(savedInstanceState, defVal = elevations[0])
        }

        configurePreference<Preference>("figma") {
            tintIcons()
            setOnPreferenceClickListener {
                runCatching { startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(figmaUrl))) }
                    .onFailure { Boast.showText(requireContext(), "Browser not found") }
                true
            }
        }
    }
}
