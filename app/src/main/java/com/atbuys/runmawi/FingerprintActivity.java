package com.atbuys.runmawi;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricPrompt;
import androidx.biometric.BiometricPrompt.PromptInfo;
import androidx.core.content.ContextCompat;

import java.util.concurrent.Executor;

public class FingerprintActivity extends AppCompatActivity {
    Executor executor;
    BiometricPrompt biometricPrompt;
    PromptInfo promptInfo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       //setContentView(R.layout.activity_main);

        executor = ContextCompat.getMainExecutor(this);

        biometricPrompt = new BiometricPrompt(this, executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                Intent in = new Intent(getApplicationContext(), HomePageActivitywithFragments.class);
                startActivity(in);
                finish();

            }

            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                Toast.makeText(FingerprintActivity.this,"User Does not have any security", Toast.LENGTH_LONG).show();
                Intent in = new Intent(getApplicationContext(), HomePageActivitywithFragments.class);
                startActivity(in);
                finish();

            }
            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                Toast.makeText(FingerprintActivity.this,"FAILED", Toast.LENGTH_LONG).show();
                /*Intent intent = new Intent(getApplicationContext(), FingerprintActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("EXIT", true);
                startActivity(intent);*/
            }

        });
        promptInfo = new PromptInfo.Builder()
                .setTitle("Touch id required")
                .setDescription("Please place your finger on the sensor to unlock")
                .setConfirmationRequired(true)
                .setDeviceCredentialAllowed(true)
                .build();
        biometricPrompt.authenticate(promptInfo);
    }
}