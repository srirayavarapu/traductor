package com.transjan.traductor.camera;

import android.util.Log;
import android.util.SparseArray;

import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.text.TextBlock;
import com.transjan.traductor.camera.ui.GraphicOverlay;

public class RawCameraDetectorProcessor implements Detector.Processor<TextBlock> {

    private GraphicOverlay<RawCameraGraphic> graphicOverlay;

    RawCameraDetectorProcessor(GraphicOverlay<RawCameraGraphic> ocrGraphicOverlay) {
        graphicOverlay = ocrGraphicOverlay;
    }

    @Override
    public void release() {
        graphicOverlay.clear();
    }

    @Override
    public void receiveDetections(Detector.Detections<TextBlock> detections) {
        graphicOverlay.clear();
        SparseArray<TextBlock> items = detections.getDetectedItems();
        for (int i = 0; i < items.size(); ++i) {
            TextBlock item = items.valueAt(i);
            if (item != null && item.getValue() != null) {
                Log.d("Processor", "Text detected! " + item.getValue());
                RawCameraGraphic graphic = new RawCameraGraphic(graphicOverlay, item);
                graphicOverlay.add(graphic);
            }
        }
    }
}
