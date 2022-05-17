package com.flamingo.playground.components

import android.os.Bundle
import android.view.View
import androidx.core.view.doOnAttach
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.delay
import com.flamingo.demoapi.TypicalUsageDemo
import com.flamingo.playground.R
import com.flamingo.playground.databinding.TypicalUsageAvatarBinding
import com.flamingo.playground.utils.Boast
import com.flamingo.playground.utils.viewBinding
import com.flamingo.utils.updateMargins
import com.flamingo.view.components.Avatar
import com.flamingo.view.components.Indicator
import kotlin.math.roundToInt
import com.flamingo.R as FlamingoR

@TypicalUsageDemo
class AvatarTypicalUsage : Fragment(R.layout.typical_usage_avatar) {

    private val b by viewBinding(TypicalUsageAvatarBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val avatar = Avatar(requireContext())
        avatar.doOnAttach {
            it.updateMargins(top = resources.getDimension(FlamingoR.dimen.x4).roundToInt())
        }
        avatar.ds.apply {
            setIcon(
                FlamingoR.drawable.ds_ic_aperture,
                Avatar.BACKGROUND_PRIMARY,
                Avatar.SHAPE_CIRCLE,
                Avatar.SIZE_72
            )
            showIndicator(Indicator.COLOR_ERROR)
            setOnClickListener { Boast.showText(context, "\uD83D\uDD76️") }
        }
        b.list.addView(avatar)
        lifecycleScope.launchWhenCreated {
            delay(@Suppress("MagicNumber") 2000)
            b.avatar1.ds.setLetters(letters = "AB")
        }

        b.coilAvatar.ds.loadDrawableAsync(
            data = com.flamingo.demoapi.R.drawable.example_dog,
            shape = Avatar.SHAPE_CIRCLE,
            avatarSize = Avatar.SIZE_88
        ) {
            listener { request, metadata -> b.coilAvatar.ds.showIndicator(Indicator.COLOR_PRIMARY) }
        }
    }
}
