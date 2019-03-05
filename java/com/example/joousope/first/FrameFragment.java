package com.example.joousope.first;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.joousope.first.Adapter.FrameAdapter;
import com.example.joousope.first.Interface.AddFrameListener;


public class FrameFragment extends BottomSheetDialogFragment implements AddFrameListener, FrameAdapter.FrameAdapterListener {
    RecyclerView frameRecyclerView;
    Button btn_add_frame;
    static FrameFragment instance;
    AddFrameListener listener;
    int frame_selected=-1;

    public void setListener(AddFrameListener listener) {
        this.listener = listener;
    }
    public FrameFragment(){}
    public static FrameFragment getInstance() {
        if(instance==null)
        {
            instance=new FrameFragment();
        }
        return instance;

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View itemView= inflater.inflate(R.layout.fragment_frame, container, false);
        frameRecyclerView=(RecyclerView)itemView.findViewById(R.id.recycler_frame);
        btn_add_frame=(Button)itemView.findViewById(R.id.btn_add_frame);
        frameRecyclerView.setHasFixedSize(true);
        frameRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));
        frameRecyclerView.setAdapter(new FrameAdapter(getContext(),this));
        btn_add_frame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onAddFrame(frame_selected);
            }
        });
        return itemView;
    }


    @Override
    public void onFrameSelected(int frame) {
        frame_selected=frame;

    }

    @Override
    public void onAddFrame(int frame) {

    }
}
