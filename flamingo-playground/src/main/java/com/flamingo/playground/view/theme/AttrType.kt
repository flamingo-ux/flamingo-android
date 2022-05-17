package com.flamingo.playground.view.theme

enum class AttrType {
    COLOR, OTHER;

    companion object {

        fun fromEmoji(emoji: String) = when (emoji) {
            "🎨" -> COLOR
            "\uD83E\uDD37" -> OTHER // 🤷
            else -> OTHER
        }
    }
}
