package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hslf.record;

import java.io.IOException;
import java.io.OutputStream;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.LittleEndian;

public final class NotesAtom extends RecordAtom {
    private static final long _type = 1009L;
    private byte[] _header;
    private int slideID;
    private boolean followMasterObjects;
    private boolean followMasterScheme;
    private boolean followMasterBackground;
    private byte[] reserved;

    private NotesAtom(byte[] source, int start, int len) {

        if (len < 8) {
            len = 8;
        }

        _header = new byte[8];
        System.arraycopy(source, start, _header, 0, 8);

        slideID = LittleEndian.getInt(source, start + 8);

        int flags = LittleEndian.getUShort(source, start + 12);
        followMasterBackground = (flags & 4) == 4;
        followMasterScheme = (flags & 2) == 2;
        followMasterObjects = (flags & 1) == 1;

        reserved = new byte[len - 14];
        System.arraycopy(source, start + 14, reserved, 0, reserved.length);
    }

    public int getSlideID() {
        return slideID;
    }

    public void setSlideID(int id) {
        slideID = id;
    }

    public boolean getFollowMasterObjects() {
        return followMasterObjects;
    }

    public void setFollowMasterObjects(boolean flag) {
        followMasterObjects = flag;
    }

    public boolean getFollowMasterScheme() {
        return followMasterScheme;
    }

    public void setFollowMasterScheme(boolean flag) {
        followMasterScheme = flag;
    }

    public boolean getFollowMasterBackground() {
        return followMasterBackground;
    }

    public void setFollowMasterBackground(boolean flag) {
        followMasterBackground = flag;
    }

    public long getRecordType() {
        return _type;
    }

    public void writeOut(OutputStream out) throws IOException {

        out.write(_header);

        writeLittleEndian(slideID, out);

        short flags = 0;
        if (followMasterObjects) {
            flags += 1;
        }
        if (followMasterScheme) {
            flags += 2;
        }
        if (followMasterBackground) {
            flags += 4;
        }
        writeLittleEndian(flags, out);

        out.write(reserved);
    }

    public void dispose() {
        _header = null;
        reserved = null;
    }
}
