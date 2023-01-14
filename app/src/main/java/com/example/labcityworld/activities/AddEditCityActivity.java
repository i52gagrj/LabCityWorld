package com.example.labcityworld.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.labcityworld.R;
import com.example.labcityworld.models.City;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

import io.realm.Realm;

public class AddEditCityActivity extends AppCompatActivity {

    private int cityId;
    private boolean isCreation;

    private City city;
    private Realm realm;

    private EditText editTextCityName;
    private EditText editTextCityDescription;
    private EditText editTextCityLink;
    private ImageView cityImage;
    private Button btnPreview;
    private FloatingActionButton fab;
    private RatingBar ratingBarCity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_city);

        realm = Realm.getDefaultInstance();
        bindUIReferences();

        if(getIntent().getExtras() != null) {
            cityId = getIntent().getExtras().getInt("id");
            isCreation = false;
        } else {
            isCreation = true;
        }

        setActivityTitle();

        if(!isCreation) {
            city = getCityById(cityId);
            bindDataToFields();
        }

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addEditNewCity();
            }
        });

        btnPreview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String link = editTextCityLink.getText().toString();
            }
        });
    }

    private void bindUIReferences() {
        editTextCityName = (EditText) findViewById(R.id.editTextCityName);
        editTextCityDescription = (EditText) findViewById(R.id.editTextCityDescription);
        editTextCityLink = (EditText) findViewById(R.id.editTextCityImage);
        cityImage = (ImageView) findViewById(R.id.imageViewPreview);
        btnPreview = (Button) findViewById(R.id.buttonPreview);
        fab = (FloatingActionButton) findViewById(R.id.FABEditCity);
        ratingBarCity = (RatingBar) findViewById(R.id.ratingBarCity);
    }

    private void setActivityTitle() {
        String title = "Edit City";
        if (isCreation) title = "Create New City";
        setTitle(title);
    }

    private City getCityById(int cityId) {
        return realm.where(City.class).equalTo("id", cityId).findFirst();
    }

    private void loadImageLinkForPreview(String image) {
        Picasso.with(this).load(image).fit().into(cityImage);
    }

    private void bindDataToFields() {
        editTextCityName.setText(city.getName());
        editTextCityDescription.setText(city.getDescription());
        editTextCityLink.setText((city.getImage()));
        loadImageLinkForPreview(city.getImage());
        ratingBarCity.setRating(city.getStars());
    }

    private boolean isValidDataForNewCity() {
        if(editTextCityName.getText().toString().length() > 0 &&
                editTextCityDescription.getText().toString().length() > 0 &&
                editTextCityLink.getText().toString().length() > 0) {
            return true;
        } else return false;
    }

    private void goToMainActivity() {
        Intent intent = new Intent(AddEditCityActivity.this, MainActivity.class);
        startActivity(intent);
    }

    private void addEditNewCity() {
        if(isValidDataForNewCity()) {
            String name = editTextCityName.getText().toString();
            String description = editTextCityDescription.getText().toString();
            String link = editTextCityLink.getText().toString();
            float stars = ratingBarCity.getRating();

            City city = new City(name, description, link, stars);

            if(!isCreation) city.setId(cityId);

            realm.beginTransaction();
            realm.copyToRealmOrUpdate(city);
            realm.commitTransaction();

            goToMainActivity();
        } else {
            Toast.makeText(this, "The data is not valid, please check the fields again", Toast.LENGTH_SHORT).show();
        }
    }

}