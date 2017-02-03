package ru.sample.tabolin.itunepreview.service.media;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.IOException;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import ru.sample.tabolin.itunepreview.BuildConfig;
import ru.sample.tabolin.itunepreview.domain.PreviewModel;

public class MediaPlaybackService extends Service implements MediaPlayer.OnCompletionListener, MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener {

    public interface Callback{

        void onStateChanged(PlayerState state);

        void onProgressChanged(int current, int total);

        void onPreviewChanged(PreviewModel currentPreview);

    }

    public enum PlayerState{
        PREPARING, STARTED, STOP, PAUSE
    }

    private final IBinder mBinder = new LocalBinder();

    private Handler durationHandler = new Handler();
    private MediaPlayer mediaPlayer;
    private PlayerState currentState;
    private Callback callback;
    private PreviewModel currentPreview;

    public class LocalBinder extends Binder {
        public MediaPlaybackService getService() {
            return MediaPlaybackService.this;
        }
    }

    private Runnable updateProgressTask = new Runnable() {

        public void run() {
            if (callback != null && mediaPlayer.isPlaying()){
                callback.onProgressChanged(mediaPlayer.getCurrentPosition(), mediaPlayer.getDuration());
            }
            durationHandler.postDelayed(this, 100);
        }
    };



    public void setCallback(Callback callback) {
        this.callback = callback;
        if (callback != null){
            callback.onStateChanged(currentState);
            callback.onPreviewChanged(currentPreview);
            if (mediaPlayer.isPlaying()){
                callback.onProgressChanged(mediaPlayer.getCurrentPosition(), mediaPlayer.getDuration());
            }
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        setState(PlayerState.STOP);
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaPlayer.setOnCompletionListener(this);
        mediaPlayer.setOnPreparedListener(this);
        mediaPlayer.setOnErrorListener(this);
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        setState(PlayerState.STOP);
        mp.stop();
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        setState(PlayerState.STARTED);
        mp.start();
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        setState(PlayerState.STOP);
        return true;
    }

    public PlayerState getState() {
        return currentState;
    }

    private void setState(PlayerState state){
        if (currentState == state){
            return;
        }
        if (BuildConfig.DEBUG){
            Log.d("MediaPlaybackService", "state: " + state);
        }

        if (state == PlayerState.STARTED){
            durationHandler.post(updateProgressTask);
        }else{
            durationHandler.removeCallbacks(updateProgressTask);
        }
        currentState = state;
        if (callback != null){
            callback.onStateChanged(currentState);
        }
    }

    public void stop() {
        setState(PlayerState.STOP);
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
        }
        mediaPlayer.reset();
    }

    public void seekTo(int mseconds) {
        mediaPlayer.seekTo(mseconds);
    }

    public void pause() {
        if (mediaPlayer.isPlaying()) {
            setState(PlayerState.PAUSE);
            mediaPlayer.pause();
        }
    }

    public void setModel(final PreviewModel preview) {
        currentPreview = preview;
        stop();


        if (callback != null){
            callback.onPreviewChanged(currentPreview);
        }
    }

    public void play() {
        if (currentState == PlayerState.PREPARING || currentPreview == null){
            return;
        }

        if (currentState == PlayerState.PAUSE){
            onPrepared(mediaPlayer);
            return;
        }

        if (mediaPlayer.isPlaying()){
            mediaPlayer.stop();
        }
        mediaPlayer.reset();

        try {
            setState(PlayerState.PREPARING);
            mediaPlayer.setDataSource(currentPreview.previewUrl);
            mediaPlayer.prepareAsync();
        } catch (IOException error) {
            setState(PlayerState.STOP);
            Log.e("MediaPlaybackService", "play", error);
        }
    }

    @Override
    public void onDestroy() {
        if (mediaPlayer != null) {
            setState(PlayerState.STOP);
            mediaPlayer.release();
        }
        durationHandler.removeCallbacks(updateProgressTask);
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }


}
