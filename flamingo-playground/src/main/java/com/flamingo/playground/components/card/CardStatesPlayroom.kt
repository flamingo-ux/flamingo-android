package com.flamingo.playground.components.card

import android.os.Bundle
import android.view.View
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.preference.DropDownPreference
import androidx.preference.PreferenceFragmentCompat
import com.flamingo.Flamingo
import com.flamingo.components.Card
import com.flamingo.components.CornerRadius
import com.flamingo.components.Elevation
import com.flamingo.components.Text
import com.flamingo.demoapi.DemoPreference
import com.flamingo.demoapi.StatesPlayroomDemo
import com.flamingo.demoapi.configurePreference
import com.flamingo.demoapi.findPreference
import com.flamingo.demoapi.initPref
import com.flamingo.demoapi.onChange
import com.flamingo.playground.R
import com.flamingo.playground.deepSealedSubclasses

@StatesPlayroomDemo
class CardStatesPlayroom : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.states_playroom_card, rootKey)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var elevation: Elevation by mutableStateOf(Elevation.Solid.Small)
        var cornerRadius by mutableStateOf(CornerRadius.MEDIUM)
        findPreference<DemoPreference>("component")?.setComposeDesignComponent {
            Box(contentAlignment = Alignment.Center) {
                Text(text = "\uD83D\uDE43", style = Flamingo.typography.h4) // 🙃
                Card(
                    elevation = elevation,
                    cornerRadius = cornerRadius,
                ) {
                    Spacer(modifier = Modifier.requiredSize(100.dp))
                }
            }
        }

        val elevationClasses = Elevation::class.deepSealedSubclasses
        val elevationClassName = Elevation::class.qualifiedName
        configurePreference<DropDownPreference>("elevation") {
            entries = elevationClasses.map {
                it.qualifiedName!!.substringAfterLast(elevationClassName!! + ".")
            }.toTypedArray()
            entryValues = elevationClasses.map { it.qualifiedName!! }.toTypedArray()
            onChange { newValue ->
                elevation = elevationClasses
                    .find { it.qualifiedName == newValue }!!
                    .objectInstance!!
                true
            }
            initPref(savedInstanceState, defVal = Elevation.Solid.Small::class.qualifiedName!!)
        }

        configurePreference<DropDownPreference>("cornerRadius") {
            entries = CornerRadius.values().map { it.name }.toTypedArray()
            entryValues = entries
            onChange { newValue ->
                cornerRadius = CornerRadius.valueOf(newValue)
                true
            }
            initPref(savedInstanceState, defVal = cornerRadius)
        }
    }
}
