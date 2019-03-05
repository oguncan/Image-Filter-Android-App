package com.example.joousope.first.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.joousope.first.Fragment.EmojiFragment;
import com.example.joousope.first.R;

import java.util.ArrayList;
import java.util.List;

import io.github.rockerhieu.emojicon.EmojiconTextView;

public class EmojiAdapter extends RecyclerView.Adapter<EmojiAdapter.EmojiViewHolder> {
    Context context;
    EmojiAdapterListener emojiAdapterListener;
    List<String> emojiList;

    public EmojiAdapter(Context context, ArrayList<String> emojis, EmojiFragment emojiFragment) {
        this.context=context;
        this.emojiList=emojis;
        this.emojiAdapterListener=emojiFragment;
    }

    @NonNull
    @Override
    public EmojiViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView= LayoutInflater.from(context).inflate(R.layout.emoji_item,viewGroup,false);
        return new EmojiViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull EmojiViewHolder emojiViewHolder, int i) {
        emojiViewHolder.emojiconTextView.setText(emojiList.get(i));
    }

    @Override
    public int getItemCount() {
        return emojiList.size();
    }

    public class EmojiViewHolder extends RecyclerView.ViewHolder {
        EmojiconTextView emojiconTextView;
        public EmojiViewHolder(@NonNull View itemView) {
            super(itemView);
            emojiconTextView=(EmojiconTextView)itemView.findViewById(R.id.emoji_text_view);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    emojiAdapterListener.onEmojiItemSelected(emojiList.get(getAdapterPosition()));
                }
            });
        }
    }
    public interface EmojiAdapterListener{
        void onEmojiItemSelected(String emoji);
    }
}
