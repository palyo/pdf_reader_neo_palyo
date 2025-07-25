package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.java.awt.geom;

import java.util.NoSuchElementException;

public class FlatteningPathIterator implements PathIterator {
    static final int GROW_SIZE = 24;

    PathIterator src;

    double squareflat;

    int limit;

    double[] hold = new double[14];

    double curx, cury;

    double movx, movy;

    int holdType;

    int holdEnd;

    int holdIndex;

    int[] levels;

    int levelIndex;

    boolean done;

    public FlatteningPathIterator(PathIterator src, double flatness) {
        this(src, flatness, 10);
    }

    public FlatteningPathIterator(PathIterator src, double flatness, int limit) {
        if (flatness < 0.0) {
            throw new IllegalArgumentException("flatness must be >= 0");
        }
        if (limit < 0) {
            throw new IllegalArgumentException("limit must be >= 0");
        }
        this.src = src;
        this.squareflat = flatness * flatness;
        this.limit = limit;
        this.levels = new int[limit + 1];

        next(false);
    }

    public double getFlatness() {
        return Math.sqrt(squareflat);
    }

    public int getRecursionLimit() {
        return limit;
    }

    public int getWindingRule() {
        return src.getWindingRule();
    }

    public boolean isDone() {
        return done;
    }

    void ensureHoldCapacity(int want) {
        if (holdIndex - want < 0) {
            int have = hold.length - holdIndex;
            int newsize = hold.length + GROW_SIZE;
            double[] newhold = new double[newsize];
            System.arraycopy(hold, holdIndex, newhold, holdIndex + GROW_SIZE, have);
            hold = newhold;
            holdIndex += GROW_SIZE;
            holdEnd += GROW_SIZE;
        }
    }

    public void next() {
        next(true);
    }

    private void next(boolean doNext) {
        int level;

        if (holdIndex >= holdEnd) {
            if (doNext) {
                src.next();
            }
            if (src.isDone()) {
                done = true;
                return;
            }
            holdType = src.currentSegment(hold);
            levelIndex = 0;
            levels[0] = 0;
        }

        switch (holdType) {
            case SEG_MOVETO:
            case SEG_LINETO:
                curx = hold[0];
                cury = hold[1];
                if (holdType == SEG_MOVETO) {
                    movx = curx;
                    movy = cury;
                }
                holdIndex = 0;
                holdEnd = 0;
                break;
            case SEG_CLOSE:
                curx = movx;
                cury = movy;
                holdIndex = 0;
                holdEnd = 0;
                break;
            case SEG_QUADTO:
                if (holdIndex >= holdEnd) {

                    holdIndex = hold.length - 6;
                    holdEnd = hold.length - 2;
                    hold[holdIndex] = curx;
                    hold[holdIndex + 1] = cury;
                    hold[holdIndex + 2] = hold[0];
                    hold[holdIndex + 3] = hold[1];
                    hold[holdIndex + 4] = curx = hold[2];
                    hold[holdIndex + 5] = cury = hold[3];
                }

                level = levels[levelIndex];
                while (level < limit) {
                    if (QuadCurve2D.getFlatnessSq(hold, holdIndex) < squareflat) {
                        break;
                    }

                    ensureHoldCapacity(4);
                    QuadCurve2D.subdivide(hold, holdIndex, hold, holdIndex - 4, hold, holdIndex);
                    holdIndex -= 4;

                    level++;
                    levels[levelIndex] = level;
                    levelIndex++;
                    levels[levelIndex] = level;
                }

                holdIndex += 4;
                levelIndex--;
                break;
            case SEG_CUBICTO:
                if (holdIndex >= holdEnd) {

                    holdIndex = hold.length - 8;
                    holdEnd = hold.length - 2;
                    hold[holdIndex] = curx;
                    hold[holdIndex + 1] = cury;
                    hold[holdIndex + 2] = hold[0];
                    hold[holdIndex + 3] = hold[1];
                    hold[holdIndex + 4] = hold[2];
                    hold[holdIndex + 5] = hold[3];
                    hold[holdIndex + 6] = curx = hold[4];
                    hold[holdIndex + 7] = cury = hold[5];
                }

                level = levels[levelIndex];
                while (level < limit) {
                    if (CubicCurve2D.getFlatnessSq(hold, holdIndex) < squareflat) {
                        break;
                    }

                    ensureHoldCapacity(6);
                    CubicCurve2D.subdivide(hold, holdIndex, hold, holdIndex - 6, hold, holdIndex);
                    holdIndex -= 6;

                    level++;
                    levels[levelIndex] = level;
                    levelIndex++;
                    levels[levelIndex] = level;
                }

                holdIndex += 6;
                levelIndex--;
                break;
        }
    }

    public int currentSegment(float[] coords) {
        if (isDone()) {
            throw new NoSuchElementException("flattening iterator out of bounds");
        }
        int type = holdType;
        if (type != SEG_CLOSE) {
            coords[0] = (float) hold[holdIndex];
            coords[1] = (float) hold[holdIndex + 1];
            if (type != SEG_MOVETO) {
                type = SEG_LINETO;
            }
        }
        return type;
    }

    public int currentSegment(double[] coords) {
        if (isDone()) {
            throw new NoSuchElementException("flattening iterator out of bounds");
        }
        int type = holdType;
        if (type != SEG_CLOSE) {
            coords[0] = hold[holdIndex];
            coords[1] = hold[holdIndex + 1];
            if (type != SEG_MOVETO) {
                type = SEG_LINETO;
            }
        }
        return type;
    }
}
