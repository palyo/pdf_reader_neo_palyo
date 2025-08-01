package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.model;

import java.util.ArrayList;
import java.util.List;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.ddf.EscherDgRecord;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.ddf.EscherDggRecord;

public class DrawingManager2 {
    EscherDggRecord dgg;
    List drawingGroups = new ArrayList();

    public DrawingManager2(EscherDggRecord dgg) {
        this.dgg = dgg;
    }

    public void clearDrawingGroups() {
        drawingGroups.clear();
    }

    public EscherDgRecord createDgRecord() {
        EscherDgRecord dg = new EscherDgRecord();
        dg.setRecordId(EscherDgRecord.RECORD_ID);
        short dgId = findNewDrawingGroupId();
        dg.setOptions((short) (dgId << 4));
        dg.setNumShapes(0);
        dg.setLastMSOSPID(-1);
        drawingGroups.add(dg);
        dgg.addCluster(dgId, 0);
        dgg.setDrawingsSaved(dgg.getDrawingsSaved() + 1);
        return dg;
    }

    public int allocateShapeId(short drawingGroupId) {
        EscherDgRecord dg = getDrawingGroup(drawingGroupId);
        return allocateShapeId(drawingGroupId, dg);
    }

    public int allocateShapeId(short drawingGroupId, EscherDgRecord dg) {
        dgg.setNumShapesSaved(dgg.getNumShapesSaved() + 1);

        for (int i = 0; i < dgg.getFileIdClusters().length; i++) {
            EscherDggRecord.FileIdCluster c = dgg.getFileIdClusters()[i];
            if (c.getDrawingGroupId() == drawingGroupId && c.getNumShapeIdsUsed() != 1024) {
                int result = c.getNumShapeIdsUsed() + (1024 * (i + 1));
                c.incrementShapeId();
                dg.setNumShapes(dg.getNumShapes() + 1);
                dg.setLastMSOSPID(result);
                if (result >= dgg.getShapeIdMax())
                    dgg.setShapeIdMax(result + 1);
                return result;
            }
        }

        dgg.addCluster(drawingGroupId, 0);
        dgg.getFileIdClusters()[dgg.getFileIdClusters().length - 1].incrementShapeId();
        dg.setNumShapes(dg.getNumShapes() + 1);
        int result = (1024 * dgg.getFileIdClusters().length);
        dg.setLastMSOSPID(result);
        if (result >= dgg.getShapeIdMax())
            dgg.setShapeIdMax(result + 1);
        return result;
    }

    short findNewDrawingGroupId() {
        short dgId = 1;
        while (drawingGroupExists(dgId))
            dgId++;
        return dgId;
    }

    EscherDgRecord getDrawingGroup(int drawingGroupId) {
        return (EscherDgRecord) drawingGroups.get(drawingGroupId - 1);
    }

    boolean drawingGroupExists(short dgId) {
        for (int i = 0; i < dgg.getFileIdClusters().length; i++) {
            if (dgg.getFileIdClusters()[i].getDrawingGroupId() == dgId)
                return true;
        }
        return false;
    }

    int findFreeSPIDBlock() {
        int max = dgg.getShapeIdMax();
        int next = ((max / 1024) + 1) * 1024;
        return next;
    }

    public EscherDggRecord getDgg() {
        return dgg;
    }

}
