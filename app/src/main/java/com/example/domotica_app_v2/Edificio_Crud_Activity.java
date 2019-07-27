package com.example.domotica_app_v2;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Layout;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.domotica_app_v2.Modelos.Edificio;
import com.example.domotica_app_v2.Modelos.Sensor;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Edificio_Crud_Activity extends AppCompatActivity {

    private EditText et_locacion, et_lat, et_long, et_userID;
    private ListView lista_edificios;

    ImageButton btn_camara;
    ImageView previa;
    public Bitmap imagen;
    static final int REQUEST_IMAGE_CAPTURE = 1;


    private List<Edificio> listEdificios = new ArrayList<Edificio>();
    ArrayAdapter<Edificio> arrayAdapterEdificio;

    FirebaseDatabase fbd;
    DatabaseReference dbr;

    Edificio edificioSelect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_edificios);

        this.setTitle("Administrar Edificio");

        et_locacion = (EditText)findViewById(R.id.txt_locacion);
        et_lat = (EditText)findViewById(R.id.txt_lat);
        et_long = (EditText)findViewById(R.id.txt_long);
        et_userID = (EditText)findViewById(R.id.txt_userID);
        lista_edificios = (ListView)findViewById(R.id.list_view_edificios);
        btn_camara = (ImageButton) findViewById(R.id.btn_camara);
        previa = (ImageView) findViewById(R.id.preview);

        iniciarFireBase();
        listaEdificios();


        btn_camara.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DispararCamara();
            }
        });


        lista_edificios.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) { //me completa campos si hago click en item
                edificioSelect = (Edificio) parent.getItemAtPosition(position);
                et_locacion.setText(edificioSelect.getLocacion());
                et_lat.setText(edificioSelect.getLat());
                et_long.setText(edificioSelect.getLon());
                et_userID.setText(String.valueOf(edificioSelect.getUserID()));
                previa.setImageBitmap(StringToBitMap(edificioSelect.getImg()));
            }
        });
    }

    private void listaEdificios(){
        dbr.child("Edificio").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listEdificios.clear();
                for (DataSnapshot ds: dataSnapshot.getChildren()){
                    Edificio e = ds.getValue(Edificio.class);
                    listEdificios.add(e);

                    arrayAdapterEdificio = new ArrayAdapter<Edificio>(Edificio_Crud_Activity.this,android.R.layout.simple_list_item_1,listEdificios);
                    lista_edificios.setAdapter(arrayAdapterEdificio); //muestro en listview la lista
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
        String locacion = et_locacion.getText().toString();
        String lat = et_lat.getText().toString();
        String lon = et_long.getText().toString();
        String userID = et_userID.getText().toString();

        if(locacion.equals("")){et_locacion.setError("Requerido");
        et_locacion.requestFocus();}
        else if(lat.equals("")){et_lat.setError("Requerido");et_lat.requestFocus();}
        else if(lon.equals("")){et_long.setError("Requerido");et_long.requestFocus();}
        else if(userID.equals("")){et_userID.setError("Requerido");et_userID.requestFocus();}
    }

    public void registrar(){

        String locacion = et_locacion.getText().toString();
        String lat = et_lat.getText().toString();
        String lon = et_long.getText().toString();
        String userID = et_userID.getText().toString();

        if (locacion.isEmpty() || lat.isEmpty() || lon.isEmpty() || userID.isEmpty()) {

            validaciones();
        }else{
            Edificio edificio = new Edificio();
            edificio.setId(String.valueOf(edificio.ID++));
            edificio.setLocacion(locacion);
            edificio.setLat(lat);
            edificio.setLon(lon);
            edificio.setUserID(Integer.parseInt(userID));
            edificio.setImg( BitMapToString(imagen));


            dbr.child("Edificio").child(String.valueOf(edificio.getId())).setValue(edificio);

            Sensor sensor = new Sensor();
            sensor.setId(String.valueOf(sensor.ID++));
            sensor.setTipo("temperatura");
            sensor.setValor_actual("0.00");
            sensor.setDescripcion("null");
            sensor.setValor_umbarl_maximo("null");
            sensor.setValor_umbarl_minimo("null");
            sensor.setEdificioID(edificio.getId());
            sensor.setUserID(userID);

            dbr.child("Sensor").child(sensor.getId()).setValue(sensor);
            dbr.child("UES/"+userID+"/"+edificio.getId()+"/"+sensor.getId()).setValue(sensor);

            Sensor sensor1 = new Sensor();
            sensor1.setId(String.valueOf(sensor1.ID++));
            sensor1.setTipo("humedad");
            sensor1.setValor_actual("0.00");
            sensor1.setDescripcion("null");
            sensor1.setValor_umbarl_maximo("null");
            sensor1.setValor_umbarl_minimo("null");
            sensor1.setEdificioID((edificio.getId()));
            sensor1.setUserID(userID);

            dbr.child("Sensor").child(sensor1.getId()).setValue(sensor1);
            dbr.child("UES/"+userID+"/"+edificio.getId()+"/"+sensor1.getId()).setValue(sensor1);

            Sensor sensor2 = new Sensor();
            sensor2.setId(String.valueOf(sensor2.ID++));
            sensor2.setTipo("iluminación");
            sensor2.setValor_actual("0.00");
            sensor2.setDescripcion("null");
            sensor2.setValor_umbarl_maximo("null");
            sensor2.setValor_umbarl_minimo("null");
            sensor2.setEdificioID(edificio.getId());
            sensor2.setUserID(userID);

            dbr.child("Sensor").child(sensor2.getId()).setValue(sensor2);
            dbr.child("UES/"+userID+"/"+edificio.getId()+"/"+sensor2.getId()).setValue(sensor2);

            Sensor sensor3 = new Sensor();
            sensor3.setId(String.valueOf(sensor3.ID++));
            sensor3.setTipo("Carbono (gas)");
            sensor3.setValor_actual("0.00");
            sensor3.setDescripcion("null");
            sensor3.setValor_umbarl_maximo("null");
            sensor3.setValor_umbarl_minimo("null");
            sensor3.setEdificioID(edificio.getId());
            sensor3.setUserID(userID);

            dbr.child("Sensor").child(sensor3.getId()).setValue(sensor3);
            dbr.child("UES/"+userID+"/"+edificio.getId()+"/"+sensor3.getId()).setValue(sensor3);

            //cuando se migre a firebase sera removida esta parte de sqlite


            et_locacion.setText("");
            et_lat.setText("");
            et_long.setText("");
            et_userID.setText("");

            Toast.makeText(this, getResources().getString(R.string.succefully),Toast.LENGTH_SHORT).show();

        }

        previa.setImageResource(0);

    }

    public void actualizar(){ //quite View v

        String locacion = et_locacion.getText().toString();
        String lat = et_lat.getText().toString();
        String lon = et_long.getText().toString();
        String userID = et_userID.getText().toString();

        if (locacion.isEmpty() || lat.isEmpty() || lon.isEmpty() || userID.isEmpty()) {
            validaciones();
        }else{
            Edificio edificio = new Edificio();
            edificio.setId(edificioSelect.getId());
            edificio.setLocacion(locacion.trim());
            edificio.setLat(lat.trim());
            edificio.setLon(lon.trim());
            edificio.setUserID(Integer.parseInt(userID.trim()));
            edificio.setImg(BitMapToString(imagen));

            dbr.child("Edificio").child(String.valueOf(edificio.getId())).setValue(edificio);

            //cuando se migre a firebase sera removida esta parte de sqlite



        et_locacion.setText("");
            et_lat.setText("");
            et_long.setText("");
            et_userID.setText("");

            Toast.makeText(this,getResources().getString(R.string.update) ,Toast.LENGTH_SHORT).show();

        }

        previa.setImageResource(0);

    }

    public void eliminar(){ //quite View v

        Edificio edificio = new Edificio();
        edificio.setId(edificioSelect.getId());

        dbr.child("Edificio").child(String.valueOf(edificio.getId())).removeValue();

        arrayAdapterEdificio.notifyDataSetChanged();


        et_locacion.setText("");
        et_lat.setText("");
        et_long.setText("");
        et_userID.setText("");


        Toast.makeText(this, getResources().getString(R.string.delete),Toast.LENGTH_SHORT).show();

        previa.setImageResource(0);

    }

    private void DispararCamara() {                                                                 //dispara la foto
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {                //toma la foto y la pega en la vista previa
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            imagen = imageBitmap;
            previa.setImageBitmap(imageBitmap);




        }
    }

    @Nullable
    public String BitMapToString(Bitmap bitmap){
        ByteArrayOutputStream baos =new  ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100, baos);
        byte [] b=baos.toByteArray();
        String img= Base64.encodeToString(b, Base64.DEFAULT);
        return img;
    }


    public Bitmap StringToBitMap(String encodedString){
        try {
            byte [] encodeByte=Base64.decode(encodedString,Base64.DEFAULT);
            Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch(Exception e) {
            e.getMessage();
            return null;
        }
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

