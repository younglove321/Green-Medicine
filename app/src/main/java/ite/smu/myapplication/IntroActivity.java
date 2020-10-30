package ite.smu.myapplication;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class IntroActivity extends AppCompatActivity {
    private CustomAnimationDialog customAnimationDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        setTitle("GREEN MEDICINE");
        initView();

        Handler timer = new Handler();
        timer.postDelayed(new Runnable(){
            public void run(){
                Intent intent = new Intent(IntroActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        }, 1500);
    }

    private void initView(){
        customAnimationDialog = new CustomAnimationDialog(IntroActivity.this);
        Handler timer = new Handler(); //Handler 생성
        timer.postDelayed(new Runnable(){public void run(){ }}, 2000);
        customAnimationDialog.show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        customAnimationDialog.dismiss();
    }
}
