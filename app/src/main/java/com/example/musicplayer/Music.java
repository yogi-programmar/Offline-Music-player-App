package com.example.musicplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import static com.example.musicplayer.Allsong.songs;


public class Music extends AppCompatActivity {
    ImageView squareImageView;

    ImageView playbtn, prevbtn, nextbtn, shufflebtn;
    SeekBar progressBar;
    TextView totaltime, lifetime, musicnmame, aartname;
    static Uri uri;
    static MediaPlayer mediaPlayer;
    int postion = -1;
    public Handler handler = new Handler();
    public  Thread play,previoyus ,next;


    static ArrayList<Song> arrayList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music);

        INitViewe();
        getIntentmethod();
        musicnmame.setText(songs.get(postion).getTitle());
        aartname.setText(songs.get(postion).getArtist());

        progressBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (mediaPlayer != null && fromUser) {
                    mediaPlayer.seekTo(progress * 1000);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        Music.this.runOnUiThread(new Runnable() {
        @Override
        public void run() {
            if (mediaPlayer != null) {
           int Currentposition = mediaPlayer.getCurrentPosition() / 1000;
            progressBar.setProgress(Currentposition);
           lifetime.setText(formattedTime(Currentposition));
            }
          handler.postDelayed(this, 1000);
        }
    });
    }

    private String formattedTime( int currentposition) {
        String totalout="";
        String totalnew="";
        String second=String.valueOf(currentposition%60);
        String minutes=String.valueOf(currentposition/60);
        totalout=minutes+":"+second;
        totalnew=minutes+":"+"0"+second;
        if(second.length()==1){
            return  totalnew;
        }else{
            return totalout;
        }
   }
    private void getIntentmethod() {
        postion=getIntent().getIntExtra("position",-1);
        arrayList=songs;
        if(arrayList!=null){
            playbtn.setImageResource(R.drawable.ic_pause);
            uri= Uri.parse(arrayList.get(postion).getPath());
        }if(mediaPlayer!=null){
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer= MediaPlayer.create(getApplicationContext(),uri);
            mediaPlayer.start();
        }else{
            mediaPlayer=MediaPlayer.create(getApplicationContext(),uri);
            mediaPlayer.start();

        }
        progressBar.setMax(mediaPlayer.getDuration()/1000);
        metadeta(uri );

    }


    private void   INitViewe() {
      squareImageView=findViewById(R.id.albumimg);
//       toggleButton=findViewById(R.id.tb_love);
        playbtn=findViewById(R.id.ic_play);
   prevbtn=findViewById(R.id.ic_previous);
        progressBar=findViewById(R.id.seekbar);
    nextbtn=findViewById(R.id.ic_next);
totaltime=findViewById(R.id.txttotaltime);
musicnmame=findViewById(R.id.music_name);
  aartname=findViewById(R.id.artist_txt);
     lifetime=findViewById(R.id.txtcurenttime);
       shufflebtn=findViewById(R.id.shuffle);

    }
    private void metadeta( Uri uri){
        MediaMetadataRetriever mediaMetadataRetriever= new MediaMetadataRetriever();
        int duration= Integer.parseInt(songs.get(postion).getDuration())/1000;
        totaltime.setText(formattedTime(duration));
//        mediaMetadataRetriever.setDataSource(uri.toString());
        byte [] art=mediaMetadataRetriever.getEmbeddedPicture();
        if(art!=null){
            Glide.with(this)
                    .asBitmap()
                    .load(art)
                    .into(squareImageView);

        }else{
            Glide.with(this)
                    .asBitmap()
                    .load(R.drawable.ic_allsong)
                    .into(squareImageView);
        }
    }

    @Override
    protected void onResume() {
        playthread();
        nextthread();
        previousthread();
        super.onResume();
    }

    private void previousthread() {
        previoyus= new Thread(){
            @Override
            public void run() {
                super.run();
                prevbtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        previousclicked();
                    }


                });
            }
        };
        previoyus.start();

    }

    private void previousclicked() {

        if(mediaPlayer.isPlaying()){
            mediaPlayer.stop();
            mediaPlayer.release();
            postion=((postion-1)%arrayList.size());
            uri=Uri.parse(arrayList.get(postion).getPath());
            mediaPlayer=MediaPlayer.create(getApplicationContext(),uri);
            metadeta(uri);
            musicnmame.setText(arrayList.get(postion).getTitle());
            aartname.setText(arrayList.get(postion).getArtist());
            Music.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (mediaPlayer != null) {
                        int Currentposition = mediaPlayer.getCurrentPosition() / 1000;
                        progressBar.setProgress(Currentposition);
                        lifetime.setText(formattedTime(Currentposition));
                    }
                    handler.postDelayed(this, 1000);
                }
            });
            playbtn.setImageResource(R.drawable.ic_pause);
            mediaPlayer.start();
        }else {
            if(!mediaPlayer.isPlaying()) {

                mediaPlayer.release();
                postion = ((postion - 1) % arrayList.size());
                uri = Uri.parse(arrayList.get(postion).getPath());
                mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
                metadeta(uri);
                musicnmame.setText(arrayList.get(postion).getTitle());
                aartname.setText(arrayList.get(postion).getArtist());
                Music.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (mediaPlayer != null) {
                            int Currentposition = mediaPlayer.getCurrentPosition() / 1000;
                            progressBar.setProgress(Currentposition);
                            lifetime.setText(formattedTime(Currentposition));
                        }
                        handler.postDelayed(this, 1000);
                    }
                });
                playbtn.setImageResource(R.drawable.ic_play);
                mediaPlayer.start();
            }


        }
    }

    private void nextthread() {
        next= new Thread(){
            @Override
            public void run() {
                super.run();
                nextbtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        nextclicked();
                    }


                });
            }
        };
        next.start();
    }

    private void nextclicked() {
        if(mediaPlayer.isPlaying()){
            mediaPlayer.stop();
            mediaPlayer.release();
            postion=((postion+1)%arrayList.size());
            uri=Uri.parse(arrayList.get(postion).getPath());
            mediaPlayer=MediaPlayer.create(getApplicationContext(),uri);
            metadeta(uri);
            musicnmame.setText(arrayList.get(postion).getTitle());
            aartname.setText(arrayList.get(postion).getArtist());
            Music.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (mediaPlayer != null) {
                        int Currentposition = mediaPlayer.getCurrentPosition() / 1000;
                        progressBar.setProgress(Currentposition);
                        lifetime.setText(formattedTime(Currentposition));
                    }
                    handler.postDelayed(this, 1000);
                }
            });
            playbtn.setImageResource(R.drawable.ic_pause);
            mediaPlayer.start();
        }else{
            if(!mediaPlayer.isPlaying()) {

                mediaPlayer.release();
                postion = ((postion - 1) % arrayList.size());
                uri = Uri.parse(arrayList.get(postion).getPath());
                mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
                metadeta(uri);
                musicnmame.setText(arrayList.get(postion).getTitle());
                aartname.setText(arrayList.get(postion).getArtist());

                Music.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (mediaPlayer != null) {
                            int Currentposition = mediaPlayer.getCurrentPosition() / 1000;
                            progressBar.setProgress(Currentposition);
                            lifetime.setText(formattedTime(Currentposition));
                        }
                        handler.postDelayed(this, 1000);
                    }
                });
                playbtn.setImageResource(R.drawable.ic_play);
                mediaPlayer.start();
            }
        }



    }

    private void playthread() {
        play= new Thread(){
            @Override
            public void run() {
                super.run();
                playbtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        playpusebuttonclicked();
                    }


                });
            }
        };
        play.start();

    }

    private void playpusebuttonclicked() {
        if(mediaPlayer.isPlaying()){
            playbtn.setImageResource(R.drawable.ic_play);
            mediaPlayer.pause();
            progressBar.setMax(mediaPlayer.getDuration()/1000);
            Music.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (mediaPlayer != null) {
                        int Currentposition = mediaPlayer.getCurrentPosition() / 1000;
                        progressBar.setProgress(Currentposition);
                        lifetime.setText(formattedTime(Currentposition));
                    }
                    handler.postDelayed(this, 1000);
                }
            });
        }else {
            playbtn.setImageResource(R.drawable.ic_pause);
            mediaPlayer.start();
            progressBar.setMax(mediaPlayer.getDuration()/1000);
            Music.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (mediaPlayer != null) {
                        int Currentposition = mediaPlayer.getCurrentPosition() / 1000;
                        progressBar.setProgress(Currentposition);
                        lifetime.setText(formattedTime(Currentposition));
                    }
                    handler.postDelayed(this, 1000);
                }
            });
        }
    }
}