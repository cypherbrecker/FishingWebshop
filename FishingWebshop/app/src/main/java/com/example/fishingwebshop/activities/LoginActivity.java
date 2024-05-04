package com.example.fishingwebshop.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.ObjectAnimator;
import android.content.Intent;
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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = LoginActivity.class.getSimpleName();

    EditText email, password;
    private FirebaseFirestore firestore;
    private FirebaseAuth auth;
    private ImageView rotated_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        rotated_image = findViewById(R.id.rotatingLogoLogin);



        ObjectAnimator rotateAnimator = ObjectAnimator.ofFloat(rotated_image, "rotation", 0f, 360f);
        rotateAnimator.setDuration(8000); // 2 másodperc időtartam
        rotateAnimator.setRepeatCount(ObjectAnimator.INFINITE); // Végtelen ismétlés
        rotateAnimator.setInterpolator(new LinearInterpolator()); // Lineáris interpoláció
        rotateAnimator.start();

        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        email = findViewById(R.id.reguseremail);
        password = findViewById(R.id.regpassword);
    }

    public void signIn(View view) {

        String userEmail = email.getText().toString();
        String userPassword = password.getText().toString();


        if(TextUtils.isEmpty(userEmail)) {
            Toast.makeText(this, "Add meg az emailed!",Toast.LENGTH_SHORT).show();
            Log.e(TAG, "ures email a reghez!");
            return;
        }

        if(TextUtils.isEmpty(userPassword)) {
            Toast.makeText(this, "Add meg a jelszavad!",Toast.LENGTH_SHORT).show();
            Log.e(TAG, "ures password a reghez!");
            return;
        }


        if (userPassword.length() < 6) {
            Toast.makeText(this, "Legyen hosszabb a jelszavad, mert túl rövid!",Toast.LENGTH_SHORT).show();
            return;
        }

        auth.signInWithEmailAndPassword(userEmail, userPassword)
                        .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(LoginActivity.this, "Sikeres bejelentkezés!", Toast.LENGTH_SHORT).show();
                                    String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                    DocumentReference userRef = firestore.collection("Users").document(userId);

                                    userRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                        @Override
                                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                                            if (documentSnapshot.exists()) {
                                                // Ha a dokumentum létezik, kiolvassuk a pénz értékét
                                                long money = documentSnapshot.getLong("money");
                                                double moneyDot = (double) money / 1000;
                                                String formattedMoney = String.format("%.3f", moneyDot);
                                                Log.i(TAG, "Sikeres login: "+ auth.getCurrentUser().getEmail() + "UID: "+ auth.getCurrentUser().getUid() + " penz: " + formattedMoney + "FT");
                                            } else {
                                                // Ha a dokumentum nem létezik, hibaüzenetet jelenítünk meg
                                                Log.e(TAG, "ADATOK nem találhatók");
                                            }
                                        }  });


                                    startActivity(new Intent(LoginActivity.this, MainActivity.class));

                                } else {
                                    Toast.makeText(LoginActivity.this, "Sikertelen bejelentkezés!" + task.getException(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                        );



    }

    public void signUp(View view) {
        startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
    }
}