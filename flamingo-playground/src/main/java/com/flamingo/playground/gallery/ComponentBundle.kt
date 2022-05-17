package com.flamingo.playground.gallery

import android.os.Bundle
import com.flamingo.annotations.view.FlamingoComponent

internal fun FlamingoComponent.toBundle() = Bundle().apply {
    putString("displayName", displayName)
    putString("docs", docs)
    putString("figmaUrl", figmaUrl)
    putString("preview", preview)
    putStringArray("permittedXmlAttributes", permittedXmlAttributes)
    putStringArray("theDemos", theDemos)
}

internal fun Bundle.toFlamingoComponent(): FlamingoComponent {
    return FlamingoComponent::class.constructors.first().call(
        getString("displayName"),
        getString("docs"),
        getString("figmaUrl"),
        getString("preview"),
        getStringArray("permittedXmlAttributes"),
        getStringArray("theDemos"),
    )
}
