package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hwpf.sprm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hwpf.usermodel.BorderCode;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hwpf.usermodel.TableAutoformatLookSpecifier;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hwpf.usermodel.TableCellDescriptor;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hwpf.usermodel.TableProperties;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.Internal;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.LittleEndian;

@Internal
public final class TableSprmCompressor {
    public TableSprmCompressor() {
    }

    public static byte[] compressTableProperty(TableProperties newTAP) {
        int size = 0;
        List<byte[]> sprmList = new ArrayList<byte[]>();

        if (newTAP.getJc() != 0) {
            size += SprmUtils.addSprm((short) 0x5400, newTAP.getJc(), null, sprmList);
        }
        if (newTAP.getFCantSplit()) {
            size += SprmUtils.addSprm((short) 0x3403, 1, null, sprmList);
        }
        if (newTAP.getFTableHeader()) {
            size += SprmUtils.addSprm((short) 0x3404, 1, null, sprmList);
        }
        byte[] brcBuf = new byte[6 * BorderCode.SIZE];
        int offset = 0;
        newTAP.getBrcTop().serialize(brcBuf, offset);
        offset += BorderCode.SIZE;
        newTAP.getBrcLeft().serialize(brcBuf, offset);
        offset += BorderCode.SIZE;
        newTAP.getBrcBottom().serialize(brcBuf, offset);
        offset += BorderCode.SIZE;
        newTAP.getBrcRight().serialize(brcBuf, offset);
        offset += BorderCode.SIZE;
        newTAP.getBrcHorizontal().serialize(brcBuf, offset);
        offset += BorderCode.SIZE;
        newTAP.getBrcVertical().serialize(brcBuf, offset);
        byte[] compare = new byte[6 * BorderCode.SIZE];
        if (!Arrays.equals(brcBuf, compare)) {
            size += SprmUtils.addSprm((short) 0xD605, 0, brcBuf, sprmList);
        }
        if (newTAP.getDyaRowHeight() != 0) {
            size += SprmUtils.addSprm((short) 0x9407, newTAP.getDyaRowHeight(), null, sprmList);
        }
        if (newTAP.getItcMac() > 0) {
            int itcMac = newTAP.getItcMac();
            byte[] buf = new byte[1 + (LittleEndian.SHORT_SIZE * (itcMac + 1))
                    + (TableCellDescriptor.SIZE * itcMac)];
            buf[0] = (byte) itcMac;

            short[] dxaCenters = newTAP.getRgdxaCenter();
            for (int x = 0; x < dxaCenters.length; x++) {
                LittleEndian.putShort(buf, 1 + (x * LittleEndian.SHORT_SIZE), dxaCenters[x]);
            }

            TableCellDescriptor[] cellDescriptors = newTAP.getRgtc();
            for (int x = 0; x < cellDescriptors.length; x++) {
                cellDescriptors[x].serialize(buf, 1 + ((itcMac + 1) * LittleEndian.SHORT_SIZE)
                        + (x * TableCellDescriptor.SIZE));
            }
            size += SprmUtils.addSpecialSprm((short) 0xD608, buf, sprmList);

        }

        if (newTAP.getTlp() != null && !newTAP.getTlp().isEmpty()) {
            byte[] buf = new byte[TableAutoformatLookSpecifier.SIZE];
            newTAP.getTlp().serialize(buf, 0);
            size += SprmUtils.addSprm((short) 0x740a, 0, buf, sprmList);
        }

        return SprmUtils.getGrpprl(sprmList, size);
    }
}
