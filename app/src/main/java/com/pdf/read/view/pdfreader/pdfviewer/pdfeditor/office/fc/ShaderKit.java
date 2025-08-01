package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc;

import java.util.List;
import java.util.Map;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.ext.KeyKt;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.common.autoshape.AutoShapeDataKit;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.common.bg.BackgroundAndFill;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.common.bg.Gradient;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.common.bg.LinearGradientShader;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.common.bg.RadialGradientShader;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.common.bg.TileShader;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.common.picture.Picture;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.dom4j.Element;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.ppt.reader.ReaderKit;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.pg.model.PGMaster;

public class ShaderKit {
    public static Gradient readGradient(PGMaster master, Element gradFill) {
        Element gsLstElement = gradFill.element("gsLst");

        List<Element> gsLst = gsLstElement.elements("gs");
        if (gsLst == null || gsLst.size() == 0) {
            return null;
        }

        int gsCnt = gsLst.size();
        int[] colors = new int[gsCnt];
        float[] positions = new float[gsCnt];
        for (int i = 0; i < gsCnt; i++) {
            Element gs = gsLst.get(i);
            String pos = gs.attributeValue("pos");
            positions[i] = Integer.parseInt(pos) / 100000.f;
            colors[i] = ReaderKit.instance().getColor(master, gs);
        }

        Element ele = gradFill.element("lin");
        if (ele != null) {

            float angle = Integer.parseInt(ele.attributeValue("ang")) / 60000;
            return new LinearGradientShader(angle, colors, positions);
        } else if ((ele = gradFill.element("path")) != null) {
            int type = getGradientType(gradFill);
            int radialCenterType = getRadialCenterType(ele.element("fillToRect"));
            if (type == BackgroundAndFill.FILL_SHADE_RADIAL
                    || type == BackgroundAndFill.FILL_SHADE_RECT
                    || type == BackgroundAndFill.FILL_SHADE_SHAPE) {
                return new RadialGradientShader(radialCenterType, colors, positions);
            }
        }

        return null;
    }

    public static Gradient readGradient(Map<String, Integer> schemeColor, Element gradFill) {
        Element gsLstElement = gradFill.element("gsLst");

        List<Element> gsLst = gsLstElement.elements("gs");
        if (gsLst == null || gsLst.size() == 0) {
            return null;
        }

        int gsCnt = gsLst.size();
        int[] colors = new int[gsCnt];
        float[] positions = new float[gsCnt];
        for (int i = 0; i < gsCnt; i++) {
            Element gs = gsLst.get(i);
            String pos = gs.attributeValue("pos");
            positions[i] = Integer.parseInt(pos) / 100000.f;
            colors[i] = AutoShapeDataKit.getColor(schemeColor, gs);
        }

        Element ele = gradFill.element("lin");
        if (ele != null) {

            float angle = Integer.parseInt(ele.attributeValue("ang")) / 60000;
            return new LinearGradientShader(angle, colors, positions);
        } else if ((ele = gradFill.element("path")) != null) {
            int type = getGradientType(gradFill);
            int radialCenterType = getRadialCenterType(ele.element("fillToRect"));
            if (type == BackgroundAndFill.FILL_SHADE_RADIAL
                    || type == BackgroundAndFill.FILL_SHADE_RECT
                    || type == BackgroundAndFill.FILL_SHADE_SHAPE) {
                return new RadialGradientShader(radialCenterType, colors, positions);
            }
        } else {
            return new LinearGradientShader(270, colors, positions);
        }

        return null;
    }

    public static byte getGradientType(Element gradFill) {
        Element ele = gradFill.element("lin");
        if (ele != null) {
            return BackgroundAndFill.FILL_SHADE_LINEAR;
        } else if ((ele = gradFill.element("path")) != null) {
            String path = ele.attributeValue("path");
            if ("circle".equalsIgnoreCase(path)) {
                return BackgroundAndFill.FILL_SHADE_RADIAL;
            } else if ("rect".equalsIgnoreCase(path)) {
                return BackgroundAndFill.FILL_SHADE_RECT;
            } else if ("shape".equalsIgnoreCase(path)) {
                return BackgroundAndFill.FILL_SHADE_SHAPE;
            }
        }

        return BackgroundAndFill.FILL_SHADE_LINEAR;
    }

    private static int getRadialCenterType(Element fillToRect) {
        if (fillToRect != null) {
            String l = fillToRect.attributeValue("l");
            String t = fillToRect.attributeValue("t");
            String r = fillToRect.attributeValue("r");
            String b = fillToRect.attributeValue("b");

            if ("100000".equalsIgnoreCase(r) && "100000".equalsIgnoreCase(b)) {
                return RadialGradientShader.Center_TL;
            } else if ("100000".equalsIgnoreCase(l) && "100000".equalsIgnoreCase(b)) {
                return RadialGradientShader.Center_TR;
            }
            if ("100000".equalsIgnoreCase(r) && "100000".equalsIgnoreCase(t)) {
                return RadialGradientShader.Center_BL;
            } else if ("100000".equalsIgnoreCase(l) && "100000".equalsIgnoreCase(t)) {
                return RadialGradientShader.Center_BR;
            } else if ("50000".equalsIgnoreCase(l) && "50000".equalsIgnoreCase(t)
                    && "50000".equalsIgnoreCase(r) && "50000".equalsIgnoreCase(b)) {
                return RadialGradientShader.Center_Center;
            }
        }

        return RadialGradientShader.Center_TL;
    }

    public static TileShader readTile(Picture picture, Element tile) {
        int flip = getFlipType(tile.attributeValue("flip"));
        float horiRatio = Integer.parseInt(tile.attributeValue("sx")) / 100000.f;
        float vertRatio = Integer.parseInt(tile.attributeValue("sy")) / 100000.f;
        int offsetX = Math.round(Integer.parseInt(tile.attributeValue("tx")) * KeyKt.PIXEL_DPI / KeyKt.EMU_PER_INCH);
        int offsetY = Math.round(Integer.parseInt(tile.attributeValue("ty")) * KeyKt.PIXEL_DPI / KeyKt.EMU_PER_INCH);
        return new TileShader(picture, flip, horiRatio, vertRatio, offsetX, offsetY);
    }

    private static int getFlipType(String flip) {
        if ("x".equalsIgnoreCase(flip)) {
            return TileShader.Flip_Horizontal;
        } else if ("y".equalsIgnoreCase(flip)) {
            return TileShader.Flip_Vertical;
        } else if ("xy".equalsIgnoreCase(flip)) {
            return TileShader.Flip_Both;
        }

        return TileShader.Flip_None;
    }
}
