package com.example.domotica_app_v2.Modelos;

import androidx.annotation.NonNull;

public class Accion {

    public static int ID = 1; //PROBLEMA YA QUE SI SE VUELVE A CREAR APP VUELVE A 1 Y ME SOBREESCRIBE

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    private String id;
    private String fecha;
    private String msg;


    public Accion() {
    }

    public Accion(String id, String fecha, String msg) {
        this.fecha = fecha;
        this.msg = msg;
    }

    @NonNull
    @Override
    public String toString() {
        return msg;
    }
}
