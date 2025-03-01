package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.simpletext.font;

import android.graphics.Paint;
import android.graphics.Typeface;

import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.ext.KeyKt;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.common.PaintKit;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.ss.model.baseModel.Cell;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.ss.model.baseModel.Workbook;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.ss.model.style.CellStyle;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.ss.model.table.SSTableCellStyle;

public class FontKit {

    private static final FontKit fontKit = new FontKit();

    private final BreakIterator lineBreak = BreakIterator.getLineInstance();

    public static FontKit instance() {
        return fontKit;
    }

    public Paint getCellPaint(Cell cell, Workbook wb, SSTableCellStyle tableCellStyle) {

        Paint paint = PaintKit.instance().getPaint();
        paint.setAntiAlias(true);
        CellStyle s = cell.getCellStyle();
        Font font = wb.getFont(s.getFontIndex());
        boolean isbold = font.isBold();
        boolean isitalics = font.isItalic();

        if (isbold && isitalics) {
            paint.setTextSkewX(-0.2f);
            paint.setFakeBoldText(true);
        } else if (isbold) {
            paint.setFakeBoldText(true);
        } else if (isitalics) {
            paint.setTextSkewX(-0.2f);
        }

        if (font.isStrikeline()) {
            paint.setStrikeThruText(true);
        }

        if (font.getUnderline() != Font.U_NONE) {
            paint.setUnderlineText(true);
        }

        paint.setTypeface(Typeface.create(Typeface.SANS_SERIF, Typeface.NORMAL));

        paint.setTextSize((float) (font.getFontSize() * KeyKt.POINT_TO_PIXEL + 0.5f));

        int color = wb.getColor(font.getColorIndex());
        if ((color & 0xFFFFFF) == 0 && tableCellStyle != null) {
            color = tableCellStyle.getFontColor();
        }
        paint.setColor(color);

        return paint;
    }

    public synchronized int findBreakOffset(String text, int pos) {
        lineBreak.setText(text);

        lineBreak.following(pos);
        int newPos = lineBreak.previous();
        return newPos == 0 ? pos : newPos;
    }

    public List<String> breakText(String content, int lineWidth, Paint paint) {
        String[] words = content.split("\\n");
        List<String> textList = new ArrayList<String>();

        int index = 0;
        List<String> item;
        while (index < words.length) {
            item = wrapText(words[index], lineWidth, paint);
            Iterator<String> iter = item.iterator();
            while (iter.hasNext()) {
                textList.add(iter.next());
            }
            index++;
        }

        return textList;
    }

    public List<String> wrapText(String content, int lineWidth, Paint paint) {
        String item = "";
        String restContent = content;
        String[] words = restContent.split(" ");
        List<String> textList = new ArrayList<String>();

        int wordIndex = 0;
        int charIndex = 0;
        char[] chars;
        while (wordIndex < words.length) {
            if (words[wordIndex].length() == 0) {
                words[wordIndex] = " ";
            }
            wordIndex++;
        }

        wordIndex = 0;
        while (wordIndex < words.length) {

            while (paint.measureText(words[wordIndex]) > lineWidth) {
                chars = words[wordIndex].toCharArray();
                charIndex = chars.length;
                item = words[wordIndex].substring(0, charIndex);
                while (charIndex > 0 && paint.measureText(item) > lineWidth) {
                    charIndex--;
                    item = words[wordIndex].substring(0, charIndex);
                }
                textList.add(item);
                words[wordIndex] = words[wordIndex].substring(charIndex);
            }

            item = "";
            while (wordIndex < words.length && paint.measureText(item + words[wordIndex]) <= lineWidth) {
                item += words[wordIndex] + " ";
                wordIndex++;
            }
            textList.add(item.substring(0, item.length() - 1));
        }

        disposeString(words);
        return textList;
    }

    private void disposeString(String[] stringArray) {
        int index = 0;
        while (index < stringArray.length) {
            stringArray[index] = null;
            index++;
        }
        stringArray = null;
    }

}
