package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.xls.Reader.table;

import java.io.InputStream;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.dom4j.Document;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.dom4j.Element;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.dom4j.io.SAXReader;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.openxml4j.opc.PackagePart;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.ss.model.CellRangeAddress;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.ss.model.baseModel.Sheet;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.ss.model.table.SSTable;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.ss.util.ReferenceUtil;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.system.IControl;

public class TableReader {
    private static final TableReader reader = new TableReader();

    public static TableReader instance() {
        return reader;
    }

    public void read(IControl control, PackagePart tablePart, Sheet sheet) throws Exception {
        SAXReader saxreader = new SAXReader();
        try {
            InputStream in = tablePart.getInputStream();
            Document talbeDoc = saxreader.read(in);
            in.close();

            SSTable table = new SSTable();
            Element root = talbeDoc.getRootElement();

            String ref = root.attributeValue("ref");
            String[] rang = ref.split(":");
            if (rang != null && rang.length == 2) {
                table.setTableReference(
                        new CellRangeAddress(ReferenceUtil.instance().getRowIndex(rang[0]),
                                ReferenceUtil.instance().getColumnIndex(rang[0]),
                                ReferenceUtil.instance().getRowIndex(rang[1]),
                                ReferenceUtil.instance().getColumnIndex(rang[1])));
            }

            String str = root.attributeValue("totalsRowDxfId");
            if (str != null) {
                table.setTotalsRowDxfId(Integer.parseInt(str));
            }

            str = root.attributeValue("totalsRowBorderDxfId");
            if (str != null) {
                table.setTotalsRowBorderDxfId(Integer.parseInt(str));
            }

            str = root.attributeValue("headerRowDxfId");
            if (str != null) {
                table.setHeaderRowDxfId(Integer.parseInt(str));
            }

            str = root.attributeValue("headerRowBorderDxfId");
            if (str != null) {
                table.setHeaderRowBorderDxfId(Integer.parseInt(str));
            }

            str = root.attributeValue("tableBorderDxfId");
            if (str != null) {
                table.setTableBorderDxfId(Integer.parseInt(str));
            }

            String headerRowCount = root.attributeValue("headerRowCount");
            if ("0".equalsIgnoreCase(headerRowCount)) {
                table.setHeaderRowShown(false);
            }

            String totalsRowCount = root.attributeValue("totalsRowCount");
            if (totalsRowCount == null) {
                totalsRowCount = "0";
            }
            String totalsRowShown = root.attributeValue("totalsRowShown");
            if (!"0".equalsIgnoreCase(totalsRowShown)
                    && "1".equalsIgnoreCase(totalsRowCount)) {
                table.setTotalRowShown(true);
            }

            Element tableStyleInfo = root.element("tableStyleInfo");
            if (tableStyleInfo != null) {
                String name = tableStyleInfo.attributeValue("name");
                table.setName(name);

                String showFirstColumn = tableStyleInfo.attributeValue("showFirstColumn");
                if (!"0".equalsIgnoreCase(showFirstColumn)) {
                    table.setShowFirstColumn(true);
                }
                String showLastColumn = tableStyleInfo.attributeValue("showLastColumn");
                if (!"0".equalsIgnoreCase(showLastColumn)) {
                    table.setShowLastColumn(true);
                }
                String showRowStripes = tableStyleInfo.attributeValue("showRowStripes");
                if (!"0".equalsIgnoreCase(showRowStripes)) {
                    table.setShowRowStripes(true);
                }
                String showColumnStripes = tableStyleInfo.attributeValue("showColumnStripes");
                if (!"0".equalsIgnoreCase(showColumnStripes)) {
                    table.setShowColumnStripes(true);
                }

                sheet.addTable(table);
            }
        } catch (Exception e) {
            throw e;
        } finally {
            saxreader.resetHandlers();
        }
    }
}
