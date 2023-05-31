package com.example.read_external_storage;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.read_by_app).setOnClickListener(
            v -> readByApp()
        );
        findViewById(R.id.read_by_webview).setOnClickListener(
            v -> readByWebView()
        );
    }

    private void readByApp() {
        startActivity(new Intent(this, ReadByAppActivity.class));
    }

    private void readByWebView() {
        startActivity(new Intent(this, ReadByWebViewActivity.class));
    }
}