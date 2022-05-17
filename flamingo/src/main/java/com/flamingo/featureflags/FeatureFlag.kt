package com.flamingo.featureflags

import com.flamingo.Flamingo

public enum class FeatureFlag(public val enabledByDefault: Boolean = false) {
    /**
     * Button on Jetpack Compose
     * Implementation of the flamingo component Button on Jetpack Compose
     */
    BUTTON_COMPOSE_IMPL,
    ;

    /**
     * @return true, if [FeatureFlag] is enabled, false otherwise
     */
    public operator fun invoke(): Boolean = Flamingo.featureFlagProvider.enabled(this)
}
