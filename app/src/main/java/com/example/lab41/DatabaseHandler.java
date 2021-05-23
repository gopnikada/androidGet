package com.example.lab41;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "songManager";
    private static final String TABLE_SONGS = "songs";
    private static final String KEY_ID = "id";
    private static final String KEY_ARTIST = "artist";
    private static final String KEY_TITLE = "title";
    private static final String KEY_TIMESTAMP = "timestamp";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_SONGS_TABLE = "CREATE TABLE " + TABLE_SONGS + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_ARTIST + " TEXT," + KEY_TITLE + " TEXT,"
                + KEY_TIMESTAMP + " integer(4) not null default (strftime('%s','now'))" + ")";
        db.execSQL(CREATE_SONGS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    void addSong(Song song){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_ARTIST, song.get_artist());
        values.put(KEY_TITLE, song.get_title());
        db.insert(TABLE_SONGS, null, values);
        db.close();
    }
    Song getSong(int id){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_SONGS, new String[] { KEY_ID,
                        KEY_ARTIST, KEY_TITLE }, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
        Song song = new Song(cursor.getString(1),
                cursor.getString(2));
        return song;
    }
    public List<Song> getAllSongs(){
        List<Song> songList = new ArrayList<Song>();
        String selectQuery = "SELECT  * FROM " + TABLE_SONGS;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);


        if (cursor.moveToFirst()) {
            do {
                Song song = new Song();
                song.set_id(Integer.parseInt(cursor.getString(0)));
                song.set_artist(cursor.getString(1));
                song.set_title(cursor.getString(2));
                songList.add(song);
            } while (cursor.moveToNext());
        }

        return songList;
    }
     public List<Integer> getIds(){
        List<Integer> idsList = new ArrayList<Integer>();
        String selectQuery = "SELECT "+KEY_ID+" FROM " + TABLE_SONGS;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Integer id;
                id = cursor.getInt(0);
                // Adding contact to list
                idsList.add(id);
            } while (cursor.moveToNext());
        }

        return idsList;
    }

    public List<String> getArtists() {
        List<String> artistsList = new ArrayList<String>();
        String selectQuery = "SELECT "+KEY_ARTIST+" FROM " + TABLE_SONGS;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                String artist;
                artist = cursor.getString(0);
                artistsList.add(artist);
            } while (cursor.moveToNext());
        }
        return artistsList;
    }
}
