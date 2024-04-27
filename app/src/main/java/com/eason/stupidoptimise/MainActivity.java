package com.eason.stupidoptimise;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.eason.stupidoptimise.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    // Used to load the 'stupidoptimise' library on application startup.
    static {
        System.loadLibrary("stupidoptimise");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Example of a call to a native method
        TextView tv = binding.sampleText;
        tv.setText(stringFromJNI());
    }

    /**
     * A native method that is implemented by the 'stupidoptimise' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();
}