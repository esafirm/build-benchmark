package bandlab.buildbenchmark.buildsrc

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.TaskCollection
import org.gradle.api.tasks.TaskContainer
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

class AnvilCompilerPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        target.plugins.apply(Plugins.ANVIL)
        target.plugins.apply(Plugins.KSP)

        target.tasks.compileKotlinTasks().configureEach {
            it.compilerOptions {
                optIn.add("com.squareup.anvil.annotations.ExperimentalAnvilApi")
            }
        }
    }
}


internal fun TaskContainer.compileKotlinTasks(): TaskCollection<KotlinCompile> =
    withType(KotlinCompile::class.java)
        .named { it.startsWith("compile") && it.endsWith("Kotlin") }
