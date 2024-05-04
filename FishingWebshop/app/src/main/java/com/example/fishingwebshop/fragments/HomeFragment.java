package com.example.fishingwebshop.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fishingwebshop.R;
import com.example.fishingwebshop.activities.ShowAllActivity;
import com.example.fishingwebshop.adapters.CategoryAdapter;
import com.example.fishingwebshop.adapters.NewProductsAdapter;
import com.example.fishingwebshop.models.CategoryModel;
import com.example.fishingwebshop.models.NewProductsModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends Fragment {


    //progressbar
    LinearLayout linearLayout;
    ProgressDialog progressDialog;

    //Category recycleview
    CategoryAdapter categoryAdapter;
    List<CategoryModel> categoryModelList;

    RecyclerView catRecyclerview, newProductRecyclerview;

    //FireStore
    FirebaseFirestore db;

    //New Product Recycler
    NewProductsAdapter newProductsAdapter;
    List<NewProductsModel> newProductsModelList;



    //empty public const.
    public HomeFragment() {

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root =  inflater.inflate(R.layout.fragment_home, container, false);
        db = FirebaseFirestore.getInstance();

        progressDialog = new ProgressDialog(getActivity());

        catRecyclerview = root.findViewById(R.id.rec_category);
        newProductRecyclerview = root.findViewById(R.id.new_product_rec);


        linearLayout = root.findViewById(R.id.home_layout);
        linearLayout.setVisibility(View.GONE); //LinearLAyout eltuntetese



        //Category
        catRecyclerview.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false));
        categoryModelList = new ArrayList<>();
        categoryAdapter = new CategoryAdapter(getContext(), categoryModelList);
        catRecyclerview.setAdapter(categoryAdapter);

        //progressbar
        progressDialog.setTitle("Horgászshop betöltése");
        progressDialog.setMessage("Teszt message");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        db.collection("Category")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                CategoryModel categoryModel = document.toObject(CategoryModel.class);
                                categoryModelList.add(categoryModel);
                                categoryAdapter.notifyDataSetChanged();
                                linearLayout.setVisibility(View.VISIBLE);
                                progressDialog.dismiss();
                            }
                        } else {
                            Toast.makeText(getActivity(), ""+task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        //New products
       // newProductRecyclerview.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false));
        //newProductRecyclerview.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
        newProductRecyclerview.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        newProductsModelList = new ArrayList<>();
        newProductsAdapter = new NewProductsAdapter(getContext(), newProductsModelList);
        newProductRecyclerview.setAdapter(newProductsAdapter);



        db.collection("NewProducts")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                NewProductsModel newProductsModel = document.toObject(NewProductsModel.class);
                               /* for (int i = 0; i < 9; i++) {*/
                                    newProductsModelList.add(newProductsModel);
                               // }

                                newProductsAdapter.notifyDataSetChanged();
                            }
                        } else {
                            Toast.makeText(getActivity(), ""+task.getException(), Toast.LENGTH_SHORT).show();

                        }
                    }
                });

        return root;
    }
}