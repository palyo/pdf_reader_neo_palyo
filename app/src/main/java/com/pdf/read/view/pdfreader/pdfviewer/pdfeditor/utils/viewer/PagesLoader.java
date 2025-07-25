package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.utils.viewer;

import android.graphics.RectF;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.utils.viewer.util.Constants;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.utils.viewer.util.Constants.Cache;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.utils.viewer.util.MathUtils;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.utils.viewer.util.Util;
import com.shockwave.pdfium.util.SizeF;

import java.util.LinkedList;
import java.util.List;

class PagesLoader {

    private PDFView pdfView;
    private int cacheOrder;
    private float xOffset;
    private float yOffset;
    private float pageRelativePartWidth;
    private float pageRelativePartHeight;
    private float partRenderWidth;
    private float partRenderHeight;
    private final RectF thumbnailRect = new RectF(0, 0, 1, 1);
    private final int preloadOffset;

    private class Holder {
        int row;
        int col;

        @Override
        public String toString() {
            return "Holder{" + "row=" + row + ", col=" + col + '}';
        }
    }

    private class RenderRange {
        int page;
        GridSize gridSize;
        Holder leftTop;
        Holder rightBottom;

        RenderRange() {
            this.page = 0;
            this.gridSize = new GridSize();
            this.leftTop = new Holder();
            this.rightBottom = new Holder();
        }

        @Override
        public String toString() {
            return "RenderRange{" + "page=" + page + ", gridSize=" + gridSize + ", leftTop=" + leftTop + ", rightBottom=" + rightBottom + '}';
        }
    }

    private class GridSize {
        int rows;
        int cols;

        @Override
        public String toString() {
            return "GridSize{" + "rows=" + rows + ", cols=" + cols + '}';
        }
    }

    PagesLoader(PDFView pdfView) {
        this.pdfView = pdfView;
        this.preloadOffset = Util.getDP(pdfView.getContext(), Constants.PRELOAD_OFFSET);
    }

    private void getPageColsRows(GridSize grid, int pageIndex) {
        SizeF size = pdfView.getPdfFile().getPageSize(pageIndex);
        float ratioX = 1f / size.getWidth();
        float ratioY = 1f / size.getHeight();
        final float partHeight = (Constants.PART_SIZE * ratioY) / pdfView.getZoom();
        final float partWidth = (Constants.PART_SIZE * ratioX) / pdfView.getZoom();
        grid.rows = MathUtils.ceil(1f / partHeight);
        grid.cols = MathUtils.ceil(1f / partWidth);
    }

    private void calculatePartSize(GridSize grid) {
        pageRelativePartWidth = 1f / (float) grid.cols;
        pageRelativePartHeight = 1f / (float) grid.rows;
        partRenderWidth = Constants.PART_SIZE / pageRelativePartWidth;
        partRenderHeight = Constants.PART_SIZE / pageRelativePartHeight;
    }

    private List<RenderRange> getRenderRangeList(float firstXOffset, float firstYOffset, float lastXOffset, float lastYOffset) {

        float fixedFirstXOffset = -MathUtils.max(firstXOffset, 0);
        float fixedFirstYOffset = -MathUtils.max(firstYOffset, 0);

        float fixedLastXOffset = -MathUtils.max(lastXOffset, 0);
        float fixedLastYOffset = -MathUtils.max(lastYOffset, 0);

        float offsetFirst = pdfView.isSwipeVertical() ? fixedFirstYOffset : fixedFirstXOffset;
        float offsetLast = pdfView.isSwipeVertical() ? fixedLastYOffset : fixedLastXOffset;

        int firstPage = pdfView.getPdfFile().getPageAtOffset(offsetFirst, pdfView.getZoom());
        int lastPage = pdfView.getPdfFile().getPageAtOffset(offsetLast, pdfView.getZoom());
        int pageCount = lastPage - firstPage + 1;

        List<RenderRange> renderRanges = new LinkedList<>();

        for (int page = firstPage; page <= lastPage; page++) {
            RenderRange range = new RenderRange();
            range.page = page;

            float pageFirstXOffset, pageFirstYOffset, pageLastXOffset, pageLastYOffset;
            if (page == firstPage) {
                pageFirstXOffset = fixedFirstXOffset;
                pageFirstYOffset = fixedFirstYOffset;
                if (pageCount == 1) {
                    pageLastXOffset = fixedLastXOffset;
                    pageLastYOffset = fixedLastYOffset;
                } else {
                    float pageOffset = pdfView.getPdfFile().getPageOffsetWithZoom(page, pdfView.getZoom());
                    SizeF pageSize = pdfView.getPdfFile().getScaledPageSize(page, pdfView.getZoom());
                    if (pdfView.isSwipeVertical()) {
                        pageLastXOffset = fixedLastXOffset;
                        pageLastYOffset = pageOffset + pageSize.getHeight();
                    } else {
                        pageLastYOffset = fixedLastYOffset;
                        pageLastXOffset = pageOffset + pageSize.getWidth();
                    }
                }
            } else if (page == lastPage) {
                float pageOffset = pdfView.getPdfFile().getPageOffsetWithZoom(page, pdfView.getZoom());

                if (pdfView.isSwipeVertical()) {
                    pageFirstXOffset = fixedFirstXOffset;
                    pageFirstYOffset = pageOffset;
                } else {
                    pageFirstYOffset = fixedFirstYOffset;
                    pageFirstXOffset = pageOffset;
                }

                pageLastXOffset = fixedLastXOffset;
                pageLastYOffset = fixedLastYOffset;

            } else {
                float pageOffset = pdfView.getPdfFile().getPageOffsetWithZoom(page, pdfView.getZoom());
                SizeF pageSize = pdfView.getPdfFile().getScaledPageSize(page, pdfView.getZoom());
                if (pdfView.isSwipeVertical()) {
                    pageFirstXOffset = fixedFirstXOffset;
                    pageFirstYOffset = pageOffset;

                    pageLastXOffset = fixedLastXOffset;
                    pageLastYOffset = pageOffset + pageSize.getHeight();
                } else {
                    pageFirstXOffset = pageOffset;
                    pageFirstYOffset = fixedFirstYOffset;

                    pageLastXOffset = pageOffset + pageSize.getWidth();
                    pageLastYOffset = fixedLastYOffset;
                }
            }

            getPageColsRows(range.gridSize, range.page); // get the page's grid size that rows and cols
            SizeF scaledPageSize = pdfView.getPdfFile().getScaledPageSize(range.page, pdfView.getZoom());
            float rowHeight = scaledPageSize.getHeight() / range.gridSize.rows;
            float colWidth = scaledPageSize.getWidth() / range.gridSize.cols;

            float secondaryOffset = pdfView.getPdfFile().getSecondaryPageOffset(page, pdfView.getZoom());

            // calculate the row,col of the point in the leftTop and rightBottom
            if (pdfView.isSwipeVertical()) {
                range.leftTop.row = MathUtils.floor(Math.abs(pageFirstYOffset - pdfView.getPdfFile().getPageOffsetWithZoom(range.page, pdfView.getZoom())) / rowHeight);
                range.leftTop.col = MathUtils.floor(MathUtils.min(pageFirstXOffset - secondaryOffset, 0) / colWidth);

                range.rightBottom.row = MathUtils.ceil(Math.abs(pageLastYOffset - pdfView.getPdfFile().getPageOffsetWithZoom(range.page, pdfView.getZoom())) / rowHeight);
                range.rightBottom.col = MathUtils.floor(MathUtils.min(pageLastXOffset - secondaryOffset, 0) / colWidth);
            } else {
                range.leftTop.col = MathUtils.floor(Math.abs(pageFirstXOffset - pdfView.getPdfFile().getPageOffsetWithZoom(range.page, pdfView.getZoom())) / colWidth);
                range.leftTop.row = MathUtils.floor(MathUtils.min(pageFirstYOffset - secondaryOffset, 0) / rowHeight);

                range.rightBottom.col = MathUtils.floor(Math.abs(pageLastXOffset - pdfView.getPdfFile().getPageOffsetWithZoom(range.page, pdfView.getZoom())) / colWidth);
                range.rightBottom.row = MathUtils.floor(MathUtils.min(pageLastYOffset - secondaryOffset, 0) / rowHeight);
            }

            renderRanges.add(range);
        }

        return renderRanges;
    }

    private void loadVisible() {
        int parts = 0;
        float scaledPreloadOffset = preloadOffset;
        float firstXOffset = -xOffset + scaledPreloadOffset;
        float lastXOffset = -xOffset - pdfView.getWidth() - scaledPreloadOffset;
        float firstYOffset = -yOffset + scaledPreloadOffset;
        float lastYOffset = -yOffset - pdfView.getHeight() - scaledPreloadOffset;

        List<RenderRange> rangeList = getRenderRangeList(firstXOffset, firstYOffset, lastXOffset, lastYOffset);

        for (RenderRange range : rangeList) {
            loadThumbnail(range.page);
        }

        for (RenderRange range : rangeList) {
            calculatePartSize(range.gridSize);
            parts += loadPage(range.page, range.leftTop.row, range.rightBottom.row, range.leftTop.col, range.rightBottom.col, Cache.CACHE_SIZE - parts);
            if (parts >= Cache.CACHE_SIZE) {
                break;
            }
        }
    }

    private int loadPage(int page, int firstRow, int lastRow, int firstCol, int lastCol, int nbOfPartsLoadable) {
        int loaded = 0;
        for (int row = firstRow; row <= lastRow; row++) {
            for (int col = firstCol; col <= lastCol; col++) {
                if (loadCell(page, row, col, pageRelativePartWidth, pageRelativePartHeight)) {
                    loaded++;
                }
                if (loaded >= nbOfPartsLoadable) {
                    return loaded;
                }
            }
        }
        return loaded;
    }

    private boolean loadCell(int page, int row, int col, float pageRelativePartWidth, float pageRelativePartHeight) {
        float relX = pageRelativePartWidth * col;
        float relY = pageRelativePartHeight * row;
        float relWidth = pageRelativePartWidth;
        float relHeight = pageRelativePartHeight;

        float renderWidth = partRenderWidth;
        float renderHeight = partRenderHeight;
        if (relX + relWidth > 1) {
            relWidth = 1 - relX;
        }
        if (relY + relHeight > 1) {
            relHeight = 1 - relY;
        }
        renderWidth *= relWidth;
        renderHeight *= relHeight;
        RectF pageRelativeBounds = new RectF(relX, relY, relX + relWidth, relY + relHeight);

        if (renderWidth > 0 && renderHeight > 0) {
            if (!pdfView.getCacheManager().upPartIfContained(page, pageRelativeBounds, cacheOrder)) {
                pdfView.getRenderingHandler().addRenderingTask(page, renderWidth, renderHeight, pageRelativeBounds, false, cacheOrder, pdfView.isBestQuality(), pdfView.isAnnotationRendering());
            }

            cacheOrder++;
            return true;
        }
        return false;
    }

    private void loadThumbnail(int page) {
        SizeF pageSize = pdfView.getPdfFile().getPageSize(page);
        float thumbnailWidth = pageSize.getWidth() * Constants.THUMBNAIL_RATIO;
        float thumbnailHeight = pageSize.getHeight() * Constants.THUMBNAIL_RATIO;
        if (!pdfView.getCacheManager().containsThumbnail(page, thumbnailRect)) {
            pdfView.getRenderingHandler().addRenderingTask(page, thumbnailWidth, thumbnailHeight, thumbnailRect, true, 0, pdfView.isBestQuality(), pdfView.isAnnotationRendering());
        }
    }

    void loadPages() {
        cacheOrder = 1;
        xOffset = -MathUtils.max(pdfView.getCurrentXOffset(), 0);
        yOffset = -MathUtils.max(pdfView.getCurrentYOffset(), 0);

        loadVisible();
    }
}
