package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.utils.viewer.model;

import android.graphics.RectF;

import com.shockwave.pdfium.PdfDocument;

public class LinkTapEvent {
    private float originalX;
    private float originalY;
    private float documentX;
    private float documentY;
    private RectF mappedLinkRect;
    private PdfDocument.Link link;

    public LinkTapEvent(float originalX, float originalY, float documentX, float documentY, RectF mappedLinkRect, PdfDocument.Link link) {
        this.originalX = originalX;
        this.originalY = originalY;
        this.documentX = documentX;
        this.documentY = documentY;
        this.mappedLinkRect = mappedLinkRect;
        this.link = link;
    }

    public float getOriginalX() {
        return originalX;
    }

    public float getOriginalY() {
        return originalY;
    }

    public float getDocumentX() {
        return documentX;
    }

    public float getDocumentY() {
        return documentY;
    }

    public RectF getMappedLinkRect() {
        return mappedLinkRect;
    }

    public PdfDocument.Link getLink() {
        return link;
    }
}
