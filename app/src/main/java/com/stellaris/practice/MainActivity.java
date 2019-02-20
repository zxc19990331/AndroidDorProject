package com.stellaris.practice;

import android.net.Uri;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {


    @BindView(R.id.main_button_test)
    Button ButtonTest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.main_button_test,R.id.main_button_test2,R.id.main_button_test3})
    public void onViewClicked(View view) {
        switch (view.getId()){
            case R.id.main_button_test: {
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
            }break;

            case R.id.main_button_test2: {
                Intent intent = new Intent(this, AddPostActivity.class);
                startActivity(intent);
            }break;

            case R.id.main_button_test3:{
                Intent intent = new Intent(this, MainFrameActivity.class);
                startActivity(intent);
            }break;
        }
    }

}
