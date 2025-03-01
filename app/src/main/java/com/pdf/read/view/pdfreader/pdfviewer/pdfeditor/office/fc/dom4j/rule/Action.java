package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.dom4j.rule;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.dom4j.Node;

public interface Action {
    void run(Node node) throws Exception;
}