package com.example.joousope.first.Fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.joousope.first.Adapter.EmojiAdapter;
import com.example.joousope.first.Interface.EmojiFragmentListener;
import com.example.joousope.first.R;

import ja.burhanrashid52.photoeditor.PhotoEditor;


public class EmojiFragment extends BottomSheetDialogFragment implements EmojiAdapter.EmojiAdapterListener {

    RecyclerView recyclerEmoji;
    static EmojiFragment instance;
    EmojiFragmentListener emojiFragmentListener;

    public void setEmojiFragmentListener(EmojiFragmentListener emojiFragmentListener) {
        this.emojiFragmentListener = emojiFragmentListener;
    }

    public static EmojiFragment getInstance()
    {
        if(instance==null)
        {
            instance=new EmojiFragment();
        }
        return instance;
    }
    public EmojiFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View itemView= inflater.inflate(R.layout.fragment_emoji, container, false);
        recyclerEmoji=(RecyclerView)itemView.findViewById(R.id.recycler_emoji);
        recyclerEmoji.setHasFixedSize(true);
        recyclerEmoji.setLayoutManager(new GridLayoutManager(getActivity(),5));
        EmojiAdapter adapter=new EmojiAdapter(getContext(), PhotoEditor.getEmojis(getContext()),this);
        recyclerEmoji.setAdapter(adapter);
        return itemView;
    }
    @Override
    public void onEmojiItemSelected(String emoji) {
        emojiFragmentListener.onEmojiSelected(emoji);
    }

    public interface EmojiFragmentListener {
        void onEmojiSelected(String emoji);
    }
}
