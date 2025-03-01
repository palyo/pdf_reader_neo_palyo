package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hwpf.model;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hwpf.model.io.HWPFOutputStream;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.Internal;

@Internal
public final class SavedByTable {

    private final SavedByEntry[] entries;

    public SavedByTable(byte[] tableStream, int offset, int size) {

        String[] strings = SttbfUtils.read(tableStream, offset);

        int numEntries = strings.length / 2;
        entries = new SavedByEntry[numEntries];
        for (int i = 0; i < numEntries; i++) {
            entries[i] = new SavedByEntry(strings[i * 2], strings[i * 2 + 1]);
        }
    }

    public List<SavedByEntry> getEntries() {
        return Collections.unmodifiableList(Arrays.asList(entries));
    }

    public void writeTo(HWPFOutputStream tableStream) throws IOException {
        String[] toSave = new String[entries.length * 2];
        int counter = 0;
        for (SavedByEntry entry : entries) {
            toSave[counter++] = entry.getUserName();
            toSave[counter++] = entry.getSaveLocation();
        }
        SttbfUtils.write(tableStream, toSave);
    }

}
