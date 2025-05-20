package bandlab.buildbenchmark.buildsrc

import com.google.devtools.ksp.gradle.KspAATask
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

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
