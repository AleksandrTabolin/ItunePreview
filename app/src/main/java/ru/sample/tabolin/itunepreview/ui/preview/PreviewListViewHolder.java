package ru.sample.tabolin.itunepreview.ui.preview;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.sample.tabolin.itunepreview.R;
import ru.sample.tabolin.itunepreview.domain.PreviewModel;

public class PreviewListViewHolder extends RecyclerView.ViewHolder {

    public static PreviewListViewHolder create(LayoutInflater inflater, ViewGroup parent){
        return new PreviewListViewHolder(inflater.inflate(R.layout.item_preview, parent, false));
    }

    @BindView(R.id.item_preview_cover) ImageView coverView;
    @BindView(R.id.item_preview_song) TextView trackNameView;
    @BindView(R.id.item_preview_artist) TextView artistNameView;

    private PreviewListViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void bind(PreviewModel item){
        artistNameView.setText(item.artistName);
        trackNameView.setText(item.trackName);
        Picasso.with(itemView.getContext()).load(item.artworkUrl100).into(coverView);
    }
}
