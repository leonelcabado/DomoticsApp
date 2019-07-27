package com.example.domotica_app_v2.Modelos;

import androidx.annotation.NonNull;

public class Sensor {


    public static int ID = 1;
    private String id;
    private String valor_actual;
    private String tipo;
    private String descripcion;
    private String valor_umbarl_maximo;
    private String valor_umbarl_minimo;
    private String userID;
    private String edificioID;

    public Sensor() {
    }

    public Sensor(String id, String tipo, String valor_actual, String descripcion, String valor_umbarl_maximo, String valor_umbarl_minimo, String userID, String edificioID) {
        this.id = id;
        this.tipo = tipo;
        this.valor_actual = valor_actual;
        this.descripcion = descripcion;
        this.valor_umbarl_maximo = valor_umbarl_maximo;
        this.valor_umbarl_minimo = valor_umbarl_minimo;
        this.userID = userID;
        this.edificioID = edificioID;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getValor_actual() {
        return valor_actual;
    }

    public void setValor_actual(String valor_actual) {
        this.valor_actual = valor_actual;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getValor_umbarl_maximo() {
        return valor_umbarl_maximo;
    }

    public void setValor_umbarl_maximo(String valor_umbarl_maximo) {
        this.valor_umbarl_maximo = valor_umbarl_maximo;
    }

    public String getValor_umbarl_minimo() {
        return valor_umbarl_minimo;
    }

    public void setValor_umbarl_minimo(String valor_umbarl_minimo) {
        this.valor_umbarl_minimo = valor_umbarl_minimo;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getEdificioID() {
        return edificioID;
    }

    public void setEdificioID(String edificioID) {
        this.edificioID = edificioID;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    @NonNull
    @Override
    public String toString() {
        return userID + " / Valor umbral máximo = "+ valor_umbarl_maximo + " / Valor umbral mínimo = "+ valor_umbarl_minimo;
    }
}
