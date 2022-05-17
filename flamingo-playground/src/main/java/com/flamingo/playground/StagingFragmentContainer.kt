package com.flamingo.playground

import androidx.fragment.app.Fragment

interface StagingFragmentContainer {
    fun openFragment(fragment: Fragment, tag: String? = null)
}
