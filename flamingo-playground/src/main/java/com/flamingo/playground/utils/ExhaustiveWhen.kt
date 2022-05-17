package com.flamingo.playground.utils

/**
 * Used in combination with "when" statements to turn them into expressions. Then compiler requires
 * to provide all possible branches to the "when" statement. Example:
 *
 * ```
 * when(section) {
 *     TYPE_ONE -> ...
 *     TYPE_TWO -> ...
 *     TYPE_THREE -> ...
 * }.exhaustive
 * ```
 * @see (https://youtu.be/OyIRuxjBORY?t=107)
 */
internal val <T> T.exhaustive: T get() = this
