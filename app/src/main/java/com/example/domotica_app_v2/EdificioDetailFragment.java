package com.example.domotica_app_v2;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;

import com.example.domotica_app_v2.Modelos.Edificio;
import com.example.domotica_app_v2.Modelos.EdificioContent;
import com.example.domotica_app_v2.Modelos.Sensor;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.transition.TransitionManager;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toolbar;

import java.util.ArrayList;
import java.util.List;

/**
 * A fragment representing a single Edificio detail screen.
 * This fragment is either contained in a {@link EdificioListActivity}
 * in two-pane mode (on tablets) or a {@link EdificioDetailActivity}
 * on handsets.
 */
public class EdificioDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";



    private List<BarEntry> entradas = new ArrayList<>();










    /**
     * The dummy content this fragment is presenting.
     */
    //private DummyContent.DummyItem mItem;
    private Edificio mItem;

    FirebaseDatabase fbd;
    DatabaseReference dbr;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */

    BarChart graficaBarras;
    LineChart graficaLineas;

    public EdificioDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        if (getArguments().containsKey(ARG_ITEM_ID)) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            //mItem = DummyContent.ITEM_MAP.get(getArguments().getString(ARG_ITEM_ID));
            mItem = EdificioContent.ITEM_MAP.get(getArguments().getString(ARG_ITEM_ID));
            Activity activity = this.getActivity();
            ImageView img_portada = (ImageView)activity.findViewById(R.id.img_portada);
            CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                appBarLayout.setTitle(getResources().getString(R.string.title_edificio_detail));
                img_portada.setImageBitmap(StringToBitMap());
                
            }
        }
    }

    public Bitmap StringToBitMap(){
        try {
            byte [] encodeByte= Base64.decode(mItem.getImg(),Base64.DEFAULT);
            Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch(Exception e) {
            e.getMessage();
            return null;
        }
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.edificio_detail, container, false);






        // Show the dummy content as text in a TextView.
        if (mItem != null) {
            String id = String.valueOf(mItem.getId());
            String id_user = String.valueOf(mItem.getUserID());
            ((TextView) rootView.findViewById(R.id.edificio_ID)).setText(getResources().getString(R.string.id)+ ":"+id);
            ((TextView) rootView.findViewById(R.id.edificio_detail)).setText("Entidad"+ ": "+mItem.getLocacion());
            ((TextView) rootView.findViewById(R.id.edificio_lat)).setText(getResources().getString(R.string.lat)+": "+mItem.getLat());
            ((TextView) rootView.findViewById(R.id.edificio_lon)).setText(getResources().getString(R.string.longs)+": "+mItem.getLon());
            ((TextView) rootView.findViewById(R.id.edificio_iduser)).setText("Identificador Propietario"+": "+id_user);






            graficaBarras= rootView.findViewById(R.id.graficaActual);

            if(isAdded()) { //error de attach fragment

                dbr = fbd.getInstance().getReference("UES/" + mItem.getUserID() + "/" + mItem.getId()); //SE OBTIENE REFERENCIA A TABLA UES
                dbr.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        entradas.clear();
                        int focal = 0;
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            Sensor s = ds.getValue(Sensor.class);


                            //CREAMOS LISTA CON VALORES DE ENTRADA

                            entradas.add(new BarEntry(focal, Float.parseFloat(s.getValor_actual())));
                            focal++;


                            //CREAMOS LOS DATOS PARA LA GRAFICA
                            BarDataSet datos = new BarDataSet(entradas, getResources().getString(R.string.val_act));


                            BarData data = new BarData(datos);

                            //PONEMOS COLOR A LAS BARRAS
                            datos.setColors(ColorTemplate.COLORFUL_COLORS);

                            //SEPARACION ENTRE LAS BARRAS
                            data.setBarWidth(0.9f);

                            //ETIQUETAS EJE X
                            final List list_x_axis_name = new ArrayList<>();
                            list_x_axis_name.add(getResources().getString(R.string.humedad));
                            list_x_axis_name.add(getResources().getString(R.string.temp));
                            list_x_axis_name.add(getResources().getString(R.string.luz));
                            list_x_axis_name.add(getResources().getString(R.string.carb));

                            XAxis xAxis = graficaBarras.getXAxis();
                            xAxis.setGranularity(1f);
                            xAxis.setCenterAxisLabels(false);
                            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                            xAxis.setLabelRotationAngle(-45);
                            xAxis.setValueFormatter(new IAxisValueFormatter() {
                                public String getFormattedValue(float value, AxisBase axis) {
                                    if (value >= 0) {
                                        if (value <= list_x_axis_name.size() - 1) {
                                            return (String) list_x_axis_name.get((int) value);
                                        }

                                        return "";
                                    }
                                    return "";
                                }
                            });


                            graficaBarras.setData(data);

                            //CENTRADO DE BARRAS
                            graficaBarras.setFitBars(true);

                            //DESCRIPCION
                            graficaBarras.getDescription().setEnabled(false);


                            //ANIMACION
                            graficaBarras.animateY(3500);


                            SetData(30, 60);
                            graficaLineas.animateX(3500);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

            //grafico de multiples lineas historial de valores
            graficaLineas =  rootView.findViewById(R.id.graficaHistorial);

        }

        return rootView;
    }



    public void SetData(int count, int range){

        ArrayList<Entry> yvals1= new ArrayList<>();
        for (int i=0; i< count; i++){
            float val = (float)(Math.random() *60)+200;
            yvals1.add(new Entry(i,val));
        }

        ArrayList<Entry> yvals2= new ArrayList<>();
        for (int i=0; i< count; i++){
            float val = (float)(Math.random() * 60)+150;
            yvals2.add(new Entry(i,val));
        }

        ArrayList<Entry> yvals3= new ArrayList<>();
        for (int i=0; i< count; i++){
            float val = (float)(Math.random() * 60)+100;
            yvals3.add(new Entry(i,val));
        }

        ArrayList<Entry> yvals4= new ArrayList<>();
        for (int i=0; i< count; i++){
            float val = (float)(Math.random()* 60)+50;
            yvals4.add(new Entry(i,val));
        }

        LineDataSet set1, set2, set3, set4;

        set1= new LineDataSet(yvals1, getResources().getString(R.string.humedad));
        set1.setColor(Color.MAGENTA);
        set1.setDrawCircles(false);


        set2= new LineDataSet(yvals2, getResources().getString(R.string.temp));
        set2.setColor(Color.CYAN);
        set2.setDrawCircles(false);

        set3= new LineDataSet(yvals3, getResources().getString(R.string.luz));
        set3.setColor(Color.YELLOW);
        set3.setDrawCircles(false);

        set4= new LineDataSet(yvals4, getResources().getString(R.string.carb));
        set4.setColor(Color.GREEN);
        set4.setDrawCircles(false);

        LineData dt = new LineData(set1, set2, set3, set4);

        //DESCRIPCION
        graficaLineas.getDescription().setEnabled(false);

        graficaLineas.setData(dt);


    }


}
