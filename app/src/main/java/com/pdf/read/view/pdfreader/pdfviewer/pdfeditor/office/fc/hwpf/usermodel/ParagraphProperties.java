package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hwpf.usermodel;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hwpf.model.types.PAPAbstractType;

public final class ParagraphProperties extends PAPAbstractType implements Cloneable {

    private boolean jcLogical = false;

    private short tabClearPosition;

    public ParagraphProperties() {
        setAnld(new byte[84]);
        setPhe(new byte[12]);
    }

    public Object clone() throws CloneNotSupportedException {
        ParagraphProperties pp = (ParagraphProperties) super.clone();
        pp.setAnld(getAnld().clone());
        pp.setBrcTop((BorderCode) getBrcTop().clone());
        pp.setBrcLeft((BorderCode) getBrcLeft().clone());
        pp.setBrcBottom((BorderCode) getBrcBottom().clone());
        pp.setBrcRight((BorderCode) getBrcRight().clone());
        pp.setBrcBetween((BorderCode) getBrcBetween().clone());
        pp.setBrcBar((BorderCode) getBrcBar().clone());
        pp.setDcs(getDcs().clone());
        pp.setLspd((LineSpacingDescriptor) getLspd().clone());
        pp.setShd((ShadingDescriptor) getShd().clone());
        pp.setPhe(getPhe().clone());
        return pp;
    }

    public BorderCode getBarBorder() {
        return super.getBrcBar();
    }

    public void setBarBorder(BorderCode bar) {
        super.setBrcBar(bar);
    }

    public BorderCode getBottomBorder() {
        return super.getBrcBottom();
    }

    public void setBottomBorder(BorderCode bottom) {
        super.setBrcBottom(bottom);
    }

    public DropCapSpecifier getDropCap() {
        return super.getDcs();
    }

    public void setDropCap(DropCapSpecifier dcs) {
        super.setDcs(dcs);
    }

    public int getFirstLineIndent() {
        return super.getDxaLeft1();
    }

    public void setFirstLineIndent(int first) {
        super.setDxaLeft1(first);
    }

    public int getFontAlignment() {
        return super.getWAlignFont();
    }

    public void setFontAlignment(int align) {
        super.setWAlignFont(align);
    }

    public int getIndentFromLeft() {
        return super.getDxaLeft();
    }

    public void setIndentFromLeft(int dxaLeft) {
        super.setDxaLeft(dxaLeft);
    }

    public int getIndentFromRight() {
        return super.getDxaRight();
    }

    public void setIndentFromRight(int dxaRight) {
        super.setDxaRight(dxaRight);
    }

    public int getJustification() {
        if (jcLogical) {
            if (!getFBiDi())
                return getJc();

            switch (getJc()) {
                case 0:
                    return 2;
                case 2:
                    return 0;
                default:
                    return getJc();
            }
        }

        return getJc();
    }

    public void setJustification(byte jc) {
        super.setJc(jc);
        this.jcLogical = false;
    }

    public BorderCode getLeftBorder() {
        return super.getBrcLeft();
    }

    public void setLeftBorder(BorderCode left) {
        super.setBrcLeft(left);
    }

    public LineSpacingDescriptor getLineSpacing() {
        return super.getLspd();
    }

    public void setLineSpacing(LineSpacingDescriptor lspd) {
        super.setLspd(lspd);
    }

    public BorderCode getRightBorder() {
        return super.getBrcRight();
    }

    public void setRightBorder(BorderCode right) {
        super.setBrcRight(right);
    }

    public ShadingDescriptor getShading() {
        return super.getShd();
    }

    public void setShading(ShadingDescriptor shd) {
        super.setShd(shd);
    }

    public int getSpacingAfter() {
        return super.getDyaAfter();
    }

    public void setSpacingAfter(int after) {
        super.setDyaAfter(after);
    }

    public int getSpacingBefore() {
        return super.getDyaBefore();
    }

    public void setSpacingBefore(int before) {
        super.setDyaBefore(before);
    }

    public BorderCode getTopBorder() {
        return super.getBrcTop();
    }

    public void setTopBorder(BorderCode top) {
        super.setBrcTop(top);
    }

    public boolean isAutoHyphenated() {
        return !super.getFNoAutoHyph();
    }

    public void setAutoHyphenated(boolean auto) {
        super.setFNoAutoHyph(!auto);
    }

    public boolean isBackward() {
        return super.isFBackward();
    }

    public void setBackward(boolean bward) {
        super.setFBackward(bward);
    }

    public boolean isKinsoku() {
        return super.getFKinsoku();
    }

    public void setKinsoku(boolean kinsoku) {
        super.setFKinsoku(kinsoku);
    }

    public boolean isLineNotNumbered() {
        return super.getFNoLnn();
    }

    public void setLineNotNumbered(boolean fNoLnn) {
        super.setFNoLnn(fNoLnn);
    }

    public boolean isSideBySide() {
        return super.getFSideBySide();
    }

    public void setSideBySide(boolean fSideBySide) {
        super.setFSideBySide(fSideBySide);
    }

    public boolean isVertical() {
        return super.isFVertical();
    }

    public void setVertical(boolean vertical) {
        super.setFVertical(vertical);
    }

    public boolean isWidowControlled() {
        return super.getFWidowControl();
    }

    public boolean isWordWrapped() {
        return super.getFWordWrap();
    }

    public void setWordWrapped(boolean wrap) {
        super.setFWordWrap(wrap);
    }

    public boolean keepOnPage() {
        return super.getFKeep();
    }

    public boolean keepWithNext() {
        return super.getFKeepFollow();
    }

    public boolean pageBreakBefore() {
        return super.getFPageBreakBefore();
    }

    public void setJustificationLogical(byte jc) {
        super.setJc(jc);
        this.jcLogical = true;
    }

    public void setKeepOnPage(boolean fKeep) {
        super.setFKeep(fKeep);
    }

    public void setKeepWithNext(boolean fKeepFollow) {
        super.setFKeepFollow(fKeepFollow);
    }

    public void setPageBreakBefore(boolean fPageBreak) {
        super.setFPageBreakBefore(fPageBreak);
    }

    public void setWidowControl(boolean widowControl) {
        super.setFWidowControl(widowControl);
    }

    public short getTabClearPosition() {
        return this.tabClearPosition;
    }

    public void setTabClearPosition(short position) {
        this.tabClearPosition = position;
    }
}
