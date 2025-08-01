package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hwpf.model;

import java.io.IOException;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hwpf.model.io.HWPFOutputStream;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.Internal;

@Internal
public class NotesTables {
    private final NoteType noteType;
    private PlexOfCps descriptors = new PlexOfCps(FootnoteReferenceDescriptor.getSize());
    private PlexOfCps textPositions = new PlexOfCps(0);

    public NotesTables(final NoteType noteType) {
        this.noteType = noteType;
        textPositions.addProperty(new GenericPropertyNode(0, 1, new byte[0]));
    }

    public NotesTables(final NoteType noteType, byte[] tableStream, FileInformationBlock fib) {
        this.noteType = noteType;
        read(tableStream, fib);
    }

    public GenericPropertyNode getDescriptor(int index) {
        return descriptors.getProperty(index);
    }

    public int getDescriptorsCount() {
        return descriptors.length();
    }

    public GenericPropertyNode getTextPosition(int index) {
        return textPositions.getProperty(index);
    }

    private void read(byte[] tableStream, FileInformationBlock fib) {
        int referencesStart = fib.getNotesDescriptorsOffset(noteType);
        int referencesLength = fib.getNotesDescriptorsSize(noteType);

        if (referencesStart != 0 && referencesLength != 0)
            this.descriptors = new PlexOfCps(tableStream, referencesStart, referencesLength,
                    FootnoteReferenceDescriptor.getSize());

        int textPositionsStart = fib.getNotesTextPositionsOffset(noteType);
        int textPositionsLength = fib.getNotesTextPositionsSize(noteType);

        if (textPositionsStart != 0 && textPositionsLength != 0)
            this.textPositions = new PlexOfCps(tableStream, textPositionsStart,
                    textPositionsLength, 0);
    }

    public void writeRef(FileInformationBlock fib, HWPFOutputStream tableStream) throws IOException {
        if (descriptors == null || descriptors.length() == 0) {
            fib.setNotesDescriptorsOffset(noteType, tableStream.getOffset());
            fib.setNotesDescriptorsSize(noteType, 0);
            return;
        }

        int start = tableStream.getOffset();
        tableStream.write(descriptors.toByteArray());
        int end = tableStream.getOffset();

        fib.setNotesDescriptorsOffset(noteType, start);
        fib.setNotesDescriptorsSize(noteType, end - start);
    }

    public void writeTxt(FileInformationBlock fib, HWPFOutputStream tableStream) throws IOException {
        if (textPositions == null || textPositions.length() == 0) {
            fib.setNotesTextPositionsOffset(noteType, tableStream.getOffset());
            fib.setNotesTextPositionsSize(noteType, 0);
            return;
        }

        int start = tableStream.getOffset();
        tableStream.write(textPositions.toByteArray());
        int end = tableStream.getOffset();

        fib.setNotesTextPositionsOffset(noteType, start);
        fib.setNotesTextPositionsSize(noteType, end - start);
    }
}
