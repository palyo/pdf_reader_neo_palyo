package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hslf.blip;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.InflaterInputStream;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.ShapeKit;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hslf.exceptions.HSLFException;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hslf.model.Picture;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.java.awt.Dimension;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.java.awt.Rectangle;

public final class EMF extends Metafile {

    public byte[] getData() {
        try {
            byte[] rawdata = getRawData();

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            InputStream is = new ByteArrayInputStream(rawdata);
            is.skip(8);

            Header header = new Header();
            header.read(rawdata, CHECKSUM_SIZE);
            is.skip(header.getSize() + CHECKSUM_SIZE);

            InflaterInputStream inflater = new InflaterInputStream(is);
            byte[] chunk = new byte[4096];
            int count;
            while ((count = inflater.read(chunk)) >= 0) {
                out.write(chunk, 0, count);
            }
            inflater.close();
            return out.toByteArray();
        } catch (IOException e) {
            throw new HSLFException(e);
        }
    }

    public void setData(byte[] data) throws IOException {
        byte[] compressed = compress(data, 0, data.length);

        Header header = new Header();
        header.wmfsize = data.length;
        header.bounds = new Rectangle(0, 0, 200, 200);
        header.size = new Dimension(header.bounds.width * ShapeKit.EMU_PER_POINT,
                header.bounds.height * ShapeKit.EMU_PER_POINT);
        header.zipsize = compressed.length;

        byte[] checksum = getChecksum(data);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        out.write(checksum);
        header.write(out);
        out.write(compressed);

        setRawData(out.toByteArray());
    }

    public int getType() {
        return Picture.EMF;
    }

    public int getSignature() {
        return 0x3D40;
    }
}
