package com.example.lab41;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.gson.*;


public class MainActivity extends AppCompatActivity {
    ListView tvIds;
    ListView tvArtists;
    ListView tvTitles;

    List<String> titlesList = new ArrayList<String>();
    List<String> artistsList = new ArrayList<String>();
    List<Integer> idsList = new ArrayList<Integer>();
    URL url = null;
    public void restartActivity(){
        Intent intent = new Intent(MainActivity.this, MainActivity.class);
        startActivity(intent);
    }
    InputStream is = null;
    Bitmap bmImg = null;
    Object responseObj;
    ImageView imageView= null;
    ProgressDialog p;
    TextView tv1;
    Button button;
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button=findViewById(R.id.asyncTask);
        imageView=findViewById(R.id.image);
        tv1 = findViewById(R.id.tv1);
        tvIds = (ListView)findViewById(R.id.tvIds);
        tvArtists = (ListView)findViewById(R.id.tvArtists);
        tvTitles = (ListView)findViewById(R.id.tvTitles);
        String actualSong = "";

        DatabaseHandler db = new DatabaseHandler(this);
//        button.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View view) {
//                //Toast.makeText(getApplicationContext(),etInputName.getText() ,Toast.LENGTH_SHORT).show();
//                db.addSong(new Song("Oleg", "Vesna"));
//                restartActivity();
//            }
//        });



        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               new AsyncTaskExample(db).execute("http://www.mofosounds.com/currentsong");
               restartActivity();
            }
        });
        //db.addSong(new Song(, "pesnaolega"));
        db.getAllSongs().forEach(song -> titlesList.add(song.get_title()));
//-----------------------------------------------------------------------
        for (String ar: db.getArtists()) {
            artistsList.add(ar);
        }
//-----------------------------------------------------------------------
        Iterator<Integer> idsIterator = db.getIds().iterator();
        while(idsIterator.hasNext()) idsList.add(idsIterator.next());
//-----------------------------------------------------------------------

        final ArrayAdapter<String> namesAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, artistsList);
        tvArtists.setAdapter(namesAdapter);
//-----------------------------------------------------------------------
        final ArrayAdapter<String> stampsAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, titlesList);
        tvTitles.setAdapter(stampsAdapter);
//-----------------------------------------------------------------------
        final ArrayAdapter<Integer> idsAdapter = new ArrayAdapter<Integer>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, idsList);
        tvIds.setAdapter(idsAdapter);
    }
    protected class AsyncTaskExample extends AsyncTask<String, Integer, String> {
        DatabaseHandler db;
        public AsyncTaskExample(DatabaseHandler db) {

            this.db = db;
        }

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
                System.out.println(conn.getResponseCode());

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
                List<String> list = Arrays.asList(s.split("-"));
                Song last = db.getAllSongs().get(db.getAllSongs().size()-1);
                if(String.valueOf(list.get(0)) != String.valueOf(last.get_artist())){
                    Toast.makeText(getApplicationContext(), "Already in db", Toast.LENGTH_SHORT).show();
                }else{

                    db.addSong(new Song(list.get(0), list.get(1)));
                    Toast.makeText(getApplicationContext(), "Added", Toast.LENGTH_SHORT).show();
                }

            }else {
                p.show();
            }
        }


    }
}