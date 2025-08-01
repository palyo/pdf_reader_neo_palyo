package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hwpf.model;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.Internal;

@Internal
public final class FSPATable {

    private final Map<Integer, GenericPropertyNode> _byStart = new LinkedHashMap<Integer, GenericPropertyNode>();

    public FSPATable(byte[] tableStream, FileInformationBlock fib, FSPADocumentPart part) {
        int offset = fib.getFSPAPlcfOffset(part);
        int length = fib.getFSPAPlcfLength(part);

        PlexOfCps plex = new PlexOfCps(tableStream, offset, length, FSPA.getSize());
        for (int i = 0; i < plex.length(); i++) {
            GenericPropertyNode property = plex.getProperty(i);
            _byStart.put(Integer.valueOf(property.getStart()), property);
        }
    }

    @Deprecated
    public FSPATable(byte[] tableStream, int fcPlcspa, int lcbPlcspa, List<TextPiece> tpt) {
        if (fcPlcspa == 0)
            return;

        PlexOfCps plex = new PlexOfCps(tableStream, fcPlcspa, lcbPlcspa, FSPA.FSPA_SIZE);
        for (int i = 0; i < plex.length(); i++) {
            GenericPropertyNode property = plex.getProperty(i);
            _byStart.put(Integer.valueOf(property.getStart()), property);
        }
    }

    public FSPA getFspaFromCp(int cp) {
        GenericPropertyNode propertyNode = _byStart.get(Integer.valueOf(cp));
        if (propertyNode == null) {
            return null;
        }
        return new FSPA(propertyNode.getBytes(), 0);
    }

    public FSPA[] getShapes() {
        List<FSPA> result = new ArrayList<FSPA>(_byStart.size());
        for (GenericPropertyNode propertyNode : _byStart.values()) {
            result.add(new FSPA(propertyNode.getBytes(), 0));
        }
        return result.toArray(new FSPA[result.size()]);
    }

    public String toString() {
        StringBuffer buf = new StringBuffer();
        buf.append("[FPSA PLC size=").append(_byStart.size()).append("]\n");

        for (Map.Entry<Integer, GenericPropertyNode> entry : _byStart.entrySet()) {
            Integer i = entry.getKey();
            buf.append("  ").append(i.toString()).append(" => \t");

            try {
                FSPA fspa = getFspaFromCp(i.intValue());
                buf.append(fspa.toString());
            } catch (Exception exc) {
                buf.append(exc.getMessage());
            }
            buf.append("\n");
        }
        buf.append("[/FSPA PLC]");
        return buf.toString();
    }
}
