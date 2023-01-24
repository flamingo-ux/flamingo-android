package com.flamingo.playground.components.checkboxgroup

import android.os.Bundle
import android.view.View
import androidx.preference.PreferenceFragmentCompat
import com.flamingo.demoapi.StatesPlayroomDemo
import com.flamingo.playground.R

@StatesPlayroomDemo
class CheckBoxGroupStatesPlayroom: PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.states_playroom_checkbox_group, rootKey)
    }

    @Suppress("LongMethod")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        TODO()
    }
}