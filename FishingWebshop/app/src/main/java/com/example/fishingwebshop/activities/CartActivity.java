package com.example.fishingwebshop.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.fishingwebshop.R;
import com.example.fishingwebshop.adapters.MyCartAdapter;
import com.example.fishingwebshop.models.MyCartModel;
import com.example.fishingwebshop.models.ShowAllModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CartActivity extends AppCompatActivity {

    private static final String TAG = CartActivity.class.getSimpleName();
    Toolbar toolbar;
    RecyclerView recyclerView;
    List<MyCartModel> cartModelList;
    MyCartAdapter cartAdapter;

    private FirebaseAuth auth;
    private FirebaseFirestore firestore;

    int overAllTotalAmount;
    TextView overAllAmount;



    ImageView cartImg;
    Button buynow;
    Button deleteAll;

    MyCartModel myCartModel = null;

    TextView name;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
       // setContentView(R.layout.my_cart_item);


        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        buynow = findViewById(R.id.buy_now);
        deleteAll = findViewById(R.id.delete_all);


//        cartImg = findViewById(R.id.teszt_background);

        toolbar = findViewById(R.id.my_cart_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //get data from my cart adapter
        LocalBroadcastManager.getInstance(this)
                .registerReceiver(mMessageReceiver, new IntentFilter("MyTotalAmount"));

       // overAllAmount = findViewById(R.id.textView3);
        recyclerView = findViewById(R.id.my_cart_rec);
        //recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setLayoutManager(new GridLayoutManager(this, 1));
        cartModelList = new ArrayList<>();
        cartAdapter = new MyCartAdapter(this, cartModelList);
        recyclerView.setAdapter(cartAdapter);

        if (myCartModel != null) {
            Glide.with(getApplicationContext()).load(myCartModel.getProductImg_url()).into(cartImg);
        }


    

        firestore.collection("AddToCart").document(auth.getCurrentUser().getUid())
                .collection("User").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        if (task.isSuccessful()) {
                            if (!task.getResult().isEmpty()) { //ha nem ures a kosar tartalma akkor mutassa meg a gombot
                                buynow.setVisibility(View.VISIBLE);
                                deleteAll.setVisibility(View.VISIBLE);
                            } else {
                                buynow.setVisibility(View.INVISIBLE);
                                deleteAll.setVisibility(View.INVISIBLE);
                            }
                            for (DocumentSnapshot doc : task.getResult().getDocuments()) {
                                MyCartModel myCartModel = doc.toObject(MyCartModel.class);
                                cartModelList.add(myCartModel);
                                cartAdapter.notifyDataSetChanged();

                            }
                        }

                    }
                });



        buynow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), OrderActivity.class);
                intent.putExtra("itemList", (Serializable) cartModelList);
                startActivity(intent);
                Log.i(TAG, "clicked");
            }
        });

        deleteAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteCartItemsFromFirestore();
                Intent intent = new Intent(v.getContext(), MainActivity.class);
                startActivity(intent);
                Log.i(TAG, "clicked: deleteAll");
            }
        });
    }

    public BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int totalBill = intent.getIntExtra("totalAmount", 0);
        }
    };


    private void deleteCartItemsFromFirestore() {
        String currentUserUid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        CollectionReference cartItemsRef = firestore.collection("AddToCart").document(currentUserUid).collection("User");

        cartItemsRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        // kosar elemeinek torlese
                        document.getReference().delete();
                        Log.e(TAG, "Sikeres torles!");
                    }
                } else {
                    // error eseten
                    Log.d(TAG, "Hiba: ", task.getException());
                }
            }
        });
    }
}