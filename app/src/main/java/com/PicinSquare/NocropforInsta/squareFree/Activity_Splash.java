package com.PicinSquare.NocropforInsta.squareFree;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Activity_Splash extends AppCompatActivity {

    LinearLayout l1, l2;
    Animation uptodown, downupto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);

        l1 = findViewById(R.id.l1);
        l2 = findViewById(R.id.l2);

        uptodown = AnimationUtils.loadAnimation(this, R.anim.uptodown);
        downupto = AnimationUtils.loadAnimation(this, R.anim.downupto);
        l2.setAnimation(downupto);
        l1.setAnimation(uptodown);

        Context context = getApplicationContext();
        PackageManager packageManager = context.getPackageManager();
        String packageName = context.getPackageName();

        String myVersionName = "not available";
        try {
            myVersionName = packageManager.getPackageInfo(packageName, 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        TextView tvVersionName = findViewById(R.id.versiontv);
        tvVersionName.setText("Version : " + myVersionName);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                    startActivity(new Intent(Activity_Splash.this, Activity_Select.class));
                    finish();
            }
        }, 2500);
    }
}
