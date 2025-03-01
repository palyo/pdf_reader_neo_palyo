package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.system;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.net.Uri;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.common.bookmark.BookmarkManage;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.common.borders.BordersManage;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.common.bulletnumber.ListManage;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.common.hyperlink.HyperlinkManage;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.common.picture.PictureManage;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.pg.animate.AnimationManager;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.pg.model.PGBulletText;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.system.beans.CalloutView.CalloutManager;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.wp.control.WPShapeManage;

public class SysKit {
    private static ShapeDrawable pageNumberDrawable;
    private ErrorUtil errorKit;
    private PictureManage pmKit;
    private HyperlinkManage hmKit;
    private ListManage lmKit;
    private PGBulletText pgLMKit;
    private BordersManage brKit;
    private WPShapeManage wpSMKit;
    private BookmarkManage bmKit;
    private IControl control;
    private AnimationManager animationMgr;
    private CalloutManager calloutMgr;

    public SysKit(IControl control) {
        this.control = control;
    }

    public static Drawable getPageNubmerDrawable() {
        if (pageNumberDrawable == null) {
            pageNumberDrawable = new ShapeDrawable(new RoundRectShape(new float[]{6, 6, 6, 6,
                    6, 6, 6, 6}, null, null));
            pageNumberDrawable.getPaint().setColor(0x88FF8844);
        }
        return pageNumberDrawable;
    }

    public static boolean isValidateRect(int pageWidth, int pageHeight, int x, int y, int width, int height) {
        return x >= 0 && y >= 0 && x < pageWidth && y < pageHeight
                && width >= 0 && height >= 0 && x + width <= pageWidth && y + height <= pageHeight;
    }

    public String charsetEncode(String str, String encode) {
        if ("".equals(str)) {
            return "";
        }

        StringBuffer strBuff = new StringBuffer();
        try {
            byte[] b = str.getBytes(encode);
            for (int n = 0; n < b.length; n++) {
                str = (Integer.toHexString(b[n] & 0XFF));
                if (str.length() == 1) {
                    strBuff.append("0").append(str);
                } else {
                    strBuff.append(str);
                }
            }
            char[] chs = strBuff.toString().toCharArray();
            strBuff.delete(0, strBuff.length());
            for (int i = 0; i < chs.length; i = i + 2) {
                strBuff.append("%").append(chs[i]).append(chs[i + 1]);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return strBuff.toString();
    }

    public void internetSearch(String str, Activity activity) {
        str = charsetEncode(str, "utf-8");

        String url = "http://www.google.com.hk/#hl=en&newwindow=1&safe=strict&site=&q=a-a-a-a&oq=a-a-a-a&aq=f&aqi=&aql=&gs_sm=3" +
                "&gs_upl=1075l1602l0l1935l3l3l0l0l0l0l0l0ll0l0&gs_l=hp.3...1075l1602l0l1935l3l3l0l0l0l0l0l0ll0l0&bav=on.2,or.r_gc.r_pw.,cf.osb" +
                "&fp=207f1fbbc21b7536&biw=1280&bih=876";
        url = url.replaceAll("a-a-a-a", str);

        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        activity.startActivity(intent);
    }

    public boolean isDebug() {
        return false;
    }

    public IControl getControl() {
        return control;
    }

    public void setControl(IControl control) {
        this.control = control;
    }

    public ErrorUtil getErrorKit() {
        if (errorKit == null) {
            errorKit = new ErrorUtil(this);
        }
        return errorKit;
    }

    public PictureManage getPictureManage() {
        if (pmKit == null) {
            pmKit = new PictureManage(control);
        }
        return pmKit;
    }

    public HyperlinkManage getHyperlinkManage() {
        if (hmKit == null) {
            hmKit = new HyperlinkManage();
        }
        return hmKit;
    }

    public ListManage getListManage() {
        if (lmKit == null) {
            lmKit = new ListManage();
        }
        return lmKit;
    }

    public PGBulletText getPGBulletText() {
        if (pgLMKit == null) {
            pgLMKit = new PGBulletText();
        }
        return pgLMKit;
    }

    public BordersManage getBordersManage() {
        if (brKit == null) {
            brKit = new BordersManage();
        }
        return brKit;
    }

    public WPShapeManage getWPShapeManage() {
        if (wpSMKit == null) {
            wpSMKit = new WPShapeManage();
        }
        return wpSMKit;
    }

    public BookmarkManage getBookmarkManage() {
        if (bmKit == null) {
            bmKit = new BookmarkManage();
        }
        return bmKit;
    }

    public AnimationManager getAnimationManager() {
        if (animationMgr == null) {
            animationMgr = new AnimationManager(control);
        }

        return animationMgr;
    }

    public CalloutManager getCalloutManager() {
        if (calloutMgr == null) {
            calloutMgr = new CalloutManager(control);
        }

        return calloutMgr;
    }

    public void dispose() {
        control = null;
        if (errorKit != null) {
            errorKit.dispose();
            errorKit = null;
        }
        if (pmKit != null) {
            pmKit.dispose();
            pmKit = null;
        }
        if (hmKit != null) {
            hmKit.dispose();
            hmKit = null;
        }
        if (lmKit != null) {
            lmKit.dispose();
            lmKit = null;
        }
        if (pgLMKit != null) {
            pgLMKit.dispose();
            pgLMKit = null;
        }
        if (brKit != null) {
            brKit.dispose();
            brKit = null;
        }
        if (wpSMKit != null) {
            wpSMKit.dispose();
            wpSMKit = null;
        }
        if (bmKit != null) {
            bmKit.dispose();
            bmKit = null;
        }

        if (animationMgr != null) {
            animationMgr.dispose();
            animationMgr = null;
        }
        if (calloutMgr != null) {
            calloutMgr.dispose();
            calloutMgr = null;
        }
    }

}
