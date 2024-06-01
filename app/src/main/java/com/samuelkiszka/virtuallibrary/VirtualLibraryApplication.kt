package com.samuelkiszka.virtuallibrary

import android.app.Application
import com.samuelkiszka.virtuallibrary.data.AppContainer
import com.samuelkiszka.virtuallibrary.data.DefaultAppContainer

class VirtualLibraryApplication: Application() {
    lateinit var container: AppContainer
    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer(this)
    }
}