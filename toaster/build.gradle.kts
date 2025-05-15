plugins {
    id("bandlab.buildbenchmark.androidlib")
}

android {
    namespace = "bandlab.buildbenchmark.toaster"
}

dependencies {
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.bundles.feature)

    implementation(project(":infra"))
    api(project(":login-api"))
}
