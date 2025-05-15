package bandlab.buildbenchmark.buildsrc

import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmProjectExtension

class LibraryPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        target.plugins.run {
            apply(Plugins.KOTLIN_JVM)
        }

        target.extensions.configure(JavaPluginExtension::class.java) {
            it.sourceCompatibility = JavaVersion.VERSION_11
            it.targetCompatibility = JavaVersion.VERSION_11
        }

        target.extensions.configure(KotlinJvmProjectExtension::class.java) {
            it.compilerOptions {
                jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_11)
            }
        }
    }
}
