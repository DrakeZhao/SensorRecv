package com.zz19u21.sensorrecv

import android.app.Application
import com.zz19u21.sensorrecv.PreferencesHelper
import com.zz19u21.sensorrecv.MyApplication

/**
 * Created by zsj on 2018/6/27.
 */
class MyApplication : Application() {
    var preferencesHelper: PreferencesHelper? = null
        private set

    override fun onCreate() {
        super.onCreate()
        myInstance = this //Initiating
        preferencesHelper = PreferencesHelper(application, "test")
    }

    /**
     *
     * @return Get font scaling
     */
    val fontScale: Float
        get() {
            val currentIndex = preferencesHelper!!.getValueInt("currentIndex", 1)
            return 1 + currentIndex * 0.1f
        }
    private val application: Application
        private get() = this

    companion object {
        var myInstance: MyApplication? = null
            private set

        // Singleton pattern to get a unique instance of Application
        fun getInstance(): Application {
            return myInstance!!.application
        }
    }
}