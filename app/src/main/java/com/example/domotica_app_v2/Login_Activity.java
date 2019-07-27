package com.example.domotica_app_v2;


import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.core.app.NotificationCompat;
import androidx.appcompat.app.ActionBar;
import java.util.Locale;

import android.util.DisplayMetrics;
import androidx.appcompat.app.AppCompatActivity;

import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.example.domotica_app_v2.db.SQLiteDbHelper;

public class Login_Activity extends AppCompatActivity {


    SQLiteDbHelper dbhelper;
    SQLiteDatabase db;
    Cursor cursor;
    final static String LOGUEO = "logueado";
    SharedPreferences preferencias;
    SharedPreferences.Editor editor;
    androidx.core.app.NotificationCompat.Builder mBuilder;             //para la notificacion


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        preferencias = getSharedPreferences(LOGUEO, Context.MODE_PRIVATE);
        int logueado = preferencias.getInt(LOGUEO, -1);
        if (logueado > 0) {
            Intent intent = new Intent(Login_Activity.this, Maps_Nav_Activity.class);
            intent.putExtra("userid", logueado);
            startActivity(intent);
        }

        //Referencia usuario, Password.
        final EditText textUser = (EditText) findViewById(R.id.textUser);
        final EditText textPass = (EditText) findViewById(R.id.textPass);
        Button btnlogin = (Button) findViewById(R.id.btnsignin);
        TextView btnreg = (TextView) findViewById(R.id.btnreg);

        //Opening SQLite Pipeline
        dbhelper = new SQLiteDbHelper(this);
        db = dbhelper.getReadableDatabase();


        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String user = textUser.getText().toString();
                String pass = textPass.getText().toString();

                cursor = db.rawQuery("SELECT * FROM persona WHERE user=? AND pass= ?", new String[]{user, pass});
                if (cursor != null) {
                    if (cursor.getCount() > 0) {

                        cursor.moveToFirst();
                        int id = cursor.getInt(0);
                        String fname = cursor.getString(cursor.getColumnIndex("fullName"));
                        String _user = cursor.getString(cursor.getColumnIndex("user"));

                        //Agrego nueva preference
                        editor = preferencias.edit();
                        editor.putInt(LOGUEO, id);
                        editor.commit();

                        Toast.makeText(Login_Activity.this, ":)", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(Login_Activity.this, Maps_Nav_Activity.class);
                        intent.putExtra("fullName", fname);
                        intent.putExtra("userid", id);
                        startActivity(intent);
                        //Se elimina la actividad.
                        finish();


                    } else {

                        View errorLogin = (View) findViewById(R.id.errorLogin);
                        errorLogin.setVisibility(View.VISIBLE);
                    }
                }

            }
        });

        // Intent For Opening RegisterAccountActivity
        btnreg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Login_Activity.this, Register_Activity.class);
                startActivity(intent);
            }
        });


        final View lenguaje = findViewById(R.id.selectLanguage);
        lenguaje.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                registerForContextMenu(lenguaje);

                return false;
            }
        });
    }

    public void setLocale(String lang) {
        Locale myLocale = new Locale(lang);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
        Intent refresh = new Intent(this, Login_Activity.class);
        createNotification();
        startActivity(refresh);
        finish();
    }


    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.lenguage_context_menu, menu);
    }

    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.spanish:
                setLocale("ES");
                break;
            case R.id.italian:
                setLocale("IT");
                break;
            case R.id.english:
                setLocale("EN");
                break;
        }
        return true;
    }

    private void createNotification() {

        NotificationManager mNotifyMgr = (NotificationManager) getApplicationContext().getSystemService(NOTIFICATION_SERVICE);
        int icono = R.drawable.language;
        Intent intent = new Intent(Login_Activity.this, Login_Activity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(Login_Activity.this, 0, intent, 0);
        mBuilder = new NotificationCompat.Builder(getApplicationContext()).
                setContentIntent(pendingIntent)
                .setSmallIcon(icono)
                .setContentTitle(getResources().getString(R.string.languageChange))
                .setContentText(getResources().getString(R.string.languageChange))
                .setVibrate(new long[]{100, 250, 100, 500})
                .setAutoCancel(true);

        mNotifyMgr.notify(1, mBuilder.build());

    }


}