package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.pdf;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.view.View;

import java.util.ArrayList;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.ext.KeyKt;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.common.ICustomDialog;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.common.IOfficeToPicture;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.constant.EventConstant;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.pdf.PDFHyperlinkInfo;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.pdf.PDFLib;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.java.awt.Rectangle;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.system.AbstractControl;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.system.IControl;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.system.IFind;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.system.IMainFrame;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.system.SysKit;

public class PDFControl extends AbstractControl {

    private boolean isDispose;

    private IControl mainControl;

    private PDFView pdfView;

    public PDFControl(IControl mainControl, PDFLib pdfLib, String filePath) {
        this.mainControl = mainControl;
        pdfView = new PDFView(mainControl.getMainFrame().getActivity().getApplicationContext(), pdfLib, this);
    }

    public void actionEvent(int actionID, final Object obj) {
        switch (actionID) {
            case EventConstant.PDF_SHOW_PAGE:
                int pn = (Integer) obj;
                if (pn >= 0 && pn < pdfView.getPageCount()) {
                    pdfView.showPDFPageForIndex(pn);
                }
                break;

            case EventConstant.APP_PASSWORD_OK_INIT:
                pdfView.post(new Runnable() {

                    public void run() {
                        if (!isDispose) {
                            pdfView.passwordVerified();
                        }
                    }
                });

                break;

            case EventConstant.SYS_SET_PROGRESS_BAR_ID:
                pdfView.post(new Runnable() {

                    public void run() {
                        if (!isDispose) {

                            mainControl.getMainFrame().showProgressBar((Boolean) obj);
                        }
                    }
                });
                break;

            case EventConstant.SYS_INIT_ID:
                pdfView.init();
                break;

            case EventConstant.APP_ZOOM_ID:
                int[] params = (int[]) obj;
                pdfView.setZoom(params[0] / (float) KeyKt.STANDARD_RATE, params[1], params[2]);
                break;

            case EventConstant.SYS_UPDATE_TOOLSBAR_BUTTON_STATUS:
                pdfView.post(new Runnable() {
                    @Override
                    public void run() {
                        if (!isDispose) {
                            getMainFrame().updateToolsbarStatus();
                        }
                    }
                });
                break;

            case EventConstant.SYS_AUTO_TEST_FINISH_ID:
                if (isAutoTest()) {
                    getMainFrame().getActivity().onBackPressed();
                }
                break;

            case EventConstant.APP_GENERATED_PICTURE_ID:

                break;

            case EventConstant.APP_PAGE_UP_ID:
                pdfView.previousPageview();
                break;

            case EventConstant.APP_PAGE_DOWN_ID:
                pdfView.nextPageView();
                break;

            case EventConstant.APP_SET_FIT_SIZE_ID:
                pdfView.setFitSize((Integer) obj);
                break;

            case EventConstant.APP_INIT_CALLOUTVIEW_ID:
                pdfView.getListView().getCurrentPageView().initCalloutView();
                break;

            default:
                break;
        }
    }

    public Object getActionValue(int actionID, Object obj) {
        switch (actionID) {
            case EventConstant.APP_ZOOM_ID:
                return pdfView.getZoom();

            case EventConstant.APP_FIT_ZOOM_ID:
                return pdfView.getFitZoom();

            case EventConstant.APP_COUNT_PAGES_ID:
                return pdfView.getPageCount();

            case EventConstant.APP_CURRENT_PAGE_NUMBER_ID:
                return pdfView.getCurrentPageNumber();

            case EventConstant.PDF_PAGE_TO_IMAGE:
                return pdfView.pageToImage((Integer) obj);

            case EventConstant.APP_PAGEAREA_TO_IMAGE:
                if (obj instanceof int[]) {
                    int[] paraArr = (int[]) obj;
                    if (paraArr != null && paraArr.length == 7) {
                        return pdfView.pageAreaToImage(paraArr[0], paraArr[1], paraArr[2], paraArr[3], paraArr[4], paraArr[5], paraArr[6]);
                    }
                }
                break;

            case EventConstant.PDF_GET_PAGE_SIZE:
                Rect rect = pdfView.getPageSize((Integer) obj - 1);
                if (rect != null) {
                    return new Rectangle(0, 0, rect.width(), rect.height());
                }
                return null;

            case EventConstant.APP_THUMBNAIL_ID:
                if (obj instanceof int[]) {
                    int[] a = (int[]) obj;
                    if (a.length < 2 || a[1] <= 0) {
                        return null;
                    }
                    return pdfView.getThumbnail(a[0], a[1] / (float) KeyKt.STANDARD_RATE);
                }
                break;

            case EventConstant.APP_AUTHENTICATE_PASSWORD:
                if (pdfView != null && obj != null) {
                    return pdfView.getPDFLib().authenticatePasswordSync((String) obj);
                }

            case EventConstant.APP_GET_HYPERLINK_URL_ID:
                if (pdfView != null && obj != null) {
                    int pageIndex = ((Integer) obj) - 1;
                    if (pageIndex >= 0 && pageIndex < pdfView.getPDFLib().getPageCountSync()) {
                        PDFHyperlinkInfo[] infos = pdfView.getPDFLib().getHyperlinkInfoSync(pageIndex);
                        if (infos != null && infos.length > 0) {
                            ArrayList<String> s = new ArrayList<String>();
                            for (int i = 0; i < infos.length; i++) {
                                if (infos[i] != null) {
                                    s.add(infos[i].getURL());
                                }
                            }
                            if (s.size() > 0) {
                                return s.toArray(new String[s.size()]);
                            } else {
                                return null;
                            }
                        }
                    }
                    return null;
                }
                break;

            case EventConstant.APP_GET_FIT_SIZE_STATE_ID:
                if (pdfView != null) {
                    return pdfView.getFitSizeState();
                }
                break;

            case EventConstant.APP_GET_SNAPSHOT_ID:
                if (pdfView != null) {
                    return pdfView.getSanpshot((Bitmap) obj);
                }
                break;

            default:
                break;
        }
        return null;
    }

    public View getView() {
        return pdfView;
    }

    public IMainFrame getMainFrame() {
        return mainControl.getMainFrame();
    }

    public Activity getActivity() {
        return getMainFrame().getActivity();
    }

    public IFind getFind() {
        return pdfView.getFind();
    }

    public boolean isAutoTest() {
        return mainControl.isAutoTest();
    }

    public IOfficeToPicture getOfficeToPicture() {
        return mainControl.getOfficeToPicture();
    }

    public ICustomDialog getCustomDialog() {
        return mainControl.getCustomDialog();
    }

    public byte getApplicationType() {
        return KeyKt.APPLICATION_TYPE_PDF;
    }

    public SysKit getSysKit() {
        return mainControl.getSysKit();
    }

    public void dispose() {
        isDispose = true;
        pdfView.dispose();
        pdfView = null;
        mainControl = null;
    }
}
