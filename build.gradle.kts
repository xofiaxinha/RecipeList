// No arquivo build.gradle.kts da RAIZ do projeto
plugins {
    alias(libs.plugins.android.application) apply false
    id("org.jetbrains.kotlin.android") version "1.9.22" apply false
    id("com.google.gms.google-services") version "4.3.15" apply false
    // id("org.jetbrains.compose") version "1.5.3" apply false  <-- REMOVA ESTA LINHA
    id("com.google.devtools.ksp") version "1.9.22-1.0.17" apply false
    id("com.google.firebase.crashlytics") version "3.0.1" apply false
}