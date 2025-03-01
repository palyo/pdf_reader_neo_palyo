package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.poifs.common;

public interface POIFSConstants {

    int SMALLER_BIG_BLOCK_SIZE = 0x0200;
    POIFSBigBlockSize SMALLER_BIG_BLOCK_SIZE_DETAILS =
            new POIFSBigBlockSize(SMALLER_BIG_BLOCK_SIZE, (short) 9);

    int LARGER_BIG_BLOCK_SIZE = 0x1000;
    POIFSBigBlockSize LARGER_BIG_BLOCK_SIZE_DETAILS =
            new POIFSBigBlockSize(LARGER_BIG_BLOCK_SIZE, (short) 12);

    int SMALL_BLOCK_SIZE = 0x0040;

    int PROPERTY_SIZE = 0x0080;

    int BIG_BLOCK_MINIMUM_DOCUMENT_SIZE = 0x1000;

    int LARGEST_REGULAR_SECTOR_NUMBER = -5;

    int DIFAT_SECTOR_BLOCK = -4;

    int FAT_SECTOR_BLOCK = -3;

    int END_OF_CHAIN = -2;

    int UNUSED_BLOCK = -1;

    byte[] OOXML_FILE_HEADER =
            new byte[]{0x50, 0x4b, 0x03, 0x04};
}   
