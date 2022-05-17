package com.flamingo.playground.components.topappbar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.compose.foundation.layout.Box
import androidx.compose.ui.Alignment
import androidx.fragment.app.Fragment
import com.flamingo.playground.Compose
import com.flamingo.demoapi.FlamingoComponentDemoName
import com.flamingo.demoapi.TypicalUsageDemo

@TypicalUsageDemo
@FlamingoComponentDemoName("Focus on Search")
class TopAppBarSearchFocusDemo : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ) = Compose { Box(contentAlignment = Alignment.TopCenter) { SearchFocusSample() } }
}
