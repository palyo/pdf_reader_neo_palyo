package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.record.common;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.record.RecordInputStream;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.LittleEndianOutput;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.StringUtil;

public final class FeatProtection implements SharedFeature {

    private final byte[] securityDescriptor;
    private int fSD;
    private int passwordVerifier;
    private String title;

    public FeatProtection() {
        securityDescriptor = new byte[0];
    }

    public FeatProtection(RecordInputStream in) {
        fSD = in.readInt();
        passwordVerifier = in.readInt();

        title = StringUtil.readUnicodeString(in);

        securityDescriptor = in.readRemainder();
    }

    public String toString() {
        String buffer = " [FEATURE PROTECTION]\n" +
                "   Self Relative = " + fSD +
                "   Password Verifier = " + passwordVerifier +
                "   Title = " + title +
                "   Security Descriptor Size = " + securityDescriptor.length +
                " [/FEATURE PROTECTION]\n";
        return buffer;
    }

    public void serialize(LittleEndianOutput out) {
        out.writeInt(fSD);
        out.writeInt(passwordVerifier);
        StringUtil.writeUnicodeString(out, title);
        out.write(securityDescriptor);
    }

    public int getDataSize() {
        return 4 + 4 + StringUtil.getEncodedSize(title) + securityDescriptor.length;
    }

    public int getPasswordVerifier() {
        return passwordVerifier;
    }

    public void setPasswordVerifier(int passwordVerifier) {
        this.passwordVerifier = passwordVerifier;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getFSD() {
        return fSD;
    }
}
