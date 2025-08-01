package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.pg.control;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.AsyncTask;
import android.widget.ProgressBar;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.pg.model.PGModel;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.pg.model.PGSlide;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.pg.view.SlideDrawKit;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.system.IControl;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.system.beans.pagelist.APageListItem;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.system.beans.pagelist.APageListView;

public class PGPageListItem extends APageListItem {
    public static final int BUSY_SIZE = 60;
    private static final int BACKGROUND_COLOR = 0xFFFFFFFF;
    private final PGEditor editor;
    private ProgressBar mBusyIndicator;
    private PGModel pgModel;

    public PGPageListItem(APageListView listView, IControl control, PGEditor editor, int pageWidth, int pageHeight) {
        super(listView, pageWidth, pageHeight);
        this.control = control;
        this.pgModel = (PGModel) listView.getModel();
        this.editor = editor;
        this.setBackgroundColor(BACKGROUND_COLOR);
    }

    public void onDraw(Canvas canvas) {
        PGSlide slide = pgModel.getSlide(pageIndex);
        if (slide != null) {
            float zoom = listView.getZoom();
            SlideDrawKit.instance().drawSlide(canvas, pgModel, editor, slide, zoom);
        }
    }

    public void setPageItemRawData(final int pIndex, int pageWidth, int pageHeight) {
        super.setPageItemRawData(pIndex, pageWidth, pageHeight);
        if (pageIndex >= pgModel.getRealSlideCount()) {
            AsyncTask<Void, Void, Void> asyncTask = new AsyncTask<Void, Void, Void>() {

                protected Void doInBackground(Void... v) {
                    while (pgModel != null && pageIndex >= pgModel.getRealSlideCount()) {
                        try {
                            Thread.sleep(200);
                        } catch (Exception e) {
                            break;
                        }
                    }
                    return null;
                }

                protected void onPreExecute() {
                    if (mBusyIndicator == null) {
                        mBusyIndicator = new ProgressBar(getContext());
                        mBusyIndicator.setIndeterminate(true);
                        mBusyIndicator.setBackgroundResource(android.R.drawable.progress_horizontal);
                        addView(mBusyIndicator);
                        mBusyIndicator.setVisibility(VISIBLE);
                    } else {
                        mBusyIndicator.setVisibility(VISIBLE);
                    }
                }

                protected void onPostExecute(Void v) {
                    if (mBusyIndicator != null) {
                        mBusyIndicator.setVisibility(INVISIBLE);
                    }
                    postInvalidate();
                    if (listView != null) {

                        if (pageIndex == listView.getCurrentPageNumber() - 1) {
                            listView.exportImage(listView.getCurrentPageView(), null);
                        }
                        isInit = false;
                    }
                }
            };
            asyncTask.execute(new Void[1]);
        } else {

            if ((int) (listView.getZoom() * 100) == 100
                    || (isInit && pIndex == 0)) {
                listView.exportImage(this, null);
            }
            isInit = false;
            if (mBusyIndicator != null) {
                mBusyIndicator.setVisibility(INVISIBLE);
            }
        }
    }

    public void releaseResources() {
        super.releaseResources();
        SlideDrawKit.instance().disposeOldSlideView(pgModel, pgModel.getSlide(pageIndex));
    }

    public void blank(int pIndex) {
        super.blank(pIndex);
    }

    protected void addRepaintImageView(Bitmap bmp) {
        postInvalidate();
        listView.exportImage(this, bmp);
    }

    protected void removeRepaintImageView() {

    }

    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        int w = right - left;
        int h = bottom - top;
        if (mBusyIndicator != null) {
            int x, y;
            if (w > listView.getWidth()) {
                x = (listView.getWidth() - BUSY_SIZE) / 2 - left;
            } else {
                x = (w - BUSY_SIZE) / 2;
            }
            if (h > listView.getHeight()) {
                y = (listView.getHeight() - BUSY_SIZE) / 2 - top;
            } else {
                y = (h - BUSY_SIZE) / 2;
            }
            mBusyIndicator.layout(x, y, x + BUSY_SIZE, y + BUSY_SIZE);
        }
    }

    public void dispose() {
        super.dispose();
        control = null;
        pgModel = null;
    }
}
