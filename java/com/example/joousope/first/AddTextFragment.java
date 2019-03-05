package com.example.joousope.first;

import android.content.Context;
import android.graphics.Color;
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
import android.widget.EditText;

import com.example.joousope.first.Adapter.ColorAdapter;
import com.example.joousope.first.Interface.AddTextFragmentListener;

public class AddTextFragment extends BottomSheetDialogFragment implements AddTextFragmentListener,ColorAdapter.ColorAdapterListener {

    int colorSelected=Color.parseColor("#000000"); //Varsayılan
    AddTextFragmentListener addTextFragmentListener;
    EditText edit_add_text;
    RecyclerView recyclerColor;
    Button button_done;
    ColorAdapter colorAdapter;
    static AddTextFragment instance;
    public void setAddTextFragmentListener(AddTextFragmentListener addTextFragmentListener) {
        this.addTextFragmentListener = addTextFragmentListener;
    }


    public AddTextFragment(){

    }
    @Override
    public void onColorSelected(int color) {
        colorSelected=color;
    }
    public static AddTextFragment getInstance() {
        if(instance==null)
            instance=new AddTextFragment();
        return instance;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View itemView= inflater.inflate(R.layout.fragment_add_text, container, false);
        button_done=(Button)itemView.findViewById(R.id.btn_add_text);
        recyclerColor=(RecyclerView)itemView.findViewById(R.id.recycler_color);
        edit_add_text=(EditText)itemView.findViewById(R.id.edt_add_text);
        recyclerColor.setHasFixedSize(true);
        recyclerColor.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false));
        colorAdapter=new ColorAdapter(getContext(),this);
        recyclerColor.setAdapter(colorAdapter);
        button_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addTextFragmentListener.onAddTextButtonClick(edit_add_text.getText().toString(),colorSelected);
            }
        });
        return itemView;
    }


    @Override
    public void onAddTextButtonClick(String text, int color) {

    }
}
