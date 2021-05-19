package com.example.lab41;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    URL url = null;
    InputStream is = null;
    Bitmap bmImg = null;
    Object responseObj;
    ImageView imageView= null;
    ProgressDialog p;
    TextView tv1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button button=findViewById(R.id.asyncTask);
        imageView=findViewById(R.id.image);
        tv1 = findViewById(R.id.tv1);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AsyncTaskExample().execute("https://webradio.io/api/radio/pi/current-song");
            }
        });
    }
    protected class AsyncTaskExample extends AsyncTask<String, Integer, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            p = new ProgressDialog(MainActivity.this);
            p.setMessage("Please wait...It is downloading");
            p.setIndeterminate(false);
            p.setCancelable(false);
            p.show();
        }
        @Override
        protected String doInBackground(String... strings) {
            String mockStr = null;
            try {
                url = new URL(strings[0]);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setDoInput(true);
                conn.connect();
                is = conn.getInputStream();
                InputStreamReader isReader = new InputStreamReader(is);
                BufferedReader reader = new BufferedReader(isReader);
                StringBuffer sb = new StringBuffer();
                String str;
                while((str = reader.readLine())!= null){
                    sb.append(str);
                }
                mockStr = sb.toString();


            }catch (IOException e) {
                e.printStackTrace();
            }
            return mockStr;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if(imageView!=null) {
                p.hide();
                tv1.setText(s);
            }else {
                p.show();
            }
        }


    }
}