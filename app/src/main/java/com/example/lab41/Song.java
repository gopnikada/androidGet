package com.example.lab41;

public class Song {
    String _artist;
    String _title;

    public Song(){}
    public Song(String artist, String song){
        this._artist = artist;
        this._title  = song;
    }

    public String get_artist() {
        return _artist;
    }

    public String get_title() {
        return _title;
    }
}
