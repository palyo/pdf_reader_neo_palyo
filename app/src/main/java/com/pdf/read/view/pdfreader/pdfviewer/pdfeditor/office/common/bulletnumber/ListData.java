package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.common.bulletnumber;

public class ListData {

    private int listID;
    private byte simpleList;
    private short[] linkStyle;
    private short linkStyleID = -1;
    private boolean isNumber;
    private ListLevel[] levels;
    private byte preParaLevel;
    private byte normalPreParaLevel;

    public int getListID() {
        return listID;
    }

    public void setListID(int listID) {
        this.listID = listID;
    }

    public byte getSimpleList() {
        return simpleList;
    }

    public void setSimpleList(byte simpleList) {
        this.simpleList = simpleList;
    }

    public short[] getLinkStyle() {
        return linkStyle;
    }

    public void setLinkStyle(short[] linkStyle) {
        this.linkStyle = linkStyle;
    }

    public boolean isNumber() {
        return isNumber;
    }

    public void setNumber(boolean isNumber) {
        this.isNumber = isNumber;
    }

    public ListLevel[] getLevels() {
        return levels;
    }

    public void setLevels(ListLevel[] levels) {
        this.levels = levels;
    }

    public ListLevel getLevel(int level) {
        if (level < levels.length) {
            return levels[level];
        }
        return null;
    }

    public byte getPreParaLevel() {
        return preParaLevel;
    }

    public void setPreParaLevel(byte preParaLevel) {
        this.preParaLevel = preParaLevel;
    }

    public byte getNormalPreParaLevel() {
        return normalPreParaLevel;
    }

    public void setNormalPreParaLevel(byte normalPreParaLevel) {
        this.normalPreParaLevel = normalPreParaLevel;
    }

    public void resetForNormalView() {
        if (levels != null) {
            for (ListLevel level : levels) {
                level.setNormalParaCount(0);
            }
        }
    }

    public short getLinkStyleID() {
        return linkStyleID;
    }

    public void setLinkStyleID(short linkStyleID) {
        this.linkStyleID = linkStyleID;
    }

    public void dispose() {
        if (levels != null) {
            for (ListLevel level : levels) {
                level.dispose();
            }
            levels = null;
        }
    }
}
