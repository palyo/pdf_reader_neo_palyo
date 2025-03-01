package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.common;

import android.graphics.Paint;
import android.graphics.Typeface;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.constant.SSConstant;

public class PaintKit {
    private static final PaintKit pk = new PaintKit();
    private Paint paint = null;

    private PaintKit() {
        paint = new Paint();
        paint.setTextSize(SSConstant.HEADER_TEXT_FONTSZIE);
        paint.setTypeface(Typeface.SERIF);
        paint.setFlags(Paint.ANTI_ALIAS_FLAG);
        paint.setStrokeCap(Paint.Cap.ROUND);
    }

    public static PaintKit instance() {
        return pk;
    }

    public Paint getPaint() {
        paint.reset();
        paint.setAntiAlias(true);

        return paint;
    }
}
