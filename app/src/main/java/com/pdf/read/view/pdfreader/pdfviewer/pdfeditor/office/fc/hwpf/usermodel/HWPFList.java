package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hwpf.usermodel;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hwpf.model.ListFormatOverride;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hwpf.model.POIListData;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hwpf.model.POIListLevel;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hwpf.model.StyleSheet;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hwpf.sprm.CharacterSprmCompressor;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hwpf.sprm.ParagraphSprmCompressor;

public final class HWPFList {
    private final POIListData _listData;
    private final ListFormatOverride _override;
    private final StyleSheet _styleSheet;
    private boolean _registered;

    public HWPFList(boolean numbered, StyleSheet styleSheet) {
        _listData = new POIListData((int) (Math.random() * System.currentTimeMillis()), numbered);
        _override = new ListFormatOverride(_listData.getLsid());
        _styleSheet = styleSheet;
    }

    public void setLevelNumberProperties(int level, CharacterProperties chp) {
        POIListLevel listLevel = _listData.getLevel(level);
        int styleIndex = _listData.getLevelStyle(level);
        CharacterProperties base = _styleSheet.getCharacterStyle(styleIndex);

        byte[] grpprl = CharacterSprmCompressor.compressCharacterProperty(chp, base);
        listLevel.setNumberProperties(grpprl);
    }

    public void setLevelParagraphProperties(int level, ParagraphProperties pap) {
        POIListLevel listLevel = _listData.getLevel(level);
        int styleIndex = _listData.getLevelStyle(level);
        ParagraphProperties base = _styleSheet.getParagraphStyle(styleIndex);

        byte[] grpprl = ParagraphSprmCompressor.compressParagraphProperty(pap, base);
        listLevel.setLevelProperties(grpprl);
    }

    public void setLevelStyle(int level, int styleIndex) {
        _listData.setLevelStyle(level, styleIndex);
    }

    public POIListData getListData() {
        return _listData;
    }

    public ListFormatOverride getOverride() {
        return _override;
    }

}
