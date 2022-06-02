package com.example.weatherappru;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.ViewTarget;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import pl.droidsonroids.gif.GifImageView;

public class MainActivity extends AppCompatActivity {

    pl.droidsonroids.gif.GifImageView imageView;
    pl.droidsonroids.gif.GifImageView imageView1;
    pl.droidsonroids.gif.GifImageView imageView2;
    pl.droidsonroids.gif.GifImageView imageView3;
    androidx.recyclerview.widget.RecyclerView card_recycler_view;

    private EditText user_field;
    private Button main_btn;
    private TextView result_info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        user_field = findViewById(R.id.user_field);
        main_btn = findViewById(R.id.main_btn);
        result_info = findViewById(R.id.result_info);
        imageView = findViewById(R.id.imageView);
        imageView1 = findViewById(R.id.imageView1);
        imageView2 = findViewById(R.id.imageView2);
        imageView3 = findViewById(R.id.imageView3);


        main_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(user_field.getText().toString().trim().equals("")){
                    Toast.makeText(MainActivity.this, R.string.no_user_input, Toast.LENGTH_LONG).show();
                }
                else{
                    String input = user_field.getText().toString();
                    String key = "9GoQG0HCh03RPJno0twQAh6MKWJcLvW3";
                    String url = "https://api.giphy.com/v1/gifs/search?q=" + input + "&api_key=" + key + "&limit=4";
                    new GetURLData().execute(url);
                }
            }
        });
    }

    private class GetURLData extends AsyncTask<String, String, String> {

        protected void onPreExecute(){
            super.onPreExecute();
            result_info.setText("Please Wait...");
        }

        @Override
        protected String doInBackground(String... strings) {
            HttpsURLConnection connection = null;
            BufferedReader reader = null;

            try {
                URL url = new URL(strings[0]);
                connection = (HttpsURLConnection) url.openConnection();
                connection.connect();

                InputStream stream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));

                StringBuffer buffer = new StringBuffer();
                String line = "";

                while((line = reader.readLine()) != null){
                    buffer.append(line).append("\n");
                }

                return buffer.toString();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally{
                if(connection != null) {
                    connection.disconnect();
                }
                try {
                    if (reader != null) {
                        reader.close();
                    }
                }catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                return null;
            }

            @SuppressLint("SetTextI18n")
            @Override
            protected void onPostExecute(String result){
                super.onPostExecute(result);

                try {
                    JSONObject jsonObject = new JSONObject(result);
                    JSONArray arr = jsonObject.getJSONArray("data");

                    String[] links = new String[4];
                    GifImageView[] imageViewAll = {imageView, imageView1, imageView2, imageView3};

                    for (int i = 0; i<=3; i++) {
                        links[i] = arr.getJSONObject(i)
                                .getJSONObject("images")
                                .getJSONObject("original")
                                .getString("url");

                        Glide.with(MainActivity.this)
                                .load(links[i])
                                .override(1000,1000)
                                .into(imageViewAll[i]);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                result_info.setText("results:");
            }
        }
    }
//}