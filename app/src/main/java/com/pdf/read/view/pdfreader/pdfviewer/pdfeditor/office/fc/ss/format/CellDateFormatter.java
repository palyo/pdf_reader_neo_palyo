package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.ss.format;

import java.text.AttributedCharacterIterator;
import java.text.CharacterIterator;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Formatter;
import java.util.regex.Matcher;

public class CellDateFormatter extends CellFormatter {
    private static final long EXCEL_EPOCH_TIME;
    private static final Date EXCEL_EPOCH_DATE;
    private static final CellFormatter SIMPLE_DATE = new CellDateFormatter("mm/d/y");

    static {
        Calendar c = Calendar.getInstance();
        c.set(1904, 0, 1, 0, 0, 0);
        EXCEL_EPOCH_DATE = c.getTime();
        EXCEL_EPOCH_TIME = c.getTimeInMillis();
    }

    private final DateFormat dateFmt;
    private boolean amPmUpper;
    private boolean showM;
    private boolean showAmPm;
    private String sFmt;

    public CellDateFormatter(String format) {
        super(format);
        DatePartHandler partHandler = new DatePartHandler();
        StringBuffer descBuf = CellFormatPart.parseFormat(format,
                CellFormatType.DATE, partHandler);
        partHandler.finish(descBuf);
        dateFmt = new SimpleDateFormat(descBuf.toString());
    }

    public void formatValue(StringBuffer toAppendTo, Object value) {
        if (value == null)
            value = 0.0;
        if (value instanceof Number) {
            Number num = (Number) value;
            double v = num.doubleValue();
            if (v == 0.0)
                value = EXCEL_EPOCH_DATE;
            else
                value = new Date((long) (EXCEL_EPOCH_TIME + v));
        }

        AttributedCharacterIterator it = dateFmt.formatToCharacterIterator(
                value);
        boolean doneAm = false;
        boolean doneMillis = false;

        it.first();
        for (char ch = it.first();
             ch != CharacterIterator.DONE;
             ch = it.next()) {
            if (it.getAttribute(DateFormat.Field.MILLISECOND) != null) {
                if (!doneMillis) {
                    Date dateObj = (Date) value;
                    int pos = toAppendTo.length();
                    Formatter formatter = new Formatter(toAppendTo);
                    long msecs = dateObj.getTime() % 1000;
                    formatter.format(LOCALE, sFmt, msecs / 1000.0);
                    toAppendTo.delete(pos, pos + 2);
                    doneMillis = true;
                }
            } else if (it.getAttribute(DateFormat.Field.AM_PM) != null) {
                if (!doneAm) {
                    if (showAmPm) {
                        if (amPmUpper) {
                            toAppendTo.append(Character.toUpperCase(ch));
                            if (showM)
                                toAppendTo.append('M');
                        } else {
                            toAppendTo.append(Character.toLowerCase(ch));
                            if (showM)
                                toAppendTo.append('m');
                        }
                    }
                    doneAm = true;
                }
            } else {
                toAppendTo.append(ch);
            }
        }
    }

    public void simpleValue(StringBuffer toAppendTo, Object value) {
        SIMPLE_DATE.formatValue(toAppendTo, value);
    }

    private class DatePartHandler implements CellFormatPart.PartHandler {
        private int mStart = -1;
        private int mLen;
        private int hStart = -1;
        private int hLen;

        public String handlePart(Matcher m, String part, CellFormatType type,
                                 StringBuffer desc) {

            int pos = desc.length();
            char firstCh = part.charAt(0);
            switch (firstCh) {
                case 's':
                case 'S':
                    if (mStart >= 0) {
                        for (int i = 0; i < mLen; i++)
                            desc.setCharAt(mStart + i, 'm');
                        mStart = -1;
                    }
                    return part.toLowerCase();

                case 'h':
                case 'H':
                    mStart = -1;
                    hStart = pos;
                    hLen = part.length();
                    return part.toLowerCase();

                case 'd':
                case 'D':
                    mStart = -1;
                    if (part.length() <= 2)
                        return part.toLowerCase();
                    else
                        return part.toLowerCase().replace('d', 'E');

                case 'm':
                case 'M':
                    mStart = pos;
                    mLen = part.length();
                    return part.toUpperCase();

                case 'y':
                case 'Y':
                    mStart = -1;
                    if (part.length() == 3)
                        part = "yyyy";
                    return part.toLowerCase();

                case '0':
                    mStart = -1;
                    int sLen = part.length();
                    sFmt = "%0" + (sLen + 2) + "." + sLen + "f";
                    return part.replace('0', 'S');

                case 'a':
                case 'A':
                case 'p':
                case 'P':
                    if (part.length() > 1) {

                        mStart = -1;
                        showAmPm = true;
                        showM = Character.toLowerCase(part.charAt(1)) == 'm';

                        amPmUpper = showM || Character.isUpperCase(part.charAt(0));

                        return "a";
                    }

                default:
                    return null;
            }
        }

        public void finish(StringBuffer toAppendTo) {
            if (hStart >= 0 && !showAmPm) {
                for (int i = 0; i < hLen; i++) {
                    toAppendTo.setCharAt(hStart + i, 'H');
                }
            }
        }
    }
}
