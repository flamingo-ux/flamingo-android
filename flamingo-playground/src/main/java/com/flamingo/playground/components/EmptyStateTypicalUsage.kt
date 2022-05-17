package com.flamingo.playground.components

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.flamingo.playground.R
import com.flamingo.demoapi.TypicalUsageDemo

@TypicalUsageDemo
class EmptyStateTypicalUsage : Fragment(R.layout.typical_usage_empty_state) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}
