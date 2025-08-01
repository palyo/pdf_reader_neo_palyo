package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.dom4j.tree;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.dom4j.CDATA;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.dom4j.Visitor;

public abstract class AbstractCDATA extends AbstractCharacterData implements CDATA {
    public AbstractCDATA() {
    }

    public short getNodeType() {
        return CDATA_SECTION_NODE;
    }

    public String toString() {
        return super.toString() + " [CDATA: \"" + getText() + "\"]";
    }

    public String asXML() {
        StringWriter writer = new StringWriter();

        try {
            write(writer);
        } catch (IOException e) {

        }

        return writer.toString();
    }

    public void write(Writer writer) throws IOException {
        writer.write("<![CDATA[");

        if (getText() != null) {
            writer.write(getText());
        }

        writer.write("]]>");
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}
