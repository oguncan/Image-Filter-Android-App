package com.example.joousope.first.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.joousope.first.R;

import java.util.ArrayList;
import java.util.List;

public class ColorAdapter extends RecyclerView.Adapter<ColorAdapter.ColorViewHolder> {
    Context context;
    List<Integer> colorList;
    ColorAdapterListener listener;
    int row_selected=-1;


    public ColorAdapter(Context context, ColorAdapterListener listener) {
        this.context = context;
        this.colorList = genColorList();
        this.listener = listener;
    }

    @NonNull
    @Override
    public ColorViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView=LayoutInflater.from(context).inflate(R.layout.color_item,viewGroup,false);
        return new ColorViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ColorViewHolder colorViewHolder, int i) {
        if(row_selected==i)
        {
            colorViewHolder.imageCheck.setVisibility(View.VISIBLE);
        }
        else
        {
            colorViewHolder.imageCheck.setVisibility(View.INVISIBLE);
        }
        colorViewHolder.color_section.setBackgroundColor(colorList.get(i));
    }

    @Override
    public int getItemCount() {
        return colorList.size();
    }

    public class ColorViewHolder extends RecyclerView.ViewHolder{
        public CardView color_section;
        public ImageView imageCheck;
        public ColorViewHolder(View itemView){
                    super(itemView);
                    imageCheck=(ImageView)itemView.findViewById(R.id.img_check2);
                    color_section=(CardView)itemView.findViewById(R.id.color_section);
                    itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            listener.onColorSelected(colorList.get(getAdapterPosition()));
                            row_selected=getAdapterPosition();
                            notifyDataSetChanged();
                        }
                    });
        }

    }
    private List<Integer> genColorList() {
        List<Integer> colorList=new ArrayList<>();
        colorList.add(Color.parseColor("#131722"));
        colorList.add(Color.parseColor("#314159"));
        colorList.add(Color.parseColor("#aa5511"));
        colorList.add(Color.parseColor("#dab420"));
        colorList.add(Color.parseColor("#caff70"));
        colorList.add(Color.parseColor("#ff1493"));
        colorList.add(Color.parseColor("#97ffff"));
        colorList.add(Color.parseColor("#c0fff4"));
        colorList.add(Color.parseColor("#b2b2b2"));
        colorList.add(Color.parseColor("#99ffcc"));
        colorList.add(Color.parseColor("#210333"));
        colorList.add(Color.parseColor("#a183b3"));
        colorList.add(Color.parseColor("#428fb9"));
        colorList.add(Color.parseColor("#487995"));
        colorList.add(Color.parseColor("#0c6483"));
        colorList.add(Color.parseColor("#45b9d3"));
        colorList.add(Color.parseColor("#10d6a2"));
        colorList.add(Color.parseColor("#ff545e"));
        colorList.add(Color.parseColor("#57bb83"));
        colorList.add(Color.parseColor("#dbeeff"));
        colorList.add(Color.parseColor("#ba5796"));
        colorList.add(Color.parseColor("#bb349b"));
        colorList.add(Color.parseColor("#6e557c"));
        colorList.add(Color.parseColor("#5e40b2"));
        colorList.add(Color.parseColor("#8051cf"));
        colorList.add(Color.parseColor("#895adc"));


        return colorList;
    }
    public interface ColorAdapterListener{
        void onColorSelected(int color);
    }
}
