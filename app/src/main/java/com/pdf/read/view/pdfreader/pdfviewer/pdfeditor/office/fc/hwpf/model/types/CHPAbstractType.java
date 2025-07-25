package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hwpf.model.types;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hwpf.model.Colorref;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hwpf.model.Hyphenation;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hwpf.usermodel.BorderCode;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hwpf.usermodel.DateAndTime;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hwpf.usermodel.ShadingDescriptor;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.BitField;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.Internal;

@Internal
public abstract class CHPAbstractType {

    protected final static byte KCD_NON = 0;
    protected final static byte KCD_DOT = 1;
    protected final static byte KCD_COMMA = 2;
    protected final static byte KCD_CIRCLE = 3;
    protected final static byte KCD_UNDER_DOT = 4;
    protected final static byte ISS_NONE = 0;
    protected final static byte ISS_SUPERSCRIPTED = 1;
    protected final static byte ISS_SUBSCRIPTED = 2;
    protected final static byte KUL_NONE = 0;
    protected final static byte KUL_SINGLE = 1;
    protected final static byte KUL_BY_WORD = 2;
    protected final static byte KUL_DOUBLE = 3;
    protected final static byte KUL_DOTTED = 4;
    protected final static byte KUL_HIDDEN = 5;
    protected final static byte KUL_THICK = 6;
    protected final static byte KUL_DASH = 7;
    protected final static byte KUL_DOT = 8;
    protected final static byte KUL_DOT_DASH = 9;
    protected final static byte KUL_DOT_DOT_DASH = 10;
    protected final static byte KUL_WAVE = 11;
    protected final static byte KUL_DOTTED_HEAVY = 20;
    protected final static byte KUL_DASHED_HEAVY = 23;
    protected final static byte KUL_DOT_DASH_HEAVY = 25;
    protected final static byte KUL_DOT_DOT_DASH_HEAVY = 26;
    protected final static byte KUL_WAVE_HEAVY = 27;
    protected final static byte KUL_DASH_LONG = 39;
    protected final static byte KUL_WAVE_DOUBLE = 43;
    protected final static byte KUL_DASH_LONG_HEAVY = 55;
    protected final static byte SFXTTEXT_NO = 0;
    protected final static byte SFXTTEXT_LAS_VEGAS_LIGHTS = 1;
    protected final static byte SFXTTEXT_BACKGROUND_BLINK = 2;
    protected final static byte SFXTTEXT_SPARKLE_TEXT = 3;
    protected final static byte SFXTTEXT_MARCHING_ANTS = 4;
    protected final static byte SFXTTEXT_MARCHING_RED_ANTS = 5;
    protected final static byte SFXTTEXT_SHIMMER = 6;
    protected final static byte LBRCRJ_NONE = 0;
    protected final static byte LBRCRJ_LEFT = 1;
    protected final static byte LBRCRJ_RIGHT = 2;
    protected final static byte LBRCRJ_BOTH = 3;
    private static final BitField fBold = new BitField(0x00000001);
    private static final BitField fItalic = new BitField(0x00000002);
    private static final BitField fRMarkDel = new BitField(0x00000004);
    private static final BitField fOutline = new BitField(0x00000008);
    private static final BitField fFldVanish = new BitField(0x00000010);
    private static final BitField fSmallCaps = new BitField(0x00000020);
    private static final BitField fCaps = new BitField(0x00000040);
    private static final BitField fVanish = new BitField(0x00000080);
    private static final BitField fRMark = new BitField(0x00000100);
    private static final BitField fSpec = new BitField(0x00000200);
    private static final BitField fStrike = new BitField(0x00000400);
    private static final BitField fObj = new BitField(0x00000800);
    private static final BitField fShadow = new BitField(0x00001000);
    private static final BitField fLowerCase = new BitField(0x00002000);
    private static final BitField fData = new BitField(0x00004000);
    private static final BitField fOle2 = new BitField(0x00008000);
    private static final BitField fEmboss = new BitField(0x00010000);
    private static final BitField fImprint = new BitField(0x00020000);
    private static final BitField fDStrike = new BitField(0x00040000);
    private static final BitField fUsePgsuSettings = new BitField(0x00080000);
    private static final BitField fBoldBi = new BitField(0x00100000);
    private static final BitField fComplexScripts = new BitField(0x00200000);
    private static final BitField fItalicBi = new BitField(0x00400000);
    private static final BitField fBiDi = new BitField(0x00800000);
    private static final BitField itypFELayout = new BitField(0x00ff);
    private static final BitField fTNY = new BitField(0x0100);
    private static final BitField fWarichu = new BitField(0x0200);
    private static final BitField fKumimoji = new BitField(0x0400);
    private static final BitField fRuby = new BitField(0x0800);
    private static final BitField fLSFitText = new BitField(0x1000);
    private static final BitField spare = new BitField(0xe000);
    private static final BitField iWarichuBracket = new BitField(0x07);
    private static final BitField fWarichuNoOpenBracket = new BitField(0x08);
    private static final BitField fTNYCompress = new BitField(0x10);
    private static final BitField fTNYFetchTxm = new BitField(0x20);
    private static final BitField fCellFitText = new BitField(0x40);
    private static final BitField unused = new BitField(0x80);
    private static final BitField icoHighlight = new BitField(0x001f);
    private static final BitField fHighlight = new BitField(0x0020);
    private static final BitField fChsDiff = new BitField(0x0001);
    private static final BitField fMacChs = new BitField(0x0020);
    protected int field_1_grpfChp;
    protected int field_2_hps;
    protected int field_3_ftcAscii;
    protected int field_4_ftcFE;
    protected int field_5_ftcOther;
    protected int field_6_ftcBi;
    protected int field_7_dxaSpace;
    protected Colorref field_8_cv;
    protected byte field_9_ico;
    protected int field_10_pctCharWidth;
    protected int field_11_lidDefault;
    protected int field_12_lidFE;
    protected byte field_13_kcd;
    protected boolean field_14_fUndetermine;
    protected byte field_15_iss;
    protected boolean field_16_fSpecSymbol;
    protected byte field_17_idct;
    protected byte field_18_idctHint;
    protected byte field_19_kul;
    protected Hyphenation field_20_hresi;
    protected int field_21_hpsKern;
    protected short field_22_hpsPos;
    protected ShadingDescriptor field_23_shd;
    protected BorderCode field_24_brc;
    protected int field_25_ibstRMark;
    protected byte field_26_sfxtText;
    protected boolean field_27_fDblBdr;
    protected boolean field_28_fBorderWS;
    protected short field_29_ufel;
    protected byte field_30_copt;
    protected int field_31_hpsAsci;
    protected int field_32_hpsFE;
    protected int field_33_hpsBi;
    protected int field_34_ftcSym;
    protected int field_35_xchSym;
    protected int field_36_fcPic;
    protected int field_37_fcObj;
    protected int field_38_lTagObj;
    protected int field_39_fcData;
    protected Hyphenation field_40_hresiOld;
    protected int field_41_ibstRMarkDel;
    protected DateAndTime field_42_dttmRMark;
    protected DateAndTime field_43_dttmRMarkDel;
    protected int field_44_istd;
    protected int field_45_idslRMReason;
    protected int field_46_idslReasonDel;
    protected int field_47_cpg;
    protected short field_48_Highlight;
    protected short field_49_CharsetFlags;
    protected short field_50_chse;
    protected boolean field_51_fPropRMark;
    protected int field_52_ibstPropRMark;
    protected DateAndTime field_53_dttmPropRMark;
    protected boolean field_54_fConflictOrig;
    protected boolean field_55_fConflictOtherDel;
    protected int field_56_wConflict;
    protected int field_57_IbstConflict;
    protected DateAndTime field_58_dttmConflict;
    protected boolean field_59_fDispFldRMark;
    protected int field_60_ibstDispFldRMark;
    protected DateAndTime field_61_dttmDispFldRMark;
    protected byte[] field_62_xstDispFldRMark;
    protected int field_63_fcObjp;
    protected byte field_64_lbrCRJ;
    protected boolean field_65_fSpecVanish;
    protected boolean field_66_fHasOldProps;
    protected boolean field_67_fSdtVanish;
    protected int field_68_wCharScale;
    protected Colorref field_69_underlineColor;

    protected CHPAbstractType() {
        this.field_2_hps = 20;
        this.field_8_cv = new Colorref();
        this.field_11_lidDefault = 0x0400;
        this.field_12_lidFE = 0x0400;
        this.field_20_hresi = new Hyphenation();
        this.field_23_shd = new ShadingDescriptor();
        this.field_24_brc = new BorderCode();
        this.field_36_fcPic = -1;
        this.field_40_hresiOld = new Hyphenation();
        this.field_42_dttmRMark = new DateAndTime();
        this.field_43_dttmRMarkDel = new DateAndTime();
        this.field_44_istd = 10;
        this.field_53_dttmPropRMark = new DateAndTime();
        this.field_58_dttmConflict = new DateAndTime();
        this.field_61_dttmDispFldRMark = new DateAndTime();
        this.field_62_xstDispFldRMark = new byte[0];
        this.field_68_wCharScale = 100;
    }

    public String toString() {

        String builder = "[CHP]\n" +
                "    .grpfChp              = " +
                " (" + getGrpfChp() + " )\n" +
                "         .fBold                    = " + isFBold() + '\n' +
                "         .fItalic                  = " + isFItalic() + '\n' +
                "         .fRMarkDel                = " + isFRMarkDel() + '\n' +
                "         .fOutline                 = " + isFOutline() + '\n' +
                "         .fFldVanish               = " + isFFldVanish() + '\n' +
                "         .fSmallCaps               = " + isFSmallCaps() + '\n' +
                "         .fCaps                    = " + isFCaps() + '\n' +
                "         .fVanish                  = " + isFVanish() + '\n' +
                "         .fRMark                   = " + isFRMark() + '\n' +
                "         .fSpec                    = " + isFSpec() + '\n' +
                "         .fStrike                  = " + isFStrike() + '\n' +
                "         .fObj                     = " + isFObj() + '\n' +
                "         .fShadow                  = " + isFShadow() + '\n' +
                "         .fLowerCase               = " + isFLowerCase() + '\n' +
                "         .fData                    = " + isFData() + '\n' +
                "         .fOle2                    = " + isFOle2() + '\n' +
                "         .fEmboss                  = " + isFEmboss() + '\n' +
                "         .fImprint                 = " + isFImprint() + '\n' +
                "         .fDStrike                 = " + isFDStrike() + '\n' +
                "         .fUsePgsuSettings         = " + isFUsePgsuSettings() + '\n' +
                "         .fBoldBi                  = " + isFBoldBi() + '\n' +
                "         .fComplexScripts          = " + isFComplexScripts() + '\n' +
                "         .fItalicBi                = " + isFItalicBi() + '\n' +
                "         .fBiDi                    = " + isFBiDi() + '\n' +
                "    .hps                  = " +
                " (" + getHps() + " )\n" +
                "    .ftcAscii             = " +
                " (" + getFtcAscii() + " )\n" +
                "    .ftcFE                = " +
                " (" + getFtcFE() + " )\n" +
                "    .ftcOther             = " +
                " (" + getFtcOther() + " )\n" +
                "    .ftcBi                = " +
                " (" + getFtcBi() + " )\n" +
                "    .dxaSpace             = " +
                " (" + getDxaSpace() + " )\n" +
                "    .cv                   = " +
                " (" + getCv() + " )\n" +
                "    .ico                  = " +
                " (" + getIco() + " )\n" +
                "    .pctCharWidth         = " +
                " (" + getPctCharWidth() + " )\n" +
                "    .lidDefault           = " +
                " (" + getLidDefault() + " )\n" +
                "    .lidFE                = " +
                " (" + getLidFE() + " )\n" +
                "    .kcd                  = " +
                " (" + getKcd() + " )\n" +
                "    .fUndetermine         = " +
                " (" + getFUndetermine() + " )\n" +
                "    .iss                  = " +
                " (" + getIss() + " )\n" +
                "    .fSpecSymbol          = " +
                " (" + getFSpecSymbol() + " )\n" +
                "    .idct                 = " +
                " (" + getIdct() + " )\n" +
                "    .idctHint             = " +
                " (" + getIdctHint() + " )\n" +
                "    .kul                  = " +
                " (" + getKul() + " )\n" +
                "    .hresi                = " +
                " (" + getHresi() + " )\n" +
                "    .hpsKern              = " +
                " (" + getHpsKern() + " )\n" +
                "    .hpsPos               = " +
                " (" + getHpsPos() + " )\n" +
                "    .shd                  = " +
                " (" + getShd() + " )\n" +
                "    .brc                  = " +
                " (" + getBrc() + " )\n" +
                "    .ibstRMark            = " +
                " (" + getIbstRMark() + " )\n" +
                "    .sfxtText             = " +
                " (" + getSfxtText() + " )\n" +
                "    .fDblBdr              = " +
                " (" + getFDblBdr() + " )\n" +
                "    .fBorderWS            = " +
                " (" + getFBorderWS() + " )\n" +
                "    .ufel                 = " +
                " (" + getUfel() + " )\n" +
                "         .itypFELayout             = " + getItypFELayout() + '\n' +
                "         .fTNY                     = " + isFTNY() + '\n' +
                "         .fWarichu                 = " + isFWarichu() + '\n' +
                "         .fKumimoji                = " + isFKumimoji() + '\n' +
                "         .fRuby                    = " + isFRuby() + '\n' +
                "         .fLSFitText               = " + isFLSFitText() + '\n' +
                "         .spare                    = " + getSpare() + '\n' +
                "    .copt                 = " +
                " (" + getCopt() + " )\n" +
                "         .iWarichuBracket          = " + getIWarichuBracket() + '\n' +
                "         .fWarichuNoOpenBracket     = " + isFWarichuNoOpenBracket() + '\n' +
                "         .fTNYCompress             = " + isFTNYCompress() + '\n' +
                "         .fTNYFetchTxm             = " + isFTNYFetchTxm() + '\n' +
                "         .fCellFitText             = " + isFCellFitText() + '\n' +
                "         .unused                   = " + isUnused() + '\n' +
                "    .hpsAsci              = " +
                " (" + getHpsAsci() + " )\n" +
                "    .hpsFE                = " +
                " (" + getHpsFE() + " )\n" +
                "    .hpsBi                = " +
                " (" + getHpsBi() + " )\n" +
                "    .ftcSym               = " +
                " (" + getFtcSym() + " )\n" +
                "    .xchSym               = " +
                " (" + getXchSym() + " )\n" +
                "    .fcPic                = " +
                " (" + getFcPic() + " )\n" +
                "    .fcObj                = " +
                " (" + getFcObj() + " )\n" +
                "    .lTagObj              = " +
                " (" + getLTagObj() + " )\n" +
                "    .fcData               = " +
                " (" + getFcData() + " )\n" +
                "    .hresiOld             = " +
                " (" + getHresiOld() + " )\n" +
                "    .ibstRMarkDel         = " +
                " (" + getIbstRMarkDel() + " )\n" +
                "    .dttmRMark            = " +
                " (" + getDttmRMark() + " )\n" +
                "    .dttmRMarkDel         = " +
                " (" + getDttmRMarkDel() + " )\n" +
                "    .istd                 = " +
                " (" + getIstd() + " )\n" +
                "    .idslRMReason         = " +
                " (" + getIdslRMReason() + " )\n" +
                "    .idslReasonDel        = " +
                " (" + getIdslReasonDel() + " )\n" +
                "    .cpg                  = " +
                " (" + getCpg() + " )\n" +
                "    .Highlight            = " +
                " (" + getHighlight() + " )\n" +
                "         .icoHighlight             = " + getIcoHighlight() + '\n' +
                "         .fHighlight               = " + isFHighlight() + '\n' +
                "    .CharsetFlags         = " +
                " (" + getCharsetFlags() + " )\n" +
                "         .fChsDiff                 = " + isFChsDiff() + '\n' +
                "         .fMacChs                  = " + isFMacChs() + '\n' +
                "    .chse                 = " +
                " (" + getChse() + " )\n" +
                "    .fPropRMark           = " +
                " (" + getFPropRMark() + " )\n" +
                "    .ibstPropRMark        = " +
                " (" + getIbstPropRMark() + " )\n" +
                "    .dttmPropRMark        = " +
                " (" + getDttmPropRMark() + " )\n" +
                "    .fConflictOrig        = " +
                " (" + getFConflictOrig() + " )\n" +
                "    .fConflictOtherDel    = " +
                " (" + getFConflictOtherDel() + " )\n" +
                "    .wConflict            = " +
                " (" + getWConflict() + " )\n" +
                "    .IbstConflict         = " +
                " (" + getIbstConflict() + " )\n" +
                "    .dttmConflict         = " +
                " (" + getDttmConflict() + " )\n" +
                "    .fDispFldRMark        = " +
                " (" + getFDispFldRMark() + " )\n" +
                "    .ibstDispFldRMark     = " +
                " (" + getIbstDispFldRMark() + " )\n" +
                "    .dttmDispFldRMark     = " +
                " (" + getDttmDispFldRMark() + " )\n" +
                "    .xstDispFldRMark      = " +
                " (" + getXstDispFldRMark() + " )\n" +
                "    .fcObjp               = " +
                " (" + getFcObjp() + " )\n" +
                "    .lbrCRJ               = " +
                " (" + getLbrCRJ() + " )\n" +
                "    .fSpecVanish          = " +
                " (" + getFSpecVanish() + " )\n" +
                "    .fHasOldProps         = " +
                " (" + getFHasOldProps() + " )\n" +
                "    .fSdtVanish           = " +
                " (" + getFSdtVanish() + " )\n" +
                "    .wCharScale           = " +
                " (" + getWCharScale() + " )\n" +
                "[/CHP]\n";
        return builder;
    }

    @Internal
    public int getGrpfChp() {
        return field_1_grpfChp;
    }

    @Internal
    public void setGrpfChp(int field_1_grpfChp) {
        this.field_1_grpfChp = field_1_grpfChp;
    }

    @Internal
    public int getHps() {
        return field_2_hps;
    }

    @Internal
    public void setHps(int field_2_hps) {
        this.field_2_hps = field_2_hps;
    }

    @Internal
    public int getFtcAscii() {
        return field_3_ftcAscii;
    }

    @Internal
    public void setFtcAscii(int field_3_ftcAscii) {
        this.field_3_ftcAscii = field_3_ftcAscii;
    }

    @Internal
    public int getFtcFE() {
        return field_4_ftcFE;
    }

    @Internal
    public void setFtcFE(int field_4_ftcFE) {
        this.field_4_ftcFE = field_4_ftcFE;
    }

    @Internal
    public int getFtcOther() {
        return field_5_ftcOther;
    }

    @Internal
    public void setFtcOther(int field_5_ftcOther) {
        this.field_5_ftcOther = field_5_ftcOther;
    }

    @Internal
    public int getFtcBi() {
        return field_6_ftcBi;
    }

    @Internal
    public void setFtcBi(int field_6_ftcBi) {
        this.field_6_ftcBi = field_6_ftcBi;
    }

    @Internal
    public int getDxaSpace() {
        return field_7_dxaSpace;
    }

    @Internal
    public void setDxaSpace(int field_7_dxaSpace) {
        this.field_7_dxaSpace = field_7_dxaSpace;
    }

    @Internal
    public Colorref getCv() {
        return field_8_cv;
    }

    @Internal
    public void setCv(Colorref field_8_cv) {
        this.field_8_cv = field_8_cv;
    }

    @Internal
    public byte getIco() {
        return field_9_ico;
    }

    @Internal
    public void setIco(byte field_9_ico) {
        this.field_9_ico = field_9_ico;
    }

    @Internal
    public int getPctCharWidth() {
        return field_10_pctCharWidth;
    }

    @Internal
    public void setPctCharWidth(int field_10_pctCharWidth) {
        this.field_10_pctCharWidth = field_10_pctCharWidth;
    }

    @Internal
    public int getLidDefault() {
        return field_11_lidDefault;
    }

    @Internal
    public void setLidDefault(int field_11_lidDefault) {
        this.field_11_lidDefault = field_11_lidDefault;
    }

    @Internal
    public int getLidFE() {
        return field_12_lidFE;
    }

    @Internal
    public void setLidFE(int field_12_lidFE) {
        this.field_12_lidFE = field_12_lidFE;
    }

    @Internal
    public byte getKcd() {
        return field_13_kcd;
    }

    @Internal
    public void setKcd(byte field_13_kcd) {
        this.field_13_kcd = field_13_kcd;
    }

    @Internal
    public boolean getFUndetermine() {
        return field_14_fUndetermine;
    }

    @Internal
    public void setFUndetermine(boolean field_14_fUndetermine) {
        this.field_14_fUndetermine = field_14_fUndetermine;
    }

    @Internal
    public byte getIss() {
        return field_15_iss;
    }

    @Internal
    public void setIss(byte field_15_iss) {
        this.field_15_iss = field_15_iss;
    }

    @Internal
    public boolean getFSpecSymbol() {
        return field_16_fSpecSymbol;
    }

    @Internal
    public void setFSpecSymbol(boolean field_16_fSpecSymbol) {
        this.field_16_fSpecSymbol = field_16_fSpecSymbol;
    }

    @Internal
    public byte getIdct() {
        return field_17_idct;
    }

    @Internal
    public void setIdct(byte field_17_idct) {
        this.field_17_idct = field_17_idct;
    }

    @Internal
    public byte getIdctHint() {
        return field_18_idctHint;
    }

    @Internal
    public void setIdctHint(byte field_18_idctHint) {
        this.field_18_idctHint = field_18_idctHint;
    }

    @Internal
    public byte getKul() {
        return field_19_kul;
    }

    @Internal
    public void setKul(byte field_19_kul) {
        this.field_19_kul = field_19_kul;
    }

    @Internal
    public Hyphenation getHresi() {
        return field_20_hresi;
    }

    @Internal
    public void setHresi(Hyphenation field_20_hresi) {
        this.field_20_hresi = field_20_hresi;
    }

    @Internal
    public int getHpsKern() {
        return field_21_hpsKern;
    }

    @Internal
    public void setHpsKern(int field_21_hpsKern) {
        this.field_21_hpsKern = field_21_hpsKern;
    }

    @Internal
    public short getHpsPos() {
        return field_22_hpsPos;
    }

    @Internal
    public void setHpsPos(short field_22_hpsPos) {
        this.field_22_hpsPos = field_22_hpsPos;
    }

    @Internal
    public ShadingDescriptor getShd() {
        return field_23_shd;
    }

    @Internal
    public void setShd(ShadingDescriptor field_23_shd) {
        this.field_23_shd = field_23_shd;
    }

    @Internal
    public BorderCode getBrc() {
        return field_24_brc;
    }

    @Internal
    public void setBrc(BorderCode field_24_brc) {
        this.field_24_brc = field_24_brc;
    }

    @Internal
    public int getIbstRMark() {
        return field_25_ibstRMark;
    }

    @Internal
    public void setIbstRMark(int field_25_ibstRMark) {
        this.field_25_ibstRMark = field_25_ibstRMark;
    }

    @Internal
    public byte getSfxtText() {
        return field_26_sfxtText;
    }

    @Internal
    public void setSfxtText(byte field_26_sfxtText) {
        this.field_26_sfxtText = field_26_sfxtText;
    }

    @Internal
    public boolean getFDblBdr() {
        return field_27_fDblBdr;
    }

    @Internal
    public void setFDblBdr(boolean field_27_fDblBdr) {
        this.field_27_fDblBdr = field_27_fDblBdr;
    }

    @Internal
    public boolean getFBorderWS() {
        return field_28_fBorderWS;
    }

    @Internal
    public void setFBorderWS(boolean field_28_fBorderWS) {
        this.field_28_fBorderWS = field_28_fBorderWS;
    }

    @Internal
    public short getUfel() {
        return field_29_ufel;
    }

    @Internal
    public void setUfel(short field_29_ufel) {
        this.field_29_ufel = field_29_ufel;
    }

    @Internal
    public byte getCopt() {
        return field_30_copt;
    }

    @Internal
    public void setCopt(byte field_30_copt) {
        this.field_30_copt = field_30_copt;
    }

    @Internal
    public int getHpsAsci() {
        return field_31_hpsAsci;
    }

    @Internal
    public void setHpsAsci(int field_31_hpsAsci) {
        this.field_31_hpsAsci = field_31_hpsAsci;
    }

    @Internal
    public int getHpsFE() {
        return field_32_hpsFE;
    }

    @Internal
    public void setHpsFE(int field_32_hpsFE) {
        this.field_32_hpsFE = field_32_hpsFE;
    }

    @Internal
    public int getHpsBi() {
        return field_33_hpsBi;
    }

    @Internal
    public void setHpsBi(int field_33_hpsBi) {
        this.field_33_hpsBi = field_33_hpsBi;
    }

    @Internal
    public int getFtcSym() {
        return field_34_ftcSym;
    }

    @Internal
    public void setFtcSym(int field_34_ftcSym) {
        this.field_34_ftcSym = field_34_ftcSym;
    }

    @Internal
    public int getXchSym() {
        return field_35_xchSym;
    }

    @Internal
    public void setXchSym(int field_35_xchSym) {
        this.field_35_xchSym = field_35_xchSym;
    }

    @Internal
    public int getFcPic() {
        return field_36_fcPic;
    }

    @Internal
    public void setFcPic(int field_36_fcPic) {
        this.field_36_fcPic = field_36_fcPic;
    }

    @Internal
    public int getFcObj() {
        return field_37_fcObj;
    }

    @Internal
    public void setFcObj(int field_37_fcObj) {
        this.field_37_fcObj = field_37_fcObj;
    }

    @Internal
    public int getLTagObj() {
        return field_38_lTagObj;
    }

    @Internal
    public void setLTagObj(int field_38_lTagObj) {
        this.field_38_lTagObj = field_38_lTagObj;
    }

    @Internal
    public int getFcData() {
        return field_39_fcData;
    }

    @Internal
    public void setFcData(int field_39_fcData) {
        this.field_39_fcData = field_39_fcData;
    }

    @Internal
    public Hyphenation getHresiOld() {
        return field_40_hresiOld;
    }

    @Internal
    public void setHresiOld(Hyphenation field_40_hresiOld) {
        this.field_40_hresiOld = field_40_hresiOld;
    }

    @Internal
    public int getIbstRMarkDel() {
        return field_41_ibstRMarkDel;
    }

    @Internal
    public void setIbstRMarkDel(int field_41_ibstRMarkDel) {
        this.field_41_ibstRMarkDel = field_41_ibstRMarkDel;
    }

    @Internal
    public DateAndTime getDttmRMark() {
        return field_42_dttmRMark;
    }

    @Internal
    public void setDttmRMark(DateAndTime field_42_dttmRMark) {
        this.field_42_dttmRMark = field_42_dttmRMark;
    }

    @Internal
    public DateAndTime getDttmRMarkDel() {
        return field_43_dttmRMarkDel;
    }

    @Internal
    public void setDttmRMarkDel(DateAndTime field_43_dttmRMarkDel) {
        this.field_43_dttmRMarkDel = field_43_dttmRMarkDel;
    }

    @Internal
    public int getIstd() {
        return field_44_istd;
    }

    @Internal
    public void setIstd(int field_44_istd) {
        this.field_44_istd = field_44_istd;
    }

    @Internal
    public int getIdslRMReason() {
        return field_45_idslRMReason;
    }

    @Internal
    public void setIdslRMReason(int field_45_idslRMReason) {
        this.field_45_idslRMReason = field_45_idslRMReason;
    }

    @Internal
    public int getIdslReasonDel() {
        return field_46_idslReasonDel;
    }

    @Internal
    public void setIdslReasonDel(int field_46_idslReasonDel) {
        this.field_46_idslReasonDel = field_46_idslReasonDel;
    }

    @Internal
    public int getCpg() {
        return field_47_cpg;
    }

    @Internal
    public void setCpg(int field_47_cpg) {
        this.field_47_cpg = field_47_cpg;
    }

    @Internal
    public short getHighlight() {
        return field_48_Highlight;
    }

    @Internal
    public void setHighlight(short field_48_Highlight) {
        this.field_48_Highlight = field_48_Highlight;
    }

    @Internal
    public short getCharsetFlags() {
        return field_49_CharsetFlags;
    }

    @Internal
    public void setCharsetFlags(short field_49_CharsetFlags) {
        this.field_49_CharsetFlags = field_49_CharsetFlags;
    }

    @Internal
    public short getChse() {
        return field_50_chse;
    }

    @Internal
    public void setChse(short field_50_chse) {
        this.field_50_chse = field_50_chse;
    }

    @Internal
    public boolean getFPropRMark() {
        return field_51_fPropRMark;
    }

    @Internal
    public void setFPropRMark(boolean field_51_fPropRMark) {
        this.field_51_fPropRMark = field_51_fPropRMark;
    }

    @Internal
    public int getIbstPropRMark() {
        return field_52_ibstPropRMark;
    }

    @Internal
    public void setIbstPropRMark(int field_52_ibstPropRMark) {
        this.field_52_ibstPropRMark = field_52_ibstPropRMark;
    }

    @Internal
    public DateAndTime getDttmPropRMark() {
        return field_53_dttmPropRMark;
    }

    @Internal
    public void setDttmPropRMark(DateAndTime field_53_dttmPropRMark) {
        this.field_53_dttmPropRMark = field_53_dttmPropRMark;
    }

    @Internal
    public boolean getFConflictOrig() {
        return field_54_fConflictOrig;
    }

    @Internal
    public void setFConflictOrig(boolean field_54_fConflictOrig) {
        this.field_54_fConflictOrig = field_54_fConflictOrig;
    }

    @Internal
    public boolean getFConflictOtherDel() {
        return field_55_fConflictOtherDel;
    }

    @Internal
    public void setFConflictOtherDel(boolean field_55_fConflictOtherDel) {
        this.field_55_fConflictOtherDel = field_55_fConflictOtherDel;
    }

    @Internal
    public int getWConflict() {
        return field_56_wConflict;
    }

    @Internal
    public void setWConflict(int field_56_wConflict) {
        this.field_56_wConflict = field_56_wConflict;
    }

    @Internal
    public int getIbstConflict() {
        return field_57_IbstConflict;
    }

    @Internal
    public void setIbstConflict(int field_57_IbstConflict) {
        this.field_57_IbstConflict = field_57_IbstConflict;
    }

    @Internal
    public DateAndTime getDttmConflict() {
        return field_58_dttmConflict;
    }

    @Internal
    public void setDttmConflict(DateAndTime field_58_dttmConflict) {
        this.field_58_dttmConflict = field_58_dttmConflict;
    }

    @Internal
    public boolean getFDispFldRMark() {
        return field_59_fDispFldRMark;
    }

    @Internal
    public void setFDispFldRMark(boolean field_59_fDispFldRMark) {
        this.field_59_fDispFldRMark = field_59_fDispFldRMark;
    }

    @Internal
    public int getIbstDispFldRMark() {
        return field_60_ibstDispFldRMark;
    }

    @Internal
    public void setIbstDispFldRMark(int field_60_ibstDispFldRMark) {
        this.field_60_ibstDispFldRMark = field_60_ibstDispFldRMark;
    }

    @Internal
    public DateAndTime getDttmDispFldRMark() {
        return field_61_dttmDispFldRMark;
    }

    @Internal
    public void setDttmDispFldRMark(DateAndTime field_61_dttmDispFldRMark) {
        this.field_61_dttmDispFldRMark = field_61_dttmDispFldRMark;
    }

    @Internal
    public byte[] getXstDispFldRMark() {
        return field_62_xstDispFldRMark;
    }

    @Internal
    public void setXstDispFldRMark(byte[] field_62_xstDispFldRMark) {
        this.field_62_xstDispFldRMark = field_62_xstDispFldRMark;
    }

    @Internal
    public int getFcObjp() {
        return field_63_fcObjp;
    }

    @Internal
    public void setFcObjp(int field_63_fcObjp) {
        this.field_63_fcObjp = field_63_fcObjp;
    }

    @Internal
    public byte getLbrCRJ() {
        return field_64_lbrCRJ;
    }

    @Internal
    public void setLbrCRJ(byte field_64_lbrCRJ) {
        this.field_64_lbrCRJ = field_64_lbrCRJ;
    }

    @Internal
    public boolean getFSpecVanish() {
        return field_65_fSpecVanish;
    }

    @Internal
    public void setFSpecVanish(boolean field_65_fSpecVanish) {
        this.field_65_fSpecVanish = field_65_fSpecVanish;
    }

    @Internal
    public boolean getFHasOldProps() {
        return field_66_fHasOldProps;
    }

    @Internal
    public void setFHasOldProps(boolean field_66_fHasOldProps) {
        this.field_66_fHasOldProps = field_66_fHasOldProps;
    }

    @Internal
    public boolean getFSdtVanish() {
        return field_67_fSdtVanish;
    }

    @Internal
    public void setFSdtVanish(boolean field_67_fSdtVanish) {
        this.field_67_fSdtVanish = field_67_fSdtVanish;
    }

    @Internal
    public int getWCharScale() {
        return field_68_wCharScale;
    }

    @Internal
    public void setWCharScale(int field_68_wCharScale) {
        this.field_68_wCharScale = field_68_wCharScale;
    }

    @Internal
    public boolean isFBold() {
        return fBold.isSet(field_1_grpfChp);
    }

    @Internal
    public void setFBold(boolean value) {
        field_1_grpfChp = fBold.setBoolean(field_1_grpfChp, value);
    }

    @Internal
    public boolean isFItalic() {
        return fItalic.isSet(field_1_grpfChp);
    }

    @Internal
    public void setFItalic(boolean value) {
        field_1_grpfChp = fItalic.setBoolean(field_1_grpfChp, value);
    }

    @Internal
    public boolean isFRMarkDel() {
        return fRMarkDel.isSet(field_1_grpfChp);
    }

    @Internal
    public void setFRMarkDel(boolean value) {
        field_1_grpfChp = fRMarkDel.setBoolean(field_1_grpfChp, value);
    }

    @Internal
    public boolean isFOutline() {
        return fOutline.isSet(field_1_grpfChp);
    }

    @Internal
    public void setFOutline(boolean value) {
        field_1_grpfChp = fOutline.setBoolean(field_1_grpfChp, value);
    }

    @Internal
    public boolean isFFldVanish() {
        return fFldVanish.isSet(field_1_grpfChp);
    }

    @Internal
    public void setFFldVanish(boolean value) {
        field_1_grpfChp = fFldVanish.setBoolean(field_1_grpfChp, value);
    }

    @Internal
    public boolean isFSmallCaps() {
        return fSmallCaps.isSet(field_1_grpfChp);
    }

    @Internal
    public void setFSmallCaps(boolean value) {
        field_1_grpfChp = fSmallCaps.setBoolean(field_1_grpfChp, value);
    }

    @Internal
    public boolean isFCaps() {
        return fCaps.isSet(field_1_grpfChp);
    }

    @Internal
    public void setFCaps(boolean value) {
        field_1_grpfChp = fCaps.setBoolean(field_1_grpfChp, value);
    }

    @Internal
    public boolean isFVanish() {
        return fVanish.isSet(field_1_grpfChp);
    }

    @Internal
    public void setFVanish(boolean value) {
        field_1_grpfChp = fVanish.setBoolean(field_1_grpfChp, value);
    }

    @Internal
    public boolean isFRMark() {
        return fRMark.isSet(field_1_grpfChp);
    }

    @Internal
    public void setFRMark(boolean value) {
        field_1_grpfChp = fRMark.setBoolean(field_1_grpfChp, value);
    }

    @Internal
    public boolean isFSpec() {
        return fSpec.isSet(field_1_grpfChp);
    }

    @Internal
    public void setFSpec(boolean value) {
        field_1_grpfChp = fSpec.setBoolean(field_1_grpfChp, value);
    }

    @Internal
    public boolean isFStrike() {
        return fStrike.isSet(field_1_grpfChp);
    }

    @Internal
    public void setFStrike(boolean value) {
        field_1_grpfChp = fStrike.setBoolean(field_1_grpfChp, value);
    }

    @Internal
    public boolean isFObj() {
        return fObj.isSet(field_1_grpfChp);
    }

    @Internal
    public void setFObj(boolean value) {
        field_1_grpfChp = fObj.setBoolean(field_1_grpfChp, value);
    }

    @Internal
    public boolean isFShadow() {
        return fShadow.isSet(field_1_grpfChp);
    }

    @Internal
    public void setFShadow(boolean value) {
        field_1_grpfChp = fShadow.setBoolean(field_1_grpfChp, value);
    }

    @Internal
    public boolean isFLowerCase() {
        return fLowerCase.isSet(field_1_grpfChp);
    }

    @Internal
    public void setFLowerCase(boolean value) {
        field_1_grpfChp = fLowerCase.setBoolean(field_1_grpfChp, value);
    }

    @Internal
    public boolean isFData() {
        return fData.isSet(field_1_grpfChp);
    }

    @Internal
    public void setFData(boolean value) {
        field_1_grpfChp = fData.setBoolean(field_1_grpfChp, value);
    }

    @Internal
    public boolean isFOle2() {
        return fOle2.isSet(field_1_grpfChp);
    }

    @Internal
    public void setFOle2(boolean value) {
        field_1_grpfChp = fOle2.setBoolean(field_1_grpfChp, value);
    }

    @Internal
    public boolean isFEmboss() {
        return fEmboss.isSet(field_1_grpfChp);
    }

    @Internal
    public void setFEmboss(boolean value) {
        field_1_grpfChp = fEmboss.setBoolean(field_1_grpfChp, value);
    }

    @Internal
    public boolean isFImprint() {
        return fImprint.isSet(field_1_grpfChp);
    }

    @Internal
    public void setFImprint(boolean value) {
        field_1_grpfChp = fImprint.setBoolean(field_1_grpfChp, value);
    }

    @Internal
    public boolean isFDStrike() {
        return fDStrike.isSet(field_1_grpfChp);
    }

    @Internal
    public void setFDStrike(boolean value) {
        field_1_grpfChp = fDStrike.setBoolean(field_1_grpfChp, value);
    }

    @Internal
    public boolean isFUsePgsuSettings() {
        return fUsePgsuSettings.isSet(field_1_grpfChp);
    }

    @Internal
    public void setFUsePgsuSettings(boolean value) {
        field_1_grpfChp = fUsePgsuSettings.setBoolean(field_1_grpfChp, value);
    }

    @Internal
    public boolean isFBoldBi() {
        return fBoldBi.isSet(field_1_grpfChp);
    }

    @Internal
    public void setFBoldBi(boolean value) {
        field_1_grpfChp = fBoldBi.setBoolean(field_1_grpfChp, value);
    }

    @Internal
    public boolean isFComplexScripts() {
        return fComplexScripts.isSet(field_1_grpfChp);
    }

    @Internal
    public void setFComplexScripts(boolean value) {
        field_1_grpfChp = fComplexScripts.setBoolean(field_1_grpfChp, value);
    }

    @Internal
    public boolean isFItalicBi() {
        return fItalicBi.isSet(field_1_grpfChp);
    }

    @Internal
    public void setFItalicBi(boolean value) {
        field_1_grpfChp = fItalicBi.setBoolean(field_1_grpfChp, value);
    }

    @Internal
    public boolean isFBiDi() {
        return fBiDi.isSet(field_1_grpfChp);
    }

    @Internal
    public void setFBiDi(boolean value) {
        field_1_grpfChp = fBiDi.setBoolean(field_1_grpfChp, value);
    }

    @Internal
    public short getItypFELayout() {
        return (short) itypFELayout.getValue(field_29_ufel);
    }

    @Internal
    public void setItypFELayout(short value) {
        field_29_ufel = (short) itypFELayout.setValue(field_29_ufel, value);
    }

    @Internal
    public boolean isFTNY() {
        return fTNY.isSet(field_29_ufel);
    }

    @Internal
    public void setFTNY(boolean value) {
        field_29_ufel = (short) fTNY.setBoolean(field_29_ufel, value);
    }

    @Internal
    public boolean isFWarichu() {
        return fWarichu.isSet(field_29_ufel);
    }

    @Internal
    public void setFWarichu(boolean value) {
        field_29_ufel = (short) fWarichu.setBoolean(field_29_ufel, value);
    }

    @Internal
    public boolean isFKumimoji() {
        return fKumimoji.isSet(field_29_ufel);
    }

    @Internal
    public void setFKumimoji(boolean value) {
        field_29_ufel = (short) fKumimoji.setBoolean(field_29_ufel, value);
    }

    @Internal
    public boolean isFRuby() {
        return fRuby.isSet(field_29_ufel);
    }

    @Internal
    public void setFRuby(boolean value) {
        field_29_ufel = (short) fRuby.setBoolean(field_29_ufel, value);
    }

    @Internal
    public boolean isFLSFitText() {
        return fLSFitText.isSet(field_29_ufel);
    }

    @Internal
    public void setFLSFitText(boolean value) {
        field_29_ufel = (short) fLSFitText.setBoolean(field_29_ufel, value);
    }

    @Internal
    public byte getSpare() {
        return (byte) spare.getValue(field_29_ufel);
    }

    @Internal
    public void setSpare(byte value) {
        field_29_ufel = (short) spare.setValue(field_29_ufel, value);
    }

    @Internal
    public byte getIWarichuBracket() {
        return (byte) iWarichuBracket.getValue(field_30_copt);
    }

    @Internal
    public void setIWarichuBracket(byte value) {
        field_30_copt = (byte) iWarichuBracket.setValue(field_30_copt, value);
    }

    @Internal
    public boolean isFWarichuNoOpenBracket() {
        return fWarichuNoOpenBracket.isSet(field_30_copt);
    }

    @Internal
    public void setFWarichuNoOpenBracket(boolean value) {
        field_30_copt = (byte) fWarichuNoOpenBracket.setBoolean(field_30_copt, value);
    }

    @Internal
    public boolean isFTNYCompress() {
        return fTNYCompress.isSet(field_30_copt);
    }

    @Internal
    public void setFTNYCompress(boolean value) {
        field_30_copt = (byte) fTNYCompress.setBoolean(field_30_copt, value);
    }

    @Internal
    public boolean isFTNYFetchTxm() {
        return fTNYFetchTxm.isSet(field_30_copt);
    }

    @Internal
    public void setFTNYFetchTxm(boolean value) {
        field_30_copt = (byte) fTNYFetchTxm.setBoolean(field_30_copt, value);
    }

    @Internal
    public boolean isFCellFitText() {
        return fCellFitText.isSet(field_30_copt);
    }

    @Internal
    public void setFCellFitText(boolean value) {
        field_30_copt = (byte) fCellFitText.setBoolean(field_30_copt, value);
    }

    @Internal
    public boolean isUnused() {
        return unused.isSet(field_30_copt);
    }

    @Internal
    public void setUnused(boolean value) {
        field_30_copt = (byte) unused.setBoolean(field_30_copt, value);
    }

    @Internal
    public byte getIcoHighlight() {
        return (byte) icoHighlight.getValue(field_48_Highlight);
    }

    @Internal
    public void setIcoHighlight(byte value) {
        field_48_Highlight = (short) icoHighlight.setValue(field_48_Highlight, value);
    }

    @Internal
    public boolean isFHighlight() {
        return fHighlight.isSet(field_48_Highlight);
    }

    @Internal
    public void setFHighlight(boolean value) {
        field_48_Highlight = (short) fHighlight.setBoolean(field_48_Highlight, value);
    }

    @Internal
    public boolean isFChsDiff() {
        return fChsDiff.isSet(field_49_CharsetFlags);
    }

    @Internal
    public void setFChsDiff(boolean value) {
        field_49_CharsetFlags = (short) fChsDiff.setBoolean(field_49_CharsetFlags, value);
    }

    @Internal
    public boolean isFMacChs() {
        return fMacChs.isSet(field_49_CharsetFlags);
    }

    @Internal
    public void setFMacChs(boolean value) {
        field_49_CharsetFlags = (short) fMacChs.setBoolean(field_49_CharsetFlags, value);
    }

    @Internal
    public int getUnderlineColor() {
        if (field_69_underlineColor != null) {
            return field_69_underlineColor.getValue();
        }

        return -1;
    }

    @Internal
    public void setUnderlineColor(Colorref field_69_underlineColor) {
        this.field_69_underlineColor = field_69_underlineColor;
    }

}  