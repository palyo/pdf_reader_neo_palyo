package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.pdf;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.common.IOfficeToPicture;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.constant.EventConstant;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.pdf.PDFLib;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.system.IControl;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.system.IFind;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.system.IMainFrame;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.system.SysKit;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.system.beans.pagelist.APageListItem;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.system.beans.pagelist.APageListView;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.system.beans.pagelist.IPageListViewListener;

public class PDFView extends FrameLayout implements IPageListViewListener {
    private int preShowPageIndex = -1;

    private IControl control;

    private PDFFind find;

    private PDFLib pdfLib;

    private APageListView listView;

    private Rect[] pagesSize;

    private Paint paint;

    private AsyncTask<Void, Object, Bitmap> exportTask;

    public PDFView(Context context) {
        super(context);
    }

    public PDFView(Context context, PDFLib pdfLib, IControl control) {
        super(context);
        this.control = control;
        this.pdfLib = pdfLib;

        listView = new APageListView(context, this);
        addView(listView, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        find = new PDFFind(this);

        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setTypeface(Typeface.SANS_SERIF);
        paint.setTextSize(24);

        if (!pdfLib.hasPasswordSync()) {
            pagesSize = pdfLib.getAllPagesSize();
        }
    }

    public void setBackgroundColor(int color) {
        super.setBackgroundColor(color);
        if (listView != null) {
            listView.setBackgroundColor(color);
        }
    }

    public void setBackgroundResource(int resid) {
        super.setBackgroundResource(resid);
        if (listView != null) {
            listView.setBackgroundResource(resid);
        }
    }

    public void setBackgroundDrawable(Drawable d) {
        super.setBackgroundDrawable(d);
        if (listView != null) {
            listView.setBackgroundDrawable(d);
        }
    }

    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        drawPageNubmer(canvas);
    }

    public void init() {
        if (pdfLib.hasPasswordSync()) {
            new PasswordDialog(control, pdfLib).show();
        }
    }

    public void setZoom(float zoom, int pointX, int pointY) {
        listView.setZoom(zoom, pointX, pointY);
    }

    public void setFitSize(int value) {
        listView.setFitSize(value);
    }

    public int getFitSizeState() {
        return listView.getFitSizeState();
    }

    public float getZoom() {
        return listView.getZoom();
    }

    public float getFitZoom() {
        return listView.getFitZoom();
    }

    public int getCurrentPageNumber() {
        return listView.getCurrentPageNumber();
    }

    public IFind getFind() {
        return this.find;
    }

    public PDFLib getPDFLib() {
        return this.pdfLib;
    }

    public APageListView getListView() {
        return this.listView;
    }

    public void nextPageView() {
        listView.nextPageView();
    }

    public void previousPageview() {
        listView.previousPageview();
    }

    public void showPDFPageForIndex(int index) {
        listView.showPDFPageForIndex(index);
    }

    public Bitmap pageToImage(int pageNumber) {
        if (pageNumber <= 0 || pageNumber > getPageCount()) {
            return null;
        }
        Rect rect = getPageSize(pageNumber - 1);
        Bitmap bitmap = Bitmap.createBitmap(rect.width(), rect.height(), Config.ARGB_8888);

        pdfLib.drawPageSync(bitmap, pageNumber - 1, rect.width(), rect.height(),
                0, 0, rect.width(), rect.height(), 1);

        return bitmap;
    }

    public Bitmap getThumbnail(int pageNumber, float zoom) {
        if (pageNumber <= 0 || pageNumber > getPageCount()) {
            return null;
        }
        Rect rect = getPageSize(pageNumber - 1);
        int w = (int) (rect.width() * zoom);
        int h = (int) (rect.height() * zoom);
        Bitmap bitmap = null;
        try {
            bitmap = Bitmap.createBitmap(w, h, Config.ARGB_8888);
            pdfLib.drawPageSync(bitmap, pageNumber - 1, w, h,
                    0, 0, w, h, 1);
        } catch (OutOfMemoryError e) {
            control.getSysKit().getErrorKit().writerLog(e);
        }
        return bitmap;
    }

    public Bitmap pageAreaToImage(int pageNumber, int srcLeft, int srcTop, int srcWidth, int srcHeight, int desWidth, int desHeight) {
        if (pageNumber <= 0 || pageNumber > getPageCount()) {
            return null;
        }

        Rect rect = getPageSize(pageNumber - 1);
        if (!SysKit.isValidateRect(rect.width(), rect.height(), srcLeft, srcTop, srcWidth, srcHeight)) {
            return null;
        }
        float paintZoom = Math.min(desWidth / (float) srcWidth, desHeight / (float) srcHeight);

        Bitmap bitmap = null;
        try {
            bitmap = Bitmap.createBitmap((int) (srcWidth * paintZoom), (int) (srcHeight * paintZoom), Config.ARGB_8888);
        } catch (OutOfMemoryError e) {
            return null;
        }
        if (bitmap == null) {
            return null;
        }
        pdfLib.drawPageSync(bitmap, pageNumber - 1, (int) (rect.width() * paintZoom), (int) (rect.height() * paintZoom),
                (int) (srcLeft * paintZoom), (int) (srcTop * paintZoom), (int) (srcWidth * paintZoom), (int) (srcHeight * paintZoom), 1);

        return bitmap;
    }

    public void passwordVerified() {
        if (listView != null) {
            pagesSize = pdfLib.getAllPagesSize();
            control.getMainFrame().openFileFinish();
            listView.init();

        }
    }

    public int getPageCount() {
        return pdfLib.getPageCountSync();
    }

    public APageListItem getPageListItem(int position, View convertView, ViewGroup parent) {
        Rect rect = getPageSize(position);
        return new PDFPageListItem(listView, control, rect.width(), rect.height());
    }

    public Rect getPageSize(int pageIndex) {
        if (pageIndex < 0 || pageIndex >= pagesSize.length) {
            return null;
        }
        return pagesSize[pageIndex];
    }

    public void exportImage(final APageListItem pageItem, final Bitmap srcBitmap) {
        if (getControl() == null || srcBitmap == null) {
            return;
        }
        if (find.isSetPointToVisible()) {
            find.setSetPointToVisible(false);
            RectF[] rectF = find.getSearchResult();
            if (rectF != null && rectF.length > 0) {
                if (!listView.isPointVisibleOnScreen((int) rectF[0].left, (int) rectF[0].top)) {
                    listView.setItemPointVisibleOnScreen((int) rectF[0].left, (int) rectF[0].top);
                    return;
                }
            }
        }
        if (exportTask != null) {
            exportTask.cancel(true);
            exportTask = null;
        }
        exportTask = new AsyncTask<Void, Object, Bitmap>() {
            private boolean isCancal;

            protected Bitmap doInBackground(Void... v) {
                if (control == null || pdfLib == null) {
                    return null;
                }
                try {
                    IOfficeToPicture otp = control.getOfficeToPicture();
                    if (otp != null && otp.getModeType() == IOfficeToPicture.VIEW_CHANGE_END) {
                        int rW = Math.min(getWidth(), srcBitmap.getWidth());
                        int rH = Math.min(getHeight(), srcBitmap.getHeight());
                        Bitmap dstBitmap = otp.getBitmap(rW, rH);
                        if (dstBitmap == null) {
                            return null;
                        }
                        Canvas canvas = new Canvas(dstBitmap);

                        int dx = 0;
                        int dy = 0;
                        int left = pageItem.getLeft();
                        int top = pageItem.getTop();
                        if (dstBitmap.getWidth() == rW && dstBitmap.getHeight() == rH) {
                            if (srcBitmap.getWidth() != rW || srcBitmap.getHeight() != rH) {
                                dx = Math.min(0, pageItem.getLeft());
                                dy = Math.min(0, pageItem.getTop());
                            }
                            canvas.drawBitmap(srcBitmap, dx, dy, paint);
                            canvas.translate(-(Math.max(left, 0) - left), -(Math.max(top, 0) - top));
                            control.getSysKit().getCalloutManager().drawPath(canvas, pageItem.getPageIndex(), getZoom());
                        } else {
                            Matrix matrix = new Matrix();
                            float xZoom = dstBitmap.getWidth() / (float) rW;
                            float yZoom = dstBitmap.getHeight() / (float) rH;
                            matrix.postScale(xZoom, yZoom);
                            if ((int) (getZoom() * 1000000) == 1000000) {
                                matrix.postTranslate(Math.min(pageItem.getLeft(), 0), Math.min(pageItem.getTop(), 0));
                                dx = Math.min(0, (int) (pageItem.getLeft() * xZoom));
                                dy = Math.min(0, (int) (pageItem.getTop() * yZoom));
                            }
                            try {
                                Bitmap scaleBitmp = Bitmap.createBitmap(srcBitmap, 0, 0,
                                        srcBitmap.getWidth(), srcBitmap.getHeight(), matrix, true);
                                canvas.drawBitmap(scaleBitmp, dx, dy, paint);
                            } catch (OutOfMemoryError e) {
                                canvas.drawBitmap(srcBitmap, matrix, paint);
                            }
                            canvas.translate(-(Math.max(left, 0) - left), -(Math.max(top, 0) - top));
                            control.getSysKit().getCalloutManager().drawPath(canvas, pageItem.getPageIndex(), getZoom());

                        }
                        return dstBitmap;
                    }
                } catch (Exception e) {

                }
                return null;
            }

            protected void onPreExecute() {

            }

            protected void onCancelled() {
                isCancal = true;
            }

            protected void onPostExecute(Bitmap bitmap) {
                try {
                    if (bitmap != null) {
                        if (control == null || isCancal) {
                            return;
                        }
                        IOfficeToPicture otp = control.getOfficeToPicture();
                        if (otp != null && otp.getModeType() == IOfficeToPicture.VIEW_CHANGE_END) {
                            otp.callBack(bitmap);
                        }
                    }
                } catch (Exception e) {

                }
            }

        };

    }

    public Bitmap getSanpshot(Bitmap destBitmap) {

        if (destBitmap == null) {
            return null;
        }
        APageListItem pageItem = listView.getCurrentPageView();
        if (pageItem == null) {
            return null;
        }
        int rW = Math.min(getWidth(), pageItem.getWidth());
        int rH = Math.min(getHeight(), pageItem.getHeight());
        float xZoom = destBitmap.getWidth() / (float) rW;
        float yZoom = destBitmap.getHeight() / (float) rH;
        int left = (int) (pageItem.getLeft() * xZoom);
        int top = (int) (pageItem.getTop() * yZoom);
        int tX = Math.max(left, 0) - left;
        int tY = Math.max(top, 0) - top;
        float tW = (pageItem.getPageWidth() * xZoom * getZoom());
        float tH = (pageItem.getPageHeight() * yZoom * getZoom());

        pdfLib.drawPageSync(destBitmap, pageItem.getPageIndex(),
                tW, tH, tX, tY,
                destBitmap.getWidth(), destBitmap.getHeight(), 1);
        if (tX == 0 && tW < destBitmap.getWidth()
                && rW == pageItem.getWidth()) {
            paint.setStyle(Paint.Style.FILL);
            paint.setColor(Color.BLACK);
            float span = destBitmap.getWidth() - tW;
            Canvas canvas = new Canvas(destBitmap);
            canvas.drawRect(destBitmap.getWidth() - span, 0, destBitmap.getWidth(), destBitmap.getHeight(), paint);
        }
        return destBitmap;
    }

    public boolean isInit() {
        return !pdfLib.hasPasswordSync();
    }

    public boolean isIgnoreOriginalSize() {
        return control.getMainFrame().isIgnoreOriginalSize();
    }

    public byte getPageListViewMovingPosition() {
        return control.getMainFrame().getPageListViewMovingPosition();
    }

    public Object getModel() {
        return pdfLib;
    }

    public IControl getControl() {
        return this.control;
    }

    public boolean onEventMethod(View v, MotionEvent e1, MotionEvent e2, float velocityX, float velocityY, byte eventMethodType) {
        return control.getMainFrame().onEventMethod(v, e1, e2, velocityX, velocityY, eventMethodType);
    }

    public void updateStutus(Object obj) {
        control.actionEvent(EventConstant.SYS_UPDATE_TOOLSBAR_BUTTON_STATUS, obj);
    }

    public void resetSearchResult(APageListItem pageItem) {
        if (find != null) {
            if (pageItem.getPageIndex() != find.getPageIndex()) {
                find.resetSearchResult();
            }
        }
    }

    public boolean isTouchZoom() {
        return control.getMainFrame().isTouchZoom();
    }

    public boolean isShowZoomingMsg() {
        return control.getMainFrame().isShowZoomingMsg();
    }

    public void changeZoom() {
        control.getMainFrame().changeZoom();
    }

    public void setDrawPictrue(boolean isDrawPictrue) {

    }

    public boolean isChangePage() {
        return control.getMainFrame().isChangePage();
    }

    private void drawPageNubmer(Canvas canvas) {
        if (control.getMainFrame().isDrawPageNumber()) {
            String pn = (listView.getCurrentPageNumber()) + " / " + pdfLib.getPageCountSync();
            int w = (int) paint.measureText(pn);
            int h = (int) (paint.descent() - paint.ascent());
            int x = (getWidth() - w) / 2;
            int y = (getHeight() - h) - 20;

            Drawable drawable = SysKit.getPageNubmerDrawable();
            drawable.setBounds(x - 10, y - 10, x + w + 10, y + h + 10);
            drawable.draw(canvas);

            y -= paint.ascent();
            canvas.drawText(pn, x, y, paint);
        }

        if (listView.isInit() && preShowPageIndex != listView.getCurrentPageNumber()) {
            control.getMainFrame().changePage();
            preShowPageIndex = listView.getCurrentPageNumber();
        }
    }

    public void dispose() {
        if (find != null) {
            find.dispose();
        }
        if (find != null) {
            find.dispose();
            find = null;
        }
        if (pdfLib != null) {
            pdfLib.setStopFlagSync(1);
            pdfLib = null;
        }
        if (listView != null) {
            listView.dispose();
        }
        control = null;
    }
}
