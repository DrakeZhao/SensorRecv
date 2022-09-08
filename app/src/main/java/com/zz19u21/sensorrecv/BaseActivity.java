package com.zz19u21.sensorrecv;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.util.Objects;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;

/**
 * Adapted from project developed by zsj on 2016/8/4.
 * Activity for getting the base values and notify other activities
 */
public abstract class BaseActivity extends AppCompatActivity {

    public Observable observable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //start an observable, notify other class
        observable = RxBus.getInstance().register(this.getClass().getSimpleName(), MessageSocket.class);
        observable.observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<MessageSocket>() {

            @Override
            public void accept(MessageSocket message) throws Exception {
                rxBusCall(message);
            }
        });
    }

    public void rxBusCall(MessageSocket message) {
    }




    public int getColorById(int resId) {
        return ContextCompat.getColor(this, resId);
    }


    public void goActivity(Class<?> activity) {
        Intent intent = new Intent();
        intent.setClass(getApplicationContext(), activity);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RxBus.getInstance().unregister(this.getClass().getSimpleName(), observable);
    }


    //Rewrite font scaling api<25
    @Override
    public Resources getResources() {
        Resources res =super.getResources();
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.N) {
            Configuration config = res.getConfiguration();
            config.fontScale= Objects.requireNonNull(MyApplication.Companion.getMyInstance()).getFontScale();//1 Set the multiplier of normal font size
            res.updateConfiguration(config,res.getDisplayMetrics());
        }
        return res;
    }
    //ewrite font scaling  api>25
    @Override
    protected void attachBaseContext(Context newBase) {
        if(Build.VERSION.SDK_INT>Build.VERSION_CODES.N){
            final Resources res = newBase.getResources();
            final Configuration config = res.getConfiguration();
//            config.fontScale= MyApplication.getMyInstance().getFontScale();//1 Set the multiplier of normal font size
            config.fontScale= MyApplication.Companion.getMyInstance().getFontScale();//1 Set the multiplier of normal font size
            final Context newContext = newBase.createConfigurationContext(config);
            super.attachBaseContext(newContext);
        }else{
            super.attachBaseContext(newBase);
        }
    }



}
