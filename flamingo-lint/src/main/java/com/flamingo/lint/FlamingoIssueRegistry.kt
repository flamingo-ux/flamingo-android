@file:Suppress("UnstableApiUsage")

package com.flamingo.lint

import com.android.tools.lint.client.api.IssueRegistry
import com.android.tools.lint.client.api.Vendor
import com.android.tools.lint.detector.api.CURRENT_API
import com.android.tools.lint.detector.api.Issue

class FlamingoIssueRegistry : IssueRegistry() {

    override val api: Int = CURRENT_API
    override val vendor: Vendor = Vendor(
        vendorName = "Flamingo Design System",
        feedbackUrl = "https://github.com/flamingo-ux/flamingo-android/issues",
    )

    @Suppress("SpreadOperator")
    override val issues: List<Issue> = listOf(
        CardElevationInXmlDetector.ISSUE,
        TextAppearanceDetector.ISSUE,
        AlphaDisabledDeclarationDetector.ISSUE,
        WrongComponentAlternativeDetector.ISSUE,
    )
}
