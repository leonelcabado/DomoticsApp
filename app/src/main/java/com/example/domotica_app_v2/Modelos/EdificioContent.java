package com.example.domotica_app_v2.Modelos;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.domotica_app_v2.EdificioListActivity;
import com.example.domotica_app_v2.Sensor_Crud_Activity;
import com.example.domotica_app_v2.db.SQLiteDbHelper;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EdificioContent extends AppCompatActivity{


    SQLiteDbHelper dbhelper;
    SQLiteDatabase db;
    FirebaseDatabase fbd;
    DatabaseReference dbr;

    public static final List<Edificio> ITEMS =new ArrayList<Edificio>();

    public static final Map<String, Edificio> ITEM_MAP = new HashMap<String, Edificio>();

    private static ArrayList<Edificio> edificiosDB = new ArrayList<Edificio>();

    public List<Edificio> getEdificiosList (){
        iniciarFireBase();
        ITEMS.clear();
        dbr.child("Edificio").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                edificiosDB.clear();
                for (DataSnapshot ds: dataSnapshot.getChildren()){
                    Edificio e = ds.getValue(Edificio.class);
                    edificiosDB.add(e);
                    addEdificio(e);
                    }
                }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return ITEMS;
    }

    private void iniciarFireBase(){
        FirebaseApp.initializeApp(this);
        fbd = FirebaseDatabase.getInstance();
        dbr = fbd.getReference();
    }

    private static void addEdificio(Edificio item) {
        ITEMS.add(item);
        ITEM_MAP.put(String.valueOf(item.getId()), item);
    }

    private static String makeDetails(int position) {
        StringBuilder builder = new StringBuilder();
        builder.append("Details about Item: ").append(position);
        /*for (int i = 0; i < position; i++) {
            builder.append("\nMore details information here.");
        }*/
        return builder.toString();
    }

    public EdificioContent(){

    }


    /*public ArrayList<Edificio> getEdificios(SQLiteDbHelper dbHelper) {

        SQLiteDatabase db       =   dbHelper.getWritableDatabase();
        String sql              =   "SELECT * FROM edificio";
        Cursor cursor           =   db.rawQuery(sql, null);
        ArrayList<Edificio> edificios = new ArrayList<Edificio>();
        Edificio edificio;


        if(cursor.moveToFirst()){
            while (!cursor.isAfterLast()) {
                int id = cursor.getInt(0);
                String locacion = cursor.getString(1);
                String lat = cursor.getString(2);
                String lon = cursor.getString(3);
                int userID = cursor.getInt(4);

//                id = id + 1;

                edificio = new Edificio(id,locacion,lat,lon,userID);
                edificios.add(edificio);
                cursor.moveToNext();
            }
        }
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
        db.close();
        if(cursor.getCount() == 0){
            return null;
        }




        return edificios;
    }*/


}
