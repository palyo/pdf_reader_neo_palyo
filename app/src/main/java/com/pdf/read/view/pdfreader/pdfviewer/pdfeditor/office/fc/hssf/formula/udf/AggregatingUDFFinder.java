package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.formula.udf;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.formula.function.FreeRefFunction;

public class AggregatingUDFFinder implements UDFFinder {

    private final Collection<UDFFinder> _usedToolPacks;

    public AggregatingUDFFinder(UDFFinder... usedToolPacks) {
        _usedToolPacks = new ArrayList<UDFFinder>(usedToolPacks.length);
        _usedToolPacks.addAll(Arrays.asList(usedToolPacks));
    }

    public FreeRefFunction findFunction(String name) {
        FreeRefFunction evaluatorForFunction;
        for (UDFFinder pack : _usedToolPacks) {
            evaluatorForFunction = pack.findFunction(name);
            if (evaluatorForFunction != null) {
                return evaluatorForFunction;
            }
        }
        return null;
    }

    public void add(UDFFinder toolPack) {
        _usedToolPacks.add(toolPack);
    }
}
