package com.flamingo.playground.theater

import androidx.annotation.StringRes
import com.flamingo.playground.R

enum class Director(@StringRes val fullName: Int) {
    AntonPopov(R.string.theater_director_anton_popov),
    AlekseyBublyaev(R.string.theater_director_aleksey_bublyaev)
}
