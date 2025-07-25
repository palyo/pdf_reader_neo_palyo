package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.ppt.reader;

import java.util.List;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.ext.KeyKt;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.common.bg.BackgroundAndFill;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.common.borders.Line;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.common.shape.TableCell;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.common.shape.TableShape;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.common.shape.TextBox;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.constant.wp.WPAttrConstant;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.constant.wp.WPModelConstant;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.ShapeKit;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.dom4j.Element;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.openxml4j.opc.PackagePart;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.openxml4j.opc.ZipPackage;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.ppt.attribute.ParaAttr;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.ppt.attribute.RunAttr;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.ppt.attribute.SectionAttr;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.java.awt.Rectangle;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.java.awt.Rectanglef;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.pg.model.PGMaster;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.pg.model.PGModel;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.pg.model.tableStyle.TableCellBorders;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.pg.model.tableStyle.TableCellStyle;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.pg.model.tableStyle.TableStyle;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.simpletext.model.AttrManage;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.simpletext.model.IAttributeSet;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.simpletext.model.ParagraphElement;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.simpletext.model.SectionElement;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.system.IControl;

public class TableReader {
    public static final int DEFAULT_CELL_WIDTH = 100;
    public static final int DEFAULT_CELL_HEIGHT = 40;

    private static final TableReader kit = new TableReader();

    public static TableReader instance() {
        return kit;
    }

    public TableShape getTable(IControl control, ZipPackage zipPackage, PackagePart packagePart, PGModel pgModel,
                               PGMaster master, Element tbl, Rectangle rect) throws Exception {
        RunAttr.instance().setTable(true);
        TableShape table = null;

        Element tblGrid = tbl.element("tblGrid");
        if (tblGrid != null) {

            int t = 0;
            List<Element> gridCols = tblGrid.elements("gridCol");
            int[] colWidths = new int[gridCols.size()];
            for (Element gridCol : gridCols) {
                int colWidth = (int) (Integer.parseInt(gridCol.attributeValue("w"))
                        * KeyKt.PIXEL_DPI / KeyKt.EMU_PER_INCH);
                if (colWidth > 0) {
                    colWidths[t++] = colWidth;
                } else {
                    colWidths[t++] = (int) (DEFAULT_CELL_WIDTH * KeyKt.POINT_TO_PIXEL);
                }
            }

            t = 0;
            List<Element> trs = tbl.elements("tr");
            int[] rowHeights = new int[trs.size()];
            for (Element tr : trs) {
                int rowHeight = (int) (Integer.parseInt(tr.attributeValue("h"))
                        * KeyKt.PIXEL_DPI / KeyKt.EMU_PER_INCH);
                if (rowHeight > 0) {
                    rowHeights[t++] = rowHeight;
                } else {
                    rowHeights[t++] = (int) (DEFAULT_CELL_HEIGHT * KeyKt.POINT_TO_PIXEL);
                }
            }

            table = new TableShape(rowHeights.length, colWidths.length);

            Element tblPr = tbl.element("tblPr");
            Element tableStyleId = tblPr.element("tableStyleId");

            TableStyle tableStyle = null;
            if (tableStyleId != null) {
                tableStyle = pgModel.getTableStyle(tableStyleId.getText());

                table.setFirstRow("1".equalsIgnoreCase(tblPr.attributeValue("firstRow")));
                table.setLastRow("1".equalsIgnoreCase(tblPr.attributeValue("lastRow")));

                table.setFirstCol("1".equalsIgnoreCase(tblPr.attributeValue("firstCol")));
                table.setLastCol("1".equalsIgnoreCase(tblPr.attributeValue("lastCol")));

                table.setBandRow("1".equalsIgnoreCase(tblPr.attributeValue("bandRow")));
                table.setBandCol("1".equalsIgnoreCase(tblPr.attributeValue("bandCol")));
            }

            processTable(control, zipPackage, packagePart, master,
                    trs, rect, table, colWidths, rowHeights, tableStyle);
        }
        RunAttr.instance().setTable(false);
        return table;
    }

    private Line processLine(IControl control, ZipPackage zipPackage, PackagePart packagePart, PGMaster master, TableStyle tableStyle, Element ln, int styleColor) {
        try {
            BackgroundAndFill lineFill = null;
            int lineWidth = 1;
            boolean dash = false;
            if (ln != null && ln.element("noFill") == null) {

                if (ln.attributeValue("w") != null) {
                    lineWidth = Math.round(Integer.parseInt(ln.attributeValue("w")) * KeyKt.PIXEL_DPI / KeyKt.EMU_PER_INCH);
                }

                Element prstDash = ln.element("prstDash");
                if (prstDash != null && !"solid".equalsIgnoreCase(prstDash.attributeValue("val"))) {
                    dash = true;
                }

                lineFill = BackgroundReader.instance().processBackground(control, zipPackage, packagePart, master, ln);
            } else {
                lineFill = new BackgroundAndFill();
                lineFill.setForegroundColor(styleColor);
            }

            Line line = new Line();
            line.setBackgroundAndFill(lineFill);
            line.setLineWidth(lineWidth);
            line.setDash(dash);

            return line;
        } catch (Exception e) {
            return null;
        }
    }

    private void processTable(IControl control, ZipPackage zipPackage, PackagePart packagePart, PGMaster master,
                              List<Element> trs, Rectangle rect, TableShape table,
                              int[] colWidths, int[] rowHeights, TableStyle tableStyle) throws Exception {

        int i = 0;
        for (Element tr : trs) {
            int j = 0;
            List<Element> tcs = tr.elements("tc");
            for (Element tc : tcs) {
                if (tc.attribute("hMerge") == null && tc.attribute("vMerge") == null) {
                    TableCell cell = new TableCell();

                    Rectanglef anchor = new Rectanglef(rect.x, rect.y, 0, 0);
                    for (int t = 0; t < j; t++) {
                        anchor.setX(anchor.getX() + colWidths[t]);
                    }

                    for (int t = 0; t < i; t++) {
                        anchor.setY(anchor.getY() + rowHeights[t]);
                    }

                    int w = colWidths[j];
                    int h = rowHeights[i];
                    if (tc.attribute("rowSpan") != null) {
                        int rowSpan = Integer.parseInt(tc.attributeValue("rowSpan"));
                        for (int t = 1; t < rowSpan; t++) {
                            h += rowHeights[i + t];
                        }
                    }
                    if (tc.attribute("gridSpan") != null) {
                        int gridSpan = Integer.parseInt(tc.attributeValue("gridSpan"));
                        for (int t = 1; t < gridSpan; t++) {
                            w += colWidths[j + t];
                        }
                    }
                    anchor.setWidth(w);
                    anchor.setHeight(h);
                    cell.setBounds(anchor);

                    TableCellStyle cellStyle = getTableCellBorders(tableStyle, i, j, table);

                    Element tcPr = tc.element("tcPr");
                    if (tcPr != null) {
                        Element temp = tcPr.element("lnL");
                        cell.setLeftLine(
                                processLine(control, zipPackage, packagePart, master, tableStyle, temp,
                                        getTableCellLeftBorderColor(zipPackage, packagePart, master, tableStyle, cellStyle)));

                        temp = tcPr.element("lnR");
                        cell.setRightLine(
                                processLine(control, zipPackage, packagePart, master, tableStyle, temp,
                                        getTableCellRightBorderColor(zipPackage, packagePart, master, tableStyle, cellStyle)));

                        temp = tcPr.element("lnT");
                        cell.setTopLine(
                                processLine(control, zipPackage, packagePart, master, tableStyle, temp,
                                        getTableCellTopBorderColor(zipPackage, packagePart, master, tableStyle, cellStyle)));

                        temp = tcPr.element("lnB");
                        cell.setBottomLine(
                                processLine(control, zipPackage, packagePart, master, tableStyle, temp,
                                        getTableCellBottomBorderColor(zipPackage, packagePart, master, tableStyle, cellStyle)));
                    } else if (cellStyle != null) {
                        cell.setLeftLine(
                                processLine(control, zipPackage, packagePart, master, tableStyle, null,
                                        getTableCellLeftBorderColor(zipPackage, packagePart, master, tableStyle, cellStyle)));

                        cell.setRightLine(
                                processLine(control, zipPackage, packagePart, master, tableStyle, null,
                                        getTableCellRightBorderColor(zipPackage, packagePart, master, tableStyle, cellStyle)));

                        cell.setTopLine(
                                processLine(control, zipPackage, packagePart, master, tableStyle, null,
                                        getTableCellTopBorderColor(zipPackage, packagePart, master, tableStyle, cellStyle)));

                        cell.setBottomLine(
                                processLine(control, zipPackage, packagePart, master, tableStyle, null,
                                        getTableCellBottomBorderColor(zipPackage, packagePart, master, tableStyle, cellStyle)));
                    } else {
                        Line line = processLine(control, zipPackage, packagePart, master, tableStyle, null, 0xFF000000);
                        cell.setLeftLine(line);
                        cell.setRightLine(line);
                        cell.setTopLine(line);
                        cell.setBottomLine(line);
                    }

                    BackgroundAndFill fill = BackgroundReader.instance().processBackground(control, zipPackage, packagePart, master, tcPr);
                    if (fill == null && cellStyle != null) {
                        fill = getTableCellFill(control, zipPackage, packagePart, master, tableStyle, cellStyle);
                    }
                    cell.setBackgroundAndFill(fill);

                    TextBox textBox = new TextBox();
                    Rectangle r = new Rectangle((int) anchor.getX(), (int) anchor.getY(), (int) anchor.getWidth(), (int) anchor.getHeight());
                    textBox.setBounds(r);
                    if (tableStyle != null && (cellStyle == null || cellStyle.getFontAttributeSet() == null)) {
                        cellStyle = tableStyle.getWholeTable();
                    }
                    processCellSection(control, master, textBox, r, tc, cellStyle);
                    cell.setText(textBox);

                    table.addCell(i * colWidths.length + j, cell);
                }
                j++;
            }
            i++;
        }
    }

    private BackgroundAndFill getTableCellFill(IControl control, ZipPackage zipPackage, PackagePart packagePart, PGMaster master,
                                               TableStyle tableStyle, TableCellStyle cellStyle) {
        try {
            Element fill = cellStyle.getTableCellBgFill();
            if (fill == null) {
                fill = tableStyle.getWholeTable().getTableCellBgFill();
            }

            return BackgroundReader.instance().processBackground(control, zipPackage, packagePart, master, fill, true);
        } catch (Exception e) {
            return null;
        }
    }

    private int getTableCellLeftBorderColor(ZipPackage zipPackage, PackagePart packagePart, PGMaster master,
                                            TableStyle tableStyle, TableCellStyle cellStyle) {
        try {
            Element left = null;
            TableCellBorders borders = cellStyle.getTableCellBorders();
            if (borders == null) {
                borders = tableStyle.getWholeTable().getTableCellBorders();
                left = borders.getLeftBorder();
            } else {
                left = borders.getLeftBorder();
                if (left == null) {
                    left = tableStyle.getWholeTable().getTableCellBorders().getLeftBorder();
                }
            }

            return BackgroundReader.instance().getBackgroundColor(zipPackage, packagePart, master, left, true);
        } catch (Exception e) {
            return 0xFF000000;
        }
    }

    private int getTableCellRightBorderColor(ZipPackage zipPackage, PackagePart packagePart, PGMaster master,
                                             TableStyle tableStyle, TableCellStyle cellStyle) {
        try {
            Element right = null;
            TableCellBorders borders = cellStyle.getTableCellBorders();
            if (borders == null) {
                borders = tableStyle.getWholeTable().getTableCellBorders();
                right = borders.getRightBorder();
            } else {
                right = borders.getRightBorder();
                if (right == null) {
                    right = tableStyle.getWholeTable().getTableCellBorders().getRightBorder();
                }
            }

            return BackgroundReader.instance().getBackgroundColor(zipPackage, packagePart, master, right, true);
        } catch (Exception e) {
            return 0xFF000000;
        }
    }

    private int getTableCellTopBorderColor(ZipPackage zipPackage, PackagePart packagePart, PGMaster master,
                                           TableStyle tableStyle, TableCellStyle cellStyle) {
        try {
            Element top = null;
            TableCellBorders borders = cellStyle.getTableCellBorders();
            if (borders == null) {
                borders = tableStyle.getWholeTable().getTableCellBorders();
                top = borders.getTopBorder();
            } else {
                top = borders.getTopBorder();
                if (top == null) {
                    top = tableStyle.getWholeTable().getTableCellBorders().getTopBorder();
                }
            }

            return BackgroundReader.instance().getBackgroundColor(zipPackage, packagePart, master, top, true);
        } catch (Exception e) {
            return 0xFF000000;
        }
    }

    private int getTableCellBottomBorderColor(ZipPackage zipPackage, PackagePart packagePart, PGMaster master,
                                              TableStyle tableStyle, TableCellStyle cellStyle) {
        try {
            Element bottom = null;
            TableCellBorders borders = cellStyle.getTableCellBorders();
            if (borders == null) {
                borders = tableStyle.getWholeTable().getTableCellBorders();
                bottom = borders.getBottomBorder();
            } else {
                bottom = borders.getBottomBorder();
                if (bottom == null) {
                    bottom = tableStyle.getWholeTable().getTableCellBorders().getBottomBorder();
                }
            }

            return BackgroundReader.instance().getBackgroundColor(zipPackage, packagePart, master, bottom, true);
        } catch (Exception e) {
            return 0xFF000000;
        }
    }

    private TableCellStyle getTableCellBorders(TableStyle tableStyle, int row, int col, TableShape table) {
        TableCellStyle cellStyle = null;

        if (tableStyle == null) {
            return null;
        }

        if (table.isFirstRow() && table.isFirstCol()) {
            cellStyle = getTableCellBorders_FirstRowFirstColumn(tableStyle, row, col, table);
        } else if (table.isFirstRow() && !table.isFirstCol()) {
            cellStyle = getTableCellBorders_FirstRow(tableStyle, row, col, table);
        } else if (!table.isFirstRow() && table.isFirstCol()) {
            cellStyle = getTableCellBorders_FirstColumn(tableStyle, row, col, table);
        } else if (!table.isFirstRow() && !table.isFirstCol()) {
            cellStyle = getTableCellBorders_NotFirstRowFirstColumn(tableStyle, row, col, table);
        }

        return cellStyle;
    }

    private TableCellStyle getTableCellBorders_FirstRowFirstColumn(TableStyle tableStyle, int row, int col, TableShape table) {
        TableCellStyle cellStyle = null;
        if (row == 0) {
            cellStyle = tableStyle.getFirstRow();
        } else if (table.isLastRow() && row == table.getRowCount() - 1) {
            cellStyle = tableStyle.getLastRow();
        } else if (col == 0) {
            cellStyle = tableStyle.getFirstCol();
        } else if (table.isLastCol() && col == table.getColumnCount() - 1) {
            cellStyle = tableStyle.getLastCol();
        } else if (table.isBandRow()) {
            if (row % 2 != 0) {
                cellStyle = tableStyle.getBand1H();
            } else if (table.isBandCol() && col % 2 != 0) {
                cellStyle = tableStyle.getBand1V();
            }
        } else if (table.isBandCol() && col % 2 != 0) {
            cellStyle = tableStyle.getBand1V();
        }

        if (cellStyle == null) {
            cellStyle = tableStyle.getWholeTable();
        }

        return cellStyle;
    }

    private TableCellStyle getTableCellBorders_FirstRow(TableStyle tableStyle, int row, int col, TableShape table) {
        TableCellStyle cellStyle = null;
        if (row == 0) {
            cellStyle = tableStyle.getFirstRow();
        } else if (table.isLastRow() && row == table.getRowCount() - 1) {
            cellStyle = tableStyle.getLastRow();
        } else if (table.isLastCol() && col == table.getColumnCount() - 1) {
            cellStyle = tableStyle.getLastCol();
        } else if (table.isBandRow()) {
            if (row % 2 != 0) {
                cellStyle = tableStyle.getBand1H();
            } else if (table.isBandCol() && col % 2 == 0) {
                cellStyle = tableStyle.getBand1V();
            }
        } else if (table.isBandCol() && col % 2 == 0) {
            cellStyle = tableStyle.getBand1V();
        }

        if (cellStyle == null) {
            cellStyle = tableStyle.getWholeTable();
        }

        return cellStyle;
    }

    private TableCellStyle getTableCellBorders_FirstColumn(TableStyle tableStyle, int row, int col, TableShape table) {
        TableCellStyle cellStyle = null;
        if (table.isLastRow() && row == table.getRowCount() - 1) {
            cellStyle = tableStyle.getLastRow();
        } else if (col == 0) {
            cellStyle = tableStyle.getFirstCol();
        } else if (table.isLastCol() && col == table.getColumnCount() - 1) {
            cellStyle = tableStyle.getLastCol();
        } else if (table.isBandRow()) {
            if (row % 2 == 0) {
                cellStyle = tableStyle.getBand1H();
            } else if (table.isBandCol() && col % 2 != 0) {
                cellStyle = tableStyle.getBand1V();
            }
        } else if (table.isBandCol() && col % 2 != 0) {
            cellStyle = tableStyle.getBand1V();
        }

        if (cellStyle == null) {
            cellStyle = tableStyle.getWholeTable();
        }

        return cellStyle;
    }

    private TableCellStyle getTableCellBorders_NotFirstRowFirstColumn(TableStyle tableStyle, int row, int col, TableShape table) {
        TableCellStyle cellStyle = null;
        if (table.isLastRow() && row == table.getRowCount() - 1) {
            cellStyle = tableStyle.getLastRow();
        } else if (table.isLastCol() && col == table.getColumnCount() - 1) {
            cellStyle = tableStyle.getLastCol();
        } else if (table.isBandRow()) {
            if (row % 2 == 0) {
                cellStyle = tableStyle.getBand1H();
            } else if (table.isBandCol() && col % 2 == 0) {
                cellStyle = tableStyle.getBand1V();
            }
        } else if (table.isBandCol() && col % 2 == 0) {
            cellStyle = tableStyle.getBand1V();
        }

        if (cellStyle == null) {
            cellStyle = tableStyle.getWholeTable();
        }

        return cellStyle;
    }

    public void processCellSection(IControl control, PGMaster master, TextBox tb, Rectangle rect, Element tc, TableCellStyle cellStyle) {

        SectionElement secElem = new SectionElement();

        secElem.setStartOffset(0);
        tb.setElement(secElem);

        IAttributeSet attr = secElem.getAttribute();

        AttrManage.instance().setPageWidth(attr, (int) (rect.width * KeyKt.PIXEL_TO_TWIPS));

        AttrManage.instance().setPageHeight(attr, (int) (rect.height * KeyKt.PIXEL_TO_TWIPS));

        int leftMargin = ShapeKit.DefaultMargin_Twip * 2;
        int topMargin = ShapeKit.DefaultMargin_Twip;
        int rightMargin = ShapeKit.DefaultMargin_Twip * 2;
        int bottomMargin = ShapeKit.DefaultMargin_Twip;

        Element temp = tc.element("txBody");
        if (temp != null) {
            SectionAttr.instance().setSectionAttribute(temp.element("bodyPr"), attr, null, null, true);

            String val = "";
            byte verAlign = WPAttrConstant.PAGE_V_TOP;
            byte horAlign = WPAttrConstant.PAGE_H_LEFT;

            Element tcPr = tc.element("tcPr");
            if (tcPr != null) {

                if (tcPr.attributeValue("marL") != null) {
                    leftMargin = (int) (Integer.parseInt(tcPr.attributeValue("marL")) * KeyKt.PIXEL_DPI / KeyKt.EMU_PER_INCH * KeyKt.PIXEL_TO_TWIPS);
                }

                if (tcPr.attributeValue("marT") != null) {
                    topMargin = (int) (Integer.parseInt(tcPr.attributeValue("marT")) * KeyKt.PIXEL_DPI / KeyKt.EMU_PER_INCH * KeyKt.PIXEL_TO_TWIPS);
                }

                if (tcPr.attributeValue("marR") != null) {
                    rightMargin = (int) (Integer.parseInt(tcPr.attributeValue("marR")) * KeyKt.PIXEL_DPI / KeyKt.EMU_PER_INCH * KeyKt.PIXEL_TO_TWIPS);
                }

                if (tcPr.attributeValue("marB") != null) {
                    bottomMargin = (int) (Integer.parseInt(tcPr.attributeValue("marB")) * KeyKt.PIXEL_DPI / KeyKt.EMU_PER_INCH * KeyKt.PIXEL_TO_TWIPS);
                }

                AttrManage.instance().setPageMarginTop(attr, topMargin);

                AttrManage.instance().setPageMarginBottom(attr, bottomMargin);

                AttrManage.instance().setPageMarginLeft(attr, leftMargin);

                AttrManage.instance().setPageMarginRight(attr, rightMargin);

                if ((val = tcPr.attributeValue("anchor")) != null) {
                    if (val.equals("t")) {
                        verAlign = WPAttrConstant.PAGE_V_TOP;
                    } else if (val.equals("ctr")) {
                        verAlign = WPAttrConstant.PAGE_V_CENTER;
                    } else if (val.equals("b")) {
                        verAlign = WPAttrConstant.PAGE_V_BOTTOM;
                    } else if (val.equals("just")) {
                        verAlign = WPAttrConstant.PAGE_V_CENTER;
                    } else if (val.equals("dist")) {
                        verAlign = WPAttrConstant.PAGE_V_CENTER;
                    }
                    AttrManage.instance().setPageVerticalAlign(attr, verAlign);
                }

                if ((val = tcPr.attributeValue("anchorCtr")) != null) {
                    if (val.equals("1")) {
                        AttrManage.instance().setPageHorizontalAlign(attr, WPAttrConstant.PAGE_H_CENTER);
                    }
                }
            }

            Element wrap = temp.element("bodyPr");
            if (wrap != null) {

                String value = wrap.attributeValue("wrap");
                tb.setWrapLine(value == null || "square".equalsIgnoreCase(value));
            }

            int offset = processParagraph(control, master, secElem, temp, cellStyle);
            secElem.setEndOffset(offset);
        }
    }

    public int processParagraph(IControl control, PGMaster master, SectionElement secElem, Element txBody, TableCellStyle cellStyle) {
        int offset = 0;
        int lnSpcReduction = 0;
        Element bodyPr = txBody.element("bodyPr");
        if (bodyPr != null) {
            Element normAutofit = bodyPr.element("normAutofit");
            if (normAutofit != null && normAutofit.attribute("lnSpcReduction") != null) {
                String val = normAutofit.attributeValue("lnSpcReduction");
                if (val != null && val.length() > 0) {
                    lnSpcReduction = Integer.parseInt(val);
                }
            }
        }
        List<Element> ps = txBody.elements("p");
        for (int i = 0; i < ps.size(); i++) {
            Element p = ps.get(i);
            ParagraphElement paraElem = new ParagraphElement();
            paraElem.setStartOffset(offset);
            ParaAttr.instance().setParaAttribute(control, p.element("pPr"), paraElem.getAttribute(), null,
                    -1, -1, lnSpcReduction, true, false);
            IAttributeSet attrLayout = null;
            if (cellStyle != null) {
                attrLayout = cellStyle.getFontAttributeSet();
            }
            offset = RunAttr.instance().processRun(master, paraElem, p, attrLayout, offset, 100, -1);

            ParaAttr.instance().processParaWithPct(p.element("pPr"), paraElem.getAttribute());

            if (i == 0) {
                AttrManage.instance().setParaBefore(paraElem.getAttribute(), 0);
            } else if (i == ps.size() - 1) {
                AttrManage.instance().setParaAfter(paraElem.getAttribute(), 0);
            }

            paraElem.setEndOffset(offset);
            secElem.appendParagraph(paraElem, WPModelConstant.MAIN);
        }
        return offset;
    }
}
