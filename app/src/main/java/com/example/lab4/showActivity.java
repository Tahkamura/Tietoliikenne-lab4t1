package com.example.lab4;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;


public class showActivity extends AppCompatActivity {

    ListView listView;
    ArrayList<String> list;
    ArrayAdapter<String> adapter;
    RequestQueue queue;
    JSONArray arr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        // Alustuksia
        queue = Volley.newRequestQueue(this);
        listView = findViewById(R.id.listView2);
        list = new ArrayList<String>();
        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_expandable_list_item_1,list);
        listView.setAdapter(adapter);

        // Haetaan tiedot main activityltä ja asetetaan otsikko
        String id = getIntent().getStringExtra("1029");
        String name = getIntent().getStringExtra("1030");
        TextView header = findViewById(R.id.otsikko2);
        header.setText(name);

        // kutsutaan jsonfunktio saadulla maa id:llä
        String url = "https://api.football-data.org/v2/competitions?areas=" + id;
        getDatJson(url);

    }

    // funktio json datan hakemiseen ja sen lisäämisen Arraylistiin
    public void getDatJson(String url){

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            Iterator it = response.keys();
                            arr = response.getJSONArray("competitions");
                            for(int i=0;i<arr.length();i++){
                                JSONObject c = arr.getJSONObject(i);
                                String name = c.getString("name");
                                list.add(name);
                                adapter.notifyDataSetChanged();
                            }

                        }catch (Exception e){
                            Log.d("Appi","failed");
                        }

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                });

        queue.add(jsonObjectRequest);
    }

}
