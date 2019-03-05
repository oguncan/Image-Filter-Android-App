package com.example.joousope.first.Interface;

public interface EditImageFragmentListener {
    void onBrightnessChanged(int brightness);
    void onSaturationChanged(float saturation);
    void onConstrantChanged(float contrast);
    void onEditStarted();
    void onEditCompleted();
}
