package com.example.domotica_app_v2.db;


import android.content.ContentValues;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class SQLiteDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "domoticadb";
    private static final int DATABASE_VERSION = 150;


    private static final String CREATE_TABLE_QUERY =
            "Create table persona (_ID_PERSONA integer primary key autoincrement, fullName text, user text, pass text, dni text);";

    private static final String CREATE_TABLE_QUERY_EDIFY =
            "Create table edificio (_ID_EDIFICIO integer primary key autoincrement, location text, direccion_lat text, direccion_long text, userid integer);";

    private static final String CONSULTA3 =
            "Create table sensor (_ID_SENSOR integer primary key autoincrement, codigo integer, descripcion text, valor_umbral real);";


    public SQLiteDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        try {
            sqLiteDatabase.execSQL(CREATE_TABLE_QUERY);
            sqLiteDatabase.execSQL(CREATE_TABLE_QUERY_EDIFY);
            sqLiteDatabase.execSQL(CONSULTA3);

        }
        catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS persona");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS edificio");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS sensor");

        onCreate(sqLiteDatabase);
    }

}



