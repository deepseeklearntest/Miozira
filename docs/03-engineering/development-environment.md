# Miozira — Development Environment

**Document:** `development-environment.md`
**Version:** 0.1
**Status:** Current Phase 0 setup checklist; locally verified
**Product:** Miozira Prototype 0.1
**Primary platform:** Android tablet
**Last updated:** 2026-08-26

## 1. Purpose

This document is the install checklist for building and validating Miozira locally.

It covers:

- required host tools;
- Android SDK components;
- repository-pinned build versions;
- optional physical-device and emulator tools;
- commands that prove the environment is ready.

The app itself remains offline-first. Internet access is needed only to install tools and resolve build dependencies; the app must not require network access at runtime.

## 2. Required host tools

Install these before running Gradle:

| Tool | Required version | Purpose | Verification |
|---|---:|---|---|
| Git | Current supported version | Source control and clean-checkout validation | `git --version` |
| JDK | 17 | Kotlin, Gradle, and Android builds | `java -version` |
| OpenJDK / Corretto | 17.x | Recommended open-source JDK 17 distribution for macOS | `/opt/homebrew/opt/openjdk@17/bin/java -version` or `/usr/libexec/java_home -v 17` |
| macOS | Supported version for installed Android tooling | Current development host | `sw_vers` |

### Installing JDK 17 via Homebrew

**Option A (Recommended, no sudo required):**
```bash
brew install openjdk@17
```

**Option B (Amazon Corretto cask, requires sudo for installer pkg):**
```bash
brew install --cask corretto@17
```

Configure `JAVA_HOME` in your shell (`~/.zshrc`):

```bash
# For Homebrew openjdk@17:
export JAVA_HOME="/opt/homebrew/opt/openjdk@17/libexec/openjdk.jdk/Contents/Home"
# Or dynamic macOS java_home lookup:
# export JAVA_HOME="$(/usr/libexec/java_home -v 17)"

export PATH="/opt/homebrew/opt/openjdk@17/bin:$PATH"
```

## 3. Android build tools

You can install Android build tooling via Homebrew CLI packages or Android Studio GUI.

### Option A: Command-line Tools via Homebrew (Headless / Fast)

```bash
# 1. Install platform tools (adb) and command line tools (sdkmanager)
brew install --cask android-platform-tools android-commandlinetools

# 2. Create standard Android SDK directory
mkdir -p "$HOME/Library/Android/sdk"

# 3. Accept licenses and install Android 36 platform + build tools
yes | sdkmanager --sdk_root="$HOME/Library/Android/sdk" --licenses
sdkmanager --sdk_root="$HOME/Library/Android/sdk" \
  "platform-tools" \
  "platforms;android-36" \
  "build-tools;36.0.0"
```

### Option B: Android Studio GUI

```bash
brew install --cask android-studio
```
Open Android Studio → **Settings → Languages & Frameworks → Android SDK**, and ensure **Android API 36** and **Android SDK Build-Tools** are checked.

### Android Studio JVM versus Gradle JVM

Android Studio and the Miozira Gradle build do not need to run on the same bundled JVM:

- Android Studio 2026.1 includes JetBrains Runtime 25. Keep that bundled runtime for running the IDE itself.
- Miozira uses Gradle 8.11.1, which can run on Java 8–23 but cannot run on Java 25.
- Miozira's modules explicitly use Java toolchain 17, Android Gradle Plugin 8.6.1 has a JDK 17 baseline, and CI runs JDK 17.

Therefore, configure this project's **Gradle JDK** as JDK 17 even though the latest Android Studio runs on JBR 25. In Android Studio, open:

**Settings → Build, Execution, Deployment → Build Tools → Gradle → Gradle JDK**

Select `JAVA_HOME`, Corretto 17, or the explicit JDK path:

```text
/Library/Java/JavaVirtualMachines/amazon-corretto-17.jdk/Contents/Home
```

Do not select Android Studio's bundled `jbr` directory for this repository while it contains Java 25. A machine-local `.gradle/config.properties` entry pointing `java.home` to Android Studio's JBR 25 will produce the JVM incompatibility error and should be changed through the Gradle JDK setting above.

JDK 21 is supported by Gradle 8.11.1, but it is not required here and would differ from the repository's Java 17 toolchain and CI baseline. Move to JDK 21 only as part of a deliberate build-toolchain upgrade, with the Gradle, AGP, Kotlin, and Compose compatibility checks rerun together.

**Do not install a dotted/preview platform revision** (e.g. `android-37.0`, `android-36.1`, or any
`-beta`/`-rc` suffixed package) as the project's compileSdk. AGP 8.6.1 (this repo's pin) only
resolves plain integer `android-<N>` platform folders; a dotted revision fails with `Failed to
find Platform SDK with path: platforms;android-<N>`, and using one as compileSdk would require an
AGP 9.x migration (Gradle wrapper bump plus re-verifying Kotlin/Compose Multiplatform
compatibility) to target a platform revision that isn't even the stable release line.

### Required SDK Components

| Component | Version / selection | Purpose |
|---|---:|---|
| Android SDK Platform | API 36 (plain, not a dotted/preview revision) | compileSdk and targetSdk for Prototype 0.1 (pinned in `androidApp/build.gradle.kts` and `shared/build.gradle.kts`) |
| Android SDK Build-Tools | 36.0.0 (or latest compatible 36.x, non-preview) | APK compilation and manifest packaging |
| Android SDK Platform-Tools | 36+ / Latest | `adb` and physical-device communication |

AGP 8.6.1 self-reports it was only tested up to compileSdk 35; compileSdk 36 builds successfully
but prints a "we recommend a newer Android Gradle plugin" warning. This is a soft warning, not a
build failure — bumping AGP further to silence it is optional future work, not required for this
build to work.

### Recommended Optional Components (for Emulator validation)

```bash
sdkmanager --sdk_root="$HOME/Library/Android/sdk" \
  "emulator" \
  "system-images;android-36;google_apis_playstore;arm64-v8a"
```

### Environment Variables (`~/.zshrc`)

Set the SDK paths and tool binaries in your `~/.zshrc`:

```bash
export ANDROID_HOME="$HOME/Library/Android/sdk"
export ANDROID_SDK_ROOT="$ANDROID_HOME"
export PATH="$ANDROID_HOME/platform-tools:$ANDROID_HOME/cmdline-tools/latest/bin:$PATH"
```

### Avoid two SDK installs

If you install the standalone `android-commandlinetools` cask (Homebrew installs it to
`/opt/homebrew/share/android-commandlinetools`) *and* separately open the project in Android
Studio, you end up with two independent SDK directories on disk. `local.properties`
(machine-local, gitignored, not part of the repo) pins whichever one Gradle uses via `sdk.dir`,
so it can silently diverge from the one Android Studio defaults to
(`~/Library/Android/sdk`) — Android Studio's SDK Manager will flag this with a "project and
Android Studio point to different Android SDKs" prompt. Fix by setting `local.properties` to the
same path Android Studio uses:

```
sdk.dir=/Users/<you>/Library/Android/sdk
```

Then confirm `platforms;android-36` and `build-tools;36.0.0` exist under that SDK root (install
them with the `sdkmanager` command in Option A above, pointed at that root, if not). Prefer a
single SDK location over keeping both in sync.

## 4. Repository-pinned build dependencies

The Gradle wrapper downloads the pinned Gradle version automatically. Do not install a separate global Gradle unless useful for personal tooling.

| Dependency | Version in repository | Location |
|---|---:|---|
| Gradle Wrapper | 8.11.1 | `gradle/wrapper/gradle-wrapper.properties` |
| Android Gradle Plugin | 8.6.1 | `gradle/libs.versions.toml` |
| Kotlin | 2.0.21 | `gradle/libs.versions.toml` |
| Compose Multiplatform | 1.7.0 | `gradle/libs.versions.toml` |
| Compose Compiler | 2.0.21 | `gradle/libs.versions.toml` |
| AndroidX Activity Compose | 1.9.3 | `gradle/libs.versions.toml` |
| AndroidX Core KTX | 1.13.1 | `gradle/libs.versions.toml` |
| AndroidX Test JUnit KTX | 1.2.1 | `gradle/libs.versions.toml` |
| AndroidX Test Runner | 1.6.2 | `gradle/libs.versions.toml` |

These are resolved by Gradle from the repositories declared in `settings.gradle.kts`. No manual JAR or SDK download is required for them.

## 5. Optional physical-tablet tools

For the mandatory technical spike and later device validation, also prepare:

- one Android tablet representing the family-test device;
- a USB cable;
- Android Developer Options and USB debugging enabled on the tablet;
- `adb devices` showing the tablet as authorized;
- the tablet's Android version, API level, RAM, storage, and screen dimensions recorded in `docs/05-quality/device-test-matrix.md`.

No Google account, backend credential, cloud speech account, analytics account, or commercial SDK account is required.

## 6. What does not need to be installed

The following are intentionally outside Prototype 0.1 setup:

- Node.js or npm;
- Python;
- Ruby or CocoaPods;
- a global Gradle installation;
- a database server;
- a cloud provider CLI;
- a speech-recognition SDK or service;
- analytics, crash-reporting, advertising, or remote-configuration SDKs;
- iOS/Xcode tooling for the Android-first Phase 0 and technical spike.

## 7. Environment verification

From the repository root, run:

```bash
# Set Java 17 path (Homebrew openjdk@17 or macOS java_home)
export JAVA_HOME="/opt/homebrew/opt/openjdk@17/libexec/openjdk.jdk/Contents/Home"
# Or: export JAVA_HOME="$(/usr/libexec/java_home -v 17)"

java -version
git --version
adb version
./gradlew --version
./gradlew :shared:testDebugUnitTest :androidApp:assembleDebug
./gradlew :androidApp:checkManifestPolicy
```

Expected Phase 0 results:

- Java reports version 17;
- Gradle reports wrapper version 8.11.1;
- shared debug unit tests pass;
- a debug APK is produced;
- the merged Android manifest contains exactly `android.permission.RECORD_AUDIO` and no `android.permission.INTERNET`.

If no physical device is connected, `adb version` may still pass; device-specific commands should wait until the tablet is available.

### Verified on 2026-08-26

This checklist has been run end-to-end on a macOS development machine and confirmed working:

| Check | Result |
|---|---|
| `java -version` | `openjdk version 17.0.20.1` (`/opt/homebrew/opt/openjdk@17`) |
| `adb version` | `Android Debug Bridge version 1.0.41`, Version `37.0.1` (`~/Library/Android/sdk/platform-tools/adb`) |
| `local.properties` `sdk.dir` | `~/Library/Android/sdk` — consolidated to Android Studio's default SDK location (see "Avoid two SDK installs" above); the project no longer points at a separate Homebrew-cask SDK |
| `./gradlew --version` | Gradle 8.11.1 on JVM 17 |
| `./gradlew :shared:testDebugUnitTest :androidApp:assembleDebug` | `BUILD SUCCESSFUL` — common unit test passed, debug APK produced |
| `./gradlew :androidApp:checkManifestPolicy` | `BUILD SUCCESSFUL` — merged manifest contains only `android.permission.RECORD_AUDIO`, no `android.permission.INTERNET` |

This is the reference outcome for a healthy setup. If any command in the checklist above produces a different result, the environment — not the build configuration — is the first thing to suspect.

### Verified again on 2026-08-26 (compileSdk 36 bump)

`compileSdk`/`targetSdk` were bumped from 35 to 36 (see [`platform-support.md`](platform-support.md), which already anticipated 36) after the SDK consolidation above surfaced that Android Studio's default SDK also had newer platforms installed. A clean rebuild against `platforms;android-36` / `build-tools;36.0.0` passed:

- `./gradlew clean :shared:testDebugUnitTest :androidApp:assembleDebug :androidApp:compileDebugAndroidTestKotlin :androidApp:checkManifestPolicy` → `BUILD SUCCESSFUL`, 78/78 tasks executed.
- AGP 8.6.1 prints a soft "we recommend a newer Android Gradle plugin" warning at compileSdk 36 (it self-reports testing only up to 35) — this is not a build failure and does not block anything.

**API 37 was evaluated and rejected for this bump.** The only "37" available on this machine is a dotted preview revision (`android-37.0`, sibling to `android-37.1` and beta channels `37.2-beta1/2/3`), not a plain stable platform. AGP 8.6.1 cannot resolve dotted platform paths at all (hard failure: `Failed to find Platform SDK with path: platforms;android-37`), and supporting them would require migrating to AGP 9.x — a Gradle-wrapper bump plus re-verifying Kotlin Multiplatform and Compose Multiplatform compatibility — to target a platform that isn't the stable release line. compileSdk 36 was chosen instead as the latest plain stable platform, deliberately, not because 37 was unavailable in principle.

## 8. Dependency/privacy review rule

Every new Gradle dependency must be:

1. pinned in `gradle/libs.versions.toml`;
2. justified against the current MVP phase;
3. checked for permissions, network behavior, storage behavior, and telemetry;
4. reflected in the relevant privacy and release evidence.

Do not add a dependency merely to simplify a feature that the current MVP phase does not require.
