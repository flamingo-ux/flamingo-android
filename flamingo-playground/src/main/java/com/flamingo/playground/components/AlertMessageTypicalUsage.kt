package com.flamingo.playground.components

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.core.text.buildSpannedString
import androidx.core.text.color
import androidx.fragment.app.Fragment
import com.flamingo.playground.R
import com.flamingo.demoapi.TypicalUsageDemo
import com.flamingo.playground.databinding.TypicalUsageAlertMessageBinding
import com.flamingo.playground.utils.viewBinding
import com.flamingo.loremIpsum
import com.flamingo.view.components.AlertMessage

@TypicalUsageDemo
class AlertMessageTypicalUsage : Fragment(R.layout.typical_usage_alert_message) {

    private val b by viewBinding(TypicalUsageAlertMessageBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init(b.alertError.ds)
        init(b.alertInfo.ds)
        init(b.alertSuccess.ds)
        init(b.alertWarning.ds)
        b.alertCustomVariant.ds.variant = AlertMessage.VARIANT_SUCCESS
        b.alertCustomVariant.ds.setMessage("variant = AlertMessage.VARIANT_SUCCESS")
    }

    @Suppress("MagicNumber")
    private fun init(alertMessage: AlertMessage.Accessor) {
        alertMessage.onCloseClick { (it.parent as ViewGroup).removeView(it) }
        alertMessage.setMessage(buildSpannedString {
            append(loremIpsum(2))
            append(" ")
            color(alertMessage.getTextHighlightColor()) { append(loremIpsum(2)) }
            append(" ")
            append(loremIpsum(4))
        })
    }
}
