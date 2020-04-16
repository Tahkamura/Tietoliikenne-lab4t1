package com.example.lab4;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
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


//  Lab4 tehtävä 1
public class MainActivity extends AppCompatActivity {

    ListView listView;
    ArrayList<String> list;
    ArrayAdapter<String> adapter;
    RequestQueue queue;
    JSONArray arr;
    ListView lista;
    final String ID = "1029"; // Datan välittämiseen id:t
    final String ID2 = "1030";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        //Alustuksia
        queue = Volley.newRequestQueue(this);
        listView = findViewById(R.id.listView);
        list = new ArrayList<String>();
        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_expandable_list_item_1,list);
        listView.setAdapter(adapter);
        lista = findViewById(R.id.listView);

        // Kutsutaan funktio jolla haetaan tiedot maista
        getDatJson("https://api.football-data.org/v2/areas");

        // Funktio sille kun listviewiä painetaan jolloin avataan uusi activity niillä tiedoilla
        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                try {
                    JSONObject c = arr.getJSONObject(position);
                    String countryId = c.getString("id");
                    String name = c.getString("name");
                    Intent openActivity = new Intent(MainActivity.this,showActivity.class);
                    openActivity.putExtra(ID,countryId);
                    openActivity.putExtra(ID2,name);
                    startActivity(openActivity);

                }catch (Exception e){

                }
            }
        });
    }

    // Funktio jolla haetaan kaikki maat ja kirjataan ne listviewiin
    public void getDatJson(String url){

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Iterator it = response.keys();
                            arr = response.getJSONArray("areas");
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

