package com.flamingo.playground.components.dropdown

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import com.flamingo.demoapi.TypicalUsageDemo
import com.flamingo.playground.Compose
import com.flamingo.playground.components.tabrow.TabsWithDropdown

@TypicalUsageDemo
class DropdownTypicalUsage : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ) = Compose { Box { DropdownSamples() } }

    @Preview
    @Composable
    private fun DropdownSamples() = Column(
        modifier = Modifier.padding(vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        ButtonDropdownWithChangingLabel()
        IconButtonDropdown()
        ChipDropdown()
        TabsWithDropdown()
    }
}