package com.example.domotica_app_v2;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import com.example.domotica_app_v2.Modelos.Edificio;
import com.example.domotica_app_v2.Modelos.EdificioContent;
import com.example.domotica_app_v2.db.SQLiteDbHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.List;

/**
 * An activity representing a list of Edificios. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link EdificioDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class EdificioListActivity extends AppCompatActivity {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;
    SQLiteDbHelper dbHelper;
    SQLiteDatabase db;
    Cursor cursor;
    FirebaseDatabase fbd;
    DatabaseReference dbr;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edificio_list);


        // simple_list_item_activated_1 sólo es soportado a partir de API 11
        // lo cambio por simple_list_item_1

        dbHelper = new SQLiteDbHelper(this);
        iniciarFireBase();


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_listItem);
        setSupportActionBar(toolbar);
        this.setTitle(getResources().getString(R.string.vig));

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent crud_edificio = new Intent(EdificioListActivity.this, Edificio_Crud_Activity.class);
                startActivity(crud_edificio);
            }
        });

        if (findViewById(R.id.edificio_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }

        View recyclerView = findViewById(R.id.edificio_list);
        assert recyclerView != null;
        setupRecyclerView((RecyclerView) recyclerView);

    }


    private void iniciarFireBase(){
        FirebaseApp.initializeApp(this);
        fbd = FirebaseDatabase.getInstance();
        dbr = fbd.getReference();
    }

    private void setupRecyclerView(@NonNull final RecyclerView recyclerView) {


        EdificioContent ec = new EdificioContent();
        final List <Edificio> edificiosDB = new ArrayList<Edificio>();
        List<Edificio> edificios = new ArrayList<Edificio>();

        edificios = ec.getEdificiosList();

        if (edificios != null){

            dbr.child("Edificio").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    edificiosDB.clear();
                    for (DataSnapshot ds: dataSnapshot.getChildren()){
                        Edificio e = ds.getValue(Edificio.class);
                        edificiosDB.add(e);
                        recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(edificiosDB));

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }else{
            recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(ec.ITEMS));
        }





    }



    public class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        //private final List<DummyContent.DummyItem> mValues;
        private final List<Edificio> mValues;

        public SimpleItemRecyclerViewAdapter(List<Edificio> items) {
            mValues = items;
        }




        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) { //enlaza adaptador con vista edificio_list_content (contenido de items)
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.edificio_list_content, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) { //comunicacion entre adaptador y clase viewHolder
            holder.mItem = mValues.get(position);
            holder.mIdView.setText(getResources().getString(R.string.iden)+":"+String.valueOf(mValues.get(position).getId()));
            holder.mIdUserView.setText(getResources().getString(R.string.iden_usu)+":"+String.valueOf(mValues.get(position).getUserID()));
            holder.mContentView.setText(" "+mValues.get(position).getLocacion());
            //holder.itemView.setTag(mValues.get(position));
            //holder.itemView.setOnClickListener(mOnClickListener);


            byte [] encodeByte= Base64.decode(mValues.get(position).getImg(),Base64.DEFAULT);
            Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            RoundedBitmapDrawable roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(getResources(), bitmap);
            final float roundPx = (float) bitmap.getWidth() * 0.06f;
            roundedBitmapDrawable.setCornerRadius(roundPx);

            holder.mImageView.setImageBitmap(bitmap);


            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mTwoPane) {
                        Bundle arguments = new Bundle();
                        arguments.putString(EdificioDetailFragment.ARG_ITEM_ID, String.valueOf(holder.mItem.getId()));
                        EdificioDetailFragment fragment = new EdificioDetailFragment();
                        fragment.setArguments(arguments);
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.edificio_detail_container, fragment)
                                .commit();
                    } else {
                        Context context = v.getContext();
                        Intent intent = new Intent(context, EdificioDetailActivity.class);
                        intent.putExtra(EdificioDetailFragment.ARG_ITEM_ID, String.valueOf(holder.mItem.getId()));

                        context.startActivity(intent);
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            //public final TextView mIdView;
            public final TextView mContentView;
            public Edificio mItem;
            public final View mView;
            public final ImageView mImageView;
            public final TextView mIdView;
            public final TextView mIdUserView;


            public ViewHolder(View view) {
                super(view);
                mView = view;
                //mIdView = (TextView) view.findViewById(R.id.id_text);
                mContentView = (TextView) view.findViewById(R.id.content);
                mIdView = (TextView) view.findViewById(R.id.subitem);
                mIdUserView = (TextView) view.findViewById(R.id.subitem2);
                mImageView = (ImageView) view.findViewById(R.id.iv_edificio);
            }
            public String toString() {
               //return super.toString() + " '" + mContentView.getText() + "'";
                return null;

            }
        }
    }
}
