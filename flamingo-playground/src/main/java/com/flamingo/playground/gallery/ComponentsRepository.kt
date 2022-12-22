package com.flamingo.playground.gallery

import androidx.annotation.WorkerThread
import com.flamingo.annotations.view.FlamingoComponent
import com.flamingo.crab.FlamingoComponentRecord
import com.flamingo.crab.FlamingoRegistry

internal class ComponentsRepository {
    val composeComponents: List<FlamingoComponentRecord> = FlamingoRegistry.components

    @WorkerThread
    fun loadViewComponents(): List<FlamingoComponent> {
        val annClass = FlamingoComponent::class.java
        val dscClass = com.flamingo.view.components.FlamingoComponent::class

        return dscClass.sealedSubclasses
            .filterNot { it.isAbstract }
            .map {
                it.java.getAnnotation(annClass)
                    ?: error("Annotation @${annClass.simpleName} not found")
            }
            .sortedBy { it.displayName }
    }
}
