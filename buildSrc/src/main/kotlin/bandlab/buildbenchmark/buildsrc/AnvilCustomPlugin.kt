package bandlab.buildbenchmark.buildsrc

import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * A plugin to apply custom code generator
 */
class AnvilCustomPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        target.plugins.apply(Plugins.KSP)
        target.dependencies.add("ksp", target.project(":codegenerator"))
    }
}
