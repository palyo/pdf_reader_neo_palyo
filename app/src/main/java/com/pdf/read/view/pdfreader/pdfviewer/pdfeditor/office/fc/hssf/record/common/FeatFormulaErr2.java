package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.record.common;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.record.RecordInputStream;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.BitField;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.BitFieldFactory;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.LittleEndianOutput;

public final class FeatFormulaErr2 implements SharedFeature {
    static BitField checkCalculationErrors =
            BitFieldFactory.getInstance(0x01);
    static BitField checkEmptyCellRef =
            BitFieldFactory.getInstance(0x02);
    static BitField checkNumbersAsText =
            BitFieldFactory.getInstance(0x04);
    static BitField checkInconsistentRanges =
            BitFieldFactory.getInstance(0x08);
    static BitField checkInconsistentFormulas =
            BitFieldFactory.getInstance(0x10);
    static BitField checkDateTimeFormats =
            BitFieldFactory.getInstance(0x20);
    static BitField checkUnprotectedFormulas =
            BitFieldFactory.getInstance(0x40);
    static BitField performDataValidation =
            BitFieldFactory.getInstance(0x80);

    private final int errorCheck;

    public FeatFormulaErr2(RecordInputStream in) {
        errorCheck = in.readInt();
    }

    public String toString() {
        String buffer = " [FEATURE FORMULA ERRORS]\n" +
                "  checkCalculationErrors    = " +
                "  checkEmptyCellRef         = " +
                "  checkNumbersAsText        = " +
                "  checkInconsistentRanges   = " +
                "  checkInconsistentFormulas = " +
                "  checkDateTimeFormats      = " +
                "  checkUnprotectedFormulas  = " +
                "  performDataValidation     = " +
                " [/FEATURE FORMULA ERRORS]\n";
        return buffer;
    }

    public void serialize(LittleEndianOutput out) {
        out.writeInt(errorCheck);
    }

    public int getDataSize() {
        return 4;
    }

    public int _getRawErrorCheckValue() {
        return errorCheck;
    }

    public boolean getCheckCalculationErrors() {
        return checkCalculationErrors.isSet(errorCheck);
    }

    public void setCheckCalculationErrors(boolean checkCalculationErrors) {
        FeatFormulaErr2.checkCalculationErrors.setBoolean(
                errorCheck, checkCalculationErrors);
    }

    public boolean getCheckEmptyCellRef() {
        return checkEmptyCellRef.isSet(errorCheck);
    }

    public void setCheckEmptyCellRef(boolean checkEmptyCellRef) {
        FeatFormulaErr2.checkEmptyCellRef.setBoolean(
                errorCheck, checkEmptyCellRef);
    }

    public boolean getCheckNumbersAsText() {
        return checkNumbersAsText.isSet(errorCheck);
    }

    public void setCheckNumbersAsText(boolean checkNumbersAsText) {
        FeatFormulaErr2.checkNumbersAsText.setBoolean(
                errorCheck, checkNumbersAsText);
    }

    public boolean getCheckInconsistentRanges() {
        return checkInconsistentRanges.isSet(errorCheck);
    }

    public void setCheckInconsistentRanges(boolean checkInconsistentRanges) {
        FeatFormulaErr2.checkInconsistentRanges.setBoolean(
                errorCheck, checkInconsistentRanges);
    }

    public boolean getCheckInconsistentFormulas() {
        return checkInconsistentFormulas.isSet(errorCheck);
    }

    public void setCheckInconsistentFormulas(
            boolean checkInconsistentFormulas) {
        FeatFormulaErr2.checkInconsistentFormulas.setBoolean(
                errorCheck, checkInconsistentFormulas);
    }

    public boolean getCheckDateTimeFormats() {
        return checkDateTimeFormats.isSet(errorCheck);
    }

    public void setCheckDateTimeFormats(boolean checkDateTimeFormats) {
        FeatFormulaErr2.checkDateTimeFormats.setBoolean(
                errorCheck, checkDateTimeFormats);
    }

    public boolean getCheckUnprotectedFormulas() {
        return checkUnprotectedFormulas.isSet(errorCheck);
    }

    public void setCheckUnprotectedFormulas(boolean checkUnprotectedFormulas) {
        FeatFormulaErr2.checkUnprotectedFormulas.setBoolean(
                errorCheck, checkUnprotectedFormulas);
    }

    public boolean getPerformDataValidation() {
        return performDataValidation.isSet(errorCheck);
    }

    public void setPerformDataValidation(boolean performDataValidation) {
        FeatFormulaErr2.performDataValidation.setBoolean(
                errorCheck, performDataValidation);
    }
}
