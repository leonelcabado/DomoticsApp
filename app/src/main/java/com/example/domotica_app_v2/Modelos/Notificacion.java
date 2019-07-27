package com.example.domotica_app_v2.Modelos;

import androidx.annotation.NonNull;

public class Notificacion {


    public static int ID = 0;
    private int id;
    private String msg;
    private Edificio ed;
    private String accion;


    private String id_edificio;

    public Notificacion() {
    }

    public Notificacion(int id, String msg, Edificio ed, String accion, String id_edificio) {
        this.id = id;
        this.msg = msg;
        this.ed = ed;
        this.accion = accion;
        this.id_edificio = id_edificio;
    }

    public Notificacion(int id, String msg) {
        this.id = id;
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Edificio getEd() {
        return ed;
    }

    public void setEd(Edificio ed) {
        this.ed = ed;
    }

    public String getAccion() {
        return accion;
    }

    public void setAccion(String accion) {
        this.accion = accion;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getId_edificio() {
        return id_edificio;
    }

    public void setId_edificio(String id_edificio) {
        this.id_edificio = id_edificio;
    }

    @NonNull
    @Override
    public String toString() {
        return msg;
    }
}
