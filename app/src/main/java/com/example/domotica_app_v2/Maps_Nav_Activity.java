package com.example.domotica_app_v2;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import com.example.domotica_app_v2.Modelos.Edificio;
import com.example.domotica_app_v2.Modelos.Notificacion;
import com.example.domotica_app_v2.db.SQLiteDbHelper;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;

import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class Maps_Nav_Activity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback {


    FirebaseDatabase fbd;
    DatabaseReference dbr;
    private GoogleMap mMap;
    private List<Edificio> listEdificios = new ArrayList<Edificio>();
    Notificaciones_Activity na;
    double lat, lon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps__nav_);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        iniciarFireBase();

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(getResources().getString(R.string.cercanos));

        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                miUbicacion();
                Toast.makeText(Maps_Nav_Activity.this,getResources().getString(R.string.upd_ubi),Toast.LENGTH_SHORT).show();
            }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);





}

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;


        miUbicacion();

    }

    private void actualizarUbicacion(Location location) {
        mMap.clear();
        if (location != null) {
            lat = location.getLatitude();
            lon = location.getLongitude();
            generarMarca(lat, lon);
        }

    }

    LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            actualizarUbicacion(location);
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

    private void miUbicacion() {
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Location loc = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        actualizarUbicacion(loc);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,15000,0,locationListener);


    }

    public void generarMarca(double lat,double lon){

        float zoomlevel = 15;
        LatLng home = new LatLng(lat, lon);
        mMap.addMarker(new MarkerOptions().position(home).title("Central TouchSolutions").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(home,zoomlevel));


        dbr.child("Edificio").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds: dataSnapshot.getChildren()){
                    Edificio e = ds.getValue(Edificio.class);
                    //if (notificacion.getId_edificio() == e.getId()){
                        //if(notificacion.getAccion() == "alerta"){
                            //LatLng pizzeria = new LatLng(Double.parseDouble(e.getLat()), Double.parseDouble(e.getLon()));
                            //mMap.addMarker(new MarkerOptions().position(pizzeria).title(e.getLocacion() + getResources().getString(R.string.atencion)).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
                        //}else{
                            //LatLng pizzeria = new LatLng(Double.parseDouble(e.getLat()), Double.parseDouble(e.getLon()));
                            //mMap.addMarker(new MarkerOptions().position(pizzeria).title(e.getLocacion() + getResources().getString(R.string.atencion)).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));
                        //}
                        //}else{
                        LatLng pizzeria = new LatLng(Double.parseDouble(e.getLat()), Double.parseDouble(e.getLon()));
                        mMap.addMarker(new MarkerOptions().position(pizzeria).title(e.getLocacion() + getResources().getString(R.string.activo)).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
                    //}
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        dbr.child("Notificacion").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds: dataSnapshot.getChildren()){
                    Notificacion n = ds.getValue(Notificacion.class);

                    LatLng pizzeria = new LatLng(Double.parseDouble(n.getEd().getLat()), Double.parseDouble(n.getEd().getLon()));
                    mMap.addMarker(new MarkerOptions().position(pizzeria).title(n.getEd().getLocacion() + getResources().getString(R.string.atencion)).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void iniciarFireBase(){
        FirebaseApp.initializeApp(this);
        fbd = FirebaseDatabase.getInstance();
        dbr = fbd.getReference();
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.maps__nav_, menu);
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

        int id = item.getItemId();

        if (id == R.id.nav_home) {
            this.recreate();

        } else if (id == R.id.nav_edificios) {
            Intent ir = new Intent(Maps_Nav_Activity.this, EdificioListActivity.class);
            startActivity(ir);
        } else if (id == R.id.nav_sensores) {
            Intent ir = new Intent(Maps_Nav_Activity.this, Sensor_Crud_Activity.class);
            startActivity(ir);
        } else if (id == R.id.nav_notificaciones) {
            Intent ir = new Intent(Maps_Nav_Activity.this, Notificaciones_Activity.class);
            startActivity(ir);
        } else if (id == R.id.nav_cerrar) {
            SharedPreferences preferencias = getSharedPreferences("logueado", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferencias.edit();
            editor.remove("logueado");
            editor.commit();
            Intent ir = new Intent(Maps_Nav_Activity.this, Login_Activity.class);
            startActivity(ir);
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return super.onOptionsItemSelected(item);
    }
}
