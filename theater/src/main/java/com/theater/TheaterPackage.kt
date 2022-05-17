package com.theater

/**
 * Wrapper for the [TheaterPlay]. Needed to:
 * 1. hide generic parameter types;
 * 2. be a referencable entry point to the [TheaterPlay].
 *
 * [TheaterPackage] is used to play the [TheaterPlay] in [Theater].
 */
public interface TheaterPackage {
    public val play: TheaterPlay<*, *>
}
