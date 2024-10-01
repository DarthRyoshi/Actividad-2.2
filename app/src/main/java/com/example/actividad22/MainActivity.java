package com.example.actividad22;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private ImageView mImageView;
    private Button downloadButton;
    private TextView sensorDataTextView;
    private SensorManager sensorManager;
    private Sensor rotationVectorSensor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inicializar vistas
        mImageView = findViewById(R.id.imageView);
        downloadButton = findViewById(R.id.downloadButton);
        sensorDataTextView = findViewById(R.id.sensorDataTextView);

        // Configurar el botón de descarga para iniciar la tarea AsyncTask
        downloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DownloadImageTask().execute("http://IP.com/image.png"); // Reemplaza con una URL válida
            }
        });

        // Inicializar el sensor
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        if (sensorManager != null) {
            rotationVectorSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Registrar el listener del sensor
        if (rotationVectorSensor != null) {
            sensorManager.registerListener(this, rotationVectorSensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Detener el listener del sensor
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ROTATION_VECTOR) {
            // Obtener la orientación del dispositivo
            float[] rotationMatrix = new float[9];
            SensorManager.getRotationMatrixFromVector(rotationMatrix, event.values);

            float[] orientationAngles = new float[3];
            SensorManager.getOrientation(rotationMatrix, orientationAngles);

            // Mostrar los datos del sensor en la interfaz de usuario
            sensorDataTextView.setText(
                    "Azimuth: " + Math.toDegrees(orientationAngles[0]) + "\n" +
                            "Pitch: " + Math.toDegrees(orientationAngles[1]) + "\n" +
                            "Roll: " + Math.toDegrees(orientationAngles[2])
            );
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // No es necesario implementar
    }

    // Método para descargar la imagen desde la URL
    private Bitmap loadImageFromNetwork(String urlString) {
        try {
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            return BitmapFactory.decodeStream(input);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    // AsyncTask para descargar la imagen en segundo plano
    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        @Override
        protected Bitmap doInBackground(String... urls) {
            return loadImageFromNetwork(urls[0]);
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            if (result != null) {
                mImageView.setImageBitmap(result);
            }
        }
    }
}