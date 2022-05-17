package com.flamingo.playground.components

import androidx.fragment.app.Fragment
import com.flamingo.playground.R
import com.flamingo.demoapi.TypicalUsageDemo
import com.flamingo.playground.databinding.TypicalUsageIndicatorBinding
import com.flamingo.playground.utils.viewBinding

@TypicalUsageDemo
class IndicatorTypicalUsage : Fragment(R.layout.typical_usage_indicator) {

    private val b by viewBinding(TypicalUsageIndicatorBinding::bind)
}
