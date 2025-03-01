package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.record;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.record.common.FeatFormulaErr2;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.record.common.FeatProtection;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.record.common.FeatSmartTag;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.record.common.FtrHeader;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.record.common.SharedFeature;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.ss.util.HSSFCellRangeAddress;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.LittleEndianOutput;

public final class FeatRecord extends StandardRecord {
    public final static short sid = 0x0868;

    private final FtrHeader futureHeader;

    private int isf_sharedFeatureType;
    private byte reserved1;
    private long reserved2;

    private long cbFeatData;
    private int reserved3;
    private HSSFCellRangeAddress[] cellRefs;

    private SharedFeature sharedFeature;

    public FeatRecord() {
        futureHeader = new FtrHeader();
        futureHeader.setRecordType(sid);
    }

    public FeatRecord(RecordInputStream in) {
        futureHeader = new FtrHeader(in);

        isf_sharedFeatureType = in.readShort();
        reserved1 = in.readByte();
        reserved2 = in.readInt();
        int cref = in.readUShort();
        cbFeatData = in.readInt();
        reserved3 = in.readShort();

        cellRefs = new HSSFCellRangeAddress[cref];
        for (int i = 0; i < cellRefs.length; i++) {
            cellRefs[i] = new HSSFCellRangeAddress(in);
        }

        switch (isf_sharedFeatureType) {
            case FeatHdrRecord.SHAREDFEATURES_ISFPROTECTION:
                sharedFeature = new FeatProtection(in);
                break;
            case FeatHdrRecord.SHAREDFEATURES_ISFFEC2:
                sharedFeature = new FeatFormulaErr2(in);
                break;
            case FeatHdrRecord.SHAREDFEATURES_ISFFACTOID:
                sharedFeature = new FeatSmartTag(in);
                break;
            default:
                System.err.println("Unknown Shared Feature " + isf_sharedFeatureType + " found!");
        }
    }

    public short getSid() {
        return sid;
    }

    public String toString() {

        String buffer = "[SHARED FEATURE]\n" +

                "[/SHARED FEATURE]\n";
        return buffer;
    }

    public void serialize(LittleEndianOutput out) {
        futureHeader.serialize(out);

        out.writeShort(isf_sharedFeatureType);
        out.writeByte(reserved1);
        out.writeInt((int) reserved2);
        out.writeShort(cellRefs.length);
        out.writeInt((int) cbFeatData);
        out.writeShort(reserved3);

        for (int i = 0; i < cellRefs.length; i++) {
            cellRefs[i].serialize(out);
        }

        sharedFeature.serialize(out);
    }

    protected int getDataSize() {
        return 12 + 2 + 1 + 4 + 2 + 4 + 2 +
                (cellRefs.length * HSSFCellRangeAddress.ENCODED_SIZE)
                + sharedFeature.getDataSize();
    }

    public int getIsf_sharedFeatureType() {
        return isf_sharedFeatureType;
    }

    public long getCbFeatData() {
        return cbFeatData;
    }

    public void setCbFeatData(long cbFeatData) {
        this.cbFeatData = cbFeatData;
    }

    public HSSFCellRangeAddress[] getCellRefs() {
        return cellRefs;
    }

    public void setCellRefs(HSSFCellRangeAddress[] cellRefs) {
        this.cellRefs = cellRefs;
    }

    public SharedFeature getSharedFeature() {
        return sharedFeature;
    }

    public void setSharedFeature(SharedFeature feature) {
        this.sharedFeature = feature;

        if (feature instanceof FeatProtection) {
            isf_sharedFeatureType = FeatHdrRecord.SHAREDFEATURES_ISFPROTECTION;
        }
        if (feature instanceof FeatFormulaErr2) {
            isf_sharedFeatureType = FeatHdrRecord.SHAREDFEATURES_ISFFEC2;
        }
        if (feature instanceof FeatSmartTag) {
            isf_sharedFeatureType = FeatHdrRecord.SHAREDFEATURES_ISFFACTOID;
        }

        if (isf_sharedFeatureType == FeatHdrRecord.SHAREDFEATURES_ISFFEC2) {
            cbFeatData = sharedFeature.getDataSize();
        } else {
            cbFeatData = 0;
        }
    }

    public Object clone() {
        return cloneViaReserialise();
    }

}
