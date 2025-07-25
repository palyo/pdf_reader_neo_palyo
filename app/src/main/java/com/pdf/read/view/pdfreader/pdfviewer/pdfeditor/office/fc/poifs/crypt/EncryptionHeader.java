package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.poifs.crypt;

import org.w3c.dom.NamedNodeMap;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilderFactory;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.EncryptedDocumentException;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.codec.Base64;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.poifs.filesystem.DocumentInputStream;

public class EncryptionHeader {
    public static final int ALGORITHM_RC4 = 0x6801;
    public static final int ALGORITHM_AES_128 = 0x660E;
    public static final int ALGORITHM_AES_192 = 0x660F;
    public static final int ALGORITHM_AES_256 = 0x6610;

    public static final int HASH_SHA1 = 0x8004;

    public static final int PROVIDER_RC4 = 1;
    public static final int PROVIDER_AES = 0x18;

    public static final int MODE_ECB = 1;
    public static final int MODE_CBC = 2;
    public static final int MODE_CFB = 3;

    private final int flags;
    private final int sizeExtra;
    private final int algorithm;
    private final int hashAlgorithm;
    private final int keySize;
    private final int providerType;
    private final int cipherMode;
    private final byte[] keySalt;
    private final String cspName;

    public EncryptionHeader(DocumentInputStream is) throws IOException {
        flags = is.readInt();
        sizeExtra = is.readInt();
        algorithm = is.readInt();
        hashAlgorithm = is.readInt();
        keySize = is.readInt();
        providerType = is.readInt();

        is.readLong();

        StringBuilder builder = new StringBuilder();

        while (true) {
            char c = (char) is.readShort();

            if (c == 0) {
                break;
            }

            builder.append(c);
        }
        cspName = builder.toString();
        cipherMode = MODE_ECB;
        keySalt = null;
    }

    public EncryptionHeader(String descriptor) throws IOException {
        NamedNodeMap keyData;
        try {
            ByteArrayInputStream is;
            is = new ByteArrayInputStream(descriptor.getBytes());
            keyData = DocumentBuilderFactory.newInstance()
                    .newDocumentBuilder().parse(is)
                    .getElementsByTagName("keyData").item(0).getAttributes();
        } catch (Exception e) {
            throw new EncryptedDocumentException("Cannot process encrypted office files!");
        }

        keySize = Integer.parseInt(keyData.getNamedItem("keyBits")
                .getNodeValue());
        flags = 0;
        sizeExtra = 0;
        cspName = null;

        int blockSize = Integer.parseInt(keyData.getNamedItem("blockSize").
                getNodeValue());
        String cipher = keyData.getNamedItem("cipherAlgorithm").getNodeValue();

        if ("AES".equals(cipher)) {
            providerType = PROVIDER_AES;
            if (blockSize == 16)
                algorithm = ALGORITHM_AES_128;
            else if (blockSize == 24)
                algorithm = ALGORITHM_AES_192;
            else if (blockSize == 32)
                algorithm = ALGORITHM_AES_256;
            else
                throw new EncryptedDocumentException("Unsupported key length");
        } else {
            throw new EncryptedDocumentException("Unsupported cipher");
        }

        String chaining = keyData.getNamedItem("cipherChaining").getNodeValue();

        if ("ChainingModeCBC".equals(chaining))
            cipherMode = MODE_CBC;
        else if ("ChainingModeCFB".equals(chaining))
            cipherMode = MODE_CFB;
        else
            throw new EncryptedDocumentException("Unsupported chaining mode");

        String hashAlg = keyData.getNamedItem("hashAlgorithm").getNodeValue();
        int hashSize = Integer.parseInt(keyData.getNamedItem("hashSize")
                .getNodeValue());

        if ("SHA1".equals(hashAlg) && hashSize == 20)
            hashAlgorithm = HASH_SHA1;
        else
            throw new EncryptedDocumentException("Unsupported hash algorithm");

        String salt = keyData.getNamedItem("saltValue").getNodeValue();
        int saltLength = Integer.parseInt(keyData.getNamedItem("saltSize")
                .getNodeValue());
        keySalt = Base64.decodeBase64(salt.getBytes());
        if (keySalt.length != saltLength)
            throw new EncryptedDocumentException("Invalid salt length");
    }

    public int getCipherMode() {
        return cipherMode;
    }

    public int getFlags() {
        return flags;
    }

    public int getSizeExtra() {
        return sizeExtra;
    }

    public int getAlgorithm() {
        return algorithm;
    }

    public int getHashAlgorithm() {
        return hashAlgorithm;
    }

    public int getKeySize() {
        return keySize;
    }

    public byte[] getKeySalt() {
        return keySalt;
    }

    public int getProviderType() {
        return providerType;
    }

    public String getCspName() {
        return cspName;
    }
}
