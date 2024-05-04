package com.example.fishingwebshop.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.DialogInterface;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fishingwebshop.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class MyProfileActivity extends AppCompatActivity {

    private static final String TAG = OrderActivity.class.getSimpleName();

    TextView email;
    Toolbar toolbar;

    FirebaseAuth auth;
    FirebaseFirestore firestore;
    String userID;
    Button resetPassLocal;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);


        ConstraintLayout constraintLayout = findViewById(R.id.gradient_layout);
        AnimationDrawable animationDrawable = (AnimationDrawable) constraintLayout.getBackground();
        animationDrawable.setEnterFadeDuration(100);
        animationDrawable.setExitFadeDuration(3000);
        animationDrawable.start();

        email = findViewById(R.id.email_get);
        resetPassLocal = findViewById(R.id.resetPasswordLocal);


        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        userID = auth.getCurrentUser().getUid();
        user = auth.getCurrentUser();


        toolbar = findViewById(R.id.my_cart_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);





        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        if (user != null) {
            String userEmail = user.getEmail();
            email.setText(userEmail);
        }

        resetPassLocal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText resetPassword = new EditText(v.getContext());
                final AlertDialog.Builder passwordResetDialog = new AlertDialog.Builder(v.getContext());

                passwordResetDialog.setTitle("Biztosan megszeretnéd változtatni a jelszavadat?");
                passwordResetDialog.setMessage("Ha igen adj meg több mint 6 karakterből álló jelszót!");
                passwordResetDialog.setView(resetPassword);

                passwordResetDialog.setPositiveButton("Megváltoztatom", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String newPassword = resetPassword.getText().toString().trim();

                        if (newPassword.length() < 6) {
                            Toast.makeText(MyProfileActivity.this, "A jelszó túl rövid! (6 karakter szükséges)", Toast.LENGTH_SHORT).show();
                        }

                        FirebaseUser user = auth.getCurrentUser();
                        user.updatePassword(newPassword)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(MyProfileActivity.this, "Sikeres jelszóváltoztatás!", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(MyProfileActivity.this, "Sikertelen jelszóváltoztatás!", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });

                    }
                });

                passwordResetDialog.setNegativeButton("Mégsem", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                // AlertDialog megjelenítése
                passwordResetDialog.create().show();


            }
        });


    }
}

