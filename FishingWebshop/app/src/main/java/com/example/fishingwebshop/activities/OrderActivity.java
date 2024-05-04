package com.example.fishingwebshop.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.fishingwebshop.R;
import com.example.fishingwebshop.adapters.MyCartAdapter;
import com.example.fishingwebshop.adapters.MyOrderAdapter;
import com.example.fishingwebshop.models.MyCartModel;
import com.example.fishingwebshop.models.MyOrderModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class OrderActivity extends AppCompatActivity {
    private static final String TAG = OrderActivity.class.getSimpleName();
    private static final String CHANNEL_ID = "my_channel";
    Toolbar toolbar;

    FirebaseAuth auth;
    FirebaseFirestore firestore;

    RecyclerView recyclerView;

    MyOrderModel myOrderModel = null;
    List<MyOrderModel> orderModelList;
    MyOrderAdapter myOrderAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();


        toolbar = findViewById(R.id.my_cart_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });




        recyclerView = findViewById(R.id.my_order_rec);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 1));
        orderModelList = new ArrayList<>();
        myOrderAdapter = new MyOrderAdapter(this, orderModelList);
        recyclerView.setAdapter(myOrderAdapter);


        firestore.collection("MyOrder").document(auth.getCurrentUser().getUid())
                .collection("User").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        if (task.isSuccessful()) {
                            for (DocumentSnapshot doc : task.getResult().getDocuments()) {
                                MyOrderModel myOrderModel1 = doc.toObject(MyOrderModel.class);
                                orderModelList.add(myOrderModel1);
                                myOrderAdapter.notifyDataSetChanged();


                            }
                        }

                    }
                });

        List<MyCartModel> list = (ArrayList<MyCartModel>) getIntent().getSerializableExtra("itemList");

        if (list != null && list.size() > 0) {
            for (MyCartModel model : list) {
                final HashMap<String, Object> cartMap = new HashMap<>();


                cartMap.put("productName", model.getProductName());
                cartMap.put("productPrice", model.getProductPrice());
                cartMap.put("currentTime", model.getCurrentTime());
                cartMap.put("currentDate", model.getCurrentDate());
                cartMap.put("totalQuantity", model.getTotalQuantity());
                cartMap.put("totalPrice", model.getTotalPrice());


                firestore.collection("MyOrder").document(auth.getCurrentUser().getUid())
                        .collection("User").add(cartMap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentReference> task) {
                                deleteCartItemsFromFirestore();
                                createNotification();
                               // Toast.makeText(OrderActivity.this, "Sikeres vásárlás!", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(OrderActivity.this, MainActivity.class));
                                finish();
                            }
                        });
            }

        }
    }


    private void createNotification() {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "My Channel";
            String description = "My Channel Description";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);

            notificationManager.createNotificationChannel(channel);
        }

        // ertesites builder
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.checked)
                .setContentTitle("FishingWebshop üzenet")
                .setContentText("Köszönjük a bizalmadat!")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        // ertesites megjelenitese
        notificationManager.notify(1, builder.build());
    }



    private void deleteCartItemsFromFirestore() {
        String currentUserUid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        CollectionReference cartItemsRef = firestore.collection("AddToCart").document(currentUserUid).collection("User");

        cartItemsRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        // összes elem torlese
                        document.getReference().delete();
                        Log.e(TAG, "Sikeres torles!");
                    }
                } else {
                    // Ha valami probléma volt a Firestore-ból való lekérdezéssel
                    Log.d(TAG, "Hiba: ", task.getException());
                }
            }
        });
    }
}