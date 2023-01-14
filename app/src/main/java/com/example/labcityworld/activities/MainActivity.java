package com.example.labcityworld.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.labcityworld.R;
import com.example.labcityworld.adapters.CityAdapter;
import com.example.labcityworld.models.City;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity implements RealmChangeListener<RealmResults<City>> {

    private FloatingActionButton fab;
    private Realm realm;

    private RealmResults<City> cities;
    private CityAdapter adapter;

    private RecyclerView recycler;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        realm = Realm.getDefaultInstance();
        cities = realm.where(City.class).findAll();
        cities.addChangeListener(this);

        recycler = (RecyclerView) findViewById(R.id.recyclerView);
        recycler.setHasFixedSize(true);
        recycler.setItemAnimator(new DefaultItemAnimator());
        mLayoutManager = new LinearLayoutManager(this);
        recycler.setLayoutManager(mLayoutManager);

        fab = (FloatingActionButton) findViewById(R.id.fabAddCity);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddEditCityActivity.class);
                startActivity(intent);
            }
        });

        setHideShowFab();

        adapter = new CityAdapter(cities, R.layout.city_item, new CityAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(City city, int position) {
                Intent intent = new Intent(MainActivity.this, AddEditCityActivity.class);
                intent.putExtra("id", city.getId());
                startActivity(intent);
            }
        }, new CityAdapter.OnButtonClickListener() {
            @Override
            public void onButtonClick(City city, int position) {
                showAlertForRemovingCity("Delete City", "Are you sure you want to delete " + city.getName() + "?", position);
            }
        });

        recycler.setAdapter(adapter);
        cities.addChangeListener(this);
    }

    private void setHideShowFab() {
        recycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0)
                    fab.hide();
                else if (dy < 0)
                    fab.show();
            }
        });
    }

    private void showAlertForRemovingCity(String title, String message, final int position) {
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("Remove", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int whichButton) {
                        deleteCity(position);
                        Toast.makeText(MainActivity.this, "It has been deleted successfully", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", null).show();
    }

    private void deleteCity(int position) {
        realm.beginTransaction();
        cities.get(position).deleteFromRealm();
        realm.commitTransaction();
    }

    public void onChange(RealmResults<City> element) {
        adapter.notifyDataSetChanged();
    }

}