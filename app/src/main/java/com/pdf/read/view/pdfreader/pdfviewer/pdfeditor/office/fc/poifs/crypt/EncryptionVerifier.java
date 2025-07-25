package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.poifs.crypt;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.ByteArrayInputStream;

import javax.xml.parsers.DocumentBuilderFactory;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.EncryptedDocumentException;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.codec.Base64;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.poifs.filesystem.DocumentInputStream;

public class EncryptionVerifier {
    private final byte[] salt;
    private final byte[] verifier;
    private final byte[] verifierHash;
    private final byte[] encryptedKey;
    private final int verifierHashSize;
    private final int spinCount;
    private final int algorithm;
    private final int cipherMode;

    public EncryptionVerifier(String descriptor) {
        NamedNodeMap keyData = null;
        try {
            ByteArrayInputStream is;
            is = new ByteArrayInputStream(descriptor.getBytes());
            NodeList keyEncryptor = DocumentBuilderFactory.newInstance()
                    .newDocumentBuilder().parse(is)
                    .getElementsByTagName("keyEncryptor").item(0).getChildNodes();
            for (int i = 0; i < keyEncryptor.getLength(); i++) {
                Node node = keyEncryptor.item(i);
                if (node.getNodeName().equals("p:encryptedKey")) {
                    keyData = node.getAttributes();
                    break;
                }
            }
            if (keyData == null)
                throw new EncryptedDocumentException("Cannot process encrypted office files!");
        } catch (Exception e) {
            throw new EncryptedDocumentException("Unable to parse keyEncryptor");
        }

        spinCount = Integer.parseInt(keyData.getNamedItem("spinCount")
                .getNodeValue());
        verifier = Base64.decodeBase64(keyData
                .getNamedItem("encryptedVerifierHashInput")
                .getNodeValue().getBytes());
        salt = Base64.decodeBase64(keyData.getNamedItem("saltValue")
                .getNodeValue().getBytes());

        encryptedKey = Base64.decodeBase64(keyData
                .getNamedItem("encryptedKeyValue")
                .getNodeValue().getBytes());

        int saltSize = Integer.parseInt(keyData.getNamedItem("saltSize")
                .getNodeValue());
        if (saltSize != salt.length)
            throw new EncryptedDocumentException("Cannot process encrypted office files!");

        verifierHash = Base64.decodeBase64(keyData
                .getNamedItem("encryptedVerifierHashValue")
                .getNodeValue().getBytes());

        int blockSize = Integer.parseInt(keyData.getNamedItem("blockSize")
                .getNodeValue());

        String alg = keyData.getNamedItem("cipherAlgorithm").getNodeValue();

        if ("AES".equals(alg)) {
            if (blockSize == 16)
                algorithm = EncryptionHeader.ALGORITHM_AES_128;
            else if (blockSize == 24)
                algorithm = EncryptionHeader.ALGORITHM_AES_192;
            else if (blockSize == 32)
                algorithm = EncryptionHeader.ALGORITHM_AES_256;
            else
                throw new EncryptedDocumentException("Unsupported block size");
        } else {
            throw new EncryptedDocumentException("Unsupported cipher");
        }

        String chain = keyData.getNamedItem("cipherChaining").getNodeValue();
        if ("ChainingModeCBC".equals(chain))
            cipherMode = EncryptionHeader.MODE_CBC;
        else if ("ChainingModeCFB".equals(chain))
            cipherMode = EncryptionHeader.MODE_CFB;
        else
            throw new EncryptedDocumentException("Unsupported chaining mode");

        verifierHashSize = Integer.parseInt(keyData.getNamedItem("hashSize")
                .getNodeValue());
    }

    public EncryptionVerifier(DocumentInputStream is, int encryptedLength) {
        int saltSize = is.readInt();

        if (saltSize != 16) {
            throw new RuntimeException("Salt size != 16 !?");
        }

        salt = new byte[16];
        is.readFully(salt);
        verifier = new byte[16];
        is.readFully(verifier);

        verifierHashSize = is.readInt();

        verifierHash = new byte[encryptedLength];
        is.readFully(verifierHash);

        spinCount = 50000;
        algorithm = EncryptionHeader.ALGORITHM_AES_128;
        cipherMode = EncryptionHeader.MODE_ECB;
        encryptedKey = null;
    }

    public byte[] getSalt() {
        return salt;
    }

    public byte[] getVerifier() {
        return verifier;
    }

    public byte[] getVerifierHash() {
        return verifierHash;
    }

    public int getSpinCount() {
        return spinCount;
    }

    public int getCipherMode() {
        return cipherMode;
    }

    public int getAlgorithm() {
        return algorithm;
    }

    public byte[] getEncryptedKey() {
        return encryptedKey;
    }
}
