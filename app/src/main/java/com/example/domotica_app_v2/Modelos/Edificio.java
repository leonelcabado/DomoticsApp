package com.example.domotica_app_v2.Modelos;

public class Edificio {

    public static int ID = 1; //PROBLEMA YA QUE SI SE VUELVE A CREAR APP VUELVE A 1 Y ME SOBREESCRIBE
    private String img;
    private String id;
    private String locacion;
    private String lat;
    private String lon;
    private int userID;

    public Edificio() {
    }

    public Edificio(String id, String locacion, String lat, String lon, int userID, String img) {
        this.id = id;
        this.locacion = locacion;
        this.lat = lat;
        this.lon = lon;
        this.userID = userID;
        this.img = img;
    }

    public Edificio(String id, String locacion, String lat, String lon, int userID) {
        this.id = id;
        this.locacion = locacion;
        this.lat = lat;
        this.lon = lon;
        this.userID = userID;
        this.img = img;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLocacion() {
        return locacion;
    }

    public void setLocacion(String locacion) {
        this.locacion = locacion;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }


    @Override
    public String toString() {
        return locacion ;
    }

}