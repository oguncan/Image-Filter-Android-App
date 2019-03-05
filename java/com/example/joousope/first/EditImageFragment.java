package com.example.joousope.first;


import android.os.Bundle;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;

import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 */
public class EditImageFragment extends BottomSheetDialogFragment implements SeekBar.OnSeekBarChangeListener {
    //private FiltersListFragmentListener listener2;
    private EditImageFragmentListener listener;
    SeekBar seekbar_brightness,seekbar_contrast,seekbar_saturation;
    static EditImageFragment instance;
    public static EditImageFragment getInstance() {
        if(instance==null)
            instance=new EditImageFragment();
        return instance;
        // Required empty public constructor
    }
    public void setListener(EditImageFragmentListener listener) {
        this.listener = listener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View itemView = inflater.inflate(R.layout.fragment_edit_image, container, false);
        ButterKnife.bind(this, itemView);
        seekbar_brightness=(SeekBar)itemView.findViewById(R.id.seekbar_brightness);
        seekbar_contrast=(SeekBar)itemView.findViewById(R.id.seekbar_contrast);
        seekbar_saturation=(SeekBar)itemView.findViewById(R.id.seekbar_saturation);
        seekbar_brightness.setMax(200);
        seekbar_brightness.setProgress(100);
        seekbar_contrast.setMax(20);
        seekbar_contrast.setProgress(0);
        seekbar_saturation.setMax(30);
        seekbar_saturation.setProgress(10);
        seekbar_saturation.setOnSeekBarChangeListener(this);
        seekbar_brightness.setOnSeekBarChangeListener(this);
        seekbar_contrast.setOnSeekBarChangeListener(this);
        return itemView;
    }
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if(listener!=null)
        {
            if(seekBar.getId()==R.id.seekbar_brightness){
                listener.onBrightnessChanged(progress-100);
            }
            else if(seekBar.getId()==R.id.seekbar_contrast)
            {
                progress+=10;
                float value=.10f*progress;
                listener.onConstrantChanged((int) value);
            }
            else if(seekBar.getId()==R.id.seekbar_saturation)
            {
                float value=.10f*progress;
                listener.onSaturationChanged((int) value);
            }
        }
    }
    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        if(listener!=null)
        {
            listener.onEditStarted();
        }
    }
    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        if(listener!=null)
        {
            listener.onEditCompleted();
        }
    }
    public void resetControls()
    {
        seekbar_brightness.setProgress(100);
        seekbar_contrast.setProgress(0);
        seekbar_saturation.setProgress(10);
    }

    public interface EditImageFragmentListener {
        void onBrightnessChanged(int brightness);

        void onSaturationChanged(float saturation);

        void onEditStarted();

        void onEditCompleted();

        void onConstrantChanged(float value);
    }

}
