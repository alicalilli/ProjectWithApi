package com.example.projectwithapi;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.projectwithapi.utils.Network;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    Button btnClick;
    EditText cityEd;
    TextView resultsTv;
    String cityName;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        cityEd = findViewById(R.id.city_et);
        resultsTv= findViewById(R.id.city_net);
        btnClick= findViewById(R.id.btn_click);

        btnClick.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        if (view.getId()== R.id.btn_click){
            cityName = cityEd.getText().toString();
            getData( );

        }

    }
    public void getData ( ){
        Uri uri =Uri.parse("https://datausa.io/api/data?drilldowns=State&measures=Population&year=latest")
        .buildUpon().build();

    }
    class DOTask extends AsyncTask<URL,Void,String>{


        @Override
        protected String doInBackground(URL... urls) {
            URL url = urls[0];
            String data = null;
            try {
                 data = Network.makeHTTPRequest(url);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return data;

        }
        @Override
        protected void onPostExecute(String s){
            try {
                parseJson(s);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        public void parseJson(String data) throws JSONException{
            JSONObject jsonObject = null;
            try {
                jsonObject=new JSONObject(data);

            }
            catch (JSONException e){
                e.printStackTrace();
            }
            JSONArray cityArray = jsonObject.getJSONArray("data");
            for (int i=0; i<cityArray.length();i++){
                JSONObject cityo = cityArray.getJSONObject(i);
                String cityn = cityo.get("State").toString();
                if (cityn.equals(cityName)){
                    String population = cityo.get("Populaion").toString();
                    resultsTv.setText(population);
                    break;

                }
                else{
                    resultsTv.setText("Fail");

                }

            }

        }
    }

}