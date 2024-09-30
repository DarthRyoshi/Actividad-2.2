package com.example.actividad22;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    String mUrl = "https://www.rocketchainsaw.com.au/wp-content/uploads/2018/03/Assassins-Creed-Origins-The-Curse-of-the-Pharaohs-feature-2.jpg";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        ExecutorService service = Executors.newSingleThreadExecutor();

        service.execute(new Runnable() {
            @Override
            public void run() {
            Bitmap bitmap = NetworkUtil.fetchImage(mUrl);

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (bitmap != null)
                        imageView.setImageBitmap(bitmap);
                }
            });

            }
        });

    }



}