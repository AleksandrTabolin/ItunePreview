package ru.sample.tabolin.itunepreview.ui.preview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.Collections;
import java.util.List;

import ru.sample.tabolin.itunepreview.domain.PreviewModel;

public class PreviewListAdapter extends RecyclerView.Adapter<PreviewListViewHolder> {

    private final LayoutInflater inflater;

    private List<PreviewModel> items = Collections.emptyList();

    public PreviewListAdapter(Context context) {
        inflater = LayoutInflater.from(context);
    }

    @Override
    public PreviewListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return PreviewListViewHolder.create(inflater, parent);
    }

    public void setItems(List<PreviewModel> items){
        this.items = items;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(PreviewListViewHolder holder, int position) {
        holder.bind(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
