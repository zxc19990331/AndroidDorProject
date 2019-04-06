package com.stellaris.practice;

import android.net.Uri;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    private Handler handler;
    private Runnable runnable;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        handler = new Handler();
        handler.postDelayed(runnable=new Runnable() {
            @Override
            public void run() {
                Intent intent=new Intent(MainActivity.this,LoginActivity.class);
                startActivity(intent);
                finish();

            }
        },3000);
    }

    @OnClick({R.id.main_skip})
    public void onViewClicked(View view) {
        switch (view.getId()){
            case R.id.main_skip: {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
                if (runnable!=null){
                    handler.removeCallbacks(runnable);
                }
            }break;
        }
    }

}
