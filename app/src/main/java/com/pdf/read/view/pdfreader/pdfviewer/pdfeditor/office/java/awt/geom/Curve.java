package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.java.awt.geom;

import java.util.Vector;

public abstract class Curve {
    public static final int INCREASING = 1;
    public static final int DECREASING = -1;

    public static final int RECT_INTERSECTS = 0x80000000;
    public static final double TMIN = 1E-3;
    protected int direction;

    public Curve(int direction) {
        this.direction = direction;
    }

    public static void insertMove(Vector curves, double x, double y) {
        curves.add(new Order0(x, y));
    }

    public static void insertLine(Vector curves,
                                  double x0, double y0,
                                  double x1, double y1) {
        if (y0 < y1) {
            curves.add(new Order1(x0, y0,
                    x1, y1,
                    INCREASING));
        } else if (y0 > y1) {
            curves.add(new Order1(x1, y1,
                    x0, y0,
                    DECREASING));
        } else {

        }
    }

    public static void insertQuad(Vector curves,
                                  double x0, double y0,
                                  double[] coords) {
        double y1 = coords[3];
        if (y0 > y1) {
            Order2.insert(curves, coords,
                    coords[2], y1,
                    coords[0], coords[1],
                    x0, y0,
                    DECREASING);
        } else if (y0 == y1 && y0 == coords[1]) {

        } else {
            Order2.insert(curves, coords,
                    x0, y0,
                    coords[0], coords[1],
                    coords[2], y1,
                    INCREASING);
        }
    }

    public static void insertCubic(Vector curves,
                                   double x0, double y0,
                                   double[] coords) {
        double y1 = coords[5];
        if (y0 > y1) {
            Order3.insert(curves, coords,
                    coords[4], y1,
                    coords[2], coords[3],
                    coords[0], coords[1],
                    x0, y0,
                    DECREASING);
        } else if (y0 == y1 && y0 == coords[1] && y0 == coords[3]) {

        } else {
            Order3.insert(curves, coords,
                    x0, y0,
                    coords[0], coords[1],
                    coords[2], coords[3],
                    coords[4], y1,
                    INCREASING);
        }
    }

    public static int pointCrossingsForPath(PathIterator pi,
                                            double px, double py) {
        if (pi.isDone()) {
            return 0;
        }
        double[] coords = new double[6];
        if (pi.currentSegment(coords) != PathIterator.SEG_MOVETO) {
            throw new IllegalPathStateException("missing initial moveto " +
                    "in path definition");
        }
        pi.next();
        double movx = coords[0];
        double movy = coords[1];
        double curx = movx;
        double cury = movy;
        double endx, endy;
        int crossings = 0;
        while (!pi.isDone()) {
            switch (pi.currentSegment(coords)) {
                case PathIterator.SEG_MOVETO:
                    if (cury != movy) {
                        crossings += pointCrossingsForLine(px, py,
                                curx, cury,
                                movx, movy);
                    }
                    movx = curx = coords[0];
                    movy = cury = coords[1];
                    break;
                case PathIterator.SEG_LINETO:
                    endx = coords[0];
                    endy = coords[1];
                    crossings += pointCrossingsForLine(px, py,
                            curx, cury,
                            endx, endy);
                    curx = endx;
                    cury = endy;
                    break;
                case PathIterator.SEG_QUADTO:
                    endx = coords[2];
                    endy = coords[3];
                    crossings += pointCrossingsForQuad(px, py,
                            curx, cury,
                            coords[0], coords[1],
                            endx, endy, 0);
                    curx = endx;
                    cury = endy;
                    break;
                case PathIterator.SEG_CUBICTO:
                    endx = coords[4];
                    endy = coords[5];
                    crossings += pointCrossingsForCubic(px, py,
                            curx, cury,
                            coords[0], coords[1],
                            coords[2], coords[3],
                            endx, endy, 0);
                    curx = endx;
                    cury = endy;
                    break;
                case PathIterator.SEG_CLOSE:
                    if (cury != movy) {
                        crossings += pointCrossingsForLine(px, py,
                                curx, cury,
                                movx, movy);
                    }
                    curx = movx;
                    cury = movy;
                    break;
            }
            pi.next();
        }
        if (cury != movy) {
            crossings += pointCrossingsForLine(px, py,
                    curx, cury,
                    movx, movy);
        }
        return crossings;
    }

    public static int pointCrossingsForLine(double px, double py,
                                            double x0, double y0,
                                            double x1, double y1) {
        if (py < y0 && py < y1) return 0;
        if (py >= y0 && py >= y1) return 0;

        if (px >= x0 && px >= x1) return 0;
        if (px < x0 && px < x1) return (y0 < y1) ? 1 : -1;
        double xintercept = x0 + (py - y0) * (x1 - x0) / (y1 - y0);
        if (px >= xintercept) return 0;
        return (y0 < y1) ? 1 : -1;
    }

    public static int pointCrossingsForQuad(double px, double py,
                                            double x0, double y0,
                                            double xc, double yc,
                                            double x1, double y1, int level) {
        if (py < y0 && py < yc && py < y1) return 0;
        if (py >= y0 && py >= yc && py >= y1) return 0;

        if (px >= x0 && px >= xc && px >= x1) return 0;
        if (px < x0 && px < xc && px < x1) {
            if (py >= y0) {
                if (py < y1) return 1;
            } else {

                if (py >= y1) return -1;
            }

            return 0;
        }

        if (level > 52) return pointCrossingsForLine(px, py, x0, y0, x1, y1);
        double x0c = (x0 + xc) / 2;
        double y0c = (y0 + yc) / 2;
        double xc1 = (xc + x1) / 2;
        double yc1 = (yc + y1) / 2;
        xc = (x0c + xc1) / 2;
        yc = (y0c + yc1) / 2;
        if (Double.isNaN(xc) || Double.isNaN(yc)) {

            return 0;
        }
        return (pointCrossingsForQuad(px, py,
                x0, y0, x0c, y0c, xc, yc,
                level + 1) +
                pointCrossingsForQuad(px, py,
                        xc, yc, xc1, yc1, x1, y1,
                        level + 1));
    }

    public static int pointCrossingsForCubic(double px, double py,
                                             double x0, double y0,
                                             double xc0, double yc0,
                                             double xc1, double yc1,
                                             double x1, double y1, int level) {
        if (py < y0 && py < yc0 && py < yc1 && py < y1) return 0;
        if (py >= y0 && py >= yc0 && py >= yc1 && py >= y1) return 0;

        if (px >= x0 && px >= xc0 && px >= xc1 && px >= x1) return 0;
        if (px < x0 && px < xc0 && px < xc1 && px < x1) {
            if (py >= y0) {
                if (py < y1) return 1;
            } else {

                if (py >= y1) return -1;
            }

            return 0;
        }

        if (level > 52) return pointCrossingsForLine(px, py, x0, y0, x1, y1);
        double xmid = (xc0 + xc1) / 2;
        double ymid = (yc0 + yc1) / 2;
        xc0 = (x0 + xc0) / 2;
        yc0 = (y0 + yc0) / 2;
        xc1 = (xc1 + x1) / 2;
        yc1 = (yc1 + y1) / 2;
        double xc0m = (xc0 + xmid) / 2;
        double yc0m = (yc0 + ymid) / 2;
        double xmc1 = (xmid + xc1) / 2;
        double ymc1 = (ymid + yc1) / 2;
        xmid = (xc0m + xmc1) / 2;
        ymid = (yc0m + ymc1) / 2;
        if (Double.isNaN(xmid) || Double.isNaN(ymid)) {

            return 0;
        }
        return (pointCrossingsForCubic(px, py,
                x0, y0, xc0, yc0,
                xc0m, yc0m, xmid, ymid, level + 1) +
                pointCrossingsForCubic(px, py,
                        xmid, ymid, xmc1, ymc1,
                        xc1, yc1, x1, y1, level + 1));
    }

    public static int rectCrossingsForPath(PathIterator pi,
                                           double rxmin, double rymin,
                                           double rxmax, double rymax) {
        if (rxmax <= rxmin || rymax <= rymin) {
            return 0;
        }
        if (pi.isDone()) {
            return 0;
        }
        double[] coords = new double[6];
        if (pi.currentSegment(coords) != PathIterator.SEG_MOVETO) {
            throw new IllegalPathStateException("missing initial moveto " +
                    "in path definition");
        }
        pi.next();
        double curx, cury, movx, movy, endx, endy;
        curx = movx = coords[0];
        cury = movy = coords[1];
        int crossings = 0;
        while (crossings != RECT_INTERSECTS && !pi.isDone()) {
            switch (pi.currentSegment(coords)) {
                case PathIterator.SEG_MOVETO:
                    if (curx != movx || cury != movy) {
                        crossings = rectCrossingsForLine(crossings,
                                rxmin, rymin,
                                rxmax, rymax,
                                curx, cury,
                                movx, movy);
                    }

                    movx = curx = coords[0];
                    movy = cury = coords[1];
                    break;
                case PathIterator.SEG_LINETO:
                    endx = coords[0];
                    endy = coords[1];
                    crossings = rectCrossingsForLine(crossings,
                            rxmin, rymin,
                            rxmax, rymax,
                            curx, cury,
                            endx, endy);
                    curx = endx;
                    cury = endy;
                    break;
                case PathIterator.SEG_QUADTO:
                    endx = coords[2];
                    endy = coords[3];
                    crossings = rectCrossingsForQuad(crossings,
                            rxmin, rymin,
                            rxmax, rymax,
                            curx, cury,
                            coords[0], coords[1],
                            endx, endy, 0);
                    curx = endx;
                    cury = endy;
                    break;
                case PathIterator.SEG_CUBICTO:
                    endx = coords[4];
                    endy = coords[5];
                    crossings = rectCrossingsForCubic(crossings,
                            rxmin, rymin,
                            rxmax, rymax,
                            curx, cury,
                            coords[0], coords[1],
                            coords[2], coords[3],
                            endx, endy, 0);
                    curx = endx;
                    cury = endy;
                    break;
                case PathIterator.SEG_CLOSE:
                    if (curx != movx || cury != movy) {
                        crossings = rectCrossingsForLine(crossings,
                                rxmin, rymin,
                                rxmax, rymax,
                                curx, cury,
                                movx, movy);
                    }
                    curx = movx;
                    cury = movy;

                    break;
            }
            pi.next();
        }
        if (crossings != RECT_INTERSECTS && (curx != movx || cury != movy)) {
            crossings = rectCrossingsForLine(crossings,
                    rxmin, rymin,
                    rxmax, rymax,
                    curx, cury,
                    movx, movy);
        }

        return crossings;
    }

    public static int rectCrossingsForLine(int crossings,
                                           double rxmin, double rymin,
                                           double rxmax, double rymax,
                                           double x0, double y0,
                                           double x1, double y1) {
        if (y0 >= rymax && y1 >= rymax) return crossings;
        if (y0 <= rymin && y1 <= rymin) return crossings;
        if (x0 <= rxmin && x1 <= rxmin) return crossings;
        if (x0 >= rxmax && x1 >= rxmax) {

            if (y0 < y1) {

                if (y0 <= rymin) crossings++;
                if (y1 >= rymax) crossings++;
            } else if (y1 < y0) {

                if (y1 <= rymin) crossings--;
                if (y0 >= rymax) crossings--;
            }
            return crossings;
        }

        if ((x0 > rxmin && x0 < rxmax && y0 > rymin && y0 < rymax) ||
                (x1 > rxmin && x1 < rxmax && y1 > rymin && y1 < rymax)) {
            return RECT_INTERSECTS;
        }

        double xi0 = x0;
        if (y0 < rymin) {
            xi0 += ((rymin - y0) * (x1 - x0) / (y1 - y0));
        } else if (y0 > rymax) {
            xi0 += ((rymax - y0) * (x1 - x0) / (y1 - y0));
        }
        double xi1 = x1;
        if (y1 < rymin) {
            xi1 += ((rymin - y1) * (x0 - x1) / (y0 - y1));
        } else if (y1 > rymax) {
            xi1 += ((rymax - y1) * (x0 - x1) / (y0 - y1));
        }
        if (xi0 <= rxmin && xi1 <= rxmin) return crossings;
        if (xi0 >= rxmax && xi1 >= rxmax) {
            if (y0 < y1) {

                if (y0 <= rymin) crossings++;
                if (y1 >= rymax) crossings++;
            } else if (y1 < y0) {

                if (y1 <= rymin) crossings--;
                if (y0 >= rymax) crossings--;
            }
            return crossings;
        }
        return RECT_INTERSECTS;
    }

    public static int rectCrossingsForQuad(int crossings,
                                           double rxmin, double rymin,
                                           double rxmax, double rymax,
                                           double x0, double y0,
                                           double xc, double yc,
                                           double x1, double y1,
                                           int level) {
        if (y0 >= rymax && yc >= rymax && y1 >= rymax) return crossings;
        if (y0 <= rymin && yc <= rymin && y1 <= rymin) return crossings;
        if (x0 <= rxmin && xc <= rxmin && x1 <= rxmin) return crossings;
        if (x0 >= rxmax && xc >= rxmax && x1 >= rxmax) {

            if (y0 < y1) {

                if (y0 <= rymin && y1 > rymin) crossings++;
                if (y0 < rymax && y1 >= rymax) crossings++;
            } else if (y1 < y0) {

                if (y1 <= rymin && y0 > rymin) crossings--;
                if (y1 < rymax && y0 >= rymax) crossings--;
            }
            return crossings;
        }

        if ((x0 < rxmax && x0 > rxmin && y0 < rymax && y0 > rymin) ||
                (x1 < rxmax && x1 > rxmin && y1 < rymax && y1 > rymin)) {
            return RECT_INTERSECTS;
        }

        if (level > 52) {
            return rectCrossingsForLine(crossings,
                    rxmin, rymin, rxmax, rymax,
                    x0, y0, x1, y1);
        }
        double x0c = (x0 + xc) / 2;
        double y0c = (y0 + yc) / 2;
        double xc1 = (xc + x1) / 2;
        double yc1 = (yc + y1) / 2;
        xc = (x0c + xc1) / 2;
        yc = (y0c + yc1) / 2;
        if (Double.isNaN(xc) || Double.isNaN(yc)) {

            return 0;
        }
        crossings = rectCrossingsForQuad(crossings,
                rxmin, rymin, rxmax, rymax,
                x0, y0, x0c, y0c, xc, yc,
                level + 1);
        if (crossings != RECT_INTERSECTS) {
            crossings = rectCrossingsForQuad(crossings,
                    rxmin, rymin, rxmax, rymax,
                    xc, yc, xc1, yc1, x1, y1,
                    level + 1);
        }
        return crossings;
    }

    public static int rectCrossingsForCubic(int crossings,
                                            double rxmin, double rymin,
                                            double rxmax, double rymax,
                                            double x0, double y0,
                                            double xc0, double yc0,
                                            double xc1, double yc1,
                                            double x1, double y1,
                                            int level) {
        if (y0 >= rymax && yc0 >= rymax && yc1 >= rymax && y1 >= rymax) {
            return crossings;
        }
        if (y0 <= rymin && yc0 <= rymin && yc1 <= rymin && y1 <= rymin) {
            return crossings;
        }
        if (x0 <= rxmin && xc0 <= rxmin && xc1 <= rxmin && x1 <= rxmin) {
            return crossings;
        }
        if (x0 >= rxmax && xc0 >= rxmax && xc1 >= rxmax && x1 >= rxmax) {

            if (y0 < y1) {

                if (y0 <= rymin && y1 > rymin) crossings++;
                if (y0 < rymax && y1 >= rymax) crossings++;
            } else if (y1 < y0) {

                if (y1 <= rymin && y0 > rymin) crossings--;
                if (y1 < rymax && y0 >= rymax) crossings--;
            }
            return crossings;
        }

        if ((x0 > rxmin && x0 < rxmax && y0 > rymin && y0 < rymax) ||
                (x1 > rxmin && x1 < rxmax && y1 > rymin && y1 < rymax)) {
            return RECT_INTERSECTS;
        }

        if (level > 52) {
            return rectCrossingsForLine(crossings,
                    rxmin, rymin, rxmax, rymax,
                    x0, y0, x1, y1);
        }
        double xmid = (xc0 + xc1) / 2;
        double ymid = (yc0 + yc1) / 2;
        xc0 = (x0 + xc0) / 2;
        yc0 = (y0 + yc0) / 2;
        xc1 = (xc1 + x1) / 2;
        yc1 = (yc1 + y1) / 2;
        double xc0m = (xc0 + xmid) / 2;
        double yc0m = (yc0 + ymid) / 2;
        double xmc1 = (xmid + xc1) / 2;
        double ymc1 = (ymid + yc1) / 2;
        xmid = (xc0m + xmc1) / 2;
        ymid = (yc0m + ymc1) / 2;
        if (Double.isNaN(xmid) || Double.isNaN(ymid)) {

            return 0;
        }
        crossings = rectCrossingsForCubic(crossings,
                rxmin, rymin, rxmax, rymax,
                x0, y0, xc0, yc0,
                xc0m, yc0m, xmid, ymid, level + 1);
        if (crossings != RECT_INTERSECTS) {
            crossings = rectCrossingsForCubic(crossings,
                    rxmin, rymin, rxmax, rymax,
                    xmid, ymid, xmc1, ymc1,
                    xc1, yc1, x1, y1, level + 1);
        }
        return crossings;
    }

    public static double round(double v) {

        return v;
    }

    public static int orderof(double x1, double x2) {
        if (x1 < x2) {
            return -1;
        }
        if (x1 > x2) {
            return 1;
        }
        return 0;
    }

    public static long signeddiffbits(double y1, double y2) {
        return (Double.doubleToLongBits(y1) - Double.doubleToLongBits(y2));
    }

    public static long diffbits(double y1, double y2) {
        return Math.abs(Double.doubleToLongBits(y1) -
                Double.doubleToLongBits(y2));
    }

    public static double prev(double v) {
        return Double.longBitsToDouble(Double.doubleToLongBits(v) - 1);
    }

    public static double next(double v) {
        return Double.longBitsToDouble(Double.doubleToLongBits(v) + 1);
    }

    public final int getDirection() {
        return direction;
    }

    public final Curve getWithDirection(int direction) {
        return (this.direction == direction ? this : getReversedCurve());
    }

    public String toString() {
        return ("Curve[" +
                getOrder() + ", " +
                ("(" + round(getX0()) + ", " + round(getY0()) + "), ") +
                controlPointString() +
                ("(" + round(getX1()) + ", " + round(getY1()) + "), ") +
                (direction == INCREASING ? "D" : "U") +
                "]");
    }

    public String controlPointString() {
        return "";
    }

    public abstract int getOrder();

    public abstract double getXTop();

    public abstract double getYTop();

    public abstract double getXBot();

    public abstract double getYBot();

    public abstract double getXMin();

    public abstract double getXMax();

    public abstract double getX0();

    public abstract double getY0();

    public abstract double getX1();

    public abstract double getY1();

    public abstract double XforY(double y);

    public abstract double TforY(double y);

    public abstract double XforT(double t);

    public abstract double YforT(double t);

    public abstract double dXforT(double t, int deriv);

    public abstract double dYforT(double t, int deriv);

    public abstract double nextVertical(double t0, double t1);

    public int crossingsFor(double x, double y) {
        if (y >= getYTop() && y < getYBot()) {
            if (x < getXMax() && (x < getXMin() || x < XforY(y))) {
                return 1;
            }
        }
        return 0;
    }

    public boolean accumulateCrossings(Crossings c) {
        double xhi = c.getXHi();
        if (getXMin() >= xhi) {
            return false;
        }
        double xlo = c.getXLo();
        double ylo = c.getYLo();
        double yhi = c.getYHi();
        double y0 = getYTop();
        double y1 = getYBot();
        double tstart, ystart, tend, yend;
        if (y0 < ylo) {
            if (y1 <= ylo) {
                return false;
            }
            ystart = ylo;
            tstart = TforY(ylo);
        } else {
            if (y0 >= yhi) {
                return false;
            }
            ystart = y0;
            tstart = 0;
        }
        if (y1 > yhi) {
            yend = yhi;
            tend = TforY(yhi);
        } else {
            yend = y1;
            tend = 1;
        }
        boolean hitLo = false;
        boolean hitHi = false;
        while (true) {
            double x = XforT(tstart);
            if (x < xhi) {
                if (hitHi || x > xlo) {
                    return true;
                }
                hitLo = true;
            } else {
                if (hitLo) {
                    return true;
                }
                hitHi = true;
            }
            if (tstart >= tend) {
                break;
            }
            tstart = nextVertical(tstart, tend);
        }
        if (hitLo) {
            c.record(ystart, yend, direction);
        }
        return false;
    }

    public abstract void enlarge(Rectangle2D r);

    public Curve getSubCurve(double ystart, double yend) {
        return getSubCurve(ystart, yend, direction);
    }

    public abstract Curve getReversedCurve();

    public abstract Curve getSubCurve(double ystart, double yend, int dir);

    public int compareTo(Curve that, double[] yrange) {

        double y0 = yrange[0];
        double y1 = yrange[1];
        y1 = Math.min(Math.min(y1, this.getYBot()), that.getYBot());
        if (y1 <= yrange[0]) {
            System.err.println("this == " + this);
            System.err.println("that == " + that);
            System.out.println("target range = " + yrange[0] + "=>" + yrange[1]);
            throw new InternalError("backstepping from " + yrange[0] + " to " + y1);
        }
        yrange[1] = y1;
        if (this.getXMax() <= that.getXMin()) {
            if (this.getXMin() == that.getXMax()) {
                return 0;
            }
            return -1;
        }
        if (this.getXMin() >= that.getXMax()) {
            return 1;
        }

        double s0 = this.TforY(y0);
        double ys0 = this.YforT(s0);
        if (ys0 < y0) {
            s0 = refineTforY(s0, ys0, y0);
            ys0 = this.YforT(s0);
        }
        double s1 = this.TforY(y1);
        if (this.YforT(s1) < y0) {
            s1 = refineTforY(s1, this.YforT(s1), y0);

        }
        double t0 = that.TforY(y0);
        double yt0 = that.YforT(t0);
        if (yt0 < y0) {
            t0 = that.refineTforY(t0, yt0, y0);
            yt0 = that.YforT(t0);
        }
        double t1 = that.TforY(y1);
        if (that.YforT(t1) < y0) {
            t1 = that.refineTforY(t1, that.YforT(t1), y0);

        }
        double xs0 = this.XforT(s0);
        double xt0 = that.XforT(t0);
        double scale = Math.max(Math.abs(y0), Math.abs(y1));
        double ymin = Math.max(scale * 1E-14, 1E-300);
        if (fairlyClose(xs0, xt0)) {
            double bump = ymin;
            double maxbump = Math.min(ymin * 1E13, (y1 - y0) * .1);
            double y = y0 + bump;
            while (y <= y1) {
                if (fairlyClose(this.XforY(y), that.XforY(y))) {
                    if ((bump *= 2) > maxbump) {
                        bump = maxbump;
                    }
                } else {
                    y -= bump;
                    while (true) {
                        bump /= 2;
                        double newy = y + bump;
                        if (newy <= y) {
                            break;
                        }
                        if (fairlyClose(this.XforY(newy), that.XforY(newy))) {
                            y = newy;
                        }
                    }
                    break;
                }
                y += bump;
            }
            if (y > y0) {
                if (y < y1) {
                    yrange[1] = y;
                }
                return 0;
            }
        }

        if (ymin <= 0) {
            System.out.println("ymin = " + ymin);
        }

        while (s0 < s1 && t0 < t1) {
            double sh = this.nextVertical(s0, s1);
            double xsh = this.XforT(sh);
            double ysh = this.YforT(sh);
            double th = that.nextVertical(t0, t1);
            double xth = that.XforT(th);
            double yth = that.YforT(th);

            try {
                if (findIntersect(that, yrange, ymin, 0, 0,
                        s0, xs0, ys0, sh, xsh, ysh,
                        t0, xt0, yt0, th, xth, yth)) {
                    break;
                }
            } catch (Throwable t) {
                System.err.println("Error: " + t);
                System.err.println("y range was " + yrange[0] + "=>" + yrange[1]);
                System.err.println("s y range is " + ys0 + "=>" + ysh);
                System.err.println("t y range is " + yt0 + "=>" + yth);
                System.err.println("ymin is " + ymin);
                return 0;
            }
            if (ysh < yth) {
                if (ysh > yrange[0]) {
                    if (ysh < yrange[1]) {
                        yrange[1] = ysh;
                    }
                    break;
                }
                s0 = sh;
                xs0 = xsh;
                ys0 = ysh;
            } else {
                if (yth > yrange[0]) {
                    if (yth < yrange[1]) {
                        yrange[1] = yth;
                    }
                    break;
                }
                t0 = th;
                xt0 = xth;
                yt0 = yth;
            }
        }
        double ymid = (yrange[0] + yrange[1]) / 2;

        return orderof(this.XforY(ymid), that.XforY(ymid));
    }

    public boolean findIntersect(Curve that, double[] yrange, double ymin,
                                 int slevel, int tlevel,
                                 double s0, double xs0, double ys0,
                                 double s1, double xs1, double ys1,
                                 double t0, double xt0, double yt0,
                                 double t1, double xt1, double yt1) {

        if (ys0 > yt1 || yt0 > ys1) {
            return false;
        }
        if (Math.min(xs0, xs1) > Math.max(xt0, xt1) ||
                Math.max(xs0, xs1) < Math.min(xt0, xt1)) {
            return false;
        }

        if (s1 - s0 > TMIN) {
            double s = (s0 + s1) / 2;
            double xs = this.XforT(s);
            double ys = this.YforT(s);
            if (s == s0 || s == s1) {
                System.out.println("s0 = " + s0);
                System.out.println("s1 = " + s1);
                throw new InternalError("no s progress!");
            }
            if (t1 - t0 > TMIN) {
                double t = (t0 + t1) / 2;
                double xt = that.XforT(t);
                double yt = that.YforT(t);
                if (t == t0 || t == t1) {
                    System.out.println("t0 = " + t0);
                    System.out.println("t1 = " + t1);
                    throw new InternalError("no t progress!");
                }
                if (ys >= yt0 && yt >= ys0) {
                    if (findIntersect(that, yrange, ymin, slevel + 1, tlevel + 1,
                            s0, xs0, ys0, s, xs, ys,
                            t0, xt0, yt0, t, xt, yt)) {
                        return true;
                    }
                }
                if (ys >= yt) {
                    if (findIntersect(that, yrange, ymin, slevel + 1, tlevel + 1,
                            s0, xs0, ys0, s, xs, ys,
                            t, xt, yt, t1, xt1, yt1)) {
                        return true;
                    }
                }
                if (yt >= ys) {
                    if (findIntersect(that, yrange, ymin, slevel + 1, tlevel + 1,
                            s, xs, ys, s1, xs1, ys1,
                            t0, xt0, yt0, t, xt, yt)) {
                        return true;
                    }
                }
                if (ys1 >= yt && yt1 >= ys) {
                    return findIntersect(that, yrange, ymin, slevel + 1, tlevel + 1,
                            s, xs, ys, s1, xs1, ys1,
                            t, xt, yt, t1, xt1, yt1);
                }
            } else {
                if (ys >= yt0) {
                    if (findIntersect(that, yrange, ymin, slevel + 1, tlevel,
                            s0, xs0, ys0, s, xs, ys,
                            t0, xt0, yt0, t1, xt1, yt1)) {
                        return true;
                    }
                }
                if (yt1 >= ys) {
                    return findIntersect(that, yrange, ymin, slevel + 1, tlevel,
                            s, xs, ys, s1, xs1, ys1,
                            t0, xt0, yt0, t1, xt1, yt1);
                }
            }
        } else if (t1 - t0 > TMIN) {
            double t = (t0 + t1) / 2;
            double xt = that.XforT(t);
            double yt = that.YforT(t);
            if (t == t0 || t == t1) {
                System.out.println("t0 = " + t0);
                System.out.println("t1 = " + t1);
                throw new InternalError("no t progress!");
            }
            if (yt >= ys0) {
                if (findIntersect(that, yrange, ymin, slevel, tlevel + 1,
                        s0, xs0, ys0, s1, xs1, ys1,
                        t0, xt0, yt0, t, xt, yt)) {
                    return true;
                }
            }
            if (ys1 >= yt) {
                return findIntersect(that, yrange, ymin, slevel, tlevel + 1,
                        s0, xs0, ys0, s1, xs1, ys1,
                        t, xt, yt, t1, xt1, yt1);
            }
        } else {

            double xlk = xs1 - xs0;
            double ylk = ys1 - ys0;
            double xnm = xt1 - xt0;
            double ynm = yt1 - yt0;
            double xmk = xt0 - xs0;
            double ymk = yt0 - ys0;
            double det = xnm * ylk - ynm * xlk;
            if (det != 0) {
                double detinv = 1 / det;
                double s = (xnm * ymk - ynm * xmk) * detinv;
                double t = (xlk * ymk - ylk * xmk) * detinv;
                if (s >= 0 && s <= 1 && t >= 0 && t <= 1) {
                    s = s0 + s * (s1 - s0);
                    t = t0 + t * (t1 - t0);
                    if (s < 0 || s > 1 || t < 0 || t > 1) {
                        System.out.println("Uh oh!");
                    }
                    double y = (this.YforT(s) + that.YforT(t)) / 2;
                    if (y <= yrange[1] && y > yrange[0]) {
                        yrange[1] = y;
                        return true;
                    }
                }
            }

        }
        return false;
    }

    public double refineTforY(double t0, double yt0, double y0) {
        double t1 = 1;
        while (true) {
            double th = (t0 + t1) / 2;
            if (th == t0 || th == t1) {
                return t1;
            }
            double y = YforT(th);
            if (y < y0) {
                t0 = th;
                yt0 = y;
            } else if (y > y0) {
                t1 = th;
            } else {
                return t1;
            }
        }
    }

    public boolean fairlyClose(double v1, double v2) {
        return (Math.abs(v1 - v2) <
                Math.max(Math.abs(v1), Math.abs(v2)) * 1E-10);
    }

    public abstract int getSegment(double[] coords);
}
