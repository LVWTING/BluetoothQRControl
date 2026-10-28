plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.example.bluetoothqrcontrol"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.bluetoothqrcontrol"
        minSdk = 23
        targetSdk = 32
        versionCode = 1
        versionName = "1.0"
    }
}
