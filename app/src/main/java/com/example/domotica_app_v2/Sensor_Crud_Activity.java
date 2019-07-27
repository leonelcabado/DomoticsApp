package com.example.domotica_app_v2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.domotica_app_v2.Modelos.Sensor;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Sensor_Crud_Activity extends AppCompatActivity {

    private EditText et_tipo, et_descripcion, et_umbral_max, et_umbral_min;
    private ListView lista_sensores;

    private List<Sensor> listSensores = new ArrayList<Sensor>();
    ArrayAdapter<Sensor> arrayAdapterSensor;

    FirebaseDatabase fbd;
    DatabaseReference dbr;

    Sensor sensorSelect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensores);

        this.setTitle(getResources().getString(R.string.adm_sens));


        et_tipo = (EditText)findViewById(R.id.txt_tipo);
        et_descripcion = (EditText)findViewById(R.id.txt_descripcion);
        et_umbral_max = (EditText)findViewById(R.id.txt_umbral_maximo);
        et_umbral_min = (EditText)findViewById(R.id.txt_umbral_minimo);
        lista_sensores = (ListView)findViewById(R.id.list_view_sensores);


        iniciarFireBase();
        listaSensores();

        lista_sensores.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) { //me completa campos si hago click en item
                sensorSelect = (Sensor)parent.getItemAtPosition(position);
                et_tipo.setText(sensorSelect.getTipo());
                et_descripcion.setText(sensorSelect.getDescripcion());
                et_umbral_max.setText(sensorSelect.getValor_umbarl_maximo());
                et_umbral_min.setText(sensorSelect.getValor_umbarl_minimo());
            }
        });
    }

    private void listaSensores(){
        dbr.child("Sensor").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listSensores.clear();
                for (DataSnapshot ds: dataSnapshot.getChildren()){
                    Sensor s = ds.getValue(Sensor.class);
                    listSensores.add(s);

                    arrayAdapterSensor = new ArrayAdapter<Sensor>(Sensor_Crud_Activity.this,android.R.layout.simple_list_item_1,listSensores);
                    lista_sensores.setAdapter(arrayAdapterSensor);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void iniciarFireBase(){
        FirebaseApp.initializeApp(this);
        fbd = FirebaseDatabase.getInstance();
        dbr = fbd.getReference();
    }

    private void validaciones(){
        String tipo = et_tipo.getText().toString();
        String desc = et_descripcion.getText().toString();
        String umbral_max = et_umbral_max.getText().toString();
        String umbral_min = et_umbral_min.getText().toString();

        if(tipo.equals("")){et_tipo.setError(getResources().getString(R.string.req));
            et_tipo.requestFocus();}
        else if(desc.equals("")){et_descripcion.setError(getResources().getString(R.string.req));et_descripcion.requestFocus();}
        else if(umbral_max.equals("")){et_umbral_max.setError(getResources().getString(R.string.req));et_umbral_max.requestFocus();}
        else if(umbral_min.equals("")){et_umbral_min.setError(getResources().getString(R.string.req));et_umbral_min.requestFocus();}
    }


    public void registrar(){

        String tipo = et_tipo.getText().toString();
        String desc = et_descripcion.getText().toString();
        String umbral_max = et_umbral_max.getText().toString();
        String umbral_min = et_umbral_min.getText().toString();

        if (tipo.isEmpty() || desc.isEmpty() || umbral_max.isEmpty() || umbral_min.isEmpty()) {

            validaciones();
        }else{
            Sensor sensor = new Sensor();
            sensor.setId(String.valueOf(sensor.ID++));
            sensor.setTipo(tipo);
            sensor.setDescripcion(desc);
            sensor.setValor_umbarl_maximo(umbral_max);
            sensor.setValor_umbarl_minimo(umbral_min);
            sensor.setEdificioID("null");
            sensor.setUserID("null");

            dbr.child("Sensor").child(sensor.getId()).setValue(sensor);

            et_tipo.setText("");
            et_descripcion.setText("");
            et_umbral_max.setText("");
            et_umbral_min.setText("");

            Toast.makeText(this, getResources().getString(R.string.reg_sensor),Toast.LENGTH_LONG).show();

        }
    }

    public void actualizar(){ //quite View v

        String tipo = et_tipo.getText().toString();
        String desc = et_descripcion.getText().toString();
        String umbral_max = et_umbral_max.getText().toString();
        String umbral_min = et_umbral_min.getText().toString();

        if (tipo.isEmpty() || desc.isEmpty() || umbral_max.isEmpty() || umbral_min.isEmpty()) {

            validaciones();
        }else{
            Sensor sensor = new Sensor();
            sensor.setId(sensorSelect.getId());
            sensor.setTipo(tipo.trim());
            sensor.setDescripcion(desc.trim());
            sensor.setValor_umbarl_maximo(umbral_max.trim());
            sensor.setValor_umbarl_minimo(umbral_min.trim());
            sensor.setEdificioID(null);
            sensor.setUserID(null);

            dbr.child("Sensor").child(sensor.getId()).setValue(sensor);

            et_tipo.setText("");
            et_descripcion.setText("");
            et_umbral_max.setText("");
            et_umbral_min.setText("");

            Toast.makeText(this, getResources().getString(R.string.sensor_modify),Toast.LENGTH_LONG).show();

        }
    }

    public void eliminar(){ //quite View v

            Sensor sensor = new Sensor();
            sensor.setId(sensorSelect.getId());

            dbr.child("Sensor").child(sensor.getId()).removeValue();

            arrayAdapterSensor.notifyDataSetChanged();


            et_tipo.setText("");
            et_descripcion.setText("");
            et_umbral_max.setText("");
            et_umbral_min.setText("");

            Toast.makeText(this, getResources().getString(R.string.sensor_delete),Toast.LENGTH_LONG).show();

    }

    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_crud, menu);
        return super.onCreateOptionsMenu(menu); //efecto para que se oculte y se muestre
    }

    public boolean onOptionsItemSelected(MenuItem item){

        switch (item.getItemId()){
            case R.id.icon_add:{ registrar(); break; }
            case R.id.icon_save:{ actualizar(); break; }
            case R.id.icon_delete:{ eliminar(); break; }
        }

        return super.onOptionsItemSelected(item); //devuelve resultado booleano del if
    }


}
