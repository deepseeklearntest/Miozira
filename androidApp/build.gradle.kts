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

    val releaseStorePath = providers.environmentVariable("MIOZIRA_RELEASE_STORE_FILE").orNull
    val releaseStorePassword = providers.environmentVariable("MIOZIRA_RELEASE_STORE_PASSWORD").orNull
    val releaseKeyAlias = providers.environmentVariable("MIOZIRA_RELEASE_KEY_ALIAS").orNull
    val releaseKeyPassword = providers.environmentVariable("MIOZIRA_RELEASE_KEY_PASSWORD").orNull
    val releaseSigningConfigured = listOf(
        releaseStorePath,
        releaseStorePassword,
        releaseKeyAlias,
        releaseKeyPassword,
    ).all { !it.isNullOrBlank() }

    signingConfigs {
        if (releaseSigningConfigured) {
            create("familyTest") {
                storeFile = file(releaseStorePath!!)
                storePassword = releaseStorePassword
                keyAlias = releaseKeyAlias
                keyPassword = releaseKeyPassword
            }
        }
    }

    buildTypes {
        getByName("release") {
            if (releaseSigningConfigured) signingConfig = signingConfigs.getByName("familyTest")
        }
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

tasks.matching { it.name == "assembleRelease" }.configureEach {
    dependsOn("checkManifestPolicy")
}

tasks.register("verifyFamilyTestSigning") {
    doLast {
        val required = providers.environmentVariable("MIOZIRA_REQUIRE_SIGNED_RELEASE").orNull == "true"
        val configured = listOf(
            "MIOZIRA_RELEASE_STORE_FILE",
            "MIOZIRA_RELEASE_STORE_PASSWORD",
            "MIOZIRA_RELEASE_KEY_ALIAS",
            "MIOZIRA_RELEASE_KEY_PASSWORD",
        ).all { !providers.environmentVariable(it).orNull.isNullOrBlank() }
        check(!required || configured) {
            "Signed family-test release requested, but MIOZIRA_RELEASE_* signing variables are incomplete"
        }
    }
}

tasks.register("checkDependencyPolicy") {
    doLast {
        val denylist = rootProject.file("config/dependency-denylist.txt")
            .readLines()
            .map(String::trim)
            .filter { it.isNotEmpty() && !it.startsWith("#") }
        val configuration = configurations.getByName("releaseRuntimeClasspath")
        val groups = configuration.incoming.resolutionResult.allComponents
            .mapNotNull { it.moduleVersion?.group }
            .toSet()
        val forbidden = groups.filter { group -> denylist.any { prefix -> group == prefix || group.startsWith("$prefix.") } }
        check(forbidden.isEmpty()) {
            "Forbidden SDK groups found in release runtime classpath: $forbidden"
        }
    }
}

tasks.register("checkBackupPolicy") {
    val legacyRules = layout.projectDirectory.file("src/main/res/xml/backup_rules.xml")
    val modernRules = layout.projectDirectory.file("src/main/res/xml/data_extraction_rules.xml")
    inputs.files(legacyRules, modernRules)
    doLast {
        val requiredExclusions = setOf("family-recordings/", "miozira.db", ".")
        listOf(legacyRules.asFile, modernRules.asFile).forEach { rulesFile ->
            check(rulesFile.isFile) { "Backup rules file not found: $rulesFile" }
            val factory = javax.xml.parsers.DocumentBuilderFactory.newInstance()
            val document = factory.newDocumentBuilder().parse(rulesFile)
            val exclusions = (0 until document.documentElement.childNodes.length)
                .flatMap { index ->
                    val node = document.documentElement.childNodes.item(index)
                    if (node.nodeName == "exclude") {
                        listOf(node.attributes.getNamedItem("path")?.nodeValue)
                    } else {
                        (0 until node.childNodes.length)
                            .mapNotNull { childIndex ->
                                val child = node.childNodes.item(childIndex)
                                if (child.nodeName == "exclude") child.attributes.getNamedItem("path")?.nodeValue else null
                            }
                    }
                }
                .filterNotNull()
                .toSet()
            check(requiredExclusions.all { it in exclusions }) {
                "$rulesFile is missing required backup exclusions: ${requiredExclusions - exclusions}"
            }
        }
    }
}

tasks.matching { it.name == "assembleRelease" || it.name == "lintRelease" }.configureEach {
    dependsOn("checkDependencyPolicy", "checkBackupPolicy")
}

dependencies {
    implementation(project(":shared"))
    implementation(compose.runtime)
    implementation(compose.foundation)
    implementation(compose.material3)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.datastore.preferences)
    androidTestImplementation(libs.androidx.test.junit)
    androidTestImplementation(libs.androidx.test.runner)
    androidTestImplementation(kotlin("test"))
}
