package com.zz19u21.sensorrecv

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.zz19u21.sensorrecv.databinding.ActivityMainBinding
import fontsliderbar.FontSliderBar.OnSliderBarChangeListener
import kotlin.properties.Delegates

class MainActivity : BaseActivity() , View.OnClickListener{

    private var textSizef by Delegates.notNull<Float>()
    private var currentIndex by Delegates.notNull<Int>()
    private var isClickable = true
    private var textsize1 = 0f
    private var textsize2 = 0f
    private var textsize3 = 0f
    private var textsize4 = 0f
    private var textsize5 = 0f
    private var textsize6 = 0f
    private var textsize7 = 0f
    private var textsize8 = 0f
    private var textsize9 = 0f
    private var textsize10 = 0f
    private var textsize11 = 0f
    private var textsize12 = 0f

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        binding.card1.setOnClickListener(this)
        binding.card2.setOnClickListener(this)
        binding.card3.setOnClickListener(this)
        binding.topAppBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId){
                R.id.add_bluetooth -> {
                    Toast.makeText(this, "Scan bluetooth", Toast.LENGTH_SHORT).show()
                    true
                }
                else -> false
            }
        }
        initData()
    }

    private fun initData() {
        currentIndex =
            MyApplication.myInstance!!.preferencesHelper!!.getValueInt("currentIndex", 1)
        //        currentIndex = 5;
        Log.d("TAG", "initData lala: $currentIndex")
        textSizef = 1 + currentIndex * 0.1f
        textsize1 = binding.text1.getTextSize() / textSizef
        textsize2 = binding.text2.getTextSize() / textSizef
        textsize3 = binding.text3.getTextSize() / textSizef
        textsize4 = binding.text4.getTextSize() / textSizef
        textsize5 = binding.text5.getTextSize() / textSizef
        textsize6 = binding.text6.getTextSize() / textSizef
        textsize7 = binding.text7.getTextSize() / textSizef
        textsize8 = binding.text8.getTextSize() / textSizef
        textsize9 = binding.text9.getTextSize() / textSizef
        textsize10 = binding.text10.getTextSize() / textSizef
        textsize11 = binding.text11.getTextSize() / textSizef
        textsize12 = binding.text12.getTextSize() / textSizef

        binding.topAppBar.setNavigationOnClickListener{
            MaterialAlertDialogBuilder(this)
                // Add customization options here
                .show()
        }
        binding.fontSliderBar.setTickCount(6)
            .setTickHeight(DisplayUtils.convertDip2Px(this, 15).toFloat()).setBarColor(Color.GRAY)
            .setTextColor(Color.BLACK)
            .setTextPadding(DisplayUtils.convertDip2Px(this, 10))
            .setTextSize(DisplayUtils.convertDip2Px(this, 14))
            .setThumbRadius(DisplayUtils.convertDip2Px(this, 10).toFloat())
            .setThumbColorNormal(Color.GRAY)
            .setThumbColorPressed(Color.GRAY)
            .setOnSliderBarChangeListener(OnSliderBarChangeListener { rangeBar, index ->
                var index = index
                if (index > 5) {
                    return@OnSliderBarChangeListener;
                }
                index = index - 2
                val textSizef = 1 + index * 0.1f
                setTextSize(textSizef)
            }).setThumbIndex(currentIndex).withAnimation(false).applay()

    }

    private fun setTextSize(textSize: Float) {
        //改变当前页面的字体大小
        binding.text1.setTextSize(DisplayUtils.px2sp(this, textsize1 * textSize).toFloat())
        binding.text2.setTextSize(DisplayUtils.px2sp(this, textsize2 * textSize).toFloat())
        binding.text3.setTextSize(DisplayUtils.px2sp(this, textsize3 * textSize).toFloat())
        binding.text4.setTextSize(DisplayUtils.px2sp(this, textsize4 * textSize).toFloat())
        binding.text5.setTextSize(DisplayUtils.px2sp(this, textsize5 * textSize).toFloat())
        binding.text6.setTextSize(DisplayUtils.px2sp(this, textsize6 * textSize).toFloat())
        binding.text7.setTextSize(DisplayUtils.px2sp(this, textsize7 * textSize).toFloat())
        binding.text8.setTextSize(DisplayUtils.px2sp(this, textsize8 * textSize).toFloat())
        binding.text9.setTextSize(DisplayUtils.px2sp(this, textsize9 * textSize).toFloat())
        binding.text10.setTextSize(DisplayUtils.px2sp(this, textsize10 * textSize).toFloat())
        binding.text11.setTextSize(DisplayUtils.px2sp(this, textsize11 * textSize).toFloat())
        binding.text12.setTextSize(DisplayUtils.px2sp(this, textsize12 * textSize).toFloat())
        if (currentIndex != binding.fontSliderBar.getCurrentIndex()) {
            if (isClickable) {
                isClickable = false
                refresh()
            }
        }
    }

    private fun refresh() {
        //存储标尺的下标
        Log.d("tagg", "refresh: " + binding.fontSliderBar.getCurrentIndex())
        MyApplication.myInstance!!.preferencesHelper!!.setValue(
            "currentIndex",
            binding.fontSliderBar.getCurrentIndex()
        )
        //通知主页面重启
        RxBus.getInstance()
            .post(DataActivity::class.java.simpleName, MessageSocket(98, null, null, null))
        RxBus.getInstance()
            .post(DataActivity::class.java.simpleName, MessageSocket(98, null, null, null))
        RxBus.getInstance()
            .post(DeviceActivity::class.java.simpleName, MessageSocket(98, null, null, null))
        RxBus.getInstance()
            .post(DeviceActivity::class.java.simpleName, MessageSocket(98, null, null, null))
        isClickable = true
        //        showMyDialog();
        //2s后关闭  延迟执行任务 重启完主页
//        Handler(Looper.getMainLooper()).postDelayed({ //                hideMyDialog();
//            finish()
//        }, 2000)
    }


    override fun onClick(v: View?) {
        when(v!!.id){
            R.id.card1 -> {
                val intent = Intent(this, DeviceActivity::class.java)
                    if (isClickable) {
                        isClickable = false
                        refresh()
                    }

                startActivity(intent)
            }
            R.id.card2 -> {
                val intent = Intent(this, DeviceActivity::class.java)
                if (currentIndex != binding.fontSliderBar.getCurrentIndex()) {
                    if (isClickable) {
                        isClickable = false
                        refresh()
                    }
                }
                startActivity(intent)
            }
            R.id.card3 -> {
                val intent = Intent(this, DeviceActivity::class.java)
                if (currentIndex != binding.fontSliderBar.getCurrentIndex()) {
                    if (isClickable) {
                        isClickable = false
                        refresh()
                    }
                }
                startActivity(intent)
            }
        }
    }

    override fun rxBusCall(message: MessageSocket?) {
        super.rxBusCall(message)
        when (message!!.id) {
            98 -> {
                //           this.recreate();
                val intent = intent
                overridePendingTransition(0, 0)
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                finish()
                overridePendingTransition(0, 0)
                startActivity(intent)
            }
        }
    }

}