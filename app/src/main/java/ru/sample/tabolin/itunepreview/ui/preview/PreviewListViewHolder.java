package ru.sample.tabolin.itunepreview.ui.preview;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.rahatarmanahmed.cpv.CircularProgressView;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ru.sample.tabolin.itunepreview.R;
import ru.sample.tabolin.itunepreview.domain.PreviewModel;

public class PreviewListViewHolder extends RecyclerView.ViewHolder {

    public static PreviewListViewHolder create(LayoutInflater inflater, ViewGroup parent, OnPreviewItemListClickListener onClickListener){
        return new PreviewListViewHolder(inflater.inflate(R.layout.item_preview, parent, false), onClickListener);
    }

    @BindView(R.id.item_preview_cover) ImageView coverView;
    @BindView(R.id.item_preview_song) TextView trackNameView;
    @BindView(R.id.item_preview_artist) TextView artistNameView;

    private PreviewModel currentItem;
    private OnPreviewItemListClickListener onClickListener;

    private PreviewListViewHolder(View itemView, OnPreviewItemListClickListener onClickListener) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        this.onClickListener = onClickListener;
    }

    public void bind(PreviewModel item){
        currentItem = item;
        artistNameView.setText(item.artistName);
        trackNameView.setText(item.trackName);
        Picasso.with(itemView.getContext()).load(item.artworkUrl100).into(coverView);
    }

    @OnClick(R.id.item_preview_item)
    void onItemClick(){
        if (onClickListener != null && currentItem != null){
            onClickListener.onPreviewItemListClick(currentItem);
        }
    }
}
