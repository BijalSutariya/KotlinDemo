package rx2demo.example.com.crud

import android.app.Application

import com.facebook.stetho.Stetho

class AppClass : Application() {
    override fun onCreate() {
        super.onCreate()
        Stetho.initializeWithDefaults(this)
    }
}