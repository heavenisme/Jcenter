package com.heaven.fly;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.heaven.annotation.aspect.Share;
import com.heaven.annotation.aspect.TraceTime;

public class MainActivity extends AppCompatActivity {
    @TraceTime
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
