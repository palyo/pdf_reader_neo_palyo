package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.usermodel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.ddf.EscherContainerRecord;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.formula.ptg.Area3DPtg;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.formula.ptg.AreaPtgBase;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.formula.ptg.Ptg;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.record.BOFRecord;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.record.DimensionsRecord;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.record.EOFRecord;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.record.FooterRecord;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.record.HCenterRecord;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.record.HeaderRecord;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.record.PrintSetupRecord;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.record.ProtectRecord;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.record.Record;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.record.RecordBase;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.record.SCLRecord;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.record.UnknownRecord;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.record.VCenterRecord;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.record.chart.AreaFormatRecord;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.record.chart.AxisLineFormatRecord;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.record.chart.AxisOptionsRecord;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.record.chart.AxisParentRecord;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.record.chart.AxisRecord;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.record.chart.AxisUsedRecord;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.record.chart.BarRecord;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.record.chart.BeginRecord;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.record.chart.CategorySeriesAxisRecord;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.record.chart.ChartFormatRecord;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.record.chart.ChartRecord;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.record.chart.ChartTitleFormatRecord;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.record.chart.DataFormatRecord;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.record.chart.DataLabelExtensionRecord;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.record.chart.DefaultDataLabelTextPropertiesRecord;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.record.chart.EndRecord;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.record.chart.FontBasisRecord;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.record.chart.FontIndexRecord;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.record.chart.FrameRecord;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.record.chart.LegendRecord;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.record.chart.LineFormatRecord;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.record.chart.LinkedDataRecord;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.record.chart.ObjectLinkRecord;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.record.chart.PlotAreaRecord;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.record.chart.PlotGrowthRecord;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.record.chart.SeriesIndexRecord;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.record.chart.SeriesRecord;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.record.chart.SeriesTextRecord;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.record.chart.SeriesToChartGroupRecord;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.record.chart.SheetPropertiesRecord;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.record.chart.TextRecord;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.record.chart.TickRecord;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.record.chart.UnitsRecord;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.record.chart.ValueRangeRecord;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.util.CellRangeAddress;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.ss.util.CellRangeAddressBase;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.ss.model.XLSModel.AWorkbook;

public final class HSSFChart extends HSSFSimpleShape {

    public final static short OBJECT_TYPE_CHART = 5;
    private final Map<SeriesTextRecord, Record> chartSeriesText = new HashMap<SeriesTextRecord, Record>();
    private final List<ValueRangeRecord> valueRanges = new ArrayList<ValueRangeRecord>();
    private final List<HSSFSeries> series = new ArrayList<HSSFSeries>();
    private HSSFSheet sheet;
    private ChartRecord chartRecord;
    private LegendRecord legendRecord;
    private ChartTitleFormatRecord chartTitleFormat;
    private AreaFormatRecord seriesBackgroundColorFormat;
    private AreaFormatRecord marginColorFormat;
    private HSSFChartType type = HSSFChartType.Unknown;

    public HSSFChart(AWorkbook workbook, EscherContainerRecord escherContainer, HSSFShape parent, HSSFAnchor anchor) {
        super(escherContainer, parent, anchor);
        if (escherContainer != null && workbook != null) {
            processLineWidth();
            processLine(escherContainer, workbook);
            processSimpleBackground(escherContainer, workbook);
            processRotationAndFlip(escherContainer);
        }

        setShapeType(OBJECT_TYPE_CHART);
    }

    private static int convetRecordsToSeriesByPostion(List<Record> records, HSSFChart chart, int seriesStart) {

        if (seriesStart >= records.size() || records.get(seriesStart).getSid() != SeriesRecord.sid) {
            return -1;
        }
        HSSFSeries series = chart.new HSSFSeries((SeriesRecord) records.get(seriesStart));
        chart.series.add(series);

        int start = seriesStart + 1;
        if (records.get(start) instanceof BeginRecord) {
            int beginReocrdsCount = 1;
            start++;
            while (start <= records.size() && beginReocrdsCount > 0) {
                Record r = records.get(start);
                if (r instanceof LinkedDataRecord) {
                    LinkedDataRecord linkedDataRecord = (LinkedDataRecord) r;
                    if (chart.series.size() > 0) {
                        series = chart.series.get(chart.series.size() - 1);
                        series.insertData(linkedDataRecord);
                    }
                } else if (r instanceof SeriesTextRecord) {
                    SeriesTextRecord str = (SeriesTextRecord) r;
                    if (chart.series.size() > 0) {
                        series = chart.series.get(chart.series.size() - 1);
                        series.seriesTitleText = str;
                    }
                } else if (r.getSid() == AreaFormatRecord.sid) {
                    AreaFormatRecord areaFormatRecord = (AreaFormatRecord) r;

                    series = chart.series.get(chart.series.size() - 1);
                    series.setAreaFormat(areaFormatRecord);

                } else if (r instanceof BeginRecord) {
                    beginReocrdsCount++;
                } else if (r instanceof EndRecord) {
                    beginReocrdsCount--;
                }
                start++;
            }
        }
        return start - 1;
    }

    private static int convetRecordsToText(List<Record> records, HSSFChart chart, int seriesStart) {

        if (seriesStart >= records.size() || records.get(seriesStart).getSid() != TextRecord.sid) {
            return -1;
        }

        TextRecord txtRecord = (TextRecord) records.get(seriesStart);

        SeriesTextRecord str = null;
        ObjectLinkRecord objLinkRecord = null;
        int start = seriesStart + 1;
        if (records.get(start) instanceof BeginRecord) {
            int beginReocrdsCount = 1;
            start++;
            while (start <= records.size() && beginReocrdsCount > 0) {
                Record r = records.get(start);
                if (r instanceof SeriesTextRecord) {
                    str = (SeriesTextRecord) records.get(start);
                } else if (r instanceof ObjectLinkRecord) {
                    objLinkRecord = (ObjectLinkRecord) r;
                } else if (r instanceof BeginRecord) {
                    beginReocrdsCount++;
                } else if (r instanceof EndRecord) {
                    beginReocrdsCount--;
                }
                start++;
            }
        }

        if (txtRecord.getWidth() > 0 && txtRecord.getHeight() > 0
                && objLinkRecord != null && chart.series.size() > 0) {
            if (str != null) {
                chart.chartSeriesText.put(str, objLinkRecord);
            } else if (chart.series.size() > chart.chartSeriesText.size()) {
                chart.chartSeriesText.put(chart.series.get(chart.chartSeriesText.size()).getSeriesTextRecord(), objLinkRecord);
            }

        }

        return start - 1;
    }

    public static void convertRecordsToChart(List<Record> records, HSSFChart chart) {
        if (chart == null || records == null) {
            return;
        }

        int size = records.size();
        int index = 0;
        Record r;
        while (index < size) {
            r = records.get(index);
            if (r instanceof ChartRecord) {
                chart.setChartRecord((ChartRecord) r);
            } else if (r instanceof LegendRecord) {
                chart.legendRecord = (LegendRecord) r;
            } else if (r.getSid() == AreaFormatRecord.sid) {
                if (chart.getSeries().length == 0) {
                    chart.marginColorFormat = (AreaFormatRecord) r;
                } else {
                    chart.seriesBackgroundColorFormat = (AreaFormatRecord) r;
                }

            } else if (r instanceof SeriesRecord) {
                index = convetRecordsToSeriesByPostion(records, chart, index);
            } else if (r instanceof TextRecord) {
                index = convetRecordsToText(records, chart, index);
            } else if (r instanceof DataLabelExtensionRecord) {
                chart.series.get(chart.series.size() - 1).setDataLabelExtensionRecord((DataLabelExtensionRecord) r);
            } else if (r instanceof ChartTitleFormatRecord) {
                chart.chartTitleFormat =
                        (ChartTitleFormatRecord) r;
            } else if (r instanceof ValueRangeRecord) {
                chart.valueRanges.add((ValueRangeRecord) r);
            } else if (r.getSid() == AxisParentRecord.sid) {
            } else if (r instanceof Record) {
                if (chart != null) {
                    Record record = r;
                    for (HSSFChartType type : HSSFChartType.values()) {
                        if (type == HSSFChartType.Unknown) {
                            continue;
                        }
                        if (record.getSid() == type.getSid()) {
                            chart.type = type;
                            break;
                        }
                    }
                }
            }
            index++;
        }
    }

    public static HSSFChart[] getSheetCharts(HSSFSheet sheet) {
        List<HSSFChart> charts = new ArrayList<HSSFChart>();
        HSSFChart lastChart = null;
        HSSFSeries lastSeries = null;
        List<RecordBase> records = sheet.getSheet().getRecords();
        for (RecordBase r : records) {

            if (r instanceof ChartRecord) {
                lastSeries = null;
                lastChart = new HSSFChart(null, null, null, null);
                lastChart.setChartRecord((ChartRecord) r);
                charts.add(lastChart);
            } else if (r instanceof LegendRecord) {
                lastChart.legendRecord = (LegendRecord) r;
            } else if (r instanceof SeriesRecord) {
                HSSFSeries series = lastChart.new HSSFSeries((SeriesRecord) r);
                lastChart.series.add(series);
                lastSeries = series;
            } else if (r instanceof ChartTitleFormatRecord) {
                lastChart.chartTitleFormat =
                        (ChartTitleFormatRecord) r;
            } else if (r instanceof SeriesTextRecord) {
                SeriesTextRecord str = (SeriesTextRecord) r;
                if (lastChart.legendRecord == null &&
                        lastChart.series.size() > 0) {
                    HSSFSeries series = lastChart.series.get(lastChart.series.size() - 1);
                    series.seriesTitleText = str;
                }
            } else if (r instanceof LinkedDataRecord) {
                LinkedDataRecord linkedDataRecord = (LinkedDataRecord) r;
                if (lastSeries != null) {
                    lastSeries.insertData(linkedDataRecord);
                }
            } else if (r instanceof ValueRangeRecord) {
                lastChart.valueRanges.add((ValueRangeRecord) r);
            } else if (r instanceof Record) {
                if (lastChart != null) {
                    Record record = (Record) r;
                    for (HSSFChartType type : HSSFChartType.values()) {
                        if (type == HSSFChartType.Unknown) {
                            continue;
                        }
                        if (record.getSid() == type.getSid()) {
                            lastChart.type = type;
                            break;
                        }
                    }
                }
            }
        }

        return charts.toArray(new HSSFChart[charts.size()]);
    }

    public List<ValueRangeRecord> getValueRangeRecord() {
        return valueRanges;
    }

    public void createBarChart(HSSFWorkbook workbook, HSSFSheet sheet) {

        List<Record> records = new ArrayList<Record>();
        records.add(createMSDrawingObjectRecord());
        records.add(createOBJRecord());
        records.add(createBOFRecord());
        records.add(new HeaderRecord(""));
        records.add(new FooterRecord(""));
        records.add(createHCenterRecord());
        records.add(createVCenterRecord());
        records.add(createPrintSetupRecord());
        records.add(createFontBasisRecord1());
        records.add(createFontBasisRecord2());
        records.add(new ProtectRecord(false));
        records.add(createUnitsRecord());
        records.add(createChartRecord(0, 0, 30434904, 19031616));
        records.add(createBeginRecord());
        records.add(createSCLRecord((short) 1, (short) 1));
        records.add(createPlotGrowthRecord(65536, 65536));
        records.add(createFrameRecord1());
        records.add(createBeginRecord());
        records.add(createLineFormatRecord(true));
        records.add(createAreaFormatRecord1());
        records.add(createEndRecord());
        records.add(createSeriesRecord());
        records.add(createBeginRecord());
        records.add(createTitleLinkedDataRecord());
        records.add(createValuesLinkedDataRecord());
        records.add(createCategoriesLinkedDataRecord());
        records.add(createDataFormatRecord());
        records.add(createSeriesToChartGroupRecord());
        records.add(createEndRecord());
        records.add(createSheetPropsRecord());
        records.add(createDefaultTextRecord(DefaultDataLabelTextPropertiesRecord.CATEGORY_DATA_TYPE_ALL_TEXT_CHARACTERISTIC));
        records.add(createAllTextRecord());
        records.add(createBeginRecord());
        records.add(createFontIndexRecord(5));
        records.add(createDirectLinkRecord());
        records.add(createEndRecord());
        records.add(createDefaultTextRecord((short) 3));
        records.add(createUnknownTextRecord());
        records.add(createBeginRecord());
        records.add(createFontIndexRecord((short) 6));
        records.add(createDirectLinkRecord());
        records.add(createEndRecord());

        records.add(createAxisUsedRecord((short) 1));
        createAxisRecords(records);

        records.add(createEndRecord());
        records.add(createDimensionsRecord());
        records.add(createSeriesIndexRecord(2));
        records.add(createSeriesIndexRecord(1));
        records.add(createSeriesIndexRecord(3));
        records.add(EOFRecord.instance);

        sheet.insertChartRecords(records);
        workbook.insertChartRecord();
    }

    public int getChartX() {
        return chartRecord.getX();
    }

    public void setChartX(int x) {
        chartRecord.setX(x);
    }

    public int getChartY() {
        return chartRecord.getY();
    }

    public void setChartY(int y) {
        chartRecord.setY(y);
    }

    public int getChartWidth() {
        return chartRecord.getWidth();
    }

    public void setChartWidth(int width) {
        chartRecord.setWidth(width);
    }

    public int getChartHeight() {
        return chartRecord.getHeight();
    }

    public void setChartHeight(int height) {
        chartRecord.setHeight(height);
    }

    public ChartRecord getChartRecord() {
        return chartRecord;
    }

    public void setChartRecord(ChartRecord chartRecord) {
        this.chartRecord = chartRecord;
    }

    public LegendRecord getLegendRecord() {
        return legendRecord;
    }

    public ChartTitleFormatRecord getChartTitleFormat() {
        return chartTitleFormat;
    }

    public HSSFSeries[] getSeries() {
        return series.toArray(new HSSFSeries[series.size()]);
    }

    public void setValueRange(int axisIndex, Double minimum, Double maximum, Double majorUnit, Double minorUnit) {
        ValueRangeRecord valueRange = valueRanges.get(axisIndex);
        if (valueRange == null) return;
        if (minimum != null) {
            valueRange.setAutomaticMinimum(minimum.isNaN());
            valueRange.setMinimumAxisValue(minimum);
        }
        if (maximum != null) {
            valueRange.setAutomaticMaximum(maximum.isNaN());
            valueRange.setMaximumAxisValue(maximum);
        }
        if (majorUnit != null) {
            valueRange.setAutomaticMajor(majorUnit.isNaN());
            valueRange.setMajorIncrement(majorUnit);
        }
        if (minorUnit != null) {
            valueRange.setAutomaticMinor(minorUnit.isNaN());
            valueRange.setMinorIncrement(minorUnit);
        }
    }

    public Map<SeriesTextRecord, Record> getSeriesText() {
        return chartSeriesText;
    }

    public AreaFormatRecord getMarginColorFormat() {
        return marginColorFormat;
    }

    public AreaFormatRecord getSeriesBackgroundColorFormat() {
        return seriesBackgroundColorFormat;
    }

    private SeriesIndexRecord createSeriesIndexRecord(int index) {
        SeriesIndexRecord r = new SeriesIndexRecord();
        r.setIndex((short) index);
        return r;
    }

    private DimensionsRecord createDimensionsRecord() {
        DimensionsRecord r = new DimensionsRecord();
        r.setFirstRow(0);
        r.setLastRow(31);
        r.setFirstCol((short) 0);
        r.setLastCol((short) 1);
        return r;
    }

    private HCenterRecord createHCenterRecord() {
        HCenterRecord r = new HCenterRecord();
        r.setHCenter(false);
        return r;
    }

    private VCenterRecord createVCenterRecord() {
        VCenterRecord r = new VCenterRecord();
        r.setVCenter(false);
        return r;
    }

    private PrintSetupRecord createPrintSetupRecord() {
        PrintSetupRecord r = new PrintSetupRecord();
        r.setPaperSize((short) 0);
        r.setScale((short) 18);
        r.setPageStart((short) 1);
        r.setFitWidth((short) 1);
        r.setFitHeight((short) 1);
        r.setLeftToRight(false);
        r.setLandscape(false);
        r.setValidSettings(true);
        r.setNoColor(false);
        r.setDraft(false);
        r.setNotes(false);
        r.setNoOrientation(false);
        r.setUsePage(false);
        r.setHResolution((short) 0);
        r.setVResolution((short) 0);
        r.setHeaderMargin(0.5);
        r.setFooterMargin(0.5);
        r.setCopies((short) 15);
        return r;
    }

    private FontBasisRecord createFontBasisRecord1() {
        FontBasisRecord r = new FontBasisRecord();
        r.setXBasis((short) 9120);
        r.setYBasis((short) 5640);
        r.setHeightBasis((short) 200);
        r.setScale((short) 0);
        r.setIndexToFontTable((short) 5);
        return r;
    }

    private FontBasisRecord createFontBasisRecord2() {
        FontBasisRecord r = createFontBasisRecord1();
        r.setIndexToFontTable((short) 6);
        return r;
    }

    private BOFRecord createBOFRecord() {
        BOFRecord r = new BOFRecord();
        r.setVersion((short) 600);
        r.setType((short) 20);
        r.setBuild((short) 0x1CFE);
        r.setBuildYear((short) 1997);
        r.setHistoryBitMask(0x40C9);
        r.setRequiredVersion(106);
        return r;
    }

    private UnknownRecord createOBJRecord() {
        byte[] data = {
                (byte) 0x15, (byte) 0x00, (byte) 0x12, (byte) 0x00, (byte) 0x05, (byte) 0x00, (byte) 0x02, (byte) 0x00, (byte) 0x11, (byte) 0x60, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0xB8, (byte) 0x03,
                (byte) 0x87, (byte) 0x03, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
        };

        return new UnknownRecord((short) 0x005D, data);
    }

    private UnknownRecord createMSDrawingObjectRecord() {

        byte[] data = {
                (byte) 0x0F, (byte) 0x00, (byte) 0x02, (byte) 0xF0, (byte) 0xC0, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x10, (byte) 0x00, (byte) 0x08, (byte) 0xF0, (byte) 0x08, (byte) 0x00, (byte) 0x00, (byte) 0x00,
                (byte) 0x02, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x02, (byte) 0x04, (byte) 0x00, (byte) 0x00, (byte) 0x0F, (byte) 0x00, (byte) 0x03, (byte) 0xF0, (byte) 0xA8, (byte) 0x00, (byte) 0x00, (byte) 0x00,
                (byte) 0x0F, (byte) 0x00, (byte) 0x04, (byte) 0xF0, (byte) 0x28, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x01, (byte) 0x00, (byte) 0x09, (byte) 0xF0, (byte) 0x10, (byte) 0x00, (byte) 0x00, (byte) 0x00,
                (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
                (byte) 0x02, (byte) 0x00, (byte) 0x0A, (byte) 0xF0, (byte) 0x08, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x04, (byte) 0x00, (byte) 0x00, (byte) 0x05, (byte) 0x00, (byte) 0x00, (byte) 0x00,
                (byte) 0x0F, (byte) 0x00, (byte) 0x04, (byte) 0xF0, (byte) 0x70, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x92, (byte) 0x0C, (byte) 0x0A, (byte) 0xF0, (byte) 0x08, (byte) 0x00, (byte) 0x00, (byte) 0x00,
                (byte) 0x02, (byte) 0x04, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x0A, (byte) 0x00, (byte) 0x00, (byte) 0x93, (byte) 0x00, (byte) 0x0B, (byte) 0xF0, (byte) 0x36, (byte) 0x00, (byte) 0x00, (byte) 0x00,
                (byte) 0x7F, (byte) 0x00, (byte) 0x04, (byte) 0x01, (byte) 0x04, (byte) 0x01, (byte) 0xBF, (byte) 0x00, (byte) 0x08, (byte) 0x00, (byte) 0x08, (byte) 0x00, (byte) 0x81, (byte) 0x01, (byte) 0x4E, (byte) 0x00,
                (byte) 0x00, (byte) 0x08, (byte) 0x83, (byte) 0x01, (byte) 0x4D, (byte) 0x00, (byte) 0x00, (byte) 0x08, (byte) 0xBF, (byte) 0x01, (byte) 0x10, (byte) 0x00, (byte) 0x11, (byte) 0x00, (byte) 0xC0, (byte) 0x01,
                (byte) 0x4D, (byte) 0x00, (byte) 0x00, (byte) 0x08, (byte) 0xFF, (byte) 0x01, (byte) 0x08, (byte) 0x00, (byte) 0x08, (byte) 0x00, (byte) 0x3F, (byte) 0x02, (byte) 0x00, (byte) 0x00, (byte) 0x02, (byte) 0x00,
                (byte) 0xBF, (byte) 0x03, (byte) 0x00, (byte) 0x00, (byte) 0x08, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x10, (byte) 0xF0, (byte) 0x12, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
                (byte) 0x04, (byte) 0x00, (byte) 0xC0, (byte) 0x02, (byte) 0x0A, (byte) 0x00, (byte) 0xF4, (byte) 0x00, (byte) 0x0E, (byte) 0x00, (byte) 0x66, (byte) 0x01, (byte) 0x20, (byte) 0x00, (byte) 0xE9, (byte) 0x00,
                (byte) 0x00, (byte) 0x00, (byte) 0x11, (byte) 0xF0, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00
        };

        return new UnknownRecord((short) 0x00EC, data);
    }

    private void createAxisRecords(List<Record> records) {
        records.add(createAxisParentRecord());
        records.add(createBeginRecord());
        records.add(createAxisRecord(AxisRecord.AXIS_TYPE_CATEGORY_OR_X_AXIS));
        records.add(createBeginRecord());
        records.add(createCategorySeriesAxisRecord());
        records.add(createAxisOptionsRecord());
        records.add(createTickRecord1());
        records.add(createEndRecord());
        records.add(createAxisRecord(AxisRecord.AXIS_TYPE_VALUE_AXIS));
        records.add(createBeginRecord());
        records.add(createValueRangeRecord());
        records.add(createTickRecord2());
        records.add(createAxisLineFormatRecord(AxisLineFormatRecord.AXIS_TYPE_MAJOR_GRID_LINE));
        records.add(createLineFormatRecord(false));
        records.add(createEndRecord());
        records.add(createPlotAreaRecord());
        records.add(createFrameRecord2());
        records.add(createBeginRecord());
        records.add(createLineFormatRecord2());
        records.add(createAreaFormatRecord2());
        records.add(createEndRecord());
        records.add(createChartFormatRecord());
        records.add(createBeginRecord());
        records.add(createBarRecord());
        records.add(createLegendRecord());
        records.add(createBeginRecord());
        records.add(createTextRecord());
        records.add(createBeginRecord());
        records.add(createLinkedDataRecord());
        records.add(createEndRecord());
        records.add(createEndRecord());
        records.add(createEndRecord());
        records.add(createEndRecord());
    }

    private LinkedDataRecord createLinkedDataRecord() {
        LinkedDataRecord r = new LinkedDataRecord();
        r.setLinkType(LinkedDataRecord.LINK_TYPE_TITLE_OR_TEXT);
        r.setReferenceType(LinkedDataRecord.REFERENCE_TYPE_DIRECT);
        r.setCustomNumberFormat(false);
        r.setIndexNumberFmtRecord((short) 0);
        r.setFormulaOfLink(null);
        return r;
    }

    private TextRecord createTextRecord() {
        TextRecord r = new TextRecord();
        r.setHorizontalAlignment(TextRecord.HORIZONTAL_ALIGNMENT_CENTER);
        r.setVerticalAlignment(TextRecord.VERTICAL_ALIGNMENT_CENTER);
        r.setDisplayMode((short) 1);
        r.setRgbColor(0x00000000);
        r.setX(-37);
        r.setY(-60);
        r.setWidth(0);
        r.setHeight(0);
        r.setAutoColor(true);
        r.setShowKey(false);
        r.setShowValue(false);
        r.setVertical(false);
        r.setAutoGeneratedText(true);
        r.setGenerated(true);
        r.setAutoLabelDeleted(false);
        r.setAutoBackground(true);
        r.setRotation((short) 0);
        r.setShowCategoryLabelAsPercentage(false);
        r.setShowValueAsPercentage(false);
        r.setShowBubbleSizes(false);
        r.setShowLabel(false);
        r.setIndexOfColorValue((short) 77);
        r.setDataLabelPlacement((short) 0);
        r.setTextRotation((short) 0);
        return r;
    }

    private LegendRecord createLegendRecord() {
        LegendRecord r = new LegendRecord();
        r.setXAxisUpperLeft(3542);
        r.setYAxisUpperLeft(1566);
        r.setXSize(437);
        r.setYSize(213);
        r.setType(LegendRecord.TYPE_RIGHT);
        r.setSpacing(LegendRecord.SPACING_MEDIUM);
        r.setAutoPosition(true);
        r.setAutoSeries(true);
        r.setAutoXPositioning(true);
        r.setAutoYPositioning(true);
        r.setVertical(true);
        r.setDataTable(false);
        return r;
    }

    private BarRecord createBarRecord() {
        BarRecord r = new BarRecord();
        r.setBarSpace((short) 0);
        r.setCategorySpace((short) 150);
        r.setHorizontal(false);
        r.setStacked(false);
        r.setDisplayAsPercentage(false);
        r.setShadow(false);
        return r;
    }

    private ChartFormatRecord createChartFormatRecord() {
        ChartFormatRecord r = new ChartFormatRecord();
        r.setXPosition(0);
        r.setYPosition(0);
        r.setWidth(0);
        r.setHeight(0);
        r.setVaryDisplayPattern(false);
        return r;
    }

    private PlotAreaRecord createPlotAreaRecord() {
        PlotAreaRecord r = new PlotAreaRecord();
        return r;
    }

    private AxisLineFormatRecord createAxisLineFormatRecord(short format) {
        AxisLineFormatRecord r = new AxisLineFormatRecord();
        r.setAxisType(format);
        return r;
    }

    private ValueRangeRecord createValueRangeRecord() {
        ValueRangeRecord r = new ValueRangeRecord();
        r.setMinimumAxisValue(0.0);
        r.setMaximumAxisValue(0.0);
        r.setMajorIncrement(0);
        r.setMinorIncrement(0);
        r.setCategoryAxisCross(0);
        r.setAutomaticMinimum(true);
        r.setAutomaticMaximum(true);
        r.setAutomaticMajor(true);
        r.setAutomaticMinor(true);
        r.setAutomaticCategoryCrossing(true);
        r.setLogarithmicScale(false);
        r.setValuesInReverse(false);
        r.setCrossCategoryAxisAtMaximum(false);
        r.setReserved(true);
        return r;
    }

    private TickRecord createTickRecord1() {
        TickRecord r = new TickRecord();
        r.setMajorTickType((byte) 2);
        r.setMinorTickType((byte) 0);
        r.setLabelPosition((byte) 3);
        r.setBackground((byte) 1);
        r.setLabelColorRgb(0);
        r.setZero1((short) 0);
        r.setZero2((short) 0);
        r.setZero3((short) 45);
        r.setAutorotate(true);
        r.setAutoTextBackground(true);
        r.setRotation((short) 0);
        r.setAutorotate(true);
        r.setTickColor((short) 77);
        return r;
    }

    private TickRecord createTickRecord2() {
        TickRecord r = createTickRecord1();
        r.setZero3((short) 0);
        return r;
    }

    private AxisOptionsRecord createAxisOptionsRecord() {
        AxisOptionsRecord r = new AxisOptionsRecord();
        r.setMinimumCategory((short) -28644);
        r.setMaximumCategory((short) -28715);
        r.setMajorUnitValue((short) 2);
        r.setMajorUnit((short) 0);
        r.setMinorUnitValue((short) 1);
        r.setMinorUnit((short) 0);
        r.setBaseUnit((short) 0);
        r.setCrossingPoint((short) -28644);
        r.setDefaultMinimum(true);
        r.setDefaultMaximum(true);
        r.setDefaultMajor(true);
        r.setDefaultMinorUnit(true);
        r.setIsDate(true);
        r.setDefaultBase(true);
        r.setDefaultCross(true);
        r.setDefaultDateSettings(true);
        return r;
    }

    private CategorySeriesAxisRecord createCategorySeriesAxisRecord() {
        CategorySeriesAxisRecord r = new CategorySeriesAxisRecord();
        r.setCrossingPoint((short) 1);
        r.setLabelFrequency((short) 1);
        r.setTickMarkFrequency((short) 1);
        r.setValueAxisCrossing(true);
        r.setCrossesFarRight(false);
        r.setReversed(false);
        return r;
    }

    private AxisRecord createAxisRecord(short axisType) {
        AxisRecord r = new AxisRecord();
        r.setAxisType(axisType);
        return r;
    }

    private AxisParentRecord createAxisParentRecord() {
        AxisParentRecord r = new AxisParentRecord();
        r.setAxisType(AxisParentRecord.AXIS_TYPE_MAIN);
        r.setX(479);
        r.setY(221);
        r.setWidth(2995);
        r.setHeight(2902);
        return r;
    }

    private AxisUsedRecord createAxisUsedRecord(short numAxis) {
        AxisUsedRecord r = new AxisUsedRecord();
        r.setNumAxis(numAxis);
        return r;
    }

    private LinkedDataRecord createDirectLinkRecord() {
        LinkedDataRecord r = new LinkedDataRecord();
        r.setLinkType(LinkedDataRecord.LINK_TYPE_TITLE_OR_TEXT);
        r.setReferenceType(LinkedDataRecord.REFERENCE_TYPE_DIRECT);
        r.setCustomNumberFormat(false);
        r.setIndexNumberFmtRecord((short) 0);
        r.setFormulaOfLink(null);
        return r;
    }

    private FontIndexRecord createFontIndexRecord(int index) {
        FontIndexRecord r = new FontIndexRecord();
        r.setFontIndex((short) index);
        return r;
    }

    private TextRecord createAllTextRecord() {
        TextRecord r = new TextRecord();
        r.setHorizontalAlignment(TextRecord.HORIZONTAL_ALIGNMENT_CENTER);
        r.setVerticalAlignment(TextRecord.VERTICAL_ALIGNMENT_CENTER);
        r.setDisplayMode(TextRecord.DISPLAY_MODE_TRANSPARENT);
        r.setRgbColor(0);
        r.setX(-37);
        r.setY(-60);
        r.setWidth(0);
        r.setHeight(0);
        r.setAutoColor(true);
        r.setShowKey(false);
        r.setShowValue(true);
        r.setVertical(false);
        r.setAutoGeneratedText(true);
        r.setGenerated(true);
        r.setAutoLabelDeleted(false);
        r.setAutoBackground(true);
        r.setRotation((short) 0);
        r.setShowCategoryLabelAsPercentage(false);
        r.setShowValueAsPercentage(false);
        r.setShowBubbleSizes(false);
        r.setShowLabel(false);
        r.setIndexOfColorValue((short) 77);
        r.setDataLabelPlacement((short) 0);
        r.setTextRotation((short) 0);
        return r;
    }

    private TextRecord createUnknownTextRecord() {
        TextRecord r = new TextRecord();
        r.setHorizontalAlignment(TextRecord.HORIZONTAL_ALIGNMENT_CENTER);
        r.setVerticalAlignment(TextRecord.VERTICAL_ALIGNMENT_CENTER);
        r.setDisplayMode(TextRecord.DISPLAY_MODE_TRANSPARENT);
        r.setRgbColor(0);
        r.setX(-37);
        r.setY(-60);
        r.setWidth(0);
        r.setHeight(0);
        r.setAutoColor(true);
        r.setShowKey(false);
        r.setShowValue(false);
        r.setVertical(false);
        r.setAutoGeneratedText(true);
        r.setGenerated(true);
        r.setAutoLabelDeleted(false);
        r.setAutoBackground(true);
        r.setRotation((short) 0);
        r.setShowCategoryLabelAsPercentage(false);
        r.setShowValueAsPercentage(false);
        r.setShowBubbleSizes(false);
        r.setShowLabel(false);
        r.setIndexOfColorValue((short) 77);
        r.setDataLabelPlacement((short) 11088);
        r.setTextRotation((short) 0);
        return r;
    }

    private DefaultDataLabelTextPropertiesRecord createDefaultTextRecord(short categoryDataType) {
        DefaultDataLabelTextPropertiesRecord r = new DefaultDataLabelTextPropertiesRecord();
        r.setCategoryDataType(categoryDataType);
        return r;
    }

    private SheetPropertiesRecord createSheetPropsRecord() {
        SheetPropertiesRecord r = new SheetPropertiesRecord();
        r.setChartTypeManuallyFormatted(false);
        r.setPlotVisibleOnly(true);
        r.setDoNotSizeWithWindow(false);
        r.setDefaultPlotDimensions(true);
        r.setAutoPlotArea(false);
        return r;
    }

    private SeriesToChartGroupRecord createSeriesToChartGroupRecord() {
        return new SeriesToChartGroupRecord();
    }

    private DataFormatRecord createDataFormatRecord() {
        DataFormatRecord r = new DataFormatRecord();
        r.setPointNumber((short) -1);
        r.setSeriesIndex((short) 0);
        r.setSeriesNumber((short) 0);
        r.setUseExcel4Colors(false);
        return r;
    }

    private LinkedDataRecord createCategoriesLinkedDataRecord() {
        LinkedDataRecord r = new LinkedDataRecord();
        r.setLinkType(LinkedDataRecord.LINK_TYPE_CATEGORIES);
        r.setReferenceType(LinkedDataRecord.REFERENCE_TYPE_WORKSHEET);
        r.setCustomNumberFormat(false);
        r.setIndexNumberFmtRecord((short) 0);
        Area3DPtg p = new Area3DPtg(0, 31, 1, 1,
                false, false, false, false, 0);
        r.setFormulaOfLink(new Ptg[]{p,});
        return r;
    }

    private LinkedDataRecord createValuesLinkedDataRecord() {
        LinkedDataRecord r = new LinkedDataRecord();
        r.setLinkType(LinkedDataRecord.LINK_TYPE_VALUES);
        r.setReferenceType(LinkedDataRecord.REFERENCE_TYPE_WORKSHEET);
        r.setCustomNumberFormat(false);
        r.setIndexNumberFmtRecord((short) 0);
        Area3DPtg p = new Area3DPtg(0, 31, 0, 0,
                false, false, false, false, 0);
        r.setFormulaOfLink(new Ptg[]{p,});
        return r;
    }

    private LinkedDataRecord createTitleLinkedDataRecord() {
        LinkedDataRecord r = new LinkedDataRecord();
        r.setLinkType(LinkedDataRecord.LINK_TYPE_TITLE_OR_TEXT);
        r.setReferenceType(LinkedDataRecord.REFERENCE_TYPE_DIRECT);
        r.setCustomNumberFormat(false);
        r.setIndexNumberFmtRecord((short) 0);
        r.setFormulaOfLink(null);
        return r;
    }

    private SeriesRecord createSeriesRecord() {
        SeriesRecord r = new SeriesRecord();
        r.setCategoryDataType(SeriesRecord.CATEGORY_DATA_TYPE_NUMERIC);
        r.setValuesDataType(SeriesRecord.VALUES_DATA_TYPE_NUMERIC);
        r.setNumCategories((short) 32);
        r.setNumValues((short) 31);
        r.setBubbleSeriesType(SeriesRecord.BUBBLE_SERIES_TYPE_NUMERIC);
        r.setNumBubbleValues((short) 0);
        return r;
    }

    private EndRecord createEndRecord() {
        return new EndRecord();
    }

    private AreaFormatRecord createAreaFormatRecord1() {
        AreaFormatRecord r = new AreaFormatRecord();
        r.setForegroundColor(16777215);
        r.setBackgroundColor(0);
        r.setPattern((short) 1);
        r.setAutomatic(true);
        r.setInvert(false);
        r.setForecolorIndex((short) 78);
        r.setBackcolorIndex((short) 77);
        return r;
    }

    private AreaFormatRecord createAreaFormatRecord2() {
        AreaFormatRecord r = new AreaFormatRecord();
        r.setForegroundColor(0x00c0c0c0);
        r.setBackgroundColor(0x00000000);
        r.setPattern((short) 1);
        r.setAutomatic(false);
        r.setInvert(false);
        r.setForecolorIndex((short) 22);
        r.setBackcolorIndex((short) 79);
        return r;
    }

    private LineFormatRecord createLineFormatRecord(boolean drawTicks) {
        LineFormatRecord r = new LineFormatRecord();
        r.setLineColor(0);
        r.setLinePattern(LineFormatRecord.LINE_PATTERN_SOLID);
        r.setWeight((short) -1);
        r.setAuto(true);
        r.setDrawTicks(drawTicks);
        r.setColourPaletteIndex((short) 77);
        return r;
    }

    private LineFormatRecord createLineFormatRecord2() {
        LineFormatRecord r = new LineFormatRecord();
        r.setLineColor(0x00808080);
        r.setLinePattern((short) 0);
        r.setWeight((short) 0);
        r.setAuto(false);
        r.setDrawTicks(false);
        r.setUnknown(false);
        r.setColourPaletteIndex((short) 23);
        return r;
    }

    private FrameRecord createFrameRecord1() {
        FrameRecord r = new FrameRecord();
        r.setBorderType(FrameRecord.BORDER_TYPE_REGULAR);
        r.setAutoSize(false);
        r.setAutoPosition(true);
        return r;
    }

    private FrameRecord createFrameRecord2() {
        FrameRecord r = new FrameRecord();
        r.setBorderType(FrameRecord.BORDER_TYPE_REGULAR);
        r.setAutoSize(true);
        r.setAutoPosition(true);
        return r;
    }

    private PlotGrowthRecord createPlotGrowthRecord(int horizScale, int vertScale) {
        PlotGrowthRecord r = new PlotGrowthRecord();
        r.setHorizontalScale(horizScale);
        r.setVerticalScale(vertScale);
        return r;
    }

    private SCLRecord createSCLRecord(short numerator, short denominator) {
        SCLRecord r = new SCLRecord();
        r.setDenominator(denominator);
        r.setNumerator(numerator);
        return r;
    }

    private BeginRecord createBeginRecord() {
        return new BeginRecord();
    }

    private ChartRecord createChartRecord(int x, int y, int width, int height) {
        ChartRecord r = new ChartRecord();
        r.setX(x);
        r.setY(y);
        r.setWidth(width);
        r.setHeight(height);
        return r;
    }

    private UnitsRecord createUnitsRecord() {
        UnitsRecord r = new UnitsRecord();
        r.setUnits((short) 0);
        return r;
    }

    public HSSFSeries createSeries() throws Exception {
        ArrayList<RecordBase> seriesTemplate = new ArrayList<RecordBase>();
        boolean seriesTemplateFilled = false;

        int idx = 0;
        int deep = 0;
        int chartRecordIdx = -1;
        int chartDeep = -1;
        int lastSeriesDeep = -1;
        int endSeriesRecordIdx = -1;
        int seriesIdx = 0;
        final List<RecordBase> records = sheet.getSheet().getRecords();

        for (final RecordBase record : records) {

            idx++;

            if (record instanceof BeginRecord) {
                deep++;
            } else if (record instanceof EndRecord) {
                deep--;

                if (lastSeriesDeep == deep) {
                    lastSeriesDeep = -1;
                    endSeriesRecordIdx = idx;
                    if (!seriesTemplateFilled) {
                        seriesTemplate.add(record);
                        seriesTemplateFilled = true;
                    }
                }

                if (chartDeep == deep) {
                    break;
                }
            }

            if (record instanceof ChartRecord) {
                if (record == chartRecord) {
                    chartRecordIdx = idx;
                    chartDeep = deep;
                }
            } else if (record instanceof SeriesRecord) {
                if (chartRecordIdx != -1) {
                    seriesIdx++;
                    lastSeriesDeep = deep;
                }
            }

            if (lastSeriesDeep != -1 && !seriesTemplateFilled) {
                seriesTemplate.add(record);
            }
        }

        if (endSeriesRecordIdx == -1) {
            return null;
        }

        idx = endSeriesRecordIdx + 1;

        HSSFSeries newSeries = null;

        ArrayList<RecordBase> clonedRecords = new ArrayList<RecordBase>();
        for (final RecordBase record : seriesTemplate) {

            Record newRecord = null;

            if (record instanceof BeginRecord) {
                newRecord = new BeginRecord();
            } else if (record instanceof EndRecord) {
                newRecord = new EndRecord();
            } else if (record instanceof SeriesRecord) {
                SeriesRecord seriesRecord = (SeriesRecord) ((SeriesRecord) record).clone();
                newSeries = new HSSFSeries(seriesRecord);
                newRecord = seriesRecord;
            } else if (record instanceof LinkedDataRecord) {
                LinkedDataRecord linkedDataRecord = (LinkedDataRecord) ((LinkedDataRecord) record).clone();
                if (newSeries != null) {
                    newSeries.insertData(linkedDataRecord);
                }
                newRecord = linkedDataRecord;
            } else if (record instanceof DataFormatRecord) {
                DataFormatRecord dataFormatRecord = (DataFormatRecord) ((DataFormatRecord) record).clone();

                dataFormatRecord.setSeriesIndex((short) seriesIdx);
                dataFormatRecord.setSeriesNumber((short) seriesIdx);

                newRecord = dataFormatRecord;
            } else if (record instanceof SeriesTextRecord) {
                SeriesTextRecord seriesTextRecord = (SeriesTextRecord) ((SeriesTextRecord) record).clone();
                if (newSeries != null) {
                    newSeries.setSeriesTitleText(seriesTextRecord);
                }
                newRecord = seriesTextRecord;
            } else if (record instanceof Record) {
                newRecord = (Record) ((Record) record).clone();
            }

            if (newRecord != null) {
                clonedRecords.add(newRecord);
            }
        }

        if (newSeries == null) {
            return null;
        }

        for (final RecordBase record : clonedRecords) {
            records.add(idx++, record);
        }

        return newSeries;
    }

    public void removeSeries(HSSFSeries series) {
        this.series.remove(series);
    }

    public HSSFChartType getType() {
        return type;
    }

    public enum HSSFChartType {
        Area {
            @Override
            public short getSid() {
                return 0x101A;
            }
        },
        Bar {
            @Override
            public short getSid() {
                return 0x1017;
            }
        },
        Line {
            @Override
            public short getSid() {
                return 0x1018;
            }
        },
        Pie {
            @Override
            public short getSid() {
                return 0x1019;
            }
        },
        Scatter {
            @Override
            public short getSid() {
                return 0x101B;
            }
        },
        Unknown {
            @Override
            public short getSid() {
                return 0;
            }
        };

        public abstract short getSid();
    }

    public class HSSFSeries {
        private final SeriesRecord series;
        private SeriesTextRecord seriesTitleText;
        private LinkedDataRecord dataName;
        private LinkedDataRecord dataValues;
        private LinkedDataRecord dataCategoryLabels;
        private LinkedDataRecord dataSecondaryCategoryLabels;
        private AreaFormatRecord areaFormatRecord;

        private TextRecord textRecord;
        private DataLabelExtensionRecord dleRec;

        HSSFSeries(SeriesRecord series) {
            this.series = series;
        }

        void insertData(LinkedDataRecord data) {
            switch (data.getLinkType()) {
                case 0:
                    dataName = data;
                    break;
                case 1:
                    dataValues = data;
                    break;
                case 2:
                    dataCategoryLabels = data;
                    break;
                case 3:
                    dataSecondaryCategoryLabels = data;
                    break;
            }
        }

        public TextRecord getTextRecord() {
            return textRecord;
        }

        public void setTextRecord(TextRecord textRecord) {
            this.textRecord = textRecord;
        }

        public DataLabelExtensionRecord getDataLabelExtensionRecord() {
            return dleRec;
        }

        public void setDataLabelExtensionRecord(DataLabelExtensionRecord dleRec) {
            this.dleRec = dleRec;
        }

        void setSeriesTitleText(SeriesTextRecord seriesTitleText) {
            this.seriesTitleText = seriesTitleText;
        }

        public short getNumValues() {
            return series.getNumValues();
        }

        public short getValueType() {
            return series.getValuesDataType();
        }

        public String getSeriesTitle() {
            if (seriesTitleText != null) {
                return seriesTitleText.getText();
            }
            return null;
        }

        public void setSeriesTitle(String title) {
            if (seriesTitleText != null) {
                seriesTitleText.setText(title);
            } else {
                throw new IllegalStateException("No series title found to change");
            }
        }

        public SeriesTextRecord getSeriesTextRecord() {
            return seriesTitleText;
        }

        public LinkedDataRecord getDataName() {
            return dataName;
        }

        public LinkedDataRecord getDataValues() {
            return dataValues;
        }

        public LinkedDataRecord getDataCategoryLabels() {
            return dataCategoryLabels;
        }

        public LinkedDataRecord getDataSecondaryCategoryLabels() {
            return dataSecondaryCategoryLabels;
        }

        public AreaFormatRecord getAreaFormat() {
            return areaFormatRecord;
        }

        public void setAreaFormat(AreaFormatRecord areaFormatRecord) {
            this.areaFormatRecord = areaFormatRecord;
        }

        public SeriesRecord getSeries() {
            return series;
        }

        private CellRangeAddressBase getCellRange(LinkedDataRecord linkedDataRecord) {
            if (linkedDataRecord == null) {
                return null;
            }

            int firstRow = 0;
            int lastRow = 0;
            int firstCol = 0;
            int lastCol = 0;

            for (Ptg ptg : linkedDataRecord.getFormulaOfLink()) {
                if (ptg instanceof AreaPtgBase) {
                    AreaPtgBase areaPtg = (AreaPtgBase) ptg;

                    firstRow = areaPtg.getFirstRow();
                    lastRow = areaPtg.getLastRow();

                    firstCol = areaPtg.getFirstColumn();
                    lastCol = areaPtg.getLastColumn();
                }
            }

            return new CellRangeAddress(firstRow, lastRow, firstCol, lastCol);
        }

        public CellRangeAddressBase getValuesCellRange() {
            return getCellRange(dataValues);
        }

        public void setValuesCellRange(CellRangeAddressBase range) {
            Integer count = setVerticalCellRange(dataValues, range);
            if (count == null) {
                return;
            }

            series.setNumValues((short) (int) count);
        }

        public CellRangeAddressBase getCategoryLabelsCellRange() {
            return getCellRange(dataCategoryLabels);
        }

        public void setCategoryLabelsCellRange(CellRangeAddressBase range) {
            Integer count = setVerticalCellRange(dataCategoryLabels, range);
            if (count == null) {
                return;
            }

            series.setNumCategories((short) (int) count);
        }

        private Integer setVerticalCellRange(LinkedDataRecord linkedDataRecord,
                                             CellRangeAddressBase range) {
            if (linkedDataRecord == null) {
                return null;
            }

            List<Ptg> ptgList = new ArrayList<Ptg>();

            int rowCount = (range.getLastRow() - range.getFirstRow()) + 1;
            int colCount = (range.getLastColumn() - range.getFirstColumn()) + 1;

            for (Ptg ptg : linkedDataRecord.getFormulaOfLink()) {
                if (ptg instanceof AreaPtgBase) {
                    AreaPtgBase areaPtg = (AreaPtgBase) ptg;

                    areaPtg.setFirstRow(range.getFirstRow());
                    areaPtg.setLastRow(range.getLastRow());

                    areaPtg.setFirstColumn(range.getFirstColumn());
                    areaPtg.setLastColumn(range.getLastColumn());
                    ptgList.add(areaPtg);
                }
            }

            linkedDataRecord.setFormulaOfLink(ptgList.toArray(new Ptg[ptgList.size()]));

            return rowCount * colCount;
        }
    }
}
