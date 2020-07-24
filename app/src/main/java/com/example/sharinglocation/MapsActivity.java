package com.example.sharinglocation;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    LocationManager locationManager;
    LocationManager locationManagerNetwork;
    LocationListener locationListener;
    FirebaseUser user;
    FirebaseDatabase database;
    DatabaseReference reference;
    String currentUserId;
    ArrayList<LatLng> Location;
    Long count;
    Circle circle;

    LinearLayout bottomSheet;
    BottomSheetBehavior bottomSheetBehavior;
    RelativeLayout shopListRelativeLayout;
    @Override
      protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        bottomSheet=(LinearLayout)findViewById(R.id.topLinearLayout);
        shopListRelativeLayout=(RelativeLayout)findViewById(R.id.shopListRelativeLayout);
        bottomSheetBehavior=BottomSheetBehavior.from(shopListRelativeLayout);
        bottomSheet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(bottomSheetBehavior.getState()!=BottomSheetBehavior.STATE_EXPANDED){
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                }
                else
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
        });


        user = FirebaseAuth.getInstance().getCurrentUser();
        currentUserId = user.getUid();
        database = FirebaseDatabase.getInstance();
        reference = database.getReference();
        Location = new ArrayList<>();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
                gettingLocation();
            }
        }
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        for (LatLng newLocation : Location) {
            mMap.addMarker(new MarkerOptions().position(newLocation).title("hgd"));
            Toast.makeText(this, "hurray....................", Toast.LENGTH_LONG).show();
            Log.d("ArrayValuesLocation", String.valueOf(newLocation.latitude));
        }
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationManagerNetwork=(LocationManager)this.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
//                LatLng newLocation = new LatLng(location.getLatitude(),location.getLongitude());
//                mMap.addMarker(new MarkerOptions().position(newLocation).title("You"));
//                mMap.moveCamera(CameraUpdateFactory.newLatLng(newLocation));
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };
        if (Build.VERSION.SDK_INT < 23) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            locationManagerNetwork.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,0,0,locationListener);
            gettingLocation();
        } else {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            } else {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
                locationManagerNetwork.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,0,0,locationListener);
                gettingLocation();

            }

        }
        reference.child("Location").addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                count = snapshot.getChildrenCount();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    userLocation userlocation = dataSnapshot.getValue(userLocation.class);
                    if (userlocation != null) {

                            double userLat, userLong;
                            userLat = Double.parseDouble(userlocation.getLatitude());
                            userLong = Double.parseDouble(userlocation.getLongitude());
                            Location.add(new LatLng(userLat, userLong));
                            LatLng newLatLng = new LatLng(userLat, userLong);
                            mMap.addMarker(new MarkerOptions().position(newLatLng).title("huiu"));
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void gettingLocation() {
        if (user != null) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            Location lastLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if(lastLocation!=null) {
                LatLng newLocation = new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude());
                mMap.addMarker(new MarkerOptions().position(newLocation).title("user"));
                mMap.moveCamera(CameraUpdateFactory.zoomTo(16));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(newLocation));

                circle = mMap.addCircle(new CircleOptions().center(newLocation).radius(1000).strokeWidth(1).strokeColor(Color.RED).fillColor(0x5500ff00));
                double latitude, longitude;
                String markerName, sLat, sLong;
                latitude = lastLocation.getLatitude();
                longitude = lastLocation.getLongitude();
                markerName = currentUserId;
                String localIsShopOwner;

                sLat = Double.toString(latitude);
                sLong = Double.toString(longitude);

                    reference.child("Location").child(currentUserId).setValue("");
                    HashMap<String, String> LocatiomHash = new HashMap<>();
                    LocatiomHash.put("Latitude", sLat);
                    LocatiomHash.put("Longitude", sLong);
                    LocatiomHash.put("markerName", markerName);


                    reference.child("Location").child(currentUserId).setValue(LocatiomHash).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(MapsActivity.this, "Location Updated", Toast.LENGTH_SHORT).show();
                            } else
                                Toast.makeText(MapsActivity.this, "Update Failed", Toast.LENGTH_SHORT).show();
                        }
                    });

            }
        else{
            locationManagerNetwork.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,0,0,locationListener);
                Location lastLocationNetwork= locationManagerNetwork.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                if(lastLocationNetwork!=null) {
                    LatLng newLocation = new LatLng(lastLocationNetwork.getLatitude(), lastLocationNetwork.getLongitude());
                    mMap.addMarker(new MarkerOptions().position(newLocation).title("user"));
                    mMap.moveCamera(CameraUpdateFactory.zoomTo(16));
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(newLocation));

                    circle = mMap.addCircle(new CircleOptions().center(newLocation).radius(1000).strokeWidth(1).strokeColor(Color.RED).fillColor(0x5500ff00));
                    double latitude, longitude;
                    String localIsShopOwner;

                    String markerName, sLat, sLong;
                    latitude = lastLocationNetwork.getLatitude();
                    longitude = lastLocationNetwork.getLongitude();
                    markerName = currentUserId;
                    sLat = Double.toString(latitude);
                    sLong = Double.toString(longitude);

                        reference.child("Location").child(currentUserId).setValue("");

                        HashMap<String, String> LocatiomHash = new HashMap<>();
                        LocatiomHash.put("Latitude", sLat);
                        LocatiomHash.put("Longitude", sLong);
                        LocatiomHash.put("markerName", markerName);

                        reference.child("Location").child(currentUserId).setValue(LocatiomHash).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(MapsActivity.this, "Location Updated", Toast.LENGTH_SHORT).show();
                                } else
                                    Toast.makeText(MapsActivity.this, "Update Failed", Toast.LENGTH_SHORT).show();
                            }
                        });

                }

              // gettingLocation();
        }
        }else {
            Toast.makeText(this, "cant create user ", Toast.LENGTH_LONG).show();
            gotoMainActivity();
        }
    }



    private void gotoMainActivity() {
        Intent gotoMainActivity=new Intent(MapsActivity.this,MainActivity.class);
        startActivity(gotoMainActivity);
    }

}
