package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.poifs.storage;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.poifs.common.POIFSConstants;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.LittleEndianConsts;

public interface HeaderBlockConstants {
    long _signature = 0xE11AB1A1E011CFD0L;
    int _bat_array_offset = 0x4c;
    int _max_bats_in_header =
            (POIFSConstants.SMALLER_BIG_BLOCK_SIZE - _bat_array_offset)
                    / LittleEndianConsts.INT_SIZE;

    int _signature_offset = 0;
    int _bat_count_offset = 0x2C;
    int _property_start_offset = 0x30;
    int _sbat_start_offset = 0x3C;
    int _sbat_block_count_offset = 0x40;
    int _xbat_start_offset = 0x44;
    int _xbat_count_offset = 0x48;
}   

