package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.macro;

import android.app.Activity;
import android.graphics.Color;
import android.view.MotionEvent;
import android.view.View;

import java.io.File;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.res.ResKit;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.system.IMainFrame;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.system.beans.pagelist.IPageListViewListener;

class MacroFrame implements IMainFrame {

    private boolean isTouchZoom = true;

    private boolean isDrawPageNumber = true;

    private boolean showZoomingMsg = true;

    private boolean popupErrorDlg = true;

    private boolean showPasswordDlg = true;

    private boolean showProgressbarDlg = true;

    private boolean showFindDlg = true;

    private boolean showTXTEncodeDlg = true;

    private boolean isChangePage = true;

    private String txtDefalutEncode;

    private boolean isZoomAfterLayoutForWord = true;

    private byte wordDefaultView;

    private int bottomBarHeight;

    private int topBarHeight;

    private String appName;

    private Application app;

    private Activity activity;

    private TouchEventListener touchEventListener;

    private UpdateStatusListener updateStatusListener;

    private OpenFileFinishListener openFileFinishListener;

    private ErrorListener errorListener;

    private Map<String, Integer> resI18N;

    private boolean writeLog = true;

    private boolean isThumbnail;

    private Object bg = Color.GRAY;

    private boolean ignoreOriginalSize = false;
    private byte pageListViewMovingPosition = IPageListViewListener.Moving_Horizontal;

    public MacroFrame(Application application, Activity activity) {
        this.app = application;
        this.activity = activity;

        int resID = activity.getApplication().getApplicationInfo().labelRes;
        if (resID > 0) {
            this.appName = activity.getResources().getString(resID);
        }
    }

    public Activity getActivity() {
        return activity;
    }

    protected void addTouchEventListener(TouchEventListener listener) {
        this.touchEventListener = listener;
    }

    public void addUpdateStatusListener(UpdateStatusListener listener) {
        this.updateStatusListener = listener;
    }

    public void addOpenFileFinishListener(OpenFileFinishListener listener) {
        this.openFileFinishListener = listener;
    }

    public void addErrorListener(ErrorListener listener) {
        this.errorListener = listener;
    }

    public File getTemporaryDirectory() {
        if (activity != null) {

            File file = activity.getExternalFilesDir(null);
            if (file != null) {
                return file;
            } else {
                return activity.getFilesDir();
            }
        }

        return null;
    }

    public boolean onEventMethod(View v, MotionEvent e1, MotionEvent e2, float velocityX, float velocityY, byte eventMethodType) {
        if (touchEventListener != null) {
            touchEventListener.onEventMethod(v, e1, e2, velocityX, velocityY, eventMethodType);
        }
        return false;
    }

    public void updateToolsbarStatus() {
        if (updateStatusListener != null) {
            updateStatusListener.updateStatus();
        }
    }

    public void changeZoom() {
        if (updateStatusListener != null) {
            updateStatusListener.changeZoom();
        }
    }

    public void changePage() {
        if (updateStatusListener != null) {
            updateStatusListener.changePage();
        }
    }

    public void completeLayout() {
        if (updateStatusListener != null) {
            updateStatusListener.completeLayout();
        }
    }

    public void fullScreen(boolean fullscreen) {

    }

    public void showProgressBar(boolean visible) {
        if (showProgressbarDlg) {
            activity.setProgressBarIndeterminateVisibility(visible);
        }
    }

    public void updateViewImages(List<Integer> viewList) {
        if (updateStatusListener != null) {
            updateStatusListener.updateViewImage(viewList.toArray(new Integer[viewList.size()]));
        }
    }

    public boolean doActionEvent(int actionID, Object obj) {
        return false;
    }

    public void openFileFinish() {
        app.openFileFinish();
        if (openFileFinishListener != null) {
            openFileFinishListener.openFileFinish();
        }

    }

    public void error(int errorCode) {
        if (errorListener != null) {
            errorListener.error(errorCode);
        }
    }

    public void destroyEngine() {
        if (errorListener != null) {

        }
    }

    public void setFindBackForwardState(boolean state) {

    }

    public int getBottomBarHeight() {
        return this.bottomBarHeight;
    }

    public void setBottomBarHeight(int value) {
        this.bottomBarHeight = value;
    }

    public int getTopBarHeight() {
        return this.topBarHeight;
    }

    public void setTopBarHeight(int value) {
        this.topBarHeight = value;
    }

    public String getAppName() {
        return appName == null ? "wxiwei" : appName;
    }

    public void setAppName(String name) {
        this.appName = name;
    }

    public boolean isDrawPageNumber() {
        return this.isDrawPageNumber;
    }

    public void setDrawPageNumber(boolean value) {
        this.isDrawPageNumber = value;
    }

    public boolean isShowZoomingMsg() {
        return showZoomingMsg;
    }

    public void setShowZoomingMsg(boolean showZoomingMsg) {
        this.showZoomingMsg = showZoomingMsg;
    }

    public boolean isPopUpErrorDlg() {
        return popupErrorDlg;
    }

    public void setPopUpErrorDlg(boolean popupErrorDlg) {
        this.popupErrorDlg = popupErrorDlg;
    }

    public boolean isShowPasswordDlg() {
        return showPasswordDlg;
    }

    public void setShowPasswordDlg(boolean showPasswordDlg) {
        this.showPasswordDlg = showPasswordDlg;
    }

    public boolean isShowProgressBar() {
        return showProgressbarDlg;
    }

    public void setShowProgressBar(boolean showProgressbarDlg) {
        this.showProgressbarDlg = showProgressbarDlg;
    }

    public boolean isShowFindDlg() {
        return showFindDlg;
    }

    public void setShowFindDlg(boolean b) {
        this.showFindDlg = b;
    }

    public boolean isShowTXTEncodeDlg() {
        return showTXTEncodeDlg;
    }

    public void setShowTXTEncodeDlg(boolean showTXTEncodeDlg) {
        this.showTXTEncodeDlg = showTXTEncodeDlg;
    }

    public String getTXTDefaultEncode() {
        if (!showTXTEncodeDlg) {
            return txtDefalutEncode;
        }

        return null;
    }

    public void setTXTDefaultEncode(String encode) {
        this.txtDefalutEncode = encode;
    }

    public boolean isTouchZoom() {
        return this.isTouchZoom;
    }

    public void setTouchZoom(boolean value) {
        this.isTouchZoom = value;
    }

    public boolean isZoomAfterLayoutForWord() {
        return this.isZoomAfterLayoutForWord;
    }

    public void setZoomAfterLayoutForWord(boolean value) {
        this.isZoomAfterLayoutForWord = value;
    }

    public byte getWordDefaultView() {
        return wordDefaultView;
    }

    public void setWordDefaultView(byte value) {
        this.wordDefaultView = value;
    }

    public boolean isChangePage() {
        return this.isChangePage;
    }

    public void setChangePage(boolean b) {
        this.isChangePage = b;
    }

    public String getLocalString(String resName) {
        if (resI18N == null) {
            resI18N = new HashMap<String, Integer>();
            try {
                String className = activity.getPackageName();

                Class cls = Class.forName(className + ".R$string");

                Field[] fields = cls.getDeclaredFields();
                String fieldName;
                for (Field field : fields) {
                    fieldName = field.getName().toUpperCase();
                    if (ResKit.instance().hasResName(fieldName)) {
                        resI18N.put(fieldName, field.getInt(null));
                    }
                }
            } catch (Exception e) {

            }
        }
        String str = null;
        Integer id = resI18N.get(resName);
        if (id != null) {
            str = activity.getResources().getString(id);
        }
        if (str == null || str.length() == 0) {
            str = ResKit.instance().getLocalString(resName);
        }
        return str;
    }

    protected void addI18NResID(String resName, int resID) {
        if (resI18N == null) {
            resI18N = new HashMap<String, Integer>();
        }
        resI18N.put(resName, resID);
    }

    public boolean isWriteLog() {
        return writeLog;
    }

    public void setWriteLog(boolean saveLog) {
        this.writeLog = saveLog;
    }

    public boolean isThumbnail() {
        return isThumbnail;
    }

    public void setThumbnail(boolean isThumbnail) {
        this.isThumbnail = isThumbnail;
    }

    public Object getViewBackground() {
        return bg;
    }

    public void setViewBackground(Object bg) {
        this.bg = bg;
    }

    public boolean isIgnoreOriginalSize() {
        return ignoreOriginalSize;
    }

    public void setIgnoreOriginalSize(boolean ignoreOriginalSize) {
        this.ignoreOriginalSize = ignoreOriginalSize;
    }

    public byte getPageListViewMovingPosition() {
        return pageListViewMovingPosition;
    }

    public void setPageListViewMovingPosition(byte position) {
        this.pageListViewMovingPosition = position;
    }

    public void dispose() {
        app = null;
        activity = null;
        updateStatusListener = null;
        touchEventListener = null;
        errorListener = null;
        openFileFinishListener = null;
        txtDefalutEncode = null;
    }
}
