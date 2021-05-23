package com.example.lab41;

public class Song {
    String _artist;
    String _title;
    int _id;

    public Song(){}
    public Song(String artist, String title){
        this._artist = artist;
        this._title  = title;
    }
    public Song(int id, String artist, String title){
        this._artist = artist;
        this._title  = title;
        this._id = id;
    }

    public void set_artist(String _artist) {
        this._artist = _artist;
    }

    public void set_title(String _title) {
        this._title = _title;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String get_artist() {
        return _artist;
    }

    public String get_title() {
        return _title;
    }
}
