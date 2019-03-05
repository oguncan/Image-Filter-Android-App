package com.example.joousope.first.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.joousope.first.FrameFragment;
import com.example.joousope.first.R;

import java.util.ArrayList;
import java.util.List;

public class FrameAdapter extends RecyclerView.Adapter<FrameAdapter.FrameViewHolder> {
    List<Integer> frameList;
    Context context;
    int row_selected=-1;
    FrameAdapterListener listener;
    public FrameAdapter(Context context, FrameAdapterListener listener)
    {
        this.context=context;
        this.frameList=getFramesList();
        this.listener=listener;
    }

    private List<Integer> getFramesList() {
        List<Integer> result=new ArrayList<>();
        result.add(R.drawable.card_2_resize);
        result.add(R.drawable.card_3_resize);
        result.add(R.drawable.card_4_resize);
        result.add(R.drawable.card_5_resize);
        result.add(R.drawable.card_7_resize);
        result.add(R.drawable.card_8_resize);
        result.add(R.drawable.card_9_resize);
        result.add(R.drawable.card_10_resize);
        return result;
    }

    @NonNull
    @Override
    public FrameViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView=LayoutInflater.from(context).inflate(R.layout.frame_list,viewGroup,false);
        FrameViewHolder viewHolder=new FrameViewHolder(itemView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull FrameViewHolder frameViewHolder, int i) {
        if(row_selected==i)
        {
            frameViewHolder.imageCheck.setVisibility(View.VISIBLE);
        }
        else
        {
            frameViewHolder.imageCheck.setVisibility(View.INVISIBLE);
        }
        frameViewHolder.imageFrame.setImageResource(frameList.get(i));
    }

    @Override
    public int getItemCount() {
        return frameList.size();
    }

    public class FrameViewHolder extends RecyclerView.ViewHolder {
        ImageView imageCheck,imageFrame;
        public FrameViewHolder(@NonNull View itemView) {
            super(itemView);
            imageCheck=(ImageView)itemView.findViewById(R.id.img_check);
            imageFrame=(ImageView)itemView.findViewById(R.id.img_frame);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onFrameSelected(frameList.get(getAdapterPosition()));
                    row_selected=getAdapterPosition();
                    notifyDataSetChanged();
                }
            });
        }
    }
    public interface FrameAdapterListener {
        void onFrameSelected(int frame);
    }
}
