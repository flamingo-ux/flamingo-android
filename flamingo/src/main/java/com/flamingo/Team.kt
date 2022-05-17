package com.flamingo

import com.flamingo.annotations.LocalFlamingoComponent
import com.flamingo.annotations.TeamMarker

/**
 * Each class that represents a team in the project must be annotated with [TeamMarker] and must
 * implement [Team].
 *
 * @see LocalFlamingoComponent.team
 */
public interface Team
