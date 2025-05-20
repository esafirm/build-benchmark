package bandlab.buildbenchmark.buildsrc

import com.android.build.api.variant.ApplicationAndroidComponentsExtension
import org.gradle.api.Plugin
import org.gradle.api.Project

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
}
