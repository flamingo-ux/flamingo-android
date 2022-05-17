package com.flamingo.annotations

import com.flamingo.Team

/**
 * Each class that represents a team in the project must be annotated with [TeamMarker] and must
 * implement [Team].
 *
 * Note, that because of [AnnotationRetention.SOURCE], info about teams will be excluded from the
 * production apk.
 *
 * @see LocalFlamingoComponent.team
 */
@Retention(AnnotationRetention.SOURCE)
@MustBeDocumented
@Target(AnnotationTarget.CLASS)
public annotation class TeamMarker(
    val displayName: String,
    val jiraKey: String,
)
