plugins {
    id("bandlab.buildbenchmark.androidlib")
    id("bandlab.buildbenchmark.anvil")
    id("bandlab.buildbenchmark.anvil.custom")
    id("bandlab.buildbenchmark.dagger.compiler")
}

android {
    namespace = "bandlab.buildbenchmark.login"
}

dependencies {
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.bundles.feature)

    implementation(project(":login-api"))
    api(project(":infra"))
}
