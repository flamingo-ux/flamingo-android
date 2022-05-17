package com.flamingo.playground

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import com.flamingo.components.EmptyStateImage
import com.flamingo.components.listitem.ListItem
import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.isAccessible

class IllustrationsDemoFragment : Fragment() {

    private val resIdProperty = EmptyStateImage::class
        .memberProperties
        .first()
        .apply { isAccessible = true }

    private val illustrations: List<Int> = EmptyStateImage.values()
        .map { resIdProperty.get(it) as Int }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ) = Compose {
        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            itemsIndexed(illustrations) { index, resId ->
                Column {
                    Image(
                        modifier = Modifier.fillMaxWidth(),
                        painter = painterResource(resId),
                        contentDescription = null
                    )
                    val imageName = LocalContext.current.resources.getResourceEntryName(resId)
                    ListItem(title = imageName, showDivider = index < illustrations.lastIndex)
                }
            }
        }
    }
}
