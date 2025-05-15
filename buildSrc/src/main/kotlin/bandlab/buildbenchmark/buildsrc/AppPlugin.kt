package bandlab.buildbenchmark.buildsrc

import com.android.build.api.variant.ApplicationAndroidComponentsExtension
import com.google.devtools.ksp.gradle.KspAATask
import com.google.devtools.ksp.gradle.KspTaskJvm
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

class AppPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        target.plugins.run {
            apply(Plugins.ANDROID_APP)
            apply(Plugins.KOTLIN_ANDROID)
            apply(Plugins.COMPOSE)
            apply(Plugins.KAPT)
            apply(Plugins.ANVIL)
        }

        AnvilConfiguration(enableGenerateDaggerFactories = false)
            .configure(target)

        target.extensions.configure(ApplicationAndroidComponentsExtension::class.java) {
            it.onVariants(it.selector().all()) { variant ->
                target.connectKspOutputsToKaptInputs(variant.name)
            }
        }
    }

    internal fun Project.connectKspOutputsToKaptInputs(variantName: String) {
        afterEvaluate {
            val buildType = variantName.replaceFirstChar { it.titlecaseChar() }
            val kspTaskName = "ksp${buildType}Kotlin"
            val kspReleaseTask = tasks.named(kspTaskName, KspAATask::class.java)
            val generatedKspKotlinFiles = kspReleaseTask.flatMap { it.kspConfig.kotlinOutputDir }

            tasks.named("kaptGenerateStubs${buildType}Kotlin", KotlinCompile::class.java)
                .configure {
                    it.source(generatedKspKotlinFiles)
                }
        }
    }
}
