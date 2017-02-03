package ru.sample.tabolin.itunepreview.service.media;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.IBinder;

import ru.sample.tabolin.itunepreview.domain.PreviewModel;

public class MediaPlaybackServiceHelper {

    private MediaPlaybackService playbackService;
    private MediaPlaybackService.Callback  callback;

    private ServiceConnection serviceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            MediaPlaybackService.LocalBinder binder = (MediaPlaybackService.LocalBinder) service;
            playbackService = binder.getService();
            playbackService.setCallback(callback);
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            playbackService.setCallback(null);
            playbackService = null;
        }
    };

    public void setCallback(MediaPlaybackService.Callback callback) {
        this.callback = callback;
    }

    public void connect(Context context){
        Intent intent = new Intent(context, MediaPlaybackService.class);
        context.bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    public void disconnect(Context context){
        if (isBinded()){
            context.unbindService(serviceConnection);
        }
    }

    public void start(Context context){
        context.startService(new Intent(context, MediaPlaybackService.class));
    }

    public void stopService(Context context){
        disconnect(context);
        context.stopService(new Intent(context, MediaPlaybackService.class));
        playbackService = null;
    }

    public boolean isBinded() {
        return playbackService != null;
    }


    public void setPreview(PreviewModel preview){
        stop();
        if (isBinded()){
            playbackService.setModel(preview);
        }
    }

    public void action(){
        if (!isBinded()){
            return;
        }

        switch (playbackService.getState() ){
            case STARTED:
                pause();
                break;
            case STOP:
            case PAUSE:
                play();
                break;
        }
    }

    public void seekTo(int mseconds) {
        if (isBinded()){
            playbackService.seekTo(mseconds);
        }
    }


    public void play(){
        if (isBinded()){
            playbackService.play();
        }
    }

    public void stop() {
        if (isBinded()) {
            playbackService.stop();
        }
    }

    public void pause() {
        if (isBinded()) {
            playbackService.pause();
        }
    }
}
