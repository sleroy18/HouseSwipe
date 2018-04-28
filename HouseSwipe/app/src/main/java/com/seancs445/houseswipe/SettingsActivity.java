package com.seancs445.houseswipe;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.location.Geocoder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;
import java.util.ArrayList;
import java.util.List;

public class SettingsActivity extends AppCompatActivity {
    private static final String USER = "User";
    private static final String LOADER = "loading";
    private static final int YES = 0;
    private static final int NO = 1;

    private Geocoder geocoder;
    private Button save;
    private Button cancel;
    private User user;
    private MaterialBetterSpinner isBuying;
    private MaterialBetterSpinner isRenting;
    private MaterialBetterSpinner beds;
    private MaterialBetterSpinner baths;
    private EditText email;
    private EditText firstName;
    private EditText lastName;
    private EditText minPrice;
    private EditText maxPrice;
    private EditText zip;
    private EditText dist;
    ArrayAdapter<CharSequence> adapterYesNo;
    ArrayAdapter<CharSequence> adapterBedsBaths;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        geocoder = new Geocoder(this);
        user = (User) getIntent().getSerializableExtra(USER);

        adapterYesNo = ArrayAdapter.createFromResource(this,
                R.array.arrayYesNo,
                android.R.layout.simple_dropdown_item_1line);

        adapterBedsBaths = ArrayAdapter.createFromResource(this,
                R.array.arrayNum,
                android.R.layout.simple_dropdown_item_1line);

        email = findViewById(R.id.editTextEmail);
        firstName = findViewById(R.id.editTextFirstName);
        lastName = findViewById(R.id.editTextLastName);
        minPrice = findViewById(R.id.editTextMin);
        maxPrice = findViewById(R.id.editTextMax);
        zip = findViewById(R.id.editTextZip);
        dist = findViewById(R.id.editTextDist);

        isBuying = findViewById(R.id.buyingSpinner);
        isBuying.setAdapter(adapterYesNo);
        isRenting = findViewById(R.id.rentingSpinner);
        isRenting.setAdapter(adapterYesNo);
        beds = findViewById(R.id.bedSpinner);
        beds.setAdapter(adapterBedsBaths);
        baths = findViewById(R.id.bathSpinner);
        baths.setAdapter(adapterBedsBaths);

        save = findViewById(R.id.buttonSave);
        cancel = findViewById(R.id.buttonCancel);

//      Applying current settings to views
        email.setText(user.getEmail());
        firstName.setText(user.getFirstName());
        lastName.setText(user.getLastName());
        minPrice.setText(Double.toString(user.getPriceMin()));
        maxPrice.setText(Double.toString(user.getPriceMax()));
        zip.setText(Integer.toString(user.getZipCode()));
        dist.setText(Integer.toString(user.getDistance()));

//      Setting array adapters is 0 = Yes; 1 = No;
        if(user.isBuying()){
            isBuying.setText("Yes");
        }
        else{
            isBuying.setText("No");
        }
        if(user.isRenting()){
            isRenting.setText("Yes");
        }
        else{
            isRenting.setText("No");
        }
        if(user.getMinBedNumber() != 0){
            beds.setText(String.valueOf(user.getMinBedNumber()));
        }
        if(user.getMinBathNumber() != 0){
            baths.setText(String.valueOf(user.getMinBathNumber()));
        }

//      The position in the array adapter, starting from 0,  is the number of beds/baths - 1

//        beds.change(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
//                // if selected string contains '+' => as in 5+ => then min beds is 5 and logic is 'if beds >=5'
//                if(beds.getText().toString().contains(("+"))){
//                    user.setMinBedNumber(5);
//                }
//                else{
//                    user.setMinBedNumber(Integer.parseInt(beds.getText().toString()));
//                }
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });

        baths.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                if(parent.getItemAtPosition(pos).toString().contains("+")){
                    user.setMinBathNumber(5);
                }
                else{
                    user.setMinBathNumber(Integer.parseInt(parent.getItemAtPosition(pos).toString()));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        isRenting.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                if(parent.getItemAtPosition(pos).toString().equals("Yes")){
                    user.setRenting(true);
                }
                else{
                    user.setRenting(false);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


        isBuying.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                if(parent.getItemAtPosition(pos).toString().equals("Yes")){
                    user.setBuying(true);
                }
                else{
                    user.setBuying(false);
                }
                isBuying.setSelection(0);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseHelper db = new DatabaseHelper(getApplicationContext());
                user.setFirstName((firstName.getText().toString()));
                user.setLastName((lastName.getText().toString()));
                user.setPriceMin(Double.parseDouble(minPrice.getText().toString()));
                user.setPriceMax(Double.parseDouble(maxPrice.getText().toString()));
                user.setZipCode(Integer.parseInt(zip.getText().toString()));
                user.setDistance(Integer.parseInt(dist.getText().toString()));
                if(isBuying.getText().toString().equals("Yes")){
                    user.setBuying(true);
                }
                else{
                    user.setBuying(false);
                }
                if(isRenting.getText().toString().equals("Yes")){
                    user.setRenting(true);
                }
                else{
                    user.setRenting(false);
                }
                if(beds.getText().toString().contains("+")){
                    user.setMinBedNumber(5);
                }
                else{
                    user.setMinBedNumber(Integer.parseInt(beds.getText().toString()));
                }
                if(baths.getText().toString().contains("+")){
                    user.setMinBathNumber(5);
                }
                else{
                    user.setMinBathNumber(Integer.parseInt(baths.getText().toString()));
                }
//                try {
//                    List<Address> addresses = geocoder.getFromLocationName(zip.getText().toString(), 1);
//                    if (addresses != null && !addresses.isEmpty()) {
//                        Address address = addresses.get(0);
//                        user.setLatitude(address.getLatitude());
//                        user.setLongitude(address.getLongitude());
//                    } else {
//                        Toast.makeText(getApplicationContext(), "Could Not Find Zip Code", Toast.LENGTH_SHORT).show();                    }
//                } catch (IOException e) {
//                    //handle exception
//                }
                if(db.UpdateUser(user) == 1){
                    Toast toast = Toast.makeText(getApplicationContext(), "Settings Updated", Toast.LENGTH_LONG);
                    toast.show();
                    Intent intent = new Intent(SettingsActivity.this, LandingPageActivity.class);
                    intent.putExtra(USER, user);
                    startActivity(intent);
                }
                else{
                    Toast toast = Toast.makeText(getApplicationContext(), "Error Updating", Toast.LENGTH_LONG);
                    toast.show();
                }
                db.UpdateUser(user);
            }
        });

        cancel.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LandingPageActivity.class);
                intent.putExtra(USER, user);
                startActivity(intent);
            }
        });
        if(user.isBuying()){
            isBuying.setSelection(0);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_activity_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Navigation nav = new Navigation();
        return nav.Navigate(item, user, getApplicationContext());
    }

//    private void loadFragment(Fragment fragment, int resourceId) {
//        FragmentManager fm = getFragmentManager();
//        FragmentTransaction fragmentTransaction = fm.beginTransaction();
//        fragmentTransaction.replace(resourceId, fragment);
//        fragmentTransaction.commit();
//    }
}
