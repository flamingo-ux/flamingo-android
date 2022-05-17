package com.flamingo.playground.components.card

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import com.flamingo.Flamingo
import com.flamingo.components.Card
import com.flamingo.components.CornerRadius
import com.flamingo.components.Elevation
import com.flamingo.components.Icon
import com.flamingo.demoapi.FlamingoComponentDemoName
import com.flamingo.demoapi.TypicalUsageDemo
import com.flamingo.playground.Compose
import com.flamingo.playground.boast

@TypicalUsageDemo
@FlamingoComponentDemoName("Clickable Card")
class ClickableCardTypicalUsage : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ) = Compose {
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.spacedBy(40.dp, Alignment.CenterHorizontally),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            ClickableCard()
            IncorrectlyImplementedClickableCard()
        }
    }
}

@Composable
fun ClickableCard() {
    Card(elevation = Elevation.Solid.Small, cornerRadius = CornerRadius.LARGE) {
        Box(
            modifier = Modifier
                .requiredSize(100.dp)
                .clickable(onClick = boast("Click")),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                modifier = Modifier.requiredSize(40.dp),
                icon = Flamingo.icons.Check,
                tint = Flamingo.colors.success
            )
        }
    }
}

@Composable
fun IncorrectlyImplementedClickableCard() {
    Box(modifier = Modifier.clickable(onClick = boast("Click"))) {
        Card(elevation = Elevation.Solid.Small, cornerRadius = CornerRadius.LARGE) {
            Box(modifier = Modifier.requiredSize(100.dp), contentAlignment = Alignment.Center) {
                Icon(
                    modifier = Modifier.requiredSize(40.dp),
                    icon = Flamingo.icons.Slash,
                    tint = Flamingo.colors.error
                )
            }
        }
    }
}
