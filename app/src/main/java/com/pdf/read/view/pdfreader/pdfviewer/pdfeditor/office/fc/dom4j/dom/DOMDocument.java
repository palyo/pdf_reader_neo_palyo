package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.dom4j.dom;

import org.w3c.dom.Attr;
import org.w3c.dom.CDATASection;
import org.w3c.dom.DOMConfiguration;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.EntityReference;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.ProcessingInstruction;
import org.w3c.dom.UserDataHandler;

import java.util.ArrayList;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.dom4j.DocumentFactory;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.dom4j.QName;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.dom4j.tree.DefaultDocument;

public class DOMDocument extends DefaultDocument implements Document {
    private static final DOMDocumentFactory DOCUMENT_FACTORY = (DOMDocumentFactory) DOMDocumentFactory
            .getInstance();

    public DOMDocument() {
        init();
    }

    public DOMDocument(String name) {
        super(name);
        init();
    }

    public DOMDocument(DOMElement rootElement) {
        super(rootElement);
        init();
    }

    public DOMDocument(DOMDocumentType docType) {
        super(docType);
        init();
    }

    public DOMDocument(DOMElement rootElement, DOMDocumentType docType) {
        super(rootElement, docType);
        init();
    }

    public DOMDocument(String name, DOMElement rootElement, DOMDocumentType docType) {
        super(name, rootElement, docType);
        init();
    }

    private void init() {
        setDocumentFactory(DOCUMENT_FACTORY);
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
        return "#document";
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
        return DOMNodeHelper.createNodeList(content());
    }

    public Node getFirstChild() {
        return DOMNodeHelper.asDOMNode(node(0));
    }

    public Node getLastChild() {
        return DOMNodeHelper.asDOMNode(node(nodeCount() - 1));
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
        return null;
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
                || (nodeType == Node.COMMENT_NODE)
                || (nodeType == Node.PROCESSING_INSTRUCTION_NODE) || (nodeType == Node.DOCUMENT_TYPE_NODE))) {
            throw new DOMException(DOMException.HIERARCHY_REQUEST_ERR,
                    "Given node cannot be a child of document");
        }
    }

    public boolean hasChildNodes() {
        return nodeCount() > 0;
    }

    public Node cloneNode(boolean deep) {
        return DOMNodeHelper.cloneNode(this, deep);
    }

    public boolean isSupported(String feature, String version) {
        return DOMNodeHelper.isSupported(this, feature, version);
    }

    public boolean hasAttributes() {
        return DOMNodeHelper.hasAttributes(this);
    }

    public NodeList getElementsByTagName(String name) {
        ArrayList list = new ArrayList();
        DOMNodeHelper.appendElementsByTagName(list, this, name);

        return DOMNodeHelper.createNodeList(list);
    }

    public NodeList getElementsByTagNameNS(String namespace, String name) {
        ArrayList list = new ArrayList();
        DOMNodeHelper.appendElementsByTagNameNS(list, this, namespace, name);

        return DOMNodeHelper.createNodeList(list);
    }

    public org.w3c.dom.DocumentType getDoctype() {
        return DOMNodeHelper.asDOMDocumentType(getDocType());
    }

    public org.w3c.dom.DOMImplementation getImplementation() {
        if (getDocumentFactory() instanceof org.w3c.dom.DOMImplementation) {
            return (org.w3c.dom.DOMImplementation) getDocumentFactory();
        } else {
            return DOCUMENT_FACTORY;
        }
    }

    public org.w3c.dom.Element getDocumentElement() {
        return DOMNodeHelper.asDOMElement(getRootElement());
    }

    public org.w3c.dom.Element createElement(String name) throws DOMException {
        return (org.w3c.dom.Element) getDocumentFactory().createElement(name);
    }

    public org.w3c.dom.DocumentFragment createDocumentFragment() {
        DOMNodeHelper.notSupported();

        return null;
    }

    public org.w3c.dom.Text createTextNode(String data) {
        return (org.w3c.dom.Text) getDocumentFactory().createText(data);
    }

    public org.w3c.dom.Comment createComment(String data) {
        return (org.w3c.dom.Comment) getDocumentFactory().createComment(data);
    }

    public CDATASection createCDATASection(String data) throws DOMException {
        return (CDATASection) getDocumentFactory().createCDATA(data);
    }

    public ProcessingInstruction createProcessingInstruction(String target, String data)
            throws DOMException {
        return (ProcessingInstruction) getDocumentFactory()
                .createProcessingInstruction(target, data);
    }

    public Attr createAttribute(String name) throws DOMException {
        QName qname = getDocumentFactory().createQName(name);

        return (Attr) getDocumentFactory().createAttribute(null, qname, "");
    }

    public EntityReference createEntityReference(String name) throws DOMException {
        return (EntityReference) getDocumentFactory().createEntity(name, null);
    }

    public Node importNode(Node importedNode, boolean deep)
            throws DOMException {
        DOMNodeHelper.notSupported();

        return null;
    }

    public org.w3c.dom.Element createElementNS(String namespaceURI, String qualifiedName)
            throws DOMException {
        QName qname = getDocumentFactory().createQName(qualifiedName, namespaceURI);

        return (org.w3c.dom.Element) getDocumentFactory().createElement(qname);
    }

    public Attr createAttributeNS(String namespaceURI, String qualifiedName)
            throws DOMException {
        QName qname = getDocumentFactory().createQName(qualifiedName, namespaceURI);

        return (Attr) getDocumentFactory().createAttribute(null, qname, null);
    }

    public org.w3c.dom.Element getElementById(String elementId) {
        return DOMNodeHelper.asDOMElement(elementByID(elementId));
    }

    protected DocumentFactory getDocumentFactory() {
        if (super.getDocumentFactory() == null) {
            return DOCUMENT_FACTORY;
        } else {
            return super.getDocumentFactory();
        }
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

    @Override
    public String getInputEncoding() {

        return null;
    }

    @Override
    public String getXmlEncoding() {

        return null;
    }

    @Override
    public boolean getXmlStandalone() {

        return false;
    }

    @Override
    public void setXmlStandalone(boolean xmlStandalone) throws DOMException {

    }

    @Override
    public String getXmlVersion() {

        return null;
    }

    @Override
    public void setXmlVersion(String xmlVersion) throws DOMException {

    }

    @Override
    public boolean getStrictErrorChecking() {

        return false;
    }

    @Override
    public void setStrictErrorChecking(boolean strictErrorChecking) {

    }

    @Override
    public String getDocumentURI() {

        return null;
    }

    @Override
    public void setDocumentURI(String documentURI) {

    }

    @Override
    public Node adoptNode(Node source) throws DOMException {

        return null;
    }

    @Override
    public DOMConfiguration getDomConfig() {

        return null;
    }

    @Override
    public void normalizeDocument() {

    }

    @Override
    public Node renameNode(Node n, String namespaceURI, String qualifiedName) throws DOMException {

        return null;
    }
}
