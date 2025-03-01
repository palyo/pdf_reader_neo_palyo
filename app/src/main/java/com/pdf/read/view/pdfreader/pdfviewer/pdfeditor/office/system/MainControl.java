package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.system;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import java.lang.reflect.Method;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.ext.SheetKt;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.ext.KeyKt;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.common.ICustomDialog;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.common.IOfficeToPicture;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.common.ISlideShow;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.common.picture.PictureKit;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.constant.EventConstant;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.doc.TXTKit;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.pdf.PDFLib;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.pdf.PDFControl;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.pg.control.PGControl;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.pg.model.PGModel;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.simpletext.model.IDocument;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.ss.control.SSControl;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.ss.model.baseModel.Workbook;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.wp.control.WPControl;

public class MainControl extends AbstractControl {

    public SysKit sysKit;
    private boolean isDispose;
    private boolean isCancel;
    private boolean isAutoTest;
    private byte applicationType = -1;
    private String filePath;
    private IMainFrame frame;
    private IOfficeToPicture officeToPicture;
    private ICustomDialog customDialog;
    private ISlideShow slideShow;
    private IReader reader;
    private Toast toast;
    private Dialog progressDialog;
    private DialogInterface.OnKeyListener onKeyListener;
    private Handler handler;
    private IControl appControl;
    private AUncaughtExceptionHandler uncaught;

    public MainControl(IMainFrame frame) {
        this.frame = frame;
        uncaught = new AUncaughtExceptionHandler(this);
        Thread.setDefaultUncaughtExceptionHandler(uncaught);
        sysKit = new SysKit(this);
        init();
    }

    private void init() {
        initListener();
        toast = Toast.makeText(getActivity().getApplicationContext(), "", Toast.LENGTH_SHORT);
        Intent intent = getActivity().getIntent();
        String autoTest = intent.getStringExtra("autoTest");
        isAutoTest = autoTest != null && autoTest.equals("true");
    }

    private void initListener() {
        onKeyListener = (dialog, keyCode, event) -> {
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                dialog.dismiss();
                isCancel = true;
                if (reader != null) {
                    reader.abortReader();
                    reader.dispose();
                }
                getActivity().onBackPressed();
                return true;
            }
            return false;
        };

        handler = new Handler(Looper.getMainLooper()) {
            public void handleMessage(Message msg) {
                if (isCancel) {
                    return;
                }
                final Message message = msg;
                switch (msg.what) {
                    case KeyKt.HANDLER_MESSAGE_SUCCESS:
                        post(new Runnable() {
                            public void run() {
                                try {
                                    if (getMainFrame().isShowProgressBar()) {
                                        dismissProgressDialog();
                                    } else {
                                        if (customDialog != null) {
                                            customDialog.dismissDialog(ICustomDialog.DIALOGTYPE_LOADING);
                                        }
                                    }
                                    createApplication(message.obj);
                                } catch (Exception e) {
                                    sysKit.getErrorKit().writerLog(e, true);
                                }
                            }
                        });
                        break;

                    case KeyKt.HANDLER_MESSAGE_ERROR:
                        post(new Runnable() {
                            public void run() {
                                dismissProgressDialog();
                                if (message.obj instanceof Throwable) {
                                    sysKit.getErrorKit().writerLog((Throwable) message.obj, true);
                                }
                            }
                        });
                        break;

                    case KeyKt.HANDLER_MESSAGE_SHOW_PROGRESS:
                        if (getMainFrame().isShowProgressBar()) {
                            post(new Runnable() {
                                public void run() {
                                    progressDialog = SheetKt.showProgressDialog(getActivity());
                                    progressDialog.setOnKeyListener(onKeyListener);
                                    progressDialog.show();
                                }
                            });
                        } else {
                            if (customDialog != null) {
                                customDialog.showDialog(ICustomDialog.DIALOGTYPE_LOADING);
                            }
                        }

                        break;

                    case KeyKt.HANDLER_MESSAGE_DISMISS_PROGRESS:
                        post(new Runnable() {
                            public void run() {
                                dismissProgressDialog();
                            }
                        });
                        break;

                    case KeyKt.HANDLER_MESSAGE_SEND_READER_INSTANCE:
                        reader = (IReader) msg.obj;
                        break;
                }
            }
        };
    }

    public void dismissProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
        }
    }

    private void createApplication(Object obj) throws Exception {
        if (obj == null) {
            throw new Exception("Document with password");
        }

        if (applicationType == KeyKt.APPLICATION_TYPE_WP) {
            appControl = new WPControl(this, (IDocument) obj, filePath);
        } else if (applicationType == KeyKt.APPLICATION_TYPE_SS) {
            appControl = new SSControl(this, (Workbook) obj, filePath);
        } else if (applicationType == KeyKt.APPLICATION_TYPE_PPT) {
            appControl = new PGControl(this, (PGModel) obj, filePath);
        } else if (applicationType == KeyKt.APPLICATION_TYPE_PDF) {
            appControl = new PDFControl(this, (PDFLib) obj, filePath);
        }

        View view = appControl.getView();
        if (view != null) {
            Object bg = frame.getViewBackground();
            if (bg != null) {
                if (bg instanceof Integer) {
                    view.setBackgroundColor((Integer) bg);
                } else if (bg instanceof Drawable) {
                    view.setBackgroundDrawable((Drawable) bg);
                }
            }
        }

        final boolean hassPassword = applicationType == KeyKt.APPLICATION_TYPE_PDF && ((PDFLib) obj).hasPasswordSync();
        if (applicationType == KeyKt.APPLICATION_TYPE_PDF) {
            if (!hassPassword) {
                frame.openFileFinish();
            }
        } else {
            frame.openFileFinish();
        }

        PictureKit.instance().setDrawPictrue(true);

        handler.post(new Runnable() {
            @Override
            public void run() {
                try {
                    View contentView = getView();
                    Method isHardwareAccelerated = contentView.getClass().getMethod("isHardwareAccelerated", null);
                    Object o = isHardwareAccelerated.invoke(contentView, null);
                    if (null != o && o instanceof Boolean && (Boolean) o) {
                        Method setLayerType = contentView.getClass().getMethod("setLayerType", int.class, android.graphics.Paint.class);
                        int LAYER_TYPE_SOFTWARE = contentView.getClass().getField("LAYER_TYPE_SOFTWARE").getInt(null);
                        setLayerType.invoke(contentView, LAYER_TYPE_SOFTWARE, null);

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                actionEvent(EventConstant.SYS_SET_PROGRESS_BAR_ID, false);
                actionEvent(EventConstant.SYS_INIT_ID, null);
                if (applicationType == KeyKt.APPLICATION_TYPE_PDF) {
                    if (!hassPassword) {
                        frame.updateToolsbarStatus();
                    }
                } else {
                    frame.updateToolsbarStatus();
                }
                getView().postInvalidate();
            }
        });
    }

    public boolean openFile(String filePath) {
        this.filePath = filePath;
        String fileName = filePath.toLowerCase();
        if (fileName.endsWith(KeyKt.FILE_TYPE_DOC)
                || fileName.endsWith(KeyKt.FILE_TYPE_DOCX)
                || fileName.endsWith(KeyKt.FILE_TYPE_TXT)
                || fileName.endsWith(KeyKt.FILE_TYPE_DOT)
                || fileName.endsWith(KeyKt.FILE_TYPE_DOTX)
                || fileName.endsWith(KeyKt.FILE_TYPE_DOTM)) {
            applicationType = KeyKt.APPLICATION_TYPE_WP;
        } else if (fileName.endsWith(KeyKt.FILE_TYPE_XLS)
                || fileName.endsWith(KeyKt.FILE_TYPE_XLSX)
                || fileName.endsWith(KeyKt.FILE_TYPE_XLT)
                || fileName.endsWith(KeyKt.FILE_TYPE_XLTX)
                || fileName.endsWith(KeyKt.FILE_TYPE_XLTM)
                || fileName.endsWith(KeyKt.FILE_TYPE_XLSM)) {
            applicationType = KeyKt.APPLICATION_TYPE_SS;
        } else if (fileName.endsWith(KeyKt.FILE_TYPE_PPT)
                || fileName.endsWith(KeyKt.FILE_TYPE_PPTX)
                || fileName.endsWith(KeyKt.FILE_TYPE_POT)
                || fileName.endsWith(KeyKt.FILE_TYPE_PPTM)
                || fileName.endsWith(KeyKt.FILE_TYPE_POTX)
                || fileName.endsWith(KeyKt.FILE_TYPE_POTM)) {
            applicationType = KeyKt.APPLICATION_TYPE_PPT;
        } else if (fileName.endsWith(KeyKt.FILE_TYPE_PDF)) {
            applicationType = KeyKt.APPLICATION_TYPE_PDF;
        } else {
            applicationType = KeyKt.APPLICATION_TYPE_WP;
        }

        boolean isSupport = FileKit.instance().isSupport(fileName);
        if (fileName.endsWith(KeyKt.FILE_TYPE_TXT) || !isSupport) {
            TXTKit.instance().readText(this, handler, filePath);
        } else {
            new FileReaderThread(this, handler, filePath, null).start();
        }
        return true;
    }

    public void layoutView(int x, int y, int w, int h) {
    }

    public void actionEvent(int actionID, final Object obj) {
        if (actionID == EventConstant.SYS_READER_FINSH_ID) {
            if (reader != null) {
                if (appControl != null) {
                    appControl.actionEvent(actionID, obj);
                }

                reader.dispose();
                reader = null;
            }
        }
        if (frame == null || frame.doActionEvent(actionID, obj)) {
            return;
        }
        switch (actionID) {
            case KeyKt.HANDLER_MESSAGE_SUCCESS: {
                try {
                    Message msg = new Message();
                    msg.obj = obj;
                    reader.dispose();
                    msg.what = KeyKt.HANDLER_MESSAGE_SUCCESS;

                    handler.handleMessage(msg);
                } catch (Throwable e) {
                    sysKit.getErrorKit().writerLog(e);
                }
            }
            break;
            case EventConstant.SYS_SET_PROGRESS_BAR_ID:
                if (handler != null) {
                    handler.post(new Runnable() {
                        public void run() {
                            if (!isDispose) {
                                frame.showProgressBar((Boolean) obj);
                            }
                        }
                    });
                }
                break;
            case EventConstant.TEST_REPAINT_ID:
                getView().postInvalidate();
                break;

            case EventConstant.SYS_SHOW_TOOLTIP:
                if (obj != null && obj instanceof String) {
                    toast.setText((String) obj);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
                break;

            case EventConstant.SYS_CLOSE_TOOLTIP:
                toast.cancel();
                break;

            case EventConstant.APP_CONTENT_SELECTED:
                appControl.actionEvent(actionID, obj);
                frame.updateToolsbarStatus();
                break;

            case EventConstant.APP_ABORTREADING:
                if (reader != null) {
                    reader.abortReader();
                }
                break;

            case EventConstant.SYS_START_BACK_READER_ID:
                if (handler != null) {
                    handler.post(new Runnable() {

                        @Override
                        public void run() {
                            if (!isDispose) {
                                frame.showProgressBar(true);
                            }
                        }
                    });
                }
                break;

            case EventConstant.SYS_READER_FINSH_ID:
                if (handler != null) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (!isDispose) {
                                frame.showProgressBar(false);
                            }
                        }
                    });
                }
                break;

            case EventConstant.TXT_DIALOG_FINISH_ID:
                TXTKit.instance().reopenFile(this, handler, filePath, (String) obj);
                break;

            case EventConstant.TXT_REOPNE_ID:
                String[] strings = (String[]) obj;
                if (strings.length == 2) {
                    this.filePath = strings[0];
                    applicationType = KeyKt.APPLICATION_TYPE_WP;
                    TXTKit.instance().reopenFile(this, handler, filePath, strings[1]);
                }
                break;

            default:
                if (appControl != null) {
                    appControl.actionEvent(actionID, obj);
                }
        }
    }

    public IFind getFind() {
        return appControl.getFind();
    }

    public Object getActionValue(int actionID, Object obj) {
        if (actionID == EventConstant.SYS_FILEPAHT_ID) {
            return filePath;
        }
        if (appControl == null) {
            return null;
        }

        Object ret = null;
        if (actionID == EventConstant.APP_THUMBNAIL_ID
                || actionID == EventConstant.WP_PAGE_TO_IMAGE
                || actionID == EventConstant.APP_PAGEAREA_TO_IMAGE
                || actionID == EventConstant.PG_SLIDE_TO_IMAGE
                || actionID == EventConstant.PG_SLIDESHOW_SLIDESHOWTOIMAGE) {
            boolean b = PictureKit.instance().isDrawPictrue();
            boolean isThumbnail = frame.isThumbnail();
            PictureKit.instance().setDrawPictrue(true);
            if (actionID == EventConstant.APP_THUMBNAIL_ID) {
                frame.setThumbnail(true);
            }
            ret = appControl.getActionValue(actionID, obj);
            if (actionID == EventConstant.APP_THUMBNAIL_ID) {
                frame.setThumbnail(isThumbnail);
            }
            PictureKit.instance().setDrawPictrue(b);
        } else {
            ret = appControl.getActionValue(actionID, obj);
        }

        return ret;
    }

    public View getView() {
        if (appControl == null) {
            return null;
        }
        return appControl.getView();
    }

    public Dialog getDialog(Activity activity, int id) {
        return null;
    }

    public boolean isAutoTest() {
        return isAutoTest;
    }

    public IMainFrame getMainFrame() {
        return frame;
    }

    public Activity getActivity() {
        return frame.getActivity();
    }

    public IOfficeToPicture getOfficeToPicture() {
        return officeToPicture;
    }

    public ICustomDialog getCustomDialog() {
        return customDialog;
    }

    public void setCustomDialog(ICustomDialog dlg) {
        this.customDialog = dlg;
    }

    public ISlideShow getSlideShow() {
        return slideShow;
    }

    public void setSlideShow(ISlideShow slideshow) {
        this.slideShow = slideshow;
    }

    public IReader getReader() {
        return reader;
    }

    public byte getApplicationType() {
        return applicationType;
    }

    public void setOffictToPicture(IOfficeToPicture opt) {
        officeToPicture = opt;
    }

    public SysKit getSysKit() {
        return this.sysKit;
    }

    public int getCurrentViewIndex() {
        return appControl.getCurrentViewIndex();
    }

    public void dispose() {
        isDispose = true;
        if (appControl != null) {
            appControl.dispose();
            appControl = null;
        }
        if (reader != null) {
            reader.dispose();
            reader = null;
        }
        if (officeToPicture != null) {
            officeToPicture.dispose();
            officeToPicture = null;
        }
        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }

        if (customDialog != null) {
            customDialog = null;
        }

        if (slideShow != null) {
            slideShow = null;
        }

        frame = null;
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
            handler = null;
        }
        if (uncaught != null) {
            uncaught.dispose();
            uncaught = null;
        }
        onKeyListener = null;
        toast = null;
        filePath = null;
        System.gc();
        if (sysKit != null) {
            sysKit.dispose();
        }
    }
}
