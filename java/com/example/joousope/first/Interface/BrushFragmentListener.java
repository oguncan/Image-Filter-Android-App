package com.example.joousope.first.Interface;

public interface BrushFragmentListener {
    void onBrushSizeChangedListener(float size);
    void onBrushOpacityChangedListener(int size);
    void onBrushColorChangedListener(int color);
    void onBrushStateChangedListener(boolean isEraser);
}
