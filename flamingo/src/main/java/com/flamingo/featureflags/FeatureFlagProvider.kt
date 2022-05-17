package com.flamingo.featureflags

/**
 * Allows library users to overwrite [FeatureFlag.enabledByDefault] for every [FeatureFlag].
 * Enums can be dynamically listed on the feature flags settings screen of the app that uses
 * flamingo.
 */
public interface FeatureFlagProvider {
    public fun enabled(featureFlag: FeatureFlag): Boolean = featureFlag.enabledByDefault
}
