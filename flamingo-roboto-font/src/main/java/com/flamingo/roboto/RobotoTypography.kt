@file:Suppress(
    "MagicNumber",
    "NoMultipleSpaces",
    "FunctionNaming",
    "NoMultipleSpaces",
)

package com.flamingo.roboto

import com.flamingo.Flamingo
import com.flamingo.theme.FlamingoTheme
import com.flamingo.theme.typography.FlamingoTypographyManager
import com.flamingo.theme.typography.robotoTypographyProvider
import com.flamingo.view.components.AlertMessage
import com.flamingo.view.components.Avatar
import com.flamingo.view.components.Button

/**
 * Call [initRobotoTypography] before the first call to [FlamingoTheme] to provide roboto-based
 * typography in the Flamingo Design System.
 *
 * Can only be called once and only one of `init*****Typography()` methods can be called.
 */
public fun Flamingo.initRobotoTypography() {
    FlamingoTypographyManager.provideTypography(robotoTypographyProvider)
    initFontSetters()
}

private fun initFontSetters() {
    // for Button
    Button.fontSetter = Button.Companion.FontSetter { textView, isLarge ->
        textView.dsTextStyle(R.style.TextStyle_Flamingo_Button)
    }
    // for AlertMessage
    AlertMessage.fontSetter = AlertMessage.Companion.FontSetter { textView ->
        textView.dsTextStyle(R.style.TextStyle_Flamingo_Body1)
    }
    // for Avatar
    Avatar.fontSetter = Avatar.Companion.FontSetter { textView ->
        textView.dsTextStyle(R.style.TextStyle_Flamingo_Body1)
    }
}
