package com.flamingo.components.topappbar

import android.content.Context
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Immutable
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.ImeAction
import com.flamingo.R
import com.flamingo.annotations.DelicateFlamingoApi
import com.flamingo.components.AvatarContent
import com.flamingo.components.AvatarIndicator
import com.flamingo.components.AvatarShape
import com.flamingo.components.IconButtonIndicator
import com.flamingo.theme.FlamingoIcon

public sealed class EdgeItem {

    /** @see com.flamingo.components.IconButton */
    @Immutable
    public data class IconButton(
        val icon: FlamingoIcon,
        val indicator: IconButtonIndicator? = null,
        val disabled: Boolean = false,
        val onClick: () -> Unit,
    ) : EdgeItem()

    /** @see com.flamingo.components.button.Button */
    @Immutable
    public data class Button(
        val label: String,
        val onClick: () -> Unit,
    ) : EdgeItem()

    /** @see com.flamingo.components.Avatar */
    @Immutable
    public data class Avatar(
        val content: AvatarContent,
        val onClick: (() -> Unit)? = null,
        val shape: AvatarShape = AvatarShape.CIRCLE,
    ) : EdgeItem()
}

public sealed class CenterItem {
    /**
     * Only icons are allowed in [title] and [subtitle] at this point.
     */
    @Immutable
    @DelicateFlamingoApi
    public open class AdvancedCenter(
        public val title: AnnotatedString? = null,
        public val subtitle: AnnotatedString? = null,
        public val avatar: Avatar? = null,
    ) : CenterItem()

    @Immutable
    public class Center(
        title: String? = null,
        subtitle: String? = null,
        avatar: Avatar? = null,
    ) : AdvancedCenter(
        title?.let(::AnnotatedString),
        subtitle?.let(::AnnotatedString),
        avatar,
    )

    /** @see com.flamingo.components.Avatar */
    @Immutable
    public data class Avatar(
        val content: AvatarContent,
        val shape: AvatarShape = AvatarShape.CIRCLE,
        val indicator: AvatarIndicator? = null,
    )

    /** @see com.flamingo.components.Search */
    @Immutable
    public data class Search(
        val context: Context,
        val value: String,
        val onClick: (() -> Unit)? = null,
        val onValueChange: (String) -> Unit,
        val placeholder: String? = context.getString(R.string.search_placeholder),
        val loading: Boolean = false,
        val disabled: Boolean = false,
        val keyboardOptions: KeyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        val keyboardActions: KeyboardActions = KeyboardActions.Default,
        val focusRequester: FocusRequester? = null,
    ) : CenterItem()
}

/** @see com.flamingo.components.IconButton */
@Immutable
public data class ActionItem(
    val icon: FlamingoIcon,
    val indicator: IconButtonIndicator? = null,
    val disabled: Boolean = false,
    val onClick: () -> Unit,
)
