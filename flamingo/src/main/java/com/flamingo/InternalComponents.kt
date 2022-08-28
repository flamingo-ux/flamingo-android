package com.flamingo

import com.flamingo.annotations.FlamingoComponent

/**
 * [FlamingoComponent]s, which use [InternalComponents] as a receiver, are meant for internal use
 * only and SHOULD NOT be used by library users. Instead of just making those functions `internal`,
 * they are made `public` to allow `flamingo-playground` module to access them for use in demos by
 * reflectively instantiating [InternalComponents].
 *
 * It is __PROHIBITED__ to reflectively instantiate [InternalComponents] outside of
 * `flamingo-playground` module.
 *
 * `crab` checks if a function has this receiver and adds a corresponding property to the
 * [FlamingoRegistry].
 *
 * @see com.flamingo.components.Search
 */
public class InternalComponents private constructor() {
    internal companion object {
        @JvmStatic internal val INSTANCE = InternalComponents()
    }
}

/** @see InternalComponents */
internal val internalComponents get() = InternalComponents.INSTANCE
