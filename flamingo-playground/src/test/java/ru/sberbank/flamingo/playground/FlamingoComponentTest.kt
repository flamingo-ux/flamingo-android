package com.flamingo.playground

import androidx.fragment.app.Fragment
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import com.flamingo.annotations.view.FlamingoComponent
import com.flamingo.playground.preview.Preview
import com.flamingo.demoapi.FlamingoComponentDemoType
import java.util.stream.Stream
import com.flamingo.view.components.FlamingoComponent as FlamingoComponentInterface

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class FlamingoComponentTest {

    private val componentAnnClass = FlamingoComponent::class.java

    private lateinit var classes: List<Class<out FlamingoComponentInterface>>

    @BeforeAll
    fun setup() {
        classes = FlamingoComponentInterface::class.sealedSubclasses
            .filterNot { it.isAbstract }
            .map { it.java }
            .ifEmpty { error("Error while trying to locate DSC classes!") }
    }

    @Suppress("unused")
    fun classes(): Stream<Class<out FlamingoComponentInterface>> = classes.stream()

    @ParameterizedTest
    @MethodSource("classes")
    fun checkDisplayName(clazz: Class<out FlamingoComponentInterface>) {
        val ann = clazz.getAnnotation(componentAnnClass) ?: error(noAnnotation(clazz))
        if (ann.displayName.isBlank()) error(blankDisplayName(clazz))
    }

    @ParameterizedTest
    @MethodSource("classes")
    fun checkDemoReferences(clazz: Class<out FlamingoComponentInterface>) {
        val ann = clazz.getAnnotation(componentAnnClass) ?: error(noAnnotation(clazz))
        checkClassReference(
            componentClazz = clazz,
            demoClassName = ann.preview,
            expectedInterfaces = listOf(Preview::class.java),
            expectedDemoAnnotation = false,
            blankAllowed = false
        )?.run(::error)
        if (ann.theDemos.isEmpty()) error(noDemos(clazz))
        ann.theDemos.mapNotNull {
            checkClassReference(
                componentClazz = clazz,
                demoClassName = it,
                expectedDemoAnnotation = true,
                blankAllowed = false,
                expectedInterfaces = listOf(Fragment::class.java),
                demoName = "Demo"
            )
        }.forEach(::error)
    }

    /**
     * Checks the reference
     * @param demoClassName fully qualified demo class reference
     * @param expectedInterfaces that a demo class must implement
     * @param demoName to use in the error msg
     * @param blankAllowed if true, blank reference does not cause [noDemo] error
     * @returns null, if the reference is valid, else - error message
     */
    @Suppress("LongParameterList", "ReturnCount")
    private fun checkClassReference(
        componentClazz: Class<out FlamingoComponentInterface>,
        demoClassName: String,
        expectedInterfaces: List<Class<*>>,
        expectedDemoAnnotation: Boolean,
        blankAllowed: Boolean,
        demoName: String = expectedInterfaces.last().simpleName,
    ): String? {
        if (demoClassName.isBlank()) {
            if (blankAllowed) return null
            else error(noDemo(componentClazz, demoClassName, demoName))
        }
        val demoClass = try {
            Class.forName(demoClassName)
        } catch (e: ClassNotFoundException) {
            return noDemo(componentClazz, demoClassName, demoName)
        }
        expectedInterfaces.forEach {
            if (!it.isAssignableFrom(demoClass)) return invalidDemo(demoClassName, it, demoName)
        }
        if (expectedDemoAnnotation) {
            val noDemoAnnotations = demoClass.annotations.none {
                it!!.annotationClass.java.isAnnotationPresent(FlamingoComponentDemoType::class.java)
            }
            if (noDemoAnnotations) return invalidDemo(demoClassName, demoName)
        }
        val hasEmptyConstructor = demoClass.constructors.find { it.parameterCount == 0 } != null
        if (!hasEmptyConstructor) return noEmptyConstructor(demoClassName, demoName)
        return null
    }

    @ParameterizedTest
    @MethodSource("classes")
    fun checkFigmaUrl(clazz: Class<out FlamingoComponentInterface>) {
        val figmaUrlPrefix = "https://www.todo.com"
        val todoPrefix = "https://todo."
        val annotation = clazz.getAnnotation(componentAnnClass) ?: error(noAnnotation(clazz))
        val valid = annotation.figmaUrl.run { startsWith(figmaUrlPrefix) || startsWith(todoPrefix) }
        if (!valid) error(invalidFigmaUrl(figmaUrlPrefix, clazz))
    }

    @ParameterizedTest
    @MethodSource("classes")
    fun checkAccessorPatternUsage(clazz: Class<out FlamingoComponentInterface>) {
        val hasAccessor = FlamingoComponentInterface::class.java.isAssignableFrom(clazz)
        if (!hasAccessor) error(noAccessor(clazz))
    }

    @ParameterizedTest
    @MethodSource("classes")
    fun checkAnnotationPresence(clazz: Class<out FlamingoComponentInterface>) {
        if (!clazz.isAnnotationPresent(componentAnnClass)) error(noAnnotation(clazz))
    }

    private fun noEmptyConstructor(demoClassName: String, demoName: String): String {
        return "$demoName \"$demoClassName\" has no empty constructor, which is required"
    }

    private fun blankDisplayName(clazz: Class<out FlamingoComponentInterface>): String {
        return "A display name of the design system component ${clazz.name} shouldn't be blank"
    }

    private fun invalidDemo(demoClassName: String, demoName: String): String {
        return "$demoName class \"$demoClassName\" doesn't have annotations that are annotated " +
                "with @${FlamingoComponentDemoType::class.java.simpleName} annotation"
    }

    private fun invalidDemo(
        demoClassName: String,
        expectedInterface: Class<*>,
        demoName: String,
    ) = "$demoName class \"$demoClassName\" doesn't implement " + expectedInterface.simpleName

    private fun noDemos(
        componentClazz: Class<out FlamingoComponentInterface>,
    ) = "At least one demo of the ${componentClazz.name} must be provided"

    private fun noDemo(
        componentClazz: Class<out FlamingoComponentInterface>,
        demoClassName: String,
        demoName: String,
    ): String {
        return "$demoName class \"$demoClassName\" isn't found for design system component " +
                componentClazz.name
    }

    private fun invalidFigmaUrl(
        figmaUrlPrefix: String,
        clazz: Class<out FlamingoComponentInterface>,
    ): String {
        return "Invalid Figma URL. If should start with $figmaUrlPrefix: ${clazz.name}"
    }

    private fun noAccessor(clazz: Class<out FlamingoComponentInterface>): String {
        return "Design component class ${clazz.name} doesn't implement Accessor pattern " +
                "(see ${FlamingoComponentInterface::class.java})"
    }

    private fun noAnnotation(clazz: Class<out FlamingoComponentInterface>): String {
        return "Design component class ${clazz.name} is not annotated with " +
                "@${componentAnnClass.simpleName}"
    }
}
