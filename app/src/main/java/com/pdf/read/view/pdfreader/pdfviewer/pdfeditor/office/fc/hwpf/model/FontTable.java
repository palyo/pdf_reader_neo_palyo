package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hwpf.model;

import java.io.IOException;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hwpf.model.io.HWPFFileSystem;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hwpf.model.io.HWPFOutputStream;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.Internal;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.LittleEndian;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.POILogFactory;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.POILogger;

@Internal
public final class FontTable {
    private final static POILogger _logger = POILogFactory.getLogger(FontTable.class);
    private final short _extraDataSz;
    private final int lcbSttbfffn;
    private final int fcSttbfffn;
    private short _stringCount;
    private Ffn[] _fontNames = null;

    public FontTable(byte[] buf, int offset, int lcbSttbfffn) {
        this.lcbSttbfffn = lcbSttbfffn;
        this.fcSttbfffn = offset;

        _stringCount = LittleEndian.getShort(buf, offset);
        offset += LittleEndian.SHORT_SIZE;
        _extraDataSz = LittleEndian.getShort(buf, offset);
        offset += LittleEndian.SHORT_SIZE;

        _fontNames = new Ffn[_stringCount];
        for (int i = 0; i < _stringCount; i++) {
            _fontNames[i] = new Ffn(buf, offset);
            offset += _fontNames[i].getSize();
        }
    }

    public short getStringCount() {
        return _stringCount;
    }

    public void setStringCount(short stringCount) {
        this._stringCount = stringCount;
    }

    public short getExtraDataSz() {
        return _extraDataSz;
    }

    public Ffn[] getFontNames() {
        return _fontNames;
    }

    public int getSize() {
        return lcbSttbfffn;
    }

    public String getMainFont(int chpFtc) {
        if (chpFtc >= _stringCount) {
            _logger.log(POILogger.INFO, "Mismatch in chpFtc with stringCount");
            return null;
        }

        return _fontNames[chpFtc].getMainFontName();
    }

    public String getAltFont(int chpFtc) {
        if (chpFtc >= _stringCount) {
            _logger.log(POILogger.INFO, "Mismatch in chpFtc with stringCount");
            return null;
        }

        return _fontNames[chpFtc].getAltFontName();
    }

    @Deprecated
    public void writeTo(HWPFFileSystem sys) throws IOException {
        HWPFOutputStream tableStream = sys.getStream("1Table");
        writeTo(tableStream);
    }

    public void writeTo(HWPFOutputStream tableStream) throws IOException {
        byte[] buf = new byte[LittleEndian.SHORT_SIZE];
        LittleEndian.putShort(buf, _stringCount);
        tableStream.write(buf);
        LittleEndian.putShort(buf, _extraDataSz);
        tableStream.write(buf);

        for (int i = 0; i < _fontNames.length; i++) {
            tableStream.write(_fontNames[i].toByteArray());
        }

    }

    public boolean equals(Object o) {
        boolean retVal = true;

        if (((FontTable) o).getStringCount() == _stringCount) {
            if (((FontTable) o).getExtraDataSz() == _extraDataSz) {
                Ffn[] fontNamesNew = ((FontTable) o).getFontNames();
                for (int i = 0; i < _stringCount; i++) {
                    if (!(_fontNames[i].equals(fontNamesNew[i]))) {
                        retVal = false;
                        break;
                    }
                }
            } else
                retVal = false;
        } else
            retVal = false;

        return retVal;
    }

}
