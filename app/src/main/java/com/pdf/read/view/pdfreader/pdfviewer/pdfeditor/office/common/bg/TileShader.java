package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.common.bg;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.BitmapShader;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.Shader.TileMode;

import java.io.FileInputStream;
import java.io.InputStream;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.common.picture.Picture;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.system.IControl;

public class TileShader extends AShader {
    public static final int Flip_None = 0;
    public static final int Flip_Horizontal = 1;
    public static final int Flip_Vertical = 2;
    public static final int Flip_Both = 3;
    private final Picture picture;
    private final int flip;
    private final float horiRatio;
    private final float vertRatio;
    private int offsetX;
    private int offsetY;

    public TileShader(Picture picture, int flip, float horiRatio, float vertRatio) {
        this.picture = picture;
        this.flip = flip;
        this.horiRatio = horiRatio;
        this.vertRatio = vertRatio;
    }

    public TileShader(Picture picture, int flip, float horiRatio, float vertRatio, int offsetX, int offsetY) {
        this(picture, flip, horiRatio, vertRatio);
        this.offsetX = offsetX;
        this.offsetY = offsetY;
    }

    public static Bitmap getBitmap(IControl control, int viewIndex, Picture picture, Rect rect, Options options) {
        try {
            String path = picture.getTempFilePath();
            Bitmap sBitmap = control.getSysKit().getPictureManage().getBitmap(path);
            if (sBitmap == null) {
                byte imageType = picture.getPictureType();
                if (imageType == Picture.WMF || imageType == Picture.EMF) {
                    String dst = control.getSysKit().getPictureManage().convertVectorgraphToPng(viewIndex, imageType, path, rect.width(), rect.height(), true);
                    InputStream in = new FileInputStream(dst);
                    sBitmap = BitmapFactory.decodeStream(in, null, options);
                } else {
                    InputStream in = new FileInputStream(path);
                    sBitmap = BitmapFactory.decodeStream(in, null, options);
                }
                if (sBitmap == null) {
                    return null;
                }
                control.getSysKit().getPictureManage().addBitmap(path, sBitmap);
            }

            return sBitmap;
        } catch (OutOfMemoryError e) {
            if (control.getSysKit().getPictureManage().hasBitmap()) {
                control.getSysKit().getPictureManage().clearBitmap();
                return getBitmap(control, viewIndex, picture, rect, options);
            } else {
                if (options == null) {
                    options = new Options();
                    options.inSampleSize = 2;
                } else {
                    options.inSampleSize *= 2;
                }
                return getBitmap(control, viewIndex, picture, rect, options);
            }
        } catch (Exception e) {
            return null;
        }
    }

    public Shader createShader(IControl control, int viewIndex, Rect rect) {
        try {
            Bitmap bmp = getBitmap(control, viewIndex, picture, rect, null);
            int width = bmp.getWidth();
            int height = bmp.getHeight();
            bmp = Bitmap.createScaledBitmap(bmp, Math.round(width * horiRatio), Math.round(height * vertRatio), true);
            TileMode tileX = TileMode.REPEAT;
            TileMode tileY = TileMode.REPEAT;
            switch (flip) {
                case Flip_Horizontal:
                    tileX = TileMode.MIRROR;
                case Flip_Vertical:
                    tileY = TileMode.MIRROR;
                case Flip_Both:
                    tileX = TileMode.MIRROR;
                    tileY = TileMode.MIRROR;
            }
            shader = new BitmapShader(bmp, tileX, tileY);
            return shader;
        } catch (Exception e) {
            return null;
        }
    }

    public int getOffsetX() {
        return offsetX;
    }

    public int getOffsetY() {
        return offsetY;
    }
}
