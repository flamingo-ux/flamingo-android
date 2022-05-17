package com.flamingo.playground.theater

import com.flamingo.annotations.FlamingoComponent
import com.flamingo.crab.FlamingoComponentRecord
import com.flamingo.crab.FlamingoRegistry
import com.theater.TheaterPackage

/**
 * @return [FlamingoComponentRecord] of the component in which [TheaterPackage] is referenced in
 * [FlamingoComponent.theaterPackage].
 */
fun TheaterPackage.componentRecord(): FlamingoComponentRecord {
    return FlamingoRegistry.components.find { it.theaterPackage == this::class }!!
}
