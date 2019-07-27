package com.example.domotica_app_v2;

import android.app.Service;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.domotica_app_v2.Modelos.Edificio;
import com.example.domotica_app_v2.Modelos.Notificacion;
import com.example.domotica_app_v2.Servicios.Servicio_BGN;
import com.example.domotica_app_v2.db.SQLiteDbHelper;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Notificaciones_Activity extends AppCompatActivity{

    //creo instacios de firebase
    FirebaseDatabase fbd;
    DatabaseReference dbr;

    //donde muestro la lista de notificaciones
    private ListView lista_notificaciones;

    //lista de notificaciones
    private List<Notificacion> listNoti = new ArrayList<Notificacion>();
    ArrayAdapter<Notificacion> arrayAdapterNoti;

    //lista de edificios para realizar las acciones que se detallan en las notificaciones
    private List<Edificio> listEdificios = new ArrayList<Edificio>();

    //variable que adquiere el valor de la notificaciones que selecciono en el LISTVIEW
    Notificacion notiSelect;




    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notificaciones);

        this.setTitle("Bandeja de Entrada");

        lista_notificaciones = (ListView)findViewById(R.id.list_view_notificaciones);


        iniciarFireBase();

        listaNotificaciones();


        //me genera ventana de dialogo donde me muestra la accion a ralizar, me permite aceptar o rechazar la solicitud
        lista_notificaciones.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) { //me completa campos si hago click en item
                notiSelect = (Notificacion) parent.getItemAtPosition(position);
                String accion_noti = notiSelect.getAccion();
                AlertDialog.Builder builder = new AlertDialog.Builder(Notificaciones_Activity.this);
                builder.setMessage(getResources().getString(R.string.not_msg)+accion_noti)
                        .setTitle(getResources().getString(R.string.not))
                        .setPositiveButton(getResources().getString(R.string.accept), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (notiSelect.getAccion()){
                                    case "insert":{ registrar(notiSelect.getEd()); break; }
                                    case "update":{  actualizar(notiSelect.getEd());break; }
                                    case "delete":{ eliminar(notiSelect.getEd());break; }
                                }

                                dbr.child("Notificacion").child(String.valueOf(notiSelect.getId())).removeValue(); //se borra notificacion una vez resuelta
                                respuestaUsuario("La petición fue resuelta exitosamente");

                            }
                        })
                        .setNegativeButton(getResources().getString(R.string.rechaze), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dbr.child("Notificacion").child(String.valueOf(notiSelect.getId())).removeValue();
                                respuestaUsuario("La petición fue rechazada");//si rechazo tambien se borra notificacion
                                dialog.cancel();


                            }
                        }).show();
            }
        });



    }



    private void iniciarFireBase() {
        FirebaseApp.initializeApp(this);
        fbd = FirebaseDatabase.getInstance();
        dbr = fbd.getReference();
    }



    //enlisto notificaciones en listview
    private void listaNotificaciones(){
        dbr.child("Notificacion").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listNoti.clear();
                for (DataSnapshot ds: dataSnapshot.getChildren()){
                    Notificacion noti = ds.getValue(Notificacion.class);
                    listNoti.add(noti);

                    arrayAdapterNoti = new ArrayAdapter<Notificacion>(Notificaciones_Activity.this,android.R.layout.simple_list_item_1,listNoti);
                    lista_notificaciones.setAdapter(arrayAdapterNoti); //muestro en listview la lista
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    //interruptor de servicios backgroud
    public boolean onCreateOptionsMenu(Menu menu){ //manejar el switch para activar servicio
        getMenuInflater().inflate(R.menu.activacion_serviciobgn, menu);

        MenuItem itemSW = menu.findItem(R.id.activador_sw);//mapeo item de switch con id de item que contiene el switch
        itemSW.setActionView(R.layout.switch_layout); //seteo vista en donde esta el switch

        final Switch sw = (Switch)menu.findItem(R.id.activador_sw).getActionView().findViewById(R.id.miSwitch); //mapeo item con componente switch
        sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked){

                    startService(new Intent(Notificaciones_Activity.this, Servicio_BGN.class));
                    Toast.makeText(Notificaciones_Activity.this,getResources().getString(R.string.serv_hab),Toast.LENGTH_SHORT).show();

                }else{
                    stopService(new Intent(Notificaciones_Activity.this, Servicio_BGN.class));
                    Toast.makeText(Notificaciones_Activity.this,getResources().getString(R.string.serv_des),Toast.LENGTH_SHORT).show();

                }
            }
        });

        return super.onCreateOptionsMenu(menu); //efecto para que se oculte y se muestre
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.icon_update) {
            this.recreate();
        }
        return super.onOptionsItemSelected(item);
    }

    // registro edificio con valores que viene en la notificacion
    public void registrar(Edificio ed){

        dbr.child("EdificioPorNotificacion").child(String.valueOf(ed.getId())).setValue(ed);

        Toast.makeText(this, "Registro exitoso reedirigiendo a administrador sensor",Toast.LENGTH_SHORT).show();

        Intent ir = new Intent(Notificaciones_Activity.this, Sensor_Crud_Activity.class);
        startActivity(ir);

    }

    //actualizo edificio con datos de la notificacion
    public void actualizar(final Edificio ed){

        dbr.child("EdificioPorNotificacion").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds: dataSnapshot.getChildren()){
                    Edificio e = ds.getValue(Edificio.class);
                    if(e.getId()== ed.getId()){

                        dbr.child("EdificioPorNotificacion").child(String.valueOf(ed.getId())).setValue(ed); //se setean valores
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        Toast.makeText(this,getResources().getString(R.string.update) ,Toast.LENGTH_SHORT).show();

    }

    //elimino edificio
    public void eliminar(Edificio ed){

        dbr.child("EdificioPorNotificacion").child(String.valueOf(ed.getId())).removeValue();

        Toast.makeText(this, getResources().getString(R.string.delete),Toast.LENGTH_SHORT).show();

    }

    public void respuestaUsuario(String msg){

        Notificacion notificacion = new Notificacion(Notificacion.ID++, msg);

        dbr.child("RespuestaUsuario").child(String.valueOf(notificacion.getId())).setValue(notificacion);

    }

}
