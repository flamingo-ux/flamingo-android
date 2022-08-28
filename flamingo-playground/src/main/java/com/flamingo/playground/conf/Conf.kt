@file:Suppress("NoConsecutiveBlankLines", "MagicNumber", "NoUnusedImports", "unused")

package com.flamingo.playground.conf

import android.content.res.Configuration
import android.graphics.drawable.Drawable
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.flamingo.Flamingo
import com.flamingo.InternalComponents
import com.flamingo.annotations.FlamingoComponent
import com.flamingo.components.AlertMessage
import com.flamingo.components.AlertMessageVariant
import com.flamingo.components.Card
import com.flamingo.components.Chip
import com.flamingo.components.CornerRadius
import com.flamingo.components.Divider
import com.flamingo.components.Elevation
import com.flamingo.components.Icon
import com.flamingo.components.IconButton
import com.flamingo.components.LinkCard
import com.flamingo.components.Search
import com.flamingo.components.Text
import com.flamingo.components.button.Button
import com.flamingo.components.button.ButtonColor
import com.flamingo.components.button.ButtonWidthPolicy
import com.flamingo.components.listitem.ListItem
import com.flamingo.components.topappbar.TopAppBar
import com.flamingo.components.widgetcard.WidgetCardGroup
import com.flamingo.crab.FlamingoRegistry
import com.flamingo.loremIpsum
import com.flamingo.playground.boast
import com.flamingo.playground.components.alertmessage.TheaterPkg
import com.flamingo.playground.conf.Conf.Companion.Preview
import com.flamingo.playground.internalComponents
import com.flamingo.playground.overlay.DebugOverlayImpl
import com.flamingo.playground.overlay.disableDebugOverlay
import com.flamingo.playground.overlay.enableDebugOverlay
import com.flamingo.playground.preview.ButtonComposePreview
import com.flamingo.playground.preview.TopAppBarPreview
import com.flamingo.theme.FlamingoTheme
import com.flamingo.uiTestingTag

/**
 * # What is this
 * This is the presentation about the Flamingo Design System. Is is intended to be presented in an
 * Android Studio IDE v. 2021.2.1 Patch 2.
 *
 * ## Video recording is available [here](https://youtu.be/QW6lD5ip9xs)
 *
 * Do not auto-format this file, because giant line brakes will be removed.
 *
 * ## How to present
 * - Click the icon left from the KDocs block
 * - Right-click on the rendered KDocs block
 *      - "Adjust font size" and set the __smallest__ front size
 *      - Render All Doc Comments
 * - Press <kbd>Control</kbd> + <kbd>`</kbd>
 * - View Mode
 * - Enter Presentation Mode
 * - Exit Full Screen
 * - Adjust code font size in the _presentation mode_ using pinch-to-zoom on the MacBook's touchpad
 *
 * ### OR
 *
 * - Click the icon left from the KDocs block
 * - Right-click on the rendered KDocs block
 *      - "Adjust font size" and set the __largest__ front size
 *      - Render All Doc Comments
 * - Adjust code font size:
 *      - using pinch-to-zoom on the MacBook's touchpad
 *      - __OR__ in the Android Studio's settings
 *
 * ### THEN
 *
 * - Use <kbd>Command</kbd> + <kbd>/</kbd> to (un)comment @[Preview] annotations
 * - Use <kbd>Control</kbd> + <kbd>Shift</kbd> + <kbd>←</kbd> and
 * <kbd>Control</kbd> + <kbd>Shift</kbd> + <kbd>→</kbd> to open/close composable preview pane
 */
internal class Conf {



















    /**
     * # Дизайн-система Flamingo: Jetpack Compose
     *
     * ⠀
     *
     *  __Антон Попов__ ([https://popov-anton.web.app/])
     */
    class Intro



















    /**
     * ## 2 части
     *
     * - Введение в дизайн систему Flamingo
     * - Внутреннее устройство дизайн системы Flamingo
     */
    class TwoParts


















    /**
     * ## Что такое дизайн-система?
     *
     * Набор компонентов, правил, предписаний и инструментов для повышения качества и скорости
     * разработки продуктов, а также эффективной поддержки существующих.
     *
     * ⠀
     *
     * ## Составляющие ДС Flamingo
     *
     * - 🎨 Палитра цветов
     * - 🖍 Цвета темы
     * - __📦 UI компоненты__
     *      - [Button]
     *      - [AlertMessage]
     *      - [ListItem]
     *      - и т. д.
     * - Градиенты
     * - Тени
     * - ℹ️ Иконки
     * - 🖼️ Иллюстрации
     * - 🔤 Шрифты
     */
    class WhatIsDS


















    /**
     * ## Где документация?
     *
     * Документацией всей ДС является
     * [Flamingo Playground](https://confluence.companyname.ru/x/Aw1jdQE) —
     * демонстрационное приложение, позволяющее просматривать реальное содержимое дизайн-системы на
     * каждой платформе. Здесь можно посмотреть:
     *
     * - 📦 список и состояния всех компонентов
     * - 📖 документацию по использованию компонентов со ссылками на спецификацию и figma
     * - 🎨 палитру цветов
     * - 🖍 цвета темы
     * - ℹ️ набор иконок
     * - 🔤 стили шрифтов
     * - 🖼️ список иллюстраций
     * - и др.
     *
     * ⠀
     *
     * Библиотека 🦩 очень хорошо документирована с помощью
     * [KDocs](https://kotlinlang.org/docs/kotlin-doc.html).
     *
     * В будущем планируется выгружать все доки из кода с помощью
     * [Dokka](https://kotlin.github.io/dokka/1.6.0/), (аналог _Javadocs_) в HTML.
     */
    class WhereAreDocs


















    /**
     * ## Как открыть _Flamingo Playground_
     *
     * - Если у вас есть staging-сборка СберДруга
     *      - Откройте раздел "__Настройки staging-сборки__" через уведомление в шторке уведомлений
     *      - Нажмите на "__Дизайн система Flamingo__"
     *      - Откроется Flamingo Playground той версии Flamingo, с которой была собрана сборка
     *      СберДруга
     */
    class FlamingoPlaygroundInSberFriend


















    /**
     * ## Как открыть _Flamingo Playground_
     *
     * - Если у вас __нет__ staging-сборки СберДруга
     *      - Скачайте последнюю версию apk-файла Flamingo Playground из
     *      [GitHub репозитория](https://github.com/flamingo-ux/flamingo-android/releases), на
     *      странице __Releases__
     *      - Не забывайте обновлять это приложение, скачивая apk-файл вручную при выходе новой
     *      версии Flamingo
     */
    class FlamingoPlaygroundInApk














    /**
     * ## Две реализации
     *
     * Библиотека 🦩 имеет в себе 2 реализации ДС:
     * 1. Compose-based
     * 2. Android View-based (___Legacy___)
     *
     * Поддержка Android View-based реализации ограниченна, UI-компоненты постепенно заменяются на
     * Compose-based реализации.
     *
     * Несмотря на это, 🦩 можно использовать в Android View UI благодаря обратной совместимости
     * Compose с Android View.
     */
    class TwoImpls













    /**
     * ## Gradle Модули
     * [README.md]
     *
     */
    class GradleModules













    /**
     * ## Core API
     *
     * Object [Flamingo] содержит в себе все основные и часто используемые 🦩 API. Такое
     * объединение улучшает ___discoverability___: легко изучить ДС с нуля.
     */
    class CoreAPI



















    /**
     * ## Тема
     *
     * Использование любой части ДС возможно только внутри [FlamingoTheme].
     * Иначе произойдёт crash. ([Flamingo.checkFlamingoPresence]).
     */
    @Composable
    fun Theme() {
        MaterialTheme { /* code */ }
        FlamingoTheme { Chip(label = "Chip label") } // ✅
        Chip(label = "Chip label") // ❌
    }


















    /**
     * ## Цвета темы
     *
     * Это набор цветов, изменяющих своё значение в зависимости от выбранной темы.
     *
     * Сейчас разработано 2 темы: __светлая__ и __тёмная__. _В теории_ возможно создание
     * - 🎄 новогодней темы
     * - 🎃 темы для halloween
     * - и т. д.
     *
     * При создании UI необходимо юзать __🚨ТОЛЬКО🚨__ цвета из [Flamingo.colors]
     */
    @Composable
//    @Preview
//    @Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
    fun ThemeColors() = Preview {
        Box(
            modifier = Modifier
                .size(64.dp)
                .background(Flamingo.colors.primary)
        )
    }











    /**
     * За исключением UI, размещаемого __на__ градиенте или изображении/видео.
     *
     * В таких случаях нужно юзать [Flamingo.palette] - набор статичных цветов, не меняющих
     * своё значение.
     */
    @Composable
//    @Preview
    fun Palette() = Preview {
        Box(contentAlignment = Alignment.Center) {
            Image(
                modifier = Modifier.size(64.dp),
                painter = painterResource(com.flamingo.R.drawable.ds_gradient_pink),
                contentDescription = null
            )
            Text(text = "ABCD", color = Flamingo.palette.white)
        }
    }











    /**
     * ## Тени и скругления
     */
    @Composable
//    @Preview
//    @Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
    fun Shadows() = Preview {
        Box(modifier = Modifier.padding(12.dp)) {
            Card(
                elevation = Elevation.Solid.Medium,
                cornerRadius = CornerRadius.MEDIUM,
            ) {
                Spacer(modifier = Modifier.requiredSize(100.dp))
            }
        }
    }












    /**
     * ## Градиенты и иллюстрации
     *
     * Это [Drawable]s с префиксом `ds_gradient` и `ds_image` соответственно.
     */
    @Composable
    fun GradientsAndIllustrations() {
        com.flamingo.R.drawable.ds_gradient_pink
        com.flamingo.R.drawable.ds_image_calendar
    }















    /**
     * ## Шрифты
     *
     * В проекте необходимо юзать __🚨ТОЛЬКО🚨__ функцию [com.flamingo.components.Text].
     *
     * Запрещено юзать [androidx.compose.material.Text].
     *
     * Для изменения стиля текста нужно юзать [TextStyle]s из [Flamingo.typography].
     */
    @Composable
//    @Preview
//    @Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
    fun Fonts() = Preview {
        Text(text = loremIpsum(3), style = Flamingo.typography.body1)
    }



















    /**
     * ## Иконки
     *
     * Можно получить из [Flamingo.icons].
     *
     * Для отображения юзается [Icon].
     */
    @Composable
//    @Preview
//    @Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
    fun Icons() = Preview {
        Icon(icon = Flamingo.icons.Bell)
    }














    /**
     * # Компоненты
     *
     * Список доступных компонентов можно посмотреть в __Flamingo Playground__.
     *
     * Есть 2 типа компонентов:
     * 1. Compose-based
     * 2. Android View-based (___Legacy___)
     *
     * Со временем планируется полное избавление от Android View-based компонентов.
     *
     * Декларации всех 🦩-компонентов помечены аннотацией @[FlamingoComponent].
     *
     * Например, компонент [Chip].
     */
    @Composable
//    @Preview
    fun Components() = Preview {
        val n = FlamingoRegistry.components.count()
        Text(
            modifier = Modifier.padding(16.dp),
            text = buildAnnotatedString {
                append("Всего создано ")
                withStyle(
                    SpanStyle(fontWeight = FontWeight.Bold, color = Flamingo.colors.primary)
                ) { append(n.toString()) }
                append(" компонент(-ов)")
            },
        )
    }













    /**
     * ## Отсутствие modifier в 🦩-компонентах
     *
     * Параметр modifier намерено не помещается практически во все 🦩-компоненты по следующей
     * причине: явное наличие modifier позволяет легко применять запрещённые modifier'ы к
     * 🦩-компоненту (clickable, border, alpha, background) и изменять размеры запрещённым способом.
     * Компоненты не созданы для таких модификаций и не поддерживают их. Это означает, что поведение
     * компонентов не определено, если к ним применены несовместимые modifier'ы.
     *
     * ⠀
     *
     * Их всё равно можно применить, обернув компоненты в Box, но это explicitly обращает
     * внимание разработчиков на то, что modifier не применяется к самому компоненту, а лишь к его
     * обёртке. То есть компонент не ответственен за то, что делает его обёртка.
     *
     * ⠀
     *
     * Минусом такого подхода является то, что использование распространённых modifier'ов (padding)
     * усложняется.
     */
    @Composable
    fun NoModifiers() {
        Box(modifier = Modifier.padding(8.dp)) {
            IconButton(
                onClick = { TODO("snap 📸") },
                icon = Flamingo.icons.Camera,
                contentDescription = "Сделать фото"
            )
        }
    }














    /**
     * ## Debug Overlay
     *
     * Это специальный дебаг-режим, при включении которого (это возможно только в staging-сборке)
     * на всех 🦩-компонентах появляется:
     * 1. Красная квадратная обводка
     * 2. Полупрозрачный фон
     * 3. Имя компонента в левом верхнем углу
     *
     * ### Как включить
     *
     * 1. Пользователям:
     *      - Переключить toggle __Debug Overlay__ в Flamingo Playground
     *      - Переключить __quick settings toggle__ в шторке уведомлений, если пользователь заранее
     *      его добавил в шторку уведомлений (подробнее об этом — в Flamingo Playground)
     * 2. Разработчикам:
     *      - Если необходимо включить Debug Overlay из кода, нужно вызвать функции
     *      [Flamingo.enableDebugOverlay] и [Flamingo.disableDebugOverlay], находящиеся в
     *      `flamingo-playground` модуле
     */
    @Composable
//    @Preview
    fun DebugOverlay() {
        Flamingo.enableDebugOverlay()
        FlamingoTheme {
            Box(modifier = Modifier.padding(12.dp)) {
                ButtonComposePreview()
            }
        }
    }














    /**
     * ## White mode
     *
     * Это опция, которую поддерживают некоторые 🦩-компоненты (если
     * [FlamingoComponent.supportsWhiteMode] == `true`).
     *
     * Если она включена, компоненты изменяются так, что их можно размещать на цветном фоне
     * (градиенты, картинки, видео, др.)
     */
    @Composable
//    @Preview
    fun WhiteMode() = FlamingoTheme {
        Box {
            // background
            Image(
                modifier = Modifier.matchParentSize(),
                contentScale = ContentScale.FillBounds,
                painter = painterResource(com.flamingo.R.drawable.ds_gradient_pink),
                contentDescription = null
            )

            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                TopAppBarPreview()
                CompositionLocalProvider(Flamingo.LocalWhiteMode provides true) {
                    TopAppBarPreview()
                }

                Divider()
                CompositionLocalProvider(Flamingo.LocalWhiteMode provides true) {
                    Divider()
                }

                Button(
                    onClick = boast("Click"),
                    label = "Button",
                    color = ButtonColor.Default,
                    icon = Flamingo.icons.Briefcase,
                )
                Button(
                    onClick = boast("Click"),
                    label = "Button",
                    color = ButtonColor.White,
                    icon = Flamingo.icons.Briefcase,
                )
            }
        }
    }















    /**
     * ## Theater
     *
     * Это небольшой фреймворк для создания коротких 3D видео-тизеров о flamingo-компонентах.
     *
     * Он позволяет написать сюжет (plot) видеоролика на языке программирования kotlin и запустить
     * его на Android устройстве.
     */
    class Theater















    /**
     * Некоторые UI компоненты дизайн системы являются внутренними ([InternalComponents]) — их можно
     * использовать только __внутри__ ДС, например, для реализации других компонентов. Их __нельзя__
     * использовать в клиентском коде.
     *
     * Такие компоненты всё равно видны в галерее компонентов, но помечены как внутренние.
     */
    @Composable
    fun InternalComponents() {
        internalComponents.Search(value = "", onValueChange = {})
    }















    /**
     * ## [Button]
     * [ButtonWidthPolicy]
     */
    class ButtonComponent















    /**
     * ## [TopAppBar]
     * Для показа тени при скроллинге используйте специальные function overloads:
     */
    @Composable
    fun TopAppBarComponent() {
        TopAppBar(listState = rememberLazyListState())
        TopAppBar(scrollState = rememberScrollState())
    }











    /**
     * ## [ListItem]
     * - Ограничения на контент слотов
     * - Скелетон при загрузке
     */
    @Composable
    fun ListItemComponent() {
        ListItem(title = loremIpsum(3))
    }











    /**
     * ## [WidgetCardGroup]
     * - Концепция _layout_
     * - [LinkCard]
     */
    @Composable
    fun WidgetCardGroupComponent() {
        WidgetCardGroup { LinkCard(text = loremIpsum(2)) }
    }













    /**
     * ## Lint checks
     *
     * ### Common
     * - [com.flamingo.lint.AlphaDisabledDeclarationDetector]
     *
     * ### Android View
     * - [com.flamingo.lint.CardElevationInXmlDetector]
     * - [com.flamingo.lint.TextAppearanceDetector]
     *
     * ### Compose
     * - [com.flamingo.lint.WrongComponentAlternativeDetector]
     *      - Запрещает использовать аналоги flamingo-компонентов из других библиотек (material)
     *      - Неверные альтернативы показываются в Flamingo Playground на странице компонента
     */
    @Composable
    fun LintChecks() {
//        androidx.compose.material.RadioButton(selected = false, onClick = {})
    }

//    public val ALPHA_DISABLED = 0


















    /**
     * ## Автоматизированное UI-тестирование
     *
     * Библиотека поддерживает автоматизированное тестирование UI, написанного с её использованием.
     *
     * А именно: при включении [Flamingo.uiTestingTagsEnabled] все компоненты оборачиваются в
     * контейнеры, у которых есть contentDescription, содержащий имя компонента.
     *
     * Это нужно для тестирования с помощью фреймворка Appium.
     *
     * Если этих тегов недостаточно для тестирования вашего UI, есть [Modifier.uiTestingTag].
     */
    class UiTestingAutomation















    /**
     * # Конец первой части
     * ⌛ Перерыв 15 мин.
     */
    class Pause















    /**
     * # Внутренности Flamingo
     *
     * Эта часть презентации посвящена внутреннему устройству UI компонентов и Flamingo Playground
     */
    class Internals















    /**
     * # Crab
     *
     * Crab это написанный на KSP annotation processor, собирающий из исходного кода gradle-модуля
     * `flamingo`:
     *
     * - Значения properties в аннотации @[FlamingoComponent]
     * - KDocs функций, помеченных @FlamingoComponent (если @[FlamingoComponent.extractKDocs] ==
     * true)
     * - `@samples`, указанные в KDocs функций (подробнее — ниже)
     *
     */
    class Crab















    /**
     * # Зачем?
     *
     * ## Требования
     * Нужно, чтобы метаданные о UI компонентах
     * - были видны разработчику в файле исходного кода
     * самого компонента (не в отдельном файле)
     * - не попадали в production apk
     * - были доступны в runtime из модуля `flamingo-playground`
     */
    class WhyCrabRequirements















    /**
     * ## Проблемы
     *
     * - Невозможно сканировать classpath модуля во время выполнения и собирать список всех composable функций, которые также являются flamingo-компонентами.
     * - Composable функции недоступны через reflection в runtime: они не поддерживают _function references_.
     * - Они также не могут быть вызваны через reflection, потому что плагин компилятора Jetpack Compose изменяет их параметры.
     * - Текущий подход использует аннотации для подклассов View с [AnnotationRetention.RUNTIME],
     * что означает, что возможно конфиденциальная внутренняя документация включена в production apk.
     */
    class WhyCrabProblems















    /**
     * ## Существующее решение
     *
     * Библиотека [Showkase](https://github.com/airbnb/Showkase) от Airbnb также использует
     * annotation processing, но не подходит из-за этих ограничений:
     *
     * - `@ShowkaseComposable` имеет ограниченный набор параметров, нельзя добавить свои (figma,
     * specUrl, supportsWhiteMode, и т. д.)
     * - `@Preview`-annotated функции включаются в processing
     * - Нельзя отличить flamingo-компоненты от других composable функций
     */
    class ExistingSolutions















    /**
     * ## Как работает Crab
     *
     * - [F0ae2f6c706cff60bff7c0125b99cae]
     * - [FlamingoRegistry]
     */
    class HowCrabWorks















    /**
     * ## Lint check — _Wrong Component Alternative Detector_
     *
     * - [UsedInsteadOf] ([TopAppBar])
     * - [F0ae2f6c706cff60bff7c0125b99cae]
     * - [KspWrongComponentAlternatives]
     * - [copyConfigIntoResourcesDebug]
     *
     * Подробнее — [WrongComponentAlternativeDetector]
     */
    class WrongComponentAlternativeLint















    /**
     * ## Debug Overlay
     *
     * - [FlamingoComponentBase]
     * - [DebugOverlay]
     * - [Flamingo.enableDebugOverlay]
     * - [DebugOverlayImpl]
     */
    class InternalsOfDebugOverlay















    /**
     * ## Внутренности Theater
     *
     * - [Modifier.graphicsLayer]
     * - Backstage
     * - [TheaterPkg]
     */
    class TheaterInternals















    /**
     * ## Как быть Flaming Developer™-ом
     *
     * [Flamingo Developer Workflow](https://confluence.companyname.ru/x/3xs7AQI)
     */
    class HowToDevelopFlamingo















    /**
     * # The End!
     *
     * - [Репозиторий на GitHub](https://github.com/flamingo-ux/flamingo-android)
     * - [Confluence](https://confluence.companyname.ru/x/cIMLOwE)
     * - [Confluence — раздел Android](https://confluence.companyname.ru/x/v4cgegE)
     * - [Статья-введение на Medium](https://popovanton0.medium.com/building-a-modern-design-system-using-jetpack-compose-8bd8084e8b0c)
     * - [Youtube-плейлист с видеороликами Theater](https://www.youtube.com/playlist?list=PLGEKQ_tCWabRme1pUVZJLektqXXfDIW2G)
     * - [Запись этой презентации](https://youtu.be/QW6lD5ip9xs)
     * - [Запись предыдущей презентации о ДС](https://youtu.be/oRbQns82FX4)
     */
    class TheEnd














    companion object {
        /**
         * To be able to (un-)comment pieces of and still have import statements
         */
        @Suppress("UNUSED_EXPRESSION")
        @Composable
        fun References() {
            Preview::class
            Configuration::class
            Flamingo.disableDebugOverlay()
            Button(onClick = { /*TODO*/ }, label = "")
            AlertMessage(text = "", variant = AlertMessageVariant.SUCCESS)
            ListItem(title = "")
            InternalComponents::class
            ButtonWidthPolicy::class
            DebugOverlayImpl::class
            Modifier.graphicsLayer {}
            TheaterPkg::class
        }

        @Composable
        fun Preview(content: @Composable () -> Unit) = FlamingoTheme {
            Box(modifier = Modifier.background(Flamingo.colors.background)) {
                content()
            }
        }
    }
}
