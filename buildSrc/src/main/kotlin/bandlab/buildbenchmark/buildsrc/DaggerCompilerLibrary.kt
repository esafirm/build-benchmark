package bandlab.buildbenchmark.buildsrc

import com.android.build.api.variant.LibraryAndroidComponentsExtension
import com.squareup.anvil.plugin.AnvilExtension
import org.gradle.api.Plugin
import org.gradle.api.Project

class DaggerCompilerLibrary : Plugin<Project> {
    override fun apply(target: Project) {
        target.plugins.apply(Plugins.KAPT)

        // KSP cannot generate component when dagger kapt is active
        target.extensions.configure(AnvilExtension::class.java) {
            it.generateDaggerFactories.set(false)
        }
        target.dependencies.add("kapt", "com.google.dagger:dagger-compiler:2.56.2")

        target.extensions.configure(LibraryAndroidComponentsExtension::class.java) {
            it.onVariants(it.selector().all()) { variant ->
                target.connectKspOutputsToKaptInputs(variant.name)
            }
        }
    }
}
