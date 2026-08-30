plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.kotlinAndroid)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
}

android {
    namespace = "com.miozira"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.miozira"
        minSdk = 26
        targetSdk = 36
        versionCode = 1
        versionName = "0.1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildFeatures {
        compose = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

kotlin {
    jvmToolchain(17)
}

androidComponents {
    onVariants(selector().withBuildType("release")) { variant ->
        val mergedManifest = variant.artifacts.get(com.android.build.api.artifact.SingleArtifact.MERGED_MANIFEST)

        tasks.register("checkManifestPolicy") {
            inputs.file(mergedManifest)

            doLast {
                val manifestFile = mergedManifest.get().asFile
                check(manifestFile.isFile) { "Merged release manifest not found: $manifestFile" }

                val factory = javax.xml.parsers.DocumentBuilderFactory.newInstance()
                factory.isNamespaceAware = true
                val document = factory.newDocumentBuilder().parse(manifestFile)
                val androidName = "http://schemas.android.com/apk/res/android"
                val permissions = (0 until document.documentElement.childNodes.length)
                    .map(document.documentElement.childNodes::item)
                    .filter { it.nodeName == "uses-permission" }
                    .map { it.attributes.getNamedItemNS(androidName, "name")?.nodeValue }
                    .filterNotNull()
                    // AndroidX injects self-signed, app-scoped permissions (e.g. profileinstaller's
                    // DYNAMIC_RECEIVER_NOT_EXPORTED_PERMISSION) that grant no external capability;
                    // only android.permission.* entries are meaningful to the offline-access policy.
                    .filter { it.startsWith("android.permission.") }
                    .toSet()
                val allowlist = rootProject.file("config/android-permission-allowlist.txt")
                    .readLines()
                    .map(String::trim)
                    .filter { it.isNotEmpty() && !it.startsWith("#") }
                    .toSet()

                check(permissions == allowlist) {
                    "Merged manifest permissions $permissions do not equal allowlist $allowlist"
                }
                check("android.permission.INTERNET" !in permissions) {
                    "Forbidden network permission found in merged manifest"
                }
            }
        }
    }
}

dependencies {
    implementation(project(":shared"))
    implementation(compose.runtime)
    implementation(compose.foundation)
    implementation(compose.material3)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.core.ktx)
    androidTestImplementation(libs.androidx.test.junit)
    androidTestImplementation(libs.androidx.test.runner)
    androidTestImplementation(kotlin("test"))
}
