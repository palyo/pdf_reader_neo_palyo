package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hwpf;

import java.io.IOException;
import java.io.InputStream;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hwpf.model.BookmarksTables;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hwpf.model.CHPBinTable;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hwpf.model.CPSplitCalculator;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hwpf.model.ComplexFileTable;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hwpf.model.EscherRecordHolder;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hwpf.model.FSPADocumentPart;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hwpf.model.FSPATable;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hwpf.model.FieldsTables;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hwpf.model.FontTable;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hwpf.model.ListTables;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hwpf.model.PAPBinTable;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hwpf.model.PicturesTable;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hwpf.model.PlcfTxbxBkd;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hwpf.model.SectionTable;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hwpf.model.ShapesTable;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hwpf.model.StyleSheet;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hwpf.model.SubdocumentType;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hwpf.model.TextPieceTable;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hwpf.usermodel.Bookmarks;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hwpf.usermodel.BookmarksImpl;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hwpf.usermodel.Field;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hwpf.usermodel.Fields;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hwpf.usermodel.FieldsImpl;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hwpf.usermodel.HWPFList;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hwpf.usermodel.OfficeDrawings;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hwpf.usermodel.OfficeDrawingsImpl;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hwpf.usermodel.Range;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.Internal;

public final class HWPFDocument extends HWPFDocumentCore {
    private static final String PROPERTY_PRESERVE_BIN_TABLES = "com.wxiwei.fc.hwpf.preserveBinTables";
    private static final String PROPERTY_PRESERVE_TEXT_TABLE = "com.wxiwei.fc.hwpf.preserveTextTable";

    private static final String STREAM_DATA = "Data";
    private static final String STREAM_TABLE_0 = "0Table";
    private static final String STREAM_TABLE_1 = "1Table";

    private final FSPATable _fspaHeaders;

    private final FSPATable _fspaMain;

    private final byte[] _tableStream;
    private final ComplexFileTable _cft;
    private final StringBuilder _text;
    private final EscherRecordHolder _escherRecordHolder;
    private final PicturesTable _pictures;
    @Deprecated
    private final ShapesTable _officeArts;
    private final OfficeDrawingsImpl _officeDrawingsHeaders;
    private final OfficeDrawingsImpl _officeDrawingsMain;
    private final BookmarksTables _bookmarksTables;
    private final Bookmarks _bookmarks;
    private final FieldsTables _fieldsTables;
    private final Fields _fields;
    private final PlcfTxbxBkd txbxBkd;
    private byte[] _dataStream;

    public HWPFDocument(InputStream istream) throws IOException {
        super(istream);

        CPSplitCalculator _cpSplit = new CPSplitCalculator(_fib);

        if (_fib.getNFib() < 106) {
            if (_fib.getNFib() == 0) {
                throw new NullPointerException();
            }
            throw new OldWordFileFormatException(
                    "The document is too old - Word 95 or older. Try HWPFOldDocument instead?");
        }

        String name = STREAM_TABLE_0;
        if (_fib.isFWhichTblStm()) {
            name = STREAM_TABLE_1;
        }

        try {
            _tableStream = cfbFS.getPropertyRawData(name);
        } catch (Exception fnfe) {
            throw new IllegalStateException("Table Stream '" + name
                    + "' wasn't found - Either the document is corrupt, or is Word95 (or earlier)");
        }

        _fib.fillVariableFields(_mainStream, _tableStream);

        try {

            _dataStream = cfbFS.getPropertyRawData(STREAM_DATA);
        } catch (Exception e) {
            _dataStream = new byte[0];
        }

        int fcMin = 0;

        _cft = new ComplexFileTable(_mainStream, _tableStream, _fib.getFcClx(), fcMin);
        TextPieceTable _tpt = _cft.getTextPieceTable();

        _cbt = new CHPBinTable(_mainStream, _tableStream, _fib.getFcPlcfbteChpx(), _fib.getLcbPlcfbteChpx(), _tpt);
        _pbt = new PAPBinTable(_mainStream, _tableStream, _dataStream, _fib.getFcPlcfbtePapx(), _fib.getLcbPlcfbtePapx(), _tpt);

        _text = _tpt.getText();

        {
            _cbt.rebuild(_cft);
            _pbt.rebuild(_text, _cft);
        }

        _fspaHeaders = new FSPATable(_tableStream, _fib, FSPADocumentPart.HEADER);
        _fspaMain = new FSPATable(_tableStream, _fib, FSPADocumentPart.MAIN);

        if (_fib.getFcDggInfo() != 0) {
            _escherRecordHolder = new EscherRecordHolder(_tableStream, _fib.getFcDggInfo(),
                    _fib.getLcbDggInfo());
        } else {
            _escherRecordHolder = new EscherRecordHolder();
        }

        _pictures = new PicturesTable(this, _dataStream, _mainStream, _fspaMain,
                _escherRecordHolder);
        _officeArts = new ShapesTable(_tableStream, _fib);

        _officeDrawingsHeaders = new OfficeDrawingsImpl(_fspaHeaders, _escherRecordHolder,
                _mainStream);
        _officeDrawingsMain = new OfficeDrawingsImpl(_fspaMain, _escherRecordHolder, _mainStream);

        _st = new SectionTable(_mainStream, _tableStream, _fib.getFcPlcfsed(),
                _fib.getLcbPlcfsed(), fcMin, _tpt, _cpSplit);
        _ss = new StyleSheet(_tableStream, _fib.getFcStshf());
        _ft = new FontTable(_tableStream, _fib.getFcSttbfffn(), _fib.getLcbSttbfffn());

        int listOffset = _fib.getFcPlcfLst();
        int lfoOffset = _fib.getFcPlfLfo();
        if (listOffset != 0 && _fib.getLcbPlcfLst() != 0) {
            _lt = new ListTables(_tableStream, _fib.getFcPlcfLst(), _fib.getFcPlfLfo());
        }

        _bookmarksTables = new BookmarksTables(_tableStream, _fib);
        _bookmarks = new BookmarksImpl(_bookmarksTables);

        _fieldsTables = new FieldsTables(_tableStream, _fib);
        _fields = new FieldsImpl(_fieldsTables);

        txbxBkd = new PlcfTxbxBkd(_tableStream, _fib.getFcPlcfTxbxBkd(), _fib.getLcbPlcfTxbxBkd());
    }

    @Internal
    public TextPieceTable getTextTable() {
        return _cft.getTextPieceTable();
    }

    @Internal
    @Override
    public StringBuilder getText() {
        return _text;
    }

    public Range getOverallRange() {
        return new Range(0, _text.length(), this);
    }

    public Range getRange() {

        return getRange(SubdocumentType.MAIN);
    }

    private Range getRange(SubdocumentType subdocument) {
        int startCp = 0;
        for (SubdocumentType previos : SubdocumentType.ORDERED) {
            int length = getFileInformationBlock().getSubdocumentTextStreamLength(previos);
            if (subdocument == previos) {
                return new Range(startCp, startCp + length, this);
            }
            startCp += length;
        }
        throw new UnsupportedOperationException("Subdocument type not supported: " + subdocument);
    }

    public Range getFootnoteRange() {
        return getRange(SubdocumentType.FOOTNOTE);
    }

    public Range getEndnoteRange() {
        return getRange(SubdocumentType.ENDNOTE);
    }

    public Range getCommentsRange() {
        return getRange(SubdocumentType.ANNOTATION);
    }

    public Range getMainTextboxRange() {
        return getRange(SubdocumentType.TEXTBOX);
    }

    public Range getHeaderStoryRange() {
        return getRange(SubdocumentType.HEADER);
    }

    public int characterLength() {
        return _text.length();
    }

    public PicturesTable getPicturesTable() {
        return _pictures;
    }

    @Internal
    public EscherRecordHolder getEscherRecordHolder() {
        return _escherRecordHolder;
    }

    @Deprecated
    @Internal
    public ShapesTable getShapesTable() {
        return _officeArts;
    }

    public OfficeDrawings getOfficeDrawingsHeaders() {
        return _officeDrawingsHeaders;
    }

    public OfficeDrawings getOfficeDrawingsMain() {
        return _officeDrawingsMain;
    }

    public Bookmarks getBookmarks() {
        return _bookmarks;
    }

    @Deprecated
    @Internal
    public FieldsTables getFieldsTables() {
        return _fieldsTables;
    }

    public Fields getFields() {
        return _fields;
    }

    @Internal
    public byte[] getDataStream() {
        return _dataStream;
    }

    @Internal
    public byte[] getTableStream() {
        return _tableStream;
    }

    public int registerList(HWPFList list) {
        if (_lt == null) {
            _lt = new ListTables();
        }
        return _lt.addList(list.getListData(), list.getOverride());
    }

    public void delete(int start, int length) {
        Range r = new Range(start, start + length, this);
        r.delete();
    }

    public int getTextboxStart(int txbx) {
        return txbxBkd.getCharPosition(txbx);
    }

    public int getTextboxEnd(int txbx) {
        return txbxBkd.getCharPosition(txbx + 1);
    }
}
