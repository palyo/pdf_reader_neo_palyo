package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.common.shape;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.common.pictureefftect.PictureEffectInfo;

public class WatermarkShape extends WPAutoShape {
    public final static byte Watermark_Text = 0;
    public final static byte Watermark_Picture = 1;
    private final float OPACITY = 0.2f;
    private byte watermarkType;
    private String watermartString;
    private boolean isAutoFontSize = false;
    private int fontSize = 36;
    private int fontColor = 0xFF000000;
    private int pictureIndex = -1;
    private float blacklevel;
    private float gain;
    private PictureEffectInfo effect;
    private float opacity = OPACITY;

    public short getType() {
        if (watermarkType == Watermark_Text) {
            return SHAPE_AUTOSHAPE;
        } else {
            return SHAPE_PICTURE;
        }
    }

    public boolean isWatermarkShape() {
        return true;
    }

    public byte getWatermarkType() {
        return watermarkType;
    }

    public void setWatermarkType(byte type) {
        this.watermarkType = type;
    }

    public String getWatermartString() {
        return watermartString;
    }

    public void setWatermartString(String watermartString) {
        this.watermartString = watermartString;
    }

    public boolean isAutoFontSize() {
        return isAutoFontSize;
    }

    public void setAutoFontSize(boolean isAutoFontSize) {
        this.isAutoFontSize = isAutoFontSize;
    }

    public int getFontSize() {
        return fontSize;
    }

    public void setFontSize(int fontSize) {
        this.fontSize = fontSize;
    }

    public int getFontColor() {
        return fontColor;
    }

    public void setFontColor(int fontColor) {
        this.fontColor = fontColor;
    }

    public float getOpacity() {
        return opacity;
    }

    public void setOpacity(float opacity) {
        this.opacity = OPACITY * opacity;
    }

    public int getPictureIndex() {
        return pictureIndex;
    }

    public void setPictureIndex(int pictureIndex) {
        this.pictureIndex = pictureIndex;
    }

    public float getBlacklevel() {
        return blacklevel;
    }

    public void setBlacklevel(float blacklevel) {
        this.blacklevel = blacklevel;
    }

    public float getGain() {
        return gain;
    }

    public void setGain(float gain) {
        this.gain = gain;
    }

    public PictureEffectInfo getEffectInfor() {
        if (watermarkType == Watermark_Picture) {
            if (effect == null) {
                effect = new PictureEffectInfo();
                effect.setAlpha(Math.round(255 * opacity));
                effect.setBrightness(Math.round(255 * blacklevel));
            }

            return effect;
        }

        return null;
    }

    public void dispose() {
        watermartString = null;
        if (effect != null) {
            effect.dispose();
            effect = null;
        }
    }
}
