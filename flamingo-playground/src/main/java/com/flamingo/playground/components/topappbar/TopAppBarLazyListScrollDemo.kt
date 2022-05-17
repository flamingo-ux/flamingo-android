package com.flamingo.playground.components.topappbar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.flamingo.playground.Compose
import com.flamingo.demoapi.FlamingoComponentDemoName
import com.flamingo.demoapi.TypicalUsageDemo

@TypicalUsageDemo
@FlamingoComponentDemoName("Scrolling in LazyColumn with Shadow")
class TopAppBarLazyListScrollDemo : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ) = Compose { LazyColumnScrollingShadowSample() }
}
