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
        myInstance = this //初始化
        preferencesHelper = PreferencesHelper(application, "test")
    }

    /**
     *
     * @return 获取字体缩放比例
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

        // 单例模式获取唯一的Application实例
        fun getInstance(): Application {
            return myInstance!!.application
        }
    }
}