package com.example.wecom.dependency_injection

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
// application entry point to allow Android using  Dagger-Hilt.
@HiltAndroidApp
class BaseApplication:Application() {
}