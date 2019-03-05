package com.example.joousope.first.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.joousope.first.FiltersList;
import com.example.joousope.first.Interface.FiltersListFragmentListener;
import com.example.joousope.first.R;
import com.zomato.photofilters.imageprocessors.Filter;
import com.zomato.photofilters.utils.ThumbnailItem;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.rockerhieu.emojicon.EmojiconTextView;

public class ThumbnailAdapter extends RecyclerView.Adapter<ThumbnailAdapter.MyViewHolder> {


    private List<ThumbnailItem> thumbnailItems;
    private FiltersListFragmentListener listener;
    private Context context;
    private int selectedIndex=0;

    public class MyViewHolder extends RecyclerView.ViewHolder{
        ImageView thumbnail;
        TextView filter_name;
        public MyViewHolder(View itemView) {
            super(itemView);
            thumbnail=(ImageView)itemView.findViewById(R.id.thumbnail);
            filter_name=(TextView)itemView.findViewById(R.id.filter_name);
            ButterKnife.bind(this,itemView);
        }
    }

    public ThumbnailAdapter(List<ThumbnailItem> thumbnailItems, FiltersListFragmentListener listener, Context context) {
        this.thumbnailItems = thumbnailItems;
        this.listener = listener;
        this.context = context;
        }


    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView= LayoutInflater.from(context).inflate(R.layout.thumbnail_list_item,viewGroup,false);
        MyViewHolder myViewHolder=new MyViewHolder(itemView);
        return myViewHolder;
    }



    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, final int i) {
        final ThumbnailItem thumbnailItem= thumbnailItems.get(i);
        myViewHolder.thumbnail.setImageBitmap(thumbnailItem.image);
        myViewHolder.thumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onFilterSelected(thumbnailItem.filter);
                selectedIndex=i;
                notifyDataSetChanged();
            }
        });
        myViewHolder.filter_name.setText(thumbnailItem.filterName);

        if(selectedIndex==i)
            myViewHolder.filter_name.setTextColor(ContextCompat.getColor(context,R.color.selected_filter));
        else
            myViewHolder.filter_name.setTextColor(ContextCompat.getColor(context,R.color.normal_filter));
    }

    @Override
    public int getItemCount() {
        return thumbnailItems.size();
    }

    public interface ThumbnailsAdapterListener {
        void onFilterSelected(Filter filter);
    }
}
