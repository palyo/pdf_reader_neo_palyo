package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.common.picture;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.constant.EventConstant;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.pdf.PDFLib;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.system.IControl;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.thirdpart.emf.util.EMFUtil;

public class PictureConverterMgr {

    private final List<Thread> convertingThread;
    private final Map<String, Thread> convertingPictPathMap;
    private final Map<String, List<Integer>> vectorgraphViews;
    private final Map<Integer, List<String>> viewVectorgraphs;
    private IControl control;

    public PictureConverterMgr(IControl control) {
        this.control = control;
        convertingThread = new ArrayList<Thread>();
        convertingPictPathMap = new HashMap<String, Thread>();
        vectorgraphViews = new HashMap<String, List<Integer>>();
        viewVectorgraphs = new HashMap<Integer, List<String>>();
    }

    public void setControl(IControl control) {
        this.control = control;
    }

    public synchronized void addConvertPicture(int viewIndex, byte type, String srcPath, String dstPath, int width, int height, boolean singleThread) {
        control.actionEvent(EventConstant.SYS_SET_PROGRESS_BAR_ID, true);

        if (singleThread) {
            convertWMF_EMF(type, srcPath, dstPath, width, height, true);
        } else {
            VectorgraphConverterThread thread = new VectorgraphConverterThread(this, type, srcPath, dstPath, width, height);

            convertingThread.add(thread);
            convertingPictPathMap.put(dstPath, thread);

            List<Integer> listIndex = new ArrayList<Integer>();
            listIndex.add(viewIndex);
            vectorgraphViews.put(dstPath, listIndex);

            if (viewVectorgraphs.get(viewIndex) == null) {
                List<String> listPath = new ArrayList<String>();
                listPath.add(dstPath);
                viewVectorgraphs.put(viewIndex, listPath);
            } else {
                viewVectorgraphs.get(viewIndex).add(dstPath);
            }

            if (convertingThread.size() == 1) {

                convertingThread.get(convertingThread.size() - 1).start();
            }
        }
    }

    public void convertWMF_EMF(byte type, String sourPath, String destPath, int picWidth, int picHeight, boolean thumbnail) {
        try {
            Bitmap sBitmap = null;
            if (type == Picture.WMF) {
                int ret = PDFLib.getPDFLib().wmf2Jpg(sourPath, destPath, picWidth, picHeight);
                sBitmap = BitmapFactory.decodeFile(destPath);
            } else if (type == Picture.EMF) {
                sBitmap = EMFUtil.convert(sourPath, destPath, picWidth, picHeight);
            }

            if (control != null && (convertingPictPathMap.get(destPath) == null || control.getView() == null)) {

                return;
            }

            if (sBitmap != null) {
                control.getSysKit().getPictureManage().addBitmap(destPath, sBitmap);
                remove(destPath);

                if (!thumbnail) {
                    control.actionEvent(EventConstant.TEST_REPAINT_ID, null);
                }
            } else {
                remove(destPath);
            }
        } catch (OutOfMemoryError e) {
            if (control.getSysKit().getPictureManage().hasBitmap()) {
                control.getSysKit().getPictureManage().clearBitmap();
                convertWMF_EMF(type, sourPath, destPath, picWidth, picHeight, thumbnail);
            } else {
                control.getSysKit().getErrorKit().writerLog(e);
                remove(destPath);
            }
        } catch (Exception e) {
            if (control != null && (convertingPictPathMap.get(destPath) == null || control.getView() == null)) {

                return;
            }

            control.getSysKit().getErrorKit().writerLog(e);
            remove(destPath);
        }
    }

    public synchronized void addConvertPicture(int viewIndex, String srcPath, String dstPath, String picType, boolean singleThread) {
        control.actionEvent(EventConstant.SYS_SET_PROGRESS_BAR_ID, true);

        if (singleThread) {
            convertPNG(srcPath, dstPath, picType, true);
        } else {
            PictureConverterThread thread = new PictureConverterThread(this, srcPath, dstPath, picType);

            convertingThread.add(thread);
            convertingPictPathMap.put(dstPath, thread);

            List<Integer> listIndex = new ArrayList<Integer>();
            listIndex.add(viewIndex);
            vectorgraphViews.put(dstPath, listIndex);

            if (viewVectorgraphs.get(viewIndex) == null) {
                List<String> listPath = new ArrayList<String>();
                listPath.add(dstPath);
                viewVectorgraphs.put(viewIndex, listPath);
            } else {
                viewVectorgraphs.get(viewIndex).add(dstPath);
            }

            if (convertingThread.size() == 1) {

                convertingThread.get(convertingThread.size() - 1).start();
            }
        }
    }

    public void convertPNG(String sourPath, String destPath, String picType, boolean thumbnail) {
        try {
            boolean ret = PDFLib.getPDFLib().convertToPNG(sourPath, destPath, picType);

            if (control != null && (convertingPictPathMap.get(destPath) == null || control.getView() == null)) {

                return;
            }

            if (ret) {
                InputStream in = new FileInputStream(destPath);
                Bitmap sBitmap = BitmapFactory.decodeStream(in, null, null);
                if (sBitmap != null) {
                    control.getSysKit().getPictureManage().addBitmap(destPath, sBitmap);
                    remove(destPath);

                    if (!thumbnail) {
                        control.actionEvent(EventConstant.TEST_REPAINT_ID, null);
                    }
                } else {
                    remove(destPath);
                }
            } else {
                remove(destPath);
            }
        } catch (OutOfMemoryError e) {
            if (control.getSysKit().getPictureManage().hasBitmap()) {
                control.getSysKit().getPictureManage().clearBitmap();
                convertPNG(sourPath, destPath, picType, thumbnail);
            } else {
                control.getSysKit().getErrorKit().writerLog(e);
                remove(destPath);
            }
        } catch (Exception e) {
            if (control != null && (convertingPictPathMap.get(destPath) == null || control.getView() == null)) {

                return;
            }

            control.getSysKit().getErrorKit().writerLog(e);
            remove(destPath);
        }
    }

    public void remove(String path) {
        synchronized (control) {
            if (convertingPictPathMap != null) {
                Thread thread = convertingPictPathMap.remove(path);
                convertingThread.remove(thread);

                List<Integer> updateViewList = null;
                List<Integer> viewList = vectorgraphViews.remove(path);
                for (int i = 0; i < viewList.size(); i++) {
                    int viewIndex = viewList.get(i);
                    List<String> vectorgraphs = viewVectorgraphs.get(viewIndex);
                    vectorgraphs.remove(path);
                    if (vectorgraphs.size() == 0) {

                        viewVectorgraphs.remove(viewIndex);

                        if (updateViewList == null) {
                            updateViewList = new ArrayList<Integer>();
                        }

                        updateViewList.add(viewIndex);
                    }
                }

                if (convertingThread.size() > 0) {

                    List<String> vectorgraphs = viewVectorgraphs.get(control.getCurrentViewIndex());
                    if (vectorgraphs != null && vectorgraphs.size() > 0) {

                        convertingPictPathMap.get(vectorgraphs.get(0)).start();
                    } else {

                        convertingThread.get(convertingThread.size() - 1).start();
                    }
                }

                if (updateViewList != null && updateViewList.size() > 0) {
                    if (updateViewList.contains(control.getCurrentViewIndex())) {
                        control.actionEvent(EventConstant.APP_GENERATED_PICTURE_ID, null);
                    }

                    control.actionEvent(EventConstant.SYS_VECTORGRAPH_PROGRESS, updateViewList);
                }

                if (convertingPictPathMap.size() == 0) {
                    control.actionEvent(EventConstant.SYS_SET_PROGRESS_BAR_ID, false);
                }
            }
        }
    }

    public boolean hasConvertingVectorgraph(int viewIndex) {
        synchronized (control) {
            return viewVectorgraphs.containsKey(viewIndex);
        }
    }

    public boolean isPictureConverting(String path) {
        synchronized (control) {
            return vectorgraphViews.containsKey(path);
        }
    }

    public void appendViewIndex(String path, int viewIndex) {
        synchronized (control) {
            if (isPictureConverting(path)) {
                vectorgraphViews.get(path).add(viewIndex);

                if (viewVectorgraphs.get(viewIndex) == null) {
                    List<String> listPath = new ArrayList<String>();
                    listPath.add(path);
                    viewVectorgraphs.put(viewIndex, listPath);
                } else {
                    viewVectorgraphs.get(viewIndex).add(path);
                }
            }
        }
    }

    public synchronized void dispose() {
        if (convertingPictPathMap != null) {
            Iterator<Thread> iter = convertingPictPathMap.values().iterator();
            while (iter.hasNext()) {
                try {
                    iter.next().interrupt();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
            convertingPictPathMap.clear();

            vectorgraphViews.clear();
            viewVectorgraphs.clear();
        }
    }
}
