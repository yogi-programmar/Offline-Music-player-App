package com.example.musicplayer;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;


public class Allsong extends Fragment {

    RecyclerView recyclerView;

    public musicAdaptere adapter;

    public static ArrayList<Song> songs;

    public Allsong() {
        // Required empty public constructor
    }






    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_allsong, container, false);
        songs=getAllAudioFromDevice(getContext());

        recyclerView=  view.findViewById(R.id.recyallsong);
        recyclerView.setHasFixedSize(true);
        if(!(songs.size()<1)){
            adapter=new musicAdaptere(songs,getContext());
            recyclerView.setAdapter( adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),RecyclerView.VERTICAL,false));     }
        return view;
    }

    private ArrayList<Song> getAllAudioFromDevice(Context context) {
        ArrayList<Song> songArrayList= new ArrayList<>();
        Uri uri= android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String[] projection={MediaStore.Audio.Media.ALBUM,MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.DURATION,MediaStore.Audio.Media.DATA,MediaStore.Audio.Media.ARTIST
        } ;




        Cursor cursor=context.getContentResolver().query(uri,projection,null,null,null);
        if(cursor!=null ){

            while (cursor.moveToNext()){
                String Album=cursor.getString(0);
                String title=cursor.getString(1);
                String duration=cursor.getString(2);
                String Path=cursor.getString(3);
                String Artist=cursor.getString(4);
                Song song= new Song(Path,title,Artist,Album,duration);
                Log.e("title:"+title,"artist"+Artist);
                songArrayList.add(song);

            }
            cursor.close();

        }
        return songArrayList;

    }
}