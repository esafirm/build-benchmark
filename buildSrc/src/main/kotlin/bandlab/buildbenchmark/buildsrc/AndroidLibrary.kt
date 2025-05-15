@file:Suppress("DEPRECATION")

package bandlab.buildbenchmark.buildsrc

import com.android.build.gradle.BaseExtension
import org.gradle.api.Action
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmOptions

class AndroidLibrary : Plugin<Project> {
    override fun apply(target: Project) {
        target.plugins.run {
            apply(Plugins.ANDROID_LIBRARY)
            apply(Plugins.KOTLIN_ANDROID)
        }

        target.extensions.configure(BaseExtension::class.java) {
            it.compileSdkVersion(35)
            it.compileOptions {
                it.sourceCompatibility = JavaVersion.VERSION_11
                it.targetCompatibility = JavaVersion.VERSION_11
            }
            it.kotlinOptions {
                it.jvmTarget = "11"
            }
        }
    }
}


private fun BaseExtension.kotlinOptions(configure: Action<KotlinJvmOptions>): Unit =
    (this as org.gradle.api.plugins.ExtensionAware).extensions.configure("kotlinOptions", configure)
