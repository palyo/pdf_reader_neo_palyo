package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.pg.model;

public class PGPlaceholderUtil {
    public static final String TITLE = "title";
    public static final String CTRTITLE = "ctrTitle";
    public static final String SUBTITLE = "subTitle";
    public static final String BODY = "body";
    public static final String HALF = "half";
    public static final String DT = "dt";
    public static final String FTR = "ftr";
    public static final String SLDNUM = "sldNum";
    public static final String HEADER = "hdr";
    public static final String OBJECT = "obj";
    public static final String CHART = "chart";
    public static final String TABLE = "tbl";
    public static final String CLIPART = "clipArt";
    public static final String DIAGRAM = "dgm";
    public static final String MEDIA = "media";
    public static final String SLIDEIMAGE = "sldImg";
    public static final String PICTURE = "pic";

    private static final PGPlaceholderUtil kit = new PGPlaceholderUtil();

    public static PGPlaceholderUtil instance() {
        return kit;
    }

    public boolean isTitle(String type) {
        return TITLE.equals(type) || CTRTITLE.equals(type);
    }

    public boolean isBody(String type) {
        return !TITLE.equals(type) && !CTRTITLE.equals(type) && !DT.equals(type)
                && !FTR.equals(type) && !SLDNUM.equals(type);
    }

    public boolean isTitleOrBody(String type) {
        return TITLE.equals(type) || CTRTITLE.equals(type) || SUBTITLE.equals(type)
                || BODY.equals(type);
    }

    public boolean isHeaderFooter(String type) {
        return DT.equals(type) || FTR.equals(type) || SLDNUM.equals(type);
    }

    public boolean isDate(String type) {
        return DT.equals(type);
    }

    public boolean isFooter(String type) {
        return FTR.equals(type);
    }

    public boolean isSldNum(String type) {
        return SLDNUM.equals(type);
    }

    public String checkTypeName(String type) {
        if (PGPlaceholderUtil.CTRTITLE.equals(type)) {
            type = PGPlaceholderUtil.TITLE;
        }
        return type;
    }

    public String processType(String name, String type) {

        return type;
    }
}
