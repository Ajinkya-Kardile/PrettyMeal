package com.ajinkya.prettymeal.activity;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;

import com.ajinkya.prettymeal.R;
import com.ajinkya.prettymeal.activity.businessAccount.BusinessHistoryActivity;
import com.ajinkya.prettymeal.model.HistoryModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class ClientHistoryActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private HistoryRecyclerViewAdapter historyRecyclerViewAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_history);
        Initialize();
        FetchData();
    }



    private void Initialize() {
        toolbar = findViewById(R.id.ClientHistoryToolbar);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
        toolbar.setTitle("History");

        recyclerView = findViewById(R.id.ClientHistoryRecyclerView);
//        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setReverseLayout(true);
        recyclerView.setLayoutManager(linearLayoutManager);

//        historyModelArrayList = new ArrayList<>();
//        historyRecyclerViewAdapter = new HistoryRecyclerViewAdapter(historyModelArrayList,this,"Client");
//        recyclerView.setAdapter(historyRecyclerViewAdapter);
    }

    private void FetchData() {
        String Current_UID = FirebaseAuth.getInstance().getUid();
        DatabaseReference ClientHistoryRef = FirebaseDatabase.getInstance().getReference().child("Client_Application").child("Users").child(Current_UID).child("History");

        ClientHistoryRef.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<HistoryModel> historyModelArrayList = new ArrayList<>();
                if (snapshot.hasChildren()){
                    for (DataSnapshot data : snapshot.getChildren()) {
                        String key = data.getKey();
                        Log.e(TAG, "onDataChange: "+key );
                        assert key != null;
                        if (!key.isEmpty()){
                            String Name = Objects.requireNonNull(snapshot.child(key).child("MessName").getValue()).toString();
                            String TransactionNo = Objects.requireNonNull(snapshot.child(key).child("TransactionNo").getValue()).toString();
                            String DateTime = Objects.requireNonNull(snapshot.child(key).child("DateTime").getValue()).toString();
                            HistoryModel historyModel = new HistoryModel(Name, TransactionNo, DateTime);
                            historyModelArrayList.add(historyModel);
                        }

                    }

                }
                historyRecyclerViewAdapter = new HistoryRecyclerViewAdapter(historyModelArrayList, ClientHistoryActivity.this, "Client");
                recyclerView.setAdapter(historyRecyclerViewAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}