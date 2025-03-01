package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hwpf.model.types;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hwpf.model.HDFType;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.BitField;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.Internal;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.LittleEndian;

@Internal
public abstract class DOPAbstractType implements HDFType {

    private static final BitField fFacingPages = new BitField(0x01);
    private static final BitField fWidowControl = new BitField(0x02);
    private static final BitField fPMHMainDoc = new BitField(0x04);
    private static final BitField grfSupression = new BitField(0x18);
    private static final BitField fpc = new BitField(0x60);
    private static final BitField unused1 = new BitField(0x80);
    private static final BitField rncFtn = new BitField(0x0003);
    private static final BitField nFtn = new BitField(0xfffc);
    private static final BitField fOnlyMacPics = new BitField(0x01);
    private static final BitField fOnlyWinPics = new BitField(0x02);
    private static final BitField fLabelDoc = new BitField(0x04);
    private static final BitField fHyphCapitals = new BitField(0x08);
    private static final BitField fAutoHyphen = new BitField(0x10);
    private static final BitField fFormNoFields = new BitField(0x20);
    private static final BitField fLinkStyles = new BitField(0x40);
    private static final BitField fRevMarking = new BitField(0x80);
    private static final BitField fBackup = new BitField(0x01);
    private static final BitField fExactCWords = new BitField(0x02);
    private static final BitField fPagHidden = new BitField(0x04);
    private static final BitField fPagResults = new BitField(0x08);
    private static final BitField fLockAtn = new BitField(0x10);
    private static final BitField fMirrorMargins = new BitField(0x20);
    private static final BitField unused3 = new BitField(0x40);
    private static final BitField fDfltTrueType = new BitField(0x80);
    private static final BitField fPagSupressTopSpacing = new BitField(0x01);
    private static final BitField fProtEnabled = new BitField(0x02);
    private static final BitField fDispFormFldSel = new BitField(0x04);
    private static final BitField fRMView = new BitField(0x08);
    private static final BitField fRMPrint = new BitField(0x10);
    private static final BitField unused4 = new BitField(0x20);
    private static final BitField fLockRev = new BitField(0x40);
    private static final BitField fEmbedFonts = new BitField(0x80);
    private static final BitField oldfNoTabForInd = new BitField(0x0001);
    private static final BitField oldfNoSpaceRaiseLower = new BitField(0x0002);
    private static final BitField oldfSuppressSpbfAfterPageBreak = new BitField(0x0004);
    private static final BitField oldfWrapTrailSpaces = new BitField(0x0008);
    private static final BitField oldfMapPrintTextColor = new BitField(0x0010);
    private static final BitField oldfNoColumnBalance = new BitField(0x0020);
    private static final BitField oldfConvMailMergeEsc = new BitField(0x0040);
    private static final BitField oldfSupressTopSpacing = new BitField(0x0080);
    private static final BitField oldfOrigWordTableRules = new BitField(0x0100);
    private static final BitField oldfTransparentMetafiles = new BitField(0x0200);
    private static final BitField oldfShowBreaksInFrames = new BitField(0x0400);
    private static final BitField oldfSwapBordersFacingPgs = new BitField(0x0800);
    private static final BitField unused5 = new BitField(0xf000);
    private static final BitField rncEdn = new BitField(0x0003);
    private static final BitField nEdn = new BitField(0xfffc);
    private static final BitField epc = new BitField(0x0003);
    private static final BitField nfcFtnRef1 = new BitField(0x003c);
    private static final BitField nfcEdnRef1 = new BitField(0x03c0);
    private static final BitField fPrintFormData = new BitField(0x0400);
    private static final BitField fSaveFormData = new BitField(0x0800);
    private static final BitField fShadeFormData = new BitField(0x1000);
    private static final BitField fWCFtnEdn = new BitField(0x8000);
    private static final BitField wvkSaved = new BitField(0x0007);
    private static final BitField wScaleSaved = new BitField(0x0ff8);
    private static final BitField zkSaved = new BitField(0x3000);
    private static final BitField fRotateFontW6 = new BitField(0x4000);
    private static final BitField iGutterPos = new BitField(0x8000);
    private static final BitField fNoTabForInd = new BitField(0x00000001);
    private static final BitField fNoSpaceRaiseLower = new BitField(0x00000002);
    private static final BitField fSupressSpdfAfterPageBreak = new BitField(0x00000004);
    private static final BitField fWrapTrailSpaces = new BitField(0x00000008);
    private static final BitField fMapPrintTextColor = new BitField(0x00000010);
    private static final BitField fNoColumnBalance = new BitField(0x00000020);
    private static final BitField fConvMailMergeEsc = new BitField(0x00000040);
    private static final BitField fSupressTopSpacing = new BitField(0x00000080);
    private static final BitField fOrigWordTableRules = new BitField(0x00000100);
    private static final BitField fTransparentMetafiles = new BitField(0x00000200);
    private static final BitField fShowBreaksInFrames = new BitField(0x00000400);
    private static final BitField fSwapBordersFacingPgs = new BitField(0x00000800);
    private static final BitField fSuppressTopSPacingMac5 = new BitField(0x00010000);
    private static final BitField fTruncDxaExpand = new BitField(0x00020000);
    private static final BitField fPrintBodyBeforeHdr = new BitField(0x00040000);
    private static final BitField fNoLeading = new BitField(0x00080000);
    private static final BitField fMWSmallCaps = new BitField(0x00200000);
    private static final BitField lvl = new BitField(0x001e);
    private static final BitField fGramAllDone = new BitField(0x0020);
    private static final BitField fGramAllClean = new BitField(0x0040);
    private static final BitField fSubsetFonts = new BitField(0x0080);
    private static final BitField fHideLastVersion = new BitField(0x0100);
    private static final BitField fHtmlDoc = new BitField(0x0200);
    private static final BitField fSnapBorder = new BitField(0x0800);
    private static final BitField fIncludeHeader = new BitField(0x1000);
    private static final BitField fIncludeFooter = new BitField(0x2000);
    private static final BitField fForcePageSizePag = new BitField(0x4000);
    private static final BitField fMinFontSizePag = new BitField(0x8000);
    private static final BitField fHaveVersions = new BitField(0x0001);
    private static final BitField fAutoVersions = new BitField(0x0002);
    private static final BitField fVirusPrompted = new BitField(0x0001);
    private static final BitField fVirusLoadSafe = new BitField(0x0002);
    private static final BitField KeyVirusSession30 = new BitField(0xfffffffc);
    protected byte field_1_formatFlags;
    protected byte field_2_unused2;
    protected short field_3_footnoteInfo;
    protected byte field_4_fOutlineDirtySave;
    protected byte field_5_docinfo;
    protected byte field_6_docinfo1;
    protected byte field_7_docinfo2;
    protected short field_8_docinfo3;
    protected int field_9_dxaTab;
    protected int field_10_wSpare;
    protected int field_11_dxaHotz;
    protected int field_12_cConsexHypLim;
    protected int field_13_wSpare2;
    protected int field_14_dttmCreated;
    protected int field_15_dttmRevised;
    protected int field_16_dttmLastPrint;
    protected int field_17_nRevision;
    protected int field_18_tmEdited;
    protected int field_19_cWords;
    protected int field_20_cCh;
    protected int field_21_cPg;
    protected int field_22_cParas;
    protected short field_23_Edn;
    protected short field_24_Edn1;
    protected int field_25_cLines;
    protected int field_26_cWordsFtnEnd;
    protected int field_27_cChFtnEdn;
    protected short field_28_cPgFtnEdn;
    protected int field_29_cParasFtnEdn;
    protected int field_30_cLinesFtnEdn;
    protected int field_31_lKeyProtDoc;
    protected short field_32_view;
    protected int field_33_docinfo4;
    protected short field_34_adt;
    protected byte[] field_35_doptypography;
    protected byte[] field_36_dogrid;
    protected short field_37_docinfo5;
    protected short field_38_docinfo6;
    protected byte[] field_39_asumyi;
    protected int field_40_cChWS;
    protected int field_41_cChWSFtnEdn;
    protected int field_42_grfDocEvents;
    protected int field_43_virusinfo;
    protected byte[] field_44_Spare;
    protected int field_45_reserved1;
    protected int field_46_reserved2;
    protected int field_47_cDBC;
    protected int field_48_cDBCFtnEdn;
    protected int field_49_reserved;
    protected short field_50_nfcFtnRef;
    protected short field_51_nfcEdnRef;
    protected short field_52_hpsZoonFontPag;
    protected short field_53_dywDispPag;

    protected DOPAbstractType() {
        this.field_35_doptypography = new byte[0];
        this.field_36_dogrid = new byte[0];
        this.field_39_asumyi = new byte[0];
        this.field_44_Spare = new byte[0];
    }

    public static int getSize() {
        return 1 + 1 + 2 + 1 + 1 + 1 + 1 + 2 + 2 + 2 + 2 + 2 + 2 + 4 + 4 + 4 + 2 + 4 + 4 + 4 + 2 + 4 + 2 + 2 + 4 + 4 + 4 + 2 + 4 + 4 + 4 + 2 + 4 + 2 + 310 + 10 + 2 + 2 + 12 + 4 + 4 + 4 + 4 + 30 + 4 + 4 + 4 + 4 + 4 + 2 + 2 + 2 + 2;
    }

    protected void fillFields(byte[] data, int offset) {
        field_1_formatFlags = data[offset];
        field_2_unused2 = data[0x1 + offset];
        field_3_footnoteInfo = LittleEndian.getShort(data, 0x2 + offset);
        field_4_fOutlineDirtySave = data[0x4 + offset];
        field_5_docinfo = data[0x5 + offset];
        field_6_docinfo1 = data[0x6 + offset];
        field_7_docinfo2 = data[0x7 + offset];
        field_8_docinfo3 = LittleEndian.getShort(data, 0x8 + offset);
        field_9_dxaTab = LittleEndian.getShort(data, 0xa + offset);
        field_10_wSpare = LittleEndian.getShort(data, 0xc + offset);
        field_11_dxaHotz = LittleEndian.getShort(data, 0xe + offset);
        field_12_cConsexHypLim = LittleEndian.getShort(data, 0x10 + offset);
        field_13_wSpare2 = LittleEndian.getShort(data, 0x12 + offset);
        field_14_dttmCreated = LittleEndian.getInt(data, 0x14 + offset);
        field_15_dttmRevised = LittleEndian.getInt(data, 0x18 + offset);
        field_16_dttmLastPrint = LittleEndian.getInt(data, 0x1c + offset);
        field_17_nRevision = LittleEndian.getShort(data, 0x20 + offset);
        field_18_tmEdited = LittleEndian.getInt(data, 0x22 + offset);
        field_19_cWords = LittleEndian.getInt(data, 0x26 + offset);
        field_20_cCh = LittleEndian.getInt(data, 0x2a + offset);
        field_21_cPg = LittleEndian.getShort(data, 0x2e + offset);
        field_22_cParas = LittleEndian.getInt(data, 0x30 + offset);
        field_23_Edn = LittleEndian.getShort(data, 0x34 + offset);
        field_24_Edn1 = LittleEndian.getShort(data, 0x36 + offset);
        field_25_cLines = LittleEndian.getInt(data, 0x38 + offset);
        field_26_cWordsFtnEnd = LittleEndian.getInt(data, 0x3c + offset);
        field_27_cChFtnEdn = LittleEndian.getInt(data, 0x40 + offset);
        field_28_cPgFtnEdn = LittleEndian.getShort(data, 0x44 + offset);
        field_29_cParasFtnEdn = LittleEndian.getInt(data, 0x46 + offset);
        field_30_cLinesFtnEdn = LittleEndian.getInt(data, 0x4a + offset);
        field_31_lKeyProtDoc = LittleEndian.getInt(data, 0x4e + offset);
        field_32_view = LittleEndian.getShort(data, 0x52 + offset);
        field_33_docinfo4 = LittleEndian.getInt(data, 0x54 + offset);
        field_34_adt = LittleEndian.getShort(data, 0x58 + offset);
        field_35_doptypography = LittleEndian.getByteArray(data, 0x5a + offset, 310);
        field_36_dogrid = LittleEndian.getByteArray(data, 0x190 + offset, 10);
        field_37_docinfo5 = LittleEndian.getShort(data, 0x19a + offset);
        field_38_docinfo6 = LittleEndian.getShort(data, 0x19c + offset);
        field_39_asumyi = LittleEndian.getByteArray(data, 0x19e + offset, 12);
        field_40_cChWS = LittleEndian.getInt(data, 0x1aa + offset);
        field_41_cChWSFtnEdn = LittleEndian.getInt(data, 0x1ae + offset);
        field_42_grfDocEvents = LittleEndian.getInt(data, 0x1b2 + offset);
        field_43_virusinfo = LittleEndian.getInt(data, 0x1b6 + offset);
        field_44_Spare = LittleEndian.getByteArray(data, 0x1ba + offset, 30);
        field_45_reserved1 = LittleEndian.getInt(data, 0x1d8 + offset);
        field_46_reserved2 = LittleEndian.getInt(data, 0x1dc + offset);
        field_47_cDBC = LittleEndian.getInt(data, 0x1e0 + offset);
        field_48_cDBCFtnEdn = LittleEndian.getInt(data, 0x1e4 + offset);
        field_49_reserved = LittleEndian.getInt(data, 0x1e8 + offset);
        field_50_nfcFtnRef = LittleEndian.getShort(data, 0x1ec + offset);
        field_51_nfcEdnRef = LittleEndian.getShort(data, 0x1ee + offset);
        field_52_hpsZoonFontPag = LittleEndian.getShort(data, 0x1f0 + offset);
        field_53_dywDispPag = LittleEndian.getShort(data, 0x1f2 + offset);
    }

    public void serialize(byte[] data, int offset) {
        data[offset] = field_1_formatFlags;
        data[0x1 + offset] = field_2_unused2;
        LittleEndian.putShort(data, 0x2 + offset, field_3_footnoteInfo);
        data[0x4 + offset] = field_4_fOutlineDirtySave;
        data[0x5 + offset] = field_5_docinfo;
        data[0x6 + offset] = field_6_docinfo1;
        data[0x7 + offset] = field_7_docinfo2;
        LittleEndian.putShort(data, 0x8 + offset, field_8_docinfo3);
        LittleEndian.putShort(data, 0xa + offset, (short) field_9_dxaTab);
        LittleEndian.putShort(data, 0xc + offset, (short) field_10_wSpare);
        LittleEndian.putShort(data, 0xe + offset, (short) field_11_dxaHotz);
        LittleEndian.putShort(data, 0x10 + offset, (short) field_12_cConsexHypLim);
        LittleEndian.putShort(data, 0x12 + offset, (short) field_13_wSpare2);
        LittleEndian.putInt(data, 0x14 + offset, field_14_dttmCreated);
        LittleEndian.putInt(data, 0x18 + offset, field_15_dttmRevised);
        LittleEndian.putInt(data, 0x1c + offset, field_16_dttmLastPrint);
        LittleEndian.putShort(data, 0x20 + offset, (short) field_17_nRevision);
        LittleEndian.putInt(data, 0x22 + offset, field_18_tmEdited);
        LittleEndian.putInt(data, 0x26 + offset, field_19_cWords);
        LittleEndian.putInt(data, 0x2a + offset, field_20_cCh);
        LittleEndian.putShort(data, 0x2e + offset, (short) field_21_cPg);
        LittleEndian.putInt(data, 0x30 + offset, field_22_cParas);
        LittleEndian.putShort(data, 0x34 + offset, field_23_Edn);
        LittleEndian.putShort(data, 0x36 + offset, field_24_Edn1);
        LittleEndian.putInt(data, 0x38 + offset, field_25_cLines);
        LittleEndian.putInt(data, 0x3c + offset, field_26_cWordsFtnEnd);
        LittleEndian.putInt(data, 0x40 + offset, field_27_cChFtnEdn);
        LittleEndian.putShort(data, 0x44 + offset, field_28_cPgFtnEdn);
        LittleEndian.putInt(data, 0x46 + offset, field_29_cParasFtnEdn);
        LittleEndian.putInt(data, 0x4a + offset, field_30_cLinesFtnEdn);
        LittleEndian.putInt(data, 0x4e + offset, field_31_lKeyProtDoc);
        LittleEndian.putShort(data, 0x52 + offset, field_32_view);
        LittleEndian.putInt(data, 0x54 + offset, field_33_docinfo4);
        LittleEndian.putShort(data, 0x58 + offset, field_34_adt);
        System.arraycopy(field_35_doptypography, 0, data, 0x5a + offset,
                field_35_doptypography.length);
        System.arraycopy(field_36_dogrid, 0, data, 0x190 + offset, field_36_dogrid.length);
        LittleEndian.putShort(data, 0x19a + offset, field_37_docinfo5);
        LittleEndian.putShort(data, 0x19c + offset, field_38_docinfo6);
        System.arraycopy(field_39_asumyi, 0, data, 0x19e + offset, field_39_asumyi.length);
        LittleEndian.putInt(data, 0x1aa + offset, field_40_cChWS);
        LittleEndian.putInt(data, 0x1ae + offset, field_41_cChWSFtnEdn);
        LittleEndian.putInt(data, 0x1b2 + offset, field_42_grfDocEvents);
        LittleEndian.putInt(data, 0x1b6 + offset, field_43_virusinfo);
        System.arraycopy(field_44_Spare, 0, data, 0x1ba + offset, field_44_Spare.length);
        LittleEndian.putInt(data, 0x1d8 + offset, field_45_reserved1);
        LittleEndian.putInt(data, 0x1dc + offset, field_46_reserved2);
        LittleEndian.putInt(data, 0x1e0 + offset, field_47_cDBC);
        LittleEndian.putInt(data, 0x1e4 + offset, field_48_cDBCFtnEdn);
        LittleEndian.putInt(data, 0x1e8 + offset, field_49_reserved);
        LittleEndian.putShort(data, 0x1ec + offset, field_50_nfcFtnRef);
        LittleEndian.putShort(data, 0x1ee + offset, field_51_nfcEdnRef);
        LittleEndian.putShort(data, 0x1f0 + offset, field_52_hpsZoonFontPag);
        LittleEndian.putShort(data, 0x1f2 + offset, field_53_dywDispPag);
    }

    public String toString() {

        String builder = "[DOP]\n" +
                "    .formatFlags          = " +
                " (" + getFormatFlags() + " )\n" +
                "         .fFacingPages             = " + isFFacingPages() +
                '\n' +
                "         .fWidowControl            = " + isFWidowControl() +
                '\n' +
                "         .fPMHMainDoc              = " + isFPMHMainDoc() +
                '\n' +
                "         .grfSupression            = " + getGrfSupression() +
                '\n' +
                "         .fpc                      = " + getFpc() + '\n' +
                "         .unused1                  = " + isUnused1() + '\n' +
                "    .unused2              = " +
                " (" + getUnused2() + " )\n" +
                "    .footnoteInfo         = " +
                " (" + getFootnoteInfo() + " )\n" +
                "         .rncFtn                   = " + getRncFtn() + '\n' +
                "         .nFtn                     = " + getNFtn() + '\n' +
                "    .fOutlineDirtySave    = " +
                " (" + getFOutlineDirtySave() + " )\n" +
                "    .docinfo              = " +
                " (" + getDocinfo() + " )\n" +
                "         .fOnlyMacPics             = " + isFOnlyMacPics() +
                '\n' +
                "         .fOnlyWinPics             = " + isFOnlyWinPics() +
                '\n' +
                "         .fLabelDoc                = " + isFLabelDoc() + '\n' +
                "         .fHyphCapitals            = " + isFHyphCapitals() +
                '\n' +
                "         .fAutoHyphen              = " + isFAutoHyphen() +
                '\n' +
                "         .fFormNoFields            = " + isFFormNoFields() +
                '\n' +
                "         .fLinkStyles              = " + isFLinkStyles() +
                '\n' +
                "         .fRevMarking              = " + isFRevMarking() +
                '\n' +
                "    .docinfo1             = " +
                " (" + getDocinfo1() + " )\n" +
                "         .fBackup                  = " + isFBackup() + '\n' +
                "         .fExactCWords             = " + isFExactCWords() +
                '\n' +
                "         .fPagHidden               = " + isFPagHidden() + '\n' +
                "         .fPagResults              = " + isFPagResults() +
                '\n' +
                "         .fLockAtn                 = " + isFLockAtn() + '\n' +
                "         .fMirrorMargins           = " + isFMirrorMargins() +
                '\n' +
                "         .unused3                  = " + isUnused3() + '\n' +
                "         .fDfltTrueType            = " + isFDfltTrueType() +
                '\n' +
                "    .docinfo2             = " +
                " (" + getDocinfo2() + " )\n" +
                "         .fPagSupressTopSpacing     = " + isFPagSupressTopSpacing() +
                '\n' +
                "         .fProtEnabled             = " + isFProtEnabled() +
                '\n' +
                "         .fDispFormFldSel          = " + isFDispFormFldSel() +
                '\n' +
                "         .fRMView                  = " + isFRMView() + '\n' +
                "         .fRMPrint                 = " + isFRMPrint() + '\n' +
                "         .unused4                  = " + isUnused4() + '\n' +
                "         .fLockRev                 = " + isFLockRev() + '\n' +
                "         .fEmbedFonts              = " + isFEmbedFonts() +
                '\n' +
                "    .docinfo3             = " +
                " (" + getDocinfo3() + " )\n" +
                "         .oldfNoTabForInd          = " + isOldfNoTabForInd() +
                '\n' +
                "         .oldfNoSpaceRaiseLower     = " + isOldfNoSpaceRaiseLower() +
                '\n' +
                "         .oldfSuppressSpbfAfterPageBreak     = " +
                isOldfSuppressSpbfAfterPageBreak() + '\n' +
                "         .oldfWrapTrailSpaces      = " + isOldfWrapTrailSpaces() +
                '\n' +
                "         .oldfMapPrintTextColor     = " + isOldfMapPrintTextColor() +
                '\n' +
                "         .oldfNoColumnBalance      = " + isOldfNoColumnBalance() +
                '\n' +
                "         .oldfConvMailMergeEsc     = " + isOldfConvMailMergeEsc() +
                '\n' +
                "         .oldfSupressTopSpacing     = " + isOldfSupressTopSpacing() +
                '\n' +
                "         .oldfOrigWordTableRules     = " +
                isOldfOrigWordTableRules() + '\n' +
                "         .oldfTransparentMetafiles     = " +
                isOldfTransparentMetafiles() + '\n' +
                "         .oldfShowBreaksInFrames     = " +
                isOldfShowBreaksInFrames() + '\n' +
                "         .oldfSwapBordersFacingPgs     = " +
                isOldfSwapBordersFacingPgs() + '\n' +
                "         .unused5                  = " + getUnused5() + '\n' +
                "    .dxaTab               = " +
                " (" + getDxaTab() + " )\n" +
                "    .wSpare               = " +
                " (" + getWSpare() + " )\n" +
                "    .dxaHotz              = " +
                " (" + getDxaHotz() + " )\n" +
                "    .cConsexHypLim        = " +
                " (" + getCConsexHypLim() + " )\n" +
                "    .wSpare2              = " +
                " (" + getWSpare2() + " )\n" +
                "    .dttmCreated          = " +
                " (" + getDttmCreated() + " )\n" +
                "    .dttmRevised          = " +
                " (" + getDttmRevised() + " )\n" +
                "    .dttmLastPrint        = " +
                " (" + getDttmLastPrint() + " )\n" +
                "    .nRevision            = " +
                " (" + getNRevision() + " )\n" +
                "    .tmEdited             = " +
                " (" + getTmEdited() + " )\n" +
                "    .cWords               = " +
                " (" + getCWords() + " )\n" +
                "    .cCh                  = " +
                " (" + getCCh() + " )\n" +
                "    .cPg                  = " +
                " (" + getCPg() + " )\n" +
                "    .cParas               = " +
                " (" + getCParas() + " )\n" +
                "    .Edn                  = " +
                " (" + getEdn() + " )\n" +
                "         .rncEdn                   = " + getRncEdn() + '\n' +
                "         .nEdn                     = " + getNEdn() + '\n' +
                "    .Edn1                 = " +
                " (" + getEdn1() + " )\n" +
                "         .epc                      = " + getEpc() + '\n' +
                "         .nfcFtnRef1               = " + getNfcFtnRef1() +
                '\n' +
                "         .nfcEdnRef1               = " + getNfcEdnRef1() +
                '\n' +
                "         .fPrintFormData           = " + isFPrintFormData() +
                '\n' +
                "         .fSaveFormData            = " + isFSaveFormData() +
                '\n' +
                "         .fShadeFormData           = " + isFShadeFormData() +
                '\n' +
                "         .fWCFtnEdn                = " + isFWCFtnEdn() + '\n' +
                "    .cLines               = " +
                " (" + getCLines() + " )\n" +
                "    .cWordsFtnEnd         = " +
                " (" + getCWordsFtnEnd() + " )\n" +
                "    .cChFtnEdn            = " +
                " (" + getCChFtnEdn() + " )\n" +
                "    .cPgFtnEdn            = " +
                " (" + getCPgFtnEdn() + " )\n" +
                "    .cParasFtnEdn         = " +
                " (" + getCParasFtnEdn() + " )\n" +
                "    .cLinesFtnEdn         = " +
                " (" + getCLinesFtnEdn() + " )\n" +
                "    .lKeyProtDoc          = " +
                " (" + getLKeyProtDoc() + " )\n" +
                "    .view                 = " +
                " (" + getView() + " )\n" +
                "         .wvkSaved                 = " + getWvkSaved() + '\n' +
                "         .wScaleSaved              = " + getWScaleSaved() +
                '\n' +
                "         .zkSaved                  = " + getZkSaved() + '\n' +
                "         .fRotateFontW6            = " + isFRotateFontW6() +
                '\n' +
                "         .iGutterPos               = " + isIGutterPos() + '\n' +
                "    .docinfo4             = " +
                " (" + getDocinfo4() + " )\n" +
                "         .fNoTabForInd             = " + isFNoTabForInd() +
                '\n' +
                "         .fNoSpaceRaiseLower       = " + isFNoSpaceRaiseLower() +
                '\n' +
                "         .fSupressSpdfAfterPageBreak     = " +
                isFSupressSpdfAfterPageBreak() + '\n' +
                "         .fWrapTrailSpaces         = " + isFWrapTrailSpaces() +
                '\n' +
                "         .fMapPrintTextColor       = " + isFMapPrintTextColor() +
                '\n' +
                "         .fNoColumnBalance         = " + isFNoColumnBalance() +
                '\n' +
                "         .fConvMailMergeEsc        = " + isFConvMailMergeEsc() +
                '\n' +
                "         .fSupressTopSpacing       = " + isFSupressTopSpacing() +
                '\n' +
                "         .fOrigWordTableRules      = " + isFOrigWordTableRules() +
                '\n' +
                "         .fTransparentMetafiles     = " + isFTransparentMetafiles() +
                '\n' +
                "         .fShowBreaksInFrames      = " + isFShowBreaksInFrames() +
                '\n' +
                "         .fSwapBordersFacingPgs     = " + isFSwapBordersFacingPgs() +
                '\n' +
                "         .fSuppressTopSPacingMac5     = " +
                isFSuppressTopSPacingMac5() + '\n' +
                "         .fTruncDxaExpand          = " + isFTruncDxaExpand() +
                '\n' +
                "         .fPrintBodyBeforeHdr      = " + isFPrintBodyBeforeHdr() +
                '\n' +
                "         .fNoLeading               = " + isFNoLeading() + '\n' +
                "         .fMWSmallCaps             = " + isFMWSmallCaps() +
                '\n' +
                "    .adt                  = " +
                " (" + getAdt() + " )\n" +
                "    .doptypography        = " +
                " (" + getDoptypography() + " )\n" +
                "    .dogrid               = " +
                " (" + getDogrid() + " )\n" +
                "    .docinfo5             = " +
                " (" + getDocinfo5() + " )\n" +
                "         .lvl                      = " + getLvl() + '\n' +
                "         .fGramAllDone             = " + isFGramAllDone() +
                '\n' +
                "         .fGramAllClean            = " + isFGramAllClean() +
                '\n' +
                "         .fSubsetFonts             = " + isFSubsetFonts() +
                '\n' +
                "         .fHideLastVersion         = " + isFHideLastVersion() +
                '\n' +
                "         .fHtmlDoc                 = " + isFHtmlDoc() + '\n' +
                "         .fSnapBorder              = " + isFSnapBorder() +
                '\n' +
                "         .fIncludeHeader           = " + isFIncludeHeader() +
                '\n' +
                "         .fIncludeFooter           = " + isFIncludeFooter() +
                '\n' +
                "         .fForcePageSizePag        = " + isFForcePageSizePag() +
                '\n' +
                "         .fMinFontSizePag          = " + isFMinFontSizePag() +
                '\n' +
                "    .docinfo6             = " +
                " (" + getDocinfo6() + " )\n" +
                "         .fHaveVersions            = " + isFHaveVersions() +
                '\n' +
                "         .fAutoVersions            = " + isFAutoVersions() +
                '\n' +
                "    .asumyi               = " +
                " (" + getAsumyi() + " )\n" +
                "    .cChWS                = " +
                " (" + getCChWS() + " )\n" +
                "    .cChWSFtnEdn          = " +
                " (" + getCChWSFtnEdn() + " )\n" +
                "    .grfDocEvents         = " +
                " (" + getGrfDocEvents() + " )\n" +
                "    .virusinfo            = " +
                " (" + getVirusinfo() + " )\n" +
                "         .fVirusPrompted           = " + isFVirusPrompted() +
                '\n' +
                "         .fVirusLoadSafe           = " + isFVirusLoadSafe() +
                '\n' +
                "         .KeyVirusSession30        = " + getKeyVirusSession30() +
                '\n' +
                "    .Spare                = " +
                " (" + getSpare() + " )\n" +
                "    .reserved1            = " +
                " (" + getReserved1() + " )\n" +
                "    .reserved2            = " +
                " (" + getReserved2() + " )\n" +
                "    .cDBC                 = " +
                " (" + getCDBC() + " )\n" +
                "    .cDBCFtnEdn           = " +
                " (" + getCDBCFtnEdn() + " )\n" +
                "    .reserved             = " +
                " (" + getReserved() + " )\n" +
                "    .nfcFtnRef            = " +
                " (" + getNfcFtnRef() + " )\n" +
                "    .nfcEdnRef            = " +
                " (" + getNfcEdnRef() + " )\n" +
                "    .hpsZoonFontPag       = " +
                " (" + getHpsZoonFontPag() + " )\n" +
                "    .dywDispPag           = " +
                " (" + getDywDispPag() + " )\n" +
                "[/DOP]\n";
        return builder;
    }

    @Internal
    public byte getFormatFlags() {
        return field_1_formatFlags;
    }

    @Internal
    public void setFormatFlags(byte field_1_formatFlags) {
        this.field_1_formatFlags = field_1_formatFlags;
    }

    @Internal
    public byte getUnused2() {
        return field_2_unused2;
    }

    @Internal
    public void setUnused2(byte field_2_unused2) {
        this.field_2_unused2 = field_2_unused2;
    }

    @Internal
    public short getFootnoteInfo() {
        return field_3_footnoteInfo;
    }

    @Internal
    public void setFootnoteInfo(short field_3_footnoteInfo) {
        this.field_3_footnoteInfo = field_3_footnoteInfo;
    }

    @Internal
    public byte getFOutlineDirtySave() {
        return field_4_fOutlineDirtySave;
    }

    @Internal
    public void setFOutlineDirtySave(byte field_4_fOutlineDirtySave) {
        this.field_4_fOutlineDirtySave = field_4_fOutlineDirtySave;
    }

    @Internal
    public byte getDocinfo() {
        return field_5_docinfo;
    }

    @Internal
    public void setDocinfo(byte field_5_docinfo) {
        this.field_5_docinfo = field_5_docinfo;
    }

    @Internal
    public byte getDocinfo1() {
        return field_6_docinfo1;
    }

    @Internal
    public void setDocinfo1(byte field_6_docinfo1) {
        this.field_6_docinfo1 = field_6_docinfo1;
    }

    @Internal
    public byte getDocinfo2() {
        return field_7_docinfo2;
    }

    @Internal
    public void setDocinfo2(byte field_7_docinfo2) {
        this.field_7_docinfo2 = field_7_docinfo2;
    }

    @Internal
    public short getDocinfo3() {
        return field_8_docinfo3;
    }

    @Internal
    public void setDocinfo3(short field_8_docinfo3) {
        this.field_8_docinfo3 = field_8_docinfo3;
    }

    @Internal
    public int getDxaTab() {
        return field_9_dxaTab;
    }

    @Internal
    public void setDxaTab(int field_9_dxaTab) {
        this.field_9_dxaTab = field_9_dxaTab;
    }

    @Internal
    public int getWSpare() {
        return field_10_wSpare;
    }

    @Internal
    public void setWSpare(int field_10_wSpare) {
        this.field_10_wSpare = field_10_wSpare;
    }

    @Internal
    public int getDxaHotz() {
        return field_11_dxaHotz;
    }

    @Internal
    public void setDxaHotz(int field_11_dxaHotz) {
        this.field_11_dxaHotz = field_11_dxaHotz;
    }

    @Internal
    public int getCConsexHypLim() {
        return field_12_cConsexHypLim;
    }

    @Internal
    public void setCConsexHypLim(int field_12_cConsexHypLim) {
        this.field_12_cConsexHypLim = field_12_cConsexHypLim;
    }

    @Internal
    public int getWSpare2() {
        return field_13_wSpare2;
    }

    @Internal
    public void setWSpare2(int field_13_wSpare2) {
        this.field_13_wSpare2 = field_13_wSpare2;
    }

    @Internal
    public int getDttmCreated() {
        return field_14_dttmCreated;
    }

    @Internal
    public void setDttmCreated(int field_14_dttmCreated) {
        this.field_14_dttmCreated = field_14_dttmCreated;
    }

    @Internal
    public int getDttmRevised() {
        return field_15_dttmRevised;
    }

    @Internal
    public void setDttmRevised(int field_15_dttmRevised) {
        this.field_15_dttmRevised = field_15_dttmRevised;
    }

    @Internal
    public int getDttmLastPrint() {
        return field_16_dttmLastPrint;
    }

    @Internal
    public void setDttmLastPrint(int field_16_dttmLastPrint) {
        this.field_16_dttmLastPrint = field_16_dttmLastPrint;
    }

    @Internal
    public int getNRevision() {
        return field_17_nRevision;
    }

    @Internal
    public void setNRevision(int field_17_nRevision) {
        this.field_17_nRevision = field_17_nRevision;
    }

    @Internal
    public int getTmEdited() {
        return field_18_tmEdited;
    }

    @Internal
    public void setTmEdited(int field_18_tmEdited) {
        this.field_18_tmEdited = field_18_tmEdited;
    }

    @Internal
    public int getCWords() {
        return field_19_cWords;
    }

    @Internal
    public void setCWords(int field_19_cWords) {
        this.field_19_cWords = field_19_cWords;
    }

    @Internal
    public int getCCh() {
        return field_20_cCh;
    }

    @Internal
    public void setCCh(int field_20_cCh) {
        this.field_20_cCh = field_20_cCh;
    }

    @Internal
    public int getCPg() {
        return field_21_cPg;
    }

    @Internal
    public void setCPg(int field_21_cPg) {
        this.field_21_cPg = field_21_cPg;
    }

    @Internal
    public int getCParas() {
        return field_22_cParas;
    }

    @Internal
    public void setCParas(int field_22_cParas) {
        this.field_22_cParas = field_22_cParas;
    }

    @Internal
    public short getEdn() {
        return field_23_Edn;
    }

    @Internal
    public void setEdn(short field_23_Edn) {
        this.field_23_Edn = field_23_Edn;
    }

    @Internal
    public short getEdn1() {
        return field_24_Edn1;
    }

    @Internal
    public void setEdn1(short field_24_Edn1) {
        this.field_24_Edn1 = field_24_Edn1;
    }

    @Internal
    public int getCLines() {
        return field_25_cLines;
    }

    @Internal
    public void setCLines(int field_25_cLines) {
        this.field_25_cLines = field_25_cLines;
    }

    @Internal
    public int getCWordsFtnEnd() {
        return field_26_cWordsFtnEnd;
    }

    @Internal
    public void setCWordsFtnEnd(int field_26_cWordsFtnEnd) {
        this.field_26_cWordsFtnEnd = field_26_cWordsFtnEnd;
    }

    @Internal
    public int getCChFtnEdn() {
        return field_27_cChFtnEdn;
    }

    @Internal
    public void setCChFtnEdn(int field_27_cChFtnEdn) {
        this.field_27_cChFtnEdn = field_27_cChFtnEdn;
    }

    @Internal
    public short getCPgFtnEdn() {
        return field_28_cPgFtnEdn;
    }

    @Internal
    public void setCPgFtnEdn(short field_28_cPgFtnEdn) {
        this.field_28_cPgFtnEdn = field_28_cPgFtnEdn;
    }

    @Internal
    public int getCParasFtnEdn() {
        return field_29_cParasFtnEdn;
    }

    @Internal
    public void setCParasFtnEdn(int field_29_cParasFtnEdn) {
        this.field_29_cParasFtnEdn = field_29_cParasFtnEdn;
    }

    @Internal
    public int getCLinesFtnEdn() {
        return field_30_cLinesFtnEdn;
    }

    @Internal
    public void setCLinesFtnEdn(int field_30_cLinesFtnEdn) {
        this.field_30_cLinesFtnEdn = field_30_cLinesFtnEdn;
    }

    @Internal
    public int getLKeyProtDoc() {
        return field_31_lKeyProtDoc;
    }

    @Internal
    public void setLKeyProtDoc(int field_31_lKeyProtDoc) {
        this.field_31_lKeyProtDoc = field_31_lKeyProtDoc;
    }

    @Internal
    public short getView() {
        return field_32_view;
    }

    @Internal
    public void setView(short field_32_view) {
        this.field_32_view = field_32_view;
    }

    @Internal
    public int getDocinfo4() {
        return field_33_docinfo4;
    }

    @Internal
    public void setDocinfo4(int field_33_docinfo4) {
        this.field_33_docinfo4 = field_33_docinfo4;
    }

    @Internal
    public short getAdt() {
        return field_34_adt;
    }

    @Internal
    public void setAdt(short field_34_adt) {
        this.field_34_adt = field_34_adt;
    }

    @Internal
    public byte[] getDoptypography() {
        return field_35_doptypography;
    }

    @Internal
    public void setDoptypography(byte[] field_35_doptypography) {
        this.field_35_doptypography = field_35_doptypography;
    }

    @Internal
    public byte[] getDogrid() {
        return field_36_dogrid;
    }

    @Internal
    public void setDogrid(byte[] field_36_dogrid) {
        this.field_36_dogrid = field_36_dogrid;
    }

    @Internal
    public short getDocinfo5() {
        return field_37_docinfo5;
    }

    @Internal
    public void setDocinfo5(short field_37_docinfo5) {
        this.field_37_docinfo5 = field_37_docinfo5;
    }

    @Internal
    public short getDocinfo6() {
        return field_38_docinfo6;
    }

    @Internal
    public void setDocinfo6(short field_38_docinfo6) {
        this.field_38_docinfo6 = field_38_docinfo6;
    }

    @Internal
    public byte[] getAsumyi() {
        return field_39_asumyi;
    }

    @Internal
    public void setAsumyi(byte[] field_39_asumyi) {
        this.field_39_asumyi = field_39_asumyi;
    }

    @Internal
    public int getCChWS() {
        return field_40_cChWS;
    }

    @Internal
    public void setCChWS(int field_40_cChWS) {
        this.field_40_cChWS = field_40_cChWS;
    }

    @Internal
    public int getCChWSFtnEdn() {
        return field_41_cChWSFtnEdn;
    }

    @Internal
    public void setCChWSFtnEdn(int field_41_cChWSFtnEdn) {
        this.field_41_cChWSFtnEdn = field_41_cChWSFtnEdn;
    }

    @Internal
    public int getGrfDocEvents() {
        return field_42_grfDocEvents;
    }

    @Internal
    public void setGrfDocEvents(int field_42_grfDocEvents) {
        this.field_42_grfDocEvents = field_42_grfDocEvents;
    }

    @Internal
    public int getVirusinfo() {
        return field_43_virusinfo;
    }

    @Internal
    public void setVirusinfo(int field_43_virusinfo) {
        this.field_43_virusinfo = field_43_virusinfo;
    }

    @Internal
    public byte[] getSpare() {
        return field_44_Spare;
    }

    @Internal
    public void setSpare(byte[] field_44_Spare) {
        this.field_44_Spare = field_44_Spare;
    }

    @Internal
    public int getReserved1() {
        return field_45_reserved1;
    }

    @Internal
    public void setReserved1(int field_45_reserved1) {
        this.field_45_reserved1 = field_45_reserved1;
    }

    @Internal
    public int getReserved2() {
        return field_46_reserved2;
    }

    @Internal
    public void setReserved2(int field_46_reserved2) {
        this.field_46_reserved2 = field_46_reserved2;
    }

    @Internal
    public int getCDBC() {
        return field_47_cDBC;
    }

    @Internal
    public void setCDBC(int field_47_cDBC) {
        this.field_47_cDBC = field_47_cDBC;
    }

    @Internal
    public int getCDBCFtnEdn() {
        return field_48_cDBCFtnEdn;
    }

    @Internal
    public void setCDBCFtnEdn(int field_48_cDBCFtnEdn) {
        this.field_48_cDBCFtnEdn = field_48_cDBCFtnEdn;
    }

    @Internal
    public int getReserved() {
        return field_49_reserved;
    }

    @Internal
    public void setReserved(int field_49_reserved) {
        this.field_49_reserved = field_49_reserved;
    }

    @Internal
    public short getNfcFtnRef() {
        return field_50_nfcFtnRef;
    }

    @Internal
    public void setNfcFtnRef(short field_50_nfcFtnRef) {
        this.field_50_nfcFtnRef = field_50_nfcFtnRef;
    }

    @Internal
    public short getNfcEdnRef() {
        return field_51_nfcEdnRef;
    }

    @Internal
    public void setNfcEdnRef(short field_51_nfcEdnRef) {
        this.field_51_nfcEdnRef = field_51_nfcEdnRef;
    }

    @Internal
    public short getHpsZoonFontPag() {
        return field_52_hpsZoonFontPag;
    }

    @Internal
    public void setHpsZoonFontPag(short field_52_hpsZoonFontPag) {
        this.field_52_hpsZoonFontPag = field_52_hpsZoonFontPag;
    }

    @Internal
    public short getDywDispPag() {
        return field_53_dywDispPag;
    }

    @Internal
    public void setDywDispPag(short field_53_dywDispPag) {
        this.field_53_dywDispPag = field_53_dywDispPag;
    }

    @Internal
    public boolean isFFacingPages() {
        return fFacingPages.isSet(field_1_formatFlags);
    }

    @Internal
    public void setFFacingPages(boolean value) {
        field_1_formatFlags = (byte) fFacingPages.setBoolean(field_1_formatFlags, value);
    }

    @Internal
    public boolean isFWidowControl() {
        return fWidowControl.isSet(field_1_formatFlags);
    }

    @Internal
    public void setFWidowControl(boolean value) {
        field_1_formatFlags = (byte) fWidowControl.setBoolean(field_1_formatFlags, value);
    }

    @Internal
    public boolean isFPMHMainDoc() {
        return fPMHMainDoc.isSet(field_1_formatFlags);
    }

    @Internal
    public void setFPMHMainDoc(boolean value) {
        field_1_formatFlags = (byte) fPMHMainDoc.setBoolean(field_1_formatFlags, value);
    }

    @Internal
    public byte getGrfSupression() {
        return (byte) grfSupression.getValue(field_1_formatFlags);
    }

    @Internal
    public void setGrfSupression(byte value) {
        field_1_formatFlags = (byte) grfSupression.setValue(field_1_formatFlags, value);
    }

    @Internal
    public byte getFpc() {
        return (byte) fpc.getValue(field_1_formatFlags);
    }

    @Internal
    public void setFpc(byte value) {
        field_1_formatFlags = (byte) fpc.setValue(field_1_formatFlags, value);
    }

    @Internal
    public boolean isUnused1() {
        return unused1.isSet(field_1_formatFlags);
    }

    @Internal
    public void setUnused1(boolean value) {
        field_1_formatFlags = (byte) unused1.setBoolean(field_1_formatFlags, value);
    }

    @Internal
    public byte getRncFtn() {
        return (byte) rncFtn.getValue(field_3_footnoteInfo);
    }

    @Internal
    public void setRncFtn(byte value) {
        field_3_footnoteInfo = (short) rncFtn.setValue(field_3_footnoteInfo, value);
    }

    @Internal
    public short getNFtn() {
        return (short) nFtn.getValue(field_3_footnoteInfo);
    }

    @Internal
    public void setNFtn(short value) {
        field_3_footnoteInfo = (short) nFtn.setValue(field_3_footnoteInfo, value);
    }

    @Internal
    public boolean isFOnlyMacPics() {
        return fOnlyMacPics.isSet(field_5_docinfo);
    }

    @Internal
    public void setFOnlyMacPics(boolean value) {
        field_5_docinfo = (byte) fOnlyMacPics.setBoolean(field_5_docinfo, value);
    }

    @Internal
    public boolean isFOnlyWinPics() {
        return fOnlyWinPics.isSet(field_5_docinfo);
    }

    @Internal
    public void setFOnlyWinPics(boolean value) {
        field_5_docinfo = (byte) fOnlyWinPics.setBoolean(field_5_docinfo, value);
    }

    @Internal
    public boolean isFLabelDoc() {
        return fLabelDoc.isSet(field_5_docinfo);
    }

    @Internal
    public void setFLabelDoc(boolean value) {
        field_5_docinfo = (byte) fLabelDoc.setBoolean(field_5_docinfo, value);
    }

    @Internal
    public boolean isFHyphCapitals() {
        return fHyphCapitals.isSet(field_5_docinfo);
    }

    @Internal
    public void setFHyphCapitals(boolean value) {
        field_5_docinfo = (byte) fHyphCapitals.setBoolean(field_5_docinfo, value);
    }

    @Internal
    public boolean isFAutoHyphen() {
        return fAutoHyphen.isSet(field_5_docinfo);
    }

    @Internal
    public void setFAutoHyphen(boolean value) {
        field_5_docinfo = (byte) fAutoHyphen.setBoolean(field_5_docinfo, value);
    }

    @Internal
    public boolean isFFormNoFields() {
        return fFormNoFields.isSet(field_5_docinfo);
    }

    @Internal
    public void setFFormNoFields(boolean value) {
        field_5_docinfo = (byte) fFormNoFields.setBoolean(field_5_docinfo, value);
    }

    @Internal
    public boolean isFLinkStyles() {
        return fLinkStyles.isSet(field_5_docinfo);
    }

    @Internal
    public void setFLinkStyles(boolean value) {
        field_5_docinfo = (byte) fLinkStyles.setBoolean(field_5_docinfo, value);
    }

    @Internal
    public boolean isFRevMarking() {
        return fRevMarking.isSet(field_5_docinfo);
    }

    @Internal
    public void setFRevMarking(boolean value) {
        field_5_docinfo = (byte) fRevMarking.setBoolean(field_5_docinfo, value);
    }

    @Internal
    public boolean isFBackup() {
        return fBackup.isSet(field_6_docinfo1);
    }

    @Internal
    public void setFBackup(boolean value) {
        field_6_docinfo1 = (byte) fBackup.setBoolean(field_6_docinfo1, value);
    }

    @Internal
    public boolean isFExactCWords() {
        return fExactCWords.isSet(field_6_docinfo1);
    }

    @Internal
    public void setFExactCWords(boolean value) {
        field_6_docinfo1 = (byte) fExactCWords.setBoolean(field_6_docinfo1, value);
    }

    @Internal
    public boolean isFPagHidden() {
        return fPagHidden.isSet(field_6_docinfo1);
    }

    @Internal
    public void setFPagHidden(boolean value) {
        field_6_docinfo1 = (byte) fPagHidden.setBoolean(field_6_docinfo1, value);
    }

    @Internal
    public boolean isFPagResults() {
        return fPagResults.isSet(field_6_docinfo1);
    }

    @Internal
    public void setFPagResults(boolean value) {
        field_6_docinfo1 = (byte) fPagResults.setBoolean(field_6_docinfo1, value);
    }

    @Internal
    public boolean isFLockAtn() {
        return fLockAtn.isSet(field_6_docinfo1);
    }

    @Internal
    public void setFLockAtn(boolean value) {
        field_6_docinfo1 = (byte) fLockAtn.setBoolean(field_6_docinfo1, value);
    }

    @Internal
    public boolean isFMirrorMargins() {
        return fMirrorMargins.isSet(field_6_docinfo1);
    }

    @Internal
    public void setFMirrorMargins(boolean value) {
        field_6_docinfo1 = (byte) fMirrorMargins.setBoolean(field_6_docinfo1, value);
    }

    @Internal
    public boolean isUnused3() {
        return unused3.isSet(field_6_docinfo1);
    }

    @Internal
    public void setUnused3(boolean value) {
        field_6_docinfo1 = (byte) unused3.setBoolean(field_6_docinfo1, value);
    }

    @Internal
    public boolean isFDfltTrueType() {
        return fDfltTrueType.isSet(field_6_docinfo1);
    }

    @Internal
    public void setFDfltTrueType(boolean value) {
        field_6_docinfo1 = (byte) fDfltTrueType.setBoolean(field_6_docinfo1, value);
    }

    @Internal
    public boolean isFPagSupressTopSpacing() {
        return fPagSupressTopSpacing.isSet(field_7_docinfo2);
    }

    @Internal
    public void setFPagSupressTopSpacing(boolean value) {
        field_7_docinfo2 = (byte) fPagSupressTopSpacing.setBoolean(field_7_docinfo2, value);
    }

    @Internal
    public boolean isFProtEnabled() {
        return fProtEnabled.isSet(field_7_docinfo2);
    }

    @Internal
    public void setFProtEnabled(boolean value) {
        field_7_docinfo2 = (byte) fProtEnabled.setBoolean(field_7_docinfo2, value);
    }

    @Internal
    public boolean isFDispFormFldSel() {
        return fDispFormFldSel.isSet(field_7_docinfo2);
    }

    @Internal
    public void setFDispFormFldSel(boolean value) {
        field_7_docinfo2 = (byte) fDispFormFldSel.setBoolean(field_7_docinfo2, value);
    }

    @Internal
    public boolean isFRMView() {
        return fRMView.isSet(field_7_docinfo2);
    }

    @Internal
    public void setFRMView(boolean value) {
        field_7_docinfo2 = (byte) fRMView.setBoolean(field_7_docinfo2, value);
    }

    @Internal
    public boolean isFRMPrint() {
        return fRMPrint.isSet(field_7_docinfo2);
    }

    @Internal
    public void setFRMPrint(boolean value) {
        field_7_docinfo2 = (byte) fRMPrint.setBoolean(field_7_docinfo2, value);
    }

    @Internal
    public boolean isUnused4() {
        return unused4.isSet(field_7_docinfo2);
    }

    @Internal
    public void setUnused4(boolean value) {
        field_7_docinfo2 = (byte) unused4.setBoolean(field_7_docinfo2, value);
    }

    @Internal
    public boolean isFLockRev() {
        return fLockRev.isSet(field_7_docinfo2);
    }

    @Internal
    public void setFLockRev(boolean value) {
        field_7_docinfo2 = (byte) fLockRev.setBoolean(field_7_docinfo2, value);
    }

    @Internal
    public boolean isFEmbedFonts() {
        return fEmbedFonts.isSet(field_7_docinfo2);
    }

    @Internal
    public void setFEmbedFonts(boolean value) {
        field_7_docinfo2 = (byte) fEmbedFonts.setBoolean(field_7_docinfo2, value);
    }

    @Internal
    public boolean isOldfNoTabForInd() {
        return oldfNoTabForInd.isSet(field_8_docinfo3);
    }

    @Internal
    public void setOldfNoTabForInd(boolean value) {
        field_8_docinfo3 = (short) oldfNoTabForInd.setBoolean(field_8_docinfo3, value);
    }

    @Internal
    public boolean isOldfNoSpaceRaiseLower() {
        return oldfNoSpaceRaiseLower.isSet(field_8_docinfo3);
    }

    @Internal
    public void setOldfNoSpaceRaiseLower(boolean value) {
        field_8_docinfo3 = (short) oldfNoSpaceRaiseLower.setBoolean(field_8_docinfo3, value);
    }

    @Internal
    public boolean isOldfSuppressSpbfAfterPageBreak() {
        return oldfSuppressSpbfAfterPageBreak.isSet(field_8_docinfo3);
    }

    @Internal
    public void setOldfSuppressSpbfAfterPageBreak(boolean value) {
        field_8_docinfo3 = (short) oldfSuppressSpbfAfterPageBreak
                .setBoolean(field_8_docinfo3, value);
    }

    @Internal
    public boolean isOldfWrapTrailSpaces() {
        return oldfWrapTrailSpaces.isSet(field_8_docinfo3);
    }

    @Internal
    public void setOldfWrapTrailSpaces(boolean value) {
        field_8_docinfo3 = (short) oldfWrapTrailSpaces.setBoolean(field_8_docinfo3, value);
    }

    @Internal
    public boolean isOldfMapPrintTextColor() {
        return oldfMapPrintTextColor.isSet(field_8_docinfo3);
    }

    @Internal
    public void setOldfMapPrintTextColor(boolean value) {
        field_8_docinfo3 = (short) oldfMapPrintTextColor.setBoolean(field_8_docinfo3, value);
    }

    @Internal
    public boolean isOldfNoColumnBalance() {
        return oldfNoColumnBalance.isSet(field_8_docinfo3);
    }

    @Internal
    public void setOldfNoColumnBalance(boolean value) {
        field_8_docinfo3 = (short) oldfNoColumnBalance.setBoolean(field_8_docinfo3, value);
    }

    @Internal
    public boolean isOldfConvMailMergeEsc() {
        return oldfConvMailMergeEsc.isSet(field_8_docinfo3);
    }

    @Internal
    public void setOldfConvMailMergeEsc(boolean value) {
        field_8_docinfo3 = (short) oldfConvMailMergeEsc.setBoolean(field_8_docinfo3, value);
    }

    @Internal
    public boolean isOldfSupressTopSpacing() {
        return oldfSupressTopSpacing.isSet(field_8_docinfo3);
    }

    @Internal
    public void setOldfSupressTopSpacing(boolean value) {
        field_8_docinfo3 = (short) oldfSupressTopSpacing.setBoolean(field_8_docinfo3, value);
    }

    @Internal
    public boolean isOldfOrigWordTableRules() {
        return oldfOrigWordTableRules.isSet(field_8_docinfo3);
    }

    @Internal
    public void setOldfOrigWordTableRules(boolean value) {
        field_8_docinfo3 = (short) oldfOrigWordTableRules.setBoolean(field_8_docinfo3, value);
    }

    @Internal
    public boolean isOldfTransparentMetafiles() {
        return oldfTransparentMetafiles.isSet(field_8_docinfo3);
    }

    @Internal
    public void setOldfTransparentMetafiles(boolean value) {
        field_8_docinfo3 = (short) oldfTransparentMetafiles.setBoolean(field_8_docinfo3, value);
    }

    @Internal
    public boolean isOldfShowBreaksInFrames() {
        return oldfShowBreaksInFrames.isSet(field_8_docinfo3);
    }

    @Internal
    public void setOldfShowBreaksInFrames(boolean value) {
        field_8_docinfo3 = (short) oldfShowBreaksInFrames.setBoolean(field_8_docinfo3, value);
    }

    @Internal
    public boolean isOldfSwapBordersFacingPgs() {
        return oldfSwapBordersFacingPgs.isSet(field_8_docinfo3);
    }

    @Internal
    public void setOldfSwapBordersFacingPgs(boolean value) {
        field_8_docinfo3 = (short) oldfSwapBordersFacingPgs.setBoolean(field_8_docinfo3, value);
    }

    @Internal
    public byte getUnused5() {
        return (byte) unused5.getValue(field_8_docinfo3);
    }

    @Internal
    public void setUnused5(byte value) {
        field_8_docinfo3 = (short) unused5.setValue(field_8_docinfo3, value);
    }

    @Internal
    public byte getRncEdn() {
        return (byte) rncEdn.getValue(field_23_Edn);
    }

    @Internal
    public void setRncEdn(byte value) {
        field_23_Edn = (short) rncEdn.setValue(field_23_Edn, value);
    }

    @Internal
    public short getNEdn() {
        return (short) nEdn.getValue(field_23_Edn);
    }

    @Internal
    public void setNEdn(short value) {
        field_23_Edn = (short) nEdn.setValue(field_23_Edn, value);
    }

    @Internal
    public byte getEpc() {
        return (byte) epc.getValue(field_24_Edn1);
    }

    @Internal
    public void setEpc(byte value) {
        field_24_Edn1 = (short) epc.setValue(field_24_Edn1, value);
    }

    @Internal
    public byte getNfcFtnRef1() {
        return (byte) nfcFtnRef1.getValue(field_24_Edn1);
    }

    @Internal
    public void setNfcFtnRef1(byte value) {
        field_24_Edn1 = (short) nfcFtnRef1.setValue(field_24_Edn1, value);
    }

    @Internal
    public byte getNfcEdnRef1() {
        return (byte) nfcEdnRef1.getValue(field_24_Edn1);
    }

    @Internal
    public void setNfcEdnRef1(byte value) {
        field_24_Edn1 = (short) nfcEdnRef1.setValue(field_24_Edn1, value);
    }

    @Internal
    public boolean isFPrintFormData() {
        return fPrintFormData.isSet(field_24_Edn1);
    }

    @Internal
    public void setFPrintFormData(boolean value) {
        field_24_Edn1 = (short) fPrintFormData.setBoolean(field_24_Edn1, value);
    }

    @Internal
    public boolean isFSaveFormData() {
        return fSaveFormData.isSet(field_24_Edn1);
    }

    @Internal
    public void setFSaveFormData(boolean value) {
        field_24_Edn1 = (short) fSaveFormData.setBoolean(field_24_Edn1, value);
    }

    @Internal
    public boolean isFShadeFormData() {
        return fShadeFormData.isSet(field_24_Edn1);
    }

    @Internal
    public void setFShadeFormData(boolean value) {
        field_24_Edn1 = (short) fShadeFormData.setBoolean(field_24_Edn1, value);
    }

    @Internal
    public boolean isFWCFtnEdn() {
        return fWCFtnEdn.isSet(field_24_Edn1);
    }

    @Internal
    public void setFWCFtnEdn(boolean value) {
        field_24_Edn1 = (short) fWCFtnEdn.setBoolean(field_24_Edn1, value);
    }

    @Internal
    public byte getWvkSaved() {
        return (byte) wvkSaved.getValue(field_32_view);
    }

    @Internal
    public void setWvkSaved(byte value) {
        field_32_view = (short) wvkSaved.setValue(field_32_view, value);
    }

    @Internal
    public short getWScaleSaved() {
        return (short) wScaleSaved.getValue(field_32_view);
    }

    @Internal
    public void setWScaleSaved(short value) {
        field_32_view = (short) wScaleSaved.setValue(field_32_view, value);
    }

    @Internal
    public byte getZkSaved() {
        return (byte) zkSaved.getValue(field_32_view);
    }

    @Internal
    public void setZkSaved(byte value) {
        field_32_view = (short) zkSaved.setValue(field_32_view, value);
    }

    @Internal
    public boolean isFRotateFontW6() {
        return fRotateFontW6.isSet(field_32_view);
    }

    @Internal
    public void setFRotateFontW6(boolean value) {
        field_32_view = (short) fRotateFontW6.setBoolean(field_32_view, value);
    }

    @Internal
    public boolean isIGutterPos() {
        return iGutterPos.isSet(field_32_view);
    }

    @Internal
    public void setIGutterPos(boolean value) {
        field_32_view = (short) iGutterPos.setBoolean(field_32_view, value);
    }

    @Internal
    public boolean isFNoTabForInd() {
        return fNoTabForInd.isSet(field_33_docinfo4);
    }

    @Internal
    public void setFNoTabForInd(boolean value) {
        field_33_docinfo4 = fNoTabForInd.setBoolean(field_33_docinfo4, value);
    }

    @Internal
    public boolean isFNoSpaceRaiseLower() {
        return fNoSpaceRaiseLower.isSet(field_33_docinfo4);
    }

    @Internal
    public void setFNoSpaceRaiseLower(boolean value) {
        field_33_docinfo4 = fNoSpaceRaiseLower.setBoolean(field_33_docinfo4, value);
    }

    @Internal
    public boolean isFSupressSpdfAfterPageBreak() {
        return fSupressSpdfAfterPageBreak.isSet(field_33_docinfo4);
    }

    @Internal
    public void setFSupressSpdfAfterPageBreak(boolean value) {
        field_33_docinfo4 = fSupressSpdfAfterPageBreak.setBoolean(field_33_docinfo4, value);
    }

    @Internal
    public boolean isFWrapTrailSpaces() {
        return fWrapTrailSpaces.isSet(field_33_docinfo4);
    }

    @Internal
    public void setFWrapTrailSpaces(boolean value) {
        field_33_docinfo4 = fWrapTrailSpaces.setBoolean(field_33_docinfo4, value);
    }

    @Internal
    public boolean isFMapPrintTextColor() {
        return fMapPrintTextColor.isSet(field_33_docinfo4);
    }

    @Internal
    public void setFMapPrintTextColor(boolean value) {
        field_33_docinfo4 = fMapPrintTextColor.setBoolean(field_33_docinfo4, value);
    }

    @Internal
    public boolean isFNoColumnBalance() {
        return fNoColumnBalance.isSet(field_33_docinfo4);
    }

    @Internal
    public void setFNoColumnBalance(boolean value) {
        field_33_docinfo4 = fNoColumnBalance.setBoolean(field_33_docinfo4, value);
    }

    @Internal
    public boolean isFConvMailMergeEsc() {
        return fConvMailMergeEsc.isSet(field_33_docinfo4);
    }

    @Internal
    public void setFConvMailMergeEsc(boolean value) {
        field_33_docinfo4 = fConvMailMergeEsc.setBoolean(field_33_docinfo4, value);
    }

    @Internal
    public boolean isFSupressTopSpacing() {
        return fSupressTopSpacing.isSet(field_33_docinfo4);
    }

    @Internal
    public void setFSupressTopSpacing(boolean value) {
        field_33_docinfo4 = fSupressTopSpacing.setBoolean(field_33_docinfo4, value);
    }

    @Internal
    public boolean isFOrigWordTableRules() {
        return fOrigWordTableRules.isSet(field_33_docinfo4);
    }

    @Internal
    public void setFOrigWordTableRules(boolean value) {
        field_33_docinfo4 = fOrigWordTableRules.setBoolean(field_33_docinfo4, value);
    }

    @Internal
    public boolean isFTransparentMetafiles() {
        return fTransparentMetafiles.isSet(field_33_docinfo4);
    }

    @Internal
    public void setFTransparentMetafiles(boolean value) {
        field_33_docinfo4 = fTransparentMetafiles.setBoolean(field_33_docinfo4, value);
    }

    @Internal
    public boolean isFShowBreaksInFrames() {
        return fShowBreaksInFrames.isSet(field_33_docinfo4);
    }

    @Internal
    public void setFShowBreaksInFrames(boolean value) {
        field_33_docinfo4 = fShowBreaksInFrames.setBoolean(field_33_docinfo4, value);
    }

    @Internal
    public boolean isFSwapBordersFacingPgs() {
        return fSwapBordersFacingPgs.isSet(field_33_docinfo4);
    }

    @Internal
    public void setFSwapBordersFacingPgs(boolean value) {
        field_33_docinfo4 = fSwapBordersFacingPgs.setBoolean(field_33_docinfo4, value);
    }

    @Internal
    public boolean isFSuppressTopSPacingMac5() {
        return fSuppressTopSPacingMac5.isSet(field_33_docinfo4);
    }

    @Internal
    public void setFSuppressTopSPacingMac5(boolean value) {
        field_33_docinfo4 = fSuppressTopSPacingMac5.setBoolean(field_33_docinfo4, value);
    }

    @Internal
    public boolean isFTruncDxaExpand() {
        return fTruncDxaExpand.isSet(field_33_docinfo4);
    }

    @Internal
    public void setFTruncDxaExpand(boolean value) {
        field_33_docinfo4 = fTruncDxaExpand.setBoolean(field_33_docinfo4, value);
    }

    @Internal
    public boolean isFPrintBodyBeforeHdr() {
        return fPrintBodyBeforeHdr.isSet(field_33_docinfo4);
    }

    @Internal
    public void setFPrintBodyBeforeHdr(boolean value) {
        field_33_docinfo4 = fPrintBodyBeforeHdr.setBoolean(field_33_docinfo4, value);
    }

    @Internal
    public boolean isFNoLeading() {
        return fNoLeading.isSet(field_33_docinfo4);
    }

    @Internal
    public void setFNoLeading(boolean value) {
        field_33_docinfo4 = fNoLeading.setBoolean(field_33_docinfo4, value);
    }

    @Internal
    public boolean isFMWSmallCaps() {
        return fMWSmallCaps.isSet(field_33_docinfo4);
    }

    @Internal
    public void setFMWSmallCaps(boolean value) {
        field_33_docinfo4 = fMWSmallCaps.setBoolean(field_33_docinfo4, value);
    }

    @Internal
    public byte getLvl() {
        return (byte) lvl.getValue(field_37_docinfo5);
    }

    @Internal
    public void setLvl(byte value) {
        field_37_docinfo5 = (short) lvl.setValue(field_37_docinfo5, value);
    }

    @Internal
    public boolean isFGramAllDone() {
        return fGramAllDone.isSet(field_37_docinfo5);
    }

    @Internal
    public void setFGramAllDone(boolean value) {
        field_37_docinfo5 = (short) fGramAllDone.setBoolean(field_37_docinfo5, value);
    }

    @Internal
    public boolean isFGramAllClean() {
        return fGramAllClean.isSet(field_37_docinfo5);
    }

    @Internal
    public void setFGramAllClean(boolean value) {
        field_37_docinfo5 = (short) fGramAllClean.setBoolean(field_37_docinfo5, value);
    }

    @Internal
    public boolean isFSubsetFonts() {
        return fSubsetFonts.isSet(field_37_docinfo5);
    }

    @Internal
    public void setFSubsetFonts(boolean value) {
        field_37_docinfo5 = (short) fSubsetFonts.setBoolean(field_37_docinfo5, value);
    }

    @Internal
    public boolean isFHideLastVersion() {
        return fHideLastVersion.isSet(field_37_docinfo5);
    }

    @Internal
    public void setFHideLastVersion(boolean value) {
        field_37_docinfo5 = (short) fHideLastVersion.setBoolean(field_37_docinfo5, value);
    }

    @Internal
    public boolean isFHtmlDoc() {
        return fHtmlDoc.isSet(field_37_docinfo5);
    }

    @Internal
    public void setFHtmlDoc(boolean value) {
        field_37_docinfo5 = (short) fHtmlDoc.setBoolean(field_37_docinfo5, value);
    }

    @Internal
    public boolean isFSnapBorder() {
        return fSnapBorder.isSet(field_37_docinfo5);
    }

    @Internal
    public void setFSnapBorder(boolean value) {
        field_37_docinfo5 = (short) fSnapBorder.setBoolean(field_37_docinfo5, value);
    }

    @Internal
    public boolean isFIncludeHeader() {
        return fIncludeHeader.isSet(field_37_docinfo5);
    }

    @Internal
    public void setFIncludeHeader(boolean value) {
        field_37_docinfo5 = (short) fIncludeHeader.setBoolean(field_37_docinfo5, value);
    }

    @Internal
    public boolean isFIncludeFooter() {
        return fIncludeFooter.isSet(field_37_docinfo5);
    }

    @Internal
    public void setFIncludeFooter(boolean value) {
        field_37_docinfo5 = (short) fIncludeFooter.setBoolean(field_37_docinfo5, value);
    }

    @Internal
    public boolean isFForcePageSizePag() {
        return fForcePageSizePag.isSet(field_37_docinfo5);
    }

    @Internal
    public void setFForcePageSizePag(boolean value) {
        field_37_docinfo5 = (short) fForcePageSizePag.setBoolean(field_37_docinfo5, value);
    }

    @Internal
    public boolean isFMinFontSizePag() {
        return fMinFontSizePag.isSet(field_37_docinfo5);
    }

    @Internal
    public void setFMinFontSizePag(boolean value) {
        field_37_docinfo5 = (short) fMinFontSizePag.setBoolean(field_37_docinfo5, value);
    }

    @Internal
    public boolean isFHaveVersions() {
        return fHaveVersions.isSet(field_38_docinfo6);
    }

    @Internal
    public void setFHaveVersions(boolean value) {
        field_38_docinfo6 = (short) fHaveVersions.setBoolean(field_38_docinfo6, value);
    }

    @Internal
    public boolean isFAutoVersions() {
        return fAutoVersions.isSet(field_38_docinfo6);
    }

    @Internal
    public void setFAutoVersions(boolean value) {
        field_38_docinfo6 = (short) fAutoVersions.setBoolean(field_38_docinfo6, value);
    }

    @Internal
    public boolean isFVirusPrompted() {
        return fVirusPrompted.isSet(field_43_virusinfo);
    }

    @Internal
    public void setFVirusPrompted(boolean value) {
        field_43_virusinfo = fVirusPrompted.setBoolean(field_43_virusinfo, value);
    }

    @Internal
    public boolean isFVirusLoadSafe() {
        return fVirusLoadSafe.isSet(field_43_virusinfo);
    }

    @Internal
    public void setFVirusLoadSafe(boolean value) {
        field_43_virusinfo = fVirusLoadSafe.setBoolean(field_43_virusinfo, value);
    }

    @Internal
    public int getKeyVirusSession30() {
        return KeyVirusSession30.getValue(field_43_virusinfo);
    }

    @Internal
    public void setKeyVirusSession30(int value) {
        field_43_virusinfo = KeyVirusSession30.setValue(field_43_virusinfo, value);
    }

} 