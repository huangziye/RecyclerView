package com.hzy.recyclerview;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btn_pull_to_refresh).setOnClickListener(this);
        findViewById(R.id.btn_slide_delete).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_pull_to_refresh:
                startActivity(new Intent(MainActivity.this, RecyclerActivity.class));
                break;
            case R.id.btn_slide_delete:
                startActivity(new Intent(MainActivity.this, DragActivity.class));
                break;
        }
    }
}
