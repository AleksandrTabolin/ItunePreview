package ru.sample.tabolin.itunepreview.ui.player;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.github.rahatarmanahmed.cpv.CircularProgressView;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnPageChange;
import ru.sample.tabolin.itunepreview.R;
import ru.sample.tabolin.itunepreview.domain.PreviewModel;
import ru.sample.tabolin.itunepreview.service.media.MediaPlaybackService;

public class MediaPlaybackUiHelper implements MediaPlaybackService.Callback{

    public interface Callback{
        void onActionBtnClick();
        void onCloseClick();
        void onProgressChanged(int progress);
    }

    public static MediaPlaybackUiHelper createAndAttach(ViewGroup container, Callback callback){
        return new MediaPlaybackUiHelper(container, callback);
    }

    @BindView(R.id.fragment_player_cover) ImageView coverView;
    @BindView(R.id.fragment_player_song) TextView trackNameView;
    @BindView(R.id.fragment_player_artist) TextView artistNameView;
    @BindView(R.id.fragment_player_action) ImageView actionView;
    @BindView(R.id.fragment_player_progress) SeekBar progressBarView;
    @BindView(R.id.fragment_player_preparing) CircularProgressView preparingView;

    private ViewGroup container;
    private Context context;
    private Callback callback;

    private MediaPlaybackUiHelper(ViewGroup container, Callback callback){
        this.container = container;
        this.callback = callback;
        context = container.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.fragment_media_playback, container);
        ButterKnife.bind(this, view);
        progressBarView.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser && callback != null){
                    callback.onProgressChanged(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }

    public void show(){
        container.setVisibility(View.VISIBLE);
    }

    public void hide(){
        container.setVisibility(View.GONE);
    }

    @OnClick(R.id.fragment_player_action)
    void onActionBtnClick(){
        if (callback != null){
            callback.onActionBtnClick();
        }
    }

    @OnClick(R.id.fragment_player_close)
    void onCloseBtnClick(){
        hide();
        if (callback != null){
            callback.onCloseClick();
        }
    }

    @Override
    public void onStateChanged(MediaPlaybackService.PlayerState state) {
        switch (state){
            case PREPARING:
                preparingView.setVisibility(View.VISIBLE);
                actionView.setVisibility(View.GONE);
                break;
            case STARTED:
                preparingView.setVisibility(View.INVISIBLE);
                actionView.setVisibility(View.VISIBLE);
                actionView.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_pause_circle));
                break;
            case STOP:
            case PAUSE:
                preparingView.setVisibility(View.INVISIBLE);
                actionView.setVisibility(View.VISIBLE);
                actionView.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_play_circle));
                break;
        }
    }

    @Override
    public void onProgressChanged(int current, int total) {
        progressBarView.setMax(total);
        progressBarView.setProgress(current);
    }

    @Override
    public void onPreviewChanged(PreviewModel preview) {
        if (preview == null){
            hide();
            return;
        }
        show();
        artistNameView.setText(preview.artistName);
        trackNameView.setText(preview.trackName);
        progressBarView.setProgress(0);
        Picasso.with(container.getContext()).load(preview.artworkUrl100).into(coverView);
    }
}
