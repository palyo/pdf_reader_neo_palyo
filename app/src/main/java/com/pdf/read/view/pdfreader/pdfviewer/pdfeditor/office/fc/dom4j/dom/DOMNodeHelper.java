package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.dom4j.dom;

import org.w3c.dom.DOMException;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;

import java.util.List;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.dom4j.Branch;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.dom4j.CharacterData;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.dom4j.Document;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.dom4j.DocumentType;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.dom4j.Element;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.dom4j.Node;

public class DOMNodeHelper {
    public static final NodeList EMPTY_NODE_LIST = new EmptyNodeList();

    protected DOMNodeHelper() {
    }

    public static boolean supports(Node node, String feature, String version) {
        return false;
    }

    public static String getNamespaceURI(Node node) {
        return null;
    }

    public static String getPrefix(Node node) {
        return null;
    }

    public static String getLocalName(Node node) {
        return null;
    }

    public static void setPrefix(Node node, String prefix) throws DOMException {
        notSupported();
    }

    public static String getNodeValue(Node node) throws DOMException {
        return node.getText();
    }

    public static void setNodeValue(Node node, String nodeValue) throws DOMException {
        node.setText(nodeValue);
    }

    public static org.w3c.dom.Node getParentNode(Node node) {
        return asDOMNode(node.getParent());
    }

    public static NodeList getChildNodes(Node node) {
        return EMPTY_NODE_LIST;
    }

    public static org.w3c.dom.Node getFirstChild(Node node) {
        return null;
    }

    public static org.w3c.dom.Node getLastChild(Node node) {
        return null;
    }

    public static org.w3c.dom.Node getPreviousSibling(Node node) {
        Element parent = node.getParent();

        if (parent != null) {
            int index = parent.indexOf(node);

            if (index > 0) {
                Node previous = parent.node(index - 1);

                return asDOMNode(previous);
            }
        }

        return null;
    }

    public static org.w3c.dom.Node getNextSibling(Node node) {
        Element parent = node.getParent();

        if (parent != null) {
            int index = parent.indexOf(node);

            if (index >= 0) {
                if (++index < parent.nodeCount()) {
                    Node next = parent.node(index);

                    return asDOMNode(next);
                }
            }
        }

        return null;
    }

    public static NamedNodeMap getAttributes(Node node) {
        return null;
    }

    public static org.w3c.dom.Document getOwnerDocument(Node node) {
        return asDOMDocument(node.getDocument());
    }

    public static org.w3c.dom.Node insertBefore(Node node, org.w3c.dom.Node newChild,
                                                org.w3c.dom.Node refChild) throws DOMException {
        if (node instanceof Branch) {
            Branch branch = (Branch) node;
            List list = branch.content();
            int index = list.indexOf(refChild);

            if (index < 0) {
                branch.add((Node) newChild);
            } else {
                list.add(index, newChild);
            }

            return newChild;
        } else {
            throw new DOMException(DOMException.HIERARCHY_REQUEST_ERR,
                    "Children not allowed for this node: " + node);
        }
    }

    public static org.w3c.dom.Node replaceChild(Node node, org.w3c.dom.Node newChild,
                                                org.w3c.dom.Node oldChild) throws DOMException {
        if (node instanceof Branch) {
            Branch branch = (Branch) node;
            List list = branch.content();
            int index = list.indexOf(oldChild);

            if (index < 0) {
                throw new DOMException(DOMException.NOT_FOUND_ERR,
                        "Tried to replace a non existing child " + "for node: " + node);
            }

            list.set(index, newChild);

            return oldChild;
        } else {
            throw new DOMException(DOMException.HIERARCHY_REQUEST_ERR,
                    "Children not allowed for this node: " + node);
        }
    }

    public static org.w3c.dom.Node removeChild(Node node, org.w3c.dom.Node oldChild)
            throws DOMException {
        if (node instanceof Branch) {
            Branch branch = (Branch) node;
            branch.remove((Node) oldChild);

            return oldChild;
        }

        throw new DOMException(DOMException.HIERARCHY_REQUEST_ERR,
                "Children not allowed for this node: " + node);
    }

    public static org.w3c.dom.Node appendChild(Node node, org.w3c.dom.Node newChild)
            throws DOMException {
        if (node instanceof Branch) {
            Branch branch = (Branch) node;
            org.w3c.dom.Node previousParent = newChild.getParentNode();

            if (previousParent != null) {
                previousParent.removeChild(newChild);
            }

            branch.add((Node) newChild);

            return newChild;
        }

        throw new DOMException(DOMException.HIERARCHY_REQUEST_ERR,
                "Children not allowed for this node: " + node);
    }

    public static boolean hasChildNodes(Node node) {
        return false;
    }

    public static org.w3c.dom.Node cloneNode(Node node, boolean deep) {
        return asDOMNode((Node) node.clone());
    }

    public static void normalize(Node node) {
        notSupported();
    }

    public static boolean isSupported(Node n, String feature, String version) {
        return false;
    }

    public static boolean hasAttributes(Node node) {
        if ((node != null) && node instanceof Element) {
            return ((Element) node).attributeCount() > 0;
        } else {
            return false;
        }
    }

    public static String getData(CharacterData charData) throws DOMException {
        return charData.getText();
    }

    public static void setData(CharacterData charData, String data) throws DOMException {
        charData.setText(data);
    }

    public static int getLength(CharacterData charData) {
        String text = charData.getText();

        return (text != null) ? text.length() : 0;
    }

    public static String substringData(CharacterData charData, int offset, int count)
            throws DOMException {
        if (count < 0) {
            throw new DOMException(DOMException.INDEX_SIZE_ERR, "Illegal value for count: " + count);
        }

        String text = charData.getText();
        int length = (text != null) ? text.length() : 0;

        if ((offset < 0) || (offset >= length)) {
            throw new DOMException(DOMException.INDEX_SIZE_ERR, "No text at offset: " + offset);
        }

        if ((offset + count) > length) {
            return text.substring(offset);
        }

        return text.substring(offset, offset + count);
    }

    public static void appendData(CharacterData charData, String arg) throws DOMException {
        if (charData.isReadOnly()) {
            throw new DOMException(DOMException.NO_MODIFICATION_ALLOWED_ERR,
                    "CharacterData node is read only: " + charData);
        } else {
            String text = charData.getText();

            if (text == null) {
                charData.setText(text);
            } else {
                charData.setText(text + arg);
            }
        }
    }

    public static void insertData(CharacterData data, int offset, String arg) throws DOMException {
        if (data.isReadOnly()) {
            throw new DOMException(DOMException.NO_MODIFICATION_ALLOWED_ERR,
                    "CharacterData node is read only: " + data);
        } else {
            String text = data.getText();

            if (text == null) {
                data.setText(arg);
            } else {
                int length = text.length();

                if ((offset < 0) || (offset > length)) {
                    throw new DOMException(DOMException.INDEX_SIZE_ERR, "No text at offset: "
                            + offset);
                } else {
                    StringBuffer buffer = new StringBuffer(text);
                    buffer.insert(offset, arg);
                    data.setText(buffer.toString());
                }
            }
        }
    }

    public static void deleteData(CharacterData charData, int offset, int count)
            throws DOMException {
        if (charData.isReadOnly()) {
            throw new DOMException(DOMException.NO_MODIFICATION_ALLOWED_ERR,
                    "CharacterData node is read only: " + charData);
        } else {
            if (count < 0) {
                throw new DOMException(DOMException.INDEX_SIZE_ERR, "Illegal value for count: "
                        + count);
            }

            String text = charData.getText();

            if (text != null) {
                int length = text.length();

                if ((offset < 0) || (offset >= length)) {
                    throw new DOMException(DOMException.INDEX_SIZE_ERR, "No text at offset: "
                            + offset);
                } else {
                    StringBuffer buffer = new StringBuffer(text);
                    buffer.delete(offset, offset + count);
                    charData.setText(buffer.toString());
                }
            }
        }
    }

    public static void replaceData(CharacterData charData, int offset, int count, String arg)
            throws DOMException {
        if (charData.isReadOnly()) {
            throw new DOMException(DOMException.NO_MODIFICATION_ALLOWED_ERR,
                    "CharacterData node is read only: " + charData);
        } else {
            if (count < 0) {
                throw new DOMException(DOMException.INDEX_SIZE_ERR, "Illegal value for count: "
                        + count);
            }

            String text = charData.getText();

            if (text != null) {
                int length = text.length();

                if ((offset < 0) || (offset >= length)) {
                    throw new DOMException(DOMException.INDEX_SIZE_ERR, "No text at offset: "
                            + offset);
                } else {
                    StringBuffer buffer = new StringBuffer(text);
                    buffer.replace(offset, offset + count, arg);
                    charData.setText(buffer.toString());
                }
            }
        }
    }

    public static void appendElementsByTagName(List list, Branch parent, String name) {
        final boolean isStar = "*".equals(name);

        for (int i = 0, size = parent.nodeCount(); i < size; i++) {
            Node node = parent.node(i);

            if (node instanceof Element) {
                Element element = (Element) node;

                if (isStar || name.equals(element.getName())) {
                    list.add(element);
                }

                appendElementsByTagName(list, element, name);
            }
        }
    }

    public static void appendElementsByTagNameNS(List list, Branch parent, String namespace,
                                                 String localName) {
        final boolean isStarNS = "*".equals(namespace);
        final boolean isStar = "*".equals(localName);

        for (int i = 0, size = parent.nodeCount(); i < size; i++) {
            Node node = parent.node(i);

            if (node instanceof Element) {
                Element element = (Element) node;

                if ((isStarNS
                        || (((namespace == null) || (namespace.length() == 0)) && ((element
                        .getNamespaceURI() == null) || (element.getNamespaceURI().length() == 0))) || ((namespace != null) && namespace
                        .equals(element.getNamespaceURI())))
                        && (isStar || localName.equals(element.getName()))) {
                    list.add(element);
                }

                appendElementsByTagNameNS(list, element, namespace, localName);
            }
        }
    }

    public static NodeList createNodeList(final List list) {
        return new NodeList() {
            public org.w3c.dom.Node item(int index) {
                if (index >= getLength()) {

                    return null;
                } else {
                    return DOMNodeHelper.asDOMNode((Node) list.get(index));
                }
            }

            public int getLength() {
                return list.size();
            }
        };
    }

    public static org.w3c.dom.Node asDOMNode(Node node) {
        if (node == null) {
            return null;
        }

        if (node instanceof org.w3c.dom.Node) {
            return (org.w3c.dom.Node) node;
        } else {

            notSupported();

            return null;
        }
    }

    public static org.w3c.dom.Document asDOMDocument(Document document) {
        if (document == null) {
            return null;
        }

        if (document instanceof org.w3c.dom.Document) {
            return (org.w3c.dom.Document) document;
        } else {

            notSupported();

            return null;
        }
    }

    public static org.w3c.dom.DocumentType asDOMDocumentType(DocumentType dt) {
        if (dt == null) {
            return null;
        }

        if (dt instanceof org.w3c.dom.DocumentType) {
            return (org.w3c.dom.DocumentType) dt;
        } else {

            notSupported();

            return null;
        }
    }

    public static org.w3c.dom.Text asDOMText(CharacterData text) {
        if (text == null) {
            return null;
        }

        if (text instanceof org.w3c.dom.Text) {
            return (org.w3c.dom.Text) text;
        } else {

            notSupported();

            return null;
        }
    }

    public static org.w3c.dom.Element asDOMElement(Node element) {
        if (element == null) {
            return null;
        }

        if (element instanceof org.w3c.dom.Element) {
            return (org.w3c.dom.Element) element;
        } else {

            notSupported();

            return null;
        }
    }

    public static org.w3c.dom.Attr asDOMAttr(Node attribute) {
        if (attribute == null) {
            return null;
        }

        if (attribute instanceof org.w3c.dom.Attr) {
            return (org.w3c.dom.Attr) attribute;
        } else {

            notSupported();

            return null;
        }
    }

    public static void notSupported() {
        throw new DOMException(DOMException.NOT_SUPPORTED_ERR, "Not supported yet");
    }

    public static class EmptyNodeList implements NodeList {
        public org.w3c.dom.Node item(int index) {
            return null;
        }

        public int getLength() {
            return 0;
        }
    }
}
