package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.common.shape;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.common.picture.Picture;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.common.pictureefftect.PictureEffectInfo;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.system.IControl;

public class PictureShape extends AbstractShape {
    private int pictureIndex;
    private short zoomX;
    private short zoomY;
    private PictureEffectInfo effectInfor;

    public static Picture getPicture(IControl control, int pictureIndex) {
        if (control == null) {
            return null;
        }
        return control.getSysKit().getPictureManage().getPicture(pictureIndex);
    }

    public short getType() {
        return SHAPE_PICTURE;
    }

    public int getPictureIndex() {
        return pictureIndex;
    }

    public void setPictureIndex(int pictureIndex) {
        this.pictureIndex = pictureIndex;
    }

    public Picture getPicture(IControl control) {
        if (control == null) {
            return null;
        }
        return control.getSysKit().getPictureManage().getPicture(pictureIndex);
    }

    public void setZoomX(short zoomX) {
        this.zoomX = zoomX;
    }

    public void setZoomY(short zoomY) {
        this.zoomY = zoomY;
    }

    public PictureEffectInfo getPictureEffectInfor() {
        return effectInfor;
    }

    public void setPictureEffectInfor(PictureEffectInfo effectInfor) {
        this.effectInfor = effectInfor;
    }

    public void dispose() {
        super.dispose();
    }
}
