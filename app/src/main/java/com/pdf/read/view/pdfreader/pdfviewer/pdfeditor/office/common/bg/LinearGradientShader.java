package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.common.bg;

import android.graphics.LinearGradient;
import android.graphics.Rect;
import android.graphics.Shader;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.system.IControl;

public class LinearGradientShader extends Gradient {
    private final float angle;

    public LinearGradientShader(float angle, int[] colors, float[] positions) {
        super(colors, positions);
        this.angle = angle;
    }

    public int getAngle() {
        return (int) angle;
    }

    public Shader createShader(IControl control, int viewIndex, Rect rect) {
        try {
            int[] coordinate = getLinearGradientCoordinate();
            shader = new LinearGradient(coordinate[0], coordinate[1], coordinate[2], coordinate[3],
                    colors, positions, Shader.TileMode.MIRROR);
            return shader;
        } catch (Exception e) {
            return null;
        }

    }

    private int[] getLinearGradientCoordinate() {
        switch (Math.round((angle + 22) % 360 / 45)) {
            case 0:
                return new int[]{0, 0, COORDINATE_LENGTH, 0};
            case 1:
                return new int[]{0, 0, COORDINATE_LENGTH, COORDINATE_LENGTH};
            case 2:
                return new int[]{0, 0, 0, COORDINATE_LENGTH};
            case 3:
                return new int[]{COORDINATE_LENGTH, 0, 0, COORDINATE_LENGTH};
            case 4:
                return new int[]{COORDINATE_LENGTH, 0, 0, 0};
            case 5:
                return new int[]{COORDINATE_LENGTH, COORDINATE_LENGTH, 0, 0};
            case 6:
                return new int[]{0, COORDINATE_LENGTH, 0, 0};
            default:
                return new int[]{0, COORDINATE_LENGTH, COORDINATE_LENGTH, 0};
        }
    }
}
