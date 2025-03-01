package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.dom4j.tree;

import java.io.IOException;
import java.io.Writer;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.dom4j.Text;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.dom4j.Visitor;

public abstract class AbstractText extends AbstractCharacterData implements Text {
    public AbstractText() {
    }

    public short getNodeType() {
        return TEXT_NODE;
    }

    public String toString() {
        return super.toString() + " [Text: \"" + getText() + "\"]";
    }

    public String asXML() {
        return getText();
    }

    public void write(Writer writer) throws IOException {
        writer.write(getText());
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}