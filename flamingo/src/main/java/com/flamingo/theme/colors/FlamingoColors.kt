package com.flamingo.theme.colors

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color

@Immutable
public data class FlamingoColors internal constructor(
    /**
     * Whether this [FlamingoColors] is considered as a 'light' or 'dark' set of colors.
     */
    public val isLight: Boolean,

    /**
     * Основной цвет фирменного стиля для приложения
     * [figma](https://f.com/file/siTvBrYyUPigpjkZfOhJ1h?node-id=713%3A14)
     */
    public val primary: Color,

    /**
     * Вторичный фирменный цвет вашего приложения, используемый в основном в качестве акцента для
     * определенных виджетов, которые должны выделяться
     * [figma](https://f.com/file/siTvBrYyUPigpjkZfOhJ1h?node-id=714%3A15)
     */
    public val secondary: Color,

    /**
     * Информация
     * [figma](https://f.com/file/siTvBrYyUPigpjkZfOhJ1h?node-id=714%3A44)
     */
    public val info: Color,

    /**
     * Предупреждение
     * [figma](https://f.com/file/siTvBrYyUPigpjkZfOhJ1h?node-id=714%3A47)
     */
    public val warning: Color,

    /**
     * Успех
     * [figma](https://f.com/file/siTvBrYyUPigpjkZfOhJ1h?node-id=934%3A1069)
     */
    public val success: Color,

    /**
     * Оценка (для звёздочек, например)
     * [figma](https://f.com/file/siTvBrYyUPigpjkZfOhJ1h?node-id=716%3A29)
     */
    public val rating: Color,

    /**
     * Ошибка
     * [figma](https://f.com/file/siTvBrYyUPigpjkZfOhJ1h?node-id=714%3A42)
     */
    public val error: Color,

    /**
     * Используется для разделителей в списках
     * [figma](https://f.com/file/siTvBrYyUPigpjkZfOhJ1h?node-id=436%3A5)
     */
    public val separator: Color,

    /**
     * Для рамки, окружающей элемент
     * [figma](https://f.com/file/siTvBrYyUPigpjkZfOhJ1h?node-id=436%3A8)
     */
    public val outline: Color,

    /**
     * Для рамки, окружающей элемент
     * [figma](https://f.com/file/siTvBrYyUPigpjkZfOhJ1h?node-id=438%3A298)
     */
    public val outlineDark: Color,

    /**
     * Фон
     */
    public val background: Color,

    /**
     * [figma](https://f.com/file/siTvBrYyUPigpjkZfOhJ1h?node-id=735%3A301)
     */
    public val backgroundSecondary: Color,

    /**
     * [figma](https://f.com/file/siTvBrYyUPigpjkZfOhJ1h?node-id=735%3A312)
     */
    public val backgroundTertiary: Color,

    /**
     * [figma](https://f.com/file/siTvBrYyUPigpjkZfOhJ1h?node-id=735%3A311)
     */
    public val backgroundQuaternary: Color,

    /**
     * [figma](https://f.com/file/siTvBrYyUPigpjkZfOhJ1h?node-id=999%3A1219)
     */
    public val backgroundTextField: Color,

    public val backdrop: Color,

    /**
     * Основной (заголовочный) текст.
     * [figma](https://f.com/file/siTvBrYyUPigpjkZfOhJ1h?node-id=337%3A1436)
     */
    public val textPrimary: Color,

    /**
     * Вторичный текст
     * [figma](https://f.com/file/siTvBrYyUPigpjkZfOhJ1h?node-id=337%3A1437)
     */
    public val textSecondary: Color,

    /**
     * Используется в качестве disabled текста
     * [figma](https://f.com/file/siTvBrYyUPigpjkZfOhJ1h?node-id=337%3A1438)
     */
    public val textTertiary: Color,

    public val global: GlobalColors,
    public val inverse: InverseColors,
    public val extensions: ExtensionColors,
)

@Immutable
public data class GlobalColors internal constructor(
    public val light: GlobalColorsVersion,
    public val dark: GlobalColorsVersion,
) {
    @Immutable
    public data class GlobalColorsVersion internal constructor(
        public val textPrimary: Color,
        public val textSecondary: Color,
        public val textTertiary: Color,
        public val backgroundPrimary: Color,
        public val backgroundSecondary: Color,
        public val backgroundTertiary: Color,
    )
}

@Immutable
public data class InverseColors internal constructor(
    public val textPrimary: Color,
    public val textSecondary: Color,
    public val textTertiary: Color,
    public val backgroundPrimary: Color,
    public val backgroundSecondary: Color,
    public val backgroundTertiary: Color,
)

@Immutable
public data class ExtensionColors internal constructor(
    public val background: Background,
    public val outline: Outline,
) {
    @Immutable
    public data class Background internal constructor(
        public val error: Color,
        public val info: Color,
        public val success: Color,
        public val warning: Color,
        public val rating: Color,
    )

    @Immutable
    public data class Outline internal constructor(
        public val error: Color,
        public val info: Color,
        public val success: Color,
        public val warning: Color,
    )
}
