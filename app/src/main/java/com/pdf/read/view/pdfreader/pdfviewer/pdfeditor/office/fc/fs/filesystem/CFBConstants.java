package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.fs.filesystem;

public interface CFBConstants {
    int SMALLER_BIG_BLOCK_SIZE = 0x0200;
    BlockSize SMALLER_BIG_BLOCK_SIZE_DETAILS = new BlockSize(SMALLER_BIG_BLOCK_SIZE, (short) 9);
    int LARGER_BIG_BLOCK_SIZE = 0x1000;
    BlockSize LARGER_BIG_BLOCK_SIZE_DETAILS = new BlockSize(LARGER_BIG_BLOCK_SIZE, (short) 12);

    int SMALL_BLOCK_SIZE = 0x0040;

    int PROPERTY_SIZE = 0x0080;
    int BIG_BLOCK_MINIMUM_DOCUMENT_SIZE = 0x1000;

    int LARGEST_REGULAR_SECTOR_NUMBER = -5;

    int DIFAT_SECTOR_BLOCK = -4;
    int FAT_SECTOR_BLOCK = -3;
    int END_OF_CHAIN = -2;
    int UNUSED_BLOCK = -1;
} 
