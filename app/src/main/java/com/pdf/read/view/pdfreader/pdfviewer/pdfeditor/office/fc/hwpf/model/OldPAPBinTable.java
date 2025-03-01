package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hwpf.model;

import java.util.Collections;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.poifs.common.POIFSConstants;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.Internal;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.LittleEndian;

@Internal
public final class OldPAPBinTable extends PAPBinTable {

    public OldPAPBinTable(byte[] documentStream, int offset, int size, int fcMin, TextPieceTable tpt) {
        PlexOfCps binTable = new PlexOfCps(documentStream, offset, size, 2);

        int length = binTable.length();
        for (int x = 0; x < length; x++) {
            GenericPropertyNode node = binTable.getProperty(x);

            int pageNum = LittleEndian.getShort(node.getBytes());
            int pageOffset = POIFSConstants.SMALLER_BIG_BLOCK_SIZE * pageNum;

            PAPFormattedDiskPage pfkp = new PAPFormattedDiskPage(documentStream, documentStream,
                    pageOffset, tpt);

            int fkpSize = pfkp.size();

            for (int y = 0; y < fkpSize; y++) {
                PAPX papx = pfkp.getPAPX(y);
                if (papx != null) {
                    _paragraphs.add(papx);
                }
            }
        }
        Collections.sort(_paragraphs, PropertyNode.StartComparator.instance);
    }
}
