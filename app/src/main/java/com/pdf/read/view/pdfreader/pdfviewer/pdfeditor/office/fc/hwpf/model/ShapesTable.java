package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hwpf.model;

import java.util.ArrayList;
import java.util.List;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hwpf.usermodel.Shape;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.Internal;

@Internal
@Deprecated
public final class ShapesTable {
    private final List<Shape> _shapes;
    private final List<Shape> _shapesVisibili;

    public ShapesTable(byte[] tblStream, FileInformationBlock fib) {
        PlexOfCps binTable = new PlexOfCps(tblStream, fib.getFcPlcspaMom(), fib.getLcbPlcspaMom(),
                26);

        _shapes = new ArrayList<Shape>();
        _shapesVisibili = new ArrayList<Shape>();

        for (int i = 0; i < binTable.length(); i++) {
            GenericPropertyNode nodo = binTable.getProperty(i);

            Shape sh = new Shape(nodo);
            _shapes.add(sh);
            if (sh.isWithinDocument())
                _shapesVisibili.add(sh);
        }
    }

    public List<Shape> getAllShapes() {
        return _shapes;
    }

    public List<Shape> getVisibleShapes() {
        return _shapesVisibili;
    }
}
