package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.ss.model.baseModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.ss.other.ExpandedCellRangeAddress;

public class RowProperty {
    public static final short ROWPROPID_ZEROHEIGHT = 0;

    public static final short ROWPROPID_COMPLETED = 1;

    public static final short ROWPROPID_INITEXPANDEDRANGEADDR = 2;

    public static final short ROWPROPID_EXPANDEDRANGEADDRLIST = 3;

    private final Map<Short, Object> rowPropMap;

    public RowProperty() {
        rowPropMap = new HashMap<Short, Object>();
    }

    public void setRowProperty(short rowPropID, Object value) {
        if (rowPropID != ROWPROPID_EXPANDEDRANGEADDRLIST) {
            rowPropMap.put(rowPropID, value);
        } else {
            List<ExpandedCellRangeAddress> expandedRangeAddr = (List<ExpandedCellRangeAddress>) rowPropMap.get(ROWPROPID_EXPANDEDRANGEADDRLIST);
            if (expandedRangeAddr == null) {
                expandedRangeAddr = new ArrayList<ExpandedCellRangeAddress>();
                rowPropMap.put(rowPropID, expandedRangeAddr);
            }
            expandedRangeAddr.add((ExpandedCellRangeAddress) value);

        }
    }

    public boolean isZeroHeight() {
        Object obj = rowPropMap.get(ROWPROPID_ZEROHEIGHT);
        if (obj != null) {
            return (Boolean) obj;
        }

        return false;
    }

    public boolean isCompleted() {
        Object obj = rowPropMap.get(ROWPROPID_COMPLETED);
        if (obj != null) {
            return (Boolean) obj;
        }

        return false;
    }

    public boolean isInitExpandedRangeAddr() {
        Object obj = rowPropMap.get(ROWPROPID_INITEXPANDEDRANGEADDR);
        if (obj != null) {
            return (Boolean) obj;
        }

        return false;
    }

    public int getExpandedCellCount() {
        List<ExpandedCellRangeAddress> expandedRangeAddr = (List<ExpandedCellRangeAddress>) rowPropMap.get(ROWPROPID_EXPANDEDRANGEADDRLIST);
        if (expandedRangeAddr == null) {
            return 0;
        }
        return expandedRangeAddr.size();
    }

    public ExpandedCellRangeAddress getExpandedCellRangeAddr(int index) {
        List<ExpandedCellRangeAddress> expandedRangeAddr = (List<ExpandedCellRangeAddress>) rowPropMap.get(ROWPROPID_EXPANDEDRANGEADDRLIST);
        if (expandedRangeAddr == null) {
            return null;
        }
        return expandedRangeAddr.get(index);
    }

    public void dispose() {
        List<ExpandedCellRangeAddress> expandedRangeAddr = (List<ExpandedCellRangeAddress>) rowPropMap.get(ROWPROPID_EXPANDEDRANGEADDRLIST);
        if (expandedRangeAddr != null) {
            Iterator<ExpandedCellRangeAddress> iter = expandedRangeAddr.iterator();
            while (iter.hasNext()) {
                iter.next().dispose();
            }
        }

    }

}
