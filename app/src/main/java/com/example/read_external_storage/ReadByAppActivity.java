package com.example.read_external_storage;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.io.InputStreamReader;
import java.io.Reader;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ReadByAppActivity extends AppCompatActivity {

    private static final int REQUEST_CODE = 100;

    private final Executor executor = Executors.newSingleThreadExecutor();
    private TextView messageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_by_app);

        messageView = findViewById(R.id.message);
        findViewById(R.id.button).setOnClickListener(
            v -> loadContent()
        );
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode != REQUEST_CODE || resultCode != RESULT_OK) {
            return;
        }
        if (data == null || data.getData() == null) {
            return;
        }
        loadContentAsync(data.getData());
    }

    private void loadContentAsync(Uri uri) {
        executor.execute(() -> {
            addMessage("Loading content");
            try (Reader reader = new InputStreamReader(getContentResolver().openInputStream(uri))) {
                StringBuilder builder = new StringBuilder();
                char[] buf = new char[256];
                while (true) {
                    int count = reader.read(buf);
                    if (count < 1) {
                        break;
                    }
                    builder.append(buf, 0, count);
                }
                addMessage("Success");
                addMessage(builder.toString());
            } catch (Throwable th) {
                addMessage(Log.getStackTraceString(th));
            }
        });
    }

    private void loadContent() {
        clearMessage();

        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("*/*");
        String[] mimeTypes = {"image/*", "text/*", "application/*", "audio/*", "video/*"};
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);

        startActivityForResult(intent, REQUEST_CODE);
    }

    private void clearMessage() {
        runOnUiThread(() -> {
            messageView.setText("");
        });
    }

    private void addMessage(String message) {
        runOnUiThread(() -> {
            StringBuilder builder = new StringBuilder();
            if (messageView.getText() != null) {
                builder.append(messageView.getText());
                builder.append("\n");
            }
            builder.append(message);
            builder.append("\n---");
            messageView.setText(builder);
        });
    }
}