plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
   
}

android {
    namespace = "com.example.mindfulllearn"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.mindfulllearn"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        viewBinding = true

    }
}

dependencies {
    implementation ("com.github.bumptech.glide:glide:4.12.0")

    dependencies {
        implementation("io.socket:socket.io-client:2.1.0")
        // Other dependencies...
    }

    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")

    implementation ("com.google.code.gson:gson:2.10.1")
    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.10.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("com.google.android.gms:play-services-maps:18.2.0")
    implementation("androidx.navigation:navigation-fragment-ktx:2.7.5")
    implementation("androidx.navigation:navigation-ui-ktx:2.7.5")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    implementation ("org.osmdroid:osmdroid-android:6.1.14")
    implementation ("com.google.android.gms:play-services-location:18.0.0")
    implementation ("com.jakewharton.timber:timber:5.0.+")
    implementation ("org.greenrobot:eventbus:3.2.0")
    implementation ("com.squareup.okhttp3:okhttp:5.0.0-alpha.2")
    implementation ("com.mesibo.api:webrtc:1.0.5")
    implementation ("org.java-websocket:Java-WebSocket:1.5.3")
    implementation ("com.guolindev.permissionx:permissionx:1.6.1")
}