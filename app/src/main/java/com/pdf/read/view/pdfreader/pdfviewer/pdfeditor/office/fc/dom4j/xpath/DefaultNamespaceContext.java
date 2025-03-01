package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.dom4j.xpath;

import java.io.Serializable;
import java.util.Iterator;

import javax.xml.namespace.NamespaceContext;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.dom4j.Document;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.dom4j.Element;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.dom4j.Namespace;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.dom4j.Node;

public class DefaultNamespaceContext implements NamespaceContext, Serializable {
    private final Element element;

    public DefaultNamespaceContext(Element element) {
        this.element = element;
    }

    public static DefaultNamespaceContext create(Object node) {
        Element element = null;

        if (node instanceof Element) {
            element = (Element) node;
        } else if (node instanceof Document) {
            Document doc = (Document) node;
            element = doc.getRootElement();
        } else if (node instanceof Node) {
            element = ((Node) node).getParent();
        }

        if (element != null) {
            return new DefaultNamespaceContext(element);
        }

        return null;
    }

    public String translateNamespacePrefixToUri(String prefix) {
        if ((prefix != null) && (prefix.length() > 0)) {
            Namespace ns = element.getNamespaceForPrefix(prefix);

            if (ns != null) {
                return ns.getURI();
            }
        }

        return null;
    }

    @Override
    public String getNamespaceURI(String prefix) {

        return null;
    }

    @Override
    public String getPrefix(String namespaceURI) {

        return null;
    }

    @Override
    public Iterator getPrefixes(String namespaceURI) {

        return null;
    }
}