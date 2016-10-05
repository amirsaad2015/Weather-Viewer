package com.example.amir.webapi;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private String city;
    private String API_URL;
    private String API_KEY;
    private String description;
    private double min;
    private double max;
    private double humidity;

    private List<String> obj;
    private ListView listView;
    private ArrayAdapter<String> arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        obj = new ArrayList<String>();
        listView = (ListView) findViewById(R.id.responseView);

        arrayAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, obj);

        listView.setAdapter(arrayAdapter);

        API_URL = getResources().getString(R.string.API_URL);
        API_KEY = getResources().getString(R.string.API_KEY);

    }

    public void search(View view) {
        EditText cityText = (EditText) findViewById(R.id.cityText);
        RetrieveFeedTask RFT = new RetrieveFeedTask();
        hideKeyBoard(cityText);
        RFT.execute();
    }

    private void hideKeyBoard(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private void getCity() {
        EditText cityText = (EditText) findViewById(R.id.cityText);
        city = cityText.getText().toString();
    }

    class RetrieveFeedTask extends AsyncTask<Void, Void, String> {

        ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);

        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);

            //Clear existing weather objects
            arrayAdapter.clear();

        }

        @Override
        protected String doInBackground(Void... urls) {
            getCity();

            try {
                URL url = new URL(API_URL + URLEncoder.encode(city, "UTF-8") + "&units=imperial&cnt=16&APPID=" + API_KEY);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                try {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuilder stringBuilder = new StringBuilder();
                    String line;

                    while ((line = bufferedReader.readLine()) != null) {
                        stringBuilder.append(line).append("\n");
                    }
                    bufferedReader.close();
                    return stringBuilder.toString();

                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    urlConnection.disconnect();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String response) {
            if (response == null) {
                response = "THERE WAS AN ERROR";
            }
            progressBar.setVisibility(View.GONE);

            //Parse JSON data

            try {
                JSONObject parentObject = new JSONObject(response);
                JSONArray list = parentObject.getJSONArray("list");

                for (int i = 0; i < list.length(); i++) {

                    JSONObject firstElement = list.getJSONObject(i);

                    //Get the temperature data
                    JSONObject temp = firstElement.getJSONObject("temp");
                    max = temp.getDouble("max");
                    min = temp.getDouble("min");

                    //Get humidity
                    humidity = firstElement.getDouble("humidity");
                    //Get weather description
                    JSONArray weather = firstElement.getJSONArray("weather");
                    JSONObject weatherElem = weather.getJSONObject(0);
                    description = weatherElem.getString("description");

                    obj.add(new Weather(description, max, min, humidity).toString());
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
                arrayAdapter.addAll(obj);
                arrayAdapter.notifyDataSetChanged();
        }
    }
}
