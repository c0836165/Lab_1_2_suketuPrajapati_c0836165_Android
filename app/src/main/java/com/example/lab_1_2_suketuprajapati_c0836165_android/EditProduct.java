package com.example.lab_1_2_suketuprajapati_c0836165_android;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.provider.SyncStateContract;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.lab_1_2_suketuprajapati_c0836165_android.constants.Constants;
import com.example.lab_1_2_suketuprajapati_c0836165_android.database.ProductDataBase;
import com.example.lab_1_2_suketuprajapati_c0836165_android.database.ProductRepo;
import com.example.lab_1_2_suketuprajapati_c0836165_android.model.Product;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class EditProduct extends AppCompatActivity {

    public static final String TAG = "EditProduct";
    public static final int REQUEST_CODE = 1;
    public static final int UPDATE_INTERVAL = 5000; // 5 secs
    public static final int FASTEST_INTERVAL = 3000; // 3secs
    public static String lat;

    //3rd
    private FusedLocationProviderClient fusedLocationProviderClient;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;

    private List<String> permissionToRequest;
    private List<String> permission = new ArrayList<>();
    private List<String> permissionsRejected = new ArrayList<>();







    EditText PName , P_description , P_price , P_latitude , P_longitude;
    Button button;
    int mProductId;
    Intent intent;
    private ProductDataBase mDb;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_product);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        initViews();
        mDb = ProductDataBase.getInstance(getApplicationContext());
        intent = getIntent();
        if (intent != null && intent.hasExtra(Constants.UPDATE_Person_Id)) {
            button.setText("Update");

            mProductId = intent.getIntExtra(Constants.UPDATE_Person_Id, -1);

            ProductRepo.getInstance().diskIO().execute(new Runnable() {
                @Override
                public void run() {
                    Product product = mDb.productDAO().loadProductById(mProductId);
                    populateUI(product);
                }
            });


        }






    }

    private void populateUI(Product product) {

        if (product == null) {
            return;
        }

        PName.setText(product.getName());
        P_description.setText(product.getDescription());
        P_price.setText(product.getPrice());
        P_longitude.setText(product.getLon());
        P_latitude.setText(product.getLat());
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void initViews() {
        PName = findViewById(R.id.P_name);
        P_description = findViewById(R.id.P_Description);
        P_price = findViewById(R.id.P_price);


        // location

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        permission.add(Manifest.permission.ACCESS_FINE_LOCATION);
        permissionToRequest = permissionToRequest(permission);
        if (permissionToRequest.size() > 0) {
            requestPermissions(permissionToRequest.toArray(new String[permissionToRequest.size()]), REQUEST_CODE);
        }

        P_latitude = findViewById(R.id.P_latitude);



        // location
        P_longitude = findViewById(R.id.P_longitude);
        button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSaveButtonClicked();
            }
        });


    }

    public void onSaveButtonClicked() {
        final Product product = new Product(PName.getText().toString() , P_description.getText().toString() , P_price.getText().toString() , P_latitude.getText().toString() , P_longitude.getText().toString() );

        ProductRepo.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                if (!intent.hasExtra(Constants.UPDATE_Person_Id)) {
                    mDb.productDAO().insertProduct(product);
                } else {
                    product.setId(mProductId);
                    mDb.productDAO().updateProduct(product);
                }
                finish();
            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // location


    @Override
    protected void onPostResume() {
        super.onPostResume();
        int resultCode = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            Dialog errorDialog = GoogleApiAvailability.getInstance().getErrorDialog(this,
                    REQUEST_CODE, REQUEST_CODE, dialog -> {
                        Toast.makeText(this, "The service is not available",
                                Toast.LENGTH_SHORT).show();
                    });
            errorDialog.show();
        } else {
            findLocation();
        }
    }

    private void findLocation(){
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        fusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    Log.d(TAG, "onSuccess" + location.getLatitude() + " " + location.getLongitude());
                }
            }
        });
        startUpdatingLocation();


    }
    @SuppressLint("Issue with Permissions") // COMPLATE ONLY
    private void startUpdatingLocation() {


        locationRequest = locationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(UPDATE_INTERVAL);
        locationRequest.setFastestInterval(FASTEST_INTERVAL);

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                super.onLocationResult(locationResult);

                Location location = locationResult.getLastLocation();
                Log.d(TAG, "onLocationResult: " + location.getLatitude() + " " + location.getLongitude());

                lat = Double.toString(location.getLatitude());
                P_latitude.setText(lat);
                P_longitude.setText(Double.toString(location.getLongitude()));

                String address = "";
                Geocoder gg = new Geocoder(EditProduct.this, Locale.getDefault());
                try {
                    List<Address> addresses = gg.getFromLocation(location.getLatitude(),location.getLongitude(),1);

                    if(address!=null)
                    {
                        Address returnAddress = addresses.get(0);
                        StringBuilder stringBuilderReturnAddress = new StringBuilder("");

                        for (int i= 0; i <= returnAddress.getMaxAddressLineIndex();i++)
                        {
                            stringBuilderReturnAddress.append(returnAddress.getAddressLine(i)).append("\n");
                        }


                    }
                }

                catch(Exception e)
                {
                    Toast.makeText(EditProduct.this, "invalid address", Toast.LENGTH_SHORT).show();
                }


            }
        };
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private List<String> permissionToRequest(List<String> permission)
    {
        ArrayList<String> results = new ArrayList<>();
        for (String permissio : permission) {
            if(!isGranted(permissio)) {
                results.add(permissio);
            }
        }
        return results;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private boolean isGranted(String permission) {
        return checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(REQUEST_CODE == requestCode) {
            for(String permission : permissions){
                if(!isGranted(permission)){
                    permissionsRejected.add(permission);
                }
            }
        }

        if(permissionsRejected.size() > 0 ) {

            if(shouldShowRequestPermissionRationale(permissionsRejected.get(0))){
                new AlertDialog.Builder(this).setMessage("Accessing the location is Mandatory").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        requestPermissions(permissionsRejected.toArray(new String[permissionToRequest.size()]),REQUEST_CODE);
                    }
                }).setNegativeButton("Cancel", null).create().show();
            }
        }
    }

    // location



}
