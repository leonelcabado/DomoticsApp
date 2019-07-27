package com.example.domotica_app_v2.Servicios;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import android.media.Image;
import android.os.IBinder;
import android.util.Base64;

import androidx.annotation.DrawableRes;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.example.domotica_app_v2.Modelos.Edificio;
import com.example.domotica_app_v2.Modelos.Notificacion;
import com.example.domotica_app_v2.R;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;


public class Servicio_BGN extends Service {

    FirebaseDatabase fbd;
    DatabaseReference dbr;
    Timer timer = new Timer();


    //generar accion aleatoria
    private Random randomGenerator = new Random();
    ArrayList<String> accion = new ArrayList<>();





    @Override
    public void onCreate() {

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        accion.add("insert");
        //accion.add("update");
        accion.add("delete");

        iniciarFireBase();
        //creo una timer con una tarea a realizar
        TimerTask tt = new TimerTask() {
            @Override
            public void run() {
                int id = 1;
                Notificacion noti = new Notificacion();
                Edificio ed = new Edificio(String.valueOf(Edificio.ID),"Prueba por Notificacion","-34.77336090174149","-58.26757880609536",id++,"null");
                noti.setId(Notificacion.ID++);
                noti.setAccion(ObtenerAccion());
                noti.setMsg("El Usuario: "+ed.getUserID()+" desea realizar la siguiente accion sobre la base de datos: "+noti.getAccion());
                noti.setEd(ed);
                noti.setId_edificio(ed.getId());
                dbr.child("Notificacion").child(String.valueOf(noti.getId())).setValue(noti);
            }
        };

        timer.schedule(tt,0,10000); //ejecuto la tarea cada 10 segundos

        return START_STICKY;
    }

    public String ObtenerAccion()
    {
        int index = randomGenerator.nextInt(accion.size());
        return accion.get(index);
    }

    


    //paro el servicio
    @Override
    public void onDestroy() {
        timer.cancel();
        timer.purge();
    }

    private void iniciarFireBase() {
        FirebaseApp.initializeApp(this);
        fbd = FirebaseDatabase.getInstance();
        dbr = fbd.getReference();
    }


}
