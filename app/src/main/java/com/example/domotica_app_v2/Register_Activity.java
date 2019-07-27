package com.example.domotica_app_v2;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.domotica_app_v2.Modelos.Edificio;
import com.example.domotica_app_v2.db.SQLiteDbHelper;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.UUID;


public class Register_Activity extends AppCompatActivity {

    SQLiteDbHelper dbHelper;
    SQLiteDatabase db;
    FirebaseDatabase fbd;
    DatabaseReference dbr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //To hide AppBar for fullscreen.
//        ActionBar ab = getSupportActionBar();
//        ab.hide();
        iniciarFireBase();
        dbHelper = new SQLiteDbHelper(this);
        db = dbHelper.getReadableDatabase();



        //Referencias a los inputs
        final EditText fullName_ = (EditText) findViewById(R.id.txtname_reg);
        final EditText user_ = (EditText) findViewById(R.id.txteUser_reg);
        final EditText pass_ = (EditText) findViewById(R.id.txtpass_reg);
        final EditText dni_ = (EditText) findViewById(R.id.txtdni_reg);
        Button regButton = (Button) findViewById(R.id.btn_reg);

        //Al clickear el boton de registro
        regButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                db = dbHelper.getWritableDatabase();

                String fullname = fullName_.getText().toString();
                String user = user_.getText().toString();
                String pass = pass_.getText().toString();
                String dni = dni_.getText().toString();

                //Metodo declarado debajo
                InsertData(fullname, user, pass, dni);
                Toast.makeText(Register_Activity.this, getResources().getString(R.string.re), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Register_Activity.this,Login_Activity.class);
                startActivity(intent);
            }
        });

    }

    private void iniciarFireBase(){
        FirebaseApp.initializeApp(this);
        fbd = FirebaseDatabase.getInstance();
        dbr = fbd.getReference();
    }

    //Inserción en la base de datos
    public void InsertData(String fullName, String user, String pass, String dni ) {

        ContentValues values = new ContentValues();
        values.put("fullName",fullName);
        values.put("user",user);
        values.put("pass",pass);
        values.put("dni",dni);

        long id = db.insert("persona",null,values);

    }
}
