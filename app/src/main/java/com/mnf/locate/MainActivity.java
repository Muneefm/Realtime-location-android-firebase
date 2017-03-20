package com.mnf.locate;

import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.TextViewCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mnf.locate.UIclass.PaperButton;
import com.mnf.locate.model.ModelLocationItem;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback {

    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    private LocationRequest mLocationRequest;
   static String user_id = "";
    static String user_name = "";
    PaperButton serviceBtn;
    Intent locationService;
    static Context c ;
    ImageView iconSignal;
    private static DatabaseReference mDatabase;
    private GoogleMap mMap;

    private DatabaseReference mDatabseReference;
    private FirebaseDatabase mFirebaseDatabase;
    private ChildEventListener mChildEventListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);*/
        setContentView(R.layout.activity_real_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        c =getApplicationContext();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        ActivityCompat.requestPermissions(this,
                new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                1);
        locationService = new Intent(this, BackgroundService.class);


        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user!=null) {
        if(user.getUid()!=null) {
            user_id = user.getUid();
            user_name  = user.getDisplayName();
            if (isMyServiceRunning(BackgroundService.class)) {
                Log.e("Tag", "btn service running ");
            //    startTransmittingLocation(false);
            } else {
                Log.e("Tag", "btn service not running ");
               startTransmittingLocation(true);
            }

            mFirebaseDatabase = FirebaseDatabase.getInstance();

            mDatabseReference = mFirebaseDatabase.getReference().child("location");

            defineEventListener();

        }
        }else{
            Toast.makeText(getApplicationContext(),"Login first",Toast.LENGTH_LONG).show();
        }


    }
    public void defineEventListener(){

        mDatabseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.e("MainActivity", "onDataChange total = "+dataSnapshot.toString());
                    if(mMap!=null) {
                        mMap.clear();
                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                            // TODO: handle the post
                            Log.e("MainActivity", "onDataChange postData = " + postSnapshot.toString());
                            double lat = postSnapshot.getValue(ModelLocationItem.class).getLat();
                            double longi = postSnapshot.getValue(ModelLocationItem.class).getLongi();
                            String name = postSnapshot.getValue(ModelLocationItem.class).getName();
                            Log.e("MainActivity", "onDataChange datas = lat = " + lat+" lng = "+longi+" name = "+name);
                            LatLng user = new LatLng(lat,longi);
                            //mMap.addMarker(new MarkerOptions().position(user).title(name)).showInfoWindow();
                            mMap.addMarker(new MarkerOptions().position(user).icon(BitmapDescriptorFactory.fromBitmap(getMarkerBitmapFromView(name))));
                          //  moveToCurrentLocation(mMap,user);
                           // mMap.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);
                            mMap.moveCamera(CameraUpdateFactory.newLatLng(user));
                            mMap.setBuildingsEnabled(true);
                            mMap.setTrafficEnabled(true);

                        }
                    }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.e("MainActivity", "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        });
    }

    private void moveToCurrentLocation(GoogleMap map ,LatLng currentLocation)
    {
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation,15));
        // Zoom in, animating the camera.
        map.animateCamera(CameraUpdateFactory.zoomIn());
        // Zoom out to zoom level 10, animating with a duration of 2 seconds.
        map.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.e("Tag", "onMapReady ");

        mMap = googleMap;

        // Add a marker in Sydney and move the camera
     //   LatLng sydney = new LatLng(-34, 151);
     //   mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
      //  mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    private Bitmap getMarkerBitmapFromView(String name) {

        View customMarkerView = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.marker, null);
        TextView tvMarker = (TextView) customMarkerView.findViewById(R.id.marker_name);
        tvMarker.setText(name);
        customMarkerView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        customMarkerView.layout(0, 0, customMarkerView.getMeasuredWidth(), customMarkerView.getMeasuredHeight());
        customMarkerView.buildDrawingCache();
        Bitmap returnedBitmap = Bitmap.createBitmap(customMarkerView.getMeasuredWidth(), customMarkerView.getMeasuredHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(returnedBitmap);
        canvas.drawColor(Color.WHITE, PorterDuff.Mode.SRC_IN);
        Drawable drawable = customMarkerView.getBackground();
        if (drawable != null)
            drawable.draw(canvas);
        customMarkerView.draw(canvas);
        return returnedBitmap;
    }

    public void startTransmittingLocation(boolean key) {

        Log.e("Tag", "startTransmittingLocation ");


        if (key) {

            startService(locationService);
  
        }
        else {
            stopService(locationService);
        }
       /* if(isMyServiceRunning(BackgroundService.class)){
            serviceBtn.setColor(c.getResources().getColor(R.color.red500));
            serviceBtn.setText("Stop");

            iconSignal.setImageResource(R.mipmap.wifion);

        }else{
            serviceBtn.setColor(c.getResources().getColor(R.color.green_500));
            serviceBtn.setText("Start Trip");
            iconSignal.setImageResource(R.mipmap.wifioff);
        }*/
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the task you need to do.

                } else {

                    // permission denied, boo! Disable the functionality that depends on this permission.
                }
                return;
            }
        }
    }




    public static class BackgroundService extends Service implements LocationListener, NavigationView.OnNavigationItemSelectedListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

        GoogleApiClient mGoogleApiClientB;
        private LocationRequest mLocationRequestB;
        int i =0;

        @Override
        public void onLocationChanged(Location location) {
            Log.e("Tag", "Service onLocationChanged   loc =" + location.getLatitude() + " long = " + location.getLongitude());
            mDatabase.child("location").child(user_id).child("longi").setValue(location.getLongitude()).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.e("tag","onFailure   e = "+e.getLocalizedMessage());
                }
            });
            i++;
            //location.getLatitude()
           if(mDatabase.child("location").child(user_id).child("lat").setValue(location.getLatitude()).isSuccessful()){
               Log.e("tag","onSuccess  lat setValue");
           }else{
               Log.e("tag","not onSuccess  lat setValue");

           }

            if(mDatabase.child("location").child(user_id).child("name").setValue(user_name).isSuccessful()){
                Log.e("tag","onSuccess  username setValue");
            }else{
                Log.e("tag","not onSuccess  username setValue");

            }
        }

        @Override
        public void onCreate() {
            mLocationRequestB = LocationRequest.create();
            mLocationRequestB.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            mLocationRequestB.setInterval(3000);
            mLocationRequestB.setFastestInterval(1000);
            if (mGoogleApiClientB == null) {
                mGoogleApiClientB = new GoogleApiClient.Builder(this)
                        .addConnectionCallbacks(this)
                        .addOnConnectionFailedListener(this)
                        .addApi(LocationServices.API)
                        .build();
            }
            mGoogleApiClientB.connect();

            Log.e("tag", "Service onCreate");


        }

        @Nullable
        @Override
        public IBinder onBind(Intent intent) {
            return null;
        }

        @Override
        public void onDestroy() {
            Log.e("Tag","onDestroy");
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClientB,BackgroundService.this);
            mGoogleApiClientB.disconnect();

            super.onDestroy();
        }

        @Override
        public void onConnected(@Nullable Bundle bundle) {
            Log.e("Tag", "service onConnected");
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                Log.e("TAG","permission if ");



                return;
            }else{
                Log.e("TAG","permission else ");
                LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClientB, mLocationRequestB, this);

            }

        }

        @Override
        public void onConnectionSuspended(int i) {
            Log.e("Tag","service onConnectionSuspended");
        }

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            return false;
        }

        @Override
        public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

        }
    }



    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }




}
