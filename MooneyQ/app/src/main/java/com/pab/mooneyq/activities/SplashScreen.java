package com.pab.mooneyq.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.pab.mooneyq.R;
import com.pab.mooneyq.sharedpreferences.PreferencesManager;

public class SplashScreen extends BaseActivity {

    private PreferencesManager pref;
    private final int delay = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        pref = new PreferencesManager(this);
        new Handler(getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                if (pref.getBoolean("pref_is_login"))
                    startActivity(new Intent(SplashScreen.this, MainActivity.class));
                else
                    startActivity(new Intent(SplashScreen.this, StartActivity.class));

                finish();
            }
        }, delay );
    }
}