package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.record;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.IntMapper;

class SSTDeserializer {

    private final IntMapper<UnicodeString> strings;

    public SSTDeserializer(IntMapper<UnicodeString> strings) {
        this.strings = strings;
    }

    static public void addToStringTable(IntMapper<UnicodeString> strings, UnicodeString string) {
        strings.add(string);
    }

    public void manufactureStrings(int stringCount, RecordInputStream in) {
        for (int i = 0; i < stringCount; i++) {

            UnicodeString str;
            if (in.available() == 0 && !in.hasNextRecord()) {
                System.err.println("Ran out of data before creating all the strings! String at index " + i + "");
                str = new UnicodeString("");
            } else {
                str = new UnicodeString(in);
            }
            addToStringTable(strings, str);
        }
    }
}
