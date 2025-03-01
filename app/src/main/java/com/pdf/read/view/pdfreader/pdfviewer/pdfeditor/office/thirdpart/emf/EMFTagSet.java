package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.thirdpart.emf;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.thirdpart.emf.data.AbortPath;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.thirdpart.emf.data.AlphaBlend;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.thirdpart.emf.data.AngleArc;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.thirdpart.emf.data.Arc;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.thirdpart.emf.data.ArcTo;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.thirdpart.emf.data.BeginPath;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.thirdpart.emf.data.BitBlt;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.thirdpart.emf.data.Chord;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.thirdpart.emf.data.CloseFigure;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.thirdpart.emf.data.CreateBrushIndirect;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.thirdpart.emf.data.CreateDIBPatternBrushPt;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.thirdpart.emf.data.CreatePen;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.thirdpart.emf.data.DeleteObject;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.thirdpart.emf.data.EMFPolygon;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.thirdpart.emf.data.EMFRectangle;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.thirdpart.emf.data.EOF;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.thirdpart.emf.data.Ellipse;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.thirdpart.emf.data.EndPath;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.thirdpart.emf.data.ExcludeClipRect;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.thirdpart.emf.data.ExtCreateFontIndirectW;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.thirdpart.emf.data.ExtCreatePen;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.thirdpart.emf.data.ExtFloodFill;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.thirdpart.emf.data.ExtSelectClipRgn;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.thirdpart.emf.data.ExtTextOutA;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.thirdpart.emf.data.ExtTextOutW;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.thirdpart.emf.data.FillPath;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.thirdpart.emf.data.FlattenPath;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.thirdpart.emf.data.GDIComment;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.thirdpart.emf.data.GradientFill;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.thirdpart.emf.data.IntersectClipRect;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.thirdpart.emf.data.LineTo;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.thirdpart.emf.data.ModifyWorldTransform;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.thirdpart.emf.data.MoveToEx;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.thirdpart.emf.data.OffsetClipRgn;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.thirdpart.emf.data.Pie;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.thirdpart.emf.data.PolyBezier;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.thirdpart.emf.data.PolyBezier16;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.thirdpart.emf.data.PolyBezierTo;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.thirdpart.emf.data.PolyBezierTo16;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.thirdpart.emf.data.PolyDraw;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.thirdpart.emf.data.PolyDraw16;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.thirdpart.emf.data.PolyPolygon;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.thirdpart.emf.data.PolyPolygon16;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.thirdpart.emf.data.PolyPolyline;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.thirdpart.emf.data.PolyPolyline16;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.thirdpart.emf.data.Polygon16;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.thirdpart.emf.data.Polyline;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.thirdpart.emf.data.Polyline16;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.thirdpart.emf.data.PolylineTo;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.thirdpart.emf.data.PolylineTo16;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.thirdpart.emf.data.RealizePalette;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.thirdpart.emf.data.ResizePalette;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.thirdpart.emf.data.RestoreDC;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.thirdpart.emf.data.RoundRect;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.thirdpart.emf.data.SaveDC;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.thirdpart.emf.data.ScaleViewportExtEx;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.thirdpart.emf.data.ScaleWindowExtEx;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.thirdpart.emf.data.SelectClipPath;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.thirdpart.emf.data.SelectObject;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.thirdpart.emf.data.SelectPalette;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.thirdpart.emf.data.SetArcDirection;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.thirdpart.emf.data.SetBkColor;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.thirdpart.emf.data.SetBkMode;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.thirdpart.emf.data.SetBrushOrgEx;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.thirdpart.emf.data.SetICMMode;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.thirdpart.emf.data.SetMapMode;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.thirdpart.emf.data.SetMapperFlags;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.thirdpart.emf.data.SetMetaRgn;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.thirdpart.emf.data.SetMiterLimit;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.thirdpart.emf.data.SetPixelV;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.thirdpart.emf.data.SetPolyFillMode;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.thirdpart.emf.data.SetROP2;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.thirdpart.emf.data.SetStretchBltMode;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.thirdpart.emf.data.SetTextAlign;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.thirdpart.emf.data.SetTextColor;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.thirdpart.emf.data.SetViewportExtEx;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.thirdpart.emf.data.SetViewportOrgEx;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.thirdpart.emf.data.SetWindowExtEx;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.thirdpart.emf.data.SetWindowOrgEx;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.thirdpart.emf.data.SetWorldTransform;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.thirdpart.emf.data.StretchDIBits;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.thirdpart.emf.data.StrokeAndFillPath;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.thirdpart.emf.data.StrokePath;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.thirdpart.emf.data.WidenPath;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.thirdpart.emf.io.TagSet;

public class EMFTagSet extends TagSet {

    public EMFTagSet(int version) {
        if (version >= 1) {

            addTag(new PolyBezier());
            addTag(new EMFPolygon());
            addTag(new Polyline());
            addTag(new PolyBezierTo());
            addTag(new PolylineTo());
            addTag(new PolyPolyline());
            addTag(new PolyPolygon());
            addTag(new SetWindowExtEx());
            addTag(new SetWindowOrgEx());
            addTag(new SetViewportExtEx());
            addTag(new SetViewportOrgEx());
            addTag(new SetBrushOrgEx());
            addTag(new EOF());
            addTag(new SetPixelV());
            addTag(new SetMapperFlags());
            addTag(new SetMapMode());
            addTag(new SetBkMode());
            addTag(new SetPolyFillMode());
            addTag(new SetROP2());
            addTag(new SetStretchBltMode());
            addTag(new SetTextAlign());

            addTag(new SetTextColor());
            addTag(new SetBkColor());
            addTag(new OffsetClipRgn());
            addTag(new MoveToEx());
            addTag(new SetMetaRgn());
            addTag(new ExcludeClipRect());
            addTag(new IntersectClipRect());
            addTag(new ScaleViewportExtEx());
            addTag(new ScaleWindowExtEx());
            addTag(new SaveDC());
            addTag(new RestoreDC());
            addTag(new SetWorldTransform());
            addTag(new ModifyWorldTransform());
            addTag(new SelectObject());
            addTag(new CreatePen());
            addTag(new CreateBrushIndirect());
            addTag(new DeleteObject());
            addTag(new AngleArc());
            addTag(new Ellipse());
            addTag(new EMFRectangle());
            addTag(new RoundRect());
            addTag(new Arc());
            addTag(new Chord());
            addTag(new Pie());
            addTag(new SelectPalette());

            addTag(new ResizePalette());
            addTag(new RealizePalette());
            addTag(new ExtFloodFill());
            addTag(new LineTo());
            addTag(new ArcTo());
            addTag(new PolyDraw());
            addTag(new SetArcDirection());
            addTag(new SetMiterLimit());
            addTag(new BeginPath());
            addTag(new EndPath());
            addTag(new CloseFigure());
            addTag(new FillPath());
            addTag(new StrokeAndFillPath());
            addTag(new StrokePath());
            addTag(new FlattenPath());
            addTag(new WidenPath());
            addTag(new SelectClipPath());
            addTag(new AbortPath());

            addTag(new GDIComment());

            addTag(new ExtSelectClipRgn());
            addTag(new BitBlt());

            addTag(new StretchDIBits());
            addTag(new ExtCreateFontIndirectW());
            addTag(new ExtTextOutA());
            addTag(new ExtTextOutW());
            addTag(new PolyBezier16());
            addTag(new Polygon16());
            addTag(new Polyline16());
            addTag(new PolyBezierTo16());
            addTag(new PolylineTo16());
            addTag(new PolyPolyline16());
            addTag(new PolyPolygon16());
            addTag(new PolyDraw16());

            addTag(new CreateDIBPatternBrushPt());
            addTag(new ExtCreatePen());

            addTag(new SetICMMode());

            addTag(new AlphaBlend());

            addTag(new GradientFill());

        }
    }
}
