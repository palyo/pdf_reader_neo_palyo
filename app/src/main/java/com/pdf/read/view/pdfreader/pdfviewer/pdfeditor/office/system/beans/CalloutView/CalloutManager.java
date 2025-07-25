package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.system.beans.CalloutView;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.ext.KeyKt;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.common.PaintKit;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.system.IControl;

public class CalloutManager {
    private int alpha = 0xFF;
    private int color = Color.RED;
    private int width = 10;
    private int mode = KeyKt.DRAW_MODE_NORMAL;
    private IControl control;
    private HashMap<Integer, List<PathInfo>> mPathMap;

    public CalloutManager(IControl control) {
        this.control = control;
        mPathMap = new HashMap<Integer, List<PathInfo>>();
    }

    public void drawPath(Canvas canvas, int index, float zoom) {
        canvas.scale(zoom, zoom);
        List<PathInfo> pathList = mPathMap.get(index);
        Paint paint = PaintKit.instance().getPaint();
        if (pathList != null) {
            for (int i = 0; i < pathList.size(); i++) {
                PathInfo pathInfo = pathList.get(i);
                paint.setStrokeWidth(pathInfo.width);
                paint.setColor(pathInfo.color);
                canvas.drawPath(pathInfo.path, paint);
            }
        }
    }

    public boolean isPathEmpty() {
        return mPathMap.size() == 0;
    }

    public boolean isPathEmpty(int index) {
        return mPathMap.get(index) == null;
    }

    public List<PathInfo> getPath(int index, boolean assignPath) {
        if (assignPath && mPathMap.get(index) == null) {
            mPathMap.put(index, new ArrayList<PathInfo>());
        }
        return mPathMap.get(index);
    }

    public int getAlpha() {
        return alpha;
    }

    public void setAlpha(int alpha) {
        this.alpha = alpha;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getDrawingMode() {
        return mode;
    }

    public void setDrawingMode(int mode) {
        if (mode < KeyKt.DRAW_MODE_NORMAL || mode > KeyKt.DRAW_MODE_CALL_OUT_ERASE) {
            return;
        }
        this.mode = mode;
    }

    public void dispose() {
        mPathMap.clear();
        mPathMap = null;
        control = null;
    }
}
