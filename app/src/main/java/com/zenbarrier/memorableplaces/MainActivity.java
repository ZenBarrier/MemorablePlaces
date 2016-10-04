package com.zenbarrier.memorableplaces;

import android.content.Intent;
import android.location.Geocoder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    ArrayList<String> places;
    ListView listView;
    ArrayAdapter<String> adapter;

    public void addPlace(View view){
        Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
        intent.putStringArrayListExtra("places", places);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i("request code",requestCode+"");
        if(requestCode == 1){
            Log.i("result code",resultCode+"");
            if(resultCode == RESULT_OK){
                ArrayList<String> dataPlaces = data.getStringArrayListExtra("places");
                if(dataPlaces != null){
                    places.addAll(dataPlaces);
                    adapter.notifyDataSetChanged();
                }
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView) findViewById(R.id.listView);

        places = new ArrayList<>();
        places.add("herro");

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, places);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
    }
}
