package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.common.pictureefftect;

public class PictureEffectUtil {
    public static float[] getBrightnessArray(int brightness) {
        return new float[]
                {1, 0, 0, 0, brightness,
                        0, 1, 0, 0, brightness,
                        0, 0, 1, 0, brightness,
                        0, 0, 0, 1, 0};
    }

    public static float[] getReverseColorArray() {
        return new float[]
                {-1, 0, 0, 0, 255,
                        0, -1, 0, 0, 255,
                        0, 0, -1, 0, 255,
                        0, 0, 0, 1, 0};
    }

    public static float[] getGrayScaleArray() {
        return new float[]
                {0.3086f, 0.6094f, 0.0820f, 0, 0,
                        0.3086f, 0.6094f, 0.0820f, 0, 0,
                        0.3086f, 0.6094f, 0.0820f, 0, 0,
                        0, 0, 0, 1, 0};
    }

    public static float[] getSaturationArray(float sat) {
        return new float[]
                {
                        0.3086f * (1 - sat) + sat, 0.6094f * (1 - sat), 0.0820f * (1 - sat), 0, 0,
                        0.3086f * (1 - sat), 0.6094f * (1 - sat) + sat, 0.0820f * (1 - sat), 0, 0,
                        0.3086f * (1 - sat), 0.6094f * (1 - sat), 0.0820f * (1 - sat) + sat, 0, 0,
                        0, 0, 0, 1, 0
                };
    }

    public static float[] getContrastArray(float contrast) {
        return new float[]
                {
                        contrast, 0, 0, 0, 128 * (1 - contrast),
                        0f, contrast, 0, 0, 128 * (1 - contrast),
                        0f, 0, contrast, 0, 128 * (1 - contrast),
                        0f, 0, 0, 1, 0
                };
    }

    public static float[] getBrightAndContrastArray(float bright, float contrast) {
        return new float[]
                {
                        contrast, 0, 0, 0, bright,
                        0f, contrast, 0, 0, bright,
                        0f, 0, contrast, 0, bright,
                        0f, 0, 0, 1, 0
                };
    }

    public static float[] getBlackWhiteArray(float threshode) {
        return new float[]
                {
                        0.3086f * 256, 0.6094f * 256, 0.0820f * 256, 0, -256 * threshode,
                        0.3086f * 256, 0.6094f * 256, 0.0820f * 256, 0, -256 * threshode,
                        0.3086f * 256, 0.6094f * 256, 0.0820f * 256, 0, -256 * threshode,
                        0, 0, 0, 1, 0
                };
    }
}
