package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.java.awt.geom;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.Vector;

public abstract class AreaOp {

    public static final int CTAG_LEFT = 0;
    public static final int CTAG_RIGHT = 1;

    public static final int ETAG_IGNORE = 0;
    public static final int ETAG_ENTER = 1;
    public static final int ETAG_EXIT = -1;

    public static final int RSTAG_INSIDE = 1;
    public static final int RSTAG_OUTSIDE = -1;
    private static final Comparator YXTopComparator = new Comparator() {
        public int compare(Object o1, Object o2) {
            Curve c1 = ((Edge) o1).getCurve();
            Curve c2 = ((Edge) o2).getCurve();
            double v1, v2;
            if ((v1 = c1.getYTop()) == (v2 = c2.getYTop())) {
                if ((v1 = c1.getXTop()) == (v2 = c2.getXTop())) {
                    return 0;
                }
            }
            if (v1 < v2) {
                return -1;
            }
            return 1;
        }
    };
    private static final CurveLink[] EmptyLinkList = new CurveLink[2];
    private static final ChainEnd[] EmptyChainList = new ChainEnd[2];

    private AreaOp() {
    }

    private static void addEdges(Vector edges, Vector curves, int curvetag) {
        Enumeration enum_ = curves.elements();
        while (enum_.hasMoreElements()) {
            Curve c = (Curve) enum_.nextElement();
            if (c.getOrder() > 0) {
                edges.add(new Edge(c, curvetag));
            }
        }
    }

    public static void finalizeSubCurves(Vector subcurves, Vector chains) {
        int numchains = chains.size();
        if (numchains == 0) {
            return;
        }
        if ((numchains & 1) != 0) {
            throw new InternalError("Odd number of chains!");
        }
        ChainEnd[] endlist = new ChainEnd[numchains];
        chains.toArray(endlist);
        for (int i = 1; i < numchains; i += 2) {
            ChainEnd open = endlist[i - 1];
            ChainEnd close = endlist[i];
            CurveLink subcurve = open.linkTo(close);
            if (subcurve != null) {
                subcurves.add(subcurve);
            }
        }
        chains.clear();
    }

    public static void resolveLinks(Vector subcurves,
                                    Vector chains,
                                    Vector links) {
        int numlinks = links.size();
        CurveLink[] linklist;
        if (numlinks == 0) {
            linklist = EmptyLinkList;
        } else {
            if ((numlinks & 1) != 0) {
                throw new InternalError("Odd number of new curves!");
            }
            linklist = new CurveLink[numlinks + 2];
            links.toArray(linklist);
        }
        int numchains = chains.size();
        ChainEnd[] endlist;
        if (numchains == 0) {
            endlist = EmptyChainList;
        } else {
            if ((numchains & 1) != 0) {
                throw new InternalError("Odd number of chains!");
            }
            endlist = new ChainEnd[numchains + 2];
            chains.toArray(endlist);
        }
        int curchain = 0;
        int curlink = 0;
        chains.clear();
        ChainEnd chain = endlist[0];
        ChainEnd nextchain = endlist[1];
        CurveLink link = linklist[0];
        CurveLink nextlink = linklist[1];
        while (chain != null || link != null) {

            boolean connectchains = (link == null);
            boolean connectlinks = (chain == null);

            if (!connectchains && !connectlinks) {

                connectchains = ((curchain & 1) == 0 &&
                        chain.getX() == nextchain.getX());
                connectlinks = ((curlink & 1) == 0 &&
                        link.getX() == nextlink.getX());

                if (!connectchains && !connectlinks) {

                    double cx = chain.getX();
                    double lx = link.getX();
                    connectchains =
                            (nextchain != null && cx < lx &&
                                    obstructs(nextchain.getX(), lx, curchain));
                    connectlinks =
                            (nextlink != null && lx < cx &&
                                    obstructs(nextlink.getX(), cx, curlink));
                }
            }
            if (connectchains) {
                CurveLink subcurve = chain.linkTo(nextchain);
                if (subcurve != null) {
                    subcurves.add(subcurve);
                }
                curchain += 2;
                chain = endlist[curchain];
                nextchain = endlist[curchain + 1];
            }
            if (connectlinks) {
                ChainEnd openend = new ChainEnd(link, null);
                ChainEnd closeend = new ChainEnd(nextlink, openend);
                openend.setOtherEnd(closeend);
                chains.add(openend);
                chains.add(closeend);
                curlink += 2;
                link = linklist[curlink];
                nextlink = linklist[curlink + 1];
            }
            if (!connectchains && !connectlinks) {

                chain.addLink(link);
                chains.add(chain);
                curchain++;
                chain = nextchain;
                nextchain = endlist[curchain + 1];
                curlink++;
                link = nextlink;
                nextlink = linklist[curlink + 1];
            }
        }
        if ((chains.size() & 1) != 0) {
            System.out.println("Odd number of chains!");
        }
    }

    public static boolean obstructs(double v1, double v2, int phase) {
        return (((phase & 1) == 0) ? (v1 <= v2) : (v1 < v2));
    }

    public abstract void newRow();

    public abstract int classify(Edge e);

    public abstract int getState();

    public Vector calculate(Vector left, Vector right) {
        Vector edges = new Vector();
        addEdges(edges, left, AreaOp.CTAG_LEFT);
        addEdges(edges, right, AreaOp.CTAG_RIGHT);
        edges = pruneEdges(edges);
        if (false) {
            System.out.println("result: ");
            int numcurves = edges.size();
            Curve[] curvelist = (Curve[]) edges.toArray(new Curve[numcurves]);
            for (int i = 0; i < numcurves; i++) {
                System.out.println("curvelist[" + i + "] = " + curvelist[i]);
            }
        }
        return edges;
    }

    private Vector pruneEdges(Vector edges) {
        int numedges = edges.size();
        if (numedges < 2) {
            return edges;
        }
        Edge[] edgelist = (Edge[]) edges.toArray(new Edge[numedges]);
        Arrays.sort(edgelist, YXTopComparator);
        if (false) {
            System.out.println("pruning: ");
            for (int i = 0; i < numedges; i++) {
                System.out.println("edgelist[" + i + "] = " + edgelist[i]);
            }
        }
        Edge e;
        int left = 0;
        int right = 0;
        int cur = 0;
        int next = 0;
        double[] yrange = new double[2];
        Vector subcurves = new Vector();
        Vector chains = new Vector();
        Vector links = new Vector();

        while (left < numedges) {
            double y = yrange[0];

            for (cur = next = right - 1; cur >= left; cur--) {
                e = edgelist[cur];
                if (e.getCurve().getYBot() > y) {
                    if (next > cur) {
                        edgelist[next] = e;
                    }
                    next--;
                }
            }
            left = next + 1;

            if (left >= right) {
                if (right >= numedges) {
                    break;
                }
                y = edgelist[right].getCurve().getYTop();
                if (y > yrange[0]) {
                    finalizeSubCurves(subcurves, chains);
                }
                yrange[0] = y;
            }

            while (right < numedges) {
                e = edgelist[right];
                if (e.getCurve().getYTop() > y) {
                    break;
                }
                right++;
            }

            yrange[1] = edgelist[left].getCurve().getYBot();
            if (right < numedges) {
                y = edgelist[right].getCurve().getYTop();
                if (yrange[1] > y) {
                    yrange[1] = y;
                }
            }
            if (false) {
                System.out.println("current line: y = [" +
                        yrange[0] + ", " + yrange[1] + "]");
                for (cur = left; cur < right; cur++) {
                    System.out.println("  " + edgelist[cur]);
                }
            }

            int nexteq = 1;
            for (cur = left; cur < right; cur++) {
                e = edgelist[cur];
                e.setEquivalence(0);
                for (next = cur; next > left; next--) {
                    Edge prevedge = edgelist[next - 1];
                    int ordering = e.compareTo(prevedge, yrange);
                    if (yrange[1] <= yrange[0]) {
                        throw new InternalError("backstepping to " + yrange[1] +
                                " from " + yrange[0]);
                    }
                    if (ordering >= 0) {
                        if (ordering == 0) {

                            int eq = prevedge.getEquivalence();
                            if (eq == 0) {
                                eq = nexteq++;
                                prevedge.setEquivalence(eq);
                            }
                            e.setEquivalence(eq);
                        }
                        break;
                    }
                    edgelist[next] = prevedge;
                }
                edgelist[next] = e;
            }
            if (false) {
                System.out.println("current sorted line: y = [" +
                        yrange[0] + ", " + yrange[1] + "]");
                for (cur = left; cur < right; cur++) {
                    System.out.println("  " + edgelist[cur]);
                }
            }

            newRow();
            double ystart = yrange[0];
            double yend = yrange[1];
            for (cur = left; cur < right; cur++) {
                e = edgelist[cur];
                int etag;
                int eq = e.getEquivalence();
                if (eq != 0) {

                    int origstate = getState();
                    etag = (origstate == AreaOp.RSTAG_INSIDE
                            ? AreaOp.ETAG_EXIT
                            : AreaOp.ETAG_ENTER);
                    Edge activematch = null;
                    Edge longestmatch = e;
                    double furthesty = yend;
                    do {

                        classify(e);
                        if (activematch == null &&
                                e.isActiveFor(ystart, etag)) {
                            activematch = e;
                        }
                        y = e.getCurve().getYBot();
                        if (y > furthesty) {
                            longestmatch = e;
                            furthesty = y;
                        }
                    } while (++cur < right &&
                            (e = edgelist[cur]).getEquivalence() == eq);
                    --cur;
                    if (getState() == origstate) {
                        etag = AreaOp.ETAG_IGNORE;
                    } else {
                        e = (activematch != null ? activematch : longestmatch);
                    }
                } else {
                    etag = classify(e);
                }
                if (etag != AreaOp.ETAG_IGNORE) {
                    e.record(yend, etag);
                    links.add(new CurveLink(e.getCurve(), ystart, yend, etag));
                }
            }

            if (getState() != AreaOp.RSTAG_OUTSIDE) {
                System.out.println("Still inside at end of active edge list!");
                System.out.println("num curves = " + (right - left));
                System.out.println("num links = " + links.size());
                System.out.println("y top = " + yrange[0]);
                if (right < numedges) {
                    System.out.println("y top of next curve = " +
                            edgelist[right].getCurve().getYTop());
                } else {
                    System.out.println("no more curves");
                }
                for (cur = left; cur < right; cur++) {
                    e = edgelist[cur];
                    System.out.println(e);
                    int eq = e.getEquivalence();
                    if (eq != 0) {
                        System.out.println("  was equal to " + eq + "...");
                    }
                }
            }
            if (false) {
                System.out.println("new links:");
                for (int i = 0; i < links.size(); i++) {
                    CurveLink link = (CurveLink) links.elementAt(i);
                    System.out.println("  " + link.getSubCurve());
                }
            }
            resolveLinks(subcurves, chains, links);
            links.clear();

            yrange[0] = yend;
        }
        finalizeSubCurves(subcurves, chains);
        Vector ret = new Vector();
        Enumeration enum_ = subcurves.elements();
        while (enum_.hasMoreElements()) {
            CurveLink link = (CurveLink) enum_.nextElement();
            ret.add(link.getMoveto());
            CurveLink nextlink = link;
            while ((nextlink = nextlink.getNext()) != null) {
                if (!link.absorb(nextlink)) {
                    ret.add(link.getSubCurve());
                    link = nextlink;
                }
            }
            ret.add(link.getSubCurve());
        }
        return ret;
    }

    public static abstract class CAGOp extends AreaOp {
        boolean inLeft;
        boolean inRight;
        boolean inResult;

        public void newRow() {
            inLeft = false;
            inRight = false;
            inResult = false;
        }

        public int classify(Edge e) {
            if (e.getCurveTag() == CTAG_LEFT) {
                inLeft = !inLeft;
            } else {
                inRight = !inRight;
            }
            boolean newClass = newClassification(inLeft, inRight);
            if (inResult == newClass) {
                return ETAG_IGNORE;
            }
            inResult = newClass;
            return (newClass ? ETAG_ENTER : ETAG_EXIT);
        }

        public int getState() {
            return (inResult ? RSTAG_INSIDE : RSTAG_OUTSIDE);
        }

        public abstract boolean newClassification(boolean inLeft,
                                                  boolean inRight);
    }

    public static class AddOp extends CAGOp {
        public boolean newClassification(boolean inLeft, boolean inRight) {
            return (inLeft || inRight);
        }
    }

    public static class SubOp extends CAGOp {
        public boolean newClassification(boolean inLeft, boolean inRight) {
            return (inLeft && !inRight);
        }
    }

    public static class IntOp extends CAGOp {
        public boolean newClassification(boolean inLeft, boolean inRight) {
            return (inLeft && inRight);
        }
    }

    public static class XorOp extends CAGOp {
        public boolean newClassification(boolean inLeft, boolean inRight) {
            return (inLeft != inRight);
        }
    }

    public static class NZWindOp extends AreaOp {
        private int count;

        public void newRow() {
            count = 0;
        }

        public int classify(Edge e) {

            int newCount = count;
            int type = (newCount == 0 ? ETAG_ENTER : ETAG_IGNORE);
            newCount += e.getCurve().getDirection();
            count = newCount;
            return (newCount == 0 ? ETAG_EXIT : type);
        }

        public int getState() {
            return ((count == 0) ? RSTAG_OUTSIDE : RSTAG_INSIDE);
        }
    }

    public static class EOWindOp extends AreaOp {
        private boolean inside;

        public void newRow() {
            inside = false;
        }

        public int classify(Edge e) {

            boolean newInside = !inside;
            inside = newInside;
            return (newInside ? ETAG_ENTER : ETAG_EXIT);
        }

        public int getState() {
            return (inside ? RSTAG_INSIDE : RSTAG_OUTSIDE);
        }
    }
}
