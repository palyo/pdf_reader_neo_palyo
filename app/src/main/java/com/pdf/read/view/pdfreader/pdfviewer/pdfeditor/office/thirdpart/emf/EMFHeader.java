package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.thirdpart.emf;

import java.io.IOException;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.java.awt.Dimension;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.java.awt.Rectangle;

public class EMFHeader implements EMFConstants {
    private static final Dimension screenMM = new Dimension(320, 240);

    private final Rectangle bounds;

    private final Rectangle frame;

    private final String signature;

    private final int versionMajor;

    private final int versionMinor;

    private final int bytes;

    private final int records;

    private final int handles;

    private final String description;

    private final int palEntries;

    private final Dimension device;

    private final Dimension millimeters;

    private Dimension micrometers;

    private boolean openGL;

    public EMFHeader(Rectangle bounds, int versionMajor, int versionMinor,
                     int bytes, int records, int handles, String application,
                     String name, Dimension device) {
        this.bounds = bounds;

        double pixelWidth = (double) screenMM.width / device.width;
        double pixelHeight = (double) screenMM.height / device.height;
        this.frame = new Rectangle((int) (bounds.x * 100 * pixelWidth),
                (int) (bounds.y * 100 * pixelHeight),
                (int) (bounds.width * 100 * pixelWidth),
                (int) (bounds.height * 100 * pixelHeight));

        this.signature = " EMF";
        this.versionMajor = versionMajor;
        this.versionMinor = versionMinor;
        this.bytes = bytes;
        this.records = records;
        this.handles = handles;
        this.description = application.trim() + "\0" + name.trim() + "\0\0";

        this.palEntries = 0;
        this.device = device;
        this.millimeters = screenMM;

        this.openGL = false;
        this.micrometers = new Dimension(screenMM.width * 1000,
                screenMM.height * 1000);
    }

    EMFHeader(EMFInputStream emf) throws IOException {

        emf.readUnsignedInt();

        int length = emf.readDWORD();

        bounds = emf.readRECTL();
        frame = emf.readRECTL();
        signature = new String(emf.readBYTE(4));

        int version = emf.readDWORD();
        versionMajor = version >> 16;
        versionMinor = version & 0xFFFF;
        bytes = emf.readDWORD();
        records = emf.readDWORD();
        handles = emf.readWORD();
        emf.readWORD();

        int dLen = emf.readDWORD();
        int dOffset = emf.readDWORD();
        palEntries = emf.readDWORD();
        device = emf.readSIZEL();
        millimeters = emf.readSIZEL();

        int bytesRead = 88;
        if (dOffset > 88) {
            emf.readDWORD();
            emf.readDWORD();
            openGL = emf.readDWORD() != 0;
            bytesRead += 12;
            if (dOffset > 100) {
                micrometers = emf.readSIZEL();
                bytesRead += 8;
            }
        }

        if (bytesRead < dOffset) {
            emf.skipBytes(dOffset - bytesRead);
            bytesRead = dOffset;
        }

        description = emf.readWCHAR(dLen);
        bytesRead += dLen * 2;

        if (bytesRead < length) {
            emf.skipBytes(length - bytesRead);
        }
    }

    public int size() {
        return 108 + (2 * description.length());
    }

    public String toString() {

        String s = "EMF Header\n" + "  bounds: " + bounds + "\n" +
                "  frame: " + frame + "\n" +
                "  signature: " + signature + "\n" +
                "  versionMajor: " + versionMajor + "\n" +
                "  versionMinor: " + versionMinor + "\n" +
                "  #bytes: " + bytes + "\n" +
                "  #records: " + records + "\n" +
                "  #handles: " + handles + "\n" +
                "  description: " + description + "\n" +
                "  #palEntries: " + palEntries + "\n" +
                "  device: " + device + "\n" +
                "  millimeters: " + millimeters + "\n" +
                "  openGL: " + openGL + "\n" +
                "  micrometers: " + micrometers;

        return s;
    }

    public Rectangle getBounds() {
        return bounds;
    }

    public Rectangle getFrame() {
        return frame;
    }

    public String getSignature() {
        return signature;
    }

    public String getDescription() {
        return description;
    }

    public Dimension getDevice() {
        return device;
    }

    public Dimension getMillimeters() {
        return millimeters;
    }

    public Dimension getMicrometers() {
        return micrometers;
    }

    public boolean isOpenGL() {
        return openGL;
    }
}
