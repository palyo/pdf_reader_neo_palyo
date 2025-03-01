package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.formula.udf;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.formula.atp.AnalysisToolPak;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.formula.function.FreeRefFunction;

public interface UDFFinder {
    UDFFinder DEFAULT = new AggregatingUDFFinder(AnalysisToolPak.instance);

    FreeRefFunction findFunction(String name);
}
