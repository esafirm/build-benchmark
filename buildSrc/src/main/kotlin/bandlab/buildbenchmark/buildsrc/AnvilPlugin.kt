package bandlab.buildbenchmark.buildsrc

import com.squareup.anvil.plugin.AnvilExtension
import org.gradle.api.Plugin
import org.gradle.api.Project

class AnvilPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        target.plugins.apply(Plugins.ANVIL)
        AnvilConfiguration().configure(target)
    }
}

class AnvilConfiguration(
    private val enableGenerateDaggerFactories: Boolean = true,
) {
    fun configure(project: Project) {
        project.dependencies.add("implementation", "com.google.dagger:dagger:2.56.2")

        project.extensions.configure(AnvilExtension::class.java) {
            // Enable the Anvil factory generation by default
            it.generateDaggerFactories.set(enableGenerateDaggerFactories)
            // To support incremental compilation
            it.trackSourceFiles.set(true)

            it.useKsp(
                contributesAndFactoryGeneration = true,
                componentMerging = true
            )
        }
    }
}
