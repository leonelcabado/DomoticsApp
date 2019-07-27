package com.example.domotica_app_v2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.domotica_app_v2.Modelos.Accion;
import com.example.domotica_app_v2.Modelos.Edificio;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AccionesAutomaticas_Activity extends AppCompatActivity {

    private List<Accion> listAcciones = new ArrayList<Accion>();
    ArrayAdapter<Accion> arrayAdapterAcciones;
    private ListView lista_acciones;
    FirebaseDatabase fbd;
    DatabaseReference dbr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acciones_automaticas_);
        this.setTitle("Detalle Acciones");
        lista_acciones = (ListView)findViewById(R.id.list_view_acciones);
        iniciarFireBase();
        listaAcciones();
    }

    private void iniciarFireBase(){
        FirebaseApp.initializeApp(this);
        fbd = FirebaseDatabase.getInstance();
        dbr = fbd.getReference();
    }

    private void listaAcciones(){
        dbr.child("Notificacion_Accion").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listAcciones.clear();
                for (DataSnapshot ds: dataSnapshot.getChildren()){
                    Accion a = ds.getValue(Accion.class);
                    listAcciones.add(a);

                    arrayAdapterAcciones = new ArrayAdapter<Accion>(AccionesAutomaticas_Activity.this,android.R.layout.simple_list_item_1,listAcciones);
                    lista_acciones.setAdapter(arrayAdapterAcciones); //muestro en listview la lista
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
