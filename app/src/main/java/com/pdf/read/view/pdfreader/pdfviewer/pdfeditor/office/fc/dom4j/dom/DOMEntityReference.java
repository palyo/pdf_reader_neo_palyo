package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.dom4j.dom;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.UserDataHandler;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.dom4j.Element;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.dom4j.tree.DefaultEntity;

public class DOMEntityReference extends DefaultEntity implements org.w3c.dom.EntityReference {
    public DOMEntityReference(String name) {
        super(name);
    }

    public DOMEntityReference(String name, String text) {
        super(name, text);
    }

    public DOMEntityReference(Element parent, String name, String text) {
        super(parent, name, text);
    }

    public boolean supports(String feature, String version) {
        return DOMNodeHelper.supports(this, feature, version);
    }

    public String getNamespaceURI() {
        return DOMNodeHelper.getNamespaceURI(this);
    }

    public String getPrefix() {
        return DOMNodeHelper.getPrefix(this);
    }

    public void setPrefix(String prefix) throws DOMException {
        DOMNodeHelper.setPrefix(this, prefix);
    }

    public String getLocalName() {
        return DOMNodeHelper.getLocalName(this);
    }

    public String getNodeName() {
        return getName();
    }

    public String getNodeValue() throws DOMException {
        return null;
    }

    public void setNodeValue(String nodeValue) throws DOMException {
    }

    public Node getParentNode() {
        return DOMNodeHelper.getParentNode(this);
    }

    public NodeList getChildNodes() {
        return DOMNodeHelper.getChildNodes(this);
    }

    public Node getFirstChild() {
        return DOMNodeHelper.getFirstChild(this);
    }

    public Node getLastChild() {
        return DOMNodeHelper.getLastChild(this);
    }

    public Node getPreviousSibling() {
        return DOMNodeHelper.getPreviousSibling(this);
    }

    public Node getNextSibling() {
        return DOMNodeHelper.getNextSibling(this);
    }

    public NamedNodeMap getAttributes() {
        return null;
    }

    public Document getOwnerDocument() {
        return DOMNodeHelper.getOwnerDocument(this);
    }

    public Node insertBefore(Node newChild, Node refChild)
            throws DOMException {
        checkNewChildNode(newChild);

        return DOMNodeHelper.insertBefore(this, newChild, refChild);
    }

    public Node replaceChild(Node newChild, Node oldChild)
            throws DOMException {
        checkNewChildNode(newChild);

        return DOMNodeHelper.replaceChild(this, newChild, oldChild);
    }

    public Node removeChild(Node oldChild) throws DOMException {
        return DOMNodeHelper.removeChild(this, oldChild);
    }

    public Node appendChild(Node newChild) throws DOMException {
        checkNewChildNode(newChild);

        return DOMNodeHelper.appendChild(this, newChild);
    }

    private void checkNewChildNode(Node newChild) throws DOMException {
        final int nodeType = newChild.getNodeType();

        if (!((nodeType == Node.ELEMENT_NODE)
                || (nodeType == Node.TEXT_NODE)
                || (nodeType == Node.COMMENT_NODE)
                || (nodeType == Node.PROCESSING_INSTRUCTION_NODE)
                || (nodeType == Node.CDATA_SECTION_NODE) || (nodeType == Node.ENTITY_REFERENCE_NODE))) {
            throw new DOMException(DOMException.HIERARCHY_REQUEST_ERR,
                    "Given node cannot be a child of an entity " + "reference");
        }
    }

    public boolean hasChildNodes() {
        return DOMNodeHelper.hasChildNodes(this);
    }

    public Node cloneNode(boolean deep) {
        return DOMNodeHelper.cloneNode(this, deep);
    }

    public void normalize() {
        DOMNodeHelper.normalize(this);
    }

    public boolean isSupported(String feature, String version) {
        return DOMNodeHelper.isSupported(this, feature, version);
    }

    public boolean hasAttributes() {
        return DOMNodeHelper.hasAttributes(this);
    }

    @Override
    public String getBaseURI() {

        return null;
    }

    @Override
    public short compareDocumentPosition(Node other) throws DOMException {

        return 0;
    }

    @Override
    public String getTextContent() throws DOMException {

        return null;
    }

    @Override
    public void setTextContent(String textContent) throws DOMException {

    }

    @Override
    public boolean isSameNode(Node other) {

        return false;
    }

    @Override
    public String lookupPrefix(String namespaceURI) {

        return null;
    }

    @Override
    public boolean isDefaultNamespace(String namespaceURI) {

        return false;
    }

    @Override
    public String lookupNamespaceURI(String prefix) {

        return null;
    }

    @Override
    public boolean isEqualNode(Node arg) {

        return false;
    }

    @Override
    public Object getFeature(String feature, String version) {

        return null;
    }

    @Override
    public Object setUserData(String key, Object data, UserDataHandler handler) {

        return null;
    }

    @Override
    public Object getUserData(String key) {

        return null;
    }
}
