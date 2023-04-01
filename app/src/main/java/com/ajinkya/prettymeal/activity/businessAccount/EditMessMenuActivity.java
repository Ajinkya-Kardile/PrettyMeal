package com.ajinkya.prettymeal.activity.businessAccount;

import static android.content.ContentValues.TAG;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ajinkya.prettymeal.R;
import com.ajinkya.prettymeal.adapter.EditMenuAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class EditMessMenuActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    ArrayList<String> List;
    RecyclerView recyclerView;
    FloatingActionButton floatingActionButton;
    Button UpdateBtn;
    EditMenuAdapter editMenuAdapter;
    private Spinner menuTypeSpinner;
    private String MessType, MenuType;
    private FirebaseAuth auth;
    private FirebaseUser firebaseUser;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference vegMenuRef, NonVegMenuRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_mess_menu);
        Intent intent = getIntent();
        MessType = intent.getStringExtra("MessType");

        Initialize();
        Buttons();
    }


    private void Initialize() {
        recyclerView = findViewById(R.id.RecyclerView);
        UpdateBtn = findViewById(R.id.UpdateBtn);
        floatingActionButton = findViewById(R.id.floatingActionBtn);
        menuTypeSpinner = findViewById(R.id.MenuTypeSpinner);



//        set data to spinner
        menuTypeSpinner.setOnItemSelectedListener(this);
        // Spinner Drop down elements
        java.util.List<String> categories = new ArrayList<String>();
        categories.add("PureVeg");
        if(MessType.equals("Veg-NonVeg")){
            categories.add("Veg-NonVeg");
        }

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        menuTypeSpinner.setAdapter(dataAdapter);







        //firebase ==>
        String Current_Uid = FirebaseAuth.getInstance().getUid();
        Log.e(TAG, "Initialize: Current_Uid: "+Current_Uid );
        assert Current_Uid != null;
        vegMenuRef = FirebaseDatabase.getInstance().getReference().child("Business_Application").child("Users").child(Current_Uid).child("MessDetails").child("VegMenu");
        NonVegMenuRef = FirebaseDatabase.getInstance().getReference().child("Business_Application").child("Users").child(Current_Uid).child("MessDetails").child("NonVegMenu");

    }

    private void Buttons() {
        floatingActionButton.setOnClickListener(View -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(this)
                    .setTitle("Add Item")
                    .setCancelable(false)
                    .setMessage("Please add your menu item...");
            LinearLayout linearLayout = new LinearLayout(this);
            final EditText editText = new EditText(this);

            // write the Mess Menu Item
            editText.setHint("Enter Menu Item");
            editText.setMinEms(16);
            editText.setInputType(InputType.TYPE_TEXT_VARIATION_SHORT_MESSAGE);
            linearLayout.addView(editText);
            linearLayout.setPadding(10, 10, 10, 10);
            builder.setView(linearLayout);


            // Click on Recover and a email will be sent to your registered email id
            builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String Item = editText.getText().toString().trim();


                    if (!Item.isEmpty()) {
                        editMenuAdapter.addItem(Item);
                    } else {
                        new SweetAlertDialog(EditMessMenuActivity.this, SweetAlertDialog.ERROR_TYPE)
                                .setTitleText("Empty Entry")
                                .setContentText("Please provide Menu Item! ")
                                .show();
                        Log.e(TAG, "onClick: Sorry, You Not provided Menu Item");
                    }

                }
            });

            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.create().show();
        });


        UpdateBtn.setOnClickListener(View -> {
            ArrayList<String> UpdatedList = editMenuAdapter.getUpdatedList();
//            Toast.makeText(this, UpdatedList.toString(), Toast.LENGTH_SHORT).show();
            Log.e(TAG, "onCreate: UpdatedList :  " + UpdatedList);

            if (!UpdatedList.isEmpty()){
                StringBuilder Menu= new StringBuilder(UpdatedList.get(0));
                for (int i=1; i<UpdatedList.size();i++){
                    Menu.append("+").append(UpdatedList.get(i));
                }
                Log.e(TAG, "Buttons: Menu: "+Menu );
                if (MenuType.equals("PureVeg")){
                    vegMenuRef.setValue(Menu.toString());
                }else NonVegMenuRef.setValue(Menu.toString());


                Intent intent = new Intent(EditMessMenuActivity.this, BusinessHomePage.class);
                startActivity(intent);
                finish();
            }else Toast.makeText(this, "Please Enter Menu", Toast.LENGTH_SHORT).show();


        });
    }



    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        MenuType = adapterView.getItemAtPosition(i).toString();
        fatchMenuData(MessType);

        // Showing selected spinner item
        Log.e(TAG, "onItemSelected: "+MenuType );
    }



    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }


    private void fatchMenuData(String MenuType) {
        if (MenuType=="PureVeg") {


            vegMenuRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    String vegMenu = snapshot.getValue().toString();
                    ArrayList<String> menuList = new ArrayList<>();

                    if (!vegMenu.equals("NA")) {
                        String[] tokens = vegMenu.split("\\+");
                        for (int i = 0; i < tokens.length; i++) {
                            menuList.add(tokens[i]);
                        }
                    }
                    editMenuAdapter = new EditMenuAdapter(menuList, getApplicationContext());
                    recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                    recyclerView.setAdapter(editMenuAdapter);

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }else{

            NonVegMenuRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    String vegMenu = snapshot.getValue().toString();
                    ArrayList<String> menuList = new ArrayList<>();

                    if (!vegMenu.equals("NA")) {
                        String[] tokens = vegMenu.split("\\+");
                        for (int i = 0; i < tokens.length; i++) {
                            menuList.add(tokens[i]);
                        }
                    }
                    editMenuAdapter = new EditMenuAdapter(menuList, getApplicationContext());
                    recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                    recyclerView.setAdapter(editMenuAdapter);

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }
}
