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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Toast;

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
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    SQLiteDbHelper dbhelper;
    SQLiteDatabase db;
    FirebaseDatabase fbd;
    DatabaseReference dbr;
    private GoogleMap mMap;
    private List<Edificio> listEdificios = new ArrayList<Edificio>();
    Notificacion notificacion;
    double lat, lon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        iniciarFireBase();



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

        miUbicacion();

    }

    private void actualizarUbicacion(Location location) {
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


        /*dbr.child("Edificio").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds: dataSnapshot.getChildren()){
                    Edificio e = ds.getValue(Edificio.class);
                    if (notificacion.getId_edificio() == e.getId()){
                        if(notificacion.getAccion() == "alerta"){
                            LatLng pizzeria = new LatLng(Double.parseDouble(e.getLat()), Double.parseDouble(e.getLon()));
                            mMap.addMarker(new MarkerOptions().position(pizzeria).title(e.getLocacion() + getResources().getString(R.string.atencion)).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
                        }else{
                            LatLng pizzeria = new LatLng(Double.parseDouble(e.getLat()), Double.parseDouble(e.getLon()));
                            mMap.addMarker(new MarkerOptions().position(pizzeria).title(e.getLocacion() + getResources().getString(R.string.atencion)).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));
                        }
                        }else{
                        LatLng pizzeria = new LatLng(Double.parseDouble(e.getLat()), Double.parseDouble(e.getLon()));
                        mMap.addMarker(new MarkerOptions().position(pizzeria).title(e.getLocacion() + getResources().getString(R.string.activo)).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });*/
    }

    private void iniciarFireBase(){
        FirebaseApp.initializeApp(this);
        fbd = FirebaseDatabase.getInstance();
        dbr = fbd.getReference();
    }


    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.masop, menu);
        return true; //efecto para que se oculte y se muestre
    }

    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();



        if(id == R.id.not_usuarios){
            Intent ir = new Intent(MapsActivity.this, Notificaciones_Activity.class);
            startActivity(ir);
        }else if (id == R.id.crud_sensores) {
            Intent ir = new Intent(MapsActivity.this, Sensor_Crud_Activity.class);
            startActivity(ir);
        } else if (id == R.id.cerrar_session){
                SharedPreferences preferencias = getSharedPreferences("logueado", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferencias.edit();
                editor.remove("logueado");
                editor.commit();
                Intent ir = new Intent(MapsActivity.this, Login_Activity.class);
                startActivity(ir);
        }else if (id == R.id.listado_edificios){
            Intent ir = new Intent(MapsActivity.this, EdificioListActivity.class);
            startActivity(ir);
        }else if (id == R.id.icon_update){
            this.recreate();
        }

        return super.onOptionsItemSelected(item); //devuelve resultado booleano del if
    }
}