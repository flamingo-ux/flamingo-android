# Crab

Crab is a KSP-based annotation processor that collects:

1.  Values of properties in @FlamingoComponent annotation usages
2.  KDocs of functions, annotated with @FlamingoComponent (if @FlamingoComponent.extractKDocs == 
    true)
3.  Samples, mentioned in KDocs of functions (more on that later)

# Why

## Problems

1.  It is not possible to scan module classpath at runtime and collect a list of all composable 
    functions that are also flamingo components.
2.  Composable functions are not accessible through reflection at runtime: they do not support 
    function references.
3.  They also cannot be called through reflection, because Jetpack Compose compiler plugin modifies 
    their parameters.
4.  Current approach uses annotations on View subclasses with RUNTIME retention, meaning that 
    possibly sensitive internal documentation is included in the production apk.


## Solution

Airbnb's [Showkase](https://github.com/airbnb/Showkase) also uses annotation processing, but cannot 
be used in our project due to a following limitations:

1.  @ShowkaseComposable has limited set of parameters, which cannot be extended.
    1.  There is no way to mark and distinguish flamingo components from other composable functions.
2.  @Preview\-annotated functions are also included in processing.
3.  Right now, it uses kapt, which is **slow**. There is a 
    [PR](https://github.com/airbnb/Showkase/pull/146) to rewrite Showkase in ksp, but the status is 
    unknown.


Airbnb also has an internal tool, similar to _Showkase_. It also uses annotation processing. You can
learn about it [here](https://player.vimeo.com/video/380953225) (jump to 20m 53s)

All things considered, it was decided to write our own annotation processor to solve all mentioned 
problems.

# Stages of processing

1.  MetadataExtractor scans all gradle modules, for which crab is enabled, (even _demo modules_) and
    extracts previously mentioned information.
2.  Then RegistryGenerator scans only _demo modules_ and produces FlamingoRegistry, which contains 
    previously mentioned information from all modules, on whom this _demo module_ depends.

# Gradle configuration

All modules, from whom previously mentioned information must be collected, MUST add this code to the
build.gradle file:
```groovy
apply plugin: 'com.google.devtools.ksp'

android {
   // let IDE know about generated files (enables autocompletion, for example). 
   // needed only for "demo modules"
   sourceSets {
      debug {
         java {
            srcDir 'build/generated/ksp/debug/kotlin'
         }
      }
      release {
         java {
            srcDir 'build/generated/ksp/release/kotlin'
         }
      }
   }
}

ksp {
    arg("crab.rootProjectDir", rootProject.projectDir.absolutePath)
    arg("crab.moduleName", project.name)
    // For a module to be considered "demo module", this line MUST BE added:
    arg("crab.demoModule", "yaas")
    // If there are multiple demo modules, all non-demo modules MUST add:
    arg("crab.targetDemoModuleName", "demo-module-1")
}

dependencies {
   ksp project(":crab")
}
```

## For ':app' module

Also, this must be added to build.gradle of the app module (from which apk is build):

```groovy
packagingOptions {
    // MUST be present to remove enormous amount of metadata files (with source code in them)
    // from the build artifact
    exclude '**/com/flamingo/crab/generated/**'
}
```

# Samples extraction

If function annotated with @FlamingoComponent has KDocs, and KDocs has samples, specified like this:

```kotlin
/**
 * ...
 * @sample com.flamingo.playground.components.EmptyStateSample1
 * @sample com.flamingo.playground.components.EmptyStateSample2 no code
 * @sample com.flamingo.playground.components.EmptyStateSample3 no preview
 * @sample com.flamingo.playground.components.EmptyStateSample4 no code no preview
 * ...
 */
@FlamingoComponent(
   // ...
)
@Composable
public fun EmptyState() = error("Not implemented")
```

then RegistryGenerator will resolve fully qualified names of specified samples and, if no code is 
not specified, add source code of the file that contains particular sample to the FlamingoRegistry. 
Also, if no preview is not specified, RegistryGenerator will add a composable lambda to 
FlamingoRegistry, which calls the sample function with no arguments.

If no code no preview is specified, sample will not be included in FlamingoRegistry at all.

> **_NOTE:_** All samples MUST BE @Composable functions. Else, they MUST specify no code no preview

# Impact on build time

This KSP annotation processor supports 
[KSP's incremental processing](https://github.com/google/ksp/blob/main/docs/incremental.md). Thus, 
incremental builds are faster.

## Additional time

1.  Clean build:
    *   `:flamingo:kspDebugKotlin` = 11.1s (7.3%)
    *   `:flamingo-playground:kspDebugKotlin` = 2.3s (1.5%)
2.  Incremental build:
    *   `:flamingo:kspDebugKotlin` = 0.9s (6.6%)
    *   `:flamingo-playground:kspDebugKotlin` = 0.1s (0.9%)


# Naming

The name "Crab" was chosen because of semantic connection to the _flamingo_ and because crabs are
presumably good at carefully and selectively collecting stuff from the shore.
