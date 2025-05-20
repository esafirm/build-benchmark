plugins {
    id("bandlab.buildbenchmark.lib")
    id("bandlab.buildbenchmark.anvil.compiler")
}

dependencies {
    implementation(project(":infra"))

    implementation(libs.anvil.compiler.api)
    implementation(libs.anvil.compiler.utils)
    implementation(libs.dagger)
    implementation(libs.google.auto.service.annotations)
    ksp(libs.google.auto.service.ksp)
}
