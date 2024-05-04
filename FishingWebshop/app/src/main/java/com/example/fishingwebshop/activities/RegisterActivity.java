package com.example.fishingwebshop.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.fishingwebshop.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {
    private static final String TAG = RegisterActivity.class.getSimpleName();

    EditText email, password;
    private FirebaseAuth auth;
    private FirebaseFirestore firestore;
    private ImageView rotated_image;

    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        rotated_image = findViewById(R.id.rotatingLogo);



        ObjectAnimator rotateAnimator = ObjectAnimator.ofFloat(rotated_image, "rotation", 0f, 360f);
        rotateAnimator.setDuration(8000); // 2 másodperc időtartam
        rotateAnimator.setRepeatCount(ObjectAnimator.INFINITE); // Végtelen ismétlés
        rotateAnimator.setInterpolator(new LinearInterpolator()); // Lineáris interpoláció

        rotateAnimator.start();

         /*  Animation anim = AnimationUtils
                .loadAnimation(this, R.anim.rotate);*/
       // rotated_image.setAnimation(anim);

        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        if (auth.getCurrentUser() != null) {
            startActivity(new Intent(RegisterActivity.this, MainActivity.class));
            finish();
        }


        email = findViewById(R.id.reguseremail);
        password = findViewById(R.id.regpassword);


    }

    public void signup(View view) {
        String userEmail = email.getText().toString();
        String userPassword = password.getText().toString();


        if(TextUtils.isEmpty(userEmail)) {
            Toast.makeText(this, "Add meg az emailed!",Toast.LENGTH_SHORT).show();
            Log.e(TAG, "ures email a regisztraciohoz!");
            return;
        }

        if(TextUtils.isEmpty(userPassword)) {
            Toast.makeText(this, "Add meg a jelszavad!",Toast.LENGTH_SHORT).show();
            Log.e(TAG, "ures jelszo a regisztraciohoz!");
            return;
        }


        if (userPassword.length() < 6) {
            Toast.makeText(this, "Legyen hosszabb a jelszavad (6), mert túl rövid!",Toast.LENGTH_SHORT).show();
            return;
        }


        auth.createUserWithEmailAndPassword(userEmail, userPassword)
                .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(RegisterActivity.this, "Sikeres regisztráció!", Toast.LENGTH_SHORT).show();
                             startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                        } else {
                            Toast.makeText(RegisterActivity.this, "Sikertelen regisztráció!" + task.getException(), Toast.LENGTH_SHORT).show();
                        }

                    }
                }
                );


    }

    public void signin(View view) {
        startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
    }
}