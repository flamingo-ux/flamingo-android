package com.flamingo.components.counter

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidthIn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.flamingo.Flamingo
import com.flamingo.annotations.FlamingoComponent
import com.flamingo.components.CircularLoader
import com.flamingo.components.CircularLoaderColor
import com.flamingo.components.CircularLoaderSize
import com.flamingo.components.IconButton
import com.flamingo.components.IconButtonColor
import com.flamingo.components.Text

/**
 * Counter, that counts from 0 to [maxCount]. Count can be set externally via [count].
 *
 * @param count sets the counter value
 * @param maxCount maximum value of counter. Defaults to [Int.MAX_VALUE]
 * @param loading if true, changes text to [CircularLoader] and disables [Counter]
 * @param plusDisabled if true, disables 'plus' button
 * @param minusDisabled if true, disables 'minus' button
 * @param onCountChange called when count changes
 *
 */
@FlamingoComponent(
    preview = "com.flamingo.playground.preview.CounterPreview",
    figma = "https://f.com/file/6qbNsEofr4vu0p8bAGCM65?node-id=31390%3A246955&t=cKfzistbFdxn55uY-0",
    specification = "https://confluence.companyname.ru/x/j4cjKQE",
    demo = ["com.flamingo.playground.components.counter.CounterStatesPlayroom"],
    supportsWhiteMode = false,
)
@Composable
public fun Counter(
    count: Int = 0,
    maxCount: Int = Int.MAX_VALUE,
    loading: Boolean = false,
    plusDisabled: Boolean = false,
    minusDisabled: Boolean = false,
    onCountChange: (Int) -> Unit,
) {
    var counter by remember(count) { mutableStateOf(count) }
    Row(modifier = Modifier, verticalAlignment = Alignment.CenterVertically) {
        if (counter != 0) {
            IconButton(
                onClick = {
                    counter--
                    onCountChange(counter)
                },
                icon = Flamingo.icons.Minus,
                contentDescription = null,
                color = IconButtonColor.DEFAULT,
                disabled = minusDisabled || loading
            )
        }

        if (counter != 0 && !loading) {
            Text(
                text = counter.toString(),
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .requiredWidthIn(min = 32.dp),
                color = Flamingo.colors.textPrimary,
                style = Flamingo.typography.h6,
                textAlign = TextAlign.Center
            )
        }
        if (counter != 0 && loading) {
            Box(modifier = Modifier.padding(horizontal = 12.dp)) {
                CircularLoader(
                    size = CircularLoaderSize.MEDIUM,
                    color = CircularLoaderColor.DEFAULT
                )
            }
        }

        IconButton(
            onClick = {
                counter++
                onCountChange(counter)
            },
            icon = Flamingo.icons.Plus,
            contentDescription = null,
            color = IconButtonColor.DEFAULT,
            disabled = plusDisabled || loading || counter >= maxCount,
            loading = counter == 0 && loading
        )
    }
}
